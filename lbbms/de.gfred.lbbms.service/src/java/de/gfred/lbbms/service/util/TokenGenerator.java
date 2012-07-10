package de.gfred.lbbms.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
public class TokenGenerator {
    private static final String TAG = "de.gfred.lbbms.service.util.TokenGenerator";
    private static final boolean DEBUG = false;

    public static String generateToken(final String email){
        String value = email+System.currentTimeMillis();
        UUID uuid = UUID.nameUUIDFromBytes(value.getBytes());       
        return uuid.toString().replaceAll("-", "");
    }
}
