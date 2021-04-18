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

I want to add items to the system, categorize them, and check the number of items currently in the shop. The shop should be connected to the inventory and update the number of items when necessary (for example, selling something). I want the ability to generate reports on the items in the inventory. I need to add the suppliers and see the list of items that each supplier can provide.

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
|	FR2.7   | Generate reports about items in the inventory |
| 	FR2.8   | List suppliers that can provide an item |
|	FR3	    | Manage suppliers | 
|  	FR3.1   | Add suppliers |
|  	FR3.2   | Delete suppliers |
|  	FR3.3   | Modify suppliers |
|  	FR3.4   | List all suppliers |
|	FR4	    | Manage customers | 
|	FR4.1   | Add customers |
| 	FR4.2   | Modify customers |
|	FR4.3   | Delete customers |
|	FR4.4   | List all customers |
|	FR4.5   | Search customers |
|	FR4.6   | Filter list of customers |
|	FR4.7   | Notify customers with newsletter |
|   FR5     | Manage sales transaction | 
|   FR5.1   | Start sale transaction | 
|   FR5.2   | End sale transaction |
|   FR5.3   | Add / delete product |
|   FR5.4   | Compute total|
|   FR5.5   | Display total amount due|
|   FR5.6   | Print receipt|
|   FR5.7   | Handle payment  |
|   FR5.7.1 | Payment by cash  |
|   FR5.7.2 | Payment by credit card  |
|   FR6     | Manage accounting |
|   FR6.1   | Add/Record income |
|   FR6.2   | Modify income |
|   FR6.3   | Delete income |
|   FR6.2   | Add/Record expense |
|   FR6.2   | Modify expense |
|   FR6.2   | Delete expense |
|   FR6.3   | List expenses |
|   FR6.4   | List incomes |
|   FR6.5   | Show balance sheet |
|   FR7     | Manage cash register |
|   FR7.1   | Open the cash register |
|   FR7.2   | Add money to the cash register | 
|   FR7.3   | Take money from the cash register |
|   FR7.4   | Close the cash register |


## Non Functional Requirements

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | Privacy  | The system must not save credit card data | FR5.7.2 |
|  NFR2     | Usability | Time to learn how to use for non Engineers < 1 day | all FR |
|  NFR3     | Availability | Availability at least 99% of the time| all FR|
|  NFR4     | Reliability  | Data integrity | FR2, FR3, FR4, FR6 |
|  NFR5     | Performance  | Response time < 0.5s | all FR |
|  Domain1 | | Currency is Euro | |


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

m --> (FR3 Manage suppliers)

(FR4 Manage customers) .down.> (FR4.1 Add customer) :include
(FR4 Manage customers) .down.> (FR4.7 Notify customers about discounts) :include
m --> (FR4 Manage customers)
(FR4.1 Add customer) <-- c
(FR4.1 Add customer) --> b
(FR4.7 Notify customers about discounts) --> g

(FR5 Manage sales transaction) .down.> (FR5.3 Add / Delete product) :include
(FR5 Manage sales transaction) .down.> (FR5.7.2 Payment by credit card) :include
m --> (FR5 Manage sales transaction)
c --> (FR5 Manage sales transaction)
(FR5.3 Add / Delete product) --> b
(FR5.7.2 Payment by credit card) --> p

m --> (FR6 Manage accounting)

m --> (FR7 Manage cash register)
c --> (FR7 Manage cash register)
c --> (FR2 Manage inventory)
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
|  4     | The system checks the passwords if the current password is not correct and new passwords don't match, issues a warning |

### Use case 4, UC4 - Add items to the inventory
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |  
|  Post condition     | Number of items in the system = number of item before adding + 1  |
|  Nominal Scenario     | The user adds the product with barcode and defines quantity, price, and category of the item |
|  Variants     | Required fields are not entered, ID/Barcode is not unique/valid |

##### Scenario 4.1
| Scenario | Required fields are not entered |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |
|  Post condition     | Number of items in the system = number of item before adding + 1 |
| Step#        | Description  |
|  1     | The user scans the product with barcode reader|
|  2     | The user defines the Item description and quantity of product |
|  3     | The user hits save button, issue an error and list the required fields that are not entered |

##### Scenario 4.2
| Scenario | Barcode is not valid |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |
|  Post condition     | Number of items in the system = number of item before adding + 1 |
| Step#        | Description  |
|  1     | The user scans the product with barcode reader|
|  2     | The system cannot read the barcode's data, issues a warning about invalid barcode |

