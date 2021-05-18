# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

### **Class *BalanceOperationImpl* - *setDate***

**Criteria for *setDate*:**
	

 - validity of date format

**Predicates for *setDate*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of date format | yes |
|                               | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of Date format | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| no | Invalid |  BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setDate(20-03-01) <br />  -> Invalid date||
| yes | Valid |  BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setDate(2020-04-09) <br />  obj.getDate()-> 2020-04-09||

### **Class *BalanceOperationImpl* - *setMoney***

**Criteria for *setMoney*:**
	
 - validity of double money

**Predicates for *setMoney*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of double money | yes |
|                               | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of Double amount | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| no | Invalid |  BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setMoney(null) <br /> -> Invalid entry||
| yes | Valid |  BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setMoney(5000) <br /> obj.getMoney() -> 5000||

### **Class *BalanceOperationImpl* - *setType***

**Criteria for *setType*:**
	
 - validity of string type
 - match of string

**Predicates for *setType*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of string type | yes |
|                               | no |
| string match | "SALE" |
| 				| "ORDER" |
|				| "RETURN" |
| 				| "CREDIT" |
|				| "DEBIT" |
|				| other |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of string type | string match | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|------|
|no		|* 			|Invalid	|	BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setType(null) <br />  -> Invalid||
|yes 	|SALE		|Valid		|	BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br />obj.setType("SALE")<br /> obj.getType()-> "SALE"| |
|yes 	|ORDER		|Valid 	|	BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setType("ORDER")<br /> obj.getType()-> "ORDER"||
|yes 	|RETURN	|Valid 	| 	BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br />  obj.setType("RETURN")<br /> obj.getType()-> "RETURN"||
|yes 	|CREDIT	|Valid 	| 	BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br />  obj.setType("CREDIT")<br /> obj.getType()-> "CREDIT"||
|yes 	|DEBIT 	|Valid 	| 	BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setType("DEBIT")<br /> obj.getType()-> "DEBIT"||
|yes 	|other 	|Valid 	| 	BalanceOperationImpl obj = {123, 2020-03-01, 10000, "ORDER"} <br /> obj.setType("test")<br /> obj.getType()-> ""||



 ### **Class *CreditCard* - method *checkValidity***

**Criteria for method *checkValidity*:**
	

 - validity of String creditCard (null, empty string) 
 - creditCard represents a number 
 - creditCard passes lunh algorithm test

