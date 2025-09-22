package com.example.transactionalbox.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Data
@RequiredArgsConstructor
public class MdcContextMapDecorator {
    private final Map<String, String> contextMap;

    public static MdcContextMapDecorator ofCurrentMdc() {
        return new MdcContextMapDecorator(currentContextMap());
    }

    public <T> T decorate(Supplier<T> supplier) {
        appendToContextMap();
        try {
            return supplier.get();
        } finally {
            MDC.clear();
        }
    }

    public void decorate(Runnable runnable) {
        appendToContextMap();
        try {
            runnable.run();
        } finally {
            MDC.clear();
        }
    }

    private void appendToContextMap() {
        var newContextMap = new HashMap<>(currentContextMap());
        newContextMap.putAll(contextMap);
        MDC.setContextMap(newContextMap);
    }

    private static Map<String, String> currentContextMap() {
        return Optional.ofNullable(MDC.getCopyOfContextMap())
                .orElseGet(Collections::emptyMap);
    }
}
