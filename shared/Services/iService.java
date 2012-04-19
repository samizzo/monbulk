package daris.Monbulk.shared.Services;

import java.util.ArrayList;

import daris.Monbulk.shared.util.MonbulkEnums;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.Architecture.IPresenter;
import daris.Monbulk.shared.Architecture.iModel;
import daris.Monbulk.shared.Form.iFormField;
import daris.Monbulk.shared.Model.IPojo;

/*
 * Services provide an interface to do two things: send data and retrieve data.
 */
public interface iService
{
	// TODO: We could use some kind of rtti-like mechanism
	// where we return a type object instead of an enum.
	public MonbulkEnums.ServiceNames getServiceType();

	// -- Deprecated ------------
	public abstract void sendServiceRequest(ArrayList<iFormField> Parameters, ServiceActions action, iModel responseModel);
	public abstract void sendServiceActionRequest(ArrayList<iFormField> Parameters, ServiceActions action, IPresenter responseModel);
	public abstract void sendPojoRequest(IPojo Parameters, ServiceActions action, IPresenter responseModel);
	public abstract String getServiceName();
}
