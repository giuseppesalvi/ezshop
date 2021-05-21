# Integration and API Test Documentation

Authors:

Date:

Version:

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>

```plantuml
@startuml
class EzShop {}
class FileWrite {}
class FileRead {}
class BalanceOperationImpl {}
class CreditCard {}
class CustomerImpl {}
class LoyaltyCard {}
class OrderImpl {}
class Position {}
class ProductTypeImpl {}
class ReturnTransaction {}
class SaleTransactionImpl {}
class TicketEntryImpl {}
class UserImpl {}

FileWrite --> BalanceOperationImpl
FileWrite --> CustomerImpl
FileWrite --> LoyaltyCard
FileWrite --> OrderImpl 
FileWrite --> Position 
FileWrite --> ProductTypeImpl 
FileWrite --> ReturnTransaction 
FileWrite --> SaleTransactionImpl 
FileWrite --> UserImpl
FileWrite --> CreditCard
FileWrite --> TicketEntryImpl

FileRead --> BalanceOperationImpl
FileRead --> CustomerImpl
FileRead --> LoyaltyCard
FileRead --> OrderImpl 
FileRead --> Position 
FileRead --> ProductTypeImpl 
FileRead --> ReturnTransaction 
FileRead --> SaleTransactionImpl 
FileRead --> UserImpl
FileRead --> CreditCard
FileRead --> TicketEntryImpl

EzShop --> FileRead
EzShop --> FileWrite
EzShop --> BalanceOperationImpl
EzShop --> CustomerImpl
EzShop --> LoyaltyCard
EzShop --> OrderImpl 
EzShop --> Position 
EzShop --> ProductTypeImpl 
EzShop --> ReturnTransaction 
EzShop --> SaleTransactionImpl 
EzShop --> UserImpl
EzShop --> CreditCard
EzShop --> TicketEntryImpl

OrderImpl --> ProductTypeImpl
OrderImpl --> BalanceOperationImpl

ProductTypeImpl --> Position

CustomerImpl --> LoyaltyCard

LoyaltyCard --> CustomerImpl

SaleTransactionImpl --> TicketEntryImpl
SaleTransactionImpl --> CreditCard
SaleTransactionImpl --> BalanceOperationImpl

ReturnTransaction --> TicketEntryImpl
ReturnTransaction --> SaleTransactionImpl
@enduml
```
     
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>
    


#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1
| Classes  | JUnit test cases |
|--|--|
|||


## Step 2
| Classes  | JUnit test cases |
|--|--|
|||


## Step n 

   

| Classes  | JUnit test cases |
|--|--|
|||




# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |  
|  2     |  ... |

##### Scenario 3.4
| Scenario |  Order of product type X, balance not enough |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S exists and is logged in |
| | Product type X exists |
| | Order O exists | 
|  Post condition     | Order O does not change  |
| | X.units not changed |
| | Balance not changed |
| Step#        | Description  |
|  1    |  S search for Order O |  
|  2    |  S register payment done for O |
|  3    |  Error: balance not enough |



# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
| 3-1 | FR4 | testIssueOrderNominalCase|             
| 3-2 | FR4 | testPayOrderNominalCase|             
| 3-3 | FR4 | testRecordOrderArrivalNominalCase | 
| 3-4 | FR4 | testPayOrderBalanceNotEnough |  
| 4-1 | FR5 | testDefineCustomerNominalCase|             
| 4-2 | FR5 | testAttachCardToCustomerNominalCase<br />testModifyCustomerNominalCase|             
| 4-3 | FR5 | testModifyCustomerModifyCard |             
| 4-4 | FR5 | testModifyCustomerNominalCase|              



# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
| NRF6 |  testCreateCardNominalCase         |


