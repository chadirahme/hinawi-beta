package common;

import home.MailClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import admin.TasksModel;

public class TaskMailReminder implements Job  {
	
	private List<TasksModel> lstTask=new ArrayList<TasksModel>();
	private List<TasksModel> lstAllTask=new ArrayList<TasksModel>();
	
	List<SalesRepModel> lstSalesRep=new ArrayList<SalesRepModel>();
	
	List<CutomerSummaryReport> customerSummaryReport=new ArrayList<CutomerSummaryReport>();
	
	List<CashInvoiceModel> listQuotation=new ArrayList<CashInvoiceModel>();
	
	ReminderData taskData=new ReminderData();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	Date fromDate=null;
	Date toDate=null;
	
	Date d1 = null;
	Date d2 = null;
	
	List<String> taskNumber=new ArrayList<String>();
	
	List<String> taskStatus=new ArrayList<String>();
	
	List<String> taskCreatedDate=new ArrayList<String>();
	
	List<String> userName=new ArrayList<String>();
	
	List<String> employeeEmail=new ArrayList<String>();
	
	List<String> customerName= new ArrayList<String>();
	
	List<String> customerType= new ArrayList<String>();
	
	List<String> taskName= new ArrayList<String>();
	
	List<String> customerExpiry= new ArrayList<String>();
	
	List<String> customerAdress= new ArrayList<String>();
	
	
	
	
	
	Calendar c = Calendar.getInstance();
	
