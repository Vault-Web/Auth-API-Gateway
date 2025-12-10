package vaultweb.apigateway.util;

import static org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion.$2B;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtil {
  private static final int BCRYPT_ROUNDS = 12;
  private static final BCryptPasswordEncoder.BCryptVersion BCRYPT_VERSION = $2B;
  private static final BCryptPasswordEncoder encoder =
      new BCryptPasswordEncoder(BCRYPT_VERSION, BCRYPT_ROUNDS);

  /**
   * Encodes the password using the bcrypt algorithm.
   *
   * @param password to be encoded
   * @return hashed version of the password
   */
  public static String encode(final String password) {
    return encoder.encode(password);
  }

  /**
   * Checks if two passwords are equivalent.
   *
   * @param password that is unencrypted
   * @param hashedPassword from database
   * @return true if the passwords are the same
   */
  public static boolean matches(final String password, final String hashedPassword) {
    return encoder.matches(password, hashedPassword);
  }
}
