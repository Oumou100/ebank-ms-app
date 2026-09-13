package net.oumoudev.ebankservice.services;

import net.oumoudev.ebankservice.entities.BankAccount;
import net.oumoudev.ebankservice.feign.CustomerRestClient;
import net.oumoudev.ebankservice.model.Customer;
import net.oumoudev.ebankservice.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EBankService {
    private BankAccountRepository accountRepository;

    private CustomerRestClient customerRestClient;
    public EBankService(BankAccountRepository accountRepository, CustomerRestClient customerRestClient){
        this.accountRepository = accountRepository;
        this.customerRestClient  = customerRestClient;
    }

    public List<BankAccount>  getAllBankAccounts(){
        return accountRepository.findAll();
    }

    public BankAccount getAllBankAccountsById(String id){
        BankAccount bankAccount = accountRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));

        return bankAccount;
    }

    public BankAccount save(BankAccount bankAccount){
        try{
            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
            return accountRepository.save(bankAccount);

        }catch(Exception e){
                throw new RuntimeException((e.getMessage()));
        }
    }
}