##### Scenario 4.3
| Scenario | ID is not unique |
| ------------- |:-------------:| 
|  Precondition     | The barcode is unique and valid |
|  Post condition     | Number of items in the system = number of item before adding + 1 |
| Step#        | Description  |
|  1     | The user scans the product with barcode reader or adds the ID manually |
|  2     | The user completes the form and adds the details |
|  3     | The user hits save button, system finds the ID duplicate, so issues a warning with a proper message |

### Use case 5, UC5 - Modify info about items
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The user should see the item on list of items  |  
|  Post condition     | ID is valid and unique  |
|  Nominal Scenario     | The user finds the item on list of items and opens the item to modify the info |
|  Variants     | Required fields are not provided, ID should be unique |

### Use case 6, UC6 - decrease quantity of items
| Actors Involved        | - |
| ------------- |:-------------:| 
|  Precondition     | number of items added to sales transaction < number of items in the inventory |  
|  Post condition     | new quantity = previous quantity - number of items sold  |
|  Nominal Scenario     | A product is added to the sales transaction and the shop will request a reduction for specific item and the inventory decreases that amount |
|  Variants     | Number of items in the system is less than actual numbers to be added to the sales transaction |


### Use case 6, UC6 - Add categories for items
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The category name should be unique  |  
|  Post condition     | new number of categories = previous number + 1   |
|  Nominal Scenario     | The user opens the add category option and defines a name |
|  Variants     | Category name is not unique (exist) |

### Use case 7, UC7 - Search items
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The user should provide the item description or scan with barcode  |  
|  Post condition     | - |
|  Nominal Scenario     | The user scans the product with barcode reader or enters the description |
|  Variants     | The result is empty |

### Use case 8, UC8 - Generate reports about inventory
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | In the list of items there is a result of search  |  
|  Post condition     | - |
|  Nominal Scenario     | The user will export the result, the export function will generate a file (PDF/XLS) |
|  Variants     | The result is empty |


### Use case 1, UC1 - Add supplier
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is not present in the system |  
|  Post condition     | Supplier X added in the system, and number of suppliers is increased  |
|  Nominal Scenario     | The manager adds the information of a new supplier in the system  |
|  Variants     | Required fields are not entered |

### Use case 2, UC2 - Remove supplier
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is already present in the system |  
|  Post condition     | Supplier X is not in the system anymore, the number of suppliers is decreased |
|  Nominal Scenario     | The manager removes Supplier X form the system |
|  Variants     | - |

### Use case 2, UC2 - Modify supplier
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is already present in the system |  
|  Post condition     | - |
|  Nominal Scenario     | The manager modifies Supplier X info |
|  Variants     | Required fields are not entered |

### Use case 4, UC4 - List of suppliers
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | - |
|  Nominal Scenario     | The manager retrives a list of suppliers |
|  Variants     | The list is empty |

### Use case 3, UC3 - List supplier that can provide an item
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Item X exists |  
|  Post condition     | - |
|  Nominal Scenario     | The manager retrives a list of suppliers that can provide the item X |
|  Variants     | - |

### Use case 4, UC4 - Add customer
| Actors Involved        |  Cashier |
| ------------- |:-------------:| 
|  Precondition     | Customer X does not have a fidelity card |  
|  Post condition     | Customer should be in the system, number of customers will increase by one |
|  Nominal Scenario     | Customer X agrees to give us some personal information (name, surname, email), the cashier insert those information in the system and attach to them a card ID, scanning with the barcode reader a new fidelity card |
|  Variants     | required fileds are not entered, barcode is not valid |

### Use case 5, UC5 - Modify info about customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Customer X already present in the system |  
|  Post condition     | - |
|  Nominal Scenario     | The manager selects customer X to modify his info; the manager modifies one or more fields|
|  Variants     | required fileds are not entered |

### Use case 6, UC6 - Delete customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Customer X already present in the system   |  
|  Post condition     | Customer X deleted from the system, number of customers will decrease by one |
|  Nominal Scenario     | The manager selects customer X to delete it |
|  Variants     | - |

### Use case 7, UC7 - List all customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | There is at least one customer registered in the system |  
|  Post condition     | Number of customers shown are equal to number of customers in the system |
|  Nominal Scenario     | The manager retrives a list of customers registered in the system |
|  Variants     | - |

### Use case 8, UC8 - Search customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | - |
|  Nominal Scenario     | The manager searches for a customer and possibly retrives a list of customers related to provided info |
|  Variants     | Customer X is not present in the system |

