# JavaCore final project
## Description
Simple and not tread safe REST API shop service.
Created as final task for my java core course.

Property of griddynamics.com

## Requirments
- MySQL server
- Apache Tomcat
- Maven

### Supported Rest methods

#### Security

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
7. **/displayOrder - display your orders(GET)**

#### Web.xml params
**databaseUrl - database url**
**databaseName - database name**
**databaseLogin - database login**
**databasePassword -database password**
**hashPasswords - if true server will hash password**
**sessionExpirationTime in ms- defines how long user should be incactive until his cart expires**
**sessionExpirationCheckInterval in ms - sleep timeout for a thread that checks expired carts**