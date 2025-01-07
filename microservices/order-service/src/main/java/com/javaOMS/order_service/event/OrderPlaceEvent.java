package com.javaOMS.order_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPlaceEvent {
    private String orderNumber;
    private String email;
}
