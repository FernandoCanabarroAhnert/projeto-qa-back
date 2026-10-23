# Sistema de Folha de Pagamento – Back-end

API REST do Sistema de Folha de Pagamento, desenvolvida no Trabalho Integrador da unidade curricular **Gestão e Qualidade de Software**.

A API cadastra colaboradores de três categorias, valida os dados de entrada, calcula automaticamente o salário final de cada um e gera os relatórios da folha de pagamento.

| Categoria | Cálculo do salário final |
|---|---|
| Padrão | Salário base |
| Comissionado | Salário base + (valor das vendas × percentual de comissão) |
| Produção | Salário base + (quantidade produzida × valor por unidade) |

O percentual de comissão é informado de 0 a 100 (10 = 10%).

## Equipe

- Eduardo Gladzik
- Fernando Canabarro
- Luan Ribeiro
- Pedro Marin

## Tecnologias

- Java 21
- Spring Boot 4 (Spring Web, Spring Data JPA)
- PostgreSQL 18, executado em Docker
- Maven (com Maven Wrapper)
- JasperReports, para os relatórios em PDF
- JUnit 5, Mockito e Spring Boot Test, para os testes
- JaCoCo, para a cobertura de testes

## Estrutura do projeto

```
src/main/java        código da aplicação (controladores, serviços, entidades e repositórios)
src/main/resources   configurações da aplicação e modelos de relatório
src/test/java        testes unitários e de integração
docker/              configuração do banco de dados PostgreSQL
pom.xml              dependências do projeto
```

## Como executar

Pré-requisitos: Java 21 e Docker instalados.

1. Suba o banco de dados a partir da pasta `docker`:

   ```bash
   cd docker
   docker compose up -d
   ```

2. Na raiz do projeto, inicie a aplicação:

   ```bash
   ./mvnw spring-boot:run        # Linux e macOS
   mvnw.cmd spring-boot:run      # Windows
   ```

3. A API fica disponível em `http://localhost:8080`.

## Testes

```bash
./mvnw test
```

O relatório de cobertura do JaCoCo é gerado em `target/site/jacoco/index.html`. A meta do projeto é de pelo menos 80% de cobertura.

## Padrões de contribuição

- Cada funcionalidade ou correção é feita em uma branch própria e entra na `main` por pull request revisado por outro integrante.
- Mensagens de commit no padrão `tipo: descrição`, por exemplo `feat: validar matrícula única`. Tipos: `feat`, `fix`, `test`, `docs` e `refactor`.
- Defeitos são registrados como issues, com severidade, responsável e caso de teste de origem.

## Repositórios relacionados

- Front-end: https://github.com/FernandoCanabarroAhnert/projeto-qa-front
