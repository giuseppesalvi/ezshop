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
|no|invalid |T0("456def456") <br /> T0("")| src/test/java/it/polito/ezshop/test/PositionTests.testSetPositionWithInvalidString | 
|yes|valid| T1("456-def-456", true) | src/test/java/it/polito/ezshop/test/PositionTests.testSetPositionWithValidString | 

### **Class *ReturnTransaction* - method  *addEntry***

**Criteria for *addEntry*:**
	
 - validity of TicketEntryImp entry

**Predicates for *addEntry*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of TicketEntryImp entry | yes |
|                               | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of TicketEntryImp entry |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|no|Invalid |  ReturnTransaction obj = {129, new_transaction, List_products, "OPEN", true, 199823} <br /> obj.addEntry{product, true, 0.5} <br /> -> Invalid Entry||
| yes |Valid |  ReturnTransaction obj = {129, new_transaction, List_products, "OPEN", true, 199823} <br /> obj.addEntry{product, 10, 0.5} <br /> -> true ||

### **Class *ReturnTransaction* - method  *deleteEntry***

**Criteria for *deleteEntry*:**
	
 - validity of string barcode

**Predicates for *deleteEntry*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of string barcode | yes |
|                               | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of string barcode |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|no|Invalid |  ReturnTransaction obj = {129, new_transaction, List_products, "OPEN", true, 199823} <br /> obj.deleteEntry(null) <br /> -> Invalid Entry||
| yes |Valid |  ReturnTransaction obj = {129, new_transaction, List_products, "OPEN", true, 199823} <br /> obj.deleteEntry("AZ1234") <br /> -> AZ1234 ||

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
|no|Invalid |  SaleTransactionImpl obj = {129, List_products,199823, "OPEN", "2020-01-12", "timeString", 212934, "cash", "creditcardString" } <br /> obj.addEntry{product, true, 0.5} <br /> -> Invalid Entry||
| yes |Valid |  SaleTransactionImpl obj = {129, List_products,199823, "OPEN", "2020-01-12", "timeString", 212934, "cash", "creditcardString" } <br /> obj.addEntry{product, 10, 0.5} <br /> -> true ||

### **Class *SaleTransactionImpl* - method  *deleteEntry***

**Criteria for *deleteEntry*:**
	
 - validity of string barcode

**Predicates for *deleteEntry*:**

| Criteria | Predicate |
| -------- | --------- |
| validity of string barcode | yes |
|                               | no |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |

**Combination of predicates**:

| validity of string barcode |Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|no|Invalid | SaleTransactionImpl obj = {129, List_products,199823, "OPEN", "2020-01-12", "timeString", 212934, "cash", "creditcardString" } <br /> obj.deleteEntry(null) <br /> -> Invalid Entry||
| yes |Valid |  SaleTransactionImpl obj = {129, List_products,199823, "OPEN", "2020-01-12", "timeString", 212934, "cash", "creditcardString" }  <br /> obj.deleteEntry("AZ1234") <br /> -> AZ1234 ||






# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoop0Iterations|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoop0Iterations|
| CreditCard | src/test/java/it/polito/ezshop/test/CreditCardTests.testCheckValidityForLoopMultipleIterations|
| BalanceOperationImpl | src/test/java/it/polito/ezshop/test/BalanceOperationImplTests.testGettersSettersConstructors|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testGetCustomerName|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testSetCustomerName|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testGetCustomerCard|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testSetCustomerCard|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testGetId|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testSetId|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testGetPoints|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testSetPoints|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testGetCard|
| CustomerImpl | src/test/java/it/polito/ezshop/test/CustomerImplTests.testSetCard|
| LoyaltyCard | src/test/java/it/polito/ezshop/test/LoyaltyCardTests.testGetPoints |
| LoyaltyCard | src/test/java/it/polito/ezshop/test/LoyaltyCardTests.testSetPoints |
| LoyaltyCard | src/test/java/it/polito/ezshop/test/LoyaltyCardTests.testGetCustomer |
| LoyaltyCard | src/test/java/it/polito/ezshop/test/LoyaltyCardTests.testSetCustomer |
| LoyaltyCard | src/test/java/it/polito/ezshop/test/LoyaltyCardTests.testGetCardId |
| LoyaltyCard | src/test/java/it/polito/ezshop/test/LoyaltyCardTests.testSetCardId |
| OrderImpl | src/test/java/it/polito/ezshop/test/OrderImplTests.testGettersSettersConstructors |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetQuantity |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testSetQuantity |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetLocation |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testSetLocation |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetNote |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testSetNote |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetProductDescription |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testSetProductDescription |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetBarCode |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testSetBarCode |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetPricePerUnit |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testSetPricePerUnit |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetId |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testSetId |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testGetEliminated |
| ProductTypeImpl | src/test/java/it/polito/ezshop/test/ProductTypeImplTests.testInvertEliminated |
| ReturnTransaction | src/test/java/it/polito/ezshop/test/ReturnTransactionTests.testGettersSettersConstructors |
| SaleTransactionImpl | src/test/java/it/polito/ezshop/test/SaleTransactionImpl.testGettersSettersConstructors |
| TicketEntryImpl | src/test/java/it/polito/ezshop/test/TicketEntryImplTests.testGettersSettersConstructors |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testUserImpl |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testGetId |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testSetIdWithValidId |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testGetUsername |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testSetUsername |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testGetPassword |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testSetPassword |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testGetRole |
| UserImpl | src/test/java/it/polito/ezshop/test/UserImplTests.testSetRole |

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




