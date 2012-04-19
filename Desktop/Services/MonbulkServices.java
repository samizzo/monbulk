package daris.Monbulk.Desktop.Services;

import daris.Monbulk.shared.Services.ServiceRegistry;

public class MonbulkServices
{
	public static void registerMonbulkServices()
	{
		ServiceRegistry.registerService(new MonbulkMetadataService());
		
	}
}
