package net.oumoudev.ebankservice.services;

import net.oumoudev.ebankservice.entities.BankAccount;
import net.oumoudev.ebankservice.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EBankService {
    private BankAccountRepository accountRepository;

    public EBankService(BankAccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<BankAccount>  getAllBankAccounts(){
        return accountRepository.findAll();
    }

    public BankAccount getAllBankAccountsById(String id){
        return accountRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Account not found"));
    }

    public BankAccount save(BankAccount bankAccount){
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        return accountRepository.save(bankAccount);
    }
}
