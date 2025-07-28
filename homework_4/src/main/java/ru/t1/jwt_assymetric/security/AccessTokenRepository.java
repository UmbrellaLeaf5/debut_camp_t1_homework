package ru.t1.jwt_assymetric.security;

import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {}
