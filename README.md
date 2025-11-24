# itau-challenge

API de Consulta de Saldo e Transferência (Java / Spring Boot). Infra e mocks incluídos para rodar localmente ou via `docker-compose`.

## Estrutura do repositório
- `bank-api` \- API Spring Boot (código, testes, `application.yaml`)
- `mocks` \- `cadastro-mock`, `bacen-mock` (APIs fake)
- `redis` \- serviço Redis via `docker-compose`
- `postgres` \- DB init em `db/init`
- `localstack` \- mock AWS (SQS)
- `db-reset` \- exemplo de serviço para reset diário de limites
- `nginx.conf` \- usado apenas para expor e visualizar o desafio (opcional)
- `docker-compose.yml` \- orquestra os serviços

Arquivos importantes: `bank-api/src/main/resources/application.yaml` e `docker-compose.yml`.

## Serviços obrigatórios
Os serviços necessários:
- `cadastro-mock` (mock da API de cadastro)
- `bacen-mock` (mock do BACEN)
- `redis`
- `postgres`
- `localstack` (SQS)
- `bank-api-1` (instância principal da API)

Observações:
- `nginx` é apenas para visualização (opcional).
- `db-reset` é um exemplo didático de serviço que poderia resetar limites diariamente.

## Como subir a infra com Docker Compose

# Opção A — subir tudo (inclui `bank-api` container):
```bash
docker compose up -d --build
```

# Opção B — infra via Docker, app via IDE (recomendado para debug)

## Passo a passo

### 1) Subir apenas infra e mocks
Execute:
```bash
docker compose up -d --build cadastro-mock bacen-mock redis postgres localstack
```

### 2) Abrir e executar o bank-api na IDE

Pelo Maven:
```bash
mvn spring-boot:run
```

---

## Endpoints & health

Base:
```
http://localhost:8003/bank-api
```

Health / metrics:
- Health: `http://localhost:8003/bank-api/actuator/health`
- Prometheus: `http://localhost:8003/bank-api/actuator/prometheus`

Exemplos de API:

- Consultar Conta/Saldo:
  ```
  GET /bank-api/accounts/{accountId}
  ```

- Transferência:
  ```
  POST /bank-api/transfers
  Content-Type: application/json
  Body: 
  {
    "fromAccountId": "db5ab4c8-6a6b-4bd5-a20c-919793e128d3",
    "toAccountNumber": "0002",
    "amount": 10
  }
  ```

---

## Testes

- Testes unitários localizados em `bank-api/src/test/java`.
- Rodar testes:
  ```bash
  cd bank-api
  mvn test
  ```

---

## Como resolvi o desafio

Objetivo: Consulta de Saldo e Transferência com resiliência, alta disponibilidade e metas de desempenho (6k TPS, <100ms).

Pontos-chave:
- Consulta de cliente via `cadastro-mock`.
- Validações encadeadas (Chain of Responsibility):
  - Conta ativa;
  - Limite disponível;
  - Limite diário (configurável em `bank.daily-limit`, default `1000`).
- Notificação ao BACEN:
  - Chamada síncrona protegida por `resilience4j` (retry + circuit breaker).
  - Em caso de throttling (`429`) ou falha persistente, a notificação é enfileirada em SQS (localstack) para processamento assíncrono.
- Padrões usados: Chain of Responsibility (validators); princípios SOLID e Clean Architecture.
- Testes unitários cobrindo validadores e fluxos principais.
- Cache: Redis para leituras frequentes e redução de latência.
- Pool de conexões Hikari e timeouts configurados para alta concorrência.

---

## Repositories de consulta — queries nativas e FOR UPDATE

Para evitar problemas de concorrencia durante transferências, nas consultas críticas (flow de débito/crédito) foi utilizado queries nativas com FOR UPDATE para obter locks pessimistas nas linhas envolvidas.

---

## Observações finais

- `nginx` no `docker-compose` serve apenas para expor via porta `8003` para demonstração; não é obrigatório.
- `db-reset` é exemplo de tarefa agendada/serviço para reset diário de limites (apenas demonstração).
- Recomenda-se: subir infra via Docker e executar `bank-api` localmente na IDE para desenvolvimento e debugging.
