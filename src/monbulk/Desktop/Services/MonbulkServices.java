package monbulk.Desktop.Services;

import monbulk.shared.Services.ServiceRegistry;

public class MonbulkServices
{
	public static void registerMonbulkServices()
	{
		ServiceRegistry.registerService(new MonbulkMetadataService());
		
	}
}
