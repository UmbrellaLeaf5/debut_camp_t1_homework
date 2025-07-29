package io.github.UmbrellaLeaf5.authorization.auth;

import io.github.UmbrellaLeaf5.authorization.auth.request.AuthenticationRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.RefreshRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.RegistrationRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.SetPremiumRequest;
import io.github.UmbrellaLeaf5.authorization.auth.response.AuthenticationResponse;

public interface AuthenticationService {
  AuthenticationResponse login(AuthenticationRequest request);

  void register(RegistrationRequest request);
  AuthenticationResponse refreshToken(RefreshRequest request);
  void logout(String userId);
  void setPremium(SetPremiumRequest request);
}
