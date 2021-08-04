# gameHub-backend   
##  for gamehub-frontend => [https://github.com/aj941ga/gamehub-frontend](https://github.com/aj941ga/gamehub-frontend) )
A one stop game site where users can download, rate and upload games. 
Registered Users can make profile. Comment on game feed. upvote/downvote other comments.
supports mail based authentication for registeration.
uses Jwt token based authorization

### home page
![home_page](https://github.com/aj941ga/gameHub-backend/blob/master/snapshots/screencapture-localhost-3000-games-2021-02-21-18_56_04.png)

### game page
![game_page](https://github.com/aj941ga/gameHub-backend/blob/master/snapshots/gamePage.png)

## supports 

## Table of contents

  * [Application Screenshots](#application-screenshots)
  * [Tech stack & Open-source libraries](#tech-stack---open-source-libraries)
    + [Data](#data)
    + [Client - Frontend/UI](#client---frontend-ui)
    + [Server - Backend](#server---backend)
    + [Others](#others)
  * [To-Do](#to-do)
  * [Running the application locally](#running-the-application-locally)
  * [Running the application with IDE](#Running-the-application-with-IDE)
  * [Running the application with Maven](#Running-the-application-with-Maven)
  * [EER Diagram](#EER-Diagram)
  * [Packages](#Packages)

## Application Screenshots

![home page]()
![]()

## Tech stack & Open-source libraries

### Data

* 	[MySQL](https://www.mysql.com/) - Open-Source Relational Database Management System

### Client - Frontend/UI

* 	REACTJs

### Cloud 

*   AWS RDS to host MySql database 
*   AWS S3 to store images and game Files
*   frontend and backend of application hosted on AWS elastic beanstalk

### Server - Backend

* 	[JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java™ Platform, Standard Edition Development Kit
* 	[Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications
* 	[Maven](https://maven.apache.org/) - Dependency Management


###  Libraries and Plugins

* 	Bootstrap
* 	[Lombok](https://projectlombok.org/) - Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.
* 	[Swagger](https://swagger.io/) - Open-Source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.

### Others 

* 	[git](https://git-scm.com/) - Free and Open-Source distributed version control system

### External Tools & Services

* 	[Postman](https://www.getpostman.com/) - API Development Environment (Testing Docmentation)
* 	[gitignore.io](https://www.toptal.com/developers/gitignore/api/java,eclipse,intellij) - Create useful .gitignore files for your project.

### To-Do

- [X] login module
- [X] activation for users via SMTP
- [X] basic gameHub features
- [X] authentication by HTTPSessionId
- [ ] testing of all restControllers
- [ ] HTTPS
- [ ] JWT
- [ ] improve frontend
- [ ] load testing
- [ ] others


## Running the application locally

*	You need to have **MySQL** installed on your machine to run the application on **`dev`** profile. Using the `MySQL Workbench` or on any other MySQL client/console, create a database/schema named `gameHub`. 

run mySQL code FROM .data/db_version_control/latest_version in my SQL workbench then run the following mySQL queries to add required data

~~~sql
 INSERT INTO gamehub.hibernate_sequence VALUES(1);
 
 # after running the springBoot application and creating your profile run the following query to change the users role to ADMIN
 
 UPDATE gamehub.user set role = 'ROLE_ADMIN' where gamehub.user.id = 1;
 
 # NOTE : you may need to make sure that ... if table_a has oneToManyRelationshipWith table_b then table_a should be populated first with 
 # necessary data else it can lead to foreign key constraint violation
~~~

In the properties file change the following 
~~~
.
.
spring.datasource.username=root  # your mySQL workbench username
spring.datasource.password=admin_password   # your mySQL workbench password
.
spring.mail.username= enter_ur_emailId_here@gmail.com   # your email should be ..@gmail.com only
spring.mail.password=enter_ur_google_account_app_key    # see google how to generate google app key else you can use your password here(unsafe)
.
.
~~~

* 	URL to access application UI: **http://localhost:8080/login*

### Running the application with ECLIPSE IDE

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `TaskMain.java` class from your IDE.

* 	Download the zip or clone the Git repository.
* 	Unzip the zip file (if you downloaded one)
* 	Open Command Prompt and Change directory (cd) to folder containing pom.xml
* 	Open Eclipse
	* File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
	* Select the project
* 	Choose the Spring Boot Application file (search for @SpringBootApplication)
* 	Right Click on the file and Run as Java Application

### Running the application with Maven

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
$ git clone https://github.com/aj941ga/gameHub.git
$ cd gameHub
$ mvn spring-boot:run
```

### Guide to implementing frontend of this project

first uncomment the commented line in [ApplicationSecurityAdapter]() to disable authorization and allow swagger to read the controllers present in
the springBoot Application.

After running the spring boot application go to **http://localhost:8080/swagger-ui/** to read the controller http request documentation, but some
request methods may not work in swagger as they require the user to be authenticated (via http Session id) ... so use postman for now .
JWT based authentication will be added later. you can refer swagger link just to see input and response body format (it differs for posting files
so see the [src/main/java/app/gameController]() )

for getting user view go to **http://localhost:8080/login*

NOTE : [userController]() and [loginController]() use thymeleaf models instead of JSON ... so i will update them later. you are supposed to make frontend 
for other controllers.

 NOTE : some controller methods have role based authorization .. they are annotated with @Admin (see )
 
 for css, images and javaScript files the standard link is [src/main/webApp]() .. they can be accessed without authorization
 
 for html content the standard link is [src/main/resources]().
 

## EER Diagram
![EER diagram v2](https://github.com/aj941ga/gameHub-backend/blob/master/snapshots/game_hub_database_pic.PNG)

## Packages

*   `api` - API utilities;
* 	`controllers` - to listen to the client;
* 	`service` - to hold business logic;
* 	`util` - to hold our utility classes;

* 	`resources/` - Contains all the static resources, templates and property files.
* 	`resources/static` - contains static resources such as css, js and images.
* 	`resources/templates` - contains server-side templates which are rendered by Spring.
* 	`resources/application.properties` - It contains application-wide properties. Spring reads the properties defined in this file to configure your application. You can define server’s default port, server’s context path, database URLs etc, in this file.

* 	`pom.xml` - contains all the project dependencies
