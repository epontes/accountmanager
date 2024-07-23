package br.com.paymanager.domain.model;

import br.com.paymanager.application.enumator.AccountStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "ACCOUNT")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATA_VENCIMENTO")
    private LocalDate dataVencimento;

    @Column(name = "DATA_PAGAMENTO")
    private LocalDate dataPagamento;

    @Column(name = "VALOR")
    private Double valor;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "SITUACAO")
    @Enumerated(EnumType.ORDINAL)
    private AccountStatusEnum situacao;
}