	   public void execute(JobExecutionContext context)
			     throws JobExecutionException {
			  try {
				  
				  int tempEmpKey=0;
				  
				  int initail=100;
				 	
				  lstTask=taskData.getAllTask();//get the list of task sorted by empid and created date
				  
				  lstSalesRep=taskData.getSalesRepNamesForReminder("Y");//get Sales All salesrep and there email
				  
				  ListIterator<TasksModel> iterator = lstTask.listIterator();
				 
				  ListIterator<SalesRepModel> salesRepIterator = lstSalesRep.listIterator();
			     
			      while(iterator.hasNext())
			      {
			    	  TasksModel model = iterator.next();
			    	  
			    	  if(initail==100)
			    	  {
			    		  tempEmpKey=model.getEmployeeid();
			    		  initail++;
			    	  }
			    	  
			    	  
			    	if(model.getEmployeeid()==tempEmpKey) 
			    	{	
			    	  
			    	  
			    	  if(model.getHoursOrDays()!=null && model.getHoursOrDays().equalsIgnoreCase("H"))
			    	  {
			    		  if(model.getRemindIn()>=0)
			    		  {
			    			  d1 = model.getCreationDate();
			    			  c.setTime(model.getCreationDate());
			    			  c.add(Calendar.HOUR, (int)model.getRemindIn());  // number of days to add
			    			  d2 = c.getTime();
			    			  long diff=0l; 
			    			  	if(d1!=null && d2!=null && (d1.compareTo(d2)<0 || d1.compareTo(d2)==0))
			    			  	{
			    			  		diff= d2.getTime() - d1.getTime();
			    			  		if(diff==0)
			    			  		{
			    			  			taskNumber.add(model.getTaskNumber());
			    			  			taskStatus.add(model.getStatusName());
			    			  			taskCreatedDate.add(model.getCreationDateStr());
			    			  			userName.add(model.getEmployeeName());
			    			  			employeeEmail.add(model.getEmail());
			    			  			customerName.add(model.getCustomerNAme());
			    			  			customerType.add(model.getClientTypeFullName());
			    			  			taskName.add(model.getTaskName());
			    			  			customerExpiry.add(model.getCustomerExpiryDate());
			    			  			customerAdress.add(model.getCustomerAddress());
			    			  		}
			    			  		if(diff>0)
			    			  		{
			    			  			taskNumber.add(model.getTaskNumber());
			    			  			taskStatus.add(model.getStatusName());
			    			  			taskCreatedDate.add(model.getCreationDateStr());
			    			  			userName.add(model.getEmployeeName());
			    			  			employeeEmail.add(model.getEmail());
			    			  			customerName.add(model.getCustomerNAme());
			    			  			customerType.add(model.getClientTypeFullName());
			    			  			taskName.add(model.getTaskName());
			    			  			customerExpiry.add(model.getCustomerExpiryDate());
			    			  			customerAdress.add(model.getCustomerAddress());
			    			  		}
			    			  	}
			    		  	}
			    		
			    		  
			    	  }
			    	  else if(model.getHoursOrDays()!=null && model.getHoursOrDays().equalsIgnoreCase("D"))
			    	  {
			    		  if(model.getRemindIn()>=0)
			    		  {
			    			  d1 = model.getCreationDate();
			    			  c.setTime(model.getCreationDate());
			    			  c.add(Calendar.DATE, (int)model.getRemindIn());  // number of days to add
			    			  d2 = c.getTime();
			    			  long diff=0l; 
			    			  if(d1!=null && d2!=null && (d1.compareTo(d2)<0 || d1.compareTo(d2)==0))
			    			  {
			    			  diff= d2.getTime() - d1.getTime();
			    			  if(diff==0)
			    			  {
			    				  taskNumber.add(model.getTaskNumber());
		    			  			taskStatus.add(model.getStatusName());
		    			  			taskCreatedDate.add(model.getCreationDateStr());
		    			  			userName.add(model.getEmployeeName());
		    			  			employeeEmail.add(model.getEmail());
		    			  			customerName.add(model.getCustomerNAme());
		    			  			customerType.add(model.getClientTypeFullName());
		    			  			taskName.add(model.getTaskName());
		    			  			customerExpiry.add(model.getCustomerExpiryDate());
		    			  			customerAdress.add(model.getCustomerAddress());
			    			  }
			    			  if(diff>0)
			    			  {
			    				  taskNumber.add(model.getTaskNumber());
		    			  			taskStatus.add(model.getStatusName());
		    			  			taskCreatedDate.add(model.getCreationDateStr());
		    			  			userName.add(model.getEmployeeName());
		    			  			employeeEmail.add(model.getEmail());
		    			  			customerName.add(model.getCustomerNAme());
		    			  			customerType.add(model.getClientTypeFullName());
		    			  			taskName.add(model.getTaskName());
		    			  			customerExpiry.add(model.getCustomerExpiryDate());
		    			  			customerAdress.add(model.getCustomerAddress());
			    			  }
			    			  }
			    			 
			    		  }
			    		  else
			    		  {
			    			  d1 = model.getCreationDate();
			    			  d2 = model.getReminderDate();
			    			  long diff=0l; 
			    			  if(d1!=null && d2!=null && (d1.compareTo(d2)<0 || d1.compareTo(d2)==0))
			    			  {
			    				  diff= d2.getTime() - d1.getTime();
			    				  if(diff==0)
			    				  {
			    					  taskNumber.add(model.getTaskNumber());
			    			  			taskStatus.add(model.getStatusName());
			    			  			taskCreatedDate.add(model.getCreationDateStr());
			    			  			userName.add(model.getEmployeeName());
			    			  			employeeEmail.add(model.getEmail());
			    			  			customerName.add(model.getCustomerNAme());
			    			  			customerType.add(model.getClientTypeFullName());
			    			  			taskName.add(model.getTaskName());
			    			  			customerExpiry.add(model.getCustomerExpiryDate());
			    			  			customerAdress.add(model.getCustomerAddress());
			    				  }
			    				  if(diff>0)
			    				  {
			    					  taskNumber.add(model.getTaskNumber());
			    			  			taskStatus.add(model.getStatusName());
			    			  			taskCreatedDate.add(model.getCreationDateStr());
			    			  			userName.add(model.getEmployeeName());
			    			  			employeeEmail.add(model.getEmail());
			    			  			customerName.add(model.getCustomerNAme());
			    			  			customerType.add(model.getClientTypeFullName());
			    			  			taskName.add(model.getTaskName());
			    			  			customerExpiry.add(model.getCustomerExpiryDate());
			    			  			customerAdress.add(model.getCustomerAddress());
			    				  }
			    			  }
			    		  }
			    		  
			    	  }
			    	  
			    	  
			      }
			      else
				  {
			    	  
			    	  	tempEmpKey = model.getEmployeeid();
			    	  	
			    	 	//sendClientEmail(employeeEmail.get(0)); // send email with the list of task
			    	  	
			    	  	System.out.println(employeeEmail.get(0));
			    	  	
			    	  	logger.info("-------------------------> Test TaskMailReminder ----> onLoad--->"+employeeEmail.get(0)+"");
	
			    	  	//task list is sorted by empId, once the employee id changes send email and clear list for next employee->temporary done done need to find new solution. 
			    	  
			    	  	taskNumber=new ArrayList<String>();
			    		
			    		taskStatus=new ArrayList<String>();
			    		
			    		taskCreatedDate=new ArrayList<String>();
			    		
			    		userName=new ArrayList<String>();
			    		
			    		employeeEmail=new ArrayList<String>();
			    	 
			    		customerName= new ArrayList<String>();
			    		
			    		customerType= new ArrayList<String>();
			    		
			    		taskName= new ArrayList<String>();
			    		
			    		customerExpiry= new ArrayList<String>();
			    		
			    		customerAdress= new ArrayList<String>();
			    		
			    	  
			    	  
			    	  if(model.getHoursOrDays()!=null && model.getHoursOrDays().equalsIgnoreCase("H"))
			    	  {
			    		  if(model.getRemindIn()>=0)
			    		  {
			    			  d1 = model.getCreationDate();
			    			  c.setTime(model.getCreationDate());
			    			  c.add(Calendar.HOUR, (int)model.getRemindIn());  // number of days to add
			    			  d2 = c.getTime();
			    			  long diff=0l; 
			    			  	if(d1!=null && d2!=null && (d1.compareTo(d2)<0 || d1.compareTo(d2)==0))
			    			  	{
			    			  		diff= d2.getTime() - d1.getTime();
			    			  		if(diff==0)
			    			  		{
			    			  			taskNumber.add(model.getTaskNumber());
			    			  			taskStatus.add(model.getStatusName());
			    			  			taskCreatedDate.add(model.getCreationDateStr());
			    			  			userName.add(model.getEmployeeName());
			    			  			employeeEmail.add(model.getEmail());
			    			  			customerName.add(model.getCustomerNAme());
			    			  			customerType.add(model.getClientTypeFullName());
			    			  			taskName.add(model.getTaskName());
			    			  			customerExpiry.add(model.getCustomerExpiryDate());
			    			  			customerAdress.add(model.getCustomerAddress());
			    			  			//sendClientEmail(model.getEmail());
			    			  		}
			    			  		if(diff>0)
			    			  		{
			    			  			
			    			  		taskNumber.add(model.getTaskNumber());
		    			  			taskStatus.add(model.getStatusName());
		    			  			taskCreatedDate.add(model.getCreationDateStr());
		    			  			userName.add(model.getEmployeeName());
		    			  			employeeEmail.add(model.getEmail());
		    			  			customerName.add(model.getCustomerNAme());
		    			  			customerType.add(model.getClientTypeFullName());
		    			  			taskName.add(model.getTaskName());
		    			  			customerExpiry.add(model.getCustomerExpiryDate());
		    			  			customerAdress.add(model.getCustomerAddress());
			    			  			//sendClientEmail(model.getEmail());
			    			  		}
			    			  	}
			    		  	}
			    		
			    		  
			    	  }
			    	  else if(model.getHoursOrDays()!=null && model.getHoursOrDays().equalsIgnoreCase("D"))
			    	  {
			    		  if(model.getRemindIn()>=0)
			    		  {
			    			  d1 = model.getCreationDate();
			    			  c.setTime(model.getCreationDate());
			    			  c.add(Calendar.DATE, (int)model.getRemindIn());  // number of days to add
			    			  d2 = c.getTime();
			    			  long diff=0l; 
			    			  if(d1!=null && d2!=null && (d1.compareTo(d2)<0 || d1.compareTo(d2)==0))
			    			  {
			    			  diff= d2.getTime() - d1.getTime();
			    			  if(diff==0)
			    			  {
			    				  taskNumber.add(model.getTaskNumber());
		    			  		  taskStatus.add(model.getStatusName());
		    			  			taskCreatedDate.add(model.getCreationDateStr());
		    			  			userName.add(model.getEmployeeName());
		    			  			employeeEmail.add(model.getEmail());
		    			  			customerName.add(model.getCustomerNAme());
		    			  			customerType.add(model.getClientTypeFullName());
		    			  			taskName.add(model.getTaskName());
		    			  			customerExpiry.add(model.getCustomerExpiryDate());
		    			  			customerAdress.add(model.getCustomerAddress());
		    			  			//sendClientEmail(model.getEmail());
			    			  }
			    			  if(diff>0)
			    			  {
			    				  taskNumber.add(model.getTaskNumber());
		    			  			taskStatus.add(model.getStatusName());
		    			  			taskCreatedDate.add(model.getCreationDateStr());
		    			  			userName.add(model.getEmployeeName());
		    			  			employeeEmail.add(model.getEmail());
		    			  			customerName.add(model.getCustomerNAme());
		    			  			customerType.add(model.getClientTypeFullName());
		    			  			taskName.add(model.getTaskName());
		    			  			customerExpiry.add(model.getCustomerExpiryDate());
		    			  			customerAdress.add(model.getCustomerAddress());
		    			  			//sendClientEmail(model.getEmail());
			    			  }
			    			  }
			    			 
			    		  }
			    		  else
			    		  {
			    			  d1 = model.getCreationDate();//check for reminder date diffrence  
			    			  d2 = model.getReminderDate();
			    			  long diff=0l; 
			    			  if(d1!=null && d2!=null && (d1.compareTo(d2)<0 || d1.compareTo(d2)==0))
			    			  {
			    				  diff= d2.getTime() - d1.getTime();
			    				  if(diff==0)
			    				  {
			    					  taskNumber.add(model.getTaskNumber());
			    			  			taskStatus.add(model.getStatusName());
			    			  			taskCreatedDate.add(model.getCreationDateStr());
			    			  			userName.add(model.getEmployeeName());
			    			  			employeeEmail.add(model.getEmail());
			    			  			customerName.add(model.getCustomerNAme());
			    			  			customerType.add(model.getClientTypeFullName());
			    			  			taskName.add(model.getTaskName());
			    			  			customerExpiry.add(model.getCustomerExpiryDate());
			    			  			customerAdress.add(model.getCustomerAddress());
			    			  		  //sendClientEmail(employeeEmail);
			    			  		 // sendClientEmail(model.getEmail());
			    				  }
			    				  if(diff>0)
			    				  {
			    					  taskNumber.add(model.getTaskNumber());
			    			  			taskStatus.add(model.getStatusName());
			    			  			taskCreatedDate.add(model.getCreationDateStr());
			    			  			userName.add(model.getEmployeeName());
			    			  			employeeEmail.add(model.getEmail());
			    			  			customerName.add(model.getCustomerNAme());
			    			  			customerType.add(model.getClientTypeFullName());
			    			  			taskName.add(model.getTaskName());
			    			  			customerExpiry.add(model.getCustomerExpiryDate());
			    			  			customerAdress.add(model.getCustomerAddress());
			    			  		  //sendClientEmail(employeeEmail);
			    				  }
			    			  }
			    		  }
			    		  
			    	  }
			    	  
				  }
			    	
			    if(!iterator.hasNext())
			    {
			    	//sendClientEmail(employeeEmail.get(0));
			    	System.out.println(employeeEmail.get(0));
			    	logger.info("-------------------------> Test TaskMailReminder ----> onLoad--->"+employeeEmail.get(0)+"");
			    }
			    	
			    		    	
			   }
			      
			     
			  } catch (Exception e) {
				  StringWriter sw = new StringWriter();
				  e.printStackTrace(new PrintWriter(sw)); 
				  Messagebox.show(sw.toString());
				  logger.error(""+sw.toString()+"TaskMailReminder execute Quarts-->execute ");		
				}
			      
			        
			    }
	   
	  
	
	   
	   private void sendClientEmail(String email)
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
				String subject="Hinawi Task Reminder";
				StringBuffer result=null;
				result=new StringBuffer();
				
