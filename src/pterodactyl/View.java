package pterodactyl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class View {

	Scanner reader;
	Connection conn;
	public View(){
		reader = new Scanner(System.in);
		
		
		System.out.print("Log in as:\n"
				+ "\t1. Librarian\n"
				+ "\t2. Patron\n"
				+ "\t3. Quit\n");
		
		try {
			String url = "jdbc:h2:~/workspace\\PterodactylProject\\test";
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(url,
					"sa",
					"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int inputInt = reader.nextInt();
		
		switch(inputInt){
		case 1:
			LibrarianOptions();
			break;
		case 2:
			PatronOptions();
			break;
		default:
			System.out.println("Quitting");
					
		}
		
	}
	
	public boolean LibrarianOptions(){
		System.out.print("Welcome to Team Pterosaur's Library System. What would you like to do?\n\n"
				+ "\t1. Add Item\n"
				+ "\t2. Remove Item\n"
				+ "\t3. Clear Waitlists\n"
				+ "\t4. Edit Personal Information\n"
				+ "\t5. Find Overdue Books\n"
				+ "\t6. Find Delinquent Patrons\n"
				+ "\t7. Find Part Time Librarians");
		
		int inputInt = reader.nextInt();
		
		switch(inputInt){
		
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
			editPersonalInformation();
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
			System.out.println("Error: Not a valid number");
			return false;
		
		}
		
		return true;
	}
	
	
	public void addItem(){		
	}
	public void removeItem(){
		
	}
	public void clearWaitlists(){
		
	}
	public void editPersonalInformation(){
	
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
		/*
		 * we probably want their names too right? we'll need to 
		 * do a similar merge in this one
		 */
		String s_createView = "Create Or Replace view overdue_books "
				+ "As select patronID "
				+ "From checkout "
				+ "Where endDate is null and dueDate < curDate()" ;
		String s_queryView = "Select * From overdue_books";
		
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(s_createView);
			ResultSet res = stmt.executeQuery(s_queryView);
			
			while(res.next()){
				System.out.println("\t" + res.getInt(1));
			}
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void findPartTimeLibrarians(){
		String s_queryLibrarians = "Select userid,hoursPerWeek from librarian where hoursPerWeek <= 20";
		
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryLibrarians);
			
			while(res.next()){
				System.out.println("\t" + res.getInt(1) + "\t" + res.getInt(2));
			}
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	public void searchForItems(){
		System.out.println("Use Search Term as Substring?\n"
				+ "\t1. Yes\n"
				+ "\t2. No\n");
		boolean useSubstring = reader.nextInt() == 1;
		System.out.println("Please Enter Search Term:\n");
		String titleQuery = reader.next();
		if(useSubstring)
			titleQuery = "%" + titleQuery + "%";

		
		String s_queryItems = "Select deweyID, ItemNumber, title from item where title like '" + titleQuery + "'";
		
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(s_queryItems);
			
			while(res.next()){
				System.out.println("\t" + res.getInt(1) + "\t" + res.getInt(2) + "\t" + res.getString(3));
			}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void renewItem(){
		
	}
	public void checkOutItem(){
		
	}
	public void placeHoldOnItem(){
		
	}
	public void removeHoldOnItem(){
		/*
		 * might be better(/necessary) to ask them for their userId when logging in
		 * Also, this one doesn't actually tell you if it worked or not. If you do one with an end date < today, it'll still say updated
		 * ALSO, APPARENTLY THE PATRONID AND DEWEYID GOT MESSED UP ON CREATION, THEY'RE THE SAME WHOOPS (we need to fix that)
		 */
		System.out.println("Please Enter User ID\n");
		int userID = reader.nextInt();
		System.out.println("Please Enter Item Dewey ID:\n");
		int itemDeweyID = reader.nextInt();
		System.out.println("Please Enter Item Number:\n");
		int itemNumber = reader.nextInt();

		
		String s_queryItems = "Update holds "
				+ "Set endDate = curDate() "
				+ "Where patronID = " + userID + " and DeweyID = " + itemDeweyID + " and itemNumber = " + itemNumber + " and (endDate > curDate() or endDate is null)";
		
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(s_queryItems);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		System.out.println("Updated.");
		
	}
	
	public boolean PatronOptions(){
		System.out.print("Welcome to Team Pterosaur's Library System. What would you like to do?\n\n"
				+ "\t1. Search For Items\n"
				+ "\t2. Edit Personal Information\n"
				+ "\t3. Renew an Item\n"
				+ "\t4. Check Out Item\n"
				+ "\t5. Place Hold On Item\n"
				+ "\t6. Remove Hold on Item");
		
		int inputInt = reader.nextInt(); 
		
		switch(inputInt){
		
		case 1:
			searchForItems();
			break;
		case 2:
			editPersonalInformation();
			break;
		case 3:
			renewItem();
			break;
		case 4:
			checkOutItem();
			break;
		case 5:
			placeHoldOnItem();
			break;
		case 6:
			removeHoldOnItem();
			break;
		default:
			System.out.println("Error: Not a valid number");
			return false;
		
		}
		
		return true;
	}
	
	
}

