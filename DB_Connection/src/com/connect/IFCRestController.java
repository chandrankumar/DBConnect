
package com.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/db")
public class IFCRestController {

	/**
	 * This method used to establish the internal and external connectivity of data.
	 * source
	 * 
	 * @param msg
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	@GET
	@Path("/connect")
	public Response dbConnect(@QueryParam("conn") String connectionType) {
		String response = null;
		String query = "select * from student";
		System.out.println("connection DB....");
		 if(connectionType.equals("ext")) {
			 response = externalConnection(query); 
		 }else{
			 response = internalConnection(query); 
		 }
		return Response.status(200).entity(response).build();

	}

	/**
	 * This method used to connect the database using the JNDI look up.
	 * 
	 * @param query
	 * 
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	private String externalConnection(String query) {

		String name = null;
		try {
			System.out.println("connecting external config");
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/TestDB");
			Connection con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
				name = rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3);
			System.out.println(name);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return name;

	}
	

	/**
	 * This method used to connect the database using the Driver manager.
	 * 
	 * @return
	 */
	private String internalConnection(String query) {

		String name = null;
		try {
			System.out.println("connecting internal config");
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
				name = rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3);
			System.out.println(name);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return name;
	}
}
