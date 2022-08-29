
# PRICER  :dollar: :pound: :euro:

SUMMARY
-------
**Pricer is a small java application that reads stock price data from a file.** It calculates: 

* the cost of buying shares at a specified price 
* the income from selling shares at a specified price

That income - or expense - is printed only when the cost of buying or selling changes. No data is written to any files.  

**Pricer doesn't have a GUI. It is run from a command prompt.**

####  Input Parameters: 
 -  ``targetSize``  - [Integer, Default value: 200] Minimum number of total shares needed before Pricer calculates the income / expense of buying / selling the shares. 

####  Input:

Data is read in from:

- ``pricerdata.in``

    Location:``.../Pricer/InputFiles/pricerdata.in``

A user can optionally specify their own input file path.

REQUIREMENTS
------------
- [x] Java 14 or higher 
- [x] Maven 3.6.2 or higher

OPTIONAL
--------
- [x] JUnit 5.8.1 or higher
 
 
GETTING STARTED
---------------
There are two steps to make Pricer work:
 - [x] Compile the application
 - [x] Unit test the application
 - [x] Execute the application

TO START
--------
 - Open a command prompt in Linux or Windows.
 - Change directories to the location of Pricer.
 - Compile the application.
 - Unit test the application.
 - Execute the application.

### COMPILING Pricer:
`` mvn clean compile ``

### UNIT TESTING Pricer:
`` mvn clean test ``

### EXECUTING Pricer:
#### without any arguments:
 - ``mvn exec:java ``

The default value for ``targetSize`` is used if no argument is provided.


#### with the TargetSize argument [example: 200]:
 - ``mvn exec:java -Dexec.args="200" ``

