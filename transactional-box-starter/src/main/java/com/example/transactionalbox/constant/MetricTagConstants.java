package com.example.transactionalbox.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MetricTagConstants {

    //names

    public static final String TARGET_APPLICATION = "target_application";
    public static final String PROTOCOL = "protocol";
    public static final String ERROR_CODE = "errorCode";
    public static final String EXTERNAL_APPLICATION = "external_application";

    // values

    public static final String PROTOCOL_KAFKA_VALUE = "kafka";
    public static final String PROTOCOL_JMS_VALUE = "jms";
    public static final String API_MANAGER_APPLICATION_VALUE = "csp-api-manager";
    public static final String KAFKA_APPLICATION_VALUE = "kafka";
}