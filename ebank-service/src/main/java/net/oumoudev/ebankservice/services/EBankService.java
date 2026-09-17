package net.oumoudev.ebankservice.services;

import net.oumoudev.ebankservice.entities.BankAccount;
import net.oumoudev.ebankservice.feign.CustomerRestClient;
import net.oumoudev.ebankservice.model.Customer;
import net.oumoudev.ebankservice.repository.BankAccountRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
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

    @McpTool(description = "Get all Bank accounts")
    public List<BankAccount>  getAllBankAccounts(){
        return accountRepository.findAll();
    }

    @McpTool(description = "Get a bank account by id")
    public BankAccount getAllBankAccountsById(@McpToolParam(description = "Bank account id") String id){
        BankAccount bankAccount = accountRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));

        return bankAccount;
    }

    @McpTool(description = "Save a new bank account")
    public BankAccount save(@McpToolParam(description = "The bank account to save (balance, type, customerId") BankAccount bankAccount){
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
