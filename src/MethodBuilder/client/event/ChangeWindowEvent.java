package daris.Monbulk.MethodBuilder.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class ChangeWindowEvent extends GwtEvent<ChangeWindowEventHandler> {
	public static Type<ChangeWindowEventHandler> TYPE = new Type<ChangeWindowEventHandler>();
	private final String id;
	 private final String Change;
	 public ChangeWindowEvent (String id, String Change) {
	    this.id = id;
	    this.Change = Change;
	  }
	  
	 public String getId() { return id; }
	 public String getChange() { return Change; }
	 
	 @Override
	 public Type<ChangeWindowEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	@Override
	protected void dispatch(ChangeWindowEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onChangeWindow(this);
	}
}