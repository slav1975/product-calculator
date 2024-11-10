# Title
Product Calculator
# Description
A service that will provide a REST API for calculating a given product's price and amount.
## Table of Contents
- [Installation](#installation)
- [Configuration](#configuration) 
- [Usage](#usage)
- [Assumptions](#assumptions)
## Installation
1. Clone the repository:

    ```bash
    git clone https://github.com/slav1975/product-calculator
    ```
    and move to *product-calculator* directory.

2. Run application 

    - **Local machine**

        If you have installed JDK (>=17) environment on your local machine you can start application by Gradle:
        ```bash
        ./gradlew clean bootRun
        ```
        (for Windows OS gradlew.bat)

      Close application by Ctrl-C.
   
    - **Docker**

      Starting application in Docker environment requires build Docker's image
      ```bash
      docker build --tag r11/product-calculator .
      ```
      after that, run **docker-compose.yaml**
      
      ```bash
      docker compose up
      ```

      Close application by command:

      ```bash
      docker compose down --remove-orphans
      ```
3.  Open web-browser and put http://localhost:4000/swagger-ui/index.html. Swagger initial page should be displayed.

## Configuration

Before any calculation will be conducted following cofiguration steps MUST be applied.

  1. Initialization **product** portfolio. For example by following POST request:

      ```bash
        curl --location 'http://localhost:4000/products' \
             --header 'Content-Type: application/json' \
             --data 
             '[
                 {
                     "name": "Licensed Soft Hat",
                     "price": "797.00"
                 },
                 {
                     "name": "Bespoke Steel Bacon",
                     "price": "821.00"
                 },
                 {
                     "name": "Electronic Fresh Shoes",
                     "price": "826.00"
                 }
             ]'
        ```
      or by Swagger UI http://localhost:4000/swagger-ui/index.html#/product-controller/create.
   
      List of saved can be listed by:
      ```bash
             curl --location 'http://localhost:4000/products'
      ```
      In response we get products with id's.

      ```bash
         curl --location 'http://localhost:4000/products'
         [
           {
           "id": "4a72f604-8fca-41f7-ac3a-dbe122b1e2c4",
           "name": "Licensed Soft Hat",
           "price": "797.00"
           },
           {
           "id": "66d9aea9-fbe6-49c9-87bb-de1f71805596",
           "name": "Bespoke Steel Bacon",
           "price": "821.00"
           },
           {
           "id": "0c5133ad-f0d7-4267-b375-974180186690",
           "name": "Electronic Fresh Shoes",
           "price": "826.00"
           }
         ]
      ```
  2. Initialization **discount policies** (short description in the snipped below):
       - Amount based discount
         ```bash
           curl --location 'http://localhost:4000/discounts' \
            --header 'Content-Type: application/json' \
            --data '
                {
                    "enabled": true, // Will be active
                    "policyType": "AMOUNT", //Amount based discount
                    "discountValueList": [
                            {
                            "from": 50, // From 50 items a given product 1 [currency unit == 1] discount is applied
                            "value": 1 
                            }
                            {
                            "from": 150, // From 150 items a given product 3 [currency unit == 1] discount is applied
                            "value": 3
                            },
                            {
                            "from": 200,
                            "value": 4
                            },
                            {
                            "from": 300,
                            "value": 5
                            },
                            {
                            "from": 600,
                            "value": 10
                            }
                    ]
                }
         ```
       - Percentage based discount (short description in the snipped below):
           ```bash
                   curl --location 'http://localhost:4000/discounts' \
                   --header 'Content-Type: application/json' \
                   --data '{
                   "enabled": true,
                   "policyType": "PERCENTAGE", //Percentage based discount 
                   "discountValueList": [
                   {
                       "from": 1000,  // From 1000 items of the order 1% of value is applied
                       "value": 1
                   },
                   {
                       "from": 2000,  // From 2000 items of the order 4% of value is applied 
                       "value": 4
                   },
                   {
                       "from": 4000,
                       "value": 5
                   }
                   ]
                   }'
           ```
## Usage

Calculation is processed by request

```bash
curl --location 'http://localhost:4000/calculate' \
--header 'Content-Type: application/json' \
--data '{
    "01e313b1-cc4f-427f-809d-e912f992385f": 2000, // We calculate 2000 of pices of product with id 01e313b1-cc4f-427f-809d-e912f992385f
    "e1f43c30-6dc1-4125-93a7-14e79249ae4b": 3800,
    "5ee964c3-d433-4e08-b62a-fec3a7a97463": 1000
}'
```
## Assumptions

Following assumptions are considered in configuration and calculation:

- In application currency unit is put 1.
- Many policies can be added.
- Policies can enabled and disabled. But only one for percentage based and one for amount based policies can be *enable*. http://localhost:4000/swagger-ui/index.html#/discount-policy-controller/activateDiscountPolicy enables/disables policy.
- All real examples are in [here(postman-collection) in Postman collection format
- 