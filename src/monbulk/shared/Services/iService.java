package monbulk.shared.Services;

import java.util.ArrayList;

import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.util.MonbulkEnums.ServiceActions;
import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.iModel;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;

/*
 * Services provide an interface to do two things: send data and retrieve data.
 */
public interface iService
{
	// TODO: We could use some kind of rtti-like mechanism
	// where we return a type object instead of an enum.
	public MonbulkEnums.ServiceNames getServiceType();
}
