
# PRICER  :dollar: :pound: :euro:

SUMMARY
-------
**Pricer is a small java application that reads stock price data from a file.** It calculates: 

* the cost of buying shares at a specified price 
* the income from selling shares at a specified price

That income - or expense - is printed only when the cost of buying or selling changes. No data is written to any files.  

**Pricer doesn't have a GUI. It is run from a command prompt.**

####  Input Parameters: 
 -  ``targetSize``  

    - INTEGER. (Default value: 200) When the total number of shares is this number or higher, Pricer calculates the income or expense of buying oor selling the shares. 

####  Input:

Data is read in from:

- ``pricerdata.in``

    Location:``.../Pricer/InputFiles/pricerdata.in``

A user can optionally specify their own input file path.

REQUIREMENTS
------------
- [x] Java 1.8.0_181 or higher 
(https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)


OPTIONAL
--------
- [x] JUnit 4.12 or higher
 
 
GETTING STARTED
---------------
There are two steps to make Pricer work:
 - [x] Compile the application
 - [x] Execute the application

TO START
--------
 - Open a command prompt in Linux or Windows.
 - Change directories to the location of Pricer.
 - Compile the application.
 - Execute the application.

### COMPILING Pricer:
`` javac src/main/com/pricer/businesslogic/PricerMain.java ``


### EXECUTING Pricer:
#### without any arguments:
 - ``java src/main/com/pricer/businesslogic/PricerMain ``

The default value for ``targetSize`` is used if no argument is provided.


#### with the TargetSize argument:
 - ``java src/main/com/pricer/businesslogic/PricerMain [*targetSize*] ``

