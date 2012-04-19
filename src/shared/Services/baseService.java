package daris.Monbulk.shared.Services;

import daris.Monbulk.shared.util.MonbulkEnums;
import daris.Monbulk.shared.Model.IPojo;

public abstract class baseService implements iService{

	private IPojo relatedPOJO;
	private MonbulkEnums.ServiceActions serviceAction;
	
	public baseService(MonbulkEnums.ServiceActions action)
	{
		this.serviceAction = action; 
	}
	public IPojo getPOJO(){return this.relatedPOJO;}
	public void setPOJO(IPojo tmpPojo){this.relatedPOJO = tmpPojo;}
	
	public MonbulkEnums.ServiceActions getServiceAction()
	{
		return serviceAction;
	}
}
