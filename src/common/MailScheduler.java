package common;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class MailScheduler {
	
	  public MailScheduler() throws Exception {
	        
	        SchedulerFactory sf = new StdSchedulerFactory();
	        
	        Scheduler sche = sf.getScheduler();
	        
	        sche.start();
	        
	        String cronExpressionMain=new String();
	        
	        cronExpressionMain="";
	        
	        //here we Need to get the settings from the My Sql tables or SQL to genrate the Cron expressions 
	        
	        String type="minutes";
	        int year=0;
	        int months=0;
	        int weeks=0;
	        int days=0;
	        int hours=0;
	        int minutes=3;
	        
	        cronExpressionMain= generateExpression(type,year,months,weeks,days,hours,minutes);

	        
	        //task reminders
	        JobDetail taskJob = newJob(TaskMailReminder.class).withIdentity("TaskMailReminderJobName", "MailReminderGroupName").build(); 
	        
	        Trigger taskJobtrigger = newTrigger().withIdentity("MailReminderTriggerNAme", "MailReminderGroupName").withSchedule(cronSchedule(cronExpressionMain)).forJob("TaskMailReminderJobName", "MailReminderGroupName").build();//expression 
	        
	        
	        //customer Balance Reminders
	        JobDetail customerBalceJob = newJob(CustomerBalanceReminder.class).withIdentity("CustomerBalanceMailReminderJobName", "CustomerBalanceMailReminderGroupName").build(); 
	        
	        Trigger customerBalceJobtrigger = newTrigger().withIdentity("CustomerBalanceMailReminderTriggerNAme", "CustomerBalanceMailReminderGroupName").withSchedule(cronSchedule(cronExpressionMain)).forJob("CustomerBalanceMailReminderJobName", "CustomerBalanceMailReminderGroupName").build();//expression 
	        
	        
	        
	        //Quotation to sales Rep.
	        JobDetail quoationToSalesRep = newJob(QuotationReminderForSalesRep.class).withIdentity("QuoationToSalesRepMailReminderJobName", "QuoationToSalesRepMailReminderGroupName").build(); 
	        
	        Trigger quoationToSalesReptrigger = newTrigger().withIdentity("QuoationToSalesRepMailReminderTriggerNAme", "QuoationToSalesRepMailReminderGroupName").withSchedule(cronSchedule(cronExpressionMain)).forJob("QuoationToSalesRepMailReminderJobName", "QuoationToSalesRepMailReminderGroupName").build();//expression 
	        
	        
	        //0 0/5 * 1/1 * ? *-->every 5 mins 
	        
	        //0 15 08 * * ?->every day at 8.15 am 
	        
	       /*CronTrigger crTrigger = newCronTrigger("cronTrigger", "NJob", "0/2 * * * * ?");*/
	        
	        sche.scheduleJob(taskJob, taskJobtrigger);
	        sche.scheduleJob(customerBalceJob, customerBalceJobtrigger);
	        sche.scheduleJob(quoationToSalesRep, quoationToSalesReptrigger);
	    }
	  
	  private String generateExpression(String type,int year,int months ,int weeks,int days,int hours,int minutes)
	  {
		  String cronExpression=new String();
		  cronExpression="";
		  
		  if(type!=null && type.equalsIgnoreCase("year"))
		  {
			  cronExpression="0 0 14 ? 3 WED#1 *";
		  }
		  else if(type!=null && type.equalsIgnoreCase("months"))
		  {
			  cronExpression="0 0 12 10 1/2 ? *";
		  }
		  else if(type!=null && type.equalsIgnoreCase("weeks"))
		  {
			  cronExpression="0 0 12 ? * SAT *";
		  }
		  else if(type!=null && type.equalsIgnoreCase("days"))
		  {
			  cronExpression="0 0 12 1/"+days+" * ? *";
		  }
		  else if(type!=null && type.equalsIgnoreCase("hours"))
		  {
			  cronExpression="0 0 0/"+hours+" 1/1 * ? *";
		  }
		  else if(type!=null && type.equalsIgnoreCase("minutes"))
		  {
			  cronExpression="0 0/"+minutes+" * 1/1 * ? *";
		  }
		  
		  return cronExpression;
		  
	  }

}
