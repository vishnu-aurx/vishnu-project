package com.aurx.core.utils;

import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;
import com.aurx.core.services.CryptoUtil;
import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This class is used to decrypt the text.
 */
@Component(service = CryptoUtil.class, immediate = true)
public class CryptoUtilImpl implements CryptoUtil {

  /**
   * logger - Logger object.
   */
  private static final Logger logger = LoggerFactory.getLogger(CryptoUtilImpl.class);

  /**
   * cryptoSupport - The cryptoSupport
   */
  @Reference
  private CryptoSupport cryptoSupport;

  /**
   * This method is used to decrypt text.
   *
   * @param encryptedText - Is a string with encrypted value
   * @return - decryptedText
   */
  @Override
  public String getDecryptedValue(String encryptedText) {
    String decryptedText = StringUtils.EMPTY;
    logger.info("Start of getDecryptedValue method with encryptedText is : {}", encryptedText);
    try {
      decryptedText =
          cryptoSupport.isProtected(encryptedText) ? cryptoSupport.unprotect(encryptedText)
              : encryptedText;
    } catch (CryptoException e) {
      logger.error("CryptoException : {}", e.getMessage());
    }
    logger.info("getDecryptedValue returning decryptedText : {} End of getDecryptedValue method",
        decryptedText);
    return decryptedText;
  }
}
