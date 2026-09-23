# DeliveryNow API

[![CI](https://github.com/TiagoAReiz/DeliveryNow-Api/actions/workflows/ci.yml/badge.svg)](https://github.com/TiagoAReiz/DeliveryNow-Api/actions/workflows/ci.yml)

API REST para gerenciamento de entregas, desenvolvida em **Java + Spring Boot**, seguindo princípios de **Arquitetura Hexagonal (Ports & Adapters)**. Permite o cadastro de usuários, criação e acompanhamento de entregas, além do envio de comprovantes (fotos) armazenados em um blob storage compatível com Azure.

> **App mobile:** esta API é consumida pelo aplicativo do entregador [DeliveryNow-Mobile](https://github.com/TiagoAReiz/DeliveryNow-Mobile) (Expo / React Native), que faz login, lista e busca entregas e envia fotos de comprovante.

## Sumário

- [Tech Stack](#tech-stack)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Endpoints da API](#endpoints-da-api)
- [Como executar](#como-executar)
- [Testes](#testes)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Estrutura do projeto](#estrutura-do-projeto)

## Tech Stack

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Azure Blob Storage](https://img.shields.io/badge/Azure%20Blob%20Storage-Azurite-0078D4?logo=microsoftazure&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.9-C71A36?logo=apachemaven&logoColor=white)

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5.7 (Web, Data JPA, Security) |
| Banco de dados | PostgreSQL 16 |
| Armazenamento de arquivos | Azure Blob Storage (emulado localmente via Azurite) |
| Autenticação | JWT (Auth0 `java-jwt`) + BCrypt |
| Build | Maven |
| Boilerplate | Lombok |
| Containerização | Docker / Docker Compose |

## Arquitetura

O projeto segue uma **arquitetura hexagonal (ports & adapters)**, separando regras de negócio da infraestrutura:

```
DeliveryNow.Api
├── domain            # Núcleo do negócio: entidades, value objects e portas (interfaces)
│   ├── entities
│   ├── entities/enums
│   ├── entities/valueObjects
│   └── interfaces     # Ports (repositórios e serviços que a aplicação depende)
│
├── application        # Casos de uso, orquestração e DTOs
│   ├── useCases        # Interfaces dos casos de uso
│   ├── services         # Implementação dos casos de uso
│   ├── services/dtos     # Objetos de entrada/saída (records)
│   └── mappers            # Conversão entre entidades, DTOs e modelos JPA
│
└── infrastructure    # Adaptadores externos
    ├── adapters/in/controllers    # Adaptadores de entrada (REST controllers)
    ├── adapters/out/repositories  # Adaptadores de saída (JPA/Postgres)
    ├── adapters/out/external      # Adaptadores de saída (Azure Blob)
    └── config                     # Segurança, filtros JWT, configuração Spring
```

O domínio não depende de Spring, JPA ou Azure — essas dependências ficam isoladas na camada de `infrastructure`, que implementa as portas definidas em `domain/interfaces`.

## Funcionalidades

- **Autenticação e cadastro de usuários** com senha criptografada (BCrypt) e emissão de token JWT (HMAC256, expiração de 1h).
- **Segurança stateless** via filtro JWT: apenas `/login` e `/register` são públicos, todas as demais rotas exigem `Bearer token`.
- **CRUD de entregas** (criação, listagem, busca por id, atualização parcial).
- **Busca avançada de entregas** por nome, status ou qualquer campo do endereço (cidade, rua, CEP, país, estado), filtrada por usuário.
- **Atualização automática de status**: uma entrega `PENDING` é automaticamente marcada como `LATE` quando a data prevista de entrega já passou.
- **Envio de comprovante de entrega**: upload de imagem (multipart) associada a uma entrega, armazenada em Azure Blob Storage (Azurite em ambiente local), com geração de URL de acesso ao blob.
- **Consulta de comprovantes** por entrega.

## Endpoints da API

### Usuários (`/`)

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| POST | `/register` | Cria um novo usuário | Pública |
| POST | `/login` | Autentica o usuário e retorna um token JWT | Pública |

### Entregas (`/delivery`)

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| GET | `/delivery` | Lista todas as entregas | Requerida |
| GET | `/delivery/{id}` | Busca uma entrega pelo id (atualiza status para `LATE` se aplicável) | Requerida |
| GET | `/delivery/search?search=&status=&userId=` | Busca entregas por texto livre e/ou status, filtradas por usuário (`userId` obrigatório) | Requerida |
| POST | `/delivery` | Cria uma nova entrega | Requerida |
| PATCH | `/delivery/{id}` | Atualiza parcialmente uma entrega | Requerida |

### Comprovantes (`/receipt`)

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| POST | `/receipt/send/{deliveryId}` | Envia uma imagem (`multipart/form-data`, campo `image`) como comprovante de uma entrega | Requerida |
| GET | `/receipt/delivery/{deliveryId}` | Lista os comprovantes de uma entrega | Requerida |

> As rotas protegidas exigem o header `Authorization: Bearer <token>`, obtido via `/login`.

## Como executar

### Pré-requisitos

- Docker e Docker Compose
- (Opcional, para rodar fora de container) Java 17 e Maven

### Passo a passo (Docker Compose)

1. Clone o repositório:
   ```bash
   git clone git@github.com:TiagoAReiz/DeliveryNow-Api.git
   cd DeliveryNow-Api
   ```

2. Copie o arquivo de exemplo de variáveis de ambiente e ajuste os valores conforme necessário:
   ```bash
   cp .env.example .env
   ```

3. Suba os containers (API, Postgres e Azurite):
   ```bash
   docker compose up --build
   ```

4. A API estará disponível em `http://localhost:8080`. Para conferir se subiu:
   ```bash
   curl http://localhost:8080/actuator/health   # {"status":"UP"}
   ```

5. Teste rápido do fluxo (cadastro → login → rota protegida):
   ```bash
   curl -X POST http://localhost:8080/register -H 'Content-Type: application/json'      -d '{"email":"demo@deliverynow.dev","firstName":"Demo","lastName":"User","password":"demo123"}'

   curl -X POST http://localhost:8080/login -H 'Content-Type: application/json'      -d '{"email":"demo@deliverynow.dev","password":"demo123"}'
   # => {"token":"<jwt>","id":1}

   curl http://localhost:8080/delivery -H 'Authorization: Bearer <jwt>'
   ```

| Serviço | Porta |
|---|---|
| API (`deliverynow-api`) | 8080 |
| PostgreSQL (`postgres`) | 5432 |
| Azurite — Blob | 10000 |
| Azurite — Queue | 10001 |
| Azurite — Table | 10002 |

> **Azurite:** o `.env.example` já vem com a conta de desenvolvimento pública do Azurite (`devstoreaccount1` e sua chave [documentada pela Microsoft](https://learn.microsoft.com/azure/storage/common/storage-use-azurite#well-known-storage-account-and-key)). A chave precisa ser Base64 válido — se for trocada por um valor qualquer a API não inicia. Os dados do Azurite ficam no volume Docker `azurite-data` (fora do repositório).

> **Importante:** o arquivo `.env` nunca deve ser commitado. Use sempre `.env.example` como referência e mantenha suas credenciais locais fora do controle de versão.

### Rodando localmente sem Docker

Com um PostgreSQL disponível (e as variáveis de ambiente configuradas), é possível rodar a aplicação diretamente via Maven:

```bash
./mvnw spring-boot:run
```

Também é necessário um endpoint de Blob Storage. A forma mais simples é subir só o Azurite e o Postgres pelo Compose:

```bash
docker compose up -d postgres azurite
```

## Testes

```bash
./mvnw verify
```

A suíte cobre:

| Teste | O que valida |
|---|---|
| `TokenServiceTest` | Geração/validação de JWT, rejeição de token expirado, de outro emissor ou assinado com outra chave |
| `UserEntityServiceTest` | Senha salva com hash BCrypt, bloqueio de e-mail duplicado, login retornando token + id |
| `ReceiptServiceTest` | Upload da imagem e persistência do blob, geração de URLs assinadas (SAS) na listagem |
| `DeliveryRepositoryImplTest` | Regra de negócio `PENDING` → `LATE` após a data prevista, atualização parcial, filtros de busca |
| `UserEntityControllerTest` | `/register` e `/login` públicos, `409` para e-mail duplicado, `401` para credenciais inválidas |
| `DeliveryControllerTest` | Cadeia real de segurança (JWT): `403` sem token ou com token inválido, `404`, filtros de busca |
| `DeliveryNowApplicationTests` | Sobe o contexto completo do Spring (requer PostgreSQL e Azurite em execução) |

No GitHub Actions ([`ci.yml`](.github/workflows/ci.yml)) o `./mvnw verify` roda com PostgreSQL e Azurite como *service containers*, e um segundo job sobe o `docker compose` exatamente como descrito acima e faz um smoke test de health, cadastro e login.

## Variáveis de ambiente

As variáveis de ambiente são definidas em `.env` (a partir do template `.env.example`) e cobrem:

| Categoria | Variáveis |
|---|---|
| Banco de dados | `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`, `DB_HOST`, `DB_PORT` |
| Segurança | `APP_SECRET_KEY` (chave usada para assinar os tokens JWT) |
| Armazenamento (Azurite) | `STORAGE_ACCOUNT_NAME`, `STORAGE_ACCOUNT_KEY`, `STORAGE_BLOB_ENDPOINT` |
| Upload de arquivos | `MAX_FILE_SIZE`, `MAX_REQUEST_SIZE` |
| JPA/Hibernate | `HIBERNATE_DDL_AUTO`, `HIBERNATE_SHOW_SQL`, `HIBERNATE_FORMAT_SQL` |
| JVM (Docker) | `JAVA_OPTS` |

## Estrutura do projeto

```
DeliveryNow-Api/
├── src/
│   ├── main/
│   │   ├── java/DeliveryNow/Api/
│   │   │   ├── domain/            # Entidades, value objects e portas
│   │   │   ├── application/       # Casos de uso, serviços, DTOs e mappers
│   │   │   └── infrastructure/    # Controllers, repositórios JPA, Azure, security config
│   │   └── resources/
│   │       └── application.properties
│   └── test/               # Testes unitários, de controller (MockMvc) e de contexto
├── .github/workflows/   # CI (build, testes e smoke test do docker compose)
├── docker-compose.yml   # Orquestra API, PostgreSQL e Azurite
├── Dockerfile            # Build multi-stage (Maven -> JRE Alpine)
├── .env.example          # Template de variáveis de ambiente
└── pom.xml
```
