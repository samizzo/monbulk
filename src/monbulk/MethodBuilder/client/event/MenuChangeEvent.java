package monbulk.MethodBuilder.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class MenuChangeEvent extends GwtEvent<MenuChangeEventHandler>{

	public static Type<MenuChangeEventHandler> TYPE = new Type<MenuChangeEventHandler>(); 
	
	
	private final String id;
	public MenuChangeEvent(String tmpID)
	{
		this.id = tmpID;
	}
	@Override
	public Type<MenuChangeEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	public String getId() { return id; }
	
	@Override
	protected void dispatch(MenuChangeEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onMenuChange(this);
	}

}
