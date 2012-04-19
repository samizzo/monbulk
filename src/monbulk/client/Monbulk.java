package monbulk.client;

import arc.mf.client.RemoteServer;
import arc.mf.session.ErrorDialog;
import arc.mf.session.Session;
import arc.mf.session.SessionHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import monbulk.MediaFlux.Services.MediaFluxServices;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Monbulk implements EntryPoint
{
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		RemoteServer.SVC_URL = "http://medimage.versi.edu.au:443" + RemoteServer.SVC_URL;
		Session.setAutoLogonCredentials("system", "manager", "change_me");
		Session.initialize(new SessionHandler()
		{
			@Override
			public void sessionCreated(boolean initial)
			{
			}

			@Override
			public void sessionExpired()
			{
			}

			@Override
			public void sessionTerminated()
			{
			}
		},
		null,
		new ErrorDialog()
		{
			public void setVersionHTML(String version)
			{
			}
			
			public void show(String context,String service,String args,int nbOutputs,Throwable se)
			{
				String msg = se.toString();
				if (se.getCause() != null)
				{
					msg = se.getCause().toString();
				}
				
				Window.alert(context + ": " + msg);
			}
		},
		null);

		MediaFluxServices.registerMediaFluxServices();
		HandlerManager eventBus = new HandlerManager(null);
		//MonbulkRegistry.initialise(serviceFascade);
		appManager appViewer = new appManager(eventBus);
		appViewer.go(RootPanel.get());
	}
}
