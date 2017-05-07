# Library Catalog and User Service System 
Team Project for TeamPterodactylCS320

### How this submission is organized:
* Contains this readme.txt
* Contains a contributions.txt, listing the individual contributions of the team
* Contains a revised final report
* Contains the project in folder called TeamPterodactylCS320
* Within that folder, there are database files for the database called proj in the folder called db (please do not move these files)
* Within that folder, there is a jar file which you will need to run the program

#### How to run the code:
* Extract everything from the folder (please do not mess up the folder hierarchy)
* Make sure the code is connected to the database
* On the command line, enter the command: java -jar PterodactylProject.jar

### Other
#### Is the code not connecting to the database?
You need to change the address of the database!
In the view.java, change line number 33 to where you put the database files
Originally, if you put the database files in the db folder, so the url is jdbc:h2:./db/proj
/proj is neccessary to access the database!
Then, you'll have to recreate the jar file.

#### Concurrent users accessing database issues?
Please make sure that the database is not opened in H2!


#### A small list of data for easy testing:
Data:
Patron:
*ID: 881166	PIN: 5691       NAME: Jesse Duncan      ITEM LIMIT: 5
*ID: 917062	PIN: 1922       NAME: Sarah Creamcheese     ITEM LIMIT: 50
*ID: 416246	PIN: 1474       NAME: Wanda Allen       ITEM LIMIT: 50

Librarians:
*ID: 130478	PIN: 4582       NAME: Jonathan Fisher
*ID: 197222	PIN: 2599	NAME: Gerald Fuller
*ID: 931297	PIN: 8842	NAME: Mildred Thomas


Book:
Title:          Does Dinosaur Sometimes Make You Feel Stupid?	
Genre:          Fable	
DeweyID:        931297	
Item Number:    8	
Year:           2004	
Author:         Morse Honorato

Title:          The Truth Is You Are Not The Only Person Concerned About Dinosaur	
Genre:          Poetry	
DeweyID:        329166	
Item Number:    4	
Year:           1997	
Author:         Calhoun Allen

Title:          The Death Of Dinosaur And How To Avoid It	
Genre:          Poetry	
DeweyID:        928122	
Item Number:    6	
Year:           2002	
Author:         Sloan Quail

Title:          How To Learn Dinosaur	
Genre:          Poetry	
DeweyID:        850821	
Item Number:    7	
Year:           2009	
Author:         Sloan Quail

Title:          10 Tips That Will Make You Influential In Dinosaur	
Genre:          Poetry	
DeweyID:        488095	
Item Number:    9	
Year:           1990	
Author:         Sloan Quail

Title:          I Don't Want To Spend This Much Time On Dinosaur. How About You?	
Genre:          Poetry	
DeweyID:        409164	
Item Number:    6	
Year:           2011	
Author:         Sloan Quail


DVDs:
Title:          Can You Really Find Dinosaur (on the Web)?
Genre:          Science Fiction
DeweyID:        917062
Item Number:    4
Year:           1995
Director:       James Mark

Title:          5 Ways To Get Through To Your Dinosaur
Genre:          Science Fiction
DeweyID:        955084
Item Number:    9
Year:           2012
Director:       Valencia Sheila

Title:          What Can You Do To Save Your Dinosaur From Destruction By Social Media?
Genre:          Science Fiction
DeweyID:        113826
Item Number:    5
Year:           1998
Director:       Sanchez Boris

Title:          The Quickest & Easiest Way To Dinosaur
Genre:          Science Fiction
DeweyID:        235027
Item Number:    1
Year:           2011
Director:       Murphy Britanni

Title:          Top 10 Tips With Dinosaur
Genre:          Mystery
DeweyID:        141198
Item Number:    4
Year:           1995
Director:       Gallegos Hannah

Title:          Apply These 5 Secret Techniques To Improve Dinosaur
Genre:          Mystery
DeweyID:        415830
Item Number:    9
Year:           1994
Director:       Dalton Uma

#### How to Run H2 Database:

1. Have H2 installed on your machine
2. Download the database files here on the github
3. Start up the H2 console
4. In the JDBC URL, put: jdbc:h2: (where you put the database files)\\ (name of database, which is called test)
* For example, if you downloaded the files into C:\\Users\\Sherry\Documents\\SemesterWork\\Database, then the JDBC URL would be jdbc:h2:\~/Documents\\SemesterWork\\Database\\proj  The jdbc:h2:\~/ is the path for the current user.
5. Login
* The current username: sa
* There is no password
6. Connect!

