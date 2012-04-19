package monbulk.client;

import com.google.gwt.event.shared.HandlerManager;

import monbulk.shared.Services.iServiceFascade;
import monbulk.shared.Services.ServiceFactory;
import monbulk.shared.widgets.Window.WindowFactory;
import monbulk.shared.widgets.Window.iWindowRegistry;

public final class MonbulkRegistry {

private static final MonbulkRegistry instance = new MonbulkRegistry();
	
	private MonbulkRegistry(){ }
	/*
	 * Not sure if we need instances of the fascade yet - but will hold on to it for now
	 */
	private static iServiceFascade ServiceFascadeInstance;
	private static ServiceFactory ServiceFactory;
	private static iWindowRegistry _winRegistry;
	private static WindowFactory _winFactory;
	
	public static MonbulkRegistry getInstance(){
		return instance;
	}
	public static void initialise(iServiceFascade tmpFascade,iWindowRegistry tmpReg,HandlerManager eventBus)
	{
		ServiceFascadeInstance = tmpFascade;
		ServiceFactory = new ServiceFactory(ServiceFascadeInstance);
		_winRegistry = tmpReg;
		_winFactory = new WindowFactory();
		WindowFactory.registerPlatfrom(_winRegistry);
	}
	public static ServiceFactory getServiceFactory()
	{
		return ServiceFactory;
	}
	public static WindowFactory getWindowFactory()
	{
		return _winFactory;
	}
	//we possibly need a way to register the container widget interface and root panel widget
}
