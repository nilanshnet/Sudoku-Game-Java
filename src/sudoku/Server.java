package sudoku;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



@SuppressWarnings("serial")
public class Server extends JFrame implements Runnable {
	  
	// Text area for displaying contents
	  private JTextArea ta;
	  
	  // db connection url string
	  
	  private String url = "jdbc:sqlite:javabook.db";
	  
	  // Number a client
	  private int clientNo = 0;
	  
	  public Server() {
		  ta = new JTextArea(10,10);
		  JScrollPane sp = new JScrollPane(ta);
		  this.add(sp);
		  this.setTitle("MultiThreadServer");
		  this.setSize(400,200);
		  Thread t = new Thread(this);
		  t.start();
	  }

	  public void run() {
		  try {
	        // Create a server socket
	        ServerSocket serverSocket = new ServerSocket(8000);
	        ta.append("MultiThreadServer started at " 
	          + new Date() + '\n');
	    
	        while (true) {
	          // Listen for a new connection request
	          Socket socket = serverSocket.accept();
	    
	          // Increment clientNo
	          clientNo++;
	          
	          ta.append("Starting thread for client " + clientNo +
	              " at " + new Date() + '\n');

	            // Find the client's host name, and IP address
	            InetAddress inetAddress = socket.getInetAddress();
	            ta.append("Client " + clientNo + "'s host name is "
	              + inetAddress.getHostName() + "\n");
	            ta.append("Client " + clientNo + "'s IP Address is "
	              + inetAddress.getHostAddress() + "\n");
	          
	          // Create and start a new thread for the connection
	          new Thread(new HandleAClient(socket, clientNo)).start();
	        }
	      }
	      catch(IOException ex) {
	        System.err.println(ex);
	      }
		    
	  }
	  
	  // Define the thread class for handling new connection
	  class HandleAClient implements Runnable {
	    private Socket socket; // A connected socket
	    private int clientNum;
	    private PreparedStatement queryStmt;
	    
	    /** Construct a thread */
	    public HandleAClient(Socket socket, int clientNum) {
	      this.socket = socket;
	      this.clientNum = clientNum;
	    }

	    /** Run a thread */
	    public void run() {
	      try {
	        // Create data input and output streams
	        ObjectInputStream inputFromClient = new ObjectInputStream(
	          socket.getInputStream());
	        ObjectOutputStream outputToClient = new ObjectOutputStream(
	          socket.getOutputStream());

	        // Continuously serve the client
	        while (true) {
	          
	        	// Send back to the client
	          int searchId;
	          int state = inputFromClient.readInt();
	          
	          try {
	        	Connection connect = DriverManager.getConnection(url);
	        	
	        	/// For Saving a game state to the database 
	        	if(state == 100) {
	        		System.out.println("Save State");
	        		
	        		// SQL Query for saving the state to the DB Table
	        		// 'Players' table has only two columns - Id of type integer and gamestate of type blob
	                queryStmt = connect.prepareStatement("INSERT INTO Players (gamestate) VALUES (?)");
	            	
	                queryStmt.setBytes(1, getBytes(inputFromClient.readObject())); // read the incoming object

	            	queryStmt.executeUpdate();
	            	queryStmt.close();
	            	ta.append("Game -"+this.clientNum+"\'  has been stored to the database"); 
	        	}
	        	
	        	
	        	// For Loading a saved game from the database 
	        	else if(state == 101){
	        		
	        		searchId = inputFromClient.readInt();

	        		queryStmt = connect.prepareStatement("SELECT * FROM Players WHERE id=?");
	        		queryStmt.setInt(1, searchId); 	
	        		ResultSet rset = queryStmt.executeQuery();
	        		while(rset.next()) {
	        			outputToClient.writeInt(rset.getInt("id"));
	        			outputToClient.writeObject(getObject(rset.getBytes("gamestate")));
	        			outputToClient.flush();
	        		}
	        		queryStmt.close();
	        		ta.append("Game "+this.clientNum+"\' restored"); 
	        	}
	          
	          }
	      
	          catch(IOException ex) {
	        	  ex.printStackTrace();
	          }
	    }
	  }
	      catch(Exception ex) {
	      	System.out.println("SERVER:C");
	          ex.printStackTrace();
	        }
	      }
	    }
	  
	  public static byte[] getBytes(Object obj) throws java.io.IOException {
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(bos);
		    oos.writeObject(obj);
		    oos.flush();
		    oos.close();
		    bos.close();
		    byte[] data = bos.toByteArray();
		    return data;
	  }
	  
	  public static Object getObject(byte[] bytearr) throws IOException, ClassNotFoundException{
		  ByteArrayInputStream bis = new ByteArrayInputStream(bytearr);
		  ObjectInputStream ois = new ObjectInputStream(bis);
		  return ois.readObject();
	  }
	  
	  
	  
	  public static void main(String[] args) {
	    Server new_s = new Server();
	    new_s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    new_s.setVisible(true);
	  }
	}
