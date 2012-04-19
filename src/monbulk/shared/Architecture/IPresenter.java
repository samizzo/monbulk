package monbulk.shared.Architecture;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasWidgets;

import monbulk.shared.Events.DragEvent;
import monbulk.shared.Form.FormBuilder;


public interface IPresenter {
	 public abstract void go(final HasWidgets container);
	 public void FireDragEvent(DragEvent e); 
	 public interface DockedPresenter extends IPresenter {
		    public ArrayList<String> GetDocks();
		    public abstract void go(final HasWidgets bodyContainer,final HasWidgets dockContainer);
		    public abstract void go(final HasWidgets bodyContainer,final HasWidgets dockContainer,HasWidgets navContainer);
		    
	 }
	 public interface FormPresenter extends DockedPresenter
	 {
			public String UpdateValue(FormBuilder someFormData);
			public void FormComplete(String FormName, String CommandName);
			
			public void ModelUpdate(String ServiceName);
	 }	 
}
