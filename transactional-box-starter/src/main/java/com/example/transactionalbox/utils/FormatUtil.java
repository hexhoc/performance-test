package com.example.transactionalbox.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@UtilityClass
public class FormatUtil {
    private static final String END_CHARS = ", ";

    public String format(Map<?, ?> map) {
        if (map == null) {
            return null;
        }
        var result = map.entrySet().stream()
                .map(e -> String.format("'%s' -> '%s'", e.getKey(), e.getValue()))
                .reduce("", (e1, e2) -> e1 + e2 + END_CHARS);
        return StringUtils.removeEnd(result, END_CHARS);
    }
}
