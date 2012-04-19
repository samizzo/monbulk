package monbulk.MetadataEditor;

import com.google.gwt.user.client.ui.Composite;
import monbulk.shared.Services.Metadata;

public abstract class ElementPanel extends Composite
{
	protected Metadata.Element m_element = null;

	// Interface to set UI from a metadata element.
	public void set(Metadata.Element element)
	{
		m_element = element;
	}
	
	// Interface to update a metadata element from UI.
	public abstract void update(Metadata.Element element);

	// Returns the type that this panel is for.
	public abstract Metadata.ElementTypes getType();
}