##### Scenario 8.1
| Scenario | Customer X exists |
| ------------- |:-------------:| 
|  Precondition     | - |
|  Post condition     | - |
| Step#        | Description  |
|  1     | Manager insert name X and surname Y in the appropriate fields |  
|  2     | The System displays info about all customers with name X and surname Y |

##### Scenario 8.2
| Scenario | Customer X does not exist |
| ------------- |:-------------:| 
|  Precondition     | - |
|  Post condition     | - |
| Step#        | Description  |
|  1     | Manager insert name X and surname Y in the appropriate fields |  
|  2     | The System displays a message to inform that no customer has name X and surname Y|

### Use case 9, UC9 - Filter list of customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |  
|  Post condition     | - |
|  Nominal Scenario     | The manager retrives the list of customer with a fidelity card; The manager applies a filter on a field |
|  Variants     | - |

### Use case 10, UC2 - Notify customers about discounts
| Actors Involved        |  Manager |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |  
|  Post condition     | Emails sent |
|  Nominal Scenario     | The manager sends an email to notify customers of a particular discount |
|  Variants     | Gmail server is offline |

##### Scenario 10.1
| Scenario | Gmail servers are online |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |
|  Post condition     | Emails sent |
| Step#        | Description  |
|  1     | Manager retrives the list of customer |  
|  2     | Manager applies (or not) filters to the list of customer|
| 3 | Manager sends email to the customers in the list |
| 4 | System display a success message |

##### Scenario 10.2
| Scenario | Gmail servers are offline |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |
|  Post condition     | Emails sent |
| Step#        | Description  |
|  1     | Manager retrives the list of customer |  
|  2     | Manager applies (or not) filters to the list of customer|
| 3 | Manager sends email to the customers in the list |
| 4 | System display an error message |


### Use case 21, UC21 - FR7 Manage Cash register
| Actors Involved        | Manager, Cashier |
| ------------- |:-------------:| 
|  Precondition     | Cash register added by Manager|  
|  Post condition     |  |
|  Nominal Scenario     |  |
|  Variants     | Open and close cash register |

##### Scenario 21.1 - Open the cash register
| Scenario SC21.1 | Corresponds to UC21 | |
| ------------- |:-------------:|:-------------:|
|  Precondition     | no money in cash register|  
|  Post condition     | cash amount = added cash amount |
| **Step** | **Description** | **Requirement ID**|
| 1 | Open the cash register  | FR7.1 |
| 2 | Add the money "amount" to Cash register | FR7.2 |

##### Scenario 21.2 - Close the cash register
| Scenario SC21.2 | Corresponds to UC21 ||
| ------------- |:-------------:|:-------------:|
|  Precondition     | some cash in cash register|  
|  Post condition     | no money in cash register |
| **Step** | **Description** | **Requirement ID**|
| 1 | Take the money out of Cash register | FR7.3 |
| 2 | Close the cash register  |  FR7.4 |

### Use case 22, UC22 - FR5 Sales transaction
| Actors Involved        | Manager , Cashier |
| ------------- |:-------------:| 
|  Precondition     | Open Cash register |  
|  Post condition     | sale transaction completed |
|  Nominal Scenario     |  |
|  Variants     | Sale product(s), payment by cash or by credit card, no credit abort sale,  barcode unreadable |

##### Scenario 22.1 - Sale 1 product and payment by cash
| Scenario SC22.1 | Corresponds to UC22 ||
| ------------- |:-------------:|:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| **Step** | **Description** | **Requirement ID**|
| 1 | Start a new sales transaction  |  FR5.1 | 
| 2 | Read bar code X | FR5.3.1 |
| 3 | Retrieve name and price given barcode X | FR2.5|
| 4 | Compute total T | FR5.4 |
| 5 | Manage payment amount T by cash |  FR5.7.1 |
| 6 | Deduce stock amount of product |  FR2.2 |
| 7 | Print receipt |  FR5.6 |
| 8 | Close transaction | FR5.2 |

##### Scenario 22.1 - Sale 1 product and payment by credit card
| Scenario SC22.1 | Corresponds to UC22 ||
| ------------- |:-------------:|:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| **Step** | **Description** | **Requirement ID**|
| 1 | Start a new sales transaction  |  FR5.1 | 
| 2 | Read bar code X | FR5.3.1 |
| 3 | Retrieve name and price given barcode X | FR2.5|
| 4 | Compute total T | FR5.4 |
| 5 | Manage payment amount T by credit card |  FR5.7.2 |
| 6 | Deduce stock amount of product |  FR2.2 |
| 7 | Print receipt |  FR5.6 |
| 8 | Close transaction | FR5.2 |

