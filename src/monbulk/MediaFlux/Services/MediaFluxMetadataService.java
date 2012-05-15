package monbulk.MediaFlux.Services;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

import arc.mf.client.Output;
import arc.mf.client.ServiceContext;
import arc.mf.client.xml.XmlDoc;
import arc.mf.client.xml.XmlElement;
import arc.mf.session.ServiceResponseHandler;
import arc.mf.session.Session;

import monbulk.shared.Services.*;
import monbulk.shared.util.XmlHelper;

public class MediaFluxMetadataService extends MetadataService
{
	private boolean m_testMode = false;
	
	private static class MetadataResponseHandler implements ServiceResponseHandler
	{
		private Object m_handler = null;
		private String m_metadataName = "";
		private Metadata m_metadata = null;

		public MetadataResponseHandler(Object handler, String name)
		{
			m_metadataName = name;
			m_handler = handler;
		}

		public MetadataResponseHandler(Object handler)
		{
			this(handler, (String)null);
		}

		public MetadataResponseHandler(Object handler, Metadata metadata)
		{
			this(handler, (String)null);
			m_metadata = metadata;
		}

		public void processResponse(XmlElement xe, List<Output> outputs) throws Throwable
		{
			if (xe == null)
			{
				// Error processing request.
				GWT.log("Error processing request, response is null");
				return;
			}

			if (m_handler instanceof GetMetadataTypesHandler)
			{
				// Read list of metadata types.
				GetMetadataTypesHandler handler = (GetMetadataTypesHandler)m_handler;

				/*
				 <response>
				 	<type>asset</type>
				 	<type>hfi-bruker-series</type>
				 	<type>hfi-bruker-study</type>
				 	<type>hfi.pssd.EAE.microscopy</type>
				 	..etc..
				 </response>
				 */
				List<String> types = null;

				if (xe != null)
				{
					types = xe.values("type");
				}
	
				handler.onGetMetadataTypes(types);
			}
			else if (m_handler instanceof GetMetadataHandler)
			{
				// Read a specific metadata entry.
				GetMetadataHandler handler = (GetMetadataHandler)m_handler;

				Metadata metadata = MediaFluxMetadataAdapter.createMetadata(xe);
				handler.onGetMetadata(metadata);
			}
			else if (m_handler instanceof MetadataExistsHandler)
			{
				// Check if a specific metadata entry exists.
				MetadataExistsHandler handler = (MetadataExistsHandler)m_handler;

				/*
				 	<response>
				 		<exists type="blah">false</exists>
				 	</response>
				 */
				XmlElement existsElement = xe.element("exists");
				String metadataName = existsElement.value("@type");
				Boolean exists = Boolean.parseBoolean(existsElement.value());
				handler.onMetadataExists(metadataName, exists);
			}
			else if (m_handler instanceof CreateMetadataHandler)
			{
				CreateMetadataHandler handler = (CreateMetadataHandler)m_handler;
				Metadata metadata = new Metadata(m_metadataName, "", "");
				handler.onCreateMetadata(metadata, true);
			}
			else if (m_handler instanceof DestroyMetadataHandler)
			{
				DestroyMetadataHandler handler = (DestroyMetadataHandler)m_handler;
				handler.onDestroyMetadata(m_metadataName, true);
			}
			else if (m_handler instanceof UpdateMetadataHandler)
			{
				UpdateMetadataHandler handler = (UpdateMetadataHandler)m_handler;
				handler.onUpdateMetadata(m_metadata, true);
			}
			else if (m_handler instanceof RenameMetadataHandler)
			{
				RenameMetadataHandler handler = (RenameMetadataHandler)m_handler;
				handler.onRenameMetadata(true);
			}
			else
			{
				GWT.log("No handler found for metadat service, ignoring response");
			}
		}
	}

	public MediaFluxMetadataService()
	{
		this(false);
	}
	
	public MediaFluxMetadataService(boolean testMode)
	{
		m_testMode = testMode;
	}

	// Returns whether the named metadata exists.
	public void metadataExists(String name, MetadataExistsHandler handler)
	{
		final MetadataResponseHandler h = new MetadataResponseHandler(handler);

		if (m_testMode)
		{
			runTestMode("metadata-" + name + ".xml", h, true, name);
		}
		else
		{
			Session.execute(
					new ServiceContext("MediaFluxMetadataService.getMetadataTypes"),
					"asset.doc.type.exists",
					"<type>" + name + "</type>",
					null,
					0,
					h,
					true);
		}
	}

