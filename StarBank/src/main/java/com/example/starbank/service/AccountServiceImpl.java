package com.example.starbank.service;

import com.example.starbank.controller.request.TransferBalanceRequest;
import com.example.starbank.dto.AccountStatement;
import com.example.starbank.entity.Account;
import com.example.starbank.entity.Transaction;
import com.example.starbank.repository.AccountRepository;
import com.example.starbank.repository.TransactionRepository;
import com.example.starbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public Account save(Account account) {
        accountRepository.save(account);
        return accountRepository.findByAccountNumberEquals(account.getAccountNumber());
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumberEquals(accountNumber);
        return account;
    }


    @Override
    public Transaction sendMoney(
            TransferBalanceRequest transferBalanceRequest
    ) {
        String fromAccountNumber = transferBalanceRequest.getFromAccountNumber();
        String toAccountNumber = transferBalanceRequest.getToAccountNumber();
        BigDecimal amount = transferBalanceRequest.getAmount();
        Account fromAccount = accountRepository.findByAccountNumberEquals(
                fromAccountNumber
        );
        Account toAccount = accountRepository.findByAccountNumberEquals(toAccountNumber);
        if (fromAccount.getCurrentBalance().compareTo(BigDecimal.ONE) == 1
                && fromAccount.getCurrentBalance().compareTo(amount) == 1
        ) {
            fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(amount));
            accountRepository.save(fromAccount);
            toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(amount));
            accountRepository.save(toAccount);
            Transaction transaction = transactionRepository.save(new Transaction(0L, fromAccountNumber, amount, new Timestamp(System.currentTimeMillis())));
            return transaction;
        }
        return null;
    }

    @Override
    public AccountStatement getStatement(String accountNumber) {
        Account account = accountRepository.findByAccountNumberEquals(accountNumber);
        return new AccountStatement(account.getCurrentBalance(), transactionRepository.findByAccountNumberEquals(accountNumber));
    }
}