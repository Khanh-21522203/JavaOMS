package com.javaOMS.product_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public record ProductRequest(String name, String description,
                             String skuCode, BigDecimal price) { }
