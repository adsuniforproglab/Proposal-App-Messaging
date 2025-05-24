# Proposta App

Este é um aplicativo de microsserviços para gerenciar propostas financeiras com suporte a mensagens.

## Sumário

- [Proposta App](#proposta-app)
  - [Sumário](#sumário)
  - [Introdução](#introdução)
  - [Funcionalidades](#funcionalidades)
  - [Tecnologias](#tecnologias)
  - [Arquitetura](#arquitetura)
  - [Instalação](#instalação)
  - [Uso](#uso)
  - [Documentação da API](#documentação-da-api)
  - [Contribuindo](#contribuindo)

## Introdução

O Proposta App é um aplicativo robusto baseado em microsserviços para criar e gerenciar propostas financeiras. Ele se integra com outros serviços através de mensagens RabbitMQ e fornece atualizações em tempo real via WebSockets.

## Funcionalidades

- Crie e gerencie propostas financeiras
- Integração de análise de crédito com sistema de fila de prioridade
- Notificações em tempo real via WebSockets
- API RESTful com documentação detalhada
- Tratamento de erros robusto e validação
- Mecanismo de nova tentativa automática para entrega de mensagens falhas

## Tecnologias

As seguintes tecnologias são utilizadas neste projeto:

- Java 21
- Spring Boot 3.4
- Spring AMQP (RabbitMQ)
- Spring WebSocket
- Spring Data JPA
- PostgreSQL
- Docker e Docker Compose
- Swagger/OpenAPI para documentação da API

## Arquitetura

O aplicativo segue uma arquitetura de microsserviços:

1. **Proposta App** (este serviço) - Gerencia propostas e atua como ponto de entrada para usuários
2. **Análise de Crédito App** - Processa propostas e aprova/nega com base em critérios financeiros
3. **Notificação App** - Envia notificações aos usuários sobre mudanças no status da proposta

A comunicação entre os serviços é gerenciada via trocas de mensagens e filas RabbitMQ, com WebSockets fornecendo atualizações em tempo real para os clientes.

## Instalação

Para executar o Proposta App localmente, siga estes passos:

1. Clone o repositório:

    ```shell
    git clone [https://github.com/leonardomeirels55/proposal-app.git](https://github.com/leonardomeirels55/proposal-app.git)
    ```

2. Configure as propriedades da aplicação:

    Abra o arquivo `application.properties` localizado em `src/main/resources` e atualize os detalhes de conexão do banco de dados e a configuração do RabbitMQ de acordo com o seu ambiente ou variáveis de ambiente no `docker-compose`.

3. Execute a aplicação:

    ```shell
    docker compose up
    ```

    Isso iniciará todos os serviços necessários (PostgreSQL, RabbitMQ e os microsserviços).

## Uso

Uma vez que a aplicação esteja em execução, você pode acessar os endpoints da API usando uma ferramenta como Postman ou cURL. Aqui estão alguns exemplos de requisições:

- Criar uma nova proposta:
    ```http
    POST /api/v1/proposals
    Content-Type: application/json
    ```
    ```json
    {
      "name": "Leonardo",
      "lastName": "Meireles",
      "telephone": "5599999999",
      "cpf": "111.111.111-11",
      "financialIncome": 5000.0,
      "proposalValue": 10000.0,
      "paymentTerm": 24
    }
    ```

- Obter todas as propostas:
    ```http
    GET /api/v1/proposals
    ```

- Obter uma proposta específica:
    ```http
    GET /api/v1/proposals/{id}
    ```

## Documentação da API

A documentação da API está disponível via Swagger UI em:

http://localhost:8080/api/v1/swagger-ui.html


## Contribuindo

Contribuições são bem-vindas! Se você tiver alguma ideia, sugestão ou relatório de bug, por favor, abra uma issue ou envie um pull request.
