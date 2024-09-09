# Library Management System

Welcome to the **Library Management System** project. This application is built using Spring Boot and provides a comprehensive solution for managing authors and books. It supports all standard CRUD (Create, Read, Update, Delete) operations and includes several modern features for enhanced functionality and security.

## Features

### Author API:

* #### Retrieve Author: GET /authors/{id} - Retrieve details of an author by ID.
* #### List All Authors: GET /authors/allAuthors - Retrieve a list of all authors.
* #### Create Author: POST /authors - Create a new author.
* #### Delete Author: DELETE /authors/{id} - Delete an author by ID.
* #### Update Author: PUT /authors/{id} - Update author details by ID.

### Book API:

* #### Retrieve Book: GET /books/{id} - Retrieve details of a book by ID.
* #### List All Books: GET /books/all - Retrieve a list of all books.
* #### Create Book: POST /books - Create a new book.
* #### Delete Book: DELETE /books/{id} - Delete a book by ID.
* #### Update Book: PUT /books/{id} - Update book details by ID.

### Authentication API:

* #### Register: POST /register - Register a new user.
* #### Authenticate: POST /authenticate - Authenticate a user.
* #### Refresh Token: POST /refresh-token - Refresh the authentication token.


### Security
The application is secured with Spring Security. It includes endpoints for user registration, authentication, and token refresh. All sensitive operations are protected to ensure secure access.

### Upon startup of the application, two users are automatically registered with the following credentials:

#### Admin User:

* **Email**: admin@mail.com
* **Password**: password
* **Role**: ADMIN

#### Manager User:

* **Email**: manager@mail.com
* **Password**: manager
* **Role**: MANAGER

### Dockerization
The application is dockerized to facilitate easy deployment and management. Docker Compose is used to orchestrate the services, including the database.

### Docker Compose Configuration
The docker-compose.yml file configures the following services:

### PostgreSQL:

* #### Container Name: postgres-sql
* #### Image: postgres:latest
* #### Environment Variables:
  * **POSTGRES_USER**: username
  * **POSTGRES_PASSWORD**: password
  * **POSTGRES_DB**: library_system
* #### Ports: 5432:5432
* #### Volumes: Persistent data storage
* #### Network: app-network

### pgAdmin:

* #### Container Name: pgadmin
* #### Image: dpage/pgadmin4:latest
* #### Environment Variables:
  * **PGADMIN_DEFAULT_EMAIL**: pgadmin4@pgadmin.org
  * **PGADMIN_DEFAULT_PASSWORD**: admin
  * **PGADMIN_CONFIG_SERVER_MODE**: 'False'
* #### Ports: http://localhost:5050
* ####  Volumes: Persistent data storage
* ####  Network: app-network
  
### Library Management System Application:

* #### Container Name: library-management-system
* #### Build: From the Dockerfile in the current directory
* #### Environment Variables:
    * **SPRING_DATASOURCE_URL**: jdbc:postgresql://postgres:5432/library_system
    * **SPRING_DATASOURCE_USERNAME**: username
    * **SPRING_DATASOURCE_PASSWORD**: password
* #### Ports: 8080:8080
* #### Network: app-network
* #### Depends On: postgres

**To build and run the application using Docker Compose, use the following commands**:


## Build the Docker image
`docker build -t library-management-system .`

## Run Docker Compose
`docker-compose up`

## pgAdmin Access
You can access pgAdmin at http://localhost:5050. 
Use the following credentials to log in:

* **Email**: pgadmin4@pgadmin.org
* **Password**: admin

## Database Configuration
### Database root/main password: root
* **Database Username**: username
* **Database Password**: password

**These credentials are configured in the docker-compose.yml file**

## API Documentation
**API documentation is provided using Swagger and OpenAPI. This allows for easy exploration and understanding of the available endpoints and their usage.**

**To access the API documentation, navigate to  http://localhost:8080/swagger-ui/index.html in your browser after starting the application.**