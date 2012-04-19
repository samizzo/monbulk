package daris.Monbulk.MethodBuilder.client.view;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import daris.Monbulk.shared.Events.DragEvent;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Architecture.IPresenter;
import daris.Monbulk.shared.Architecture.IView;

public class DraggableCellWidget extends Composite implements IView {

	private static DraggableCellWidgetUiBinder uiBinder = GWT
			.create(DraggableCellWidgetUiBinder.class);

	interface DraggableCellWidgetUiBinder extends
			UiBinder<Widget, DraggableCellWidget> {
	}
	IPresenter presenter;
	
	@UiField
	PushButton Add;
	
	@UiField
	Label Name;
	
	@UiField
	Grid grdDragableChild;
	
	
	private int ID;
	private Boolean canAdd;
	private String OriginalName;
	protected IPojo _DataObject;
	public DraggableCellWidget(int tmpID, Boolean isAdd, IPojo tmpData) {
		initWidget(uiBinder.createAndBindUi(this));
		ID = tmpID;
		this.canAdd = isAdd;
		this._DataObject = tmpData;
		if(this.canAdd)
		{
			this.Add.setStyleName("AddButton");
			
		}
		else
		{
			this.Add.setStyleName("RemoveButton");
		}
	}
	public void setName(String CellName)
	{
		OriginalName = CellName;
		CellName = CellName.replace("hfi.", "");
		CellName = CellName.replace("hf.", "");
		CellName = CellName.replace("pssd.", "");
		if(CellName.length()>17)
		{
			this.Name.setText(CellName.substring(0, 17));
		}
		else
		{
			this.Name.setText(CellName);
		}
	}
	public String getName(){return this.OriginalName;}
	public int getID(){return this.ID;}
	public void setID(int ID){this.ID = ID;}
	@UiHandler("Add")
	public void onClick(ClickEvent e) {
		//com.google.gwt.user.client.Window.alert("Name" + this.Name.getText() + ",canAdd:" + "" + this.canAdd.toString() + ",ID:" + this.ID);
		if(this.canAdd)
		{
			disableWidget();
		}
		this.presenter.FireDragEvent(new DragEvent(this.OriginalName,this.canAdd.toString(),this.ID,this._DataObject));
	}
	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		
	}
	public void disableWidget()
	{
		this.Add.setEnabled(false);
		this.grdDragableChild.setStyleName("DraggableChild-disabled");
	}
	public void enableWidget()
	{
		this.Add.setEnabled(true);
		this.grdDragableChild.setStyleName("DraggableChild");
	}
	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
		
	}

}
