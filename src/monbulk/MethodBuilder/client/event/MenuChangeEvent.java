package monbulk.MethodBuilder.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class MenuChangeEvent extends GwtEvent<MenuChangeEventHandler>{

	public static Type<MenuChangeEventHandler> TYPE = new Type<MenuChangeEventHandler>(); 
	
	
	private final String id;
	private final String newID;
	public MenuChangeEvent(String tmpID,String newID)
	{
		this.id = tmpID;
		this.newID = newID;
	}
	@Override
	public Type<MenuChangeEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	public String getId() { return id; }
	public String getNewID(){return newID;}
	@Override
	protected void dispatch(MenuChangeEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onMenuChange(this);
	}

}
