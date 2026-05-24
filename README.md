# Juros Boleto API

Projeto de estudo de **Arquitetura Hexagonal (Ports and Adapters)** com Spring Boot. A aplicação expõe uma API REST que recebe um código de boleto vencido, consulta os dados em uma API externa de boletos, calcula os juros proporcionais aos dias de atraso e persiste o resultado em banco de dados.

---

## Problema / Estória de Usuário

> **Enquanto** usuário da API,
> **quero** digitar um código de boleto vencido,
> **e** receber o valor dos juros calculados.

---

## Requisitos Funcionais

- Receber um código de boleto e uma data de pagamento via API
- Consultar os dados do boleto em uma API externa de boletos
- O boleto deve estar vencido (data de vencimento anterior à data de pagamento)
- Apenas boletos do tipo **XPTO** podem ter juros calculados
- Em caso de erro, retornar o motivo de forma clara
- Salvar no banco de dados todos os cálculos realizados

---

## Cálculo dos Juros

A taxa de juros segue a regra de boleto bancário: **1% ao mês**, calculada de forma proporcional aos dias de atraso.

```
Taxa diária = 1% / 30 = 0,0333% ao dia

Juros = valor original × 0,033% × dias de atraso

Valor final = valor original + juros
```

**Exemplo:** Boleto de R$ 1.000,00 vencido há 10 dias:
- Juros = 1.000 × 0,00033 × 10 = **R$ 3,30**
- Valor final = **R$ 1.003,30**

---

## Arquitetura Hexagonal

O projeto implementa o padrão **Hexagonal Architecture (Ports and Adapters)**, cujo objetivo é isolar a lógica de negócio de detalhes externos como banco de dados, frameworks e APIs de terceiros.

```
┌─────────────────────────────────────────────┐
│                   Core                      │
│                                             │
│   Domain          UseCase         Ports     │
│  (Boleto,      (Calcula juros,   in / out   │
│  BoletoCalc.)   valida regras)              │
└────────────────────┬────────────────────────┘
                     │
        ┌────────────┴────────────┐
        ▼                         ▼
  Port IN (entrada)         Port OUT (saída)
  CalculoBoletoPort         ComplementoBoletoPort
  (interface da API REST)   (API externa de boletos)
                            SalvarCalculoBoletoPort
                            (banco de dados)
```

| Camada | Responsabilidade |
|--------|-----------------|
| `core/domain` | Entidades puras do negócio: `Boleto`, `BoletoCalculado`, enums |
| `core/usecase` | Orquestra as regras: valida, calcula e persiste |
| `core/port/in` | Interface de entrada — contrato do caso de uso |
| `core/port/out` | Interfaces de saída — dependências externas abstraídas |
| Adapters (a implementar) | Controllers REST, repositórios JPA, clients HTTP |

O `CalculoBoletoUseCase` conhece apenas as interfaces dos ports — nunca implementações concretas. Isso permite trocar o banco de dados, o framework web ou a API externa sem tocar na lógica de negócio.

---

## Stack Tecnológica

| Tecnologia | Versão |
|------------|--------|
| Java | 21 |
| Spring Boot | 4.0.6 |
| Spring Data JPA | — |
| PostgreSQL | — |
| Lombok | — |
| Maven | — |

---

## Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.9+
- PostgreSQL rodando localmente

### Configuração

Configure as variáveis de banco de dados em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jurosboleto
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Comandos

```bash
# Compilar e CalculoBoletoPort todos os testes
mvn clean install

# Subir a aplicação
mvn spring-boot:run

# Executar apenas os testes
mvn test

# Executar um teste específico
mvn -Dtest=NomeDaClasseTest test
```

---

## Objetivo de Estudo

Este projeto foi criado para praticar:

- Separação entre lógica de negócio e detalhes de infraestrutura
- Definição de contratos via interfaces (ports)
- Inversão de dependências aplicada na prática
- Organização de pacotes orientada ao domínio, não ao framework
