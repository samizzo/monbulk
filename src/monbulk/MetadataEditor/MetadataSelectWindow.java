package monbulk.MetadataEditor;

import monbulk.shared.widgets.Window.*;

public class MetadataSelectWindow extends OkCancelWindow
{
	private MetadataList m_metadataList;
	private String m_selectedMetadata;

	public MetadataSelectWindow()
	{
		super("MetadataSelectWindow", "Metadata");

		m_metadataList = new MetadataList();
		m_metadataList.setWidth("400px");
		m_metadataList.setHeight("400px");
		m_metadataList.setShowNew(false);
		m_metadataList.setShowRefresh(true);
		m_metadataList.setShowRemove(false);
		m_metadataList.setShowSave(false);
		m_contentPanel.add(m_metadataList);
	
		m_ok.setEnabled(false);
		m_metadataList.setHandler(new MetadataList.Handler()
		{
			public void onMetadataSelected(String metadataName)
			{
				m_ok.setEnabled(true);
			}
			
			public void onRefreshList()
			{
				m_ok.setEnabled(false);
			}
			
			public void onRemoveMetadata(String metadataName)
			{
				// Remove button is hidden.
			}
			
			public void onNewMetadata()
			{
				// New button is hidden.
			}
		});
	}

	public String getSelectedMetadataName()
	{
		return m_metadataList.getSelectedMetadataName();
	}
	
	public void setSelectedMetadata(String name)
	{
		m_selectedMetadata = name;
	}
	
	public void onShow()
	{
		// Refresh the metadata list.
		m_metadataList.refresh(m_selectedMetadata);
	}
	
	public void onHide()
	{
		m_selectedMetadata = "";
	}
}
