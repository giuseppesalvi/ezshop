# Requirements Document 

Authors: Giuseppe Salvi, Milad Beigi Harchegani, Roberto Bosio, Naeem Ur Rehman 

Date:

Version:

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting


# Stakeholders

| Stakeholder name  | Description | 
| ----------------- |:-----------:|
| Manager     |  Is the manager / owner of the shop | 
| Cashier | Interacts with customers |
| Customer | Buys products, can have a fidelity card |
| Supplier | Provides products to the shop |
| PoS | The physical Point of Sale terminal |
| Barcode reader |  Optical scanner that can read printed barcodes |
| Gmail gateway | Mail service to send mails to customers|

# Context Diagram and interfaces

## Context Diagram

```plantuml
actor Manager as m
actor Cashier as c
actor Pos as p
actor "Barcode reader" as b
actor "Gmail gateway" as g
(EZShop) <-up- m
(EZShop) <-left- c
(EZShop) <-right- p
(EZShop) <-down- b
(EZShop) <-down- g
```

## Interfaces

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   The Manager     | GUI | Desktop |
|   The Cashier     | GUI | Desktop |
|  	The PoS		 | myPOS API (https://developers.mypos.eu/en)	| Ethernet cable|
|	  The Barcode reader | Barcode reader driver | USB connector type A | 
|   The Gmail gateway | Gmail API (https://developers.google.com/gmail/api) | Ethernet cable|

# Stories and personas

George, 55, is the manager of a small clothing shop. He has described his needs here in a couple of sentences.

I want the software to manage the sales. Adding items to the sale with the barcode reader or manually, this part should be fast and without delay. Then customer should be able to pay with cash or credit card. I need an accounting section to see all the sales and expenses during a specific period. I need to check my income each month, and it should be accurate.

I want to add items to the system, categorize them, and check the number of items currently in the shop. The shop should be connected to the inventory and update the number of items when necessary. I want the ability to generate reports on the items in the inventory. I need to add the suppliers and see the list of suppliers that can provide a spcific item.

I want to save customer’s contact info for managing customers and notify them about future discounts later. If the customer wants to have a fidelity card, the software should register a card for them. The software also should notify the registered customers about discounts through email.

# Functional and non functional requirements

## Functional Requirements


| ID        | Description  |
| ------------- |:-------------| 
|   FR1     | Authorize and authenticate |
|   FR1.1   | Log in/Log out |
|   FR1.2   | Change password |
|	FR2    	| Manage inventory | 
|	FR2.1	  | Add a item (manually or barcode)  |
|	FR2.2	  | Delete an item |
|	FR2.3	  | Decrease/Increase amount of items |
|	FR2.4	  | Modify items |
|	FR2.5	  | Add/Delete categories |
|	FR2.6   | Search items |
| 	FR2.7   | List suppliers that can provide an item |
|	FR3	    | Manage suppliers | 
|  	FR3.1   | Add suppliers |
|  	FR3.2   | Delete suppliers |
|  	FR3.3   | Modify suppliers |
|  	FR3.4   | List  suppliers |
|	FR4	    | Manage customers | 
|	FR4.1   | Add customers |
| 	FR4.2   | Modify customers |
|	FR4.3   | Delete customers |
|	FR4.4   | List  customers |
|	FR4.5   | Notify customers with newsletter |
|   FR5     | Manage sales transaction | 
|   FR5.1   | Create new sale transaction |
|   FR5.2   | Add / delete product |
|   FR5.3   | Compute total|
|   FR5.4   | Display total amount due |
|   FR5.5   | Print receipt|
|   FR5.6   | Handle payment  |
|   FR5.6.1 | Payment by cash  |
|   FR5.6.2 | Payment by credit card  |
|   FR6     | Manage accounting |
|   FR6.1   | Add income |
|   FR6.2   | Modify income |
|   FR6.3   | Delete income |
|   FR6.4   | Add expense |
|   FR6.5   | Modify expense |
|   FR6.6   | Delete expense |
|   FR6.7   | List expenses |
|   FR6.8   | List incomes |
|   FR6.9   | Show balance sheet |
|   FR7     | Manage cash register |
|   FR7.1   | Add money to the cash register | 
|   FR7.2   | Take money from the cash register |
|   FR7.3   | Close the cash register |

## Non Functional Requirements

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | Usability | Time to learn how to use for non Engineers < 1 day | all FR |
|  NFR2     | Availability | Availability at least 99% of the time| all FR |
|  NFR3     | Reliability  | Data in the accounting should be valid and accurate | FR2, FR3, FR4, FR6 |
|  NFR4     | Performance  | Response time < 0.5s | all FR |
|  NFR5     | Privacy  | The system must not save credit card data | FR5.7.2 |

# Use case diagram and use cases

## Use case diagram
```plantuml
left to right direction

actor Manager as m
actor Cashier as c
actor POS as p
actor "Barcode reader" as b
actor "Gmail gateway" as g

rectangle EZShop{
(FR1 Authorize and authenticate) .down.> (FR1.1 Log in/Log out) :include
m --> (FR1 Authorize and authenticate)
(FR1.1 Log in/Log out) <-- c

m --> (FR2 Manage inventory)
c --> (FR2 Manage inventory)
m --> (FR3 Manage suppliers)

(FR4 Manage customers) .down.> (FR4.1 Add customer) :include
(FR4 Manage customers) .down.> (FR4.7 Notify customers with newsletter) :include
m --> (FR4 Manage customers)
(FR4.1 Add customer) <-- c
(FR4.1 Add customer) --> b
(FR4.7 Notify customers with newsletter) --> g

(FR5 Manage sales transaction) .down.> (FR5.2 Add / Delete product) :include
(FR5 Manage sales transaction) .down.> (FR5.6.2 Payment by credit card) :include
m --> (FR5 Manage sales transaction)
c --> (FR5 Manage sales transaction)
(FR5.2 Add / Delete product) --> b
(FR5.6.2 Payment by credit card) --> p

m --> (FR6 Manage accounting)

m --> (FR7 Manage cash register)
c --> (FR7 Manage cash register)
}
```
### Use case 1, UC1 - Log in
| Actors Involved        | Manager and Cashier |
| ------------- |:-------------:| 
|  Precondition     | The user is not logged in and enters correct credentials |  
|  Post condition     | - |
|  Nominal Scenario     | The cashier or manager opens the software and logs in by his/her credentials |
|  Variants     | Wrong username/password |

### Use case 2, UC2 - Log out
| Actors Involved        | Manager and Cashier |
| ------------- |:-------------:| 
|  Precondition     | The user X is logged in  |  
|  Post condition     | - |
|  Nominal Scenario     | The user X closes the software or logs out |
|  Variants     | - |

### Use case 3, UC3 - Change password
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The manager is logged in |  
|  Post condition     | The new password is not equal to previous password  |
|  Nominal Scenario     | The manager loggs in and changes the password by selecting the username |
|  Variants     | current password is not correct, new passwords don't match |

##### Scenario 3.1
| Scenario | current password in not correct, new passwords don't match |
| ------------- |:-------------:| 
|  Precondition     | The manage is logged in |
|  Post condition     | The new password is not equal to previous password  |
| Step#        | Description  |
|  1     | The manager loggs in |
|  2     | The manager select the change password option |
|  3     | The manager selects the user to change its password, and enters the current password and new passwords |
|  4     | The system checks the passwords if the current password is not correct or new passwords don't match, issues a warning |

### Use case 4, UC4 - Add items to the inventory
| Actors Involved        | Manager , Cashier , Barcode reader |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |  
|  Post condition     | Number of items in the system = number of item before adding + 1  |
|  Nominal Scenario     | The user adds an item to the inventory, specifies manually the description, the quantity, the category and all the other fields of the item |
|  Variants     | Scan the item to add automatically the info, Required fields are not entered, ID/Barcode is not unique/valid |

##### Scenario 4.1
| Scenario | Scan the item to add automatically the info |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |
|  Post condition     | Number of items in the system = number of item before adding + 1 |
| Step#        | Description  |
|  1     | The user scans the product with barcode reader|
|  2     | The user defines the Item description and quantity of product |
|  3     | Some of the fields are automatically filled |
|  4     | The user completes the remaining fields |
|  5     | Hits the add button to add the item to the inventory |

##### Scenario 4.2
| Scenario | Required fields are not entered |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |
|  Post condition     | Number of items in the system = number of item before adding + 1 |
| Step#        | Description  |
|  1     | The user scans the product with barcode reader|
|  2     | The user defines the Item description and quantity of product |
|  3     | The user hits save button, issue an error and list the required fields that are not entered |

##### Scenario 4.3
| Scenario | Barcode is not valid |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |
|  Post condition     | Number of items in the system = number of item before adding + 1 |
| Step#        | Description  |
|  1     | The user scans the product with barcode reader|
|  2     | The system cannot read the barcode's data, issues a warning about invalid barcode |

##### Scenario 4.4
| Scenario | ID is not unique |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |
|  Post condition     | Number of items in the system = number of item before adding + 1 |
| Step#        | Description  |
|  1     | The user scans the product with barcode reader or adds the ID manually |
|  2     | The user completes the form and adds the details |
|  3     | The user hits save button, system finds the ID duplicate, so issues a warning with a proper message |

### Use case 5, UC5 - List items
| Actors Involved        | Manager or Cashier |
| ------------- |:-------------:| 
|  Precondition     | |  
|  Post condition     | The user sees the list of items in the inventory  |
|  Nominal Scenario     | The user retrieves the list of items in the inventory |
|  Variants     | Filter the list, Modify info about item, Delete item |

##### Scenario 5.1
| Scenario | Modify info about item |
| ------------- |:-------------:| 
|  Precondition     | |
|  Post condition     | The user modified the info about an item |
| Step#        | Description  |
|  1     | The user retrieves the list of items in the inventory |
|  2     | Finds the item that he wants to modify |
|  3     | Modifies the fields |
|  4     | Confirms the modifications |

##### Scenario 5.2
| Scenario | Delete item |
| ------------- |:-------------:| 
|  Precondition     | |
|  Post condition     | The user deleted an item from the inventory |
| Step#        | Description  |
|  1     | The user retrieves the list of items in the inventory |
|  2     | Finds the item that he wants to delete |
|  3     | Deletes it |
|  4     | Confirms the action |

### Use case 6, UC6 - Add category for items
| Actors Involved        | Manager or Cashier |
| ------------- |:-------------:| 
|  Precondition     | The category name should be unique  |  
|  Post condition     | new number of categories = previous number + 1   |
|  Nominal Scenario     | The user opens the add category option and defines a name |
|  Variants     | Category name is not unique (exist) |

### Use case 7, UC7 - Search items
| Actors Involved        | Manager or Cashier |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | - |
|  Nominal Scenario     | The user scans the product with barcode reader or enters the description, retrieves the resulting product(or products) |
|  Variants     | The result is empty |

### Use case 8, UC8 - Add supplier
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is not present in the system |  
|  Post condition     | Supplier X added in the system, and number of suppliers is increased  |
|  Nominal Scenario     | The manager adds the information of a new supplier in the system  |
|  Variants     | Required fields are not entered |

### Use case 9, UC9 - List suppliers
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | - |
|  Nominal Scenario     | The user retrives a list of suppliers |
|  Variants     | The list is empty, Modify supplier, Delete supplier |

##### Scenario 9.1
| Scenario | Modify supplier |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is already present in the system|
|  Post condition     | - |
| Step#        | Description  |
|  1     | The user retrieves the list of suppliers |
|  2     | Finds the supplier that he wants to modify |
|  3     | Modifies the fields |
|  4     | Confirms the modifications |


##### Scenario 9.2
| Scenario | Delete supplier |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is already present in the system|
|  Post condition     | Supplier X is not in the system anymore, the number of suppliers is decreased |
| Step#        | Description  |
|  1     | The user retrieves the list of suppliers |
|  2     | Finds the supplier that he wants to delete |
|  3     | Deletes it |
|  4     | Confirms the action |

### Use case 10, UC10 - List supplier that can provide an item
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Item X exists |  
|  Post condition     | - |
|  Nominal Scenario     | The user writes the description of the item X or scans its barcode, retrives a list of suppliers that can provide the item X |
|  Variants     | - |

### Use case 11, UC11 - Add customer
| Actors Involved        |  Manager , Cashier , Barcode reader |
| ------------- |:-------------:| 
|  Precondition     | Customer X does not have a fidelity ID (card) |  
|  Post condition     | Customer should be in the system, number of customers will increase by one |
|  Nominal Scenario     | Customer X agrees to give us some personal information (name, surname, address, email, date of birth), the cashier inserts those information in the system and attach to them a card ID, scanning with the barcode reader a new fidelity card |
|  Variants     | required fileds are not entered, barcode is not valid |

##### Scenario 11.1
| Scenario | Add customer required fields are not provided |
| ------------- |:-------------:| 
|  Precondition     | Customer X does not have a fidelity ID (card) |
|  Post condition     | Customer should be in the system, number of customers will increase by one |
| Step#        | Description  |
|  1     | The user opens the add customer section |  
|  2     | The user enters the information about the customer |
|  3     | required fields are not entered, system issues a warning |


### Use case 12, UC12 - List customers
| Actors Involved        | Manager or Cashier |
| ------------- |:-------------:| 
|  Precondition     | There is at least one customer registered in the system |  
|  Post condition     | Number of customers shown are equal to number of customers in the database |
|  Nominal Scenario     | The manager retrives a list of customers registered in the system |
|  Variants     | Modify customer, Delete customer,Filter the list using name of customers |

##### Scenario 12.1
| Scenario | Modify customer |
| ------------- |:-------------:| 
|  Precondition     | Customer X is already present in the system|
|  Post condition     | - |
| Step#        | Description  |
|  1     | The user retrieves the list of customers registered in the system |
|  2     | Finds the customer that he wants to modify |
|  3     | Modifies the fields |
|  4     | Confirms the modifications |

##### Scenario 12.2
| Scenario | Delete customer |
| ------------- |:-------------:| 
|  Precondition     | Customer X is already present in the system|
|  Post condition     | Customer X is deleted from the system, number of customers will decrease by one|
| Step#        | Description  |
|  1     | The user retrieves the list of customers registered in the system |
|  2     | Finds the customer that he wants to delete |
|  3     | Deletes it |
|  4     | Confirms the action |

##### Scenario 12.3
| Scenario | Filter the list using the name of customer |
| ------------- |:-------------:| 
|  Precondition     | Customer X is already present in the system|
|  Post condition     | - |
| Step#        | Description  |
|  1     | The user retrieves the list of customers registered in the system |
|  2     | Writes the customer name that he wants to search |
|  3     | The list will show only the customer(or customers) with that name |

### Use case 13, UC13 - Notify customers with newsletter
| Actors Involved        |  Manager , Cashier , Gmail gateway |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |  
|  Post condition     | Number of emails sent equals to number of filtered customers |
|  Nominal Scenario     | The manager sends an email to notify customers about new discounts or other news related to the shop |
|  Variants     | Gmail server is offline |

##### Scenario 13.1
| Scenario | Gmail servers are online |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |
|  Post condition     | Emails sent |
| Step#        | Description  |
|  1     | Manager retrives the list of customers |  
|  2     | Manager applies (or not) filters to the list of customers |
| 3 | Manager sends email to the customers in the list |
| 4 | System display a success message |

##### Scenario 13.2
| Scenario | Gmail servers are offline |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |
|  Post condition     | Emails sent |
| Step#        | Description  |
|  1     | Manager retrives the list of customers |  
|  2     | Manager applies (or not) filters to the list of customers |
| 3 | Manager sends email to the customers in the list |
| 4 | System display an error message |

### Use case 14, UC14 - Manage Cash register
| Actors Involved        | Manager, Cashier |
| ------------- |:-------------:| 
|  Precondition     | Cash register is available |  
|  Post condition     |  |
|  Nominal Scenario     | The user manages the cash register |
|  Variants     | Add Money, Drop Money, Close register |

##### Scenario 14.1 
| Scenario | Add Money |
| ------------- |:-------------:|
|  Precondition     | - |  
|  Post condition     | Money added to cash register |
| Step# | Description | 
| 1 | Specify the amount by entering the quantity of each banknote | 
| 2 | Open the drawer |
| 3 | Save operation |

##### Scenario 14.2 
| Scenario | Drop Money |
| ------------- |:-------------:|
|  Precondition     | Some cash in cash register |  
|  Post condition     | Money dropped from cash register |
| Step# | Description | 
| 1 | Specify the amount by entering the quantity of each banknote | 
| 2 | Open the drawer |
| 3 | Save operation |

##### Scenario 14.3
| Scenario | Close the cash register |
| ------------- |:-------------:|
|  Precondition     | - |  
|  Post condition     | - |
| Step# | Description | 
| 1 | Insert amount of cash present in the cash register at the end of the day specifing quantity of each banknote | 
| 2 | Produce legal document |

### Use case 15, UC15 - Sale transaction
| Actors Involved        | Manager , Cashier , POS |
| ------------- |:-------------:| 
|  Precondition     | |  
|  Post condition     | Sale transaction completed |
|  Nominal Scenario     |  |
|  Variants     | Sale product(s), payment by cash or by credit card, no credit abort sale,  barcode unreadable |

##### Scenario 15.1
| Scenario  | Sale 1 product and payment by cash |
| ------------- |:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| Step | Description | 
| 1 | Start a new sales transaction  | 
| 2 | Read bar code X | 
| 3 | Retrieve name and price given barcode X | 
| 4 | Compute total T | 
| 5 | Manage payment amount T by cash | 
| 6 | Deduce stock amount of product |
| 7 | Print receipt |
| 8 | Close transaction |

##### Scenario 15.2 
| Scenario | Sale 1 product and payment by credit card|
| ------------- |:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| Step | Description | 
| 1 | Start a new sales transaction  |
| 2 | Read bar code X | 
| 3 | Retrieve name and price given barcode X |
| 4 | Compute total T |
| 5 | Manage payment amount T by credit card | 
| 6 | Deduce stock amount of product | 
| 7 | Print receipt | 
| 8 | Close transaction |

##### Scenario 15.3  
| Scenario | Sale N products and payment by cash |
| ------------- |:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| Step | Description | 
| 1 | Start a new sales transaction  | 
| 2 | Read bar code X | 
| 3 | Retrieve name and price given barcode X |
| 4 | Read bar code X for n products| 
| 5 | Retrieve names and prices given barcode X | 
| 6 | Compute total T |
| 7 | Manage payment amount T by cash | 
| 8 | Deduce stock amount of product |
| 9 | Print receipt |
| 10 | Close transaction |

##### Scenario 15.4
| Scenario | Sale N products and payment by credit card |
| ------------- |:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| Step | Description |
| 1 | Start a new sales transaction  |
| 2 | Read bar code X | 
| 3 | Retrieve name and price given barcode X |
| 4 | Read bar code X for n products|
| 5 | Retrieve names and prices of n products given barcode X |
| 6 | Compute total T |
| 7 | Manage payment amount T by credit card |
| 8 | Deduce stock amount of product |
| 9 | Print receipt |
| 10 | Close transaction |

##### Scenario 15.5
| Scenario | Barcode unreadable |  
| ------------- |:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| Step | Description | 
| 1 | Start a new sales transaction  |  
| 2 | Read bar code X |
| 3 | Barcode not readable | 
| 4 | Input bar code manually | 
| 5 | Retrieve name and price given barcode X |
| 6 | Compute total T | 
| 7 | Manage payment amount T by cash |
| 8 | Deduce stock amount of product | 
| 9 | Print receipt |  
| 10 | Close transaction |

##### Scenario 15.6 
| Scenario | No credit in credit card, abort sale transaction |
| ------------- |:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is aborted |
| Step | Description | 
| 1 | Start a new sales transaction  | 
| 2 | Read bar code X |
| 3 | Retrieve name and price given barcode X |
| 4 | Compute total T | 
| 5 | Manage payment amount T by credit card| 
| 6 | No credit, Abort sale transaction |

### Use case 16 - Record income, UC16
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | The new income is recorded, the previous overall income < The new overall income |
|  Nominal Scenario     | The manager records a new income in the system, specifies the description, the amount, the date, the type of income, and also other details |
|  Variants     | - |

### Use case 17 - Record expense, UC17
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | The expense in recorded |
|  Nominal Scenario     | The manager records a new expense in the system, specifies the description, the amount, the date, the type of expense and also other details|
|  Variants     | A scan of the expense is attached |

### Use case 18 - List expenses, UC18
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The expenses are recorded in the system |  
|  Post condition     | The list of expenses is shown |
|  Nominal Scenario     | The manager wants to see the list of expenses recored in the system |
|  Variants     | Filter the expenses, See the expenses recorded in a period of time, the list of expenses is empty, Modify expense, Delete expense |

### Use case 19 - List incomes, UC19
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The incomes are recorded in the system |  
|  Post condition     | The list of incomes is shown |
|  Nominal Scenario     | The manager wants to see the list of incomes recorded in the system |
|  Variants     | Filter the incomes, See the incomes recorded in a period of time, the list of incomes is empty, Modify income, Delete income|

### Use case 20 - Show balance sheet, UC20
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The incomes and the expenses are recorded in the system |  
|  Post condition     | The balance sheet is shown |
|  Nominal Scenario     | The manager wants to see the balance sheet of the shop |
|  Variants     | Filter the balance sheet, See the balance sheet for a period of time |




# Glossary

```plantuml
class EZshop{}

class Manager{
+Name
+Surname
+Password
}

class Cashier{
+Name
+Surname
+Password
}

class Shop{
+Name
+Address
}

class Inventory{}
class Product{
+Barcode
+Description
+Category
+Quantity
+Price
+Cost
}

class Customer{
+Name
+Surname
+Telephone
+Email
+Date of birth
}

class "Fidelity card"{
+ID
+Date
}

class Sale{
+Total
+Date&time

}

class Supplier{
+Name
+Address
+Telephone
+Telephone2
+Email
}

class Order{
+Date
+Amount
}

class "Cash register"{}

class Pos{}

class "Barcode reader"{}


EZshop -down- "*" Manager : uses
EZshop -down- "*" Cashier : uses
Manager "*" -- "*" Shop : manages
Cashier "*" -- "*" Shop : works in
Shop -- Inventory : has
Inventory -- "*" Product : contains
Sale o-- "*" Product
Cashier -- "*" Sale
Customer -up- "*" Sale
Customer -- "0...*" "Fidelity card"  : has
Shop -- "0...*" "Fidelity card"
Supplier -- "*" Product
Order o-- "*" Product
Manager -- "*" Order : orders
Order "*" -- Supplier : receives 
Shop -right- "*" "Cash register" : has
Cashier -- "Cash register" : uses
Manager -- "Cash register" : uses
Cashier -- Pos 
Customer -up- Pos
"Barcode reader" -- "*" Product : scans
"Barcode reader" -- "*" "Fidelity card" : reads
Cashier -- "Barcode reader"

```
## Dictionary

| Term | Description|
| --------- | --------------|
| Manager | Manages the shop, if the shop is small the owner of the shop can take this role. |
| Cashier | Handles payments and receipts in the shop, interacts with customers, manages the inventory. |
| Shop | The physical shop. |
| Inventory | A store inventory is a record of all the items available for use in the daily business operations. The store inventory increases with purchases and decreases with sales .|
| Product | (or Item) An article or substance that is manufactured or refined for sale. |
| Item | (see Product) |
| Customer | A person who buys products from the shop. | 
| Fidelity card | Is a plastic card that the shop gives to regular customers. |
| Sale | A sale is a transaction between the shop and the customers in which the buyer receives products in exchange for money. |
| Supplier | A supplier is a person or organization that provides something needed such as a product or service. |
| Order | It expresses the intention of the manager of the shop to buy products from the suppliers. |
| Cash register | A cash register is a machine in a shop or other business that records sales and into which money received is put. |
| Pos | POS Terminal is an electronic tool applied at retail places to conduct card payments. |
| Barcode reader | A barcode reader (or barcode scanner) is an optical scanner that can read printed barcodes, decode the data contained in the barcode and send the data to a computer. |
| Income | Income is the money that the shop takes in, from sales, from investments or from other sources. | 
| Expense | Expenses are what the shop spends the money on, like rent, employees salaries, bills, ... |
| Balance sheet | Is a statement of the assets, liabilities, and capital of a business at a particular point in time, detailing the balance of income and expenditure over the preceding period. |


# System Design

```plantuml
class EZShop{
+F1 Authorize and authenticate()
+F2 Manage inventory()
+F3 Manage supplier()
+F4 Manage customer()
+F5 Manage sales()
+F6 Manage accounting()
+F7 Manage cash register()
}
class Computer
class Printer
class Software
class "Cash register"
EZShop o-- "Cash register"
EZShop o-- Printer
EZShop o-- Computer 
Computer -- Software 
```




# Deployment Diagram 

```plantuml
artifact EZShop
node "Computer" as c
EZShop -- c
```



