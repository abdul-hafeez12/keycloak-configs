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

#### a. Pull && RUN Keycloak Docker Image:
```bash
 docker run -p 7070:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.4 start-dev
```
#### b. Pull && RUN MySql Docker Image:
```bash
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
![p5](https://github.com/user-attachments/assets/98855666-e76a-4c7f-8260-570dc79ec0da)


## 2. Build the React.js Application
Now, let's build the React.js application that will interact with Keycloak.

### a. Initialize a React.js Application
```
npx create-react-app keycloak-react-app
cd keycloak-react-app
```

### b. Install Keycloak.js
To enable React.js to authenticate users via Keycloak, install the keycloak-js library:
```
npm install keycloak-js
```
### c. Set Up Keycloak in React.js
In the src folder of your React app, create a KeycloakService.js file to manage the Keycloak authentication flow:

```
// src/KeycloakService.js
import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://localhost:8080/auth',
  realm: 'myrealm',
  clientId: 'myreactapp'
});

export default keycloak;
```
### d. Initialize Keycloak in App.js
In your src/App.js file, initialize Keycloak and handle the login state:

```
// src/App.js
import React, { useEffect, useState } from 'react';
import keycloak from './KeycloakService';

function App() {
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    keycloak.init({ onLoad: 'login-required' }).then(authenticated => {
      setAuthenticated(authenticated);
    });
  }, []);

  if (!authenticated) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>Welcome to Keycloak with React</h1>
      <button onClick={() => keycloak.logout()}>Logout</button>
    </div>
  );
}

export default App;
```
 Now, your React app will authenticate against Keycloak when loaded and display a "Logout" button after authentication.

![p6](https://github.com/user-attachments/assets/9a8e2b48-6c51-4ffe-9ead-6ab23509022a)

# 3. Integrate External Service Using User Storage SPI
Keycloak allows you to integrate an external service to manage user storage using the User Storage SPI (Service Provider Interface).

### a. Create a Custom User Storage SPI Provider
To create the SPI, implement a custom provider. Here is an example project structure:
```
user-storage-spi/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── MyExternalUserStorageProvider.java
│   ├── resources/
│   │   └── META-INF/
│   │       └── services/
│   │           └── org.keycloak.storage.UserStorageProviderFactory
│   └── pom.xml

```
### Example code for MyExternalUserStorageProvider.java:
```
package com.example;

import org.keycloak.models.UserModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.UserStorageProviderModel;

public class MyExternalUserStorageProvider implements UserStorageProvider {

    private final UserStorageProviderModel model;

    public MyExternalUserStorageProvider(UserStorageProviderModel model) {
        this.model = model;
    }

    @Override
    public void close() {
        // Close resources if necessary
    }

    // Implement methods for interacting with your external user service
}
```

### b. Build and Deploy the SPI

Build the JAR file with Maven:

```
mvn clean install
```
## Copy the JAR to the Keycloak container:
```
docker cp /path/to/user-storage-spi/target/my-external-user-storage.jar keycloak:/opt/keycloak/standalone/deployments/
```
# c. Configure Keycloak to Use the SPI

Log in to the Keycloak Admin Console at http://localhost:7070.

Navigate to User Federation > External User Storage.

Select your custom User Storage SPI integration from the dropdown.
![p4](https://github.com/user-attachments/assets/b10e6389-e388-4164-861b-2b0d8b738e54)
![p2](https://github.com/user-attachments/assets/2e914128-32bf-413b-b39a-97e7f60ca4bd)


## 4. Add a Custom Login Theme to Keycloak

### a. Prepare Your Custom Theme
Ensure your custom login theme is structured correctly:
```
themes/
└── my_custom_theme/
    └── login/
        ├── theme.properties
        ├── login.ftl
        └── styles.css
```
### b. Copy the Custom Theme into Keycloak

Copy the theme into the Keycloak container:
```
docker cp /path/to/my_custom_theme keycloak:/opt/keycloak/themes/
```

### c. Configure Keycloak to Use the Custom Theme
Log in to the Keycloak Admin Console at http://localhost:7070.

Navigate to Realm Settings > Themes.

Under Login Theme, select my_custom_theme.

Save the changes.
![p3](https://github.com/user-attachments/assets/c99c34e0-ebcb-421d-9657-892a6e292fa5)


### 5. Restart Keycloak
After applying the custom theme and SPI changes, restart Keycloak:
docker restart keycloak
### 6. Verify the Setup
React App: Open your React app at http://localhost:3000 in your browser. It should automatically redirect to Keycloak for authentication and display the custom login page.

Keycloak: Visit http://localhost:7070 and verify the custom theme is applied to the login page.

### User Storage SPI: Verify that Keycloak is interacting with your external user service as expected.

![p7](https://github.com/user-attachments/assets/11af49a3-6def-482d-9373-ee460335e2d4)



### Key Points in This Guide:
- **React.js Integration**: React.js is integrated with Keycloak using `keycloak-js` for authentication.
- **User Storage SPI**: Custom SPI is created to interact with an external user service.
- **Custom Login Theme**: Custom login theme is applied to Keycloak.
- **Dockerization**: Keycloak is containerized for easy deployment.

Let me know if you need further clarification or help!




