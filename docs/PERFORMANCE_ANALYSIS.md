# ANALISES DE PERFOMANCE

Teste de carga realizados com JMeter em 4 cenários diferentes, sendo eles, com 100, 200, 500 e 1000 acessos simultâneos, com cada usuário fazendo apenas 1 interação.

Os endpoints testados foram os seguintes:
- **POST: /api/auth/login - Login**
- **GET:  /api/restaurantes - Listagem de restaurantes**
- **POST: /api/pedidos - Cadastro de pedido**
- **GET:  /api/pedidos/{id} - Busca de pedido por id**

Com 100 acessos simultâneos percebe-se uma perfomance muito positiva, sendo que 100% das operação retornaram SUCESSO. O tempo médio das operações ficou abaixo dos 100ms para três operações: listagem de restaurantes, cadastro de pedido e busca de pedido por id, enquanto, a operação de login obteve um tempo médio de resposta próximo dos 400ms.

Outro ponto de destaque para este cenário foi a indicador Apdex, o qual obeteve pontuação máxima em todas as operações, com exceção da operação de login, que obteve pontuação de 0.998, ou seja, muito próximo do máximo.

O cenário com 200 acessos simultâneos também obeteve 100% de SUCESSO nas requisições enviadas, porém, já mostrou indícios de sobrecarga. O tempo médio de resposta, que com 100 acessos não havia passado dos 100ms e 400ms, agora mostrou um salto considerável, ficando na faixa dos 700ms a 1050ms, dependendo da operação. Outro ponto de atenção se dá ao tempo máximo de resposta, em especial nas operações de listagem de restaurantes, cadastro de pedido e busca de pedido por id, já que passaram da faixa de 400 ms no teste com 100 acessos para 3000ms a 4000ms com 200 acessos simultâneos.

Entrando no cenário com 500 acessos simultâneos a sobrecarga fica bem clara, sendo que já se registra 25 erros na operação de consulta de pedido. Apesar do número de erros ser pequeno em relação da quantidade de operações realizadas, indicadores como tempo de resposta médio, tempo de resposta máximo e Apdex mostram uma experiência negativa para o usuário. Contata-se também uma dificuldade maior na operação de consulta de pedido, já que, com a operação de cadastro de pedido sendo realizada, obtemos mais dados nesta tambela no banco.

No cenário de 1000 acessos simultâneos percebe-se um gargalo muito evidente. As operações que registram erros representam 13,1% do total, sendo a operação de consulta de pedido por id a mais afetada. A operação de cadastro de pedido também passa a registrar erro neste cenário e tem performance bem negativa. Apesar das operações listagem de restaurantes e login não registrarem erro vemos uma experiência ruim, chegando a registrar tempo médio de resposta próximo entre 18ms e 26ms.

Em conclusão geral, percebe-se que a operação de login foi a mais estável e foi o que menos sofreu alterações com a alta carga. A operação de cadastro de pedido e busca de pedido por id foram as que mais sofreram alterações e fica claro o quanto o maior número de dados pode afetar a perfomance. Isso coloca a operação de listagem de restaurantes em evidência, já que, por contar com alguns poucos restaurantes cadastrados, performou junto com a média, no entanto, caso houvesse um banco mais populado poderia sofrer com a performance.

A implantação de cache com Redis poderia trazer uma melhoria considerável a perfomance dos testes, já que dessa forma, as operações de listagem de restaurantes e busca de pedido por id, operações estas que tendem a ser a que apresentem mais gargalo pela quantidade de dados, seriam beneficiadas por não ter fazer uma busca no banco de dados todas as operações.