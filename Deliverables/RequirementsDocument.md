# Requirements Document 

Authors:

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
|   Manager     |  Is the owner of the shop and the administrator of the system           | 
| Cashier | Interact with customers, manages the inventory |
| Customer | Buys products, can have a fidelity card |


# Context Diagram and interfaces

## Context Diagram
\<Define here Context diagram using UML use case diagram>

```plantuml
actor Manager as a
actor Cashier as o
actor User as u
a -up-|> u
o -up-|> u
u -> (EZShop)
```

\<actors are a subset of stakeholders>

## Interfaces
\<describe here each interface in the context diagram>
\<GUIs will be described graphically in a separate document>
| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   The administrator     | GUI | Desktop |
|   The cashier     | GUI | Desktop,Point of Sale,  Barcode reader |
|   The owner     | GUI | Desktop |

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
|   FR1     | Authorize and authenticate  |
|   FR1.1   | Log in |
|   FR1.2   | Log out |
|	FR2    	| Manage inventory |
|	FR2.1	| Add items (manually or barcode)  |
|	FR2.2	| Decrease/increase amount of items |
|	FR2.3	| Modify info about items |
|	FR2.4	| Add categories for items |
|	FR2.5	| Search items |
|	FR2.6	| Generate reports about inventory |
|	FR3		| Manage suppliers |
|	FR3.1	| Add suppliers |
|	FR3.2	| Remove suppliers |
|	FR3.3	| List suppliers that can provide an item |
|	FR4		| Manage customers |
|	FR4.1	| Add customer (through fidelity card) |
|	FR4.2	| Modify info about customers |
|	FR4.3	| Delete customers |
|	FR4.4	| List all customers |
|	FR4.5	| Search customers |
|	FR4.6	| Filter list of customers |
|	FR4.7	| Notify customers about discounts |
|   FR5     | Handle Sales |
|   FR5.1   | Start sale transaction |
|   FR5.2   | End sale transaction |
|   FR5.2.1 | Transaction - success |
|   FR5.2.2 | Transaction - cancel |
|   FR5.3   | Handle payment  |
|   FR5.3.1 | Payment by cash  |
|   FR5.3.2 | Payment by credit card  |
|   FR5.3.3 | Payment by other methods  |
|   FR6     | Handle Product  |
|   FR6.1   | Read bar code  |
|   FR6.2   |  Input quantity of product |
|   FR6.3   | Retrieve name and description of product|
|   FR6.4   | Cancel product from sale  |
|   FR6.5   | Last product, compute total|
|   FR6.6   | Display total amount due|
|   FR6.7   | Print receipt|
|   FR6.8   | Deduce stock amount from inventory |
|   FR6.9   | Handle return|
|   FR7     | Manage accounting |
|   FR7.1   | Calculate income |
|   FR7.2   | Record new expense |
|   FR7.2.1 | Record new products order |
|   FR7.2.2 | Record new shop expense (shop costs like rent, gas, light, garbage,...) |
|   FR7.2.3 | Record employee payment |
|   FR7.2.4 | Record taxes |
|   FR7.3   | List expenses |
|   FR7.4   | List incomes |
|   FR7.5   | Show balance |



## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | Privacy  | Customer data must be encripted | FR3|
|  NFR2     | Usability | Time to learn how to use for non Engineers < 1 day | |
|  NFR3     | | | |
| NFRx .. | | | | 
|  Domain1 | | Currency is Euro | |


# Use case diagram and use cases


## Use case diagram
\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>


\<next describe here each use case in the UCD>
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



# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design
\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >

