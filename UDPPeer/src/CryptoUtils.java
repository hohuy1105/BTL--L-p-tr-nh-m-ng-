import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "AzXt9HKDJzGitXvx";

    /**
     * Mã hóa một chuỗi.
     * @param valueToEnc Chuỗi cần mã hóa.
     * @return Chuỗi đã được mã hóa dưới dạng Base64.
     */
    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encValue);
    }

    /**
     * Giải mã một chuỗi.
     * @param encryptedValue Chuỗi đã mã hóa (dạng Base64).
     * @return Chuỗi đã được giải mã.
     */
    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue, StandardCharsets.UTF_8);
    }

    private static Key generateKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }
}