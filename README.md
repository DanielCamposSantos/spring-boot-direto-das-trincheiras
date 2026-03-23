# 🚀 Spring Boot Direto das Trincheiras

Projeto de estudos focado em **desenvolvimento backend com Spring Boot**, aplicando conceitos de forma prática e próxima de cenários reais de produção.

A proposta é sair do básico teórico e trabalhar com ferramentas e padrões usados no dia a dia de um desenvolvedor backend.

---

## 📌 Objetivos

- Consolidar fundamentos do ecossistema Spring
- Aplicar boas práticas de desenvolvimento backend
- Trabalhar com banco de dados real em ambiente isolado
- Escrever testes de integração robustos
- Simular cenários próximos de produção

---

## 🛠️ Tecnologias Utilizadas

- **Java**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Docker**
- **Testcontainers**
- **JUnit 5**
- **MySQL** (via container)
- **H2** (para cenários específicos de teste)

---

## 🧱 Estrutura do Projeto

O projeto segue uma organização baseada em camadas:
controller → service → repository → database

Além disso:

- Separação clara entre DTOs de entrada e saída
- Tratamento de exceções centralizado
- Uso de boas práticas com JPA

---

## 🧪 Testes

Os testes são um dos focos principais do projeto.

### Tipos de testes implementados:

- Testes de integração com Spring Boot
- Testes com **Testcontainers** (banco real isolado)
- Uso de scripts SQL para setup de dados
- Validação de respostas HTTP completas

### Por que Testcontainers?

Evita inconsistências comuns com banco em memória (H2) e garante:

- comportamento mais próximo de produção
- maior confiabilidade nos testes
- detecção de problemas reais de SQL e schema

---

## 🐳 Docker

O projeto utiliza Docker para:

- Subir banco de dados isolado
- Garantir reprodutibilidade do ambiente
- Evitar dependência de configuração local

---

## ⚠️ Decisões e Aprendizados

Alguns pontos importantes explorados durante o desenvolvimento:

- Diferenças entre H2 e bancos reais (MySQL)
- Problemas com `ddl-auto` e ordem de execução de scripts
- Sensibilidade a case em nomes de tabelas
- Impacto de `globally_quoted_identifiers`
- Isolamento de testes vs dependência de estado

---

## ▶️ Como executar o projeto

### Pré-requisitos

- Java 17+
- Docker

---
### Rodando a aplicação

```bash
./mvnw spring-boot:run
```

---

## 📜 Histórico de Mudanças

### [2026-03-22] - Testes de Integração e Refatoração
- **Adição de RestAssured**: Implementação de testes de integração para `ProfileController` cobrindo cenários de sucesso e erro.
- **Correção de Typo**: Renomeação de `UserProfileUserGetRespose` para `UserProfileUserGetResponse`.
- **Padronização de Dados**: Atualização dos perfis de teste ("Silviodino" -> "Admin", "CezinhaGamer" -> "Tester") para maior clareza.
- **Otimização**: Minificação de arquivos JSON de teste e melhoria nos scripts SQL de inicialização/limpeza (`clean_profiles.sql`).
