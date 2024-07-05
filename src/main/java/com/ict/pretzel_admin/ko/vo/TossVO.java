package com.ict.pretzel_admin.ko.vo;

import lombok.Data;

@Data
public class TossVO {
    private String toss_idx, orderId, approvedAt, paymentKey, authorizationHeader, orderName, 
                    cancelReason, canceledAt, user_id;
    private int amount, cancelAmount;
}
