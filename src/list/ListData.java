package list;
import hr.HRQueries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.ClassModel;
import model.CompanyDBModel;
import model.CustomerModel;
import model.HRListFieldsModel;
import model.HRListValuesModel;
import model.PropertyModel;
import model.VehicleModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ListData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	ListQueries lstQuery=new ListQueries();
	
	public ListData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HRQueries query=new HRQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---Init-->" , ex);
		}
	}

	public List<HRListFieldsModel> fillHRFields(String type)
	{
		List<HRListFieldsModel> lstHRFields=new ArrayList<HRListFieldsModel>();
		HRListFieldsModel obj=new HRListFieldsModel();
		
		if(!type.equals(""))
		{
			obj.setFieldId(0);
			obj.setDescreption(type);
			obj.setArabic(type);
			obj.setNewFlag("N");
			obj.setEditFlag("N");
			obj.setDeleteFlag("N");
			obj.setParentlistId(0);
			lstHRFields.add(obj);
		}
		try
		{
		ResultSet rs = null;
		rs=db.executeNonQuery(lstQuery.getHRListFieldsQuery());
		while(rs.next())
		{			
			obj=new HRListFieldsModel();
			obj.setFieldId(rs.getInt("FIELD_ID"));
			obj.setFieldName(rs.getString("FIELD_NAME"));
			obj.setDescreption(rs.getString("FIELD_DESCRIPTION"));
			obj.setArabic(rs.getString("ARABIC"));
			obj.setNewFlag(rs.getString("NEW_FLAG"));
			obj.setEditFlag(rs.getString("EDIT_FLAG"));
			obj.setDeleteFlag(rs.getString("DELETE_FLAG"));
			obj.setParentlistId(rs.getInt("PARENT_LIST"));
			obj.setLastModified(rs.getDate("LastModified"));
			lstHRFields.add(obj);
		}
		
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---fillHRFields-->" , ex);
		}
		
		return lstHRFields;
	}
	
	public List<HRListValuesModel> getHRListValues(int fieldId,String type,int subId)
	{
		 List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();		 		
		 HRListValuesModel obj=new HRListValuesModel();
			if(!type.equals(""))
			{
			obj.setListId(0);					
			obj.setEnDescription(type);		
			obj.setArDescription(type);
			lst.add(obj);
			}					   			
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(lstQuery.getHRListValuesQuery(fieldId,subId));
				while(rs.next())
				{
					obj=new HRListValuesModel();
					obj.setListId(rs.getInt("ID"));					
					obj.setEnDescription(rs.getString("DESCRIPTION") == null ? "": rs.getString("DESCRIPTION"));
					obj.setArDescription(rs.getString("ARABIC") == null ? "": rs.getString("ARABIC"));
					obj.setSubId(rs.getInt("SUB_ID"));
					obj.setFieldId(rs.getInt("FIELD_ID"));
					obj.setFieldName(rs.getString("FIELD_NAME"));
					obj.setPriorityId(rs.getInt("PRIORITY_ID"));
					obj.setRequired(rs.getString("REQUIRED"));
					obj.setQbListId(rs.getString("QBListID"));
					obj.setNotes(rs.getString("notes") == null ? "": rs.getString("notes"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in ListData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	private int getMaxID(String tableName,String fieldName)
	{
		int result=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(lstQuery.getMaxIDQuery(tableName, fieldName));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;
			
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---getMaxID-->" , ex);
		}	
		return result;
	}
	
	public int addNewLocalItemValue(String name,String fullName,int itemTypeRef,String description,String arDescription,int userId)
	{
		int result=0;			
		try 
		{	
			int newID=getMaxID("LocalItem", "RecNo");
			//db.executeUpdateQuery(lstQuery.updateLastModifiedHRListFieldsQuery(fieldID));
			result=db.executeUpdateQuery(lstQuery.addNewLocalItemValueQuery(newID, name, fullName, itemTypeRef, description,arDescription ,userId));	
			result=newID;
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---addNewHRListValue-->" , ex);
		}	
		return result;
	}
	
	public int addNewHRListValue(String DeptName,String DeptNameAr,String fieldName,int fieldID,int priorityId,int subId,String notes)
	{
		int result=0;			
		try 
		{	
			int newID=getMaxID("HRLISTVALUES", "ID");
			db.executeUpdateQuery(lstQuery.updateLastModifiedHRListFieldsQuery(fieldID));
			result=db.executeUpdateQuery(lstQuery.addNewHRListValueQuery(newID, DeptName, DeptNameAr, fieldName, fieldID,priorityId,subId,notes));			
			result=newID;
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---addNewHRListValue-->" , ex);
		}	
		return result;
	}
	
	public int updateHRListValue(int listId,String DeptName,String DeptNameAr,int priorityId,String notes,int fieldID)
	{
		int result=0;		
		try 
		{			
			db.executeUpdateQuery(lstQuery.updateLastModifiedHRListFieldsQuery(fieldID));
			result=db.executeUpdateQuery(lstQuery.updateHRListValueQuery(listId,DeptName, DeptNameAr,priorityId,notes));						
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---updateHRListValue-->" , ex);
		}	
		return result;
	}
	
	public int  deleteHRListValue(int listID)
	{
		int result=0;				
		try 
		{			
			result=db.executeUpdateQuery(lstQuery.deleteHRListValueQuery(listID));					
		}
		catch (Exception ex) {
			logger.error("error in ListData---deleteHRListValue-->" , ex);
		}
		return result;		
	}
	
	public int checkHRListValueUsed(String tableName,String fieldName, int listID)
	{
		int result=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(lstQuery.checkValueUsedQuery(tableName, fieldName, listID));
			while(rs.next())
			{
				result=rs.getInt("cnt");
			}			
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---checkHRListValueUsed-->" , ex);
		}	
		return result;
	}
	
	public List<CustomerModel> getPiroirtyList(String selectedType)
	{
		List<CustomerModel> lst=new ArrayList<CustomerModel>();
		CustomerModel obj=new CustomerModel();			
		try
		{
		ResultSet rs = null;
		rs=db.executeNonQuery(lstQuery.getPiroirtyListQuery(selectedType));
		while(rs.next())
		{			
			obj=new CustomerModel();
			switch (rs.getString("ListType"))
			{
				case "Customer":					
					obj.setName(rs.getString("CusName")== null ? "": rs.getString("CusName"));
					obj.setListType("Customer");
					obj.setPhone(rs.getString("CusPhone")== null ? "": rs.getString("CusPhone"));
					obj.setAltphone(rs.getString("CusPhone1")== null ? "": rs.getString("CusPhone1"));
					obj.setEmail(rs.getString("CusEmail")== null ? "": rs.getString("CusEmail"));
					obj.setNote(rs.getString("CusNote")== null ? "": rs.getString("CusNote"));
					break;
				case "Employee":
					obj.setName(rs.getString("EmpName")== null ? "": rs.getString("EmpName"));
					obj.setListType("Employee");
					obj.setPhone(rs.getString("EmpPhone")== null ? "": rs.getString("EmpPhone"));
					obj.setAltphone(rs.getString("EmpPhone1")== null ? "": rs.getString("EmpPhone1"));
					obj.setEmail(rs.getString("EmpEmail")== null ? "": rs.getString("EmpEmail"));
					obj.setNote("");
					break;
				case "Vendor":
					obj.setName(rs.getString("VendName")== null ? "": rs.getString("VendName"));
					obj.setListType("Vendor");
					obj.setPhone(rs.getString("VendPhone")== null ? "": rs.getString("VendPhone"));
					obj.setAltphone(rs.getString("VendPhone1")== null ? "": rs.getString("VendPhone1"));
					obj.setEmail(rs.getString("VendEmail")== null ? "": rs.getString("VendEmail"));
					obj.setNote(rs.getString("VendNote")== null ? "": rs.getString("VendNote"));
					break;
				case "OtherNames":
					obj.setName(rs.getString("OthName")== null ? "": rs.getString("OthName"));
					obj.setListType("Other Names");
					obj.setPhone(rs.getString("OthPhone")== null ? "": rs.getString("OthPhone"));
					obj.setAltphone(rs.getString("OthPhone1")== null ? "": rs.getString("OthPhone1"));
					obj.setEmail(rs.getString("OthEmail")== null ? "": rs.getString("OthEmail"));
					obj.setNote("");
					break;				
				default : // Optional					
			
			}
			obj.setCustkey(rs.getInt("RecNo"));
			lst.add(obj);
		}
		
		if(selectedType.equals("All") || selectedType.equals("Prospective"))
		{
			rs = null;
			rs=db.executeNonQuery(lstQuery.getPiroirtyProspectiveListQuery());
			while(rs.next())
			{			
				obj=new CustomerModel();
				obj.setCustkey(rs.getInt("RecNo"));
				obj.setName(rs.getString("Name")== null ? "": rs.getString("Name"));
				obj.setListType("Prospective");
				obj.setPhone(rs.getString("TelePhone1")== null ? "": rs.getString("TelePhone1"));
				obj.setAltphone(rs.getString("TelePhone2")== null ? "": rs.getString("TelePhone2"));
				obj.setEmail(rs.getString("Email")== null ? "": rs.getString("Email"));
				obj.setNote(rs.getString("Note")== null ? "": rs.getString("Note"));
				
				lst.add(obj);
				
			}
		}
		
		
		if (lst.size() > 0) {
			  Collections.sort(lst, new Comparator<CustomerModel>() {
			      @Override
			      public int compare(final CustomerModel object1, final CustomerModel object2) {
			          return object1.getName().compareTo(object2.getName());
			      }
			  });
			}				
		
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---getPiroirtyList-->" , ex);
		}
		
		return lst;
	}
	
	public int updateQBPriority(List<CustomerModel> lstCustomers)
	{
		int result=0;		
		try 
		{			
			 for (CustomerModel item : lstCustomers) 
			 {
				 if(!item.getListType().equals("Prospective"))
				 {
					 db.executeUpdateQuery(lstQuery.updateQBPriorityQuery(item.getCustkey(), item.getListType()));	
				 }
				 
				 switch (item.getListType())
					{
						case "Customer":
							 db.executeUpdateQuery(lstQuery.updateOtherPriorityQuery(item.getCustkey(), "Customer", "Cust_Key"));
							break;
						case "Employee":
							 db.executeUpdateQuery(lstQuery.updateOtherPriorityQuery(item.getCustkey(), "Employee", "Emp_Key"));
							break;
						case "Vendor":
							 db.executeUpdateQuery(lstQuery.updateOtherPriorityQuery(item.getCustkey(), "Vendor", "Vend_Key"));
							break;
						case "OtherNames":
							 db.executeUpdateQuery(lstQuery.updateOtherPriorityQuery(item.getCustkey(), "OtherNames", "OthNam_Key"));
							break;		
						case "Prospective":
							 db.executeUpdateQuery(lstQuery.updateOtherPriorityQuery(item.getCustkey(), "Prospective", "RecNo"));
							break;	
						default : // Optional					
					
					}				 				
			 }
									
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---updateQBPriority-->" , ex);
		}	
		return result;
	}
		
}
