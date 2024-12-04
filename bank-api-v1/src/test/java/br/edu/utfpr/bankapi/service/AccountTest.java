package br.edu.utfpr.bankapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.edu.utfpr.bankapi.dto.AccountDTO;
import br.edu.utfpr.bankapi.exception.NotFoundException;
import br.edu.utfpr.bankapi.model.Account;
import br.edu.utfpr.bankapi.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)

public class AccountTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    void deveriaRetornarContaPeloNumero() {
        // ARRANGE
        long accountNumber = 123456789L;
        Account account = new Account("Joao da Silva Sauro", accountNumber, 2000.0, 500.0);
        given(accountRepository.getByNumber(accountNumber)).willReturn(Optional.of(account));

        // ACT
        Optional<Account> result = accountService.getByNumber(accountNumber);

        // ASSERT
        assertTrue(result.isPresent()); 
        assertEquals(account.getNumber(), result.get().getNumber());  

        BDDMockito.then(accountRepository).should().getByNumber(accountNumber);                                                                       
    }

    @Test
    void DeveriaRetornarTodasContas() {
        // ARRANGE
        Account account = new Account("Maria das Dores Amada", 123456789L, 2000.0, 500.0);
        BDDMockito.given(accountRepository.findAll()).willReturn(Arrays.asList(account));

        // ACT
        List<Account> results = accountService.getAll();

        // ASSERT
        assertFalse(results.isEmpty()); 
        assertEquals(1, results.size()); 
        BDDMockito.then(accountRepository).should().findAll(); // Confirma se foi chamado
    }


    @Test
    void shouldSaveAccountWithInitialBalance() {
        // ARRANGE
        AccountDTO accountDTO = new AccountDTO("Pedro Titanossauro Rex", 123456789L, 0, 500.0);
        BDDMockito.given(accountRepository.save(any(Account.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Account savedAccount = accountService.save(accountDTO);

        // ASSERT
        assertEquals(0, savedAccount.getBalance()); 
        BDDMockito.then(accountRepository).should().save(accountCaptor.capture()); 
        Account captured = accountCaptor.getValue();
        assertEquals(accountDTO.name(), captured.getName()); 
        assertEquals(500.0, captured.getSpecialLimit()); 
    }


    @Test
    void deveriaAtualizarConta() throws NotFoundException {
        // ARRANGE
        long accountId = 1L;
        Account existingAccount = new Account("Amanda roberta", 123456789L, 1000.0, 300.0);
        AccountDTO updateDTO = new AccountDTO("A conta foi atualizada", 123456789L, 1500.0, 700.0);
        given(accountRepository.findById(accountId)).willReturn(Optional.of(existingAccount));
        

        given(accountRepository.save(any(Account.class))).willAnswer(invocation -> {
            Account a = invocation.getArgument(0);
            a.setName(updateDTO.name());
            a.setNumber(updateDTO.number());
            a.setBalance(updateDTO.balance());
            a.setSpecialLimit(updateDTO.specialLimit());
            return a;
        });

        // ACT
        Account updatedAccount = accountService.update(accountId, updateDTO);

        // ASSERT
        then(accountRepository).should().save(accountCaptor.capture()); 
        Account captured = accountCaptor.getValue();
        assertEquals("A conta foi atualizada", captured.getName()); 
        assertEquals(1500.0, captured.getBalance()); 
        assertEquals(700.0, captured.getSpecialLimit()); 
    }



    @Test
    void deveriaLancarExcecaoQuandoContaNaoEncontradaForAtualizada() {
        // ARRANGE
        long accountId = 1L;
        given(accountRepository.findById(accountId)).willReturn(Optional.empty());

        // ACT / ASSERT
        assertThrows(NotFoundException.class,
                () -> accountService.update(accountId, new AccountDTO("Mario Andrade", 123456789L, 1000.0, 500.0))); 
        then(accountRepository).should(never()).save(any(Account.class)); 
    }
}