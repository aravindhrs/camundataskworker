package com.camunda.workers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SpringBootApplication
@EnableConfigurationProperties
@PropertySource(value = { "classpath:application.properties" }, ignoreResourceNotFound = true)
public class CamundaBPMTaskWorker {

  public static void main(String[] args) {
    SpringApplication.run(CamundaBPMTaskWorker.class, args);
  }

  @Bean
  public Gson gson() {
    return new GsonBuilder().setPrettyPrinting().create();
  }

}
