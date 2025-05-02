# Keycloak Custom Login Theme Setup with Docker

This guide walks you through the process of setting up a Keycloak Docker container, and then adding your custom login theme to the Keycloak server.

## Prerequisites

Before you begin, ensure you have the following:

- Docker installed on your machine
- A custom Keycloak login theme prepared (the theme should be in a folder with the required structure)

## Steps

### 1. Create and Start a Keycloak Docker Container

Follow these steps to create and start a Keycloak container.

#### a. Pull the Keycloak Docker image:

Open a terminal and run the following command to pull the Keycloak image from Docker Hub.

```bash
docker pull quay.io/keycloak/keycloak:26.1.4

docker run -d \
  --name keycloak \
  -p 7070:8080 \
  -e KEYCLOAK_ADMIN=<admin_username> \
  -e KEYCLOAK_ADMIN_PASSWORD=<admin_password> \
  quay.io/keycloak/keycloak:26.1.4

```

### 2. Copy This Custom Login Theme to Keycloak Container

```

themes/
├── my_custom_theme/
│   ├── login/
│   │   ├── theme.properties
│   │   ├── login.ftl
│   │   └── other_files.css
│   └── other_theme_files/

docker cp /path/to/my_custom_theme keycloak:/opt/keycloak/themes/

```

### 3.  Restart the Keycloak Container

```
docker restart keycloak

```

### 4.  Configure Keycloak to Use Your Custom Theme

```
To configure Keycloak to use your custom theme for login, follow these steps:

a. Access Keycloak Admin Console
Go to your browser and navigate to the following URL (make sure Keycloak is running):

http://localhost:7070

Log in to the Keycloak admin console with the KEYCLOAK_ADMIN username and password you set earlier.

 Set the Custom Theme for Login
In the Keycloak admin console, go to Realm Settings.

Select the Themes tab.

Under the Login Theme dropdown, select your custom theme (my_custom_theme).

Save the changes.

```
