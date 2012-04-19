package monbulk.shared.Events;

import com.google.gwt.event.shared.GwtEvent;

import monbulk.shared.Model.IPojo;

public class DragEvent extends GwtEvent<DragEventHandler>{ 
	
	public static Type<DragEventHandler> TYPE = new Type<DragEventHandler>();
	private final String Id;
	private final String Name;
	private final IPojo draggedPOJO;
	private final int Index;
	
	public DragEvent(String tmpName, String tmpID,int index,IPojo tmpPOJO)
	{
		this.Id = tmpID;
		this.Name = tmpName;
		this.Index = index;
		this.draggedPOJO = tmpPOJO;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DragEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	public String getId(){return Id;}
	public String getName(){return Name;}
	public int getIndex(){return Index;}
	public IPojo getPojo(){return this.draggedPOJO;}
	@Override
	protected void dispatch(DragEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onDrag(this);
	}
}
