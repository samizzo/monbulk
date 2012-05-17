package monbulk.MethodBuilder.client;

import monbulk.MetadataEditor.MetadataList;
import monbulk.shared.widgets.Window.IWindow;
import monbulk.shared.widgets.Window.OkCancelWindow;

public class MetaDataSelector extends OkCancelWindow implements IWindow {
	private MetadataList m_metadataList;
	public MetaDataSelector(String windowId, String windowTitle) {
		super(windowId, windowTitle);
		m_metadataList = new MetadataList();
		this.setWidget(m_metadataList);
		//this.
	}

}
