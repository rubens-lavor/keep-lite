# keep-lite API

Este projeto é o artefato prático desenvolvido para o Trabalho de Conclusão de Curso (TCC) do curso de Ciência e Tecnologia da UFSC.

**Título do Trabalho:** Análise de Performance e Segurança em APIs RESTful: Um Estudo de Caso sobre Autenticação Stateless com JWT e Estratégias de Cache com Redis.

A `keep-lite` é uma API RESTful para gerenciamento de notas pessoais, projetada para demonstrar na prática conceitos de arquitetura hexagonal, segurança *stateless* robusta e otimização de performance com *caching*.

## 🚀 Tecnologias Utilizadas

  * **Linguagem:** Kotlin (JVM 21)
  * **Framework:** Spring Boot 3
  * **Banco de Dados:** MongoDB (NoSQL / Orientado a Documentos)
  * **Cache & Segurança:** Redis (In-Memory Data Store)
  * **Autenticação:** Spring Security + JWT (Access Token) + Refresh Token (Cookie HttpOnly) + Deny-list (Redis)
  * **Infraestrutura:** Docker & Docker Compose
  * **Testes de Carga:** Grafana k6

## 📋 Pré-requisitos

Para executar a aplicação e os testes, você precisará ter instalado em sua máquina:

  * [Docker](https://www.docker.com/get-started) & Docker Compose
  * [k6](https://k6.io/docs/get-started/installation/) (Apenas para rodar os scripts de teste de carga)

## ⚡ Como Rodar a Aplicação

A aplicação foi totalmente conteinerizada para facilitar a execução.

1.  **Clone o repositório:**

    ```bash
    git clone https://github.com/seu-usuario/keep-lite.git
    cd keep-lite
    ```

2.  **Suba o ambiente com Docker Compose:**
    Este comando irá construir a imagem da API e subir os contêineres do MongoDB, Redis e da Aplicação.

    ```bash
    sudo docker compose up --build -d
    ```

3.  **Verifique se está rodando:**
    Aguarde alguns instantes e verifique o *Health Check*:

      * URL: [http://localhost:8080/actuator/health](https://www.google.com/search?q=http://localhost:8080/actuator/health)
      * Resposta esperada: `{"status":"UP"}`

## 📚 Documentação da API (Swagger)

A documentação interativa dos endpoints (OpenAPI) está disponível e configurada com suporte a autenticação JWT.

  * **Acesse:** [http://localhost:8080/swagger-ui.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)

> **Nota:** Para testar endpoints protegidos no Swagger, você deve primeiro realizar o login no endpoint `/auth/login`, copiar o `accessToken` da resposta e inseri-lo no botão **Authorize** (formato: apenas o token, sem o prefixo "Bearer").

## 🧪 Executando os Experimentos (Testes de Carga)

O projeto inclui scripts k6 para simular carga e validar a performance da API. Os scripts estão localizados na pasta `k6-scripts/`.

### 1\. Preparação da Massa de Dados (Seeding)

Antes de rodar o teste de carga, é necessário popular o banco com usuários e notas.

**Passo A: Criar Usuários**
Gera 20 usuários de teste e salva as credenciais em um arquivo JSON.

```bash
# Execute na raiz do projeto
k6 run k6-scripts/seed-users.js
```

*Importante:* O arquivo `users.json` foi criado na raiz e contém o array de usuários.

**Passo B: Criar Notas**
Usa os usuários criados para gerar 50 notas para cada um (Total: 6.000 notas).

```bash
k6 run k6-scripts/seed-notes.js
```

### 2\. Rodar o Teste de Leitura Intensiva

Este é o cenário principal do TCC, simulando 120 usuários virtuais lendo notas simultaneamente.

```bash
k6 run k6-scripts/load-test-read-intensive.js
```

## 🏗️ Arquitetura e Design

O projeto segue a **Arquitetura Hexagonal (Ports and Adapters)** para garantir o desacoplamento entre a lógica de negócio e a infraestrutura.

  * `domain`: Núcleo da aplicação. Contém Entidades, Models e Interfaces (Ports). Não depende de nenhum framework.
  * `application`: Camada de orquestração. Contém os Services (implementação dos casos de uso) e os Controllers REST.
  * `infrastructure`: Detalhes técnicos. Contém as configurações de Spring, implementação dos Repositórios (MongoDB), Segurança (JWT) e Cache (Redis).

### Segurança

  * **Access Token:** JWT de curta duração.
  * **Refresh Token:** Armazenado em Cookie `HttpOnly` e `Secure` para prevenir XSS.
  * **Logout:** Implementado via *Deny-list* no Redis com TTL sincronizado ao JWT.

### Performance

  * **Cache-Aside:** Implementado com `@Cacheable` sobre o Redis para a listagem de notas.

-----

Desenvolvido por **Rubens Lavor**.