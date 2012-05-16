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

	/**
	 * Returns a list of all available metadata types.
	 * @param handler
	 */
	public abstract void getMetadataTypes(GetMetadataTypesHandler handler);

	public interface GetMetadataHandler
	{
		public void onGetMetadata(Metadata metadata);
	}

	/**
	 * Returns a specific metadata type.
	 * @param name
	 * @param handler
	 */
	public abstract void getMetadata(String name, GetMetadataHandler handler);

	public interface MetadataExistsHandler
	{
		public void onMetadataExists(String metadataName, boolean exists);
	}

	/**
	 * Returns whether the named metadata exists.
	 * @param name
	 * @param handler
	 */
	public abstract void metadataExists(String name, MetadataExistsHandler handler);

	public interface CreateMetadataHandler
	{
		public void onCreateMetadata(Metadata metadata, boolean success);
	}

	/**
	 * Creates new metadata from the specified object.
	 * @param name
	 * @param handler
	 */
	public abstract void createMetadata(String name, CreateMetadataHandler handler);

	public interface DestroyMetadataHandler
	{
		public void onDestroyMetadata(String name, boolean success);
	}

	/**
	 * Destroys the specified metadata.
	 * @param name
	 * @param handler
	 */
	public abstract void destroyMetadata(String name, DestroyMetadataHandler handler);

	public interface UpdateMetadataHandler
	{
		public void onUpdateMetadata(Metadata metadata);
	}

	/**
	 * Updates the specified metadata on the server.
	 * @param metadata
	 * @param handler
	 */
	public abstract void updateMetadata(Metadata metadata, UpdateMetadataHandler handler);

	public interface RenameMetadataHandler
	{
		public void onRenameMetadata();
	}

	/**
	 * Renames the specified metadata.
	 * @param oldName
	 * @param newName
	 * @param handler
	 */
	public abstract void renameMetadata(String oldName, String newName, RenameMetadataHandler handler);

	public final ServiceNames getServiceType()
	{
		return ServiceNames.MetaData;
	}
}
