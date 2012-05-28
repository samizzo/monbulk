package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickEvent;

import monbulk.client.desktop.Desktop;
import monbulk.shared.Services.Element;
import monbulk.shared.Services.DocumentElement;
import monbulk.shared.widgets.Window.OkCancelWindow.*;

public class ReferenceElementPanel extends ElementPanel
{
	private static ReferenceElementPanelUiBinder uiBinder = GWT.create(ReferenceElementPanelUiBinder.class);
	interface ReferenceElementPanelUiBinder extends UiBinder<Widget, ReferenceElementPanel> { }

	@UiField TextBox m_reference;
	@UiField Button m_select;
	@UiField TextBox m_referenceName;

	public ReferenceElementPanel()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void set(Element element)
	{
		super.set(element);
		
		DocumentElement docElement = (DocumentElement)element;
		
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
	
	public void update(Element element)
	{
		DocumentElement docElement = (DocumentElement)element;
		docElement.setReferenceValue(m_reference.getValue());
		docElement.setReferenceName(m_referenceName.getValue());
		docElement.setReferenceType(DocumentElement.ReferenceType.Document);
	}
	
	public Element.ElementTypes getType()
	{
		return Element.ElementTypes.Reference;
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
