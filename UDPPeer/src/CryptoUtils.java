import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class CryptoUtils {
    // Chỉ định rõ ràng thuật toán, chế độ và cách đệm
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "ThisIsA16ByteKey"; // 16-byte key

    // CBC mode cần một Initialization Vector (IV) - 16 bytes
    private static final IvParameterSpec IV = new IvParameterSpec("ThisIsAnIV123456".getBytes(StandardCharsets.UTF_8));

    public static String encrypt(String valueToEnc) throws Exception {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher c = Cipher.getInstance(TRANSFORMATION);
        // Sử dụng ENCRYPT_MODE với key và IV
        c.init(Cipher.ENCRYPT_MODE, key, IV);
        byte[] encValue = c.doFinal(valueToEnc.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encValue);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher c = Cipher.getInstance(TRANSFORMATION);
        // Sử dụng DECRYPT_MODE với key và IV
        c.init(Cipher.DECRYPT_MODE, key, IV);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue, StandardCharsets.UTF_8);
    }
}