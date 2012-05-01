package monbulk.MetadataEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Composite;

public class AttributesEditor extends Composite
{
	private static AttributesEditorUiBinder uiBinder = GWT.create(AttributesEditorUiBinder.class);
	interface AttributesEditorUiBinder extends UiBinder<Widget, AttributesEditor> {	}

	public AttributesEditor()
	{
		initWidget(uiBinder.createAndBindUi(this));
		setWidth("300px");
		setHeight("300px");
	}
}
