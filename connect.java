/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package connection;
import java.sql.*;

/**
 *
 * @author Administrator
 */
public class connect {
  static Connection con=null;
public static Connection conn()
{
    try
    {
        /*Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        con=DriverManager.getConnection("jdbc:odbc:bca","msms","bca");*/
        Class.forName("oracle.jdbc.driver.OracleDriver");
String url="jdbc:oracle:thin:@localhost:1521:xe";
Connection cn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "nasim", "khan");
        System.out.println("connection created");
    }
    catch(ClassNotFoundException | SQLException e)
    {}
    return con;
}

}
