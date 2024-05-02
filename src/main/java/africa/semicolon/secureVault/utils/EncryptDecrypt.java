package africa.semicolon.secureVault.utils;

import java.util.Arrays;
import java.util.Base64;

public class EncryptDecrypt {
    private static int key = 1000;

    public static String encrypt(String plainText, int userKey) {
        char[] plain = plainText.toCharArray();

        for (int count = 0; count < plain.length; count++) {
            plain[count] = (char) (plain[count]+key+userKey);
        }

        return new String(plain);
    }

    public static String decrypt(String cipherText, int userKey) {
        char[] cipher = cipherText.toCharArray();

        for (int count = 0; count < cipher.length; count++) {
            cipher[count] = (char) (cipher[count]-key-userKey);
        }
        return new String(cipher);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("Victor234", 234));
        System.out.println(decrypt("ԨԻԵՆՁՄԄԅԆ", 234));
    }
}
