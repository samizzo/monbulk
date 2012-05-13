package monbulk.shared.Services;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;

import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.iModel;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.util.MonbulkEnums.*;

public abstract class MetadataService implements iService
{
	// Convenience function to get a metadata service.
	public static MetadataService get()
	{
		try
		{
			MetadataService service = (MetadataService)ServiceRegistry.getService(ServiceNames.MetaData);
			return service;
		}
		catch (ServiceRegistry.ServiceNotFoundException e) 
		{
			GWT.log(e.toString());
		}
		
		return null;
	}

	public interface GetMetadataTypesHandler
	{
		public void onGetMetadataTypes(List<String> types);
	}
	
	public interface GetMetadataHandler
	{
		public void onGetMetadata(Metadata metadata);
	}
	
	public interface MetadataExistsHandler
	{
		public void onMetadataExists(String metadataName, boolean exists);
	}
	
	public interface CreateMetadataHandler
	{
		public void onCreateMetadata(Metadata metadata, boolean success);
	}
	
	public interface DestroyMetadataHandler
	{
		public void onDestroyMetadata(String name, boolean success);
	}
	
	public interface UpdateMetadataHandler
	{
		public void onUpdateMetadata(Metadata metadata, boolean success);
	}
	
	/**
	 * Returns a list of all available metadata types.
	 * @param handler
	 */
	public abstract void getMetadataTypes(GetMetadataTypesHandler handler);
	
	/**
	 * Returns a specific metadata type.
	 * @param name
	 * @param handler
	 */
	public abstract void getMetadata(String name, GetMetadataHandler handler);
	
	/**
	 * Returns whether the named metadata exists.
	 * @param name
	 * @param handler
	 */
	public abstract void metadataExists(String name, MetadataExistsHandler handler);

	/**
	 * Creates new metadata from the specified object.
	 * @param name
	 * @param handler
	 */
	public abstract void createMetadata(String name, CreateMetadataHandler handler);
	
	/**
	 * Destroys the specified metadata.
	 * @param name
	 * @param handler
	 */
	public abstract void destroyMetadata(String name, DestroyMetadataHandler handler);
	
	/**
	 * Updates the specified metadata on the server.
	 * @param metadata
	 * @param handler
	 */
	public abstract void updateMetadata(Metadata metadata, UpdateMetadataHandler handler);

	public final ServiceNames getServiceType()
	{
		return ServiceNames.MetaData;
	}
}
