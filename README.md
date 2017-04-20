# TeamPterodactylCS320
Team Project for CS320. 

## How to Run H2 Database:

1. Have H2 installed on your machine
2. Download the database files here on the github
3. Start up the H2 console
4. In the JDBC URL, put: jdbc:h2: (where you put the database files)\\ (name of database, which is called test)
* For example, if you downloaded the files into C:\\Users\\Sherry\Documents\\SemesterWork\\Database, then the JDBC URL would be jdbc:h2:\~/Documents\\SemesterWork\\Database\\test  The jdbc:h2:\~/ is the path for the current user.
5. Login
* The current username: sa
* There is no password
6. Connect!

## How to get the code connected to the database:
1. You need to change the address of the database
2. In the view.java, change line number 24 to where you put the database files
* For example, if you put the files in the folder C:\Users\Sherry\Documents\GitHub\database\TeamPterodactylCS320
change the line to jdbc:h2:~\Documents\GitHub\database\TeamPterodactylCS320\test
* \test is neccessary to access the database!
