# Unit Testing Documentation

Authors: Giuseppe Salvi, Milad Beigi Harchegani, Roberto Bosio, Naeem Ur Rehman

Date: 19/05/2021

Version: 1.0.0

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
| no | Invalid | CreditCard c1 = {"4485370086510891", 100.00} <br/> c1.setBalance(null) <br/> ->InvalidParameterException | src/test/java/it/polito/ezshop/test/CreditCardTests.testSetBalanceWithNull|
| yes | Valid |  CreditCard c1 = {"4485370086510891", 100.00} <br/> c1.setBalance(50.00) <br/> c1.getBalance() -> 50.00 | src/test/java/it/polito/ezshop/test/CreditCardTests.testSetBalanceWithValidBalance|


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
|no|invalid |T0("456def456") <br />  -> InvalidPositionException <br/> T0("") <br> -> InvalidPositionException | src/test/java/it/polito/ezshop/test/PositionTests.testSetPositionWithInvalidString | 
|yes|valid| T1("456-def-456", true) | src/test/java/it/polito/ezshop/test/PositionTests.testSetPositionWithValidString | 

### **Class *ReturnTransaction* - method  *addEntry***

**Criteria for *addEntry*:**
	
 - validity of TicketEntryImp entry

**Predicates for *addEntry*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of TicketEntryImpl entry | yes |
|                               | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of TicketEntryImp entry |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|no|Invalid |  ReturnTransaction ret1 = {sale1} <br /> obj.addEntry(null) <br /> -> false |src/test/java/it/polito/ezshop/test/ReturnTransactionTests.testAddEntryWithNull|
| yes |Valid |  ReturnTransaction ret1 = {sale1} <br /> obj.addEntry{p1, 10, 0.5} <br /> -> true |src/test/java/it/polito/ezshop/test/ReturnTransactionTests.testAddEntryWithValidInput|

### **Class *ReturnTransaction* - method  *deleteEntry***

**Criteria for *deleteEntry*:**
	
 - validity of string barcode
 - product present in the list

**Predicates for *deleteEntry*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of string barcode | yes |
|                               | no |
| product present in the list | yes |
|                              | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of string barcode | product present in the list | Valid / Invalid | Description of the test case | JUnit test case |
|-------|--------|-------|-------|-------|
|no |* | Invalid |  ReturnTransaction ret1 = {sale1} <br /> ret1.deleteEntry(null) <br /> -> null |src/test/java/it/polito/ezshop/test/ReturnTransactionTests.testDeleteEntryWithNull|
| yes | no| Valid |  ReturnTransaction ret1 = {sale1} <br /> ret1.deleteEntry("012345678912") <br /> -> null|src/test/java/it/polito/ezshop/test/ReturnTransactionTests.testDeleteEntryWithProductNotPresent|
| " | yes | Valid | ReturnTransaction ret1 = {sale1} <br/> ProductTypeImpl = {"012345678912", "apple", 1.50, "apple notes... "} <br /> TicketEntryImpl t1 = {p1, 10, 0.5} <br/> ret1.addEntry(t1) <br/> ret1.deleteEntry("012345678912") <br/> -> t1 |src/test/java/it/polito/ezshop/test/ReturnTransactionTests.testDeleteEntryWithProductPresent|

### **Class *SaleTransactionImpl* - method  *addEntry***

**Criteria for *addEntry*:**
	
 - validity of TicketEntry entry


**Predicates for *addEntry*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of TicketEntry entry | yes |
|                               | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of TicketEntry entry |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
| no |Invalid |  SaleTransactionImpl sale1 = {} <br /> obj.addEntry(null) <br /> -> false|src/test/java/it/polito/ezshop/test/SaleTransactionImplTests.testAddEntryWithNull|
| yes |Valid |  SaleTransactionImpl sale1 = {} <br /> obj.addEntry{product, 10, 0.5} <br /> -> true |false|src/test/java/it/polito/ezshop/test/SaleTransactionImplTests.testAddEntryWithValidInput|


### **Class *SaleTransactionImpl* - method  *deleteEntry***

**Criteria for *deleteEntry*:**
	
 - validity of string barcode
 - product present in the list
 

