# Design Document 


Authors: Giuseppe Salvi, Milad Beigi Harchegani, Roberto Bosio, Naeem Ur Rehman

Date: 17/05/2021

Version: 1.1.2


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

ARCHITECTURAL PATTERNS:
We used a 3 layer architecture: we have the GUI, the application logic and the data layer.
We also used MVC pattern, which model part is implemented in data and model packages, while the view and the controller are implemented externally by the GUI and the API.

DESIGN PATTERNS:
EZShop is a Facade, and implements the interface EZShopInterface.
For Drivers and Payment API we would use an intermediate class, which is an adapter for them. In our simplified version we don't need to manage hardware, so we didn't implement them.

DATA PERSISTENCE:
When the application starts, it reads all the files to load the data in the maps.
Each time a map is updated (ex: add a user, delete a user, ...), the application writes back the data to the file.
FileRead and FileWrite classes manage the reading and the writing from and to the files and expose the methods to do that.

```plantuml
@startuml
package data{}
package model{}
package exceptions{}
model -- data
data -- exceptions
model -- exceptions
@enduml
```

# Low level design

```plantuml
@startuml
scale 0.9
package model {

    Class UserImpl{
        -{static} idGen: Integer
        -username : String
        -password : String
        -role : String
        -ID : Integer
    }


    class ProductTypeImpl{
        -{static} idGen: Integer
        -productCode : String
        -description : String
        -sellPrice : Double
        -notes : String
        -ID : Integer
        -quantity : Integer
        -position : Position
        -eliminated : boolean
    }

    class OrderImpl{
        -{static} idGen: Integer
        -orderID : Integer
        -pricePerUnit : Double
        -quantity : Integer
        -product : ProductType
        -status : String
        -balanceId : Integer
    }

    class CustomerImpl{
        -{static} idGen: Integer
        -customerName : String
        -customerID : Integer
        -card : LoyaltyCard
    }

    class LoyaltyCard{
        -{static} idGen: Integer
        -cardID : String
        -points : Integer
        -customer : Customer
    }

    class SaleTransactionImpl {
        -{static} idGen: Integer
        -transactionID : Integer
        -products : List<TransactionProduct>
        -globalDiscountRate : Double
        -state : String
        -date : LocalDate
        -time : String
        -cost : Double
        -paymentType : String
        -creditCard : CreditCard
        +computeCost() : Double
    }

    class TicketEntryImpl{
        -productCode : ProductType
        -quantity : Integer
        -discountRate : Double
    }

    class ReturnTransaction{
        -{static} idGen: Integer
        -returnID : Integer
        -transaction : SaleTransaction
        -products : List<TransactionProduct>
        -state : String
        -commit : boolean
        -value : Double
        +computeValue() : Double
    }

    class BalanceOperationImpl{
        -{static} idGen: Integer
        -balanceId : Integer
        -type : String
        -amount : Double
        -date : LocalDate
    }

    class CreditCard{
        -number : String
        -balance : Double
        +{static} checkValidity() : boolean
    }

    class Position{
        -aisleID : String
        -rackID : String
        -levelID : String
    }


}

package data {

    Class EZShop{
        -users : Map<Integer,UserImpl>
        -products : Map<Integer,ProductTypeImpl>
        -orders : Map<Integer,OrderImpl>
        -customers : Map<Integer,CustomerImpl>
        -cards : Map<String,LoyaltyCard>
        -sales : Map<Integer,SaleTransactionImpl>
        -returns : Map<Integer,ReturnTransaction>
        -operations : Map<Integer,BalanceOperationImpl>
        -loggedInUser : User
        +reset() : void
        +createUser(String username, String password, String role) : Integer
        +deleteUser(Integer id) : boolean
        +getAllUser() : List<User>
        +getUser(Integer id) : User
        +updateUserRights(Integer id, String role) : boolean
        +login(String username, String password) : User
        +logout() : boolean
        +createProductType(String description, String productCode, double pricePerUnit, String note) : Integer
        +updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) : boolean
        +deleteProductType(Integer id) : boolean
        +getAllProductTypes() : List<ProductType>
        +getProductTypeByBarCode(String barcode) : ProductType
        +getProductTypesByDescription(String description) : List<ProductType> 
        +updateQuantity(Integer productId, int toBeAdded) : boolean
        +updatePosition(Integer productId, String newPos) : boolean
        +issueOrder(String productCode, int quantity, double pricePerUnit) : Integer
        +payOrderFor(String productCode, int quantity, double pricePerUnit) : Integer
        +payOrder(Integer orderId) : boolean
        +recordOrderArrival(Integer orderId) : boolean
        +getAllOrders() : List<Order>
        +defineCustomer(String customerName) : Integer
        +modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) : boolean
        +deleteCustomer(Integer id) : boolean
        +getCustomer(Integer id) : Customer
        +getAllCustomers() : List<Customer>
        +createCard() : String
        +attachCardToCustomer(String customerCard, Integer customerId) : boolean
        +modifyPointsOnCard(String customerCard, int pointsToBeAdded) : boolean
        +startSaleTransaction() : Integer
        +addProductToSale(Integer transactionId, String productCode, int amount) : boolean
        +deleteProductFromSale(Integer transactionId, String productCode, int amount) : boolean
        +applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) : boolean
        +applyDiscountRateToSale(Integer transactionId, double discountRate) : boolean
        +computePointsForSale(Integer transactionId) : int
        +endSaleTransaction(Integer transactionId) : boolean
        +deleteSaleTransaction(Integer transactionId) : boolean
        +getSaleTransaction(Integer transactionId) : SaleTransaction
        +startReturnTransaction(Integer transactionId) : Integer
        +returnProduct(Integer returnId, String productCode, int amount) : boolean
        +endReturnTransaction(Integer returnId, boolean commit) : boolean
        +deleteReturnTransaction(Integer returnId) : boolean
        +receiveCashPayment(Integer transactionId, double cash) : double
        +receiveCreditCardPayment(Integer transactionId, String creditCard) : boolean
        +returnCashPayment(Integer returnId) : double
        +returnCreditCardPayment(Integer returnId, String creditCard) : double
        +recordBalanceUpdate(double toBeAdded) : boolean
        +getCreditsAndDebits(LocalDate from, LocalDate to) : List<BalanceOperation>
        +computeBalance() : double
    }

    Class FileRead{
        +readUsers(String fileName) : Map<Integer,UserImpl>
        +readProducts(String fileName) : Map<Integer,ProductTypeImpl>
        +readOrders(String fileName) : Map<Integer,OrderImpl>
        +readCustomers(String fileName) : Map<Integer,CustomerImpl>
        +readCards(String fileName) : Map<String,LoyaltyCard>
        +readSales(String fileName) : Map<Integer,SaleTransactionImpl>
        +readReturns(String fileName) : Map<Integer,ReturnTransaction>
        +readOperations(String fileName) : Map<Integer,BalanceOperationImpl>
        +readCreditCards(String fileName) : List<CreditCard>
        
    }

    Class FileWrite{
        +writeUsers(String fileName, Map<Integer,UserImpl>) : boolean
        +writeProducts(String fileName, Map<Integer,ProductTypeImpl>) : boolean
        +writeOrders(String fileName, Map<Integer,OrderImpl>) : boolean
        +writeCustomers(String fileName, Map<Integer,CustomerImpl>) : boolean
        +writeCards(String fileName, Map<String,LoyaltyCard>) : boolean
        +writeSales(String fileName, Map<Integer,SaleTransactionImpl>) : boolean
        +writeReturns(String fileName, Map<Integer,ReturnTransaction>) : boolean
        +writeOperations(String fileName, Map<Integer,BalanceOperationImpl>) : boolean
        +writeCreditCards(String fileName, List<CreditCard>) : boolean
    }

    interface EZShopInterface{}
    interface User{}
    interface SaleTransaction{}
    interface Customer{}
    interface Order{}
    interface TicketEntry{}
    interface BalanceOperation{}
    interface ProductType{}
}

CustomerImpl -- LoyaltyCard
TicketEntryImpl "*" -- SaleTransactionImpl
TicketEntryImpl "*" -- ReturnTransaction
ReturnTransaction -- SaleTransactionImpl
BalanceOperationImpl -- OrderImpl
BalanceOperationImpl -- ReturnTransaction
BalanceOperationImpl -- SaleTransactionImpl
CreditCard -- SaleTransactionImpl
ProductTypeImpl -- OrderImpl
ProductTypeImpl -- TicketEntryImpl
ProductTypeImpl -- Position
EZShop --> FileRead 
EZShop --> FileWrite

ProductTypeImpl -up-|> ProductType
CustomerImpl -up-|> Customer
TicketEntryImpl -up-|> TicketEntry
BalanceOperationImpl -up-|> BalanceOperation
SaleTransactionImpl -up-|> SaleTransaction
UserImpl -up-|> User
OrderImpl -up-|> Order
EZShop -up-|> EZShopInterface

EZShop -up-> "*" UserImpl
EZShop -up-> "*" ProductTypeImpl
EZShop -up-> "*" OrderImpl
EZShop -up-> "*" CustomerImpl
EZShop -up-> "*" LoyaltyCard
EZShop -up-> "*" BalanceOperationImpl
EZShop -up-> "*" CreditCard
EZShop -up-> "*" SaleTransactionImpl
EZShop -up-> "*" ReturnTransaction

@enduml
```


