
# Projeto Final Bootcamp

O objetivo deste projeto final é implementar uma API REST no âmbito do slogan e aplicar
os conteúdos trabalhados durante o BOOTCAMP MELI.


## Desafio: Requisitos e Apresentação
O Seguinte desafio foi proposto para aplicar os conhecimentos até então alcançados no BootcampIt - Meli. A descrição do desafio e seus respectivos requisitos se encontram abaixo: [Enunciado Base](https://github.com/buskari/projeto-integrador/blob/develop/requisitos/Enunciado%20Base.pdf), [Requisito 1](https://github.com/buskari/projeto-integrador/blob/develop/requisitos/Requisito_1.pdf),  [Requisito 2](https://github.com/buskari/projeto-integrador/blob/develop/requisitos/Requisito_2.pdf), [Requisito 3](https://github.com/buskari/projeto-integrador/blob/develop/requisitos/Requisito_3.pdf), [Requisito 4](https://github.com/buskari/projeto-integrador/blob/develop/requisitos/Requisito_4.pdf), [Requisito 5](https://github.com/buskari/projeto-integrador/blob/develop/requisitos/Requisito_5.pdf).




## Endpoints
Arquivo de rotas do Postman: [postman_collection.json](https://github.com/buskari/projeto-integrador/blob/develop/postman/Projeto%20Integrador.postman_collection.json)

URL Base: localhost:8080/api/v1/fresh-products

| Tipo   | URI       | Função    | Payload |
| :---------- | :--------- | :----------------------- |:------------------- |
| `POST` | `/inboundorder` | Cadastra um lote | {"sector":{"sectorCode":1,"warehouseCode":1},"batchStock":[{"initialQuantity":100,"currentQuantity": 99,"initialTemperature":2,"currentTemperature": 2,"manufacturingDate":"2022-10-10","manufacturingTime":"20:20:20","dueDate": "2025-10-10","productId": 1}]}|
| `PUT` | `/inboundorder/{id}` | Altera um lote existente | {"sector":{"sectorCode":1,"warehouseCode":1},"batchStock":[{"initialQuantity":100,"currentQuantity": 99,"initialTemperature":2,"currentTemperature": 2,"manufacturingDate":"2022-10-10","manufacturingTime":"20:20:20","dueDate": "2025-10-10","productId": 1}]}|
| `GET` | `/products` | Busca todos os produtos | --|
| `GET` | `/products/{id}` | Busca produto por id | --|
| `GET` | `/products/list/{id}/?orderBy=F` | Veja uma lista de produtos ordenados com todos os lotes onde aparece (L = Lote, C = quantidade atual, F = data de vencimento)| --|
| `GET` | `products/category/{category}` | Busca produtos por categoria (FRESCOS, REFRIGERADOS, CONGELADOS) | --|
| `POST` | `/products` | Cadastra um produdo |{"name":"Asinha de frango","volume":10,"price":11.0,"category":"FRESCOS","seller":{"id": 1}}|
| `POST` | `/orders` | Registre um pedido com a lista de produtos que compõem o PurchaseOrder. |{"buyer":{"id":1},"status": "aberto","cart":[{"products":{"id": 1},"quantity": 5}]}|
| `GET` | `/orders?id=1` | Mostrar produtos no pedido | --|
| `PUT` | `/orders?id=1` | Modifica o pedido existente para torná-lo do tipo de carrinho ABERTO/FINALIZADO | --|
| `POST` | `/warehouse` | Cadastra um armazém |{"name": "embu","regiao": "Zona Sul SP"}|
| `GET` | `/warehouse/products?productId=1` | Obtenha a quantidade total de produtos por armazém | --|
| `GET` | `/warehouse/{id}` | Obtenha um armazém pelo id|-- |
| `GET` | `/batchstock/duedate?days=600&sectorId=1` | Obtenha uma lista de lotes dentro do prazo de validade solicitado, que pertencem a uma determinada categoria de produto (FS = FRESCOS, RF = REFRIGERADO, FF = CONGELADO)|-- |

[Clique aqui](https://github.com/buskari/projeto-integrador/tree/develop/script-sql) para acessar o script para popular o banco de dados.
* Usuário do tipo Buyer é o único que cadastra "/orders"
* Usuário do tipo Seller é o único que cadastra "/products"
* Usuário do tipo Supervisor é o único que cadastra "/inboundorder"


## Autores

- [Andris Buscariolli](https://github.com/buskari)
- [Geovanna Mendes](https://github.com/GeovannaSMendes)
- [João Barcelos](https://github.com/jbcoutinho)
- [Juliana Brito](https://github.com/Juliana27)
- [Melissa Amorim](https://github.com/amorimmel)
- [Renato Graça](https://github.com/renatograca)
- [Vinícios Fraga](https://github.com/itIsV)

