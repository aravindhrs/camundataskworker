package com.camunda.workers.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String orderId;

  private final String orderType;

  private final String orderName;

  private final double orderPrice;

}
