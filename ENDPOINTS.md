# Endpoints

## Login

* **URL**

    /api/v1/login

* **Method**

    POST

* **URL Params**

    Not required

* **Data params**

    ```json
    {
        "username" : "admin",
        "password" : "admin"
    }
    ```

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 

         ```{"errorMessage" : "Incorrect username and/or password"} ```
    
 * Sample Call

    ```
    curl -X POST \
    http://localhost:9000/api/v1/login \
    -H 'Content-Type: application/json' \
    -d '{
        "username": "admin",
        "password": "admin"
    }'
    ```

## Logout

* **URL**

    /api/v1/logout

* **Method**

    POST

* **URL Params**

    Not required

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 
    
        ```{"errorMessage" : "You're not allowed to access this resource."} ```
    
 * Sample Call

    ```
    curl -X POST http://localhost:9000/api/v1/logout
    ```

## List of Users

* **URL**

    /api/v1/users

* **Method**

    GET

* **URL Params**

    Not required

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK
    ```json
    [
        {
            "name" : "Admin",
            "surname" : "User",
            "uuid" : "2377bf64-846c-4cbe-a1ae-dfc7d5d99e9b",
            "role" : "Admin"
        },
        {
            "name" : "Jerome",
            "surname" : "Samson",
            "uuid" : "715e8965-d9c8-4d89-affc-cb7b8cfec16c",
            "role" : "User"
        }
    ]
    ```

* Error response:

    * **Code**: 401 UNAUTHORIZED 
    
         ```{"errorMessage" : "You're not allowed to access this resource."} ```
    
 * Sample Call

    ```
    curl -X GET http://localhost:9000/api/v1/user
    ```

## Add User

* **URL**

    /api/v1/users

* **Method**

    POST

* **URL Params**

    Not required

* **Data params**

    ```json
    {
        "name"  : "Jerome",
        "surname" : "Samson",
        "role" : "user",
        "username" : "jerome", 
        "password" : "mypassword"
    }
    ```

* Success Response:

    * **Code**: 201 CREATED
    ```json
    {
        "userUuid" : "71e9d4ba-78f9-480c-b8c9-cf2a1a6f8798"
    }
    ```

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "There already exist an user with the same name and surname"} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "There already exist an user with the same login username"} ```
    
 * Sample Call

    ```
    curl -X POST \
    http://localhost:9000/api/v1/users \
    -H 'Content-Type: application/json' \
    -d ' {
            "name"  : "Jerome",
            "surname" : "Samson",
            "role" : "user",
            "username" : "jerome", 
            "password" : "mypassword"
        }'
    ```

## Get User

* **URL**

    /api/v1/users/:uuid

* **Method**

    GET

* **URL Params**

    uuid=[string]

    example: 61dc8fe5-cc7e-4f06-9a8f-a21a3da00216

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK

    ```json
    {
        "name" : "Jerome",
        "surname" : "Samson",
        "uuid" : "715e8965-d9c8-4d89-affc-cb7b8cfec16c",
        "role" : "User"
    }
    ```

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "User does not exist."} ```
    
 * Sample Call

    ```
    curl -X GET http://localhost:9000/api/v1/users/61dc8fe5-cc7e-4f06-9a8f-a21a3da00216
    ```

## Edit User

* **URL**

    /api/v1/users/:uuid

* **Method**

    PUT

* **URL Params**

    uuid=[string]

    example: 61dc8fe5-cc7e-4f06-9a8f-a21a3da00216

* **Data params**

    ```json
    {
        "name"  : "JRM",
        "surname" : "SAM",
        "role" : "admin",
        "username" : "jrm", 
        "password" : "mynewpassword"
    }
    ```

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "There already exist an user with the same name and surname"} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "There already exist an user with the same login username"} ```
    
 * Sample Call

    ```
    curl -X PUT \
    http://localhost:9000/api/v1/users/61dc8fe5-cc7e-4f06-9a8f-a21a3da00216 \
    -H 'Content-Type: application/json' \
    -d ' {
            "name"  : "Jerome",
            "surname" : "Samson",
            "role" : "user",
            "username" : "jerome", 
            "password" : "mypassword"
        }'
    ```

## Delete User

* **URL**

    /api/v1/users/:uuid

* **Method**

    DELETE

* **URL Params**

    uuid=[string]

    example: 61dc8fe5-cc7e-4f06-9a8f-a21a3da00216

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```
    
 * Sample Call

    ```
    curl -X PUT http://localhost:9000/api/v1/users/61dc8fe5-cc7e-4f06-9a8f-a21a3da00216
    ```


## List of Customers

* **URL**

    /api/v1/customers

* **Method**

    GET