# Verification traceability matrix

| | EZShop | UserImpl | CustomerImpl | LoyaltyCard | BalanceOperationImpl | ReturnTransaction | SaleTransactionImpl | OrderImpl | TicketEntryImpl | ProductTypeImpl | Position | CreditCard | FileWrite | FileRead |
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|FR1| X | X |   |   |   |   |   |   |   |   |   |   | X |   |
|FR3| X |   |   |   |   |   |   |   |   | X | X |   | X |   |
|FR4| X |   |   |   | X |   |   | X |   | X | X |   | X |   |
|FR5| X |   | X | X |   |   |   |   |   |   |   |   | X |   |
|FR6| X |   |   |   |   | X | X |   | X | X |   |   | X |   |
|FR7| X |   | X | X | X |   |   |   |   |   |   | X | X | X |
|FR8| X |   |   |   | X |   |   |   |   |   |   |   | X |   |



# Verification sequence diagrams 

## Scenario 1.1 - Create product type X
```plantuml
@startuml
actor user
participant EZShop
participant ProductType
participant Position
participant FileWrite
user -> EZShop : createProductType()
EZShop -> ProductType : ProductType()
ProductType -> Position : Position()
ProductType <-- Position : return Position
EZShop <-- ProductType : return ProductType
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return productID
@enduml
```

