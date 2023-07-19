package ru.khananov.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseParamsDto {
    private String userId;
    private String orderId;
    private String orderPrice;
    private String orderDescription;
    private String userName;
    private String userEmail;
    private String userAddress;
}
