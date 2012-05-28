package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;

import monbulk.client.desktop.Desktop;
import monbulk.shared.Services.Metadata;
import monbulk.shared.Services.Metadata.DocumentElement;
import monbulk.shared.widgets.Window.OkCancelWindow.*;

public class DocumentElementPanel extends ElementPanel
{
	private static DocumentElementPanelUiBinder uiBinder = GWT.create(DocumentElementPanelUiBinder.class);
	interface DocumentElementPanelUiBinder extends UiBinder<Widget, DocumentElementPanel> { }

	@UiField TextBox m_reference;
	@UiField Button m_select;
	@UiField TextBox m_referenceName;

	public DocumentElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void set(Metadata.Element element)
	{
		super.set(element);
		
		Metadata.DocumentElement docElement = (Metadata.DocumentElement)element;
		
		String referenceValue = docElement.getReferenceValue();
		if (referenceValue != null && referenceValue.length() > 0)
		{
			m_reference.setValue(referenceValue);
			m_referenceName.setValue(docElement.getReferenceName());
		}
		else
		{
			m_reference.setValue("");
			m_referenceName.setValue("");
		}
	}
	
	public void update(Metadata.Element element)
	{
		Metadata.DocumentElement docElement = (Metadata.DocumentElement)element;
		docElement.setReferenceValue(m_reference.getValue());
		docElement.setReferenceName(m_referenceName.getValue());
		docElement.setReferenceType(DocumentElement.ReferenceType.Document);
	}
	
	public Metadata.ElementTypes getType()
	{
		return Metadata.ElementTypes.Reference;
	}
	
	@UiHandler("m_select")
	void onSelectClicked(ClickEvent event)
	{
		// Select was clicked, so show the metadata selection window.

		Desktop d = Desktop.get();

		final MetadataSelectWindow m = (MetadataSelectWindow)d.getWindow("MetadataSelectWindow");
		m.setSelectedMetadata(m_reference.getValue());
		m.setOkCancelHandler(new OkCancelHandler()
		{
			public void onOkCancelClicked(OkCancelHandler.Event eventType)
			{
				if (eventType == OkCancelHandler.Event.Ok)
				{
					// Ok was pressed so update the text box with the
					// user's selection.
					String metadata = m.getSelectedMetadataName();
					m_reference.setValue(metadata);
				}
			}
		});

		d.show(m, true);
	}
}
