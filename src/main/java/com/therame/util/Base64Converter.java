package com.therame.util;

import org.springframework.util.Base64Utils;

import javax.validation.constraints.NotNull;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Utility class for converting UUIDs from/to base64 strings
 */
public class Base64Converter {

    public static String toUrlSafeString(@NotNull UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return Base64Utils.encodeToUrlSafeString(buffer.array());
    }

    public static UUID fromUrlSafeString(@NotNull String string) {
        byte[] bytes = Base64Utils.decodeFromUrlSafeString(string);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

}
