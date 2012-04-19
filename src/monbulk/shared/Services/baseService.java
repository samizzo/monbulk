package monbulk.shared.Services;

import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.Model.IPojo;

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
