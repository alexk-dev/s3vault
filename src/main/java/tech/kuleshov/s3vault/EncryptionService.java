package tech.kuleshov.s3vault;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.jasypt.util.text.AES256TextEncryptor;

public class EncryptionService {
  private final Config config;

  public EncryptionService(Config config) {
    this.config = config;
  }

  public String encrypt(String rawData) {
    AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
    textEncryptor.setPassword(getHashedPassword(config.getPassword(), config.getSalt()));
    return textEncryptor.encrypt(rawData);
  }

  public String decrypt(String encryptedData) {
    AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
    textEncryptor.setPassword(getHashedPassword(config.getPassword(), config.getSalt()));
    return textEncryptor.decrypt(encryptedData);
  }

  private static String getHashedPassword(String password, String salt) {
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
    SecretKeyFactory factory;
    try {
      factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] hash = factory.generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }
}
