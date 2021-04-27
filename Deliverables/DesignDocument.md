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

```plantuml
@startuml
package Model {
Class EZShop{
    reset()
    createUser()
    deleteUser()
    getAllUser()
    getUser()
    updateUserRights()
    login()
    logout()
    createProductType()
    updateProduct()
    deleteProductType()
    getAllProductTypes()
    getProductTypeByBarCode()
    getProductTypeByDescription()
    updateQuantity()
    updatePosition()
    issueOrder()
    payOrderFor()
    payOrder()
    recordOrderArrival()
    getAllOrders()
    defineCustomer()
    modifyCustomer()
    deleteCustomer()
    getCustomer()
    getAllCustomers()
    createCard()
    attachCardToCustomer()
    modifyPointsOnCard()
    startSaleTransaction()
    addProductToSale()
    deleteProductFromSale()
    applyDiscountRateToProduct()
    applyDiscountRateToSale()
    computePointsForSale()
    endSaleTransaction()
    deleteSaleTransaction()
    getSaleTransaction()
    startReturnTransaction()
    returnProduct()
    endReturnTransaction()
}

Class User {
    String username
    String password
    String role
    Integer ID
}

Class Login {}

class ProductType{
    String productCode
    String description
    Double sellPrice
    String notes
    Integer ID
    Integer quantity
    String position

    discountRate
}

class Order {
    Integer orderID
    Double pricePerUnit
    int quantity
    String productID
    String status

    supplier
}

class Customer{
    String customerName
    Integer customerID
}

class LoyaltyCard{
    String cardID
    Integer points
}

class SaleTransaction {
    Integer transactionID
    ArrayList products
    Double globalDiscountRate
    String state
    date
    time
    cost
    paymentType
}

class ReturnTransaction{
    Integer transactionID
    Integer returnID
    ArrayList products
}

class TransactionProduct{
    ProductType productCode
    Integer quantity
    Double discountRate
}

Login -- User
ProductType -- Order
ProductType -- TransactionProduct
TransactionProduct -- SaleTransaction
LoyaltyCard -- Customer
TransactionProduct -- ReturnTransaction
ReturnTransaction -- SaleTransaction
}

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




# Low level design

<for each package, report class diagram>









# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

