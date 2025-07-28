package ru.t1.jwt_assymetric.auth;

import ru.t1.jwt_assymetric.auth.request.AuthenticationRequest;
import ru.t1.jwt_assymetric.auth.request.RefreshRequest;
import ru.t1.jwt_assymetric.auth.request.RegistrationRequest;
import ru.t1.jwt_assymetric.auth.request.SetPremiumRequest;
import ru.t1.jwt_assymetric.auth.response.AuthenticationResponse;

public interface AuthenticationService {
  AuthenticationResponse login(AuthenticationRequest request);

  void register(RegistrationRequest request);

  AuthenticationResponse refreshToken(RefreshRequest request);

  void logout(String userId);

  void setPremium(SetPremiumRequest request);
}
