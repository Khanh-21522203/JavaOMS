package com.javaOMS.product_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String description,
                              String skuCode, BigDecimal price) {
}
