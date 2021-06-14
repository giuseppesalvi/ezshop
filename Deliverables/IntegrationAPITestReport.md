# Integration and API Test Documentation

Authors: Giuseppe Salvi, Milad Beigi Harchegani, Roberto Bosio, Naeem Ur Rehman 

Date: 12/06/2021

Version: 1.1.0


# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

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
class Product{}

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
FileWrite --> Product

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
FileRead --> Product

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
EzShop --> Product

OrderImpl --> ProductTypeImpl
OrderImpl --> BalanceOperationImpl

ProductTypeImpl --> Position

CustomerImpl --> LoyaltyCard

LoyaltyCard --> CustomerImpl

SaleTransactionImpl --> TicketEntryImpl
SaleTransactionImpl --> CreditCard
SaleTransactionImpl --> BalanceOperationImpl
SaleTransactionImpl --> Product

ReturnTransaction --> TicketEntryImpl
ReturnTransaction --> SaleTransactionImpl
ReturnTransaction --> Product

Product --> ProductTypeImpl
@enduml
```
     
# Integration approach

## Integration sequence adopted : Bottom up

### step1: (Unit testing) 
classes BalanceOperationImpl + CreditCard + CustomerImpl + LoyaltyCard + OrderImpl + Position + ProductTypeImpl + ReturnTransaction + SaleTransactionImpl + TicketEntryImpl + UserImpl + Product

### step2: 
classes BalanceOperationImpl + CreditCard + CustomerImpl + LoyaltyCard + OrderImpl + Position + ProductTypeImpl + ReturnTransaction + SaleTransactionImpl + TicketEntryImpl + UserImpl + Product + FileRead + FileWrite

### step3: (API testing) 
classes BalanceOperationImpl + CreditCard + CustomerImpl + LoyaltyCard + OrderImpl + Position + ProductTypeImpl + ReturnTransaction + SaleTransactionImpl + TicketEntryImpl + UserImpl + Product + FileRead + FileWrite + EZShop 


#  Tests

## Step 1
| Classes  | JUnit test cases |
|--|--|
| BalanceOperationImpl | testGettersSettersConstructors |
| CreditCard | testCheckValidityWithInvalidString |
|| testCheckValidityWithCreditCardNotANumber |
|| testCheckValidityLuhnCheckPassed |
|| testCheckValidityLuhnCheckNotPassed |
|| testSetNumberWithInvalidString |
|| testSetNumberWithValidString |
|| testSetBalanceWithNull |
|| testSetBalanceWithValidBalance |
|| testCheckValidityForLoop0Iterations |
|| testCheckValidityForLoop1Iterations |
|| testCheckValidityForLoopMultipleIterations |
| CustomerImpl | testGettersSettersConstructors |
| LoyaltyCard | testGettersSettersConstructors |
| OrderImpl | testGettersSettersConstructors|
| Position | testSetPositionWithValidString |
|| testSetPositionWithInvalidString |
| ProductTypeImpl | testCheckBarCodeNull |
|| testCheckBarCodeWithLongString |
|| testCheckBarCodeInvalidCheckSum |
|| testCheckBarCodeValidString |
|| testGettersSettersConstructors |
|| testCheckBarCodeForLoopString12 |
|| testCheckBarCodeForLoopString13 |
|| testCheckBarCodeForLoopString14 |
| ReturnTransaction | testGettersSettersConstructors |
|| testAddEntryWithNull|
|| testAddEntryWithValidInput |
|| testDeleteEntryWithNull |
|| testDeleteEntryWithProductNotPresent |
|| testDeleteEntryWithProductPresent |
|| testDeleteEntryForLoop0Iterations |
|| testDeleteEntryForLoop1Iterations |
|| testDeleteEntryForLoopMultipleIterations |
| SaleTransactionImpl | testGettersSettersConstructors|
|| testAddEntryWithNull|
|| testAddEntryWithValidInput |
|| testDeleteEntryWithNull |
|| testDeleteEntryWithProductNotPresent |
|| testDeleteEntryWithProductPresent |
|| testDeleteEntryForLoop0Iterations |
|| testDeleteEntryForLoop1Iterations |
|| testDeleteEntryForLoopMultipleIterations |
| TicketEntryImpl | testGettersSettersConstructors | 
| UserImpl | testGettersSettersConstructors |
| Product | testGettersSettersConstructors |


## Step 2
| Classes  | JUnit test cases |
|--|--|
| FileRead + FileWrite | testReadAndWriteUsers |
| | testReadWriteProducts |
| | testReadWriteOrders |
| | testReadWriteCustomers |
| | testReadWriteCards |
| | testReadWriteSales |
| | testReadWriteReturns |
| | testReadWriteOperations |
| | testReadWriteCreditCards |


## Step 3    
| Classes  | JUnit test cases |
|--|--|
|   EzShop   | testResetNominalCase | 
| | testCreateUserWithInvalidUsername |
| | testCreateUserWithInvalidPassword |
| | testCreateUserWithInvalidRole |
| | testCreateUserWithUsernameAlreadyUsed |
| | testCreateUserNominalCase |
| | testDeleteUserWithUnauthorizedUser |
| | testDeleteUserWithInvalidUserId |
| | testDeleteUserWithNonExistingUser |
| | testDeleteUserNominalCase |
| | testGetAllUsersWithUnauthorizedUser |
| | testGetAllUsersNominalCase |
| | testGetUserWithUnauthorizedUser |
| | testGetUserWithInvalidUserId |
| | testGetUserWithNonExistingUser |
| | testGetUserNominalCase |
| | testUpdateUserRightsWithUnauthorizedUser |
| | testUpdateUserRightsWithInvalidUserId |
| | testUpdateUserRightsWithInvalidRole |
| | testUpdateUserRightsWithNonExistingUser |
| | testUpdateUserRightsNominalCase |
| | testLoginWithInvalidUsername |
| | testLoginWithInvalidPassword |
| | testLoginWithNonExistingUser |
| | testLoginWithWrongCredentials |
| | testLoginNominalCase |
| | testLogoutWithNoLoggedUser |
| | testLogoutNominalCase |
| | testCreateProductTypeNominalCase |
| | testCreateProductTypeUnauthorized |
| | testCreateProductTypeInvalidProductCode |
| | testCreateProductTypeInvalidProductDescription |
| | testCreateProductTypeInvalidPrice |
| | testCreateProductTypeBarcodeAlreadyExists |
| | testUpdateProductNominalCase |
| | testUpdateProductUnauthorized |
| | testUpdateProductInvalidProductCode |
| | testUpdateProductInvalidProductDescription |
| | testUpdateProductInvalidPrice |
| | testUpdateProductInvalidId |
| | testUpdateProductProductNotExists |
| | testUpdateProductBarcodeAlreadyUsed |
| | testDeleteProductTypeNominalCase |
| | testDeleteProductTypeUnauthorized |
| | testDeleteProductTypeInvalidId |
| | testDeleteProductTypeProductNotExists |
| | testGetAllProductTypesNominalCase |
| | testGetAllProductTypesUnauthorized |
| | testGetProductTypeByBarcodeNominalCase |
| | testGetProductTypeByBarcodeUnauthorized |
| | testGetProductTypeByBarcodeInvalidBarcode |
| | testGetProductTypeByBarcodeProductNotExists |
| | testGetProductTypesByDescriptionNominalCase |
| | testGetProductTypesByDescriptionUnauthorized |
| | testGetProductTypesByDescriptionProductNotExists |
| | testGetProductTypesByDescriptionPartialMatch |
| | testGetProductTypesByDescriptionNullDescription |
| | testUpdateQuantityNominalCase |
| | testUpdateQuantityUnauthorized |
| | testUpdateQuantityInvalidID |
| | testUpdateQuantityProductNotExists |
| | testUpdateQuantityProductNegativeQuantity |
| | testUpdateQuantityProductNoLocation |
| | testUpdatePositionNominalCase |
| | testUpdatePostionUnauthorized |
| | testUpdatePositionInvalidID |
| | testUpdatePositionProductNotExists |
| | testUpdatePositionInvalidFormat |
| | testUpdatePositionNotUnique |
| | testUpdatePositionNullShouldReset |  
| | testIssueOrderNominalCase |
| | testIssueOrderUnauthorized |
| | testIssueOrderInvalidProductCode |
| | testIssueOrderInvalidQuantity |
| | testIssueOrderInvalidPricePerUnit |
| | testIssueOrderProductNotExists |
| | testPayOrderForNominalCase |
| | testPayOrderForUnauthorized |
| | testPayOrderForInvalidProductCode |
| | testPayOrderForInvalidQuantity |
| | testPayOrderForInvalidPricePerUnit |
| | testPayOrderForProductNotExists |
| | testPayOrderForBalanceNotEnough |
| | testPayOrderNominalCase |
| | testPayOrderUnauthorized |
| | testPayOrderNotExists |
| | testPayOrderInvalidOrderID |
| | testPayOrderBalanceNotEnough |
| | testPayOrderAlreadyReceived |
| | testPayOrderAlreadyPayed |
| | testRecordOrderArrivalNominalCase |
| | testRecordOrderArrivalUnauthorized |
| | testRecordOrderArrivalInvalidOrderID |
| | testRecordOrderArrivalLocationNotExists |
| | testRecordOrderArrivalAlreadyArrived |
| | testRecordOrderArrivalNotPayed |
| | testRecordOrderArrivalRFIDNominalCase |
| | testRecordOrderArrivalRFIDUnauthorized |
| | testRecordOrderArrivalRIDInvalidOrderID |
| | testRecordOrderArrivalRFIDInvalidRFID |
| | testRecordOrderArrivalRFIDLocationNotExists |
| | testRecordOrderArrivalRFIDAlreadyArrived |
| | testRecordOrderArrivalRFIDAlreadyPresent |
| | testRecordOrderArrivalRFIDNotPayed |
| | testGetAllOrdersNominalCase |
| | testGetAllOrdersUnauthorized |
| | testDefineCustomerNominalCase |
| | testDefineCustomerUnauthorized |
| | testDefineCustomerInvalidName |
| | testDefineCustomerAlreadyExists |
| | testModifyCustomerNominalCase |
| | testModifyCustomerUnauthorized |
| | testModifyCustomerInvalidName |
| | testModifyCustomerInvalidID |
| | testModifyCustomerInvalidCard |
| | testModifyCustomerCustomerNotExists |
| | testModifyCustomerModifyCard |
| | testDeleteCustomerNominalCase |
| | testDeleteCustomerUnauthorized |
| | testDeleteCustomerInvalidID |
| | testDeleteCustomerCustomerNotExists |
| | testGetCustomerNominalCase |
| | testGetCustomerUnauthorized |
| | testGetCustomerInvalidID |
| | testGetCustomerCustomerNotExists |
| | testGetAllCustomersNominalCase |
| | testGetAllCustomerUnauthorized |
| | testCreateCardNominalCase |
| | testCreateCardUnauthorized |
| | testAttachCardToCustomerNominalCase |
| | testAttachCardToCustomerUnauthorized |
| | testAttachCardToCustomerInvalidID |
| | testAttachCardToCustomerInvalidCardID |
| | testAttachCardToCustomerVariousErrors |
| | testModifyPointsOnCardNominalCase |
| | testModifyPointsOnCardUnauthorized |
| | testModifyPointsOnCardInvalidCardID |
| | testModifyPointsOnCardNegativeBalance |
| | testStartSaleTransactionWithUnauthorizedUser |
| | testStartSaleTransactionNominalCase |
| | testAddProductToSaleWithUnauthorizedUser |
| | testAddProductToSaleWithQuantityLessThanZero |
| | testAddProductToSaleWithInvalidProductCode |
| | testAddProductToSaleWithInvalidTransactionId |
| | testAddProductToSaleProductCodeDoesNotExist |
| | testAddProductToSaleQuantityOfProductCannotSatisfyRequest |
| | testAddProductToSaleTransactionIdDoesNotIdentifyAnOpenTransaction |
| | testAddProductToSaleNominalScenario |
| | testAddProductToSaleRFIDWithUnauthorizedUser |
| | testAddProductToSaleRFIDWithInvalidProductCode |
| | testAddProductToSaleRFIDWithInvalidTransactionId |
| | testAddProductToSaleRFIDProductCodeDoesNotExist |
| | testAddProductToSaleRFIDTransactionIdDoesNotIdentifyAnOpenTransaction |
| | testAddProductToSaleRFIDNominalScenario |
| | testDeleteProductFromSaleWithUnauthorizedUser |
| | testDeleteProductFromSaleWithQuantityLessThanZero |
| | testDeleteProductFromSaleWithInvalidProductCode |
| | testDeleteProductFromSaleWithInvalidTransactionId |
| | testDeleteProductFromSaleProductCodeDoesNotExist |
| | testDeleteProductFromSaleQuantityOfProductCannotSatisfyRequest |
| | testDeleteProductFromSaleTransactionIdDoesNotIdentifyAnOpenTransaction |
| | testDeleteProductFromSaleTransactionNominalCase |
| | testDeleteProductFromSaleRFIDWithUnauthorizedUser |
| | testDeleteProductFromSaleRFIDWithInvalidProductCode |
| | testDeleteProductFromSaleRFIDWithInvalidTransactionId |
| | testDeleteProductFromSaleRFIDProductCodeDoesNotExist |
| | testDeleteProductFromSaleRFIDTransactionIdDoesNotIdentifyAnOpenTransaction |
| | testDeleteProductFromSaleRFIDTransactionNominalCase | 
| | testApplyDiscountRateToProductWithInvalidUser |
| | testAppyDiscountRateToProductWithInvalidDiscountRate |
| | testApplyDiscountRateToProductWithInvalidProductCode |
| | testApplyDiscountRateToProductWithInvalidTransactionId |
| | testApplyDiscountRateToProductProductCodeDoesNotExist |
| | testApplyDiscountRateToProductTransactionIsNotOpen |
| | testApplyDiscountRateToProductNominalCase |
| | testApplyDiscountRateToSaleWithInvalidUser |
| | testAppyDiscountRateToSaleWithInvalidDiscountRate |
| | testApplyDiscountRateToSaleWithInvalidTransactionId |
| | testApplyDiscountRateToSaleTransactionIsNotOpenOrClosed |
| | testApplyDiscountRateToSaleNominalCase |
| | testComputePointsForSaleWithUnauthorizedUser |
| | testComputePointsForSaleWithInvalidTransactionId |
| | testComputePointsForSaleWithNonExistingTransaction |
| | testComputePointsForSaleNominalCase |
| | testEndSaleTransactionWithUnauthorizedUser |
| | testEndSaleTransactionWithInvalidTransactionId |
| | testEndSaleTransactionWithNotOpenTransaction |
| | testEndSaleTransactionNominalCase |
| | testDeleteSaleTransactionWithUnauthorizedUser |
| | testDeleteSaleTransactionWithInvalidTransactionId |
| | testDeleteSaleTransactionWithAlreadyPayedTransaction |
| | testDeleteSaleTransactionNominalCase |
| | testGetSaleTransactionWithUnauthorizedUser |
| | testGetSaleTransactionWithInvalidTransactionId |
| | testGetSaleTransactionWithOpenTransaction |
| | testGetSaleTransactionNominalCase |
| | testStartReturnTransactionWithUnauthorizedUser |
| | testStartReturnTransactionWithInvalidTransactionID |
| | testStartReturnTransactionWithTransactionNotAvailableAndPayed |
| | testStartReturnTransactionNominalCase |
| | testReturnProductWithUnauthorizedUser |
| | testReturnProductWithInvalidTransactionID |
| | testReturnProductWithInvalidQuantity |
| | testReturnProductWithInvalidProductCode |
| | testReturnProductWhenTransactionDoesNotExist |
| | testReturnProductWhenProductToBeReturnedDoesNotExist |
| | testReturnProductWhenProductNotInTheSaleTransaction |
| | testReturnProductWhenAmountHigherThanSoldOne |
| | testReturnProductNominalCase |
| | testReturnProductRFIDWithUnauthorizedUser |
| | testReturnProductRFIDWithInvalidTransactionID |
| | testReturnProductRFIDWithInvalidRFID |
| | testReturnProductRFIDWhenTransactionDoesNotExist |
| | testReturnProductRFIDWhenProductToBeReturnedDoesNotExist |
| | testReturnProductRFIDWhenProductNotInTheSaleTransaction |
| | testReturnProductRFIDNominalCase |
| | testEndReturnTransactionWithUnauthorizedUser |
| | testEndReturnTransactionWithInvalidTransactionID |
| | testEndReturnTransactionWithNotOpenTransaction |
| | testEndReturnTransactionNominalCase |
| | testDeleteReturnTransactionWithUnauthorizedUser |
| | testDeleteReturnTransactionWithInvalidTransactionID |
| | testDeleteReturnTransactionWithNotExistingClosedReturnTransaction |
| | testDeleteReturnTransactionNominalCase |
| | testReceiveCashPaymentUnauthorizedUser |
| | testReceiveCashPaymentInvalidCashAmount |
| | testReceiveCashPaymentInvalidTransactionId |
| | testReceiveCashPaymentWithNotOpenSaleTransaction |
| | testReceiveCashPaymentNotEnoughCash |
| | testReceiveCashPaymentNominalCase |
| | testReceiveCreditCardPaymentWithUnauthorizedUser |
| | testReceiveCreditCardPaymentInvalidCard |
| | testReceiveCreditCardPaymentInvalidSaleId |
| | testReceiveCreditCardPaymentWithNotRegisteredCreditCard |
| | testReceiveCreditCardPaymentWithNotOpenSaleTransaction |
| | testReceiveCreditCardPaymentWithNotEnoughMoneyInCard |
| | testReceiveCreditCardPaymentNominalCase |
| | testReturnCashPaymentWithUnauthorizedUser |
| | testReturnCashPaymentInvalidReturnId |
| | testReturnCashPaymentWithNotOpenReturnTransaction |
| | testReturnCashPaymentNominalCase |
| | testReturnCreditCardPaymentWithUnauthorizedUser |
| | testReturnCreditCardPaymentInvalidReturnId |
| | testReturnCreditCardPaymentWithInvalidCreditCard |
| | testReturnCreditCardPaymentWithNotOpenReturnTransaction |
| | testReturnCreditCardPaymentWithNotRegisteredCreditCard |
| | testReturnCreditCardPaymentNominalCase |
| | testRecordBalanceUpdateUnauthorizedUser |
| | testRecordBalanceUpdateInvalidAmountToBeAdded |
| | testRecordBalanceUpdateNominalCase |
| | testGetCreditsAndDebitsUnauthorizedUser |
| | testGetCreditsAndDebitsWithNullDates |
| | testGetCreditsAndDebitsReversedDate |
| | testGetCreditsAndDebitsNominalCase |
| | testComputeBalanceUnauthorizedUser |
| | testComputeBalanceNominalCase |


# Scenarios

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

| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
| 1-1 | FR3 | testCreateProductTypeNominalCase  <br /> testUpdatePositionNominalCase |
| 1-2 | FR3 | testUpdatePositionNominalCase |
| 1-3 | FR3 | testUpdateProductNominalCase | 
| 2-1 | FR1 | testCreateUserNominalCase |
| 2-2 | FR1 | testDeleteUserNominalCase | 
| 2-3 | FR1 | testUpdateUserRightsNominalCase | 
| 3-1 | FR4 | testIssueOrderNominalCase|             
| 3-2 | FR4 | testPayOrderNominalCase|             
| 3-3 | FR4 | testRecordOrderArrivalNominalCase | 
| 3-4 | FR4 | testPayOrderBalanceNotEnough |  
| 4-1 | FR5 | testDefineCustomerNominalCase|             
| 4-2 | FR5 | testAttachCardToCustomerNominalCase <br />testModifyCustomerNominalCase|             
| 4-3 | FR5 | testModifyCustomerModifyCard |             
| 4-4 | FR5 | testModifyCustomerNominalCase|  
| 5-1 | FR1 | testLoginNominalCase |
| 5-2 | FR1 | testLogoutNominalCase |
| 6-1 | FR6,7 | testReceiveCreditCardPaymentNominalCase |
| 6-2 | FR6 | testApplyDiscountRateToProductNominalCase | 
| 6-3 | FR6 | testApplyDiscountRateToSaleNominalCase |
| 6-4 | FR6 | testComputePointsForSaleNominalCase <br /> testModifyPointsOnCardNominalCase |
| 6-5 | FR6 | testDeleteSaleTransactionNominalCase | Sale of product type X cancelled
| 6-6 | FR6,7 | testReceiveCashPaymentNominalCase |
| 7-1 | FR6,7 | testReceiveCreditCardPaymentNominalCase |
| 7-2 | FR6,7 | testReceiveCreditCardPaymentInvalidCard |
| 7-3 | FR6,7 | testReceiveCreditCardPaymentWithNotEnoughMoneyInCard |
| 7-4 | FR6,7 | testReceiveCashPaymentNominalCase |
| 8-1 | FR6,7 | testReturnCreditCardPaymentNominalCase |
| 8-2 | FR6,7 | testReturnCashPaymentNominalCase |
| 9-1 | FR8 | testGetCreditsAndDebitsNominalCase |
| 10-1 | FR6,7 | testReturnCreditCardPaymentNominalCase |
| 10-2 | FR6,7 | testReturnCashPaymentNominalCase |

# Coverage of Non Functional Requirements

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
| NRF4 | testCreateProductTypeNominalCase |
| NFR5 | testReturnCreditCardPaymentNominalCase, testReceiveCreditCardPaymentNominalCase |
| NRF6 | testCreateCardNominalCase |

###


