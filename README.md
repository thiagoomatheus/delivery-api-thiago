# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como executar

### Máquina

1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acesse: http://localhost:8080/health

### Container

1. **Pré-requisitos:** Docker instalado
2. Clone o repositório
3. Execute: `docker compose up`
4. Acesse: http://localhost:8080/health

### Container para testes

1. **Pré-requisitos:** Docker instalado
2. Clone o repositório
3. Execute: `docker compose -f compose.testes.yml up`
4. Acesse: http://localhost:8085/health

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

## 📚 Documentação

Após executar o projeto, a documentação pode ser acessada em:

- [Swagger](http://localhost:8080/swagger-ui/index.html)

## 📝 Testes

Os testes podem ser executados com o comando `./mvnw test`.

Ou se estiver utilizando o Docker, execute o comando `docker compose -f compose.testes.yml up`.

Em seguida, execute o comando `mvn test` para realizar os testes.

Se desejar gerar o relatório de cobertura, execute o comando `mvn jacoco:report`.

## 👨‍💻 Desenvolvedor
Thiago Oliveira Matheus - Arquitetura de Sistemas (TARDE) 
Desenvolvido com JDK 21 e Spring Boot 3.2.x