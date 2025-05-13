package com.ecommerce.service.integration.balance;

import com.transfer.api.service.integration.balance.request.NotificationBacenRequest;

@FunctionalInterface
public interface NotificationBacen {
    void notificationBacen(final NotificationBacenRequest notificationBacenRequest);
}
