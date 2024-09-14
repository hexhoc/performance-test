package com.example.calculateservice.utils;

import org.slf4j.MDC;

public class TraceUtil {

    private final static String TRACE_ID = "traceId";

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void updateTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }
}
