package daris.Monbulk.shared.Services;

import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceNames;


public interface iServiceFascade {

	public iService getServiceForNameandAction(ServiceNames serviceName, ServiceActions ServiceAction);
	
}
