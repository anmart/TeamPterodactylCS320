# Library Catalog and User Service System 
Team Project for TeamPterodactylCS320

### How this submission is organized:
* Contains this readme.txt
* Contains a contributions.txt, listing the individual contributions of the team
* Contains a revised final report
* Contains the database files for the database called proj in the folder called db (please do not move these files)

#### How to run the code:
* Make sure the code is connected to the database
* On the command line: run Java Main (????)

### Other
#### Is the code not connecting to the database?
You need to change the address of the database!
In the view.java, change line number 33 to where you put the database files
Originally, if you put the database files in the db folder, so the url is jdbc:h2:./db/proj
/proj is neccessary to access the database!


#### A small list of data for easy testing:
Data:
Patron:
ID: 881166	PIN: 5691       NAME: Jesse Duncan      ITEM LIMIT: 5


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


Holds:
Checkout:

Librarians:

DVDs:

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

