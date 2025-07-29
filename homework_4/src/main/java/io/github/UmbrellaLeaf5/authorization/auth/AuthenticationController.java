package io.github.UmbrellaLeaf5.authorization.auth;

import io.github.UmbrellaLeaf5.authorization.auth.request.AuthenticationRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.RefreshRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.RegistrationRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.SetPremiumRequest;
import io.github.UmbrellaLeaf5.authorization.auth.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody @Valid final AuthenticationRequest authenticationRequest) {
    return ResponseEntity.ok(authenticationService.login(authenticationRequest));
  }

  @GetMapping("/logout")
  public ResponseEntity<Void> logout(final Authentication principal) {
    this.authenticationService.logout(principal.getName());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/premium")
  public ResponseEntity<Void> setPremium(@RequestBody @Valid final SetPremiumRequest request) {
    this.authenticationService.setPremium(request);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid final RegistrationRequest request) {
    this.authenticationService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthenticationResponse> refresh(
      @RequestBody @Valid final RefreshRequest request) {
    return ResponseEntity.ok(this.authenticationService.refreshToken(request));
  }
}
