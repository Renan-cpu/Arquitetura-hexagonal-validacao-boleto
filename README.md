# Juros Boleto API

Projeto de **estudo** de **Arquitetura Hexagonal (Ports and Adapters)** com Spring Boot. A aplicação expõe uma API REST que recebe um código de boleto vencido, consulta os dados em uma API externa mockada (Beeceptor), calcula os juros proporcionais aos dias de atraso e persiste o resultado em banco de dados PostgreSQL.

---

## Problema / Estória de Usuário

> **Enquanto** usuário da API,
> **quero** digitar um código de boleto vencido,
> **e** receber o valor dos juros calculados.

---

## Arquitetura Hexagonal

O projeto implementa o padrão **Hexagonal Architecture (Ports and Adapters)**, cujo objetivo é isolar a lógica de negócio de detalhes externos como banco de dados, frameworks e APIs de terceiros. O núcleo (`core`) conhece apenas interfaces — nunca implementações concretas.

```
┌─────────────────────────────────────────────────────────────┐
│                          CORE                               │
│                                                             │
│   domain/              usecase/              port/          │
│  (Boleto,           (CalcularBoleto:        in / out        │
│  BoletoCalculado,    valida, calcula,      (interfaces)     │
│  enums, exception)   persiste)                              │
└──────────────┬──────────────────────────────┬──────────────┘
               │                              │
     ┌─────────▼──────────┐        ┌──────────▼──────────────┐
     │   Adapter IN        │        │     Adapters OUT         │
     │                     │        │                          │
     │  CalculoBoleto-     │        │  ComplementoBoleto-      │
     │  Controller         │        │  Integration             │
     │  (REST / HTTP)      │        │  (Feign → Beeceptor)     │
     │                     │        │                          │
     │                     │        │  SalvarBoletoCalculado   │
     │                     │        │  (JPA → PostgreSQL)      │
     └─────────────────────┘        └──────────────────────────┘
```

### Estrutura de pacotes

| Pacote | Responsabilidade |
|--------|-----------------|
| `core/domain` | Entidades puras: `Boleto`, `BoletoCalculado`, `TipoBoleto`, `TipoExecao` |
| `core/usecase` | `CalcularBoleto` — orquestra validação, cálculo e persistência |
| `core/port/in` | `CalculoBoletoPort` — contrato de entrada do caso de uso |
| `core/port/out` | `ComplementoBoletoPort` (API externa) e `SalvarCalculoBoletoPort` (BD) |
| `adapter/http` | Controller REST, DTOs, `@ControllerAdvice` de exceções |
| `adapter/datasource/integration` | Feign Client que consome a API do Beeceptor |
| `adapter/datasource/database` | Entidade JPA, Repository e adapter de persistência |

---

## API Mock — Beeceptor

A API externa de consulta de boletos é simulada com **Beeceptor**. A URL base está configurada em `application.properties`:

```properties
api.boleto=https://boleto.free.beeceptor.com/my/api/boleto
```

A aplicação faz `GET /{codigo}` nessa URL para buscar os dados do boleto antes de calcular os juros.

### Códigos de teste disponíveis

| Código | Tipo | Vencimento | Cenário esperado |
|--------|------|-----------|-----------------|
| `123456789` | `XPTO` | 2026-05-15 | **Sucesso** — boleto vencido, juros calculados |
| `123` | `NORMAL` | 2026-05-17 | **Erro** — tipo de boleto inválido |
| `1234` | `XPTO` | 2027-07-17 | **Erro** — boleto ainda não vencido |

**Resposta do Beeceptor para cada código:**

```json
// GET /123456789 — caso de sucesso
{
  "codigo": "123456789",
  "data_vencimento": "2026-05-15",
  "valor": 100,
  "tipo": "XPTO"
}

// GET /123 — tipo inválido
{
  "codigo": "123",
  "data_vencimento": "2026-05-17",
  "valor": 50,
  "tipo": "NORMAL"
}

// GET /1234 — boleto não vencido
{
  "codigo": "1234",
  "data_vencimento": "2027-07-17",
  "valor": 500,
  "tipo": "XPTO"
}
```

---

## Endpoints REST

### `POST /api/boleto/calcular`

Calcula os juros de um boleto vencido.

**Request body:**
```json
{
  "codigo": "123456789",
  "data_pagamento": "2026-05-24"
}
```

**Response `200 OK` — sucesso:**
```json
{
  "codigo": "123456789",
  "data_vencimento": "2026-05-15",
  "valor": 100.30,
  "tipo": "XPTO"
}
```

**Response `400 Bad Request` — erro de negócio:**
```json
{
  "mensagens": ["Tipo do boleto inválido, informe um boleto do tipo XPTO"],
  "erro": "tipo_boleto_invalido",
  "codigo": 400,
  "timestamp": "2026-05-24T10:00:00.000+00:00",
  "path": "uri=/api/boleto/calcular"
}
```

### Erros de negócio mapeados

| Código Beeceptor | Campo `erro` | Mensagem |
|-----------------|--------------|---------|
| `123` | `tipo_boleto_invalido` | Tipo do boleto inválido, informe um boleto do tipo XPTO |
| `1234` | `boleto_nao_vencido` | O boleto informado ainda não está vencido |
| código inexistente | `boleto_invalido` | O boleto encontrado é inválido |

### Swagger UI

Com a aplicação rodando, acesse a documentação interativa em:

```
http://localhost:8080/swagger-ui.html
```

---

## Regras de Negócio e Validações

O use case `CalcularBoleto` aplica as seguintes validações, nessa ordem:

1. **Boleto não pode ser nulo** → `boleto_invalido`
2. **Tipo deve ser `XPTO`** (não `NORMAL`) → `tipo_boleto_invalido`
3. **Data de vencimento deve ser anterior a hoje** → `boleto_nao_vencido`

---

## Cálculo dos Juros

A taxa de juros segue a regra de boleto bancário: **1% ao mês**, calculada de forma proporcional aos dias de atraso.

```
Taxa diária = 1% / 30 ≈ 0,033% ao dia

Juros = valor original × 0,033% × dias de atraso

Valor final = valor original + juros
```

Os valores são calculados com `BigDecimal` usando arredondamento `HALF_EVEN` com 2 casas decimais.

**Exemplo com o boleto `123456789` (pago em 2026-05-24):**
- Valor original: R$ 100,00
- Vencimento: 2026-05-15 → 9 dias de atraso
- Juros = 100 × 0,00033 × 9 = **R$ 0,30**
- Valor final = **R$ 100,30**

---

## Stack Tecnológica

| Tecnologia | Versão |
|------------|--------|
| Java | 21 |
| Spring Boot | 3.2.12 |
| Spring Cloud OpenFeign | 2023.0.4 |
| Spring Data JPA | — |
| PostgreSQL | — |
| MapStruct | 1.6.3 |
| Lombok | 1.18.36 |
| springdoc-openapi | 2.5.0 |
| Maven | — |

---

## Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.9+
- PostgreSQL rodando localmente

### Configuração do banco de dados

Configure as credenciais em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jurosboleto
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Comandos

```bash
# Compilar e executar todos os testes
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
- Consumo de API externa com Spring Cloud OpenFeign
- Mapeamento entre camadas com MapStruct
- Tratamento centralizado de exceções com `@ControllerAdvice`
- Organização de pacotes orientada ao domínio, não ao framework
