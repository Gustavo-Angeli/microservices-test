package com.gusta.bank.model.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferVO {
    private String transferTo;
    private Double value;
}
