package common;

import home.MailClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import model.CutomerSummaryReport;
import model.SalesRepModel;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.zkoss.zul.Messagebox;

public class CustomerBalanceReminder implements Job  {
	
	
	List<SalesRepModel> lstSalesRep=new ArrayList<SalesRepModel>();
	
	List<CutomerSummaryReport> customerSummaryReport=new ArrayList<CutomerSummaryReport>();
	
	ReminderData taskData=new ReminderData();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	Date fromDate=null;
	Date toDate=null;
	
	Date d1 = null;
	Date d2 = null;

	  public void execute(JobExecutionContext context)
			     throws JobExecutionException {
			  try {
				  
				  lstSalesRep=taskData.getSalesRepNamesForReminder("Y");//get Sales All salesrep and there email
				  
				  ListIterator<SalesRepModel> salesRepIterator = lstSalesRep.listIterator();
				  
				  while(salesRepIterator.hasNext())
			      {
			    	    customerSummaryReport=new ArrayList<CutomerSummaryReport>();
			    		SalesRepModel model=salesRepIterator.next();
			    		customerSummaryReport= taskData.getCustomerBalanceForReminder(model.getQbListKey());
			    		if(customerSummaryReport!=null && customerSummaryReport.size()>0)
			    		{
			    			//sendClientEmailCustomerBalnce(model.getSalesRepAsEmployeeEmail(),model.getSalesRepName());
			    			System.out.println("customerSummaryReport list size ->"+customerSummaryReport.size()+"---------Sales Rep Name and email-->"+model.getSalesRepName()+"-->"+model.getSalesRepAsEmployeeEmail());
			    		}
			      }
				  
			  }
			  catch(Exception e)
			  {
				  StringWriter sw = new StringWriter();
				  e.printStackTrace(new PrintWriter(sw)); 
				  Messagebox.show(sw.toString());
				  logger.error(""+sw.toString()+"CustomerBalanceReminder execute Quarts-->execute ");
			  }
	  }
	  
	  
	  private void sendClientEmailCustomerBalnce(String email,String Name)
			{
				try
				{
					
					String[] to =null;
					String[] cc ={null};
					String[] bcc =null;
					
					String toMail=email;
					
					ArrayList fileArray = new ArrayList();
					
					to= toMail.split(",");	
			
					MailClient mc = new MailClient();
					String subject="Hinawi Customer Balance Reminder";
					StringBuffer result=null;
					result=new StringBuffer();
					
					result.append("<p>Dear&nbsp;"+Name+",</p> ");

					result.append("<p>This is just an automatic reminder &nbsp;for total no of <strong> "+customerSummaryReport.size()+" </strong> customers and thier balances under you.</p> ");

					result.append("<table align='left' border='0' cellpadding='20' cellspacing='3'> ");
					result.append("<caption><strong>Customer Balance Summary</strong></caption> ");
					result.append("<thead> ");
					result.append("	<tr> ");
					result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Customer Name</th> ");
					result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Balance</th> ");
					result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Contract Expiry</th> ");
					result.append("	</tr> ");
					result.append("</thead> ");
					result.append("<tbody> ");
					for(int i=0;i<customerSummaryReport.size();i++)
					{
						if(customerSummaryReport.get(i).getBalance()!=0)
						{
						result.append("	<tr> ");
						result.append("	<td>"+customerSummaryReport.get(i).getEnityName()+"</td> ");
						if(customerSummaryReport.get(i).getBalance()>0)
						result.append("	<td><strong>"+customerSummaryReport.get(i).getBalance()+"</strong></td> ");
						else
						result.append("	<td><strong><span style='color:#FF0000'>"+customerSummaryReport.get(i).getBalance()+"</span></strong></td> ");	
						result.append("	<td>"+customerSummaryReport.get(i).getCustContractExpiry()+"</td> ");
						result.append("	</tr> ");
						}
					
					}	
					
					result.append("</tbody> ");
					
					result.append("</table> ");
					
					result.append("<p>&nbsp;</p> ");
											
				
					result.append("<p><strong>Sincerely,</strong></p> ");

					result.append("<p><strong>Explorer Computer LLC Technical Support Team</strong></p> ");

					result.append("<p><strong><a href='http://www.hinawi.com/' title='blocked::http://www.hinawi.com/'>www.hinawi.com</a></strong></p> ");

			

					String messageBody=result.toString();			
				
					mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,fileArray,true,"Reminder","");
				}
				catch (Exception ex) {
					StringWriter sw = new StringWriter();
					  ex.printStackTrace(new PrintWriter(sw)); 
					  Messagebox.show(sw.toString());
					  logger.error(""+sw.toString()+"sendClientEmailCustomerBalnce Quarts-->SendEmail ");
			  }	
			}
		   

}
