package br.com.paymanager.domain.service;

import br.com.paymanager.application.dto.request.AccountRequest;
import br.com.paymanager.application.enumator.AccountStatusEnum;
import br.com.paymanager.domain.model.Account;
import br.com.paymanager.infra.persistence.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(AccountRequest accountResponse) {
        try{
            var account = Account.builder()
                    .dataVencimento(accountResponse.getPayDay())
                    .dataPagamento(accountResponse.getExpiredDate())
                    .descricao(accountResponse.getDescription())
                    .situacao(accountResponse.getStatus())
                    .valor(accountResponse.getPrice())
                    .build();
            accountRepository.save(account);

        }catch (Exception e) {
            log.error("Não foi possivel criar a conta {} ", e.getMessage());
        }
    }
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }
    public Account findAccountById(Long accountById) throws Exception {
        return accountRepository.findById(accountById)
                .orElseThrow(() -> new Exception("Conta não encontrada"));
    }
    public void updateAccount(final Long idAccount, AccountRequest accountRequest) throws Exception {
        var account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new Exception("Não existe uma conta para ser removida "));

       var accountUpdate =  Account.builder()
                .id(account.getId())
                .situacao(accountRequest.getStatus() == null ? account.getSituacao() : accountRequest.getStatus())
                .valor(accountRequest.getPrice() == null ? account.getValor() : accountRequest.getPrice())
                .descricao(accountRequest.getDescription() == null ? account.getDescricao() : accountRequest.getDescription())
                .dataPagamento(accountRequest.getPayDay() == null ? account.getDataPagamento() : accountRequest.getPayDay() )
                .dataVencimento(accountRequest.getExpiredDate() == null ? account.getDataVencimento() : accountRequest.getExpiredDate())
                .build();


        accountRepository.save(accountUpdate);
    }
    public void updateAccountStatus(final Long idAccount, AccountStatusEnum status) throws Exception {
        var account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new Exception("Não existe uma conta para ser removida "));

        var accountUpdate =  Account.builder()
                .id(account.getId())
                .situacao(status)
                .build();


        accountRepository.save(accountUpdate);
    }
    public void deleteAccount(Long idAccount) throws Exception {
       var account = accountRepository.findById(idAccount)
                        .orElseThrow(() -> new Exception("Não existe uma conta para ser removida "));

        accountRepository.delete(account);
    }
    public void saveAccountsFromExcel(MultipartFile file) throws IOException {
        List<Account> accounts = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }

            Account account = new Account();
            account.setDataVencimento(convertToLocalDate(row.getCell(0)));
            account.setDataPagamento(convertToLocalDate(row.getCell(1)));
            account.setValor(parseNumericCell(row.getCell(2)));
            account.setDescricao(row.getCell(3).getStringCellValue());
            account.setSituacao(AccountStatusEnum.values()[(int) row.getCell(4).getNumericCellValue()]);

            accounts.add(account);
        }
        workbook.close();
        accountRepository.saveAll(accounts);
    }
    private LocalDate convertToLocalDate(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(cell.getStringCellValue(), formatter);
        }
        return null;
    }
    private Double parseNumericCell(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            return Double.valueOf(cell.getStringCellValue());
        } else {
            throw new IllegalStateException("Unsupported cell type for numeric value");
        }
    }


}

