# Requirements Document 

Authors: Giuseppe Salvi, Milad, Roberto, Naeem

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
| Manager     |  Is the owner of the shop and the administrator of the system           | 
| Cashier | Interact with customers, manages the inventory |
| Customer | Buys products, can have a fidelity card |
| Supplier | Provides products to the shop |
| PoS | The physical Point of Sale terminal |
| Barcode reader |  Optical scanner that can read printed barcodes |
| Gmail gateway | Mail service to send mails to customers|

# Context Diagram and interfaces

## Context Diagram
\<Define here Context diagram using UML use case diagram>

```plantuml
actor Manager as a
actor Cashier as o
actor User as u
actor Pos as p
actor "Barcode reader" as b
actor "Gmail gateway" as g
a -up-|> u
o -up-|> u
(EZShop) <-left- u
(EZShop) <-- p
(EZShop) <-- b
(EZShop) <-right- g
```

\<actors are a subset of stakeholders>

## Interfaces
\<describe here each interface in the context diagram>
\<GUIs will be described graphically in a separate document>
| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   The Manager     | GUI | Desktop |
|   The Cashier     | GUI | Desktop |
|  	The PoS		 | myPOS API (https://developers.mypos.eu/en)	| Ethernet cable|
|	  The Barcode reader | API ... | USB connector type A | 
|   The Gmail gateway | Gmail API (https://developers.google.com/gmail/api) | Ethernet cable|

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>

... I want to add items to the system and categorize them. I want to check the number of items that are currently in the shop. The shop should be connected to the inventory and update the number of items when it is necessary (for example selling something). I want the ability to generate reports on the items in the inventory. I want to add the suppliers as well and see the list of items that each supplier can provide.

... I want to save customer’s contact info to inform them about future discounts. If the customer wants to have a fidelity card the store should register a card for them. The software also should notify the registered customers about discounts through Email and SMS.

# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<they match to high level use cases>

| ID        | Description  |
| ------------- |:-------------| 
|   FR1     | Authorize and authenticate |
|   FR1.1   | Log in/Log out |
|   FR1.2   | Change password |
|	  FR2    	| Manage inventory | 
|	  FR2.1	  | Add items (manually or barcode)  |
|	  FR2.2	  | Decrease/increase amount of items |
|	  FR2.3	  | Modify info about items |
|	  FR2.4	  | Add categories for items |
|	  FR2.5   | Search items |
|	  FR2.6   | Generate reports about inventory |
|	  FR3	    | Manage suppliers | 
|  	FR3.1   | Add suppliers |
|  	FR3.2   | Remove suppliers |
| 	FR3.3   | List suppliers that can provide an item |
|	  FR4	    | Manage customers | 
|	  FR4.1   | Add customer (through fidelity card) |
| 	FR4.2   | Modify info about customers |
|	  FR4.3   | Delete customers |
|	  FR4.4   | List all customers |
|	  FR4.5   | Search customers |
|	  FR4.6   | Filter list of customers |
|	  FR4.7   | Notify customers about discounts |
|   FR5     | Manage sales transaction | 
|   FR5.1   | Start sale transaction | 
|   FR5.2   | End sale transaction |
|   FR5.3   | Handle product  |
|   FR5.3.1 | Add product to the cart |
|   FR5.3.2 | Delete product from cart  |
|   FR5.3.3 | Input quantity of product |
|   FR5.4   | Compute total|
|   FR5.5   | Display total amount due|
|   FR5.6   | Print receipt|
|   FR5.7   | Handle payment  |
|   FR5.7.1 | Payment by cash  |
|   FR5.7.2 | Payment by credit card  |
|   FR6     | Manage accounting |
|   FR6.1   | Record income |
|   FR6.2   | Record expense |
|   FR6.3   | List expenses |
|   FR6.4   | List incomes |
|   FR6.5   | Show balance sheet |
|   FR7     | Manage cash register |
|   FR7.1   | Open the cash register |
|   FR7.2   | Add money to the cash register | 
|   FR7.3   | Take money from the cash register |
|   FR7.4   | Close the cash register |




## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | Privacy  | Customer data must be encripted | FR3|
|  NFR2     | Usability | Time to learn how to use for non Engineers < 1 day | all FR |
|  NFR3     | Availability | Availability at least 99% of the time| all FR|
| NFRx .. | | | | 
|  Domain1 | | Currency is Euro | |


# Use case diagram and use cases


## Use case diagram
### General use case diagram
```plantuml
left to right direction
actor Manager as m
actor Cashier as c
actor POS as p
actor "Barcode reader" as b
actor "Gmail gateway" as g

rectangle EZshop {
  m --> (FR1 Authorize and authenticate)
  m --> (FR2 Manage inventory)
  m --> (FR3 Manage suppliers)
  m --> (FR4 Manage customers)
  m --> (FR5 Manage sales transaction)
  m --> (FR6 Manage accounting)
  m --> (FR7 Manage cash register)
  (FR1 Authorize and authenticate) <-- c
  (FR4 Manage customers) <--c
  (FR5 Manage sales transaction) <-- c
  (FR7 Manage cash register) <-- c
  (FR4 Manage customers) --> g
  (FR5 Manage sales transaction) --> b
  (FR4 Manage customers) --> b
  p <-- (FR5 Manage sales transaction)
}

```
### More in detail use case diagram 
### (splitted in different plantuml diagrams for graphical reasons, actors are repeated in different diagrams because of this)
```plantuml
left to right direction
(FR1 Authorize and authenticate) .down.> (FR1.1 Log in) :include
(FR1 Authorize and authenticate) .down.> (FR1.2 Log out) :include
(FR1 Authorize and authenticate) .down.> (FR1.3 Modify credentials) :include
(FR2 Manage inventory) .down.> (FR2.1 Add items) :include
(FR2 Manage inventory) .down.> (FR2.2 Decrease/increase amount of items) :include
(FR2 Manage inventory) .down.> (FR2.3 Modify info about items) :include
(FR2 Manage inventory) .down.> (FR2.4 Add categories for items) :include
(FR2 Manage inventory) .down.> (FR2.5 Search items) :include
(FR2 Manage inventory) .down.> (FR2.6 Generate reports about inventory) :include


(FR1.1 Log in) <-- :Cashier:
(FR1.2 Log out) <-- :Cashier:
:Manager: --> (FR1 Authorize and authenticate)
:Manager: --> (FR2 Manage inventory)
```

```plantuml
left to right direction
(FR3 Manage suppliers) .down.> (FR3.1 Add suppliers) :include
(FR3 Manage suppliers) .down.> (FR3.2 Remove suppliers) :include
(FR3 Manage suppliers) .down.> (FR3.3 List suppliers that can provide an item) :include
(FR4 Manage customers) .down.> (FR4.1 Add customer) :include
(FR4 Manage customers) .down.> (FR4.2 Modify info about customers) :include
(FR4 Manage customers) .down.> (FR4.3 Delete customers) :include
(FR4 Manage customers) .down.> (FR4.4 List all customers) :include
(FR4 Manage customers) .down.> (FR4.5 Search customers) :include
(FR4 Manage customers) .down.> (FR4.6 Filter list of customers) :include
(FR4 Manage customers) .down.> (FR4.7 Notify customers about discounts) :include


:Manager: --> (FR3 Manage suppliers)
:Manager: --> (FR4 Manage customers)
(FR4.1 Add customer) <-- :Cashier:
(FR4.7 Notify customers about discounts) --> :Gmail gateway:
(FR4.1 Add customer) --> :Barcode reader:
  
```


```plantuml
left to right direction
(FR5 Manage sales transaction) .down.> (FR5.1 Start sale transaction) :include
(FR5 Manage sales transaction) .down.> (FR5.2 End sale transaction) :include
(FR5 Manage sales transaction) .down.> (FR5.3 Handle product) :include
(FR5 Manage sales transaction) .down.> (FR5.3.1 Add product to the cart) :include
(FR5 Manage sales transaction) .down.> (FR5.3.2 Delete product from cart) :include
(FR5 Manage sales transaction) .down.> (FR5.3.3 Input quantity of product) :include
(FR5 Manage sales transaction) .down.> (FR5.4 Compute total) :include
(FR5 Manage sales transaction) .down.> (FR5.5 Display total amount due) :include
(FR5 Manage sales transaction) .down.> (FR5.6 Print receipt) :include
(FR5 Manage sales transaction) .down.> (FR5.7 Handle payment) :include
(FR5 Manage sales transaction) .down.> (FR5.7.1 Payment by cash) :include
(FR5 Manage sales transaction) .down.> (FR5.7.2 Payment by credit card) :include

:Manager: --> (FR5 Manage sales transaction)
:Cashier: --> (FR5 Manage sales transaction)
(FR5.3.1 Add product to the cart) --> :Barcode reader:
(FR5.7 Handle payment) --> :POS:
```


```plantuml
left to right direction
(FR6 Manage accounting) .down.> (FR6.1 Record income) :include
(FR6 Manage accounting) .down.> (FR6.2 Record expense) :include
(FR6 Manage accounting) .down.> (FR6.3 List expenses) :include
(FR6 Manage accounting) .down.> (FR6.4 List incomes) :include
(FR6 Manage accounting) .down.> (FR6.5 Show balance sheet) :include
(FR7 Manage cash register) .down.> (FR7.1 Open the cash register) :include
(FR7 Manage cash register) .down.> (FR7.2 Add money to the cash register) :include
(FR7 Manage cash register) .down.> (FR7.3 Take money from the cash register) :include
(FR7 Manage cash register) .down.> (FR7.4 Close the cash register) :include


:Manager: --> (FR6 Manage accounting)
:Manager: --> (FR7 Manage cash register)
:Cashier: --> (FR7 Manage cash register)
```

### Use case 1, UC1
| Actors Involved        |  |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the UC can start> |  
|  Post condition     | \<Boolean expression, must evaluate to true after UC is finished> |
|  Nominal Scenario     | \<Textual description of actions executed by the UC> |
|  Variants     | \<other executions, ex in case of errors> |

##### Scenario 1.1 

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>

| Scenario 1.1 | |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the scenario can start> |
|  Post condition     | \<Boolean expression, must evaluate to true after scenario is finished> |
| Step#        | Description  |
|  1     |  |  
|  2     |  |
|  ...     |  |

##### Scenario 1.2

##### Scenario 1.x

### Use case 2, UC2
..

### Use case x, UCx
..

### Use case 1, UC1 - Add supplier
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is not present in the system |  
|  Post condition     | Supplier X added in the system  |
|  Nominal Scenario     | The manager adds the information of a new supplier in the system  |
|  Variants     | - |

### Use case 2, UC2 - Remove supplier
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Supplier X is already present in the system |  
|  Post condition     | - |
|  Nominal Scenario     | The manager removes Supplier X form the system |
|  Variants     | - |

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
|  Post condition     | Customer info added in the system |
|  Nominal Scenario     | Customer X agrees to give us some personal information (name, surname, email), the cashier insert those information in the system and attach to them a card ID, scanning with the barcode reader a new fidelity card |
|  Variants     | - |

### Use case 5, UC5 - Modify info about customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Customer X already present in the system |  
|  Post condition     | - |
|  Nominal Scenario     | The manager selects customer X to modify his info; the manager modifies one or more fields|
|  Variants     | - |

### Use case 6, UC6 - Delete customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Customer X already present in the system   |  
|  Post condition     | Customer X deleted from the system |
|  Nominal Scenario     | The manager selects customer X to delete it |
|  Variants     | - |

### Use case 7, UC7 - List all customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | Customer database is not empty |  
|  Post condition     | - |
|  Nominal Scenario     | The manager retrives a list of customer with a fidelity card |
|  Variants     | - |

### Use case 8, UC8 - Search customer
| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | - |
|  Nominal Scenario     | The manager retrives info of customer X |
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
node Application {
    artifact EZShop
    database "database" as db
}

node "Computer" as c
node "Gmail Gateway" as gw
node "myPOS" as mp

Application -down- c
EZShop -right- db
EZShop -up- gw
EZShop -up- mp
```



