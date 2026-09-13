package net.oumoudev.ebankservice.controllers;

import net.oumoudev.ebankservice.entities.BankAccount;
import net.oumoudev.ebankservice.services.EBankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EbankRestController {
    private EBankService eBankService;

    public EbankRestController(EBankService eBankService){
        this.eBankService = eBankService;
    }

    @GetMapping("/accounts")
    public List<BankAccount> getAllBankAccounts(){
        return eBankService.getAllBankAccounts();
    }

    @GetMapping("/accounts/{id}")
    public BankAccount getAllBankAccountsById(@PathVariable String id){
        return eBankService.getAllBankAccountsById(id);
    }

    @PostMapping("/accounts")
    public BankAccount save(@RequestBody BankAccount bankAccount){
        return eBankService.save(bankAccount);
    }
}
