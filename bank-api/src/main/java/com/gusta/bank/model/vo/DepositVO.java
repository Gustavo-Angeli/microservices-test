package com.gusta.bank.model.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositVO {
    private String depositTo;
    private Double value;
}
