package monbulk.shared.Services;

import monbulk.shared.util.MonbulkEnums.ServiceActions;
import monbulk.shared.util.MonbulkEnums.ServiceNames;


public interface iServiceFascade {

	public iService getServiceForNameandAction(ServiceNames serviceName, ServiceActions ServiceAction);
	
}
