# Arquitetura do Projeto

Este documento descreve a arquitetura do projeto Api Delivery, incluindo a estrutura de software, as tecnologias utilizadas e os componentes.

## Visão Geral

O projeto Api Delivery é um sistema de delivery para restaurantes, clientes e entregadores que visa conectar clientes a restaurantes, restaurantes a clientes e entregadores e entregadores a restaurantes. O sistema é composto por controllers, services, repositorys e entitys.

## Estrutura de Software

A estrutura de software do projeto é baseada em camadas. Os principais componentes do sistema são:

* Controller: Camada de apresentação - componente responsável pela comunicação com o client.
* Service: Camada de serviço - componente responsável pelas regras de negócio.
* Repository - Camada de repositório - componente responsável pela comunicação com as entidades.
* Entity - Camada de entidade - componente responsável pela representação das tabelas no banco de dados.

## Tecnologias Utilizadas

As seguintes tecnologias são utilizadas no projeto:

* Spring Boot: Framework para desenvolvimento de aplicativos Java.
* Spring Data JPA: Framework para acesso ao banco de dados.
* H2: Banco de dados em memória.
* Spring Security: Framework para autenticação e autorização.
* JWT: Protocolo de autenticação e autorização baseado em JSON.
* JUnit: Framework para testes unitários.
* Mockito: Framework para testes de módulos.
* OpenAPI: Framework para documentação da API.
* Spring Boot Actuator: Framework para monitoramento e administração do sistema.
* Prometheus: Framework para monitoramento e administração do sistema.
* Grafana: Framework para monitoramento e administração do sistema.
* Docker: Gerenciador de containers.
* Docker Compose: Composição de containers.
* GitHub: Repositório de código.

## Componentes

Aqui está uma descrição detalhada dos componentes do sistema:

### Controller

* Descrição: Responsável pela comunicação com o client e processamento de solicitações HTTP.
* Tecnologias utilizadas: Spring Web, Validation.
* Interfaces: API REST.

### Service

* Descrição: Responsável pela regra de negócio e comunicação com o repositório.
* Tecnologias utilizadas: Spring Data JPA, Validation.
* Interfaces: API REST.

### Repository

* Descrição: Responsável pela comunicação com o banco de dados.
* Tecnologias utilizadas: Spring Data JPA.
* Interfaces: API REST.

### Entity

* Descrição: Representação das tabelas no banco de dados.
* Tecnologias utilizadas: Spring Data JPA.
* Interfaces: API REST.

## Relacionamentos entre Componentes

Aqui está uma descrição dos relacionamentos entre os componentes do sistema:

* Controller -> Service
* Service -> Repository
* Repository -> Entity

## Conclusão

A arquitetura do projeto Api Delivery é baseada na arquitetura em camadas e utiliza as tecnologias Spring Boot, Spring Data JPA, Spring Security, JWT, JUnit, Mockito, OpenAPI, Spring Boot Actuator, Prometheus, Grafana, Docker, Docker Compose e GitHub. Os componentes do sistema são Controller, Service, Repository e Entity e se relacionam entre si de acordo com o diagrama acima.