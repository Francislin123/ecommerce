package com.ecommerce.service.integration.account;

import com.transfer.api.service.integration.account.response.AccountOriginResponse;

@FunctionalInterface
public interface Account {
    AccountOriginResponse searchSourceAccountData(final String idAccountOrigin);
}