## Scenario 1.2 - Modify product type location
```plantuml
@startuml
actor user
participant EZShop
participant ProductType
participant Position
participant FileWrite
user -> EZShop : getProductTypeByBarCode()
user <-- EZShop : return ProductType
user -> EZShop : UpdatePosition()
EZShop -> ProductType : setPosition()
ProductType -> Position : setAisleID()
ProductType -> Position : setRackID()
ProductType -> Position : setLevelID()
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 1.3 - Modify product type price per unit
```plantuml
@startuml
actor user
participant EZShop
participant ProductType
participant FileWrite
user -> EZShop : getProductTypeByBarCode()
user <-- EZShop : return ProductType
user -> EZShop : updateProduct()
EZShop -> ProductType : setSellPrice()
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 2.1 - Create user and define rights
```plantuml
@startuml
actor user
participant EZShop
participant User
participant FileWrite
user -> EZShop : CreateUser()
EZShop -> User : User()
EZShop <-- User : return User
EZShop -> FileWrite : writeUsers()
EZShop <-- FileWrite : return true
user <-- EZShop : return userID
@enduml
```

## Scenario 2.2 - Delete user
```plantuml
@startuml
actor user
participant EZShop
participant FileWrite
user -> EZShop : DeleteUser()
EZShop -> FileWrite : writeUsers()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 2.3 - Modify user rights
```plantuml
@startuml
actor user
participant EZShop
participant User
participant FileWrite
user -> EZShop : updateUserRights()
EZShop -> EZShop : getUser()
EZShop -> User : setRole()
EZShop -> FileWrite : writeUsers()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 3.1 - Order of product type X issued
```plantuml
@startuml
actor user
participant EZShop
participant Order
participant FileWrite
user -> EZShop : issueOrder()
EZShop -> EZShop : getProductTypeByBarcode()
EZShop -> Order : Order()
EZShop <-- Order : return Order
EZShop -> FileWrite : writeOrders()
EZShop <-- FileWrite : return true
user <-- EZShop : return orderID
@enduml
```

