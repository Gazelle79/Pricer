Pricer
------

SUMMARY
-------
This is a small java application that reads in stock price data. It calculates the cost of buying, or the income from selling, a specific number of shares at a specific price. That cost, or expense, is printed only when the cost or expense changes. No data is written to any files.  

Pricer has only one input argument: 
 - targetSize

An INTEGER. When the total number of shares is this number, or higher, you calculate the income from selling those shares, 
or the expense of buying those shares. 

Data is read in from:
../Pricer/InputFiles/pricerdata.in

That's the default file path for input. The user can also specify their own input file path.

This application doesn't have a GUI. You run it from a command prompt.


REQUIREMENTS
------------
Java 1.8.0_181 or higher 
(https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)


OPTIONAL
--------
JUnit 4.12 or higher
This is specifically for running unit tests. JUnit isn't required, but it's suggested for any developer. 


GETTING STARTED
---------------
There are two steps to make Pricer work:
 - Compile the application
 - Execute the application

TO START
--------
 - Open a command prompt in Linux or Windows.
 - Change directories to the location of Pricer.
 - Compile the application.
 - Execute the application.

COMPILING the application:
javac src/main/com/pricer/businesslogic/PricerMain.java


EXECUTING the application:

WITH the TargetSize argument:
-----------------------------
java src/main/com/pricer/businesslogic/PricerMain [*targetSize*]

Default values will be used if no parameter is provided. They are:
targetSize: 200

