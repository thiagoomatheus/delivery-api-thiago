# API

## Controllers

### UsuarioController

* **GET /usuarios**: Lista todos os usuários
* **POST /usuarios**: Cria um novo usuário
* **GET /usuarios/{id}**: Busca um usuário por ID
* **PUT /usuarios/{id}**: Atualiza um usuário
* **DELETE /usuarios/{id}**: Deleta um usuário

### ProdutoController

* **GET /produtos**: Lista todos os produtos
* **POST /produtos**: Cria um novo produto
* **GET /produtos/{id}**: Busca um produto por ID
* **PUT /produtos/{id}**: Atualiza um produto
* **DELETE /produtos/{id}**: Deleta um produto

### ClienteController

* **GET /clientes**: Lista todos os clientes
* **POST /clientes**: Cria um novo cliente
* **GET /clientes/{id}**: Busca um cliente por ID
* **PUT /clientes/{id}**: Atualiza um cliente
* **DELETE /clientes/{id}**: Deleta um cliente

### AuthController

* **POST /login**: Realiza login
* **POST /register**: Realiza cadastro

### RestauranteController

* **GET /restaurantes**: Lista todos os restaurantes
* **POST /restaurantes**: Cria um novo restaurante
* **GET /restaurantes/{id}**: Busca um restaurante por ID
* **PUT /restaurantes/{id}**: Atualiza um restaurante
* **DELETE /restaurantes/{id}**: Deleta um restaurante

### PedidoController

* **POST /pedidos**: Cria um novo pedido
* **GET /pedidos/{id}**: Busca um pedido por ID
* **PUT /pedidos/{id}/status**: Atualiza o status de um pedido
* **DELETE /pedidos/{id}**: Cancela um pedido

### HealthController

* **GET /health**: Verifica a saúde da aplicação
* **GET /info**: Informações sobre a aplicação