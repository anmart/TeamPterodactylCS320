package pterodactyl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.Date;


/**
 * View
 * The class that does all the handling of the database
 * and user input
 */
public class View {

	Scanner reader;
	Connection conn;
	public View(){
		reader = new Scanner(System.in);

		try {

			// Connect to the database first
			// If it doesn't connect at all, then just exit the program

			String url = "jdbc:h2:~\\Documents\\GitHub\\database\\TeamPterodactylCS320\\test";
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(url,
					"sa",
					"");

			int inputInt = 0;

			// Added error checking for when the user doesn't enter a number
			do{
				System.out.println("Log in as:\n"
						+ "\t1. Librarian\n"
						+ "\t2. Patron\n"
						+ "\t3. Quit\n");

				System.out.print("Enter Your Response: ");

				try {
					inputInt = Integer.parseInt(reader.nextLine());
				}
				catch (NumberFormatException e){
					System.out.println("Enter a number.");
				}
			}while (inputInt == 0);

			// Need to implement login with ID after selecting what type of user you are
            int login;
			switch(inputInt){
				case 1:

				    // Keep looping till login successful or user quits
				    do{
				        // Login is either  0 - Not Found
                        //                  -1 - Cancel
                        //                  ID Number
                        login = LibrarianLogin();
                    } while(login == 0);

                    if (login > 0) //You logined! else you quit
					    LibrarianOptions(login);

					break;
				case 2:
                    // Keep looping till login successful or user quits
                    do{
                        // Login is either  0 - Not Found
                        //                  -1 - Cancel
                        //                  ID Number
                        login = PatronLogin();
                    } while(login == 0);

                    if (login > 0) //You logined! else you quit
                        PatronOptions(login);
					break;
				default:
					System.out.println("Thank you for using our system. Have a nice day");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("ERROR! Database file in use. Concurrent users are not handled.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print("Unhandled error");
			e.printStackTrace();
		}





	}

    /**
     * LibrarianLogin
     * How a person logins as a librarian
     * @return if user login correctly
     */
    public int LibrarianLogin(){
        System.out.print("Enter your ID (Enter -1 to cancel): ");
        int inputInt = reader.nextInt();
        boolean found = false;

        if (inputInt != -1) {
            //String s_queryLibrarians = "Select userID from librarian ";
			String s_queryLibrarians = "Select userID from librarian where userID = " + inputInt;


            Statement stmt;
            try {
                stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(s_queryLibrarians);


				if (res.next())
					found = true;

            } catch (SQLException e) {

            }

            if (found) {
                System.out.println("Successful login");
            } else
                System.out.println("Librarian ID not found");
        }


        if (inputInt == -1 || found)
            return inputInt;

        return 0;

    }

    /**
     * LibrarianOptions
     * How the librarian can perform their duties
     * @return true
     */

	public boolean LibrarianOptions(int librarianID){

        int inputInt = 0;
        do {
            // Added error checking for when the user doesn't enter a number
            do {
                System.out.println("Welcome to Team Pterosaur's Library System. What would you like to do?\n\n"
                        + "\t1. Add Item\n"
                        + "\t2. Remove Item\n"
                        + "\t3. Clear Waitlists\n"
                        + "\t4. Edit Personal Information\n"
                        + "\t5. Find Overdue Books\n"
                        + "\t6. Find Delinquent Patrons\n"
                        + "\t7. Find Part Time Librarians");

                System.out.print("Enter Your Response: ");

                try {
                    inputInt = Integer.parseInt(reader.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Enter a number.");
                }
            } while (inputInt == 0);

            switch (inputInt) {

                case 1:
                    addItem();
                    break;
                case 2:
                    removeItem();
                    break;
                case 3:
                    clearWaitlists();
                    break;
                case 4:
                    editPersonalInformation(librarianID);
                    break;
                case 5:
                    viewOverdueBooks();
                    break;
                case 6:
                    viewDelinquentPatrons();
                    break;
                case 7:
                    findPartTimeLibrarians();
                    break;
                default:
                    System.out.println("Error: Not a valid number. Quitting. \t Thank you for using the system.");
                    return false;

            }
        }while(inputInt > 0 && inputInt <= 7);

		return true;
	}


	public void addItem(){
	}
	public void removeItem(){
		/* Not 100% certain this is the proper way to do it,
		 * but I'm just removing the item from every table
		 * it can possibly show in
		 */
		System.out.println("Please enter Dewey ID");
		int dewey = reader.nextInt();
		System.out.println("Please enter Item Number");
		int itemNumber = reader.nextInt();

		//checkout, book, dvd, item

		String s_deleteItems = "where deweyid = " + dewey + " and itemnumber = " + itemNumber;


		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute("delete from book " + s_deleteItems);
			stmt.execute("delete from checkout " + s_deleteItems);
			stmt.execute("delete from dvd " + s_deleteItems);
			stmt.execute("delete from item " + s_deleteItems);


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	public void clearWaitlists(){
		System.out.println("Please enter Dewey ID");
		int dewey = reader.nextInt();
		System.out.println("Please enter Item Number");
		int itemNumber = reader.nextInt();

		//checkout, book, dvd, item

		String s_deleteItems = "where deweyid = " + dewey + " and itemnumber = " + itemNumber;


		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute("update holds set enddate = curDate() " + s_deleteItems);


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void editPersonalInformation(int userID){

	}

	public void viewOverdueBooks(){
		/*
		 * our original query didn't work because it was trying to find book name when it wasn't stored in checkout
		 * we have to merge with checkout and make this a bit more complex
		 */
		String s_createView = "Create Or Replace view overdue_books "
				+ "As select deweyid, itemnumber "
				+ "From checkout "
				+ "Where endDate is null and dueDate < curDate()" ;
		String s_queryView = "Select * From overdue_books";


		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(s_createView);
			ResultSet res = stmt.executeQuery(s_queryView);

			while(res.next()){
				System.out.println("\t" + res.getInt(1) + "\t" + res.getInt(2));
			}




		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void viewDelinquentPatrons(){
		/** Get's the name of the deliquent patrons
		 * By looking for names of patrons who have overdue books
		 */

		String s_queryView = "select name, ID " +
				"from user " +
				"where ID in (select distinct patronID "  +
				"from checkout " +
				"where endDate is null and dueDate < curDate())";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			//stmt.execute(s_createView);
			ResultSet res = stmt.executeQuery(s_queryView);

			while(res.next()){
				System.out.println("\t" + res.getString(1) + "\t\t\t" + res.getString(2));
			}




		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void findPartTimeLibrarians(){
		// Get the name and hours per week of part time Librarians
		String s_queryLibrarians = "Select name, hoursPerWeek from librarian inner join user on userid = id" +
				"where hoursPerWeek <= 20";


		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryLibrarians);

			System.out.printf("%-10s %-10s\n", "name", "Hours Per Week");
			while(res.next()){
				System.out.println("\t" + res.getString(1) + "\t" + res.getInt(2));
			}




		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    /**
     * PatronOptions
     * How the patron interacts with the system
     * @param patronID the patron currently login in userID
     * @return true
     */

    public boolean PatronOptions(int patronID){
        System.out.println("Welcome to Team Pterosaur\'s Library System. What would you like to do?\n\n"
                + "\t1. Search For Items\n"
                + "\t2. Edit Personal Information\n"
                + "\t3. Renew an Item\n"
                + "\t4. Check Out Item\n"
                + "\t5. Place Hold On Item\n"
                + "\t6. Remove Hold on Item");

        System.out.println("Enter Your Response: ");

        int inputInt = 0;
        while(inputInt == 0) {
            try {
                inputInt = Integer.parseInt(reader.nextLine());
            } catch (NumberFormatException e) {
                System.out.println();
            }
        }

        switch(inputInt){

            case 1:
                searchForItems();
                break;
            case 2:
                editPersonalInformation(patronID);
                break;
            case 3:
                renewItem(patronID);
                break;
            case 4:
                checkOutItem(patronID);
                break;
            case 5:
                placeHoldOnItem(patronID);
                break;
            case 6:
                removeHoldOnItem(patronID);
                break;
            default:
                System.out.println("Error: Not a valid number");
                return false;

        }

        return true;
    }

    /**
     * PatronLogin
     * How a person logins as a patron
     * @return userID if user login correctly, -1 if the user cancels, 0 user failed
     */
    public int PatronLogin(){
        System.out.print("Enter your ID (Enter -1 to cancel): ");
        int inputInt = 0;
        while (inputInt == 0) {
            try {
                inputInt = Integer.parseInt(reader.nextLine());

                if (inputInt <= 0 && inputInt != -1){
                    System.out.println("That's not a valid ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }

        int pin = 0;
        boolean found = false;
        boolean correct = false;

        if (inputInt != -1) {
            String s_queryPatron = "Select userID from patron where userID = " + inputInt;

            Statement stmt;
            try {
                stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(s_queryPatron);


                if (res.next())
                    found = true;

                if (found) {
                    System.out.print("Enter your PIN (Enter -1 to cancel): ");

                    try {
                        pin = Integer.parseInt(reader.nextLine());

                        s_queryPatron = "Select ID from user where ID = " + inputInt + " and PIN = " + pin;

                        res = stmt.executeQuery(s_queryPatron);

                        if (res.next()) {
                            correct = true;
                            System.out.println("Successful login");
                        } else {
                            correct = false;
                            System.out.println("Incorrect PIN");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("That's not a valid PIN");
                    }

                } else
                    System.out.println("Patron ID not found");


            } catch (SQLException e) {
                // Should probably do something more here
                System.out.println("SQL ERROR " + e.toString());
            }


        }

        if (inputInt == -1 || correct)
            return inputInt;

        return 0;

    }

    /**
     * searchForItems
     * A user can search for items through search terms like
     * author, director, year, genre, or deweyID
     * Will output the search results
     */
	public void searchForItems(){
        int inputInt = 0;

        // Added error checking for when the user doesn't enter a number
        do{
            System.out.println("Search by\n" +
                    "\t1. Author\n" +
                    "\t2. Director\n" +
                    "\t3. Year\n" +
                    "\t4. Genre\n" +
                    "\t5. DeweyID\n");

            System.out.println("Enter Your Response: ");

            try {
                inputInt = Integer.parseInt(reader.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Enter a number.");
            }

            if (inputInt > 5 || inputInt < 1){
                System.out.println("Please enter a valid number.");
            }
        }while (inputInt == 0);


        int substring = 0;
        do{
            System.out.println("Use Search Term as Substring?\n"
                    + "\t1. Yes\n"
                    + "\t2. No\n");

            System.out.print("Enter Your Response (Number): ");

            try {
                substring = Integer.parseInt(reader.nextLine());
                if (substring != 1 && substring != 2)
                    System.out.print("Defaulting to No.");
            }
            catch (NumberFormatException e){
                System.out.println("Enter a number.");
            }
        }while (substring == 0);

		boolean useSubstring = substring == 1;

		System.out.println("Enter your search term: ");
		String titleQuery = reader.next();
		if(useSubstring)
			titleQuery = "%" + titleQuery + "%";

		String whereClause = "";
		switch(inputInt){
            case 1:
                whereClause = "natural join book where author like '";
                break;
            case 2:
                whereClause = "natural join DVD where director like '";
                break;
            case 3:
                whereClause = "where year like '";
                break;
            case 4:
                whereClause = "where genre like '";
                break;
            case 5:
                whereClause = "where deweyID like '";
                break;
        }
		String s_queryItems = "Select deweyID, ItemNumber, title from item " + whereClause + titleQuery +
                "' group by deweyID";
		// group by will make sure the same books will stick together
        // it'd be really weird if Book A copy 1 and Book A copy 2 were far apart


		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryItems);

			if (res.next() == false)
				System.out.println("No items found.");
			else {
				System.out.println(res.getRow() + ". \t" + res.getInt(1) + "\t" + res.getInt(2) + "\t" + res.getString(3));
				while (res.next()) {
					System.out.println(res.getRow() + ". \t" + res.getInt(1) + "\t" + res.getInt(2) + "\t" + res.getString(3));
				}
			}

		} catch (SQLException e) {
			System.out.println("Error with the SQL " + e.toString());
			//e.printStackTrace();
		}
	}

    /**
     * renewItem
     * A patron can renew an item they currently have checked out if it's not overdue or has a waiting list
     * @param patronID
     */
	public void renewItem(int patronID){
		int inputInt = 0;
		int itemNumber = 0;

		// Added error checking for when the user doesn't enter a number
		do{
			System.out.println("Enter the dewey ID of the item you want to renew: ");

			try {
				inputInt = Integer.parseInt(reader.nextLine());
			}
			catch (NumberFormatException e){
				System.out.println("Enter a number.");
			}

			System.out.println("Enter the item number of the item you want to renew: ");

			try {
				itemNumber = Integer.parseInt(reader.nextLine());
			}
			catch (NumberFormatException e){
				System.out.println("Enter a number.");
			}

		}while (inputInt == 0 || itemNumber == 0);

		String s_queryFindItem = "Select deweyID, itemnumber from item where " +
				"deweyID = " + inputInt +
				" and itemnumber = " + itemNumber;

		String s_queryFindYourCheckOut = "Select endDate, dueDate from checkout where " +
				"deweyID = " + inputInt +
				" and itemnumber = " + itemNumber +
				" and patronID = " + patronID +
				" endDate is null";

		String s_queryFindOtherHolds = "Select * from holds where " +
				"deweyID = " + inputInt +
				" and itemnumber = " + itemNumber +
				" and patronID != " + patronID; //Not sure if patronId check is neccessary

        String s_queryUpdateCheckout = "Update checkout set dueDate = DATEADD(week,2,CURRENT_TIMESTAMP) " +
                "deweyID = " + inputInt +
                " and itemnumber = " + itemNumber +
                " and patronID = " + patronID + " and endDate is not null";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryFindItem);

			if (res.next() == false)
				System.out.println("No item found. Make sure the deweyID of " + inputInt + " and the itemnumber of " +
						itemNumber + " is correct");
			else {
				ResultSet resFindCOStatus = stmt.executeQuery(s_queryFindYourCheckOut);
				if (resFindCOStatus.next() == false) {
					System.out.println("This item is not currently checked out by you.");
					// the user hasn't checked out this book before
				} else if (resFindCOStatus.getDate(1) != null) {
					System.out.println("This item is not currently checked out by you.");
					// this user has already returned the book
				} else if (resFindCOStatus.getDate(2).compareTo(Date.valueOf(LocalDate.now())) < 0) {
					// the dueDate has already passed! can't renew overdue book
					System.out.println("This item is overdue. Cannot renew an overdue item. Please return it.");
				} else {
					ResultSet resFindBookStatus = stmt.executeQuery(s_queryFindOtherHolds);
					if (resFindBookStatus.next() == false) {
						//update the checkout
                        stmt.executeQuery(s_queryUpdateCheckout);
					}
					else{
						// another user has a hold on this item
						System.out.println("This item has existing hold(s). You cannot renew it.");
					}
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

    /**
     * checkOutItem
     * A patron can check out an item if all these conditions are met
     * 1. Item is not currently checked out
     * 2. Item has either no waiting list or Patron's hold is number one on the waitlist
     * 3. Patron has not checked out max number of books
     * @param patronID
     */
	public void checkOutItem(int patronID){
        int itemDeweyID = 0;
        int itemNumber = 0;

        // get valid input
        while (itemDeweyID == 0){
            try {
                System.out.println("Please Enter Item Dewey ID (or -1 to cancel):\n");
                itemDeweyID = Integer.parseInt(reader.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Enter a valid number.");
            }

            if (itemDeweyID < 0 && itemDeweyID != -1){
                System.out.println("Enter a valid number (greater than zero or -1).");
                itemDeweyID = 0;
            }

        }

        // did the user quit
        if (itemDeweyID == -1) {
            System.out.println("Returning back to the menu.");
            return;
        }

        // get valid input
        while (itemNumber == 0){
            try {
                System.out.println("Please Enter Item Number (or -1 to cancel):\n");
                itemNumber = Integer.parseInt(reader.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Enter a valid number.");
            }

            if (itemNumber < 0 && itemNumber != -1){
                System.out.println("Enter a valid number (greater than zero or -1).");
                itemNumber = 0;
            }
        }

        // Did the user cancel
        if (itemNumber == -1) {
            System.out.println("Returning back to the menu.");
            return;
        }
        String s_queryExists = "Select * from item where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber;

        String s_queryFindNumberItems = "Select count(*) from checkout where " +
                "patronID = " + patronID + " and endDate is null ";

        String s_queryFindACheckOut = "Select endDate, dueDate from checkout where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber +
                " and endDate is null";

        String s_queryFindOtherHolds = "Select patronID, position from holds where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber +
                " and patronID != " + patronID  + //Not sure if patronId check is neccessary
                " and endDate is null" +
                " order by position asc";

        String s_queryUpdateCheckout = "Insert into checkout " +
                "(PATRONID, DEWEYID, ITEMNUMBER, BEGINNINGDATE, ENDDATE, DUEDATE)" +
                "values (" + patronID + ", " + itemDeweyID + ", " + itemNumber + ", " +
                "curDate(), null, DATEADD(week,2,CURRENT_TIMESTAMP)";
        String s_queryChangeHolds = "Update checkout set position = position - 1 where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber +
                " and endDate is null";
        String s_queryRemoveHold = "Update checkout set endDate = curDate() where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber +
                " and patronID = " + patronID +
                " and endDate is null";

        String s_queryItemLimit = "Select itemLimit from patron where userID = " + patronID;
        Statement stmt;
        int numberOfItems = 0;

        try {
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(s_queryExists);

            if (res.next() == true) {
                res = stmt.executeQuery(s_queryFindACheckOut);
                if (res.next() == false){
                    res = stmt.executeQuery(s_queryFindNumberItems);
                    numberOfItems = res.getInt(1);
                    res = stmt.executeQuery(s_queryItemLimit);
                    res.next();
                    if (numberOfItems < res.getInt(1))
                    {
                        res = stmt.executeQuery(s_queryFindOtherHolds);
                        if (res.next() == false)
                            stmt.execute(s_queryUpdateCheckout);
                        else{
                            // check that the first hold is the patron
                            if (res.getInt(2) == patronID) {
                                stmt.execute(s_queryUpdateCheckout); // Check out book
                                stmt.execute(s_queryRemoveHold); // End the current hold
                                stmt.execute(s_queryChangeHolds); // Change other positions
                            }
                            else{
                                System.out.println("Someone else has a hold on this book.");
                            }
                        }
                    }
                    else
                        System.out.println("You are at the limit of items checked out.");


                }
                else
                    System.out.println("This item is currently checked out.");
            }
            else
                System.out.println("This item doesn't exist.");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * placeHoldOnItem
     * A patron can place a hold on an item
     * @param patronID - patron's user id
     */

	public void placeHoldOnItem(int patronID){
        int itemDeweyID = 0;
        int itemNumber = 0;

        // get valid input
        while (itemDeweyID == 0){
            try {
                System.out.println("Please Enter Item Dewey ID (or -1 to cancel):\n");
                itemDeweyID = Integer.parseInt(reader.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Enter a valid number.");
            }

            if (itemDeweyID < 0 && itemDeweyID != -1){
                System.out.println("Enter a valid number (greater than zero or -1).");
                itemDeweyID = 0;
            }

        }

        // did the user quit
        if (itemDeweyID == -1) {
            System.out.println("Returning back to the menu.");
            return;
        }

        // get valid input
        while (itemNumber == 0){
            try {
                System.out.println("Please Enter Item Number (or -1 to cancel):\n");
                itemNumber = Integer.parseInt(reader.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Enter a valid number.");
            }

            if (itemNumber < 0 && itemNumber != -1){
                System.out.println("Enter a valid number (greater than zero or -1).");
                itemNumber = 0;
            }
        }

        // Did the user cancel
        if (itemNumber == -1) {
            System.out.println("Returning back to the menu.");
            return;
        }

        String s_queryExists = "Select * from item where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber;

        String s_queryFindYourHold = "Select * from holds where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber +
                " and patronID = " + patronID +
                " and endDate is null";

        // doesn't work see github
        String s_count = "Select count(patronID) from holds where " +
                "deweyID = " + itemDeweyID +
                " and itemnumber = " + itemNumber +
                " and endDate is null";
        int count = 1;

        String s_queryAddHold = "Insert into holds " +
                "(PATRONID, DEWEYID, ITEMNUMBER, ENDDATE, POSITION)" +
                " values (" + patronID + ", " + itemDeweyID + ", " + itemNumber + ", " +
                " null, " + count + ")";

        Statement stmt;

        try {
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(s_queryExists);

            // Does this item exist?
            if (res.next() == true) {
                res = stmt.executeQuery(s_queryFindYourHold);
                // Does the patron already have a hold on this item?

                if (res.next() == false){
                    // Create a new hold for this item
                    res = stmt.executeQuery(s_count);
                    if (res.next() == true)
                        count = res.getInt(1);
                    stmt.execute(s_queryAddHold);
                    System.out.println("You have a placed a hold on this item");
                }
                else
                    System.out.println("You already have a hold on this item.");
            }
            else
                System.out.println("This item doesn't exist.");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * RemoveHoldonItem
     * A patron can remove their hold on an item
     * @param patronID patron's user id
     */
	public void removeHoldOnItem(int patronID){
		/*
		 * might be better(/necessary) to ask them for their userId when logging in
		 * Also, this one doesn't actually tell you if it worked or not. If you do one with an end date < today, it'll still say updated
		 * ALSO, APPARENTLY THE PATRONID AND DEWEYID GOT MESSED UP ON CREATION, THEY'RE THE SAME WHOOPS (we need to fix that)
		 */
        int itemDeweyID = 0;
        int itemNumber = 0;

        // get valid input
        while (itemDeweyID == 0){
            try {
                System.out.println("Please Enter Item Dewey ID (or -1 to cancel):\n");
                itemDeweyID = Integer.parseInt(reader.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Enter a valid number.");
            }

            if (itemDeweyID < 0 && itemDeweyID != -1){
                System.out.println("Enter a valid number (greater than zero or -1).");
                itemDeweyID = 0;
            }

        }

        // did the user quit
        if (itemDeweyID == -1) {
            System.out.println("Returning back to the menu.");
            return;
        }

        // get valid input
        while (itemNumber == 0){
            try {
                System.out.println("Please Enter Item Number (or -1 to cancel):\n");
                itemNumber = Integer.parseInt(reader.nextLine());
            }
            catch (NumberFormatException e){
                System.out.println("Enter a valid number.");
            }

            if (itemNumber < 0 && itemNumber != -1){
                System.out.println("Enter a valid number (greater than zero or -1).");
                itemNumber = 0;
            }
        }

        // Did the user cancel
        if (itemNumber == -1) {
            System.out.println("Returning back to the menu.");
            return;
        }

		String s_queryHoldExists = "Select * from holds where userID = " + patronID +  " and DeweyID = " + itemDeweyID
                + " and itemNumber = " + itemNumber + " and (endDate > curDate() or endDate is null)";

        String s_queryHoldEnd = "update holds set endDate = curDate() where userID = " + patronID +  " and DeweyID = " + itemDeweyID
                + " and itemNumber = " + itemNumber + " and (endDate > curDate() or endDate is null)";

		Statement stmt;
		try {
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(s_queryHoldExists);

			if (res.next() == true)
			    stmt.execute(s_queryHoldEnd);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Updated hold.");

	}

}