## Scenario 3.2 - Order of product type X payed
```plantuml
@startuml
actor user
participant EZShop
participant Order
participant FileWrite
user -> EZShop : payOrder()
EZShop -> Order : setStatus()
EZShop -> FileWrite : writeOrders()
EZShop <-- FileWrite : return true
EZShop --> BalanceOperation : BalanceOperation()
EZShop <-- BalanceOperation : return BalanceOperation
EZShop --> FileWrite : writeOperations()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 3.3 - Record order of product type X arrival
```plantuml
@startuml
actor user
participant EZShop
participant Order
participant FileWrite
user -> EZShop : recordOrderArrival()
EZShop -> Order : setStatus()
EZShop -> FileWrite : writeOrders()
EZShop <-- FileWrite : return true
EZShop -> ProductType : setQuantity()
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 4.1 - Create customer record
```plantuml
@startuml
actor user
participant EZShop
participant Customer
participant FileWrite
user -> EZShop : defineCustomer()
EZShop -> Customer : Customer()
EZShop <-- Customer : return Customer
EZShop -> FileWrite : writeCustomers()
EZShop <-- FileWrite : return true
user <-- EZShop : return customerID
@enduml
```

## Scenario 4.2 - Attach Loyalty card to customer record
```plantuml
@startuml
actor user
participant EZShop
participant LoyaltyCard
participant Customer
participant FileWrite
user -> EZShop : createCard()
EZShop -> LoyaltyCard : LoyaltyCard()
EZShop <-- LoyaltyCard : return LoyaltyCard
user <-- EZShop : return cardID
user -> EZShop : attachCardToCustomer()
EZShop -> Customer : setCard()
EZShop -> LoyaltyCard : setCustomer()
EZShop -> FileWrite : writeCustomers()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeCards()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 4.3 - Detach Loyalty card from customer record
```plantuml
@startuml
actor user
participant EZShop
participant Customer
participant LoyaltyCard
participant FileWrite
user -> EZShop : modifyCustomer()
EZShop -> EZShop : getCustomer()
EZShop -> Customer : setCard()
EZShop -> LoyaltyCard : setCustomer()
EZShop -> FileWrite : writeCustomers()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeCards()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 4.4 - Update Customer record
```plantuml
@startuml
actor user
participant EZShop
participant Customer
participant FileWrite
user -> EZShop : modifyCustomer()
EZShop -> EZShop : getCustomer()
EZShop -> Customer : setCustomerName()
EZShop -> FileWrite : writeCustomers()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 5.1 - Login

```plantuml 
@startuml
Actor user
participant EZShop
user -> EZShop : Login()
EZShop -> EZShop : getUser()
user <-- EZShop : return User
@enduml
```

## Scenario 5.2 - Logout

```plantuml 
@startuml
Actor user
participant EZShop   
user -> EZShop : Logout()
user <- EZShop : return true
@enduml
```

## Scenario 6.1 - Sale of product type X completed (Credit Card)
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant ProductType
participant TransactionProduct
user -> EZShop : startSaleTransaction()
EZShop -> SaleTransaction : SaleTransaction()
EZShop <-- SaleTransaction : SaleTransaction
user <-- EZShop : return transactionID
user -> EZShop : addProductToSale()
EZShop -> TransactionProduct : TransactionProduct()
EZShop <-- TransactionProduct : return TransactionProduct
EZShop -> ProductType : setQuantity()
user <-- EZShop : return true
user -> EZShop : endSaleTransaction()
EZShop -> SaleTransaction : computeCost()
EZShop <-- SaleTransaction : return cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
user -> EZShop : receiveCreditCardPayment()
user <-- EZShop : return true
@enduml
```

