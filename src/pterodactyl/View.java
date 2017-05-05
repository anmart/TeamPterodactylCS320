package pterodactyl;

import java.util.InputMismatchException;
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

	public View() {
		reader = new Scanner(System.in);

		try {

			// Connect to the database first
			// If it doesn't connect at all, then just exit the program

			boolean session;
            String url = "jdbc:h2:./db/proj";
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(url,
					"sa",
					"");

			int inputInt = 0;

			// Added error checking for when the user doesn't enter a number
			do {
				System.out.println("Log in as:\n"
						+ "\t1. Librarian\n"
						+ "\t2. Patron\n"
						+ "\t3. Quit\n");

				System.out.print("Enter Your Response: ");

				try {
					inputInt = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a number.");
				}
			} while (inputInt == 0);

			// Need to implement login with ID after selecting what type of user you are
			int login;
			switch (inputInt) {
				case 1:

					// Keep looping till login successful or user quits
					do {
						// Login is either  0 - Not Found
						//                  -1 - Cancel
						//                  ID Number
						login = LibrarianLogin();
					} while (login == 0);

					if (login > 0) //You logined! else you quit
						do {
							session = LibrarianOptions(login);
						} while (session);

					break;
				case 2:
					// Keep looping till login successful or user quits
					do {
						// Login is either  0 - Not Found
						//                  -1 - Cancel
						//                  ID Number
						login = PatronLogin();
					} while (login == 0);


					if (login > 0) //You logined! else you quit
						do {
							session = PatronOptions(login);
						} while (session);
					break;
				default:
					//Nothing?
					break;

			}
		} catch (SQLException e) {
			System.out.println("ERROR! Database file in use. Concurrent users are not handled.");
		} catch (Exception e) {
		    //Not sure how this would happen
			System.out.println("Cannot run");
		}

		System.out.println("Thank you for using our system. Have a nice day");


	}

	/**
	 * LibrarianLogin
	 * How a person logins as a librarian
	 *
	 * @return if user login correctly
	 */
	public int LibrarianLogin() {


		System.out.print("Enter your ID (Enter -1 to cancel): ");
		int inputInt = 0;
		while (inputInt == 0) {
			try {
				inputInt = Integer.parseInt(reader.nextLine());

				if (inputInt <= 0 && inputInt != -1) {
					System.out.println("Enter a valid ID.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Enter a number.");
			}
		}

		int pin = 0;
		boolean found = false;
		boolean correct = false;

		if (inputInt != -1) {
			String s_queryLibrarian = "Select userID from librarian where userID = " + inputInt;

			Statement stmt;
			try {
				stmt = conn.createStatement();
				ResultSet res = stmt.executeQuery(s_queryLibrarian);


				if (res.next())
					found = true;

				if (found) {
					System.out.print("Enter your PIN (Enter -1 to cancel): ");

					try {
						pin = Integer.parseInt(reader.nextLine());
						if (pin == -1)
							return -1;

						s_queryLibrarian = "Select ID from user where ID = " + inputInt + " and PIN = " + pin;

						res = stmt.executeQuery(s_queryLibrarian);

						if (res.next()) {
							correct = true;
							System.out.println("Successful login");
						} else {
							correct = false;
							System.out.println("Incorrect PIN");
						}

					} catch (NumberFormatException e) {
						System.out.println("Enter a valid PIN");
					}

				} else
					System.out.println("Librarian ID not found");


			} catch (SQLException e) {
				// Not sure how this would happen
				System.out.println("Cannot login");
			}


		}

		if (inputInt == -1 || correct)
			return inputInt;

		return 0;

	}

	/**
	 * LibrarianOptions
	 * How the librarian can perform their duties
	 *
	 * @return true
	 */

	public boolean LibrarianOptions(int librarianID) {

		// This shouldn't quit after the user enters a bad number
		int inputInt = 0;
		do {
			// Added error checking for when the user doesn't enter a number

			System.out.println("Welcome to Team Pterosaur's Library System. What would you like to do?\n\n"
					+ "\t1. Add Item\n"
					+ "\t2. Remove Item\n"
					+ "\t3. Clear Waitlists\n"
					+ "\t4. Edit Personal Information\n"
					+ "\t5. Find Overdue Books\n"
					+ "\t6. Find Delinquent Patrons\n"
					+ "\t7. Find Part Time Librarians");

			while (inputInt == 0) {
				System.out.print("Enter Your Response: ");
				try {
					inputInt = Integer.parseInt(reader.nextLine());
					if (inputInt == 0)
						inputInt = -1;
				} catch (NumberFormatException e) {
					System.out.println("Enter a number.");
				}
			}

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
					System.out.println("Not a valid choice. Quitting. \t Thank you for using the system.");
					return false;

			}
		} while (inputInt > 0 && inputInt <= 7);

		return true;
	}


	/***
	 * Adds a specified item into the database
	 */
	public void addItem(){
		try{//using a massive try since we have to enter so many numbers throughout this function
			System.out.println("What would you like to add?\n"
					+ "\t1) Book\n"
					+ "\t2) DVD\n"
					+ "\t3) Quit");
			int choiceNum = reader.nextInt();
			if(choiceNum >= 3){
				System.out.println("Quitting.");
				return;
			}

			System.out.println("Please enter the dewey ID");
			int deweyID = reader.nextInt();  reader.nextLine();//to clear newline character
			System.out.println("Please enter the item Number");
			int itemNumber= reader.nextInt(); reader.nextLine();//to clear newline character
			System.out.println("Please enter the title");
			String title = reader.nextLine();
			System.out.println("Please enter the genre");
			String genre = reader.nextLine();
			System.out.println("Please enter the release year");
			int year = reader.nextInt();reader.nextLine();//to clear newline character



			String secondStatement = "";
			if(choiceNum == 1){

				System.out.println("Please enter the Author's name");
				String authorName = reader.nextLine();
				secondStatement = "insert into book values (" + deweyID+", "+ itemNumber+", '"+ authorName+"')";
			}
			if(choiceNum == 2){

				System.out.println("Please enter the Director's name");
				String authorName = reader.nextLine();
				secondStatement = "insert into dvd values (" + deweyID+", "+ itemNumber+", '"+ authorName+"')";
			}

			Statement stmt;

			try {
				stmt = conn.createStatement();


				stmt.execute("insert into item values ('" + title+"', '"+genre+"', "+deweyID+", "+itemNumber+", "+year+")" );
				stmt.execute(secondStatement);





			} catch (SQLException e) {
				System.out.println("Something went wrong, maybe this item already exists?");
			}

		}catch(InputMismatchException e){
			System.out.println("Please enter a valid number");
			return;

		}
	}


	/***
	 * removes a specified item from the database
	 *
	 */
	public void removeItem(){

		System.out.println("Please enter Dewey ID");
		int dewey, itemNumber;
		try{
			dewey = reader.nextInt();
			System.out.println("Please enter Item Number");
			itemNumber = reader.nextInt();reader.nextLine();//to clear newline character
		}catch(InputMismatchException e){
			System.out.println("Please enter a valid number");
			return;

		}

		//checkout, book, dvd, item

		String s_deleteItems = "where deweyid = " + dewey + " and itemnumber = " + itemNumber;


		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("Select * from item " + s_deleteItems);
			if(res.next()){
				System.out.println("Item found. Deleting.");
				stmt.execute("delete from book " + s_deleteItems);
				stmt.execute("delete from checkout " + s_deleteItems);
				stmt.execute("delete from dvd " + s_deleteItems);
				stmt.execute("delete from item " + s_deleteItems);
			}
			else{
				System.out.println("Sorry, Item was not found. Please Try Again.");
			}

		} catch (SQLException e) {
			System.out.println("Cannot remove an item");
		}



	}

	/***
	 * clears all waitlists for a specified item
	 *
	 */
	public void clearWaitlists(){
		System.out.println("Please enter Dewey ID");
		int dewey, itemNumber;
		try{
			dewey = reader.nextInt();
			System.out.println("Please enter Item Number");
			itemNumber = reader.nextInt();reader.nextLine();//to clear newline character
		}catch(InputMismatchException e){
			System.out.println("Please enter a valid number");
			return;

		}

		//checkout, book, dvd, item

		String s_deleteItems = "where deweyid = " + dewey + " and itemnumber = " + itemNumber;


		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet res = stmt.executeQuery("Select * from holds " + s_deleteItems);
			if(res.next()){
				System.out.println("Holds found. Deleting.");
				stmt.execute("delete from holds " + s_deleteItems);
			}
			else{
				System.out.println("Sorry, holds was not found. Please Try Again.");
			}





		} catch (SQLException e) {
			System.out.println("Cannot clear a waitlist");
		}
	}

	/**
	 * Edit Personal Information
	 * A user will be edit their Name or PIN
	 *
	 * @param userID
	 */
	public void editPersonalInformation(int userID) {
		int inputInt = 0;
		boolean valid = false;

		// Added error checking for when the user doesn't enter a number
		do {
			System.out.println("Edit your personal information\n" +
					"\t1. Name\n" +
					"\t2. Pin\n" +
					"\t3. Quit\n");


			System.out.print("Enter Your Response: ");

			while (!valid) {
				try {
					inputInt = Integer.parseInt(reader.nextLine());
					valid = true;
				} catch (NumberFormatException e) {
					System.out.println("Enter a number.");
				}

			}

			if (inputInt > 3 || inputInt < 1) {
				System.out.println("Enter a valid choice.");
				inputInt = 0;
			}

		} while (inputInt == 0);

		String newInfo = "";

		try {
			Statement stmt;
			String s_queryUser = "Select NAME from user where ID = " + userID;
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryUser);


			if (res.next() == false)
				System.out.println("No user found.");
				// This should never happen
			else {
				switch (inputInt) {
					case 1:
						System.out.println("Your current name is " + res.getString(1));
						System.out.print("What would you like to change your name to?");
						newInfo = reader.nextLine();

						if (newInfo.equalsIgnoreCase("")) {
							System.out.println("Cannot have a blank name.");
						} else {
							String s_updateName = "Update user set name = \'" + newInfo + "\' where ID = " + userID;
							stmt.execute(s_updateName);
						}
						break;
					case 2:

						int input;

						try {

							System.out.println("Enter your new PIN (must be non-negative)");
							newInfo = reader.nextLine();
							input = Integer.parseInt(newInfo);

							while (input < 0) {
								System.out.println("Enter a non-negative number.");
								newInfo = reader.nextLine();
								input = Integer.parseInt(newInfo);
							}

							String s_updatePIN = "Update user set PIN = " + input + " where ID = " + userID;
							stmt.execute(s_updatePIN);


						} catch (NumberFormatException e) {
							System.out.println("Enter a number.");
						} catch (SQLException e) {
						    // Not sure how this would happen
							System.out.println("Cannot edit personal information");
						}

						break;
					default:
						return;


				}
			}

		} catch (SQLException e) {
			System.out.println("Cannot edit personal information");
		}

	}

	/***
	 * Shows the name and IDs of all overdue books in the system
	 */
	public void viewOverdueBooks(){

		String s_createView = "Create Or Replace view overdue_books "
				+ " as select title, item.deweyid, item.itemnumber "
				+ "From checkout inner join item on item.deweyid = checkout.deweyid and item.itemnumber = checkout.itemnumber"
				+       " Where endDate is null and dueDate < curDate()" ;
		String s_queryView = "Select * From overdue_books";


		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(s_createView);
			ResultSet res = stmt.executeQuery(s_queryView);
			boolean itemFound = false;

			while(res.next()){
				itemFound = true;
				System.out.println("\t" + res.getInt(2) + "\t" + res.getInt(3) + "\t"  + res.getString(1));
			}
			if(!itemFound){
				System.out.println("Sorry, nothing was found. Please try again.");
			}


		} catch (SQLException e) {
		    // Not sure how this would be caused?
			System.out.println("Cannot view over due books");
		}

	}

	/***
	 * Shows the names and patron IDs of every user with overdue book infractions
	 *
	 */
	public void viewDelinquentPatrons(){


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
				System.out.println("\t" + res.getInt(2) + "\t" + res.getString(1));
			}

		} catch (SQLException e) {
		    //Not sure how this would occur
			System.out.println("Cannot view delinquent patrons");
		}
	}

	/***
	 * lists names and hours of every librarian that works part time
	 */
	public void findPartTimeLibrarians(){
		// Get the name and hours per week of part time Librarians
		String s_queryLibrarians = "Select name, hoursPerWeek from librarian inner join user on userid = id " +
				"" +
				"where hoursPerWeek <= 20";


		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryLibrarians);

			System.out.printf("%-20s %-20s\n", "name", "Hours Per Week");
			while(res.next()){
				System.out.printf("%-20s %-20s\n", res.getString(1), ""+res.getInt(2));
			}




		} catch (SQLException e) {
		    //Not sure how this would happen
			System.out.println("Cannot find part time librarians");
		}

	}

	/**
	 * PatronOptions
	 * How the patron interacts with the system
	 *
	 * @param patronID the patron currently login in userID
	 * @return true
	 */

	public boolean PatronOptions(int patronID) {

		int inputInt;
		System.out.println("Welcome to Team Pterosaur's Library System.");
		do {
			//reset the input int
			inputInt = 0;
			System.out.println("What would you like to do now?\n\n"
					+ "\t1. Search For Items\n"
					+ "\t2. Edit Personal Information\n"
					+ "\t3. Renew an Item\n"
					+ "\t4. Check Out Item\n"
					+ "\t5. Place Hold On Item\n"
					+ "\t6. Remove Hold on Item\n"
					+ "\t7. Return Item\n"
					+ "\t8. Quit");

			while (inputInt == 0) {
				System.out.print("Enter Your Response: ");
				try {
					inputInt = Integer.parseInt(reader.nextLine());
					if (inputInt == 0)
						inputInt = -1;
				} catch (NumberFormatException e) {
					System.out.println("Enter a number.");
				}
			}

			switch (inputInt) {

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
				case 7:
					returnItem(patronID);
					break;
				case 8:
					return false;
				default:
					System.out.println("Enter a valid choice.");
					inputInt = 1; // reset to 1 to keep the while loop going
					break;

			}
		} while (inputInt >= 1 && inputInt <= 8);

		return true;
	}

	/**
	 * PatronLogin
	 * How a person logins as a patron
	 *
	 * @return userID if user login correctly, -1 if the user cancels, 0 user failed
	 */
	public int PatronLogin() {
		System.out.print("Enter your ID (Enter -1 to cancel): ");
		int inputInt = 0;
		while (inputInt == 0) {
			try {
				inputInt = Integer.parseInt(reader.nextLine());

				if (inputInt <= 0 && inputInt != -1) {
					System.out.println("Enter a valid ID.");
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
						if (pin == -1)
							return -1;

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
						System.out.println("Enter a valid PIN");
					}

				} else
					System.out.println("Patron ID not found");


			} catch (SQLException e) {
				// Should probably do something more here
                // not sure how this would happen
                System.out.println(e.toString());
				System.out.println("Cannot login as a Patron");
			}


		}

		if (inputInt == -1 || correct)
			return inputInt;

		return 0;

	}

	/**
	 * searchForItems
	 * A user can search for items through search terms like
	 * author, director, year, genre, or deweyID or title
	 * Will output the search results
	 */
	public void searchForItems() {
		int inputInt = 0;
		boolean valid = false;

		// Added error checking for when the user doesn't enter a number
		do {
			System.out.println("Search by\n" +
					"\t1. Author\n" +
					"\t2. Director\n" +
					"\t3. Year\n" +
					"\t4. Genre\n" +
					"\t5. DeweyID\n" +
					"\t6. Title\n");

			System.out.print("Enter Your Response: ");

			while (!valid) {
				try {
					inputInt = Integer.parseInt(reader.nextLine());
					valid = true;
				} catch (NumberFormatException e) {
					System.out.println("Enter a number.");
				}

			}

			if (inputInt > 6 || inputInt < 1) {
				System.out.println("Enter a valid choice.");
				inputInt = 0;
			}

		} while (inputInt == 0);


		int substring = 0;
		do {
			System.out.println("Use Search Term as Substring?\n"
					+ "\t1. Yes\n"
					+ "\t2. No\n");

			System.out.print("Enter Your Response (Number): ");

			try {
				substring = Integer.parseInt(reader.nextLine());
				if (substring != 1 && substring != 2)
					System.out.println("Defaulting to No.");
			} catch (NumberFormatException e) {
				System.out.println("Enter a number.");
			}
		} while (substring == 0);

		boolean useSubstring = substring == 1;

		System.out.println("Enter your search term: ");
		String titleQuery = reader.nextLine();
		if (useSubstring)
			titleQuery = "%" + titleQuery + "%";

		String whereClause = "";
		String column = "";
		switch (inputInt) {
			case 1:
				whereClause = "natural join book where author like \'";
				column = ", author ";
				break;
			case 2:
				whereClause = "natural join DVD where director like \'";
				column = ", director ";
				break;
			case 3:
				whereClause = "where year like \'";
				column = ", year";
				break;
			case 4:
				whereClause = "where genre like \'";
				column = ", genre ";
				break;
			case 5:
				whereClause = "where deweyID like \'";
				break;
			case 6:
				whereClause = "where title like \'";
				break;
		}
		String s_queryItems = "Select item.deweyID, item.ItemNumber, title" + column + " from item " + whereClause + titleQuery +
				"\' group by item.deweyID";
		// group by will make sure the same books will stick together
		// it'd be really weird if Book A copy 1 and Book A copy 2 were far apart


		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryItems);

			int columnNumber;
			if (res.next() == false)
				System.out.println("No items found.");
			else {
				columnNumber = res.getMetaData().getColumnCount();
				//System.out.print(res.getRow() + ". \t" + res.getInt(1) + "\t" + res.getInt(2) + "\t" + res.getString(3));
				System.out.print(res.getRow() + ". \t");
				for (int i = 1; i <= columnNumber; i++) {
					System.out.print(res.getString(i) + "\t");
				}
				System.out.print("\n");

				while (res.next()) {
					//System.out.println(res.getRow() + ". \t" + res.getInt(1) + "\t" + res.getInt(2) + "\t" + res.getString(3));
					System.out.print(res.getRow() + ". \t");
					for (int i = 1; i <= columnNumber; i++) {
						System.out.print(res.getString(i) + "\t");
					}
					System.out.print("\n");
				}
			}

		} catch (SQLException e) {
			System.out.println("Cannot search for an item");
		}
	}

	/**
	 * renewItem
	 * A patron can renew an item they currently have checked out if it's not overdue or has a waiting list
	 *
	 * @param patronID
	 */
	public void renewItem(int patronID) {
		int inputInt = 0;
		int itemNumber = 0;
		try {
			Statement stmt;

			stmt = conn.createStatement();

			String s_queryItemOut = "Select title, item.deweyID, item.itemnumber, dueDate " +
					"from checkout natural join item where patronID = " + patronID +
					" and endDate is null";
			ResultSet res = stmt.executeQuery(s_queryItemOut);

            // Stop the user from renewing an item
            if (res.next() == false){
                System.out.println("You have no items currently checked out.");
                return;
            }

            do {
				System.out.println("Current Item Out: " + res.getString(1) + "\t" + res.getInt(2)
						+ "\t" + res.getInt(3) + "\tDue Date: " + res.getString(4));

			}while (res.next());


			do {
				System.out.println("Enter the dewey ID of the item you want to renew: ");

				try {
					inputInt = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a number.");
				}

				System.out.println("Enter the item number of the item you want to renew: ");

				try {
					itemNumber = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a number.");
				}

			} while (inputInt == 0 || itemNumber == 0);

			String s_queryFindItem = "Select deweyID, itemnumber from item where " +
					"deweyID = " + inputInt +
					" and itemnumber = " + itemNumber;

			String s_queryFindYourCheckOut = "Select endDate, dueDate from checkout where " +
					"deweyID = " + inputInt +
					" and itemnumber = " + itemNumber +
					" and patronID = " + patronID +
					" and endDate is null";

			String s_queryFindOtherHolds = "Select * from holds where " +
					"deweyID = " + inputInt +
					" and itemnumber = " + itemNumber +
					" and patronID != " + patronID + " and endDate is null";

			String s_queryUpdateCheckout = "Update checkout set dueDate = DATEADD(week,2,CURRENT_TIMESTAMP) " +
					"deweyID = " + inputInt +
					" and itemnumber = " + itemNumber +
					" and patronID = " + patronID + " and endDate is not null";


			res = stmt.executeQuery(s_queryFindItem);

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
					} else {
						// another user has a hold on this item
						System.out.println("This item has existing hold(s). You cannot renew it.");
					}
				}

			}

		} catch (SQLException e) {
			System.out.println("Cannot renew.");
		}


	}

	/**
	 * returnItem
	 * A patron can return an item they current have checked out
	 *
	 * @param patronID
	 */
	public void returnItem(int patronID) {

		int itemDeweyID = 0;
		int itemNumber = 0;

		try {
			Statement stmt;

			stmt = conn.createStatement();

			String s_queryItemOut = "Select title, item.deweyID, item.itemnumber, dueDate " +
					"from checkout natural join item where patronID = " + patronID +
					" and endDate is null";
			ResultSet res = stmt.executeQuery(s_queryItemOut);

			// Stop the user from returning an item


            System.out.println("Return an Item");

			do {
				System.out.println("Current Item Out: " + res.getString(1) + "\t" + res.getInt(2)
						+ "\t" + res.getInt(3) + "\tDue Date: " + res.getString(4));

			} while(res.next());

			// get valid input
			while (itemDeweyID == 0) {
				try {
					System.out.print("Please Enter Item Dewey ID (or -1 to cancel): ");
					itemDeweyID = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid number.");
				}

				if (itemDeweyID < 0 && itemDeweyID != -1) {
					System.out.println("Enter a valid number (valid Dewey ID or -1).");
					itemDeweyID = 0;
				}

			}

			// did the user quit
			if (itemDeweyID == -1) {
				System.out.println("Returning back to the menu.");
				return;
			}

			// get valid input
			while (itemNumber == 0) {
				try {
					System.out.print("Please Enter Item Number (or -1 to cancel): ");
					itemNumber = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid number.");
				}

				if (itemNumber < 0 && itemNumber != -1) {
					System.out.println("Enter a valid number (greater than zero or -1).");
					itemNumber = 0;
				}
			}

			// Did the user cancel
			if (itemNumber == -1) {
				System.out.println("Returning back to the menu.");
				return;
			}

			String s_queryFindACheckOut = "Select endDate, dueDate from checkout where " +
					"deweyID = " + itemDeweyID +
					" and itemnumber = " + itemNumber + " and patronID = " + patronID +
					" and endDate is null";

			String s_queryRemoveCheckout = "Update checkout set endDate = curDate() where " +
					"deweyID = " + itemDeweyID +
					" and itemnumber = " + itemNumber + " and patronID = " + patronID +
					" and endDate is null";

			String s_queryExists = "Select * from item where " +
					"deweyID = " + itemDeweyID +
					" and itemnumber = " + itemNumber;

			res = stmt.executeQuery(s_queryExists);

			if (res.next() == true) {
				// Is this currently checked out by the user
				res = stmt.executeQuery(s_queryFindACheckOut);
				if (res.next() == true) {
					// End the checkout
					stmt.executeUpdate(s_queryRemoveCheckout);
					System.out.println("Item returned.");

				} else
					System.out.println("This item is not currently checked out by you");

			} else
				System.out.println("This item doesn't exist.");

		} catch (SQLException e) {
			System.out.println("Cannot return this item.");
		}


	}

	/**
	 * checkOutItem
	 * A patron can check out an item if all these conditions are met
	 * 1. Item is not currently checked out
	 * 2. Item has either no waiting list or Patron's hold is number one on the waitlist
	 * 3. Patron has not checked out max number of books
	 *
	 * @param patronID
	 */
	public void checkOutItem(int patronID) {
		try {
			String s_queryItemLimit = "Select itemLimit from patron where userID = " + patronID;
			Statement stmt;
			int numberOfItems = 0;


			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryItemLimit);
			int itemLimit = -1;
			if(res.next())
				itemLimit = res.getInt(1);

			if (itemLimit == 0)
				System.out.println("You have an unlimited checkout limit.");
			else if (itemLimit > 0)
				System.out.println("Your item limit is " + itemLimit);
			else {
				System.out.println("You can't check out items");
				return;
				// This should never happen
			}


			int itemDeweyID = 0;
			int itemNumber = 0;

			// get valid input
			while (itemDeweyID == 0) {
				try {
					System.out.print("Please Enter Item Dewey ID (or -1 to cancel): ");
					itemDeweyID = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid number.");
				}

				if (itemDeweyID < 0 && itemDeweyID != -1) {
					System.out.println("Enter a valid number (valid Dewey ID or -1).");
					itemDeweyID = 0;
				}

			}

			// did the user quit
			if (itemDeweyID == -1) {
				System.out.println("Returning back to the menu.");
				return;
			}

			// get valid input
			while (itemNumber == 0) {
				try {
					System.out.print("Please Enter Item Number (or -1 to cancel): ");
					itemNumber = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid number.");
				}

				if (itemNumber < 0 && itemNumber != -1) {
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
					" and patronID != " + patronID + //Not sure if patronId check is neccessary
					" and endDate is null" +
					" order by position asc";

			String s_queryUpdateCheckout = "Insert into checkout " +
					"(PATRONID, DEWEYID, ITEMNUMBER, BEGINNINGDATE, ENDDATE, DUEDATE) " +
					"values (" + patronID + ", " + itemDeweyID + ", " + itemNumber + ", " +
					"curDate(), null, DATEADD(week ,2 ,CURRENT_TIMESTAMP))";
			String s_queryChangeHolds = "Update holds set position = position - 1 where " +
					"deweyID = " + itemDeweyID +
					" and itemnumber = " + itemNumber +
					" and endDate is null";
			String s_queryRemoveHold = "Update holds set endDate = curDate() where " +
					"deweyID = " + itemDeweyID +
					" and itemnumber = " + itemNumber +
					" and patronID = " + patronID +
					" and endDate is null";




			stmt = conn.createStatement();
			res = stmt.executeQuery(s_queryExists);

			// Does the item exist?
			if (res.next() == true) {
				res = stmt.executeQuery(s_queryFindACheckOut);

				// Is this item currently checked out?
				if (res.next() == false) {

					//How many items do you have out?
					res = stmt.executeQuery(s_queryFindNumberItems);
					if (res.next())
						numberOfItems = res.getInt(1);



					// Is the number of Items out < limit or limit == 0?
					if ((numberOfItems < itemLimit) || itemLimit == 0) {
						res = stmt.executeQuery(s_queryFindOtherHolds);
						if (res.next() == false) {
							stmt.execute(s_queryUpdateCheckout);
							System.out.println("Item checked out");
						}
						else {
							// check that the first hold is the patron
							if (res.getInt(2) == patronID) {
								stmt.execute(s_queryUpdateCheckout); // Check out book
								stmt.execute(s_queryRemoveHold); // End the current hold
								stmt.execute(s_queryChangeHolds); // Change other positions
								System.out.println("Item checked out");
							} else {
								System.out.println("Someone else has a hold on this book.");
							}
						}
					} else
						System.out.println("You are at the limit of items checked out.");


				} else
					System.out.println("This item is currently checked out.");
			} else
				System.out.println("This item doesn't exist.");

		} catch (SQLException e) {
			// This happens when the SQL fails, which means the user tried to checkout a book more than once per day.
			System.out.println("You have already checked out this item already today.");
			System.out.println("There is a limit of one check out per book per day.");
		}


	}

	/**
	 * placeHoldOnItem
	 * A patron can place a hold on an item
	 *
	 * @param patronID - patron's user id
	 */

	public void placeHoldOnItem(int patronID) {
		int itemDeweyID = 0;
		int itemNumber = 0;

		// get valid input
		while (itemDeweyID == 0) {
			try {
				System.out.print("Please Enter Item Dewey ID (or -1 to cancel):");
				itemDeweyID = Integer.parseInt(reader.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Enter a valid number.");
			}

			if (itemDeweyID < 0 && itemDeweyID != -1) {
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
		while (itemNumber == 0) {
			try {
				System.out.print("Please Enter Item Number (or -1 to cancel):");
				itemNumber = Integer.parseInt(reader.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Enter a valid number.");
			}

			if (itemNumber < 0 && itemNumber != -1) {
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

		String s_count = "Select count(patronID) from holds where " +
				"deweyID = " + itemDeweyID +
				" and itemnumber = " + itemNumber +
				" and endDate is null";
		int count = 1;

		Statement stmt;

		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryExists);

			// Does this item exist?
			if (res.next() == true) {
				res = stmt.executeQuery(s_queryFindYourHold);
				// Does the patron already have a hold on this item?

				if (res.next() == false) {
					// Create a new hold for this item
					res = stmt.executeQuery(s_count);
					if (res.next() == true)
						count = res.getInt(1) + 1;

					String s_queryAddHold = "Insert into holds " +
							"(PATRONID, DEWEYID, ITEMNUMBER, ENDDATE, POSITION, CREATEDATE)" +
							" values (" + patronID + ", " + itemDeweyID + ", " + itemNumber + ", " +
							" null, " + count + ", curDate())";
					stmt.execute(s_queryAddHold);
					System.out.println("You have a placed a hold on this item");
				} else
					System.out.println("You already have a hold on this item.");
			} else
				System.out.println("This item doesn't exist.");

		} catch (SQLException e) {
			System.out.println("You can only create one hold per item per day.");
		}


	}

	/**
	 * RemoveHoldonItem
	 * A patron can remove their hold on an item
	 *
	 * @param patronID patron's user id
	 */
	public void removeHoldOnItem(int patronID) {
		int itemDeweyID = 0;
		int itemNumber = 0;

		Statement stmt;

		// Give the patron the list of their currently active holds
		try {
			stmt = conn.createStatement();
			String s_queryActiveHolds = "Select title, item.deweyID, item.itemnumber, position " +
					"from holds natural join item where patronID = " + patronID +
					" and (endDate > curDate() or endDate is null)";
			ResultSet res = stmt.executeQuery(s_queryActiveHolds);

			// If you have no holds, then you can't remove an holds
            if (res.next() == false){
                System.out.println("You have no active holds.");
                return;
            }

			do {
				System.out.println("Currently Active Hold: " + res.getString(1) + "\t" + res.getInt(2)
						+ "\t" + res.getInt(3) + "\tPosition: " + res.getInt(4));

			} while (res.next());


			// get valid input
			while (itemDeweyID == 0) {
				try {
					System.out.print("Please Enter Item Dewey ID (or -1 to cancel):");
					itemDeweyID = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid number.");
				}

				if (itemDeweyID < 0 && itemDeweyID != -1) {
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
			while (itemNumber == 0) {
				try {
					System.out.print("Please Enter Item Number (or -1 to cancel): ");
					itemNumber = Integer.parseInt(reader.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid number.");
				}

				if (itemNumber < 0 && itemNumber != -1) {
					System.out.println("Enter a valid number (greater than zero or -1).");
					itemNumber = 0;
				}
			}

			// Did the user cancel
			if (itemNumber == -1) {
				System.out.println("Returning back to the menu.");
				return;
			}

			String s_queryHoldExists = "Select * from holds where patronID = " + patronID + " and DeweyID = " + itemDeweyID
					+ " and itemNumber = " + itemNumber + " and (endDate > curDate() or endDate is null)";

			String s_queryHoldEnd = "update holds set endDate = curDate() where patronID = " + patronID + " and DeweyID = " + itemDeweyID
					+ " and itemNumber = " + itemNumber + " and (endDate > curDate() or endDate is null)";

			String s_queryHoldUpdate = "UPDATE holds SET position = position - 1 WHERE DeweyID = " + itemDeweyID + " AND itemNumber = " +
			itemNumber + " (endDate > curDate() or endDate is null) and position > (Select position from holds where " +
					"patronID = " + patronID + " DeweyID = " + itemDeweyID + " and itemNumber = " + itemNumber +
					" and (endDate > curDate() or endDate is null))";

			res = stmt.executeQuery(s_queryHoldExists);

			if (res.next() == true) {
				stmt.execute(s_queryHoldUpdate);
				stmt.execute(s_queryHoldEnd);

				System.out.println("Updated hold.");
			} else {
				System.out.println("Not a valid hold");
			}


		} catch (SQLException e) {
			System.out.println("Cannot update hold.");
		}


	}

}
