package monbulk.shared.Services;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.iModel;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoMethodComplete;
import monbulk.shared.util.MonbulkEnums.ServiceActions;
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
		Create
	}

	public interface MethodServiceHandler
	{
		public void onReadMethodList(ArrayList<pojoMethod> arrMethods);
		public void onReadMethod(pojoMethodComplete method);
	}
	@Override
	public ServiceNames getServiceType() {
		// TODO Auto-generated method stub
		return ServiceNames.Methods;
		
	}

	@Override
	public abstract void sendServiceRequest(ArrayList<iFormField> Parameters,
			ServiceActions action, iModel responseModel);

	@Override
	public abstract void sendServiceActionRequest(ArrayList<iFormField> Parameters,
			ServiceActions action, IPresenter responseModel);

	@Override
	public abstract void sendPojoRequest(IPojo Parameters, ServiceActions action,
			IPresenter responseModel);

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return "om.pssd.methods";
	}
	// Returns a list of all available Methods
	public abstract void getMethodList(MethodServiceHandler handler);
	{
		
	}
		
	// Returns a specific Method type.
	public abstract void getMethod(String ID, MethodServiceHandler handler);
	{
		
	}
}
