package monbulk.shared.Services;

import com.google.gwt.core.client.GWT;

import monbulk.shared.util.MonbulkEnums.*;

public abstract class UserService implements iService
{
	// Convenience function to get a UserService instance.
	public static UserService get()
	{
		try
		{
			UserService service = (UserService)ServiceRegistry.getService(ServiceNames.User);
			return service;
		}
		catch (ServiceRegistry.ServiceNotFoundException e) 
		{
			GWT.log(e.toString());
		}
		
		return null;
	}

	public interface GetUserHandler
	{
		public void onGetUser(User user);
	}

	/**
	 * Returns info about a particular user.
	 * @param userName
	 * @param handler
	 */
	public abstract void getUser(String name, String domain, GetUserHandler handler);

	public final ServiceNames getServiceType()
	{
		return ServiceNames.User;
	}
}