	// Returns a list of all available metadata types.
	public void getMetadataTypes(GetMetadataTypesHandler handler)
	{
		final MetadataResponseHandler h = new MetadataResponseHandler(handler);

		if (m_testMode)
		{
			runTestMode("metadata-list.xml", h, false, null);
		}
		else
		{
			Session.execute(
					new ServiceContext("MediaFluxMetadataService.getMetadataTypes"),
					"asset.doc.type.list",
					"",
					null,
					0,
					h,
					true);
		}
	}
	
	// Returns a specific metadata type.
	public void getMetadata(String name, GetMetadataHandler handler)
	{
		MetadataResponseHandler h = new MetadataResponseHandler(handler);
		
		if (m_testMode)
		{
			try
			{
				runTestMode("metadata-" + name + ".xml", h, false, name);
			}
			catch (Throwable t)
			{
				GWT.log(t.getMessage());
			}
		}
		else
		{
			Session.execute(
					new ServiceContext("MediaFluxMetadataService.getMetadata"),
					"asset.doc.type.describe",
					"<type>" + name + "</type>",
					null,
					0,
					h,
					true);
		}
	}

	// Creates new metadata from the specified object.
	public void createMetadata(String name, CreateMetadataHandler handler)
	{
		MetadataResponseHandler h = new MetadataResponseHandler(handler, name);
		Session.execute(
					new ServiceContext("MediaFluxMetadataService.createMetadata"),
					"asset.doc.type.create",
					"<type>" + name + "</type><definition/>",
					null,
					0,
					h,
					true);
	}

	// Updates the specified metadata.
	public void updateMetadata(Metadata metadata, UpdateMetadataHandler handler)
	{
		MetadataResponseHandler h = new MetadataResponseHandler(handler, metadata);
		Session.execute(
					new ServiceContext("MediaFluxMetadataService.updateMetadata"),
					"asset.doc.type.update",
					metadata.getXml(),
					null,
					0,
					h,
					true);
	}

	// Renames the specified metadata.
	public void renameMetadata(String oldName, String newName, RenameMetadataHandler handler)
	{
		StringBuilder sb = new StringBuilder();
		XmlHelper.addTagWithValue(sb, "new-type", newName);
		XmlHelper.addTagWithValue(sb, "old-type", oldName);
		MetadataResponseHandler h = new MetadataResponseHandler(handler);
		Session.execute(
					new ServiceContext("MediaFluxMetadataService.renameMetadata"),
					"asset.doc.type.rename",
					sb.toString(),
					null,
					0,
					h,
					true);
	}

	// Destroys the specified metadata.
	public void destroyMetadata(String name, DestroyMetadataHandler handler)
	{
		MetadataResponseHandler h = new MetadataResponseHandler(handler, name);
		Session.execute(
					new ServiceContext("MediaFluxMetadataService.destroyMetadata"),
					"asset.doc.type.destroy",
					"<type>" + name + "</type>",
					null,
					0,
					h,
					true);
	}
	
	private void runTestMode(String responseFile, final MetadataResponseHandler handler, final Boolean existsTest, final String name)
	{
		try
		{
			String url = GWT.getModuleBaseURL() + "../testdata/" + responseFile;
			RequestBuilder b = new RequestBuilder(RequestBuilder.GET, url);
			b.sendRequest("", new RequestCallback()
				{
					public void onResponseReceived(Request req, Response resp)
					{
						try
						{
							if (resp.getStatusCode() == 404 && !existsTest)
							{
								// Try again using data that should be there.
								String url = GWT.getModuleBaseURL() + "../testdata/metadata-asset.xml";
								RequestBuilder b = new RequestBuilder(RequestBuilder.GET, url);
								b.sendRequest("", this);
							}
							else
							{
								String s = resp.getText();
								if (existsTest)
								{
									// Hack in an exists result.
									s = "<response><exists type=\"" + name + "\">" + (resp.getStatusCode() == 404 ? "false" : "true") + "</exists></response>";
								}

								XmlElement e = XmlDoc.parse(s);
								handler.processResponse(e, null);
							}
						}
						catch (Throwable t)
						{
							GWT.log(t.getMessage());
						}
					}
					
					public void onError(Request req, Throwable t)
					{
						GWT.log("onError");
					}
				});
		}
		catch (Throwable t)
		{
			GWT.log(t.getMessage());
		}
	}
}
