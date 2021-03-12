package com.camunda.workers.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class TenantActivationStatus implements Serializable {

  public enum ACTIVATION_STATUS {
    ACTIVATED("activated"), IN_PROGESS("inprogress"), DE_ACTIVATED("de-activated");

    private final String activationStatus;

    ACTIVATION_STATUS(String activationStatus) {
      this.activationStatus = activationStatus;
    }

    public String getActivationStatus() {
      return activationStatus;
    }
  }

  private static final long serialVersionUID = 1L;

  private final String tenantId;

  private final String activationStatus;

  private final List<String> servicesList;

  private final List<String> compensatedActivites;

}
