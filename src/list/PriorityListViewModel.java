package list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.CustomerModel;
import model.DataFilter;






import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;


public class PriorityListViewModel {

	private Logger logger = Logger.getLogger(this.getClass());
	ListData data=new ListData();
	
	private List<String> lstTypes;
	private String selectedType;
	private List<CustomerModel> lstPriority;
	private List<CustomerModel> lstAllCustomers = new ArrayList<CustomerModel>();
	private CustomerModel selectedCustomer;
	private String footer;
	private DataFilter filter = new DataFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private Set<CustomerModel> selectedEntities;
	
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		FillTypeList();
		lstPageSize = new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);

		lstAllPageSize = new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize = lstAllPageSize.get(2);

		selectedPageSize = lstPageSize.get(2);
		
		
	}
	public PriorityListViewModel()
	{
		
	}
	
	private void FillTypeList()
	{
	lstTypes=new ArrayList<String>();
	lstTypes.add("All");
	lstTypes.add("Customer");
	lstTypes.add("Prospective");
	lstTypes.add("Vendor");
	lstTypes.add("Employee");
	lstTypes.add("Other Name");
	selectedType=lstTypes.get(0);
	
	 fillDataList();
	}
	
	private List<CustomerModel> filterData() {
		lstPriority.clear();
		lstPriority.addAll(lstAllCustomers);
		List<CustomerModel> lst = new ArrayList<CustomerModel>();
		for (Iterator<CustomerModel> i = lstPriority.iterator(); i.hasNext();) {
			CustomerModel tmp = i.next();
			if (tmp.getName().toLowerCase()
					.contains(filter.getName().toLowerCase())					
					&& tmp.getPhone().toLowerCase()
							.contains(filter.getPhone().toLowerCase())
					&& tmp.getAltphone().toLowerCase()
							.contains(filter.getAltphone().toLowerCase())					
					&& tmp.getEmail().toLowerCase()
							.contains(filter.getEmail().toLowerCase())					
					&& tmp.getListType().toLowerCase()
							.contains(filter.getType().toLowerCase())
					&& tmp.getNote().toLowerCase()
							.contains(filter.getNote().toLowerCase())		
					) 
			{
				lst.add(tmp);
			}
		}
		return lst;

	}
	
	@Command
	@NotifyChange({ "lstPriority", "footer" })
	public void changeFilter() {
		try {
			lstPriority = filterData();
			footer = setFooterContent();

		} catch (Exception ex) {
			logger.error("error in PriorityListViewModel---changeFilter-->", ex);
		}

	}
	private String setFooterContent()
	{
		String result="";
		int customer=0,employee=0,vendor=0,other=0,prospective=0;
		
		for (CustomerModel item : lstPriority)
		{
			switch (item.getListType())
			{
				case "Customer":
					customer++;
					break;
				case "Employee":
					employee++;
					break;
				case "Vendor":
					vendor++;
					break;
				case "OtherNames":
					other++;
					break;		
				case "Prospective":
					prospective++;
					break;	
				default : // Optional					
			
			}
		}
		
		result = "Total no. of Customers: " +customer + " | Prospectives: " + prospective + " | Vendors: " + vendor + " | Employees: " +employee + " | OtherNames: "+other;
		return result;
	}
	
	 @Command
	 @NotifyChange({ "lstPriority", "footer" })
	   public void searchCommand()
	   {
		   try
		   {			 
			   fillDataList();
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in PriorityListViewModel ----> searchCommand", ex);			
			}
	   }
	   
	 @Command
	 @NotifyChange({ "lstPriority","selectedType", "footer"})
	   public void refreshCommand()
	   {
		   try
		   {			  
			   selectedType=lstTypes.get(0);
			   fillDataList();
			   
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in PriorityListViewModel ----> refreshCommand", ex);			
			}
	   }
	 	
	 private void fillDataList()
	 {
		 try
		 {
			 lstAllCustomers = new ArrayList<CustomerModel>();		
		 selectedEntities=null;
		 lstPriority=data.getPiroirtyList(selectedType);
		   lstAllCustomers.addAll(lstPriority);
			if (lstPriority.size() > 0)
				selectedCustomer = lstPriority.get(0);
			footer = setFooterContent();
		 }
		 catch (Exception ex) {
				logger.error("error in PriorityListViewModel---fillDataList-->", ex);
			}
	 }
	 
	 @GlobalCommand 
	 @NotifyChange({ "lstPriority","selectedType", "footer"})
	 public void resetDataList()
	 {
		 try
		 {
		lstAllCustomers = new ArrayList<CustomerModel>();		
		 selectedEntities=null;
		 lstPriority=data.getPiroirtyList(selectedType);
		   lstAllCustomers.addAll(lstPriority);
			if (lstPriority.size() > 0)
				selectedCustomer = lstPriority.get(0);
			footer = setFooterContent();
		 }
		 catch (Exception ex) {
				logger.error("error in PriorityListViewModel---resetDataList-->", ex);
			}
	 }
	 
	
// @SuppressWarnings("unchecked")
	@Command
	 @NotifyChange({ "lstPriority","selectedType", "footer"})
	 public void removeCommand()
	 {
		 try
		   {
			 if(selectedEntities==null)
			 {
				 Messagebox.show("No name selected to remove from priority !. ","Remove Priority", Messagebox.OK , Messagebox.INFORMATION);
				 return;
			 }
			 
			 if(selectedEntities!=null)
			 {
			 Messagebox.show("Do you really want to remove the priority of the Selected name(s)?","Remove Priority",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
				{
				     @NotifyChange({ "lstPriority","selectedType", "footer"})
					 public void onEvent(Event e)
					 {
						 if (Messagebox.ON_YES.equals(e.getName()))
		              {								
						
							 List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
							 for (CustomerModel item : selectedEntities) 
								{
								 lstCustomers.add(item);
								}
							 
							 data.updateQBPriority(lstCustomers);
							 for (CustomerModel item : selectedEntities) 
								{
								 lstPriority.remove(item);
								}
							// postGlobalCommand("resetGrid");
							//  fillDataList();
							 BindUtils.postGlobalCommand(null, null, "resetDataList", null);								
																	
		              }
						 
					 }
				 });
			
			 }			 			 			  	
			 //fillDataList(); 			  
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in PriorityListViewModel ----> removeCommand", ex);			
			}
	 }
	
	public List<String> getLstTypes() {
		return lstTypes;
	}
	public void setLstTypes(List<String> lstTypes) {
		this.lstTypes = lstTypes;
	}
	public String getSelectedType() {
		return selectedType;
	}
	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}
	public List<CustomerModel> getLstPriority() {
		return lstPriority;
	}
	public void setLstPriority(List<CustomerModel> lstPriority) {
		this.lstPriority = lstPriority;
	}
	public CustomerModel getSelectedCustomer() {
		return selectedCustomer;
	}
	public void setSelectedCustomer(CustomerModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}
	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}
	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}
	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}
	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}
	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}
	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}
	@NotifyChange({ "selectedPageSize" })
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
		if (selectedAllPageSize.equalsIgnoreCase("All")) {
			selectedPageSize = lstPriority.size();

		} else
			selectedPageSize = Integer.parseInt(selectedAllPageSize);
	}	
	public DataFilter getFilter() {
		return filter;
	}
	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
	public Set<CustomerModel> getSelectedEntities() {
		return selectedEntities;
	}
	public void setSelectedEntities(Set<CustomerModel> selectedEntities) {
		this.selectedEntities = selectedEntities;
	}
}
