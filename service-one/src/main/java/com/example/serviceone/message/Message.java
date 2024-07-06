package com.example.serviceone.message;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class Message<T> {

  // Cloud Events compliant 
  private final String eventType;
  private final String requestId; // unique id of this message
  private final String source = "service-one";
  private final LocalDateTime time;
  private final T data;
  private final String dataContentType="application/json";
  private final String specVersion="1.0";
  
  // Extension attributes
  private final String traceId; // trace id, default: new unique
}