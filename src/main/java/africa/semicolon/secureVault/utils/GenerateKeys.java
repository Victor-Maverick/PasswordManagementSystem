package africa.semicolon.secureVault.utils;

import java.security.SecureRandom;

public class GenerateKeys {
    private static final SecureRandom secureRandom = new SecureRandom();
    public static int generateKey(){
        return 1000 + secureRandom.nextInt(4000);
    }
}
