# JavaCore final project
## Description
Simple and not tread safe REST API shop service.
Created as final task for my java core course.

Property of https://griddynamics.com

## Requirements
- MySQL server
- Apache Tomcat
- Maven

### Supported Rest methods

#### Security operations

Make sure that your client supports cookies.

1. **/register - register user (POST)** Example request:
```
{"email":"test@test.com", "password":"123456"}
```
2. **/login  - login (POST)** Example request:
```
{"email":"test@test.com", "password":"123456"}
```
3. **/logout - logout (GET)**

#### Shop operations
1. **/  - get all products (GET)**
2. **/displayCart - display your cart (GET)**
3. **/addItemToCart - add item to the cart(POST)** Example request:
```
{"id":"3", "quantity":"10"}
id - product id
quantity - product quantity
```
4. **/modifyCartItem - modify item in the cart(POST)** Example request:
```
{"id":"1", "quantity":"0"}
id - id of the item in the cart
quantity - product quantity
```
5. **/removeCartItem - remove item from the cart(POST)** Example request:
```
{"id":"1"}
id - id of the item in the cart
```
6. **/checkout - checkout cart (GET)**
7. **/displayOrders - display your orders (GET)**


#### Web.xml params
1. **databaseUrl - database url**
2. **databaseName - database name**
3. **databaseLogin - database login**
4. **databasePassword - database password**
5. **hashPasswords - if true server will hash password**
6. **sessionExpirationTime (in ms) - defines how long user should be inactive until his cart expires**
7. **sessionExpirationCheckInterval (in ms) - sleep timeout for a thread that checks expired carts**

#### Installation and Deployment guide

1. Make sure that you have Maven, Tomcat and MySql installed.
2. Make sure that Tomcat and MySql services started.
3. Pull the project and wait for maven to download all dependencies
4. Open pom.xml and change connection properties in tomcat7-maven-plugin (make sure that you have a tomcat admin account)
4.1 (Optional) Put "mysql-connector-java.jar" file to your "(Tomcat)\Lib" folder
5. Open "src\main\webapp\WEB-INF\web.xml" file and specify your MySQL database connection parameters.
6. Use command "tomcat7: deploy" to deploy the project
7. Use command "tomcat7: redeploy" to redeploy the project

#### Troubleshooting

- Note! Only MySql is supported.
- If the application doesn't start, make sure that you correctly specified all the settings
- The application uses servlet logging. The log file is in "(Tomcat)\logs" folder. (filename - localhost.YYYY-MM-DD)

#### How to test
For acceptance testing I recommend using Postman. There is a settings file with all requests - 'javaCore.postman_collection.json'.

1. Start Postman
2. Import file "javaCore.postman_collection.json" from the root project's folder.
