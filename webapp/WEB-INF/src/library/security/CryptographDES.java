package library.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptographDES implements Cryptograph {
    private String random = null;
    final Logger logger = LoggerFactory.getLogger(CryptographDES.class);
    
    
    public void setRandom(String random) {
        logger.info("Set random " + random);
        this.random = random;
    }

    public String getRandom() {
        return random;
    }

    public String encrypt(String toEncrypt) {
        try {
            SecretKey key = getGeneratedKey();
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(toEncrypt.getBytes("UTF-8"));
            return Base64.encodeBase64URLSafeString(cipherText);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("NoSuchAlgorithmException reported on encrypting "+toEncrypt, nsae);
        } catch (InvalidKeyException ike) {
            logger.error("InvalidKeyException reported on encrypting "+toEncrypt, ike);
        } catch(NoSuchPaddingException nspe) {
            logger.error("NoSuchPaddingException reported on encrypting "+toEncrypt, nspe);
        } catch(BadPaddingException bpe) {
            logger.error("BadPaddingException reported on encrypting "+toEncrypt, bpe);
        } catch(IllegalBlockSizeException ibse) {
            logger.error("IllegalBlockSizeException reported on encrypting "+toEncrypt, ibse);
        } catch(UnsupportedEncodingException ueex) {
            logger.error("UnsupportedEncodingException reported on encrypting "+toEncrypt, ueex);
        }
        return null;
    }

    public String decrypt(String toDecrypt) {
        try {
            SecretKey key = getGeneratedKey();
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(Base64.decodeBase64(toDecrypt));
            return new String(cipherText, "UTF-8");
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("NoSuchAlgorithmException reported on decrypting "+toDecrypt, nsae);
        } catch (InvalidKeyException ike) {
            logger.error("InvalidKeyException reported on decrypting "+toDecrypt, ike);
        } catch (NoSuchPaddingException nspe) {
            logger.error("NoSuchPaddingException reported on decrypting "+toDecrypt, nspe);
        } catch (BadPaddingException bpe) {
            logger.error("BadPaddingException reported on decrypting "+toDecrypt, bpe);
        } catch (IllegalBlockSizeException ibse) {
            logger.error("IllegalBlockSizeException reported on decrypting "+toDecrypt, ibse);
        } catch (UnsupportedEncodingException ueex) {
            logger.error("UnsupportedEncodingException reported on decrypting "+toDecrypt, ueex);
        }
        return null;
    }

    private SecretKey getGeneratedKey() {
        try {
            SecureRandom secran = SecureRandom.getInstance("SHA1PRNG");
            secran.setSeed(random.getBytes());
            KeyGenerator keygen = KeyGenerator.getInstance("DES");
            keygen.init(secran);
            return keygen.generateKey();
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("NoSuchAlgorithmException reported on generating SecretKey", nsae);
        }
        return null;
    }
}