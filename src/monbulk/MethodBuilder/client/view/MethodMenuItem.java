package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;

import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IView;
import monbulk.shared.Events.DragEvent;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMethod;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class MethodMenuItem extends Composite implements HasText, IView {

	private static MethodMenuItemUiBinder uiBinder = GWT
			.create(MethodMenuItemUiBinder.class);

	interface MethodMenuItemUiBinder extends UiBinder<Widget, MethodMenuItem> {
	}

	@UiField
	Label lblMenuMethodName;
	
	@UiField
	PushButton btnMenuEditMethod;
	
	@UiField
	PushButton btnMenuCloneMethod;
	
	@UiField
	HorizontalPanel _hlContainer;
	
	private String _MethodID;
	private String _MethodName;
	private IPresenter _presenter;
	private int _Index;
	private pojoMethod _DataObject;
	
	private final String ActiveStyleName = "hlActiveMenuContainer";
	private final String PassiveStyleName = "hlPassiveMenuContainer";
	public MethodMenuItem(pojoMethod tmpMethod, int Index) {
		initWidget(uiBinder.createAndBindUi(this));
		try
		{
		
		this._Index = Index;
		this._DataObject = tmpMethod;
		this.setText(this._DataObject.getFieldVale(pojoMethod.MethodNameField));
		
		this.setID(this._DataObject.getMethodID());
		//this._hlContainer.setStyleName(PassiveStyleName);
		//Window.alert("Do we construct");
		}
		catch(Exception ex)
		{
			GWT.log("Error Occurs @ MethodMenuItem.Construct" + ex.getMessage());
		}
	}

	@UiHandler("btnMenuEditMethod")
	void onClick(ClickEvent e) {
		
		this._presenter.FireDragEvent(new DragEvent(this._MethodName,"EditMethod",this._Index,(IPojo)this._DataObject));
	}
	@UiHandler("btnMenuCloneMethod")
	void onClick2(ClickEvent e) {
		
		this._presenter.FireDragEvent(new DragEvent(this._MethodName,"CloneMethod",this._Index,(IPojo)this._DataObject));
	}
	public void setText(String text) {
		if(text!=null)
		{
			lblMenuMethodName.setText(text);
			this._MethodName = text;
		}
		else
		{
			lblMenuMethodName.setText("Unknown");
			this._MethodName = "Unknown";
		}
	}

	public String getText() {
		return lblMenuMethodName.getText();
	}
	
	public void setID(String ID)
	{
		if(ID!=null)
		{
			this._MethodID = ID;
		}
		else
		{
			this._MethodID = "";
		}
	}
	public String getID()
	{
		return this._MethodID;
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		this._presenter = presenter;
		
	}

	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		this._hlContainer.setStyleName(PassiveStyleName);
	}
	public void setActive(String Name)
	{
		if(this._MethodName == Name)
		{
		//	this._hlContainer.setStyleName(ActiveStyleName);
			
		}
		else
		{
			//this._hlContainer.setStyleName(PassiveStyleName);
		}
	}
}
