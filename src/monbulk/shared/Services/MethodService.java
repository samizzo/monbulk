package daris.Monbulk.shared.Services;

import java.util.ArrayList;

import daris.Monbulk.shared.Architecture.IPresenter;
import daris.Monbulk.shared.Architecture.iModel;
import daris.Monbulk.shared.Form.iFormField;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Model.pojo.pojoMethod;
import daris.Monbulk.shared.Model.pojo.pojoMethodComplete;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceNames;

public abstract class MethodService implements iService{

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
