package ru.t1.jwt_assymetric.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.jwt_assymetric.exception.BusinessException;
import ru.t1.jwt_assymetric.exception.ErrorCode;
import ru.t1.jwt_assymetric.role.Role;
import ru.t1.jwt_assymetric.user.User;
import ru.t1.jwt_assymetric.user.UserRepository;

@Service
public class JwtService {
  private static final String TOKEN_TYPE = "token_type";

  private static final String ROLES = "role_list";

  private final PrivateKey privateKey;

  private final PublicKey publicKey;

  private final AccessTokenRepository accessTokenRepository;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserRepository userRepository;

  @Value("${security.jwt.access_token_expiration}") private long accessTokenExpiration;
  @Value("${security.jwt.refresh_token_expiration}") private long refreshTokenExpiration;

  public JwtService(AccessTokenRepository accessTokenRepository,
      RefreshTokenRepository refreshTokenRepository, UserRepository userRepository)
      throws Exception {
    this.privateKey = KeyUtils.loadPrivateKey("keys/local/private_key.pem");
    this.publicKey = KeyUtils.loadPublicKey("keys/public_key.pem");
    this.accessTokenRepository = accessTokenRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }

  public String generateAccessToken(final String userName) {
    final User savedUser = userRepository.findByEmailIgnoreCase(userName).orElseThrow(
        () -> new BusinessException(ErrorCode.USER_NOT_FOUND, userName));
    final Map<String, Object> claims = Map.of(TOKEN_TYPE, "ACCESS_TOKEN", ROLES,
        String.join(
            " | ", savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())));
    final String accessToken = buildToken(userName, claims, this.accessTokenExpiration);
    AccessToken token = accessTokenRepository.findById(userName).orElse(
        AccessToken.builder().id(userName).accessToken(new ArrayList<>()).build());
    token.getAccessToken().add(accessToken);
    accessTokenRepository.save(token);
    return accessToken;
  }

  public String generateRefreshToken(final String userName) {
    final Map<String, Object> claims = Map.of(TOKEN_TYPE, "REFRESH_TOKEN");
    final String refreshToken = buildToken(userName, claims, this.refreshTokenExpiration);
    RefreshToken token = refreshTokenRepository.findById(userName).orElse(
        RefreshToken.builder().id(userName).build());
    token.setRefreshToken(refreshToken);
    refreshTokenRepository.save(token);
    return refreshToken;
  }

  public void dropAllTokens(final String userName) {
    refreshTokenRepository.deleteById(userName);
    accessTokenRepository.deleteById(userName);
  }

  private String buildToken(String userName, Map<String, Object> claims, long expiration) {
    return Jwts.builder()
        .claims(claims)
        .subject(userName)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(this.privateKey)
        .compact();
  }

  public boolean validateToken(final String token, final String expectedUserName) {
    final String userName = extractUserName(token);
    return userName.equals(expectedUserName) && !isTokenExpired(token)
        && !isAccessTokenWithdrown(token, userName);
  }

  private boolean isTokenExpired(String token) {
    return extractClaims(token).getExpiration().before(new Date());
  }

  private boolean isRefreshTokenWithdrown(String refreshToken, String userName) {
    RefreshToken token = this.refreshTokenRepository.findById(userName).orElse(null);
    return token == null || !token.getRefreshToken().equals(refreshToken);
  }

  private boolean isAccessTokenWithdrown(String accessToken, String userName) {
    AccessToken token = this.accessTokenRepository.findById(userName).orElse(null);
    return token == null || !token.getAccessToken().contains(accessToken);
  }

  public String extractUserName(String token) {
    return extractClaims(token).getSubject();
  }

  private Claims extractClaims(String token) {
    try {
      return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();
    } catch (final JwtException ex) {
      throw new RuntimeException("Invalid JWT token", ex);
    }
  }

  public String refreshToken(final String refreshToken) {
    final Claims claims = extractClaims(refreshToken);
    final String userName = claims.getSubject();
    if (!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE))) {
      throw new RuntimeException("Invalid refresh token");
    }
    if (isTokenExpired(refreshToken) || isRefreshTokenWithdrown(refreshToken, userName)) {
      throw new RuntimeException("Refresh token expired");
    }
    return generateAccessToken(userName);
  }
}
