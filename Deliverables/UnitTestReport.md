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


 ### **Class *class_name* - method *name***



**Criteria for method *name*:**
	

 - 
 - 





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||




# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||
|||
||||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