* **URL Params**

    Not required

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK
    ```json
    [
        {
            "uuid" : "2377bf64-846c-4cbe-a1ae-dfc7d5d99e9b",
            "name" : "Jerome",
            "surname" : "Samson",
            "photoUrl" : "http://localhost:9000/api/v1/customers/photos/2377bf64-846c-4cbe-a1ae-dfc7d5d99e9b.jpg"
        },
        {
            "uuid" : "9ecb11a3-f25e-4bdf-8ee0-1adb8c5c40a7",
            "name" : "JRM",
            "surname" : "SAM",
            "photoUrl" : "http://localhost:9000/api/v1/customers/photos/9ecb11a3-f25e-4bdf-8ee0-1adb8c5c40a7.jpg"
        },
    ]
    ```
    

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```
    
 * Sample Call

    ```
    curl -X PUT \
    http://localhost:9000/api/v1/users \
    -H 'Content-Type: application/json' \
    -d ' {
            "name"  : "Jerome",
            "surname" : "Samson",
            "role" : "user",
            "username" : "jerome", 
            "password" : "mypassword"
        }'
    ```


## Add Customer

* **URL**

    /api/v1/customers

* **Method**

    POST

* **URL Params**

    Not required

* **Data params**

    ```json
    {
        "name"  : "Jerome",
        "surname" : "Samson",
    }
    ```

* Success Response:

    * **Code**: 201 CREATED
    ```json
    {
        "customerUuid" : "49cabb94-d6fd-45a7-aeba-71516e95d3db"
    }
    ```

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "There already exist a customer with the same name and surname"} ```
    
 * Sample Call

    ```
    curl -X POST \
    http://localhost:9000/api/v1/customers \
    -H 'Content-Type: application/json' \
    -d ' {
            "name"  : "Jerome",
            "surname" : "Samson"
        }'
    ```

## Get Customer

* **URL**

    /api/v1/customers/:uuid

* **Method**

    GET

* **URL Params**

    uuid=[string]

    example: 2377bf64-846c-4cbe-a1ae-dfc7d5d99e9b

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK

    ```json
    {
        "uuid" : "2377bf64-846c-4cbe-a1ae-dfc7d5d99e9b",
        "name" : "Jerome",
        "surname" : "Samson",
        "photoUrl" : "http://localhost:9000/api/v1/customers/photos/2377bf64-846c-4cbe-a1ae-dfc7d5d99e9b.jpg"
    }
    ```

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "Customer does not exist."} ```
    
 * Sample Call

    ```
    curl -X GET http://localhost:9000/api/v1/users/2377bf64-846c-4cbe-a1ae-dfc7d5d99e9b
    ```

## Edit Customer

* **URL**

    /api/v1/customers/:uuid

* **Method**

    PUT

* **URL Params**

    uuid=[string]

    example: 61dc8fe5-cc7e-4f06-9a8f-a21a3da00216

* **Data params**

    ```json
    {
        "name"  : "JRM",
        "surname" : "SAM",
    }
    ```

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "There already exist a customer with the same name and surname"} ```
    
 * Sample Call

    ```
    curl -X PUT \
    http://localhost:9000/api/v1/users/61dc8fe5-cc7e-4f06-9a8f-a21a3da00216 \
    -H 'Content-Type: application/json' \
    -d ' {
            "name"  : "Jerome",
            "surname" : "Samson"
        }'
    ```

## Delete User

* **URL**

    /api/v1/customers/:uuid

* **Method**

    DELETE

* **URL Params**

    uuid=[string]

    example: 61dc8fe5-cc7e-4f06-9a8f-a21a3da00216

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```
    
 * Sample Call

    ```
    curl -X PUT http://localhost:9000/api/v1/customers/61dc8fe5-cc7e-4f06-9a8f-a21a3da00216
    ```

## Upload Customer photo

* **URL**

    /api/v1/customers/:uuid/photos/upload

* **Method**

    POST

* **URL Params**

    uuid=[string]

    example: 61dc8fe5-cc7e-4f06-9a8f-a21a3da00216

* **Data params**

    multipart/form-data

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```

    * **Code**: 400 BAD REQUEST

        ```{"errorMessage" : "Image extension not supported. Supported: jpg, png"} ```
    
 * Sample Call

    ```
    curl -X POST \
            http://localhost:9000/api/v1/logout \
            -H 'Cache-Control: no-cache' \
            -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
            -F 'photo
            =@/home/jerome/images/20170716_203255.jpg'
    ```

## Delete User

* **URL**

    /api/v1/customers/:uuid

* **Method**

    DELETE

* **URL Params**

    uuid=[string]

    example: 61dc8fe5-cc7e-4f06-9a8f-a21a3da00216

* **Data params**

    Not required

* Success Response:

    * **Code**: 200 OK

* Error response:

    * **Code**: 401 UNAUTHORIZED 

        ```{"errorMessage" : "You're not allowed to access this resource."} ```
    
 * Sample Call

    ```
    curl -X PUT http://localhost:9000/api/v1/customers/61dc8fe5-cc7e-4f06-9a8f-a21a3da00216
    ```
