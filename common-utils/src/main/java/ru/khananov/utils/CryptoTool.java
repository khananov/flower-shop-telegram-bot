package ru.khananov.utils;

import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashids;

    public CryptoTool(String salt) {
        int minHashLength = 10;
        hashids = new Hashids(salt, minHashLength);
    }

    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    public Long valueOf(String value) {
        long[] result = hashids.decode(value);
        if (result != null && result.length > 0)
            return result[0];

        return null;
    }
}
