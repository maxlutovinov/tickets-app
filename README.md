# Cinema-shop

<!-- TOC -->

* [Description](#description)
* [Features](#features)
* [Structure](#structure)
* [Technology stack](#technology-stack)
* [Usage](#usage)

<!-- TOC -->

## Description

This is the Java back-end part of a web project as a prototype for a web cinema. Using Spring and Hibernate Frameworks,
the web application provides authentication, registration, authorization, HTTP request validation and performing
database CRUD operations on the command of HTTP requests.

## Features

Depending on the role, Admin or User may have different access to URLs and HTTP request methods:<br>

* Register user. All users, anonymous and authenticated, have access. `POST: /register`
* Get all cinema halls. User/admin. `GET: /cinema-halls`
* Create cinema hall. Admin. `POST: /cinema-halls`
* Get all movies. User/admin. `GET: /movies`
* Create movie. Admin. `POST: /movies`
* Find available movie sessions by movie id and date. User/admin. `GET: /movie-sessions/available`
* Create movie session. Admin. `POST: /movie-sessions`
* Update movie session. Admin. `PUT: /movie-sessions/{id}`
* Remove movie session. Admin. `DELETE: /movie-sessions/{id}`
* Add movie session to shopping cart. User. `PUT: /shopping-carts/movie-sessions`
* Get shopping cart. User. `GET: /shopping-carts/by-user`
* Get orders history. User. `GET: /orders`
* Complete order. User. `POST: /orders/complete`
* Get user by email. Admin. `GET: /users/by-email`

After running the program, a user with administrator authority will already be initialized with the following
credentials:

    Login: admin@domain.com
    Password: 12345678

The registered user through `POST: /register` endpoint will be granted the user authority.

## Structure

The project have a three-tier architecture that include the following layers of functionality:

- [Controller](src/main/java/cinema/controller) layer processes a user request sent from the user interface and responds
  to the user.
- [Service](src/main/java/cinema/service) layer is responsible for the business logic of the application on request from
  the controller.
- [DAO](src/main/java/cinema/dao) layer works with the database on request from the service.

The project operates with the entities which are represented in the [model](src/main/java/cinema/model) package of the
project. The relational dependencies between them is shown in the diagram [here](cinema_uml.png).

## Technology stack

Java 11, Spring (Core, Web, Security), Hibernate, SQL, Maven, Tomcat 9, JSON, Heroku Webapp Runner.

## Usage

### Clone

    git clone https://github.com/maxlutovinov/cinema-shop.git

### Create database

* Install [MySQL](https://dev.mysql.com/downloads/workbench/)
* Create a database with the name you specify below in `db.url` property
* Don't create any tables for entities, Hibernate will do it for you

### Set up connection to database

Change the database properties to yours in [application.properties](src/main/resources/application.properties) file.<br>
Example for MySQL database:

    db.driver=com.mysql.cj.jdbc.Driver
    db.url=jdbc:mysql://localhost:3306/cinema_shop
    db.user=root
    db.password=12345678

#### DB connection error:

If you can't connect to your db because of an error like this:

    The server time zone value ‘EEST’ is unrecognized or represents more than one time zone

Try to set timezone explicitly in your connection URL. <br>Example:

    ...localhost:3306/your_schema?serverTimezone=UTC

### Build

Run on command line from the root directory of the project:

    mvn package

### Run with Heroku Webapp Runner

Run on command line from the root directory of the project:

    java -jar target/dependency/webapp-runner.jar target/*.war

[Webapp Runner](https://github.com/heroku/devcenter-webapp-runner) allows you to launch an application in your
filesystem into a tomcat container with a simple java -jar command. No previous steps to install Tomcat are required
when using Webapp Runner.<br>
The Maven dependency plugin is already added in [pom.xml](pom.xml) to download webapp-runner as part of your build.

After launching the application successfully, open it in your browser:

    http://localhost:8080/

This will bring you to the login page of application. You can log in with the prepared admin account (see
in [Features](#features)).<br>

Since this is the back end of the web project, you can use Postman to send HTTP requests to the application.<br>
As an example, fork my collection:<br><br>
[![Run in Postman](https://run.pstmn.io/button.svg)](https://god.gw.postman.com/run-collection/22141349-1bdb1050-acab-40cf-9e6e-5137bd20b660?action=collection%2Ffork&collection-url=entityId%3D22141349-1bdb1050-acab-40cf-9e6e-5137bd20b660%26entityType%3Dcollection%26workspaceId%3Da134d96c-8c98-4857-a916-55b532b3c9b7)

### Run in IDE

Download [Tomcat](https://tomcat.apache.org/download-90.cgi), configure it for the project in your IDE and run.
