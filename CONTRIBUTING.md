# Como contribuir

Obrigado pelo interesse em melhorar o GymFlow. Para manter o projeto previsível e fácil de revisar:

1. Crie uma branch a partir de `main` com um nome descritivo, como `feature/exportacao-financeira`.
2. Mantenha cada alteração focada em um único objetivo.
3. Inclua testes para regras de negócio novas ou alteradas.
4. Execute `./mvnw verify` antes de abrir o pull request.
5. Descreva no pull request o problema resolvido, a solução e como validar o comportamento.

## Padrões do projeto

- Java 21 e Spring Boot.
- Código e mensagens de domínio em inglês; interface e documentação para o usuário em português.
- Validação no limite da aplicação e regras de negócio na camada de serviço.
- Alterações no esquema sempre por uma nova migration Flyway. Não edite migrations já publicadas.
- Nunca versione senhas, arquivos `.env`, bancos locais ou dados pessoais reais.

## Relato de falhas

Ao abrir uma issue, informe os passos para reproduzir, o comportamento esperado, o comportamento observado e a versão do Java utilizada. Para vulnerabilidades, evite divulgar detalhes exploráveis em uma issue pública.
