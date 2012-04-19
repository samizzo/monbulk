package monbulk.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class CloseWindowEvent extends GwtEvent<CloseWindowEventHandler> {
	public static Type<CloseWindowEventHandler> TYPE = new Type<CloseWindowEventHandler>();
	private final String id;
	  
	 public CloseWindowEvent (String id) {
	    this.id = id;
	  }
	  
	 public String getId() { return id; }
	 
	 @Override
	 public Type<CloseWindowEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	 @Override
	protected void dispatch(
			monbulk.client.event.CloseWindowEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onCloseWindow(this);
		
	}
}