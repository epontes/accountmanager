package br.com.paymanager.application.dto.request;

import br.com.paymanager.application.enumator.AccountStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private String description;
    private Double price;
    private LocalDate payDay;
    private LocalDate expiredDate;
    private AccountStatusEnum status;
}
