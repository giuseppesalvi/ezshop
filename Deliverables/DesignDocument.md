# Design Document 


Authors: 

Date:

Version:


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

<discuss architectural styles used, if any>
<report package diagram>






# Low level design

<for each package, report class diagram>

```plantuml
@startuml
top to bottom direction

package Data {
    Class EZShopInterface{}
    Class JSONread{
        +readUsers(String fileName) : List<User>
        +readProducts(String fileName) : List<ProductType>
        +readOrders(String fileName) : List<Order>
        +readCustomers(String fileName) : List<Customer>
        +readCards(String fileName) : List<Cards>
        +readSales(String fileName) : List<SaleTransaction>
        +readReturns(String fileName) : List<ReturnTransaction>
        +readOperations(String fileName) : List<Operations>
    }
    Class JSONwrite{
        +writeUsers(String fileName, List<User>) : boolean
        +writeProducts(String fileName, List<ProductType>) : boolean
        +writeOrders(String fileName, List<Order>) : boolean
        +writeCustomers(String fileName, List<Customer>) : boolean
        +writeCards(String fileName, List<Cards>) : boolean
        +writeSales(String fileName, List<SaleTransaction>) : boolean
        +writeReturns(String fileName, List<ReturnTransaction>) : boolean
        +writeOperations(String fileName, List<Operations>) : boolean
    }
}
package Model {
Class EZShop{
    +users : List<User>
    +products : List<ProductType>
    +orders : List<Order>
    +customers : List<Customer>
    +cards : List<Card>
    +sales : List<SaleTransaction>
    +returns : List<ReturnTransaction>
    +operations : List<BalanceOperation>
    +loggedInUser : User
    
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

Class User {
    -username : String
    -password : String
    -role : String
    -ID : Integer
    +login() : User
    +logout() : boolean
}


class ProductType{
    -productCode : String
    -description : String
    -sellPrice : Double
    -notes : String
    -ID : Integer
    -quantity : Integer
    -position : Position

    discountRate
}

class Position{
    -aisleID : String
    -rackID : String
    -levelID : String
    +Position(String aisleID, String rackID, String levelID)
    +Position(String position)
    +getPosition() : String
}
class Order {
    -orderID : Integer
    -pricePerUnit : Double
    -quantity : Integer
    -productID : String
    -status : String
}

class Customer{
    -customerName : String
    -customerID : Integer
    -card : LoyaltyCard
}

class LoyaltyCard{
    -cardID : String
    -points : Integer
    -customer : Customer
}

class SaleTransaction {
    -transactionID : Integer
    -products : List<TransactionProduct>
    -globalDiscountRate : Double
    -state : String
    -date : LocalDate
    -time : String
    -cost : Double
    -paymentType : String
    -creditCard : String

    +computeCost() : Double
}

class TransactionProduct{
    -productCode : ProductType
    -quantity : Integer
    -discountRate : Double
}

class ReturnTransaction{
    -returnID : Integer
    -transaction : SaleTransaction
    -products : List<TransactionProduct>
    -state : String
    -commit : boolean
    -value : Double

    +computeValue() : Double
}

class BalanceOperation{
    -type : String
    -amount : Double
    -date : LocalDate
}
EZShop -- User
EZShop -- ProductType
EZShop -- Order
EZShop -- Customer
EZShop -- LoyaltyCard
EZShop -- BalanceOperation

ProductType -- Order
ProductType -- TransactionProduct
ProductType -right- Position
Customer -- LoyaltyCard
TransactionProduct -- SaleTransaction
TransactionProduct -- ReturnTransaction
ReturnTransaction -- SaleTransaction
BalanceOperation -- Order
BalanceOperation -- ReturnTransaction
BalanceOperation -- SaleTransaction
EZShopInterface -- EZShop
JSONread -- EZShop
JSONwrite -- EZShop
}
@enduml
```
```plantuml
@startuml

package Exceptions {
Class InvalidUsernameException
Class InvalidPasswordException
Class InvalidRoleException
Class InvalidUserIdException
Class UnauthorizedException

CLass InvalidProductDescriptionException
Class InvalidProductCodeException
Class InvalidPricePerUnitException
Class InvalidProductIdException
Class InvalidLocationException

Class InvalidQuantityException
Class InvalidOrderIdException

Class InvalidCustomerNameException
Class InvalidCustomerCardException
Class InvalidCustomerIdException

CLass InvalidTransactionIdException
Class InvalidProductCodeException
Class InvalidDiscountRateException
}
@enduml
```



# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

