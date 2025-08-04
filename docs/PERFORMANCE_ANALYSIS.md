# ANALISES DE PERFOMANCE

Teste de carga realizados com JMeter em 4 cenários diferentes, sendo eles, com 100, 200, 500 e 1000 acessos simultâneos, com cada usuário fazendo apenas 1 interação.

Os endpoints testados foram os seguintes:
- **POST: /api/auth/login - Login**
- **GET:  /api/restaurantes - Listagem de restaurantes**
- **POST: /api/pedidos - Cadastro de pedido**
- **GET:  /api/pedidos/{id} - Busca de pedido por id**

Os cenários testados foram os seguintes:
- **Cenario 1: Login - 100 acessos**
- **Cenario 2: Login e listagem de restaurantes - 500 acessos**
- **Cenario 3: Login e cadastro de pedido - 200 acessos**
- **Cenario 4: Login e busca de pedido por id - 300 acessos**

## Resultados

### Cenario 1 - Login - 100 acessos

Link para o relatório: [html](../jmeter/test-results/HTML/login-simultaneos/index.html)

Percebe-se uma perfomance muito positiva, sendo que 100% das operação retornaram SUCESSO. O tempo médio de resposta ficou na faixa dos 300ms, sendo que seu tempo máximo foi de 535ms. Olhando para o indicador Apdex, podemos observar a pontuação de 0.990, o que indica uma excelente experiência para o usuário.

### Cenario 2 - Login e listagem de restaurantes - 500 acessos

Link para o relatório: [html](../jmeter/test-results/HTML/listagem-restaurantes/index.html)

Neste cenário obtivemos uma experiência negativa para o usuário, apesar de que 100% das operações retornaram SUCESSO. No entanto, o tempo médio de resposta ficou na faixa dos 9000ms, sendo que seu tempo máximo foi de ~18000ms. Há de se considerar que com um número maior de restaurantes listados, o tempo médio de resposta pode aumentar.

### Cenario 3 - Login e cadastro de pedido - 200 acessos

Link para o relatório: [html](../jmeter/test-results/HTML/cadastro-pedido/index.html)

O teste deste cenário apresentou um tempo de resposta médio na faixa dos 1050ms, sendo que o tempo máximo foi abaixo dos 3000ms. O indicador Apdex ficou próximo da pontuação média, indicando uma experiência relativamente positiva para o usuário. Olhando para o gráfico de "Reponse Time Overview", vemos que a maior parte do tempo de resposta ficou abaixo dos 1500ms.

### Cenario 4 - Login e busca de pedido por id - 300 acessos

Link para o relatório: [html](../jmeter/test-results/HTML/busca-pedido/index.html)

Neste cenário também obtivemos uma perfomance que trás uma experiência negativa para o usuário. O tempo médio de resposta ficou na faixa dos 4000ms, sendo que seu tempo máximo foi na faixa dos 11000ms. Algo que é perceptível é o quanto o temos um salto no tempo de resposta a partir de 50% da conclusão do teste, sendo que pode-se observar um salto mais acentuado a partir de 90% do teste.

## Melhorias

A implantação de cache com Redis poderia trazer uma melhoria considerável a perfomance dos testes, já que dessa forma, as operações de listagem de restaurantes e busca de pedido por id, operações estas que tendem a ser a que apresentem mais gargalo pela quantidade de dados, seriam beneficiadas por não ter fazer uma busca no banco de dados todas as operações.

Alem disso, podemos implementar um sistema de load balancer, onde se o sistema estiver sobrecarregado, ele poderia distribuir as operações para outros servidores, aumentando assim a performance.