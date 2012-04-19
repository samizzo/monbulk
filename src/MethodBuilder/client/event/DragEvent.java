package daris.Monbulk.MethodBuilder.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class DragEvent extends GwtEvent<DragEventHandler>{ 
	
	public static Type<DragEventHandler> TYPE = new Type<DragEventHandler>();
	private final String Id;
	private final String Name;
	private final int Index;
	
	public DragEvent(String tmpName, String tmpID,int index)
	{
		this.Id = tmpID;
		this.Name = tmpName;
		this.Index = index;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DragEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	public String getId(){return Id;}
	public String getName(){return Name;}
	public int getIndex(){return Index;}
	
	@Override
	protected void dispatch(DragEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onDrag(this);
	}
}
