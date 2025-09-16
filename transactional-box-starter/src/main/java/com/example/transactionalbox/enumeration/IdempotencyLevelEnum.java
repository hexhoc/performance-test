package com.example.transactionalbox.enumeration;

public enum IdempotencyLevelEnum {
    NEW,
    RERUN,
    RESENT,
    IGNORE;
}
