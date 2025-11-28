# Repository Guidelines

## Estrutura do Projeto e Modulos
- Monorepo minimal com foco no backend Spring Boot em `backend/`.
- Codigo fonte em `backend/src/main/java/com/domi/loja`: `config` (DynamoDbClient), `controller` (endpoints REST), `service` (regras de negocio), `repository` (DynamoDbProdutoRepository @Primary e InMemoryProdutoRepository), `domain` (Produto), `dto` (requests/responses) e `exception`.
- Artefatos gerados ficam em `backend/target/`; Dockerfile monta o jar `loja-cosmeticos-backend-0.0.1-SNAPSHOT.jar`.
- `docker-compose.yml` sobe LocalStack (DynamoDB) e o backend; dados locais ficam em `./localstack-data`.

## Comandos de Build, Teste e Desenvolvimento
- `cd backend && mvn clean package -DskipTests` — gera o jar em `target/`.
- `cd backend && mvn test` — roda a suite de testes JUnit/Spring (adicionar casos em `src/test/java`).
- `cd backend && mvn spring-boot:run` — inicia a API em dev com o profile padrao.
- `docker compose up --build backend` — sobe LocalStack e o backend integrados; configure `AWS_ENDPOINT=http://localstack:4566` para apontar para o simulador.

## Estilo de Codigo e Convencoes
- Java 17 e Spring Boot 3; indentacao de 4 espacos e imports ordenados.
- Fluxo tipico: Controller -> Service -> Repository; injecao por construtor.
- DTOs terminam com `Request`/`Response`; modelos de dominio em `domain` nao devem conter logica de infraestrutura.
- Validacoes com Bean Validation (Jakarta): use anotacoes como `@NotNull`, `@Min`; padronize mensagens claras.
- Nomes de endpoints em ingles simples (`/api/produtos`), usando HTTP verbs adequados; timestamps como `Instant`.

## Testes
- Framework: `spring-boot-starter-test` (JUnit 5, Mockito, MockMvc). Ainda nao ha testes: crie-os em `backend/src/test/java/com/domi/loja`.
- Nomeie classes de teste como `*Test` e foque em Service com repositorio in-memory para evitar dependencias de AWS.
- Para controller, use MockMvc cobrindo status e payload; para repositorio Dynamo, use LocalStack e limpe a tabela `produtos` entre casos.

## Commits e Pull Requests
- Prefira mensagens curtas em portugues, no presente e descritivas (ex.: `Adicionar validacao de estoque`, `Ajustar mapeamento Dynamo`). Evite commits mistos sem contexto.
- PRs devem incluir: resumo do objetivo, passos de teste executados (`mvn test`, `docker compose up backend`), impacto em APIs (novos endpoints ou campos), e links para issue se houver.

## Seguranca e Configuracao
- Nunca versionar segredos reais; use variaveis de ambiente (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_ENDPOINT`, `AWS_REGION`). Para cloud real, remova o endpoint de LocalStack e valide permissões.
- Tabela esperada: `produtos` no DynamoDB. Em desenvolvimento, prefira o repositorio in-memory se nao quiser depender de infraestrutura externa.
