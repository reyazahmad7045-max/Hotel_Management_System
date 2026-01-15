/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hms;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.awt.print.*;
public class Customer_bill extends JFrame implements ActionListener
{
    Container cp;
    PrinterJob pj;
    PageFormat pf;
    PrintPanel canvas;

    JPanel p,t;
    JLabel     lblempid;
    JComboBox  cmbemp_id;
    JButton btnShow,btnShowAll,btnPrint,btnPgSetup,btnClose;
    JTable table;
    Connection cn;
    ResultSet  rs,rst;
    ResultSetMetaData rsmd;
    Statement st,stt;
    int rc,cc,rc1,cc1;
    Object[][] cells;
    String rdt,wCOND="",columnNames[];
    public void connect()
    {
    	try{

        //DriverManager.registerDriver(new sun.jdbc.odbc.JdbcOdbcDriver());
        Class.forName("oracle.jdbc.driver.OracleDriver");
String url="jdbc:oracle:thin:@localhost:1521:xe";
Connection cn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "nasim", "khan");
		//cn=DriverManager.getConnection("jdbc:odbc:hotel","ashok","kumar");
		st=cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=st.executeQuery("select * from customer_bill_details");
		}catch(Exception e) { }

    }
    public void grand_total()
    {
        try
        {
            String query1="Select sum(cust_id) from customer_bill_details where cust_id=? group by cust_id";
            st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        rs = st.executeQuery(query1);
        rc1 = 0;
      while(rs.next())
               rc1++;
      rsmd=rs.getMetaData();
      cc1=rsmd.getColumnCount();
     }catch(Exception e) { }

    }
    public void openRecord()
    {
       try{
        String query = "SELECT * FROM customer_bill_details " + wCOND;
        st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        rs = st.executeQuery(query);
        rc = 0;
      while(rs.next())
               rc++;
      rsmd=rs.getMetaData();
      cc=rsmd.getColumnCount();
     }catch(Exception e) { }
    }
    public void setemp_code()
    {
        String query = "SELECT cust_id FROM customer_bill_details " ;
        try{
            stt = cn.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rst = stt.executeQuery(query);
            while(rst.next())
              cmbemp_id.addItem(rst.getString(1));
            rst.close();
            stt.close();
       }catch(Exception e) { }
    }
    public void setData()
    {
        int i,j,k,l;
        try
        {
            for(i=0;i<cc;i++)
              columnNames[i]=rsmd.getColumnName(i+1);

            i=0;
            rs.first();
            do{
              for(j=0;j<cc;j++)
                  cells[i][j]=rs.getString(j+1);
              i++;
            }while(rs.next());

        }catch (SQLException e){ }
    }
    public Customer_bill()
   {
      setBounds(10,10,780,560);
       setTitle("customer Bill Report ");

     cp = getContentPane();
     setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
     p=new JPanel();
     p.setLayout(new GridLayout(0,7));
     lblempid = new JLabel("customer Id :  ",SwingConstants.RIGHT);
     cmbemp_id = new JComboBox();
     btnShow       = new JButton("Show");      btnShow.setMnemonic('S');
     btnShowAll   = new JButton("Show All");  btnShow.setMnemonic('A');
     btnPrint     = new JButton("Print");     btnPrint.setMnemonic('P');
     btnPgSetup   = new JButton("Page setup");btnPgSetup.setMnemonic('G');
     btnClose         = new JButton("Close");     btnClose.setMnemonic('C');

     cmbemp_id.setEditable(false);
     btnShow.setActionCommand("Show");
     btnShowAll.setActionCommand("Show All");
     btnPrint.setActionCommand("Print");
     btnPgSetup.setActionCommand("Page Setup");
     btnClose.setActionCommand("Close");

     btnShow.addActionListener((ActionListener) this);
     btnShowAll.addActionListener(this);
     btnPrint.addActionListener(this);
     btnPgSetup.addActionListener(this);
     btnClose.addActionListener(this);

     btnShow.setForeground(Color.BLUE);
     btnShowAll.setForeground(Color.BLUE);
     btnPrint.setForeground(Color.BLUE);
     btnPgSetup.setForeground(Color.BLUE);
     btnClose.setForeground(Color.BLUE);
     lblempid.setForeground(Color.BLUE);
     cmbemp_id.setForeground(Color.BLUE);


     p.add(lblempid);
     p.add(cmbemp_id);
     p.add(btnShow);
     p.add(btnShowAll);
     p.add(btnPrint);
     p.add(btnPgSetup);
     p.add(btnClose);
     cp.add(p,"North");
     connect();
     openRecord();
     setemp_code();
     t=new JPanel();
     canvas = new PrintPanel();
     cp.add(canvas, "Center");

   }
    public void actionPerformed(ActionEvent e)
   {
      String cmd = e.getActionCommand();
      if(cmd.equals("Show"))
      {
        wCOND = " WHERE gid='" + cmbemp_id.getSelectedItem()+"'";
        showGridData();
      }
      if(cmd.equals("Show All"))
      {
        wCOND = "";
        showGridData();
      }
      if (cmd.equals("Print"))
      {
         pj = PrinterJob.getPrinterJob();
         if (pf == null)
            pf = pj.defaultPage();
         pj.setPrintable(canvas, pf);
         if (pj.printDialog())
         {  try
            {  pj.print();
            }
            catch (PrinterException exception)
            {  JOptionPane.showMessageDialog(this, exception);
            }
         }
      }

      if(cmd.equals("Page Setup"))
      {
         pj = PrinterJob.getPrinterJob();
         if (pf == null)
            pf = pj.defaultPage();
         pf = pj.pageDialog(pf);
      }

        if(cmd.equals("Close")){
           if (cn != null) {
               try{
                 cn.close();
               }catch(Exception zx) {}
           }
           setVisible(false);
           //System.exit(0);
        }
    }
    void showGridData()
  {
    openRecord();
    if(rc==0)
      JOptionPane.showMessageDialog(null,"Data not Found.","customer Bill Report",JOptionPane.INFORMATION_MESSAGE);
    else
    {
      columnNames=new String[cc];
      cells=new Object[rc][cc];
      setData();
      table = new JTable(cells, columnNames);
      remove(t);
      t=new JPanel();
      t.setBorder(BorderFactory.createTitledBorder("customer_bill_report"));
      t.setLayout(new BorderLayout());
      t.add(new JScrollPane(table));
      cp.add(t, "Center");
      setVisible(true);
    }
  }
   public static void main(String args[])
  {
    Customer_bill gb=new Customer_bill();
    gb.show();
  }
  class PrintPanel extends JPanel implements Printable
  {
        @Override
    public void paintComponent(Graphics g)
      { super.paintComponent(g);
      }

     public int print(Graphics g, PageFormat pf, int page) throws PrinterException
     {
        if (page >= 1) return Printable.NO_SUCH_PAGE;
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(Color.black);
        g2.translate(pf.getImageableX(), pf.getImageableY());
        g2.draw(new java.awt.geom.Rectangle2D.Double(0, 0,
        pf.getImageableWidth(), pf.getImageableHeight()));
        int r=2;
        try{
        rdt="";
        for(int i=0;i<cc;i++)
          rdt+=rsmd.getColumnName(1)+ "  ";
          g.drawString(rdt,10,4);
        rs.beforeFirst();
        while(rs.next())
        {
          rdt="";
          for(int i=0;i<cc;i++)
            rdt+=rs.getString(i+1)+ "  ";
          g.drawString(rdt,10,(++r)*20);
        }
       }catch (SQLException e){ }
        return Printable.PAGE_EXISTS;
     }
     public PrintPanel(){}
  }
}