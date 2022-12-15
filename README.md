# Desafio Backend Modulo 2
Desafio Módulo 2 da trilha Impulsionar 2.0 - Backend

## Objetivo
O objetivo desse desafio era de alterar o [Desafio do Módulo 1](https://github.com/Artur-Bertoni/desafioBackendModulo1) para um software com conexão com banco de dados e implementando novas features estudadas na trilha, como Automação de API, Docker, SpringBoot e [outras](doc:requisitos-e-funcionalidades-esperadas).
O contexto apresentado foi o de criar um aplicativo de uma loja simples, com as funções de adicionar, editar, excluir e importar (através de arquivos .csv) produtos

## Orientações para o uso
- Para que o software funcione corretamente, certifique-se de que não há nenhuma aplicação rodando nas portas 8080, 5432 e 5433 do computador, pois serão rodadas nessas portas (respectivamente) a aplicação Spring Web, a conexão interna no container do docker com o banco de dados PostgreSql e a exposição externa do container do BD;

- Caso o endpoint "/update" (para a função de importar um arquivo CSV não esteja funcionando, tente remover o arquivo e adicioná-lo novamente;

## [Dependências utilizadas](https://github.com/Artur-Bertoni/desafioBackendModulo2/blob/main/pom.xml)
- [Spring Boot](https://spring.io/projects/spring-boot) (org.springframework.boot) -> utilizando os artefatos [starter-data-jpa](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa),[starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web),[starter-test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test),[starter-thymeleaf](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf) e [devtools](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools);
- [Flyway](https://flywaydb.org) (org.flywaydb);
- [PostgreSQL](https://www.postgresql.org) (org.postgresql);
- [H2](https://www.h2database.com/html/main.html) (com.h2database);
- [Apache Commons](https://commons.apache.org) (org.apache.commons) -> utilizando os artefatos [commons-lang3](https://commons.apache.org/proper/commons-lang/) e [commons-csv](https://commons.apache.org/proper/commons-csv/);
- [Lombok](https://projectlombok.org) (org.projectlombok);
- [JavaFaker](https://github.com/DiUS/java-faker) (com.github.javafaker).

## Requisitos e Funcionalidades Esperadas
- Iniciar uma nova aplicação utilizando o Spring Initializr;
- Construir APIs REST (API na maturidade Richard) para os serviços de backend que foram desenvolvidos no Desafio do Módulo 1;
- Alterar o backend para que os dados sejam armazenados em banco de dados, Postgres;
- Integrar a camada de linha de comando (Desafio Módulo 1) com as APIs Rest do backend;
- Construir as interfaces para os serviços REST de inclusão, edição, exclusão, listar e importação do arquivo de mostruário da fábrica (todas as colunas);
- Construir imagem docker para o backend. Publicar essas imagem no Docker Hub;
- Disponibilizar um Docker Compose com: Banco de dados;
- Documentação das APIs com Open API;
- Utilizar o Spring-data;
- Utilizar lombok;
- Criar um README.md contendo as orientações para rodar a aplicação e suas dependências pela linha de comando;
- Criar arquivo Json para importar as APIs no Postman;
- Testes Unitários com o Mockito;
- Versionar o Projeto, criar 2 branches "DEV" e "MAIN" (Desenvolver utilizando Branch-Features).
