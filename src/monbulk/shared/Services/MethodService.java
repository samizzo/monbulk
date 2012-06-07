package monbulk.shared.Services;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoMethodComplete;
import monbulk.shared.util.MonbulkEnums.ServiceNames;

public abstract class MethodService implements iService{

	public static MethodService get()
	{
		try
		{
			MethodService service = (MethodService)ServiceRegistry.getService(ServiceNames.Methods);
			return service;
		}
		catch (ServiceRegistry.ServiceNotFoundException e) 
		{
			GWT.log(e.toString());
		}
		
		return null;
	}
	protected enum MethodRequest
	{
		List,
		Describe,
		Create,
		Edit,
		Find,
		CheckExists,
		Delete,
		InUse
	}

	public interface MethodServiceHandler
	{
		public void onReadMethodList(ArrayList<pojoMethod> arrMethods);
		public void onReadMethod(pojoMethodComplete method);
		public void onMethodInUse(Boolean isInUse);
	}
	public interface MethodUpdateHandler
	{
		public void onUpdateMethod(String response);
		public void checkExists(Boolean response);
		public void onDelete(String Response);
	}
	@Override
	public ServiceNames getServiceType() {
		// TODO Auto-generated method stub
		return ServiceNames.Methods;
		
	}

	
	/**
	 * Returns a list of all available Methods
	 * @param handler
	 */
	public abstract void getMethodList(MethodServiceHandler handler);
	
		
	
	/**
	 * Returns a specific Method type
	 * @param ID
	 * @param handler
	 */
	public abstract void getMethod(String ID, MethodServiceHandler handler);
	
	public abstract void checkExists(String Name, MethodUpdateHandler handler);
	
	public abstract void createOrUpdate(String xml, MethodUpdateHandler handler);
	
	public abstract void deleteMethod(String ID, MethodUpdateHandler handler);
	
	public abstract void checkUsage(String ID,MethodServiceHandler handler);
}
