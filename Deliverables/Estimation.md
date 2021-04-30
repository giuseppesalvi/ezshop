1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
# Project Estimation  
Authors:
Date:
Version:
# Contents
- [Estimate by product decomposition]
- [Estimate by activity decomposition ]
# Estimation approach
<Consider the EZGas  project as described in YOUR requirement document, assume that you are going to develop the project INDEPENDENT of the deadlines of the course>
# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
| NC =  Estimated number of classes to be developed   | 20 |             
|  A = Estimated average size per class, in LOC       | 80 | 
| S = Estimated size of project, in LOC (= NC * A) | 1600|
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour) | 160 |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) | 4800 | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) | 1 week |               
# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
| Process Instantiation | 8 |
| System requirements analysis | 16 |
| System architecture definition | 8 |
| Software requirements analysis | 60 |
| Software architecture definition | 8 |
| Software detailed design | 32 |
| Coding and unit testing | 100 |
| Integration of software units | 4 |
| Software validation | 10 |
| System integration | 8 |
| System validation | 10 |

###
Insert here Gantt chart with above activities
```plantuml
@startgantt
[Process Instantiation] lasts 1 days
[System requirements analysis] lasts 1 days
[System architecture definition] lasts 1 days

[Software requirements analysis] lasts 2 days
[Software architecture definition]  lasts 1 days

[Software detailed design] lasts 1 days

[Coding and unit testing] lasts 3 days
[Integration of software units] lasts 1 days

[Software validation] lasts 1 days
[System integration] lasts 1 days
[System validation] lasts 1 days

Project start 2021-03-31
[Process Instantiation] starts 2021-03-31
[System requirements analysis] starts 2021-03-31
[System architecture definition] starts 2021-03-31

[Software requirements analysis] starts 2021-04-01
[Software architecture definition]  starts 2021-04-02

[Software detailed design] starts 2021-04-05

[Coding and unit testing] starts 2021-04-06
[Integration of software units] starts 2021-04-08

[Software validation] starts 2021-04-09
[System integration] starts 2021-04-09
[System validation] starts 2021-04-09


@endgantt
```
