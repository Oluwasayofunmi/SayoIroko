

	import java.sql.Connection;
	import java.sql.Date;
	import java.sql.DriverManager;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.util.Properties;
import java.util.Scanner;

//import from JSch (Java Secure Channel) jar file - http://www.jcraft.com/jsch/ 
	import com.jcraft.jsch.JSch;
	import com.jcraft.jsch.JSchException;
	import com.jcraft.jsch.Session;


	public class jdbcConnect {
		public  Session session;						// SSH tunnel session
		Connection connection;
		
		/**
		 * Open SSH Tunnel to SSH server and forward the specified port on the local machine to the MySQL port on the MySQL server on the SSH server
		 * @param sshUser SSH username
		 * @param sshPassword SSH password
		 * @param sshHost hostname or IP of SSH server
		 * @param sshPort SSH port on SSH server
		 * @param remoteHost hostname or IP of MySQL server on SSH server (from the perspective of the SSH Server)
		 * @param localPort port on the local machine to be forwarded
		 * @param remotePort MySQL port on remoteHost 
		 */
		private void openSSHTunnel( String sshUser, String sshPassword, String sshHost, int sshPort, String remoteHost, int localPort, int remotePort ){
			try{
				final JSch jsch = new JSch();							// create a new Java Secure Channel
				session = jsch.getSession( sshUser, sshHost, sshPort);	// get the tunnel
				session.setPassword(sshPassword );						// set the password for the tunnel

				final Properties config = new Properties();				// create a properties object 
				config.put( "StrictHostKeyChecking", "no" );			// set some properties
				session.setConfig( config );							// set the properties object to the tunnel

				session.connect();										// open the tunnel
				System.out.println("\nSSH Connecting ***********************************************************************************************************************");
				System.out.println("Success: SSH tunnel open - you are connecting to "+sshHost+ "on port "+sshPort+ " with username " + sshUser);

				// set up port forwarding from a port on your local machine to a port on the MySQL server on the SSH server
				session.setPortForwardingL(localPort, remoteHost, remotePort);							
				// output a list of the ports being forwarded 
				
				System.out.println("Success: Port forwarded - You have forwared port "+ localPort + " on the local machine to port " + remotePort + " on " + remoteHost + " on " +sshHost);
			}
			catch(Exception e ){
				e.printStackTrace();
			}
		}
		
		/**
		 * Close SSH tunnel to a remote server
		 */
		private void closeSshTunnel(int localPort){
			try {
				// remove the port forwarding and output a status message
				System.out.println("\nSSH Connection Closing ******************************************************************************************************************");
				session.delPortForwardingL(localPort);
				System.out.println("Success: Port forwarding removed");
				// catch any exceptions	
			} catch (JSchException e) {
				System.out.println("Error: port forwarding removal issue");
				e.printStackTrace();
			}
			// disconnect the SSH tunnel
			session.disconnect();
			System.out.println("Success: SSH tunnel closed\n");
		}

		/**
		 * Open a connection with MySQL server. If there is an SSH Tunnel required it will open this too. 
		 */
		public void openConnection(String mysqlHost, int localPort, String mysqlDatabaseName, String mysqlUsername, String mysqlPassword){
			try{
				// create a new JDBC driver to facilitate the conversion of MySQL to java and vice versa
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

				// connect to the MySQL database through the SSH tunnel you have created using the variable above
				String jdbcConnectionString = "jdbc:mysql://"+mysqlHost+":"+localPort+"/"+mysqlDatabaseName+"?user="+mysqlUsername+"&password="+mysqlPassword;
				System.out.println("\nMySQL Connecting *********************************************************************************************************************");
				System.out.println("JDBC connection string "+jdbcConnectionString);
				connection = DriverManager.getConnection(jdbcConnectionString);
				System.out.println("Connection:"+connection.toString());
				System.out.println("Success: MySQL connection open");

				// testing connection 
				//testConnection();

			}
			// catch various exceptions and print error messages
			catch (SQLException e){ 
				System.err.println("> SQLException: " + e.getMessage());
				e.printStackTrace();
			}
			catch (InstantiationException e) {
				System.err.println("> InstantiationException: " + e.getMessage());
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				System.err.println("> IllegalAccessException: " + e.getMessage());
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				System.err.println("> ClassNotFoundException: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		public void closeConnection(){
			System.out.println("\nMySQL Connection Closing ****************************************************************************************************************");
			try {
				connection.close(); // close database connection
				System.out.println("Success: MySQL connection closed.");
			} catch (SQLException e) {
				System.out.println("Error: Could not close MySQL connection");
				System.err.println(e);	
				e.printStackTrace();}
		}
		
		/**
		 * Test the connection by printing out everything in the Customer table.
		 * @return 
		 */
		
		public void Customers()
		{
		
			try {
				Statement st = connection.createStatement(); 							// create an SQL statement
				ResultSet rs = st.executeQuery("SELECT * from Customer");  // retrieve an SQL results set

				// output the results set to the user
				System.out.println("View All Customer");

				while (rs.next()){
					int CustomerID = rs.getInt("CustomerID");
					String Name = rs.getString("Name");
					String City = rs.getString("City");
					String Street = rs.getString("Street");
					String Address = rs.getString("Address");
					
					System.out.print(CustomerID + " ");
					System.out.print(Name + " ");
					System.out.print(City + " ");
					System.out.print(Street + " ");
					System.out.print(Address + " \n");

				}
				
				if (st != null) {
					st.close();		//close the SQL statement
				}
				if (rs != null){	//close the Result Set
					rs.close();
				}


			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public void Film()
		{
			
				
				try {
					Statement st = connection.createStatement(); 							// create an SQL statement
					ResultSet rs = st.executeQuery("SELECT * from Film");  // retrieve an SQL results set

					// output the results set to the user
					System.out.println("ALL Film details");

					while (rs.next()){
						int FilmID = rs.getInt("FilmID");
						String Title = rs.getString("Title");
						String RentalPrice = rs.getString("RentalPrice");
						String Kind = rs.getString("Kind");
						
						
						System.out.print(FilmID + " ");
						System.out.print(Title + " ");
						System.out.print(RentalPrice + " ");
						System.out.print(Kind + " \n");
					}
					
					if (st != null) {
						st.close();		//close the SQL statement
					}
					if (rs != null){	//close the Result Set
						rs.close();
					}


				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		
		public void Reserverd()
		{
			
				
				try {
					Statement st = connection.createStatement(); 							// create an SQL statement
					ResultSet rs = st.executeQuery("SELECT * from Film");  // retrieve an SQL results set

					// output the results set to the user
					System.out.println("ALL Reserved Films");

					
					
					
					while (rs.next()){
						int CustomerID = rs.getInt("CustomerID");
						String FilmID = rs.getString("FilmID");
						
						
						
						System.out.print(CustomerID + " ");
						System.out.print(FilmID + " \n ");
						
						
					}
					
					if (st != null) {
						st.close();		//close the SQL statement
					}
					if (rs != null){	//close the Result Set
						rs.close();
					}


				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		
		
//		public void testConnection()
//		{
//			try {
//				Statement st = connection.createStatement(); 							// create an SQL statement
//				ResultSet rs = st.executeQuery("SELECT * from Customer");  // retrieve an SQL results set
//
//				// output the results set to the user
//				System.out.println("\nTesting connection by printing out Customer details");
//
//				while (rs.next()){
//					int CustomerID = rs.getInt("CustomerID");
//					String Name = rs.getString("Name");
//					String City = rs.getString("City");
//					String Street = rs.getString("Street");
//					String Address = rs.getString("Address");
//					
//					System.out.print(CustomerID + " ");
//					System.out.print(Name + " ");
//					System.out.print(City + " ");
//					System.out.print(Street + " ");
//					System.out.print(Address + " \n");
//
//				}
//				
//				if (st != null) {
//					st.close();		//close the SQL statement
//				}
//				if (rs != null){	//close the Result Set
//					rs.close();
//				}
//
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		
		
		public void Make_Rersavation() {
			
			try {
				Scanner userInput = new Scanner (System.in);
				Statement st = connection.createStatement();
				
				 Customers();
				
				
				
				 System.out.println("  ");
				 
				System.out.println("Please Enter Customer Details");
				int Cus_ID = userInput.nextInt();
				
				
				
				System.out.println("  ");
				Film();
				
				System.out.println("  ");
				System.out.println(" Enter Film ID");
				int Film_ID = userInput.nextInt();
				
				
				String st1= "INSERT INTO Reserved (CustomerID, FilmID ) VALUES(" + Cus_ID + ",'"+ Film_ID + "')";
				
				 st.executeUpdate(st1);
		
				
		
				
			} catch (SQLException e) {
			
				System.out.println("Film has been booked");
		
				
				
			} 	
		
			
			
		}
		
		
//		
		
			public  void ViewAReservation() {
		
			try {
			
			
			Statement st = connection.createStatement(); 
			
			Scanner myInput= new Scanner (System.in);
			
				
			
			
			 ResultSet rs = st.executeQuery("SELECT * from Film, Reserved;");  // retrieve an SQL results set

	
			
			Customers();
			
			System.out.println("  ");
			System.out.println("Please Input Customer ID");
			int cusid = myInput.nextInt();
			
			rs=	st.executeQuery("select * FROM Reserved JOIN Film ON Reserved.FilmID = Film.FilmID WHERE CustomerID = " + cusid);
		
			

			
			while (rs.next()){
				int FilmID = rs.getInt("FilmID");
				String Title = rs.getString("Title");
				String RentalPrice = rs.getString("RentalPrice");
				String Kind = rs.getString("Kind");
				
				
				System.out.print(FilmID + " ");
				System.out.print(Title + " ");
				System.out.print(RentalPrice + " ");
				System.out.print(Kind + " \n");
				
			}
			while (rs.next()){
				int CustomerID = rs.getInt("CustomerID");
				String FilmID = rs.getString("FilmID");
				
				
				
				System.out.print(CustomerID + " ");
				System.out.print(FilmID + " \n ");
				
				
			}
			
			
			if (st != null) {
				st.close();		//close the SQL statement
			}
			if (rs != null){	//close the Result Set
				rs.close();
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
			
			

public  void showStatistics() {
	
	
try {
		
		
		Statement st = connection.createStatement(); 
		
		
			
		
		
		 ResultSet rs = st.executeQuery("SELECT * from Customer;");  // retrieve an SQL results set

		 System.out.println("*****Top 5 Customer*********");
		rs=	st.executeQuery("SELECT Name, CustomerID, City  FROM " + 
				"(" + 
				"  SELECT Name, CustomerID, City " + 
				"  FROM   Customer" + 
				"  WHERE (City = 'Dublin')" + 
				"  ORDER BY  City DESC" + 
				"  Limit 5" + 
				") Top5_Customers\n" + 
				"ORDER BY CustomerID, City DESC;" + 
				" \n" );
	

		while (rs.next()){
			int CustomerID = rs.getInt("CustomerID");
			String Name = rs.getString("Name");
			String City = rs.getString("City");
			//String Street = rs.getString("Street");
			//String Address = rs.getString("Address");
			
			System.out.print(CustomerID + " ");
			System.out.print(Name + " ");
			System.out.print(City + " \n");
			//System.out.print(Street + " ");
			//System.out.print(Address + " \n");

		}
		
	
		
		
		if (st != null) {
			st.close();		//close the SQL statement
		}
		if (rs != null){	//close the Result Set
			rs.close();
		}
		


	} catch (SQLException e) {
		e.printStackTrace();
	}
}


public  void Statistics1() {
	
	
try {
		
		
		Statement st = connection.createStatement(); 
		
		
			
		
		
		 ResultSet rs = st.executeQuery("SELECT * from Film;");  // retrieve an SQL results set

		 System.out.println("*****Sum of Total Rent *********");
		rs=	st.executeQuery("SELECT  Sum(RentalPrice) TotalCost_Of_MovieRented\n" + 
				"				FROM Film;");
	

		while (rs.next()){
			//int FilmID = rs.getInt("FilmID");
			//String Title = rs.getString("Title");
			String RentalPrice = rs.getString("TotalCost_Of_MovieRented");
			//String Kind = rs.getString("Kind");
			
			
			//System.out.print(FilmID + " ");
			//System.out.print(Title + " ");
			System.out.print(RentalPrice + "\n ");
			//System.out.print(Kind + " \n");
		}
	
		
		
		if (st != null) {
			st.close();		//close the SQL statement
		}
		if (rs != null){	//close the Result Set
			rs.close();
		}
		


	} catch (SQLException e) {
		e.printStackTrace();
	}
}

public  void Statistics2() {
	
	
try {
		
		
		Statement st = connection.createStatement(); 
		
		
			
		
		
		 ResultSet rs = st.executeQuery("SELECT * from Film;");  // retrieve an SQL results set

		 System.out.println("*****Average Rental *********");
		rs=	st.executeQuery("SELECT Title, AVG(RentalPrice) AS Average_Rental\n" + 
				"FROM   Film\n" + 
				"GROUP BY Title\n" + 
				"ORDER BY Title;");
	

		while (rs.next()){
			//int FilmID = rs.getInt("FilmID");
			String Title = rs.getString("Title");
			String RentalPrice = rs.getString("Average_Rental");
			//String Kind = rs.getString("Kind");
			
			
			//System.out.print(FilmID + " ");
			System.out.print(Title + " ");
			System.out.print(RentalPrice + "\n ");
			//System.out.print(Kind + " \n");
		}
	
		
		
		if (st != null) {
			st.close();		//close the SQL statement
		}
		if (rs != null){	//close the Result Set
			rs.close();
		}
		


	} catch (SQLException e) {
		e.printStackTrace();
	}
}

public  void Statistics3() {
	
	
try {
		
		
		Statement st = connection.createStatement(); 
		
		
			
		
		
		 ResultSet rs = st.executeQuery("SELECT * from Customer;");  // retrieve an SQL results set

		 System.out.println("*****Total Occurence*********");
		rs=	st.executeQuery("Select Count(*) AS Total_Occurence, CustomerID, Name \n" + 
				"From Customer\n" + 
				"GROUP BY CustomerID; ");
	

		while (rs.next()){
			int CustomerID = rs.getInt("CustomerID");
			String Name = rs.getString("Name");
			int Count = rs.getInt("Total_Occurence");
			
			System.out.print(CustomerID + " ");
			System.out.print(Name + " \n");
			

		}
	
		
		
		if (st != null) {
			st.close();		//close the SQL statement
		}
		if (rs != null){	//close the Result Set
			rs.close();
		}
		


	} catch (SQLException e) {
		e.printStackTrace();
	}
}

			
			
			
			public void SomeStatistics() {
				
				try {
					Statement st = connection.createStatement();
					
					 ResultSet rs = st.executeQuery("SELECT * from Reserved,Film"); 
					 		
					System.out.println("Most Popular Movie");
					 rs= st.executeQuery("select Title,Count(*) as Num, Film.FilmID  FROM Reserved, Film Where Reserved.FilmID = Film.FilmID group by Title, Film.FilmID;" );
										
					
					
				while (rs.next()){
				int FilmID = rs.getInt("FilmID");
					String Title = rs.getString("Title");
						int Count = rs.getInt("Num");
					
					System.out.print(FilmID + " ");
						System.out.print(Title + " ");
						System.out.print(  Count+" \n ");
						
				}
					
					while (rs.next()){
						//int CustomerID = rs.getInt("CustomerID");
						String FilmID = rs.getString("FilmID");
						int Count = rs.getInt("Num");
						
						
						
						
						//System.out.print(CustomerID + " ");
						System.out.print(Count + " ");
						System.out.print(FilmID + " \n ");
						
						
						
					}
					
					
					
					System.out.println("");
					Statistics3();
					System.out.println("");
					Statistics1();
					System.out.println("");
					Statistics2();
					System.out.println("");
										
					

						if (st != null) {
							st.close();		//close the SQL statement
						}
						if (rs != null){	//close the Result Set
							rs.close();
						}
						
						
					
				
			}
					
					catch (SQLException e) {
						e.printStackTrace();
					}
			}
	

		public void RegCustomer() {
			
			try {
				Statement st = connection.createStatement();
				
				
				Scanner userInput = new Scanner (System.in);
				
						
				System.out.print("Please Enter Name:");
				String Name = userInput.nextLine();
				
				System.out.print("Please Enter City:");
				String City = userInput.nextLine();
				
				System.out.print("Please Enter Street:");
				String Street = userInput.nextLine();
				
				System.out.print("Please Enter Address:");
				String Address = userInput.nextLine();
				String string1= "INSERT INTO Customer ( Name, City, Street, Address ) VALUES('"+ Name + "','" +City + "','"+ Street + "','"+ Address + "')";
				
				System.out.println(string1);
				 st.executeUpdate(string1);
		
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
		
			
			
		}
		
		public static void main(String[] args) {
			System.out.println("Starting");
			String mysqlUsername = "s2957559";
			String mysqlPassword = "ndinglyw";
			String mysqlDatabaseName = "test";
			String sshUsername = "s2957559";
			String sshPassword = "ndinglyw";
			String sshRemoteHost = "knuth.gcd.ie";
			int shhRemotePort = 22;                     
			int localPort = 3310;           	
			String mysqlHost="localhost"; 
			int remoteMySQLPort = 3306;
			
			jdbcConnect con = new jdbcConnect();
			
			con.openSSHTunnel(sshUsername, sshPassword, sshRemoteHost, shhRemotePort, mysqlHost, localPort, remoteMySQLPort);
			con.openConnection(mysqlHost, localPort, mysqlDatabaseName, mysqlUsername, mysqlPassword);

			Scanner input = new Scanner(System.in);
			int response;
			
		
			do {
				
		    System.out.println(" Enter 1 to Register a new Customer");
			System.out.println(" Enter 2 a View a Reservation");
			System.out.println(" Enter 3 to Make a Reservation");
			System.out.println(" Enter 4 Show some  Statistics");
			System.out.println(" Enter 5 To Exit ");
			
			int number = input.nextInt();
			
			switch(number){
			case 1:
				System.out.println("Register New Customer");
				con.RegCustomer();

					break;
					
			case 2: 
				System.out.println(" View Reservations");
				
				con.ViewAReservation();
				
				break;
				
			case 3: 
				System.out.println("Make a Reservation");
				
				con.Make_Rersavation();
				break;
				
			case 4: 
				System.out.println("Show Some Statistics");
				con.SomeStatistics();
			
				break;
			 
			case 5:
				System.out.println("Exited Program");
				break;
				default:
					System.out.println("Selection is invalid"); 
			
			
			} 
			System.out.println("Input A to Make a Selection Again");
			response= input.next().charAt(0);
		}
		while(response != 'x' && response != 'X');
			System.out.println("Seee you next time");

	
			
			con.closeConnection();
			con.closeSshTunnel(localPort);
		}

	}

