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

import model.CashInvoiceModel;
import model.CutomerSummaryReport;
import model.SalesRepModel;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.zkoss.zul.Messagebox;

public class QuotationReminderForSalesRep implements Job   {
	
List<SalesRepModel> lstSalesRep=new ArrayList<SalesRepModel>();
	
	List<CutomerSummaryReport> customerSummaryReport=new ArrayList<CutomerSummaryReport>();
	
	ReminderData taskData=new ReminderData();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	List<CashInvoiceModel> listQuotation=new ArrayList<CashInvoiceModel>();
	
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
				    		
				    		listQuotation=new ArrayList<CashInvoiceModel>();
				    		
				    		ListIterator<CutomerSummaryReport> customerList = customerSummaryReport.listIterator();
						       
						    String custKeys="";
						       
						    while(customerList.hasNext())
						    {
						       CutomerSummaryReport customers=customerList.next();
					    	   custKeys=custKeys+customers.getCustKey()+",";
						       if(!customerList.hasNext())
						       {
						    	   custKeys=custKeys+customers.getCustKey();//get all custKey
						       }
						    					      
							}
						       
						    if(custKeys!=null && !custKeys.equalsIgnoreCase(""))
						    {
						       listQuotation=taskData.getQuotationForReminder(custKeys);
						    }
						       
						    if(listQuotation!=null && listQuotation.size()>0)
					    	{
						       	//sendQuoationReminder(model.getSalesRepAsEmployeeEmail(),model.getSalesRepName());
					    		System.out.println("Quotation list size ->"+listQuotation.size()+"---------Sales Rep Name and email-->"+model.getSalesRepName()+"-->"+model.getSalesRepAsEmployeeEmail());
					    	}
				    	  
				      }
				   				   
			  }
			  catch(Exception e)
			  {
				  StringWriter sw = new StringWriter();
				  e.printStackTrace(new PrintWriter(sw)); 
				  Messagebox.show(sw.toString());
				  logger.error(""+sw.toString()+"QuotationReminderForSalesRep Quarts-->execute ");
			  }
			
	  }
	  
	  
	  private void sendQuoationReminder(String email,String Name)
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
				String subject="Hinawi Quotation Reminder";
				StringBuffer result=null;
				result=new StringBuffer();
				
				result.append("<p>Dear&nbsp;"+Name+",</p> ");

				result.append("<p>This is just an automatic reminder &nbsp;for total no of <strong> "+listQuotation.size()+" </strong> quotation(Created and Approved) and thier amount under you.</p> ");

				result.append("<table align='left' border='0' cellpadding='20' cellspacing='3'> ");
				result.append("<caption><strong>Quotation Details</strong></caption> ");
				result.append("<thead> ");
				result.append("	<tr> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Quotation No</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Customer/Prospective Name</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Type</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Amount Expiry</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Txn Date</th> ");
				result.append("	</tr> ");
				result.append("</thead> ");
				result.append("<tbody> ");
				for(int i=0;i<listQuotation.size();i++)
				{
					
					result.append("	<tr> ");
					result.append("	<td>"+listQuotation.get(i).getRecNo()+"</td> ");
					result.append("	<td>"+listQuotation.get(i).getCustomerName()+"</td> ");
					if(listQuotation.get(i).getClientType().equalsIgnoreCase("P"))
					result.append("	<td><strong>Prospective</strong></td> ");
						else
					result.append("	<td><strong>Customer</strong></td> ");	
					result.append("	<td><strong>"+listQuotation.get(i).getAmount()+"</strong></td> ");
					result.append("	<td><strong>"+listQuotation.get(i).getInvoiceDateStr()+"</strong></td> ");
					result.append("	</tr> ");
				
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
				  logger.error(""+sw.toString()+"sendQuoationReminder Quarts-->SendEmail ");
		  }	
		}
			
}
