package monbulk.MediaFlux.Services;

import monbulk.shared.Services.ServiceRegistry;

public class MediaFluxServices
{
	public static void registerMediaFluxServices()
	{
		registerMediaFluxServices(false);
	}

	/*
	 * Registers services for reading data from mediaflux.  If testMode is
	 * true, the service will not contact the server and instead only
	 * return test data.
	 */
	public static void registerMediaFluxServices(boolean testMode)
	{
		ServiceRegistry.registerService(new MediaFluxMetadataService(testMode));
		ServiceRegistry.registerService(new MediaFluxDictionaryService());
		ServiceRegistry.registerService(new mfMethodService());
		ServiceRegistry.registerService(new MediaFluxUserService());
	}
}
