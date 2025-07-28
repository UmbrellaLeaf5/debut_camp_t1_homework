package ru.t1.jwt_assymetric.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.t1.jwt_assymetric.auth.request.AuthenticationRequest;
import ru.t1.jwt_assymetric.auth.request.RefreshRequest;
import ru.t1.jwt_assymetric.auth.request.RegistrationRequest;
import ru.t1.jwt_assymetric.auth.request.SetPremiumRequest;
import ru.t1.jwt_assymetric.auth.response.AuthenticationResponse;

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
