package list;

public class ListQueries 
{
	StringBuffer query;
	public String getHRListFieldsQuery()
	{
		 query=new StringBuffer();
		 query.append("SELECT FIELD_ID, FIELD_NAME, FIELD_DESCRIPTION, ARABIC, NEW_FLAG, EDIT_FLAG, DELETE_FLAG, PARENT_LIST, REQUIRED,LastModified");
		 query.append(" FROM HRLISTFIELDS where NEW_FLAG ='Y'");
		 query.append(" order by FIELD_DESCRIPTION");
		return query.toString();		
	}
	
	public String getHRListValuesQuery(int fieldId ,int subId)
	{
		query=new StringBuffer();
		 query.append("Select ID, QBListID, QBEditSequance, CODE, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID,notes from HRLISTVALUES");
		 if(subId==0)
		 query.append(" where FIELD_ID=" + fieldId);
		 else
		 query.append(" where SUB_ID=" + subId + " and FIELD_ID=" + fieldId);
		 
		 query.append(" order by PRIORITY_ID,DESCRIPTION");
		return query.toString();		
	}
	
	public String getMaxIDQuery(String tableName,String fieldName)
	{
		query=new StringBuffer();
		query.append(" select max("+ fieldName +") from "+ tableName);
		return query.toString();
	}
	public String addNewLocalItemValueQuery(int recNo,String name,String fullName,int itemTypeRef,String description,String arDescription, int userId)
	{
		String tempNotes=""; 
		if(tempNotes!=null)
		{
			tempNotes=description.replaceAll("'","`");
		}
		query=new StringBuffer();
		query.append(" insert into LocalItem (RecNo,ItemTypeRef,Name,FullName,Description,DescriptionAR,UserID,ModifiedDate,LastViewedBy,ViewDate)");
		query.append(" values ( "+recNo+",'" + itemTypeRef + "','" +name+"','"+fullName+"','"+description+"','"+ arDescription +"', '" +userId + "' ,getdate() , " +userId  +",getdate() )");
		return query.toString();
	}
	
	public String addNewHRListValueQuery(int ID,String DeptName,String DeptNameAr,String fieldName,int fieldID,int priorityId,int subId,String notes)
	{
		String tempNotes=""; 
		if(tempNotes!=null)
		{
			tempNotes=notes.replaceAll("'","`");
		}
		query=new StringBuffer();
		query.append(" insert into HRLISTVALUES (ID,DESCRIPTION,ARABIC,SUB_ID,FIELD_ID,FIELD_NAME,DEF_VALUE,REQUIRED,PRIORITY_ID,notes)");
		query.append(" values ( "+ID+",'"+DeptName+"','"+DeptNameAr+"',"+subId+","+ fieldID +", '" +fieldName + "','','N'," +priorityId  +",'"+tempNotes+"');");
		return query.toString();
	}
	public String updateHRListValueQuery(int ID,String DeptName,String DeptNameAr,int priorityId,String notes)
	{
		String tempNotes=""; 
		if(tempNotes!=null)
		{
			tempNotes=notes.replaceAll("'","`");
		}
		query=new StringBuffer();
		query.append(" update HRLISTVALUES set DESCRIPTION='" + DeptName + "' , ARABIC='" + DeptNameAr + "' , PRIORITY_ID="+priorityId+",notes='"+tempNotes+"' ");
		query.append(" where ID="+ID);
		return query.toString();
	}
	public String deleteHRListValueQuery(int listID)
	{
		query=new StringBuffer();
		query.append("  delete from HRLISTVALUES where ID=" + listID);
		return query.toString();		
	}
	
	public String checkValueUsedQuery(String tableName,String fieldName, int listID)
	{
		query=new StringBuffer();
		query.append("  select count(*) as cnt FROM " + tableName + " WHERE " + fieldName + " =" + listID);
		return query.toString();		
	}
	
	public String updateLastModifiedHRListFieldsQuery(int ID)
	{	
		query=new StringBuffer();
		query.append(" update HRLISTFIELDS set LastModified=getdate()");
		query.append(" where FIELD_ID="+ID);
		return query.toString();
	}
	
	public String getPiroirtyListQuery(String selectedType)
	{
		 query=new StringBuffer();
		 query.append(" Select Customer.Name As CusName,Customer.note as CusNote,Customer.Phone AS CusPhone,Customer.AltPhone As CusPhone1,Customer.Email As CusEmail,");
		 query.append(" Employee.Name As EmpName,Employee.Phone As EmpPhone,Employee.AltPhone As EmpPhone1,Employee.Email As EmpEmail,");
		 query.append(" Vendor.Name As VendName,Vendor.Note As VendNote,Vendor.Phone As VendPhone,Vendor.AltPhone As VendPhone1,Vendor.Email As VendEmail,");
		 query.append(" OtherNames.Name As OthName,OtherNames.Phone As OthPhone,OtherNames.AltPhone As OthPhone1,OtherNames.Email As OthEmail,");
		 query.append(" QBLists.* From QBLists ");
		 query.append(" LEFT JOIN Customer ON QBLISTS.REcNO=Customer.Cust_Key");
		 query.append(" LEFT JOIN Employee ON QBLISTS.REcNO=Employee.Emp_Key ");
		 query.append(" LEFT JOIN Vendor ON QBLISTS.REcNO=Vendor.Vend_Key ");
		 query.append(" LEFT JOIN OtherNames ON QBLISTS.REcNO=OtherNames.OthNam_Key ");
		 query.append(" WHERE PRIORITY_ID=1");
		 if(!selectedType.equals("All"))
		 query.append(" AND QBLISTS.ListType = '" + selectedType + "' ");		
		 query.append(" Order by Customer.Name");
		return query.toString();		
	}
	
	public String getPiroirtyProspectiveListQuery()
	{
		query=new StringBuffer();
		query.append(" SELECT * FROM PROSPECTIVE WHERE PRIORITYID=1  Order by Name");
		return query.toString();		
	}
	
	public String updateQBPriorityQuery(int recNo,String listType)
	{	
		query=new StringBuffer();
		query.append(" update QBLists set Priority_ID =0 ");
		query.append(" where RecNo="+recNo  + " and ListType='" + listType+"'" );
		return query.toString();
	}
	public String updateOtherPriorityQuery(int recNo,String tableName,String keyfield)
	{	
		query=new StringBuffer();
		query.append(" update " + tableName + " set PRIORITYID  =0 ");
		query.append(" where "+ keyfield + "= " +recNo );
		return query.toString();
	}
	
}
