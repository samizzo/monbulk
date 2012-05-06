package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;
import java.util.List;

import arc.gui.gwt.widget.label.Label;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.LayoutPanel;

import monbulk.MetadataEditor.MetadataList;
import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IPresenter.DockedPresenter;
import monbulk.shared.Architecture.IView.IDockView;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;
import monbulk.shared.Services.MetadataService;
import monbulk.shared.Services.MetadataService.GetMetadataTypesHandler;

public class metaDataListIntegrated extends MetadataList implements IDockView {

	private DockedPresenter _presenter;
	private final FlexTable _extendedMetaDataList;
	
	public metaDataListIntegrated()
	{
		super();
		super.hideListBox(true);
		super.setShowRemove(false);
		_extendedMetaDataList = new FlexTable();
		_extendedMetaDataList.setStyleName("method-mdList");
		_extendedMetaDataList.setHeight("400px");
		_extendedMetaDataList.setWidth("200px");
		_extendedMetaDataList.getElement().setAttribute("style", "z-index:1001;position:absolute;right:500px;");
		LayoutPanel tmpPanel = super.getLayout();
		super.addWidget(_extendedMetaDataList);
		
		populateListBox();
	}
	@Override
	public void populateListBox()
	{
		if(_extendedMetaDataList != null)
		{
			_extendedMetaDataList.clear();
			MetadataService service = MetadataService.get();
			if (service != null)
			{
				service.getMetadataTypes(new GetMetadataTypesHandler()
				{
					// Callback for reading a list of metadata.
					public void onGetMetadataTypes(List<String> types)
					{
						m_metadataTypes = types;
											
						if (types != null)
						{
							// Add all items.
							int selectionIndex = -1;
							
							for (int i = 0; i < types.size(); i++)
							{
								String name = types.get(i);
								Label tmpLabel = new Label();
								tmpLabel.setText(name);
								_extendedMetaDataList.setWidget(i, 0, tmpLabel);
								//m_metadataListBox.addItem(name);
								//GWT.log("Rows added" + _extendedMetaDataList.getRowCount());
								
							}
							
							//Window.alert("Rows added" + _extendedMetaDataList.getRowCount());
						}
					}
				});
			}
			
			
		}
	}
	@Override
	public void onKeyUp(KeyUpEvent event)
	{

			int keyCode = event.getNativeKeyCode();
			if ((keyCode >= 'a' && keyCode <= 'z') ||
				(keyCode >= 'A' && keyCode <= 'Z' ) ||
				(keyCode == '.') ||
				(keyCode == KeyCodes.KEY_BACKSPACE) ||
				(keyCode == KeyCodes.KEY_DELETE))
			{
				// Filter list using filter text.
				String filterText = super.getTextBoxValue();
				_extendedMetaDataList.clear();
				if (m_metadataTypes != null)
				{
					for (int i = 0; i < m_metadataTypes.size(); i++)
					{
						String m = m_metadataTypes.get(i);
						
						if (m.indexOf(filterText) >= 0 || filterText.length() == 0)
						{
							Label tmpLabel = new Label();
							tmpLabel.setText(m);
							_extendedMetaDataList.setWidget(i, 0, tmpLabel);
						}
					}
				}
			}
	}
	@Override
	public void setPresenter(IPresenter presenter) {
		this._presenter = (DockedPresenter)presenter;

	}

	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTabData(StackedCategories someData, String TabName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPojoData(ArrayList<IPojo> someData, String TabName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPresenter(DockedPresenter presenter) {
		this._presenter = presenter;

	}

}
