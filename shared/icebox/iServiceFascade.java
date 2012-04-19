package daris.Monbulk.shared.icebox;

import daris.Monbulk.shared.Services.iService;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceNames;


public interface iServiceFascade {

	public iService getServiceForNameandAction(ServiceNames serviceName, ServiceActions ServiceAction);
	
}
