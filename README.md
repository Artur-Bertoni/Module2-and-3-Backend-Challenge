# Desafio South Impulsionar dos Módulos 2 e 3
Desafio da trilha Impulsionar 2.0 - Backend

## Objetivo
O objetivo desse desafio era de alterar o [Desafio do Módulo 2](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/feature/desafio-2) para adicionar novas bibliotecas e conectividade com sistema de filas.  
O contexto apresentado inicialmente para a criação da aplicação foi o de criar um aplicativo de uma loja simples, com as funções de adicionar, editar, excluir e importar (através de arquivos .csv) produtos.

## Orientações da aplicação

### Orientações de inicialização
- O primeiro passo para iniciar a aplicação corretamente é construir um '.jar' para cada uma das aplicações através da função Package do Maven.
  - Para isso, basta abrir a pasta do projeto em um terminal (Windows PowerShell, Cmd) com 'cd C:/"Caminho para a pasta/produto-api"' e executar o comando './mvnw clean package', e repetir o processo para a pasta '/producer';
- Com os '.jar' gerados, agora basta executar o [docker-compose](https://github.com/Artur-Bertoni/desafio-south-impulsionar/blob/main/docker-compose.yml), também através de um terminal, na pasta raiz que unifica os projetos ('[desafio-south-impulsionar](https://github.com/Artur-Bertoni/desafio-south-impulsionar)') e rodar o comando 'docker-compose up -d'.

### Orientações para uso
- Para que o software funcione corretamente, certifique-se de que não há nenhuma aplicação rodando nas portas 8080, 8081, 5433, 5672 e 15672 do computador, pois serão rodadas nessas portas (respectivamente) a aplicação [produto-api](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/produto-api), a aplicação [producer](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/producer) a conexão com o banco de dados PostgreSql, a conexão com o RabbitMQ e por fim o console Web do RabbitMQ;
- Caso o endpoint "/update" (para a função de importar um arquivo '.csv') não esteja funcionando, tente remover o arquivo e adicioná-lo novamente;
- Para o funcionamento correto da aplicação [producer](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/producer), certifique-se de que as aplicações das quais ela depende estão funcionando corretamente;
- Para a realização dos [testes integrados da aplicação producer](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/producer/src/test/java/com/br/artur/producer/resources/ProductResourceTest.java), todos os componentes presentes no [docker-compose](https://github.com/Artur-Bertoni/desafio-south-impulsionar/blob/main/docker-compose.yml) devem estar rodando;
- A aplicação [produto-api](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/produto-api) tem autonimia para rodar reparadamente da aplicação [producer](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/producer), basta alterar o endpoint de 'http://localhost8081/products' para 'http://localhost8080/products';
- Para a execução do a aplicação [produto-api](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/produto-api) localmente (via H2), basta rodar o container do RabbitMQ e executar no profile 'local' na IDE, ele automaticamente utilizará as configurações descritas em [application-local.yml](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/produto-api/src/main/resources/application-local.yml).

## Dependências utilizadas
### [Produto-Api](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/produto-api/pom.xml)
- [Spring Boot](https://spring.io/projects/spring-boot) (org.springframework.boot) -> utilizando os artefatos [spring-boot-starter-data-jpa](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa), [spring-boot-starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web), [spring-boot-starter-test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test), [spring-boot-starter-thymeleaf](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf), [spring-boot-devtools](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools) e [spring-boot-starter-amqp](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp);
- [Spring AMQP](https://docs.spring.io/spring-amqp/reference/html/) (org.springframework.amqp) -> utilizando o artefato [spring-rabbit-test](https://mvnrepository.com/artifact/org.springframework.amqp/spring-rabbit-test);
- [Flyway](https://flywaydb.org) (org.flywaydb) -> utilizando o artefato [flyway-core](https://mvnrepository.com/artifact/org.flywaydb/flyway-core);
- [PostgreSQL](https://www.postgresql.org) (org.postgresql) -> utilizando o artefato [postgresql](https://mvnrepository.com/artifact/org.postgresql/postgresql);
- [H2](https://www.h2database.com/html/main.html) (com.h2database) -> utilizando o artefato [h2](https://mvnrepository.com/artifact/com.h2database/h2);
- [Apache Commons](https://commons.apache.org) (org.apache.commons) -> utilizando os artefatos [commons-lang3](https://commons.apache.org/proper/commons-lang/) e [commons-csv](https://commons.apache.org/proper/commons-csv/);
- [Lombok](https://projectlombok.org) (org.projectlombok) -> utilizando o artefato [lombok](https://projectlombok.org/setup/maven);
- [JavaFaker](https://github.com/DiUS/java-faker) (com.github.javafaker) -> utilizando o artefato [javafaker](https://mvnrepository.com/artifact/com.github.javafaker/javafaker).
### [Producer](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/producer/pom.xml)
- [Spring Boot](https://spring.io/projects/spring-boot) (org.springframework.boot) -> utilizando os artefatos [spring-boot-starter-amqp](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp), [spring-boot-starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web) e [spring-boot-starter-test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test);
- [Spring AMQP](https://docs.spring.io/spring-amqp/reference/html/) (org.springframework.amqp) -> utilizando o artefato [spring-rabbit-test](https://mvnrepository.com/artifact/org.springframework.amqp/spring-rabbit-test);
- [Lombok](https://projectlombok.org) (org.projectlombok) -> utilizando o artefato [lombok](https://projectlombok.org/setup/maven);
- [Jackson Core](https://github.com/FasterXML/jackson-core) (org.codehaus.jackson) -> utilizando o artefato [jackson-mapper-asl](https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl);
- [Apache Commons](https://commons.apache.org) (org.apache.commons) -> utilizando os artefatos [commons-lang3](https://commons.apache.org/proper/commons-lang/) e [commons-csv](https://commons.apache.org/proper/commons-csv/);
- [JavaFaker](https://github.com/DiUS/java-faker) (com.github.javafaker) -> utilizando o artefato [javafaker](https://mvnrepository.com/artifact/com.github.javafaker/javafaker).

## Requisitos e Funcionalidades Esperadas
- Utilizar a biblioteca flyway para executar as alterações de banco de dados.
    - Arquivo sql 1: Criar a tabela de produtos com os atributos presentes no arquivo CSV do módulo 1.
    - Arquivo sql 2: Alterar o atributo 'codigo de barras' para ser transformado em String e adicionar o campo inteiro que represente a quantidade em estoque do produto, o valor padrão inicial é 0.
    - Arquivo sql 3: Inserir na tabela de produtos os dados continos do arquivo CSV do módulo 1.
- Realizar conexão com o RabbitMQ.
    - Adicionar no docker-compose a imagem do RabbitMQ.
    - Criar endpoint que atualiza a quantidade do produto em estoque, os parâmetros deverão ser o código do produto e a quantidade em estoque.
        - Esta opereação deverá produzir uma mensagem no RabbitMQ contendo os dados do produto que foi atualizado.
        - O nome do evento gerado deverá ser 'PRODUCT_CHANGED'.
    - Criar um consumidor que irá receber os dados do produto e atualizar no DB.
- Ajustar os demais endpoints para contemplar o retorno do novo campo de quantidade em estoque.
- Os testes integrados deve estar funcionando no H2.
- A regra de calculo do imposto dos produtos seguem a mesma do modulo 1:
    - Cálculo do valor final do produto: valor bruto + imposto + margem de lucro de 45%
- Criar um README.md contendo as orientações para rodar a aplicação e suas dependências pela linha de comando.
### Bônus
- Criar um projeto para ser o producer de mensagens
- Adicionar endpoint para cadastro, update e delete, esse endpoint deve produzir uma mensagem que será publicada no RabbitMQ
- Os eventos de alteração e criação deve seguir o padrão: CREATE, UPDATE, DELETE, esses evento deve ser enviado junto a mensagem para o RabbitMQ
- Adicionar endpoints, que consulte o projeto de api, seja por restTemplate ou openfeing
- O endpoint deve validar os campos obrigótrios, e tratar a resposta
- Deve conter os testes unitários
- Deve ser adicionado ao docker-compose do projeto

## Links extras
- Console para acessar o RabbitMQ Mannagement: http://localhost:15672/#/ (Informações de login presentes no [docker-compose](https://github.com/Artur-Bertoni/desafio-south-impulsionar/blob/main/docker-compose.yml), no container 'rabbitmq')
- Endpoints COM [producer](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/producer):
  - Get All: http://localhost:8081/products
  - Get By Id: http://localhost:8081/products/{{id}}
  - Post: http://localhost:8081/products
  - Post By Csv: http://localhost:8081/products/upload
  - Update: http://localhost:8081/products/{{id}}
  - Delete: http://localhost:8081/products/{{id}}
  - Patch Quantity: http://localhost:8081/products/{{code}}
- Endpoint SEM [producer](https://github.com/Artur-Bertoni/desafio-south-impulsionar/tree/main/producer):
  - Get All: http://localhost:8080/products
  - Get By Id: http://localhost:8080/products/{{id}}
  - Post: http://localhost:8080/products
  - Post By Csv: http://localhost:8080/products/upload
  - Update: http://localhost:8080/products/{{id}}
  - Delete: http://localhost:8080/products/{{id}}
  - Patch Quantity: http://localhost:8080/products/{{code}}
