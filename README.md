Tendo como base o projeto BANK-API, disponível no repositório Git (https://github.com/ricardosobjak/op63k-2-2024), implemente os seguintes testes baseando-se nos exemplos vistos em aula:

Camada de Serviço: 
Classe TransactionService: implementar os métodos para teste de saque (withdraw) e transferência (transfer);
Classe AccountService: implementar os métodos para testar os métodos getByNumber, getAll, save e updade.
Camada de Controle
Classe TransactionController: implementar os métodos para teste de saque (withdraw) e transferência (transfer);
Classe AccountController: implementar os métodos para testar os métodos getByNumber, getAll, save e updade.
Validações
Testar as classes:
AvailableAccountValidation: esta classe verifica se existe uma conta bancária cadastrada no sistema.
AvailableBalanceValidation: classe responsável por verificar se a conta bancária possui saldo suficiente, considerando o saldo + limite especial.
