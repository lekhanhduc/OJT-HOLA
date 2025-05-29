package vn.khanhduc.springbootexercise.mapper;

import vn.khanhduc.springbootexercise.dto.response.BalanceUpdateResponse;
import vn.khanhduc.springbootexercise.entity.Balance;

public class BalanceMapper {

    private BalanceMapper() {}

    public static BalanceUpdateResponse toBalanceUpdateResponse(Balance balance) {
        BalanceUpdateResponse response = new BalanceUpdateResponse();
        response.setAccountId(balance.getAccount().getId());
        response.setAvailableBalance(balance.getAvailableBalance());
        response.setHoldBalance(balance.getHoldBalance());
        response.setTotalBalance(balance.getTotalBalance());
        response.setLastUpdated(balance.getLastUpdated());
        return response;
    }

}
