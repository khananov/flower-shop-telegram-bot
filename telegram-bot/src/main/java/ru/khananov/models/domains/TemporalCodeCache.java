package ru.khananov.models.domains;

import java.util.HashMap;
import java.util.Map;

public final class TemporalCodeCache {
    private static TemporalCodeCache INSTANCE;
    private final Map<Long, Long> codeCache = new HashMap<>();

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
        return codeCache.get(chatId).toString();
    }

    public void deleteCodeByChatId(Long chatId) {
        codeCache.remove(chatId);
    }
}