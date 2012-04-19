package daris.Monbulk.client.event;

import com.google.gwt.event.shared.GwtEvent;





public class OpenWindowEvent extends GwtEvent<OpenWindowEventHandler> {
	public static Type<OpenWindowEventHandler> TYPE = new Type<OpenWindowEventHandler>();
	private final String id;
	  
	 public OpenWindowEvent (String id) {
	    this.id = id;
	  }
	  
	 public String getId() { return id; }
	 
	 @Override
	 public Type<OpenWindowEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	 @Override
	protected void dispatch(
			daris.Monbulk.client.event.OpenWindowEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onOpenWindow(this);
		
	}
}
