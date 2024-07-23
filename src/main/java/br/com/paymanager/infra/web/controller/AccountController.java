package br.com.paymanager.infra.web.controller;

import br.com.paymanager.application.dto.request.AccountRequest;
import br.com.paymanager.application.enumator.AccountStatusEnum;
import br.com.paymanager.domain.model.Account;
import br.com.paymanager.domain.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
public class AccountController {

    private final AccountService accountService;


    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }



    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest accountRequest){
        try{
            accountService.createAccount(accountRequest);
            return ResponseEntity.ok("Conta criada com sucesso.");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error create account");
        }

    }
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public Page<Account> getAll(
            @RequestParam(required = true) int page,
            @RequestParam(required = true) int size,
            @RequestParam(required = false) String sortBy) {

       Pageable pageable;
       if(sortBy != null) {
           pageable = PageRequest.of(page, size, Sort.by(sortBy));
       }else {
           pageable = PageRequest.of(page,size);
       }

        return accountService.getAllAccounts(pageable);
    }
    @GetMapping("/{idAccount}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Account> findAccountById(@PathVariable("idAccount") Long idAccount) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(accountService.findAccountById(idAccount));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Account());
        }
    }

    @PutMapping("/{idAccount}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> updateAccount(@PathVariable("idAccount") Long idAccount, @RequestBody AccountRequest accountRequest) throws Exception {
        accountService.updateAccount(idAccount,accountRequest);
        return ResponseEntity.ok("Conta atualizada com sucesso.");
    }
    @PutMapping("/{idAccount}/status")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> updateAccountStatus(@PathVariable("idAccount") Long idAccount, @RequestBody AccountStatusEnum status) throws Exception {
        accountService.updateAccountStatus(idAccount,status);
        return ResponseEntity.ok("Conta atualizada com sucesso.");
    }

    @DeleteMapping("/{idAccount}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String>  deleteAccount(@PathVariable("idAccount") Long idAccount) throws Exception {
         accountService.deleteAccount(idAccount);
         return ResponseEntity.ok("Conta removida com sucesso.");
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload de arquivo Excel para importar contas",
            description = "Upload de arquivo Excel para importar contas")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> uploadFile(
            @Parameter(description = "Arquivo Excel contendo contas a serem  importadas", required = true)
            @RequestParam("file") MultipartFile file) {
        try{
            accountService.saveAccountsFromExcel(file);
            return ResponseEntity.status(HttpStatus.OK).body("Arquivo carregado e processado com sucesso.");
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o arquivo.");
        }
    }

}
