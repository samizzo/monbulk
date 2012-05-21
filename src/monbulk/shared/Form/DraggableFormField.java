package monbulk.shared.Form;

import monbulk.MethodBuilder.client.view.DraggableCellWidget;
import monbulk.shared.Model.IPojo;
import monbulk.shared.util.GWTLogger;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;


public class DraggableFormField extends FormField implements iFormField {

	public DraggableCellWidget _dragWidg;
	
	public DraggableFormField(String FormName, int Index, Boolean canAdd, IPojo dataItem) {
		super(FormName, "Foreign");
		
		_dragWidg = new DraggableCellWidget(Index, canAdd, dataItem);
		this.hasValue=true;
		this.isStatic=false;
		this._FieldVaLueWidget = _dragWidg;
		Label FieldNameLabel = new Label();
		FieldNameLabel.setText("");
		this._ValidationWidget = new Label();
		this.returnWidget = new FormWidget(this.FieldName);
		
		this.returnWidget.setLabelWidget(FieldNameLabel);
		this.returnWidget.setFormWidget(_FieldVaLueWidget);
		this.returnWidget.setValidWidget(_ValidationWidget);
		//this._FieldVaLueWidget 
	}
	public void setDragStyleName(String StyleName)
	{
		this._FieldVaLueWidget.setStyleName(StyleName);
		
	}
	public void setParentStyleName(String StyleName)
	{
		this.returnWidget.setStyleName(StyleName);
	}
	public void attachExpandHandler(ClickHandler handler)
	{
		try
		{
		DraggableCellWidget selectButton = (DraggableCellWidget) this._FieldVaLueWidget;
		selectButton.AttachExpandHandler(handler);
		
		}
		catch(java.lang.ClassCastException ex)
		{
			GWTLogger.Log("Could not cast Field Widget to PushButton:" + this.GetFieldName(), "ButtonField", "attachClickHandler", "49");
		}
	
	}

}
