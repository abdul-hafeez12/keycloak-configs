# Keycloak with React.js, External Service Integration For Custom User Storage Provide User Fedration, and Custom Login Theme Setup

This guide helps you set up a Keycloak server in Docker, build a React.js application that calls Keycloak's external services, configure User Storage SPI (Service Provider Interface), and apply a custom login theme.

## Prerequisites

Ensure you have the following tools installed:

- Docker
- Node.js and npm (for React.js)
- Keycloak Docker image
- Java Development Kit (JDK) (for developing the SPI integration)
- Custom login theme for Keycloak (that is provided above)

## Steps

### 1. Create and Start Keycloak Docker Container && MySql Container

First, we’ll create a Keycloak container that runs on Docker. Follow these steps:

#### a. Pull the Keycloak Docker Image:
```bash
 docker run -p 7070:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.4 start-dev
 
```

#### b. Pull && RUN MySql Docker Image:
``bash
docker pull mysql:latest
docker run --name test-mysql -e MYSQL_ROOT_PASSWORD=password12 -d mysql
docker exec -it test-mysql bash
mysql -u root -p

ALTER TABLE user MODIFY brn_cd DECIMAL(4,2);

docker stop test-mysql; docker rm test-mysql
docker volume rm test-mysql-data

docker run \
   --name test-mysql \
   -e MYSQL_ROOT_PASSWORD=password12 \
   -p 3306:3306 \
   -v /etc/docker/test-mysql:/etc/mysql/conf.d \
   -v test-mysql-data:/var/lib/mysql \
   -d mysql

CREATE TABLE user(
        user_id varchar(32) NOT NULL,
        password varchar(32) DEFAULT NULL,
        brn_cd   int(4) NOT NULL,
        full_name varchar(32) DEFAULT NULL,
        PRIMARY KEY (user_id)
  );
   
INSERT INTO user(user_id,password,brn_cd,full_name) VALUES ('jalal','teersol123',1001,'Jalal Hasan'); 
INSERT INTO user(user_id,password,brn_cd,full_name) VALUES ('abdul','teersol123',1025,'Abdul Hafeez'); 
```
