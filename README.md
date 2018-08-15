# Christofides-Algorithm
Christofides algorithm using Prims Algorithm for MST and main.java.
HierholzerAlgorithm Algorithm for Euler Tour. Resulting path optimized with 2-OPT, depending on how long you allow it to 
run for.

Program can be run by typing name of input txt file in argument line. 
Program assumes input document contains a text document with 3 lines representing the ID, X Coordinates and Y Coordinates.
Program will run program and output TSP path to 
screen as well as output argument appended with ".tour" containing full path and total cost of 
tour.

To run from eclipse go to run configurations. 
Click on tab "Arguments" and change Program arguments 
to whatever txt file and change the working directory(click "Other") 
to where your text file is located. 

To run from IntelliJ use settings:
1. Main class = main.mainProgram
2. program arguments test-input-7.txt
3. User class path set to Christofides Algorithm, working directory set to where ever you have Christofides algorithm

To run/install from command line do the following:
1. type "mvn install" it will create a jar for you in the target folder. If you do not have maven download it.
If you do not wish to download maven refer to a branch of this project called "LegacySnapshot" as it uses ant.
The jar created will be named "Christofides-Algoirthm-1.0-SNAPSHOT.jar".

2. Move the jar from the target folder to a location of your choice.
If you have the text documents you are planning to parse in the same folder an example command to run the program from your
terminal would be:

    java -jar Christofides-Algorithm-1.0-SNAPSHOT.jar "test-input-1.txt" 5
    
While in the same folder as your jar file and the text document you are taking data from.
