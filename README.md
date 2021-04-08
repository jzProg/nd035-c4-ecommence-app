# eCommerce App

My implementation of the final assessment project for Udacity's Java Web Developer Nanodegree `Security and DevOps` course.

## Description
<img alt="Spring" src="https://img.shields.io/badge/spring%20-%236DB33F.svg?&style=for-the-badge&logo=spring&logoColor=white"/>

An app with proper authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way.

## Project Structure
It is written in Java using Spring Boot, Hibernate ORM, and the H2 database. H2 is an in memory database.

Packages:

* demo - this package contains the main method which runs the application

* model.persistence - this package contains the data models that Hibernate persists to H2. There are 4 models: Cart, for holding a User's items; Item , for defining new items; User, to hold user account information; and UserOrder, to hold information about submitted orders. Looking back at the application “demo” class, you'll see the `@EntityScan` annotation, telling Spring that this package contains our data models

* model.persistence.repositories - these contain a `JpaRepository` interface for each of our models. This allows Hibernate to connect them with our database so we can access data in the code, as well as define certain convenience methods. Look through them and see the methods that have been declared. Looking at the application “demo” class, you’ll see the `@EnableJpaRepositories` annotation, telling Spring that this package contains our data repositories.

* model.requests - this package contains the request models. The request models will be transformed by Jackson from JSON to these models as requests are made. Note the `Json` annotations, telling Jackson to include and ignore certain fields of the requests. You can also see these annotations on the models themselves.

* controllers - these contain the api endpoints for our app, 1 per model. Note they all have the `@RestController` annotation to allow Spring to understand that they are a part of a REST API

In resources, you'll see the application configuration that sets up our database and Hibernate, It also contains a data.sql file with a couple of items to populate the database with. Spring will run this file every time the application starts

## Extra Files

- The application logs are stored to files located in `logs` folder.

- In `splunk_screenshots` folder, there are the screenshots of the Splunk queries (on log files) and alert configuration that was required by the assessment.

- In `CI_CD_pipeline_screens` folder, there are the screenshots of the CI/CD pipeline steps that i followed in order to build and deploy the application in *AWS* server instances by using *Jenkins*.
