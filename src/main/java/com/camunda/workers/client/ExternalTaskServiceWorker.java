package com.camunda.workers.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.topic.TopicSubscription;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.camunda.workers.dto.ExternalWorkerConfiguration;
import com.camunda.workers.dto.TenantActivationStatus;
import com.camunda.workers.dto.TenantActivationStatus.ACTIVATION_STATUS;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExternalTaskServiceWorker {

  @Autowired
  private Gson gsonParser;

  @Autowired
  private ExternalWorkerConfiguration externalWorkerConfiguration;

  @EventListener(ApplicationReadyEvent.class)
  public void handleExternalTasks() {
    log.info("Initialzing the ExternalTaskServiceWorker with configuration:{}", externalWorkerConfiguration);

    ExternalTaskClient externalTaskClient = ExternalTaskClient.create()
        .baseUrl(externalWorkerConfiguration.getBaseUrl())
        .addInterceptor(
            requestContext -> requestContext.addHeader("Authorization", externalWorkerConfiguration.getAuthorization()))
        .asyncResponseTimeout(1000).maxTasks(2).workerId(externalWorkerConfiguration.getWorkerId()).build();

    TopicSubscription topicSubscription = externalTaskClient.subscribe(externalWorkerConfiguration.getTopicName())
        .handler((externalTask, externalTaskService) -> {

          String compensatedActivity = externalTask.getVariable("compensatedActivity");
          if (StringUtils.hasText(compensatedActivity)) {
            compensatedActivity = compensatedActivity + "," + externalTask.getActivityId();
          } else {
            compensatedActivity = externalTask.getActivityId();
          }

          TenantActivationStatus tenantActivationStatus = TenantActivationStatus.builder()
              .activationStatus(ACTIVATION_STATUS.DE_ACTIVATED.getActivationStatus())
              .compensatedActivites(Arrays.stream(compensatedActivity.split(",")).collect(Collectors.toList()))
              .servicesList(Arrays.asList("Inventory service", "Order Management service", "Payments Service"))
              .tenantId(externalTask.getVariable("tenant")).build();

          JsonValue activationStatusValue = ClientValues.jsonValue(gsonParser.toJson(tenantActivationStatus));

          Map<String, Object> variables = new HashMap<>();
          variables.put("tenantActivationStatus", activationStatusValue);
          variables.put("compensatedActivity", compensatedActivity);

          externalTaskService.complete(externalTask, variables);

          // externalTaskService.extendLock(externalTask, newDuration);
          // externalTaskService.handleBpmnError(externalTask, errorCode);
          // externalTaskService.handleFailure(externalTask, errorMessage, errorDetails, retries,
          // retryTimeout);

          log.info("ExternalTask {} with businessKey {} completed!!!", externalTask.getId(),
              externalTask.getBusinessKey());

        }).lockDuration(30000).open();

    log.info("ExternalTaskServiceWorker has subscribed to the topic {} sucessfully", topicSubscription.getTopicName());
  }

}