				result.append("<p>Dear&nbsp;"+userName.get(0)+",</p> ");

				result.append("<p>This is just an automatic reminder &nbsp;for total no of <strong> "+taskNumber.size()+" </strong> tasks that are pending.</p> ");

				result.append("<table align='left' border='0' cellpadding='20' cellspacing='3'> ");
				result.append("<caption><strong>Pending Task Details</strong></caption> ");
				result.append("<thead> ");
				result.append("	<tr> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Task No</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Task Name</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Task Current Status</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>&nbsp;Customer Name&nbsp;</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>&nbsp;Customer Type</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>&nbsp;Customer Expiry</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>&nbsp;Customer Adress</th> ");
				result.append("	<th scope='col' style='background-color: rgb(152, 179, 166);'>Created Date</th> ");
				result.append("	</tr> ");
				result.append("</thead> ");
				result.append("<tbody> ");
				for(int i=0;i<taskNumber.size();i++)
				{
					result.append("	<tr> ");
					result.append("	<td>"+taskNumber.get(i)+"</td> ");
					result.append("	<td>"+taskName.get(i)+"</td> ");
					result.append("	<td>"+taskStatus.get(i)+"</td> ");
					result.append("	<td>"+customerName.get(i)+"</td> ");
					result.append("	<td>"+customerType.get(i)+"</td> ");
					result.append("	<td>"+customerExpiry.get(i)+"</td> ");
					result.append("	<td>"+customerAdress.get(i)+"</td> ");
					result.append("<td>"+taskCreatedDate.get(i)+"</td> ");
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
				  logger.error(""+sw.toString()+" sendClientEmail Quarts-->sendClientEmail ");
		  }	
		}
	   
	   
		
		/**
		 * @return the lstTask
		 */
		public List<TasksModel> getLstTask() {
			return lstTask;
		}
		/**
		 * @param lstTask the lstTask to set
		 */
		public void setLstTask(List<TasksModel> lstTask) {
			this.lstTask = lstTask;
		}
		/**
		 * @return the lstAllTask
		 */
		public List<TasksModel> getLstAllTask() {
			return lstAllTask;
		}
		/**
		 * @param lstAllTask the lstAllTask to set
		 */
		public void setLstAllTask(List<TasksModel> lstAllTask) {
			this.lstAllTask = lstAllTask;
		}
		
		
		
		
		
		
	   
	   
	   

}
