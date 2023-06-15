package com.aurx.core.services;
/**
 * interface CryptoUtil
 */
public interface CryptoUtil {
  /**
   * Method to convert encryptedText to decryptedText
   *
   * @param encryptedText - Is a string with encrypted value
   * @return - return decryptedText of encrypted value
   */
  String getDecryptedValue(final String encryptedText);
}
