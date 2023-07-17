package ru.khananov.models.domains;

import java.util.HashMap;
import java.util.Map;

public final class TemporalCodeCache {
    private static TemporalCodeCache INSTANCE;
    private Map<Long, Long> codeCache = new HashMap<>();

    private TemporalCodeCache() {
    }

    public static TemporalCodeCache getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TemporalCodeCache();
        }

        return INSTANCE;
    }

    public void addCode(Long chatId, Long code) {
        codeCache.put(chatId, code);
    }

    public String getCodeByChatId(Long chatId) {
        String code;
        if (codeCache.containsKey(chatId)) {
            code = codeCache.get(chatId).toString();
            codeCache.remove(chatId, code);
            return code;
        }

        return null;
    }

    public void deleteCodeByChatId(Long chatId) {
        String code;
        if (codeCache.containsKey(chatId)) {
            code = codeCache.get(chatId).toString();
            codeCache.remove(chatId, code);
        }
    }
}
