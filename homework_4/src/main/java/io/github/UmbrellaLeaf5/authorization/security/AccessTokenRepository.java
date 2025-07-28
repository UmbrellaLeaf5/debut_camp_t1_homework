package io.github.UmbrellaLeaf5.authorization.security;

import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {}