**Predicates for *deleteEntry*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of string barcode | yes |
|                               | no |
| product present in the list | yes |
|                              | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of string barcode | product present in the list | Valid / Invalid | Description of the test case | JUnit test case |
|-------|--------|-------|-------|-------|
|no |* | Invalid |   SaleTransactionImpl sale1 = {} <br /> sale1.deleteEntry(null) <br /> -> null |src/test/java/it/polito/ezshop/test/SaleTransactionImplTests.testDeleteEntryWithNull|
| yes | no| Valid |  SaleTransactionImpl sale1 = {} <br /> sale1.deleteEntry("012345678912") <br /> -> null|src/test/java/it/polito/ezshop/test/SaleTransactionImplTests.testDeleteEntryWithProductNotPresent|
| " | yes | Valid | SaleTransactionImpl sale1 = {} <br/> ProductTypeImpl = {"012345678912", "apple", 1.50, "apple notes... "} <br /> TicketEntryImpl t1 = {p1, 10, 0.5} <br/> sale1.addEntry(t1) <br/> sale1.deleteEntry("012345678912") <br/> -> t1 |src/test/java/it/polito/ezshop/test/SaleTransactionImplTests.testDeleteEntryWithProductPresent|




# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoop0Iterations|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoop1Iterations|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoopMultipleIterations|
| BalanceOperationImpl | src/test/java/it/polito/ezshop/test/BalanceOperationImplTests.testGettersSettersConstructors|
| OrderImpl | src/test/java/it/polito/ezshop/test/OrderImplTests.testGettersSettersConstructors |
| ReturnTransaction | src/test/java/it/polito/ezshop/test/ReturnTransactionTests.testGettersSettersConstructors |
| SaleTransactionImpl | src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testGettersSettersConstructors |
| TicketEntryImpl | src/test/java/it/polito/ezshop/test/TicketEntryImplTests.testGettersSettersConstructors |
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testGettersSettersConstructors|
| LoyaltyCard | src/test/java/it/polito/ezshop/test/LoyaltyCardTests.testGettersSettersConstructors |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGettersSettersConstructors |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testGettersSettersConstructors |
| ReturnTransaction | src/test/java/it/polito/ezshop/test/ReturnTransaction.testDeleteEntryForLoop0Iterations |
| ReturnTransaction | src/test/java/it/polito/ezshop/test/ReturnTransaction.testDeleteEntryForLoop1Iterations |
| ReturnTransaction | src/test/java/it/polito/ezshop/test/ReturnTransaction.testDeleteEntryForLoopMultipleIterations |
| SaleTransactionImpl| src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testDeleteEntryForLoop0Iterations |
| SaleTransactionImpl| src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testDeleteEntryForLoop1Iterations |
| SaleTransactionImpl| src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testDeleteEntryForLoopMultipleIterations |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImpl.testCheckBarCodeForLoopString12 | 
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImpl.testCheckBarCodeForLoopString13 | 
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImpl.testCheckBarCodeForLoopString14 | 

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
| ReturnTransaction | 112 - 115 | 0 | src/test/java/it/polito/ezshop/test/ReturnTransaction.testDeleteEntryForLoop0Iterations |
| ReturnTransaction | 112 - 115 | 1 | src/test/java/it/polito/ezshop/test/ReturnTransaction.testDeleteEntryForLoop1Iterations |
| ReturnTransaction | 112 - 115 | multiple | src/test/java/it/polito/ezshop/test/ReturnTransaction.testDeleteEntryForLoopMultipelIterations |
| SaleTransactionImpl | 165 - 169 | 0 | src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testDeleteEntryForLoop0Iterations |
| SaleTransactionImpl | 165 - 169 | 1 | src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testDeleteEntryForLoop1Iterations |
| SaleTransactionImpl | 165 - 169 | multiple | src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testDeleteEntryForLoopMultipelIterations |
| ProductTypeImpl | 70 - 72 | 11 | src/test/java/it/polito/ezshop/test/ProductTypeImpl.testCheckBarCodeForLoopString12 |
| ProductTypeImpl | 70 - 72 | 12 | src/test/java/it/polito/ezshop/test/ProductTypeImpl.testCheckBarCodeForLoopString13 |
| ProductTypeImpl | 70 - 72 | 13 | src/test/java/it/polito/ezshop/test/ProductTypeImpl.testCheckBarCodeForLoopString14 |





