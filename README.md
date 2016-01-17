# Christofides-Algorithm
Christofides algorithm using Prims Algorithm for MST and Hierholzer Algorithm for Euler Tour. Resulting path optimized with 2-OPT

Program can be run by typing name of input txt file in argument line. 
Program assumes input document contains a text document with 3 lines representing the ID, X Coordinates and Y Coordinates.
Program will run program and output TSP path to screen as well as output argument appeneded with ".tour" containing full path and total cost of 
tour.

Change the variable "howManyMinutesToRunTSP" to how many minutes you would like to run the TwoOpt program. It is set to 0.25 as default (15 seconds).

To run from eclipse go to run configuations. Click on tab "Arguments" and change Program arguments to whatever txt file and change the working directory(click "Other") to where your text file is located. 

To run after compile type: 

java -jar Christofides-Algorithm "insert-name-of-file-without-quotes-here.txt" 

While in the same folder as your jar file and the text document you are taking data from.
