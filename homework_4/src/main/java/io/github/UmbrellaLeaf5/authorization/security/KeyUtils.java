package io.github.UmbrellaLeaf5.authorization.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {
  private KeyUtils() {}

  public static PrivateKey loadPrivateKey(final String pemPath) throws Exception {
    final String key = readKeyFromResource(pemPath)
                           .replace("-----BEGIN PRIVATE KEY-----", "")
                           .replace("-----END PRIVATE KEY-----", "")
                           .replaceAll("\\s+", "");
    final byte[] decoded = Base64.getDecoder().decode(key);
    final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
    return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
  }

  public static PublicKey loadPublicKey(final String pemPath) throws Exception {
    final String key = readKeyFromResource(pemPath)
                           .replace("-----BEGIN PUBLIC KEY-----", "")
                           .replace("-----END PUBLIC KEY-----", "")
                           .replaceAll("\\s+", "");
    final byte[] decoded = Base64.getDecoder().decode(key);
    final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
    return KeyFactory.getInstance("RSA").generatePublic(keySpec);
  }

  private static String readKeyFromResource(String pemPath) throws Exception {
    try (final InputStream is = KeyUtils.class.getClassLoader().getResourceAsStream(pemPath)) {
      if (is == null) {
        throw new IllegalArgumentException("file not found: " + pemPath);
      }
      return new String(is.readAllBytes());
    }
  }
}
