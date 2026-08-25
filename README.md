# GymFlow

[![CI](https://github.com/Lucascmc1/GymFlow/actions/workflows/ci.yml/badge.svg)](https://github.com/Lucascmc1/GymFlow/actions/workflows/ci.yml)
[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-2F3136.svg)](LICENSE)

Plataforma web completa para a operação diária de academias. O GymFlow reúne cadastro de alunos, planos e matrículas, controle financeiro, check-in, fichas de treino, avaliações físicas e indicadores gerenciais em uma interface responsiva.

## Funcionalidades

| Área | Recursos |
| --- | --- |
| Dashboard | KPIs de alunos, receita, inadimplência e acessos; evolução semanal; horários de pico; atividades recentes |
| Alunos | Cadastro completo, busca, filtros por situação, edição, inativação e visão 360° do aluno |
| Planos | Preço, duração, benefícios, ativação e desativação de ofertas |
| Matrículas | Associação entre aluno e plano, desconto, vigência, encerramento automático do contrato anterior e geração de cobranças |
| Financeiro | Mensalidades abertas, pagas e vencidas; baixa com forma de pagamento; totais e filtros |
| Check-in | Entrada rápida, validação de acesso, bloqueio de duplicidade diária e histórico |
| Treinos | Fichas personalizadas, treinador, objetivo, exercícios, séries, repetições, descanso e observações |
| Avaliações | Peso, altura, composição e medidas corporais, histórico e cálculo automático de IMC |
| Relatórios | Receita, valores pendentes, ticket médio, retenção, alunos por plano e ranking de frequência |

## Interface

A interface foi construída para uso em recepção e administração, com navegação lateral, cabeçalho contextual, cards de indicadores, tabelas responsivas, formulários validados, modais, feedback por notificações e estados vazios. O layout se adapta a desktop, tablet e celular sem depender de um framework visual externo.

## Tecnologias

- Java 21 e Maven Wrapper
- Spring Boot 4.1.1
- Spring MVC, Thymeleaf e Bean Validation
- Spring Security com autenticação por formulário, BCrypt e CSRF
- Spring Data JPA e Hibernate
- Flyway para versionamento do banco
- H2 no ambiente local e PostgreSQL em produção
- JUnit 5, Mockito, Spring Boot Test e MockMvc
- Docker, Docker Compose, GitHub Actions e Dependabot

## Executar localmente

Pré-requisito: JDK 21.

```bash
git clone https://github.com/Lucascmc1/GymFlow.git
cd GymFlow
./mvnw spring-boot:run
```

Acesse [http://localhost:8080](http://localhost:8080) e use as credenciais de demonstração:

```text
Usuário: admin
Senha:   admin123
```

Na primeira execução, o GymFlow cria o banco local em `./data` e inclui dados fictícios para navegação. Para iniciar com a base vazia:

```bash
APP_DEMO_DATA=false ./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd` no lugar de `./mvnw`.

## Executar com Docker

Copie as variáveis de exemplo, troque as senhas e suba os serviços:

```bash
cp .env.example .env
docker compose up --build
```

O Compose inicia PostgreSQL com volume persistente e publica a aplicação na porta `8080`.

## Configuração

| Variável | Padrão | Uso |
| --- | --- | --- |
| `PORT` | `8080` | Porta HTTP |
| `DATABASE_URL` | H2 em `./data/gymflow` | URL JDBC |
| `DATABASE_USERNAME` | `sa` | Usuário do banco |
| `DATABASE_PASSWORD` | vazio | Senha do banco |
| `APP_ADMIN_USERNAME` | `admin` | Usuário administrador |
| `APP_ADMIN_PASSWORD` | `admin123` | Senha administrativa |
| `APP_DEMO_DATA` | `true` | Carrega dados fictícios na base vazia |

> Em qualquer implantação acessível pela internet, defina uma senha administrativa forte e use PostgreSQL com credenciais próprias.

## Qualidade e testes

```bash
./mvnw clean verify
```

A suíte cobre validação de CPF, regras de alunos, matrículas, pagamentos gerados, autorização das páginas, check-in e inicialização completa da aplicação com migrations.

O health check está disponível em `GET /actuator/health`.

## Estrutura

```text
src/main/java/io/github/lucascmc1/gymflow
├── assessment     avaliações físicas
├── checkin        controle de acesso
├── config         segurança e carga inicial
├── dashboard      indicadores operacionais
├── member         alunos
├── membership     matrículas
├── payment        financeiro
├── plan           planos comerciais
├── report         relatórios gerenciais
├── shared         validações e tratamento de erros
└── workout        fichas e exercícios
```

Veja o [documento de arquitetura](docs/architecture.md) para o modelo de dados, regras de negócio e decisões operacionais.

## Licença

Distribuído sob a licença [MIT](LICENSE).