**Predicates for method *checkValidity*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of String creditCard | yes |
|                               | no |
| creditCard represents a number | yes |
|                                 | no |
| creditCard passes luhn algorithm | yes |
|                                  | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of String creditCard | creditCard represents a number | creditCard passes luhn algorithm | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
| no | * | * | Invalid | T0a(null ; false) <br /> T0b("" ; false) | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityWithInvalidString|
| * | no | * | Invalid | T1("01ab45" ; false) | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityWithCreditCardNotANumber |
| yes | yes | yes | Valid | T2 ("4485370086510891" ; true) | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityLuhnCheckPassed |
| " | " | no | Valid | T3("4485370086510892"" ; false) | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityLuhnCheckNotPassed |


 ### **Class *CreditCard* - method *setNumber***

**Criteria for method *setNumber*:**
	
 - validity of String number 

**Predicates for method *setNumber*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of String number | yes |
|                           | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of String number| Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| no | Invalid | CreditCard c1 = {"4485370086510891", 100.00} <br/> c1.setNumber(null) <br/> -> InvalidCreditCardException| src/test/java/it/polito/ezshop/test/CreditCardTests.testSetNumberWithInvalidString|
| yes | Valid |  CreditCard c1 = {"4485370086510891", 100.00} <br/> c1.setNumber("5100293991053009") <br/> c1.getNumber() -> "5100293991053009"| src/test/java/it/polito/ezshop/test/CreditCardTests.testSetNumberWithValidString|


 ### **Class *CreditCard* - method *setBalance***

**Criteria for method *setBalance*:**
	
 - validity of Double balance 

**Predicates for method *setBalance*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of Double balance | yes |
|                            | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of Double balance | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| no | Invalid | CreditCard c1 = {"4485370086510891", 100.00} <br/> c1.setBalance(null) <br/> c1.getBalance() -> 100.00 | src/test/java/it/polito/ezshop/test/CreditCardTests.testSetBalanceWithNull|
| yes | Valid |  CreditCard c1 = {"4485370086510891", 100.00} <br/> c1.setBalance(50.00) <br/> c1.getBalance() -> 50.00 | src/test/java/it/polito/ezshop/test/CreditCardTests.testSetBalanceWithValidBalance|


### **Class *OrderImpl* -  *setBalanceId***

**Criteria for *setBalanceId*:**
	
 - validity of integer balance id
 - balance id is not already present

**Predicates for *setProductCode*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of integer balance id | yes |
|                               | no |
| balance id is already present | yes |
|                  | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| id already present | validity of integer balance id |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| yes | * |Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setBalanceId(1245) <br /> -> Invalid Entry id already present||
| * |no| Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setBalanceId(null) <br /> -> Invalid entry||
| no |yes| Valid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setBalanceId(2134) <br /> obj.getBalanceId()-> 2134||

### **Class *OrderImpl* -  *setProductCode***

**Criteria for *setProductCode*:**
	
 - validity of string ProductCode
 - Product code is not empty

**Predicates for *setProductCode*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of string product code | yes |
|                               | no |
| product code empty | yes |
|                  | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| product code empty | validity of string product code |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| yes | * |Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setProductCode("") <br /> -> Empty String Invalid entry||
| * |no| Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setProductCode(null) <br /> -> Invalid entry||
| no |yes| Valid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setProductCode("AZ12345") <br /> obj.getProductCode()-> AZ12345||



### **Class *OrderImpl* -  *setPricePerUnit***

**Criteria for *setPricePerUnit*:**
	
 - validity of double pricePerUnit
 - validity of sign

**Predicates for *setPricePerUnit*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of double pricePerUnit | yes |
|                               | no |
| validity of sign | yes |
|                  | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of sign | validity of double pricePerUnit |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| no | * |Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setpricePerUnit(-10) <br /> -> Invalid entry||
| * |no| Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setpricePerUnit(null) <br /> -> Invalid entry||
| yes |yes| Valid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setpricePerUnit(5) <br /> obj.getpricePerUnit()-> 5||


### **Class *OrderImpl* -  *setQuantity***

**Criteria for *setQuantity*:**
	
 - validity of integer quantity
 - validity of sign

**Predicates for *setPricePerUnit*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of integer quantity | yes |
|                               | no |
| validity of sign | yes |
|                  | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of sign | validity of integer quantity |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| no | * |Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setQuantity(-10) <br /> -> Invalid entry||
| * |no| Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setQuantity(null) <br /> -> Invalid entry||
| yes |yes| Valid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setQuantity(2) <br /> obj.getQuantity()-> 2||


### **Class *OrderImpl* - *setStatus***

**Criteria for *setStatus*:**
	
 - validity of string status
 - match of status

**Predicates for *setStatus*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of string status | yes |
|                               | no |
| status match | "ISSUED" |
| 				| "PAYED" |
|				| "COMPLETED" |
|				| other |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of string status | status match | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|------|
|no		|* 			|Invalid	|	OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setStatus(null) <br /> -> Invalid Entry||
|yes 	|ISSUED		|Valid		|	OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setStatus("ISSUED") <br /> obj.getStatus()-> ISSUED||
|yes 	|PAYED		|Valid 	|	OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setStatus("PAYED") <br /> obj.getStatus()-> PAYED||
|yes 	|COMPLETED	|Valid 	| 	OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setStatus("COMPLETED") <br /> obj.getStatus()-> COMPLETED||
|yes 	|other 	|Valid 	| 	OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setStatus("TEST") <br /> obj.getStatus()-> ||


### **Class *OrderImpl* -  *setOrderId***

**Criteria for *setOrderId*:**
	
 - validity of integer order id
 - order id is not already present

**Predicates for *setOrderId*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of integer order id | yes |
|                               | no |
| order id is already present | yes |
|                  				| no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of integer order id| order id already present |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| no |*| Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setOrderId(null) <br /> -> Invalid entry||
| yes | yes |Valid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setOrderId(34) <br /> -> Invalid Entry id already present||
| yes |no| Valid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setOrderId(48) <br /> obj.getOrderId()-> 48||


### **Class *OrderImpl* -  *setProduct***

**Criteria for *setProduct*:**
	
 - validity of product

**Predicates for *setProduct*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of product | yes |
|                     | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of product |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|no| Invalid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setProduct(null) <br /> -> Invalid entry||
|yes |Valid |  OrderImpl obj = {newproduct, 4, 12, "ISSUED", 1245, 34} <br /> obj.setProduct(newproduct2) <br /> obj.getProduct()-> newproduct2||


 ### **Class *Position* - method *setPosition***

**Criteria for method *setPosition*:**
	
 - Validity of String position

**Predicates for method *setPosition*:**

| Criteria | Predicate |
| -------- | --------- |
|    Validity of String position      |   yes        |
|          |     no      |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| Validity of String position | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|no|invalid |T0("456def456") <br /> T0("")| src/test/java/it/polito/ezshop/test/PositionTests.testSetPositionWithInvalidString | 
|yes|valid| T1("456-def-456", true) | src/test/java/it/polito/ezshop/test/PositionTests.testSetPositionWithValidString | 




# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.CreditCardTests.testCheckValidityForLoop0Iterations|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.CreditCardTests.testCheckValidityForLoop0Iterations|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.CreditCardTests.testCheckValidityForLoopMultipleIterations|
### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
| CreditCard | 40 - 50 | 0 | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoop0Iterations |
| CreditCard | 40 - 50 | 1 |src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoop1Iterations|
| CreditCard | 40 - 50 | multiple |src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoopMultipleIterations|




