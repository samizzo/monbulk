package monbulk.MediaFlux.Services;

import java.util.List;

import com.google.gwt.core.client.GWT;

import arc.mf.client.Output;
import arc.mf.client.ServiceContext;
import arc.mf.client.xml.XmlElement;
import arc.mf.session.ServiceResponseHandler;
import arc.mf.session.Session;

import monbulk.shared.Services.UserService;
import monbulk.shared.Services.User;

public class MediaFluxUserService extends UserService
{
	private class UserResponseHandler implements ServiceResponseHandler
	{
		private Object m_handler = null;

		public UserResponseHandler(Object handler)
		{
			m_handler = handler;
		}

		public void processResponse(XmlElement xe, List<Output> outputs) throws Throwable
		{
			if (m_handler instanceof GetUserHandler)
			{
				GetUserHandler handler = (GetUserHandler)m_handler;
				String name = xe.value("user/@user");
				String domain = xe.value("user/@domain");
				User user = new User(name, domain);
				List<String> roles = xe.values("user/role[@type='role']");
				user.addRoles(roles);
				handler.onGetUser(user);
			}
			else
			{
				GWT.log("No handler found for user service, ignoring response (" +  xe.toString() + ")");
			}
		}
	}

	public void getUser(String name, String domain, GetUserHandler handler)
	{
		UserResponseHandler h = new UserResponseHandler(handler);
		Session.execute(
				new ServiceContext("MediaFluxUserService.getUser"),
				"user.describe",
				"<user>" + name + "</user><domain>" + domain + "</domain><permissions>true</permissions>",
				null,
				0,
				h,
				true);
	}
}