## Scenario 6.2 - Sale of product type X with product discount
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant ProductType
participant TransactionProduct
user -> EZShop : startSaleTransaction()
EZShop -> SaleTransaction : SaleTransaction()
EZShop <-- SaleTransaction : SaleTransaction
user <-- EZShop : return transactionID
user -> EZShop : addProductToSale()
EZShop -> TransactionProduct : TransactionProduct()
EZShop <-- TransactionProduct : return TransactionProduct
EZShop -> ProductType : setQuantity()
user <-- EZShop : return true
user -> EZShop : applyDiscountRateToProduct()
EZShop -> TransactionProduct : setDiscountRate()
user <- EZShop : return true
user -> EZShop : endSaleTransaction()
EZShop -> SaleTransaction : computeCost()
EZShop <-- SaleTransaction : return cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
user -> EZShop : receiveCashPayment()
user <-- EZShop : return change
@enduml
```

## Scenario 6.3 - Sale of product type X with sale discount
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant ProductType
participant TransactionProduct
user -> EZShop : startSaleTransaction()
EZShop -> SaleTransaction : SaleTransaction()
EZShop <-- SaleTransaction : SaleTransaction
user <-- EZShop : return transactionID
user -> EZShop : addProductToSale()
EZShop -> TransactionProduct : TransactionProduct()
EZShop <-- TransactionProduct : return TransactionProduct
EZShop -> ProductType : setQuantity()
user <-- EZShop : return true
user -> EZShop : applyDiscountRateToSale()
EZShop -> SaleTransaction : setGlobalDiscountRate()
user <- EZShop : return true
user -> EZShop : endSaleTransaction()
EZShop -> SaleTransaction : computeCost()
EZShop <-- SaleTransaction : return cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
user -> EZShop : receiveCreditCardPayment()
user <-- EZShop : return true
@enduml
```

## Scenario 6.4 - Sale of product type X with Loyalty Card update
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant ProductType
participant TransactionProduct
participant Customer
user -> EZShop : startSaleTransaction()
EZShop -> SaleTransaction : SaleTransaction()
EZShop <-- SaleTransaction : SaleTransaction
user <-- EZShop : return transactionID
user -> EZShop : addProductToSale()
EZShop -> TransactionProduct : TransactionProduct()
EZShop <-- TransactionProduct : return TransactionProduct
EZShop -> ProductType : setQuantity()
user <-- EZShop : return true
user -> EZShop : endSaleTransaction()
EZShop -> SaleTransaction : computeCost()
EZShop <-- SaleTransaction : return cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
user -> EZShop : receiveCreditCardPayment()
user <-- EZShop : return true
user -> EZShop : modifyPointsOnCard()
EZShop -> LoyaltyCard: setPoints()
EZShop -> FileWrite : writeCards()
user <-- EZShop : return true
@enduml
```

## Scenario 6.5 - Sale of product type X cancelled
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant ProductType
participant TransactionProduct
user -> EZShop : startSaleTransaction()
EZShop -> SaleTransaction : SaleTransaction()
EZShop <-- SaleTransaction : SaleTransaction
user <-- EZShop : return transactionID
user -> EZShop : addProductToSale()
EZShop -> TransactionProduct : TransactionProduct()
EZShop <-- TransactionProduct : return TransactionProduct
EZShop -> ProductType : setQuantity()
user <-- EZShop : return true
user -> EZShop : endSaleTransaction()
EZShop -> SaleTransaction : computeCost()
EZShop <-- SaleTransaction : return cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
user -> EZShop : deleteSaleTransaction()
EZShop -> EZShop : getSaleTransaction()
EZShop -> ProductType : setQuantity()
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
@enduml
```

