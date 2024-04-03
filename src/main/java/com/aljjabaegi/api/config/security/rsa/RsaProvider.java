package com.aljjabaegi.api.config.security.rsa;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 설정
 * 검증 : <a href="https://cryptotools.net/rsagen">...</a>
 * (주의) RSA 키는 DB나 설정파일에서 관리되어서는 안된다.<br />
 * 예제로 해당 프로젝트에서는<br />
 * 생성된 키를 환경변수에 등록하여 설정파일에서 로드 해 사용한다. (sample)<br />
 * Could not resolve placeholder 'rsa-private' in value "${rsa-private}" -> 환경변수 등록 필요!<br />
 * value 없이 이름만 등록 후 실행하면 키값이 생성되어 로그로 출력됨.<br />
 * 해당 키값을 rsa-private, rsa-public 으로 환경변수에 등록<br />
 *
 * @author GEONLEE
 * @since 2024-04-03<br />
 */
@Configuration
public class RsaProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(RsaProvider.class);
    private final String algorithm;
    private final int keySize;
    private final String privateKey;
    private final String publicKey;

    public RsaProvider(@Value("${security.rsa.algorithm}") String algorithm, @Value("${security.rsa.key-size}") int keySize,
                       @Value("${security.rsa.private}") String privateKey, @Value("${security.rsa.public}") String publicKey)
            throws NoSuchAlgorithmException {
        this.algorithm = algorithm;
        this.keySize = keySize;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        if (!StringUtils.hasText(privateKey) || !StringUtils.hasText(publicKey)) {
            LOGGER.error("RSA public or private key does not exist. create new key. ▼");
            createKeyFile();
        }
    }

    /**
     * Generate RSA private, public key
     *
     * @author GEONLEE
     * @since 2024-04-03
     */
    private void createKeyFile() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(this.algorithm);
        keyPairGenerator.initialize(this.keySize, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        LOGGER.info("=================================RSA KEY=================================");
        LOGGER.info("private : {}", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        LOGGER.info("public : {}", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }

    /**
     * private key 리턴 메서드
     *
     * @return Private key
     * @author GEONLEE
     * @since 2024-04-03
     */
    public PrivateKey getPrivateKey() {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(this.privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            return keyFactory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException(CommonErrorCode.SERVICE_ERROR, e);
        }
    }

    /**
     * public key 리턴 메서드
     *
     * @return Public key
     * @author GEONLEE
     * @since 2024-04-03
     */
    public PublicKey getPublicKey() {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(this.publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NullPointerException e) {
            throw new ServiceException(CommonErrorCode.SERVICE_ERROR, e);
        }
    }

    /**
     * decrypt method
     *
     * @param encrypted public 키로 암호화된 Base64 encoded String
     * @return 복호화된 String
     * @author GEONLEE
     * @since 2024-04-03
     */
    public String decrypt(String encrypted) {
        PrivateKey privateKey = getPrivateKey();
        try {
            byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
            Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytePlain = cipher.doFinal(byteEncrypted);
            return new String(bytePlain, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                 | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new ServiceException(CommonErrorCode.SERVICE_ERROR, e);
        }
    }

    /**
     * encrypt method
     *
     * @param plainText 암호화 할 String
     * @return 암호화된 Base64 encoded String
     * @author GEONLEE
     * @since 2024-04-03
     */
    private String encrypt(String plainText) {
        PublicKey publicKey = getPublicKey();
        try {
            Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytePlain = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(bytePlain);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                 | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new ServiceException(CommonErrorCode.SERVICE_ERROR, e);
        }
    }
}
