package com.javaOMS.notification_service.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPlaceEvent {
    private String orderNumber;
    private String email;
}