## Scenario 6.6 - Sale of product type X completed (Cash)
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant ProductType
participant TransactionProduct
user -> EZShop : startSaleTransaction()
EZShop -> SaleTransaction : SaleTransaction()
EZShop <-- SaleTransaction : SaleTransaction
user <-- EZShop : return transactionID
user -> EZShop : addProductToSale()
EZShop -> TransactionProduct : TransactionProduct()
EZShop <-- TransactionProduct : return TransactionProduct
EZShop -> ProductType : setQuantity()
user <-- EZShop : return true
user -> EZShop : endSaleTransaction()
EZShop -> SaleTransaction : computeCost()
EZShop <-- SaleTransaction : return cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
user -> EZShop : receiveCashPayment()
user <-- EZShop : return change
@enduml
```

## Scenario 7.1 - Manage payment by valid credit card
```plantuml
@startuml
actor user
participant EZShop
participant CreditCard
participant SaleTransaction
participant BalanceOperation
user -> EZShop : receiveCreditCardPayment()
EZShop -> FileRead : readCreditCards()
EZShop <-- FileRead : List<CreditCards>
EZShop -> CreditCard : checkValidity()
EZShop <-- CreditCard : true
EZShop -> CreditCard : getBalance()
EZShop <-- CreditCard : balance
EZShop -> CreditCard : setBalance()
EZShop -> SaleTransaction : setCreditCard()
EZShop -> SaleTransaction : setState()
EZShop -> SaleTransaction : setPaymentType()
EZShop -> SaleTransaction : getCost()
EZShop <-- SaleTransaction : cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> BalanceOperation : BalanceOperation()
EZShop <-- BalanceOperation : return BalanceOperation
EZShop -> FileWrite : writeOperations()
EZShop <-- FileWrite : return true
EZShop -> FileRead : writeCreditCards()
EZShop <-- FileRead : true
user <-- EZShop : return true
@enduml
```


## Scenario 7.4 - Manage cash payment
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant BalanceOperation
user -> EZShop : receiveCashPayment()
EZShop -> SaleTransaction : setState()
EZShop -> SaleTransaction : setPaymentType()
EZShop -> SaleTransaction : getCost()
EZShop <-- SaleTransaction : cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> BalanceOperation : BalanceOperation()
EZShop <-- BalanceOperation : return BalanceOperation
EZShop -> FileWrite : writeOperations()
EZShop <-- FileWrite : return true
user <-- EZShop : return change
@enduml
```

## Scenario 8.1 - Return transaction of product type X completed, credit card
```plantuml
@startuml
actor user
participant EZShop
participant SaleTransaction
participant ProductType
participant TransactionProduct
participant ReturnTransaction
user -> EZShop : startReturnTransaction()
EZShop -> ReturnTransaction : ReturnTransaction()
EZShop <-- ReturnTransaction : ReturnTransaction
user <-- EZShop : return transactionID
user -> EZShop : returnProduct()
EZShop -> SaleTransaction : getProducts()
EZShop <-- SaleTransaction : List<TransactionProduct>
EZShop -> TransactionProduct : getQuantity()
EZShop <-- TransactionProduct : return quantity
user <-- EZShop : return true
user -> EZShop : endReturnTransaction()
EZShop -> ProductType : setQuantity()
EZShop -> SaleTransaction : setProducts()
EZShop -> SaleTransaction : computeCost()
EZShop <-- SaleTransaction : return cost
EZShop -> FileWrite : writeSales()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeProducts()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeReturns()
EZShop <-- FileWrite : return true
user <-- EZShop : return true
user -> EZShop : returnCreditCardPayment()
user <-- EZShop : return true
@enduml
```

## Scenario 9.1 - List credits and debits
```plantuml
@startuml
actor user
participant EZShop
user -> EZShop : getCreditsAndDebits()
user <-- EZShop : return List<BalanceOperation>
@enduml
```

## Scenario 10.1 - Manage return 
```plantuml
@startuml
actor user
participant EZShop
participant CreditCard
participant ReturnTransaction
participant BalanceOperation
user -> EZShop : returnCreditCardPayment()
EZShop -> FileRead : readCreditCards()
EZShop <-- FileRead : List<CreditCards>
EZShop -> CreditCard : checkValidity()
EZShop <-- CreditCard : true
EZShop -> ReturnTransaction : computeValue()
EZShop <-- ReturnTransaction : return amount
EZShop -> CreditCard : getBalance()
EZShop <-- CreditCard : Double
EZShop -> CreditCard : setBalance()
EZShop -> ReturnTransaction : setState()
EZShop --> BalanceOperation : BalanceOperation()
EZShop <-- BalanceOperation : return BalanceOperation
EZShop --> FileWrite : writeOperations()
EZShop <-- FileWrite : return true
EZShop -> FileWrite : writeCreditCards()
EZShop <-- FileWrite : true
EZShop -> FileWrite : writeReturns()
EZShop <-- FileWrite : true
user <-- EZShop : return true
@enduml
```


```

