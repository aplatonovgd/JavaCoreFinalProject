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

1. **/register - register user  (POST)**
Example request:
```
{"email":"test@test.com", "password":"123456"}
```
2. **/login  - login (POST)**
Example request:
```
{"email":"test@test.com", "password":"123456"}
```
3. **/logout - logout (GET)**

#### Shop operations
1. **/  - get all products (GET)**
2. **/displayCart - display your cart (GET)**
3. **/addItemToCart - add item to the cart(POST)**
Example request:
```
{"id":"3", "quantity":"10"}
id - product id
quantity - product quantity
```
4. **/modifyCartItem - modify item in the cart(POST)**
Example request:
```
{"id":"1", "quantity":"0"}
id - id of the item in the cart
quantity - product quantity
```
5. **/removeCartItem - remove item from the cart(POST)**
Example request:
```
{"id":"1"}
id - id of the item in the cart
```
6. **/checkout - checkout cart (GET)**
7. **/displayOrders - display your orders(GET)**

#### Web.xml params
1. **databaseUrl - database url**
2. **databaseName - database name**
3. **databaseLogin - database login**
4. **databasePassword -database password**
5. **hashPasswords - if true server will hash password**
6. **sessionExpirationTime in ms- defines how long user should be inactive until his cart expires**
7. **sessionExpirationCheckInterval in ms - sleep timeout for a thread that checks expired carts**

##### How to test

1. Start Postman
2. Import file "javaCore.postman_collection.json"
