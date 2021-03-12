package com.camunda.workers.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "external.worker", ignoreUnknownFields = true, ignoreInvalidFields = true)
public class ExternalWorkerConfiguration {

  private String workerId;

  private String topicName;

  private String authorization;

  private String baseUrl;

}
