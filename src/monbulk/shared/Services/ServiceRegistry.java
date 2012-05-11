package monbulk.shared.Services;

import java.util.ArrayList;
import monbulk.shared.util.MonbulkEnums;

public class ServiceRegistry
{
	@SuppressWarnings("serial")
	public static class ServiceNotFoundException extends Exception
	{
		private MonbulkEnums.ServiceNames m_serviceType;

		public ServiceNotFoundException(MonbulkEnums.ServiceNames serviceType)
		{
			m_serviceType = serviceType;
		}
		
		public String toString()
		{
			return "Service " + m_serviceType.toString() + " not found";
		}
	}
	
	private static ArrayList<iService> s_services = new ArrayList<iService>();

	/**
	 * Registers a service.
	 * 
	 * NOTE: There is currently no real support for
	 * services of the same type.  If multiple services of the same type
	 * are added, when a service is requested, only the first one found
	 * will be returned.
	 * 
	 * We could support multiple services of the same type by associating
	 * some sort of type data with the service object, or perhaps a string
	 * name.  A client would then request e.g. a MetadataService (which
	 * would return any (all?) metadata service) or a specific service, e.g.
	 * MediaFluxMetadataService.
	 *  
	 * @param service
	 */
	public static void registerService(iService service)
	{
		s_services.add(service);
	}

	public static iService getService(MonbulkEnums.ServiceNames serviceType) throws ServiceNotFoundException
	{
		for (iService service : s_services)
		{
			if (service.getServiceType() == serviceType)
			{
				return service;
			}
		}

		// Couldn't find requested service.
		throw new ServiceNotFoundException(serviceType);
	}
}
