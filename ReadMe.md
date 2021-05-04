
# PRICER  :dollar:

SUMMARY
-------
**A java application that calculates the cost of buying & selling shares at a specific price.** It reads stock price data from a file.
When buying shares, the income from selling them is printed. When selling shares, the expense of buying them is printed.

**Pricer doesn't have a GUI. It's run from a command prompt.**

####  Input Parameters: 
 -  ``targetSize``  

    - INTEGER. Minimum amount shares needed for Pricer to calculates the income or expense of buying & selling those shares. [Default value: 200]  

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


#### with a TargetSize argument (500 shares):
 - ``java src/main/com/pricer/businesslogic/PricerMain 500 ``