##### Scenario 22.2 - Sale N products and payment by cash
| Scenario SC22.2 | Corresponds to UC22 | |
| ------------- |:-------------:|:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| **Step** | **Description** | **Requirement ID**|
| 1 | Start a new sales transaction  |  FR5.1 |  
| 2 | Read bar code X | FR5.3.1 |
| 3 | Retrieve name and price given barcode X | FR2.5|
| 4 | Read bar code X for n products|  |
| 5 | Retrieve names and prices given barcode X | |
| 6 | Compute total T | FR5.4 |
| 7 | Manage payment amount T by cash | FR5.7.1 |
| 8 | Deduce stock amount of product |  FR2.2 |
| 9 | Print receipt |  FR5.6 |
| 10 | Close transaction | FR5.2 |

##### Scenario 22.3 - Sale N products and payment by credit card
| Scenario SC22.3 | Corresponds to UC22 | |
| ------------- |:-------------:|:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| **Step** | **Description** | **Requirement ID**|
| 1 | Start a new sales transaction  |  FR5.1 |  
| 2 | Read bar code X | FR5.3.1 |
| 3 | Retrieve name and price given barcode X | FR2.5|
| 4 | Read bar code X for n products|  |
| 5 | Retrieve names and prices of n products given barcode X | |
| 6 | Compute total T | FR5.4 |
| 7 | Manage payment amount T by credit card | FR5.7.2 |
| 8 | Deduce stock amount of product |  FR2.2 |
| 9 | Print receipt |  FR5.6 |
| 10 | Close transaction | FR5.2 |

##### Scenario 22.4 - bar code unreadable
| Scenario SC22.4 | Corresponds to UC22|  |
| ------------- |:-------------:|:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is saved and Receipt is generated |
| **Step** | **Description** | **Requirement ID**|
| 1 | Start a new sales transaction  |  FR5.1 | 
| 2 | Read bar code X | FR5.3.1 |
| 3 | Bar code not readable | 
| 4 | Input bar code manually | 
| 5 | Retrieve name and price given barcode X | FR2.5|
| 6 | Compute total T | FR5.4 |
| 7 | Manage payment amount T by cash | FR5.7.1 |
| 8 | Deduce stock amount of product |  FR2.2 |
| 9 | Print receipt |  FR5.6 |
| 10 | Close transaction | FR5.2 |

##### Scenario 22.5 - no credit in credit card, abort sale transaction
| Scenario SC22.4 | Corresponds to UC22|  |
| ------------- |:-------------:|:-------------:|
|  Precondition     | Cashier is identified and authenticated |  
|  Post condition     | Sale is aborted |
| **Step** | **Description** | **Requirement ID**|
| 1 | Start a new sales transaction  |  FR5.1 | 
| 2 | Read bar code X | FR5.3.1 |
| 3 | Retrieve name and price given barcode X | FR2.5|
| 4 | Compute total T | FR5.4 |
| 5 | Manage payment amount T by credit card|  FR5.7.2 |
| 8 | No credit, Abort sale transaction |

### Use case 1 - Record income, UC1
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | This income must not have been already recorded in the system |  
|  Post condition     | The new income is recorded |
|  Nominal Scenario     | The manager records a new income in the system, specifies the amount, the date and the type of income |
|  Variants     | - |

### Use case 2 - Record expense, UC2
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | This expense must not have been already recorded in the system |  
|  Post condition     | The expense in recorded |
|  Nominal Scenario     | The manager records a new expense in the system, specifies the amount, the date and the type of expense |
|  Variants     | A scan of the expense is attached |

### Use case 3 - List expenses, UC3
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The expenses are recorded in the system |  
|  Post condition     | The list of expenses is shown |
|  Nominal Scenario     | The manager wants to see the list of expenses recored in the system |
|  Variants     | See the expenses recorded in a period of time, the list of expenses is empty |

### Use case 4 - List incomes, UC4
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The incomes are recorded in the system |  
|  Post condition     | The list of incomes is shown |
|  Nominal Scenario     | The manager wants to list the incomes recorded in the system |
|  Variants     | See the incomes recorded in a period of time, the list of incomes is empty |

### Use case 5 - Show balance sheet, UC5
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | The incomes and the expenses are recorded in the system |  
|  Post condition     | The balance sheet is shown |
|  Nominal Scenario     | The manager wants to see the balance sheet of the shop |
|  Variants     | See the balance sheet for a period of time |



# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design
\<describe here system design>
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


\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >
```plantuml
artifact EZShop
node "Computer" as c
EZShop -- c
```



