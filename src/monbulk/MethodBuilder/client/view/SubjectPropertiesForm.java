package monbulk.MethodBuilder.client.view;

import java.util.Iterator;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.MethodBuilder.client.view.DraggableCellWidget;
import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Form.FormBuilder;

public class SubjectPropertiesForm extends baseForm implements IFormView,IDraggable{

	
	protected int AddedRowIndex; 
	protected FlexTable MetaDatatable;
	
	
	
	public SubjectPropertiesForm() {
		
		super();
		MetaDatatable = new FlexTable();
		MetaDatatable.setWidget(0, 0, new Label("Attached MetaData"));
		AddedRowIndex= 1;
		
	}
	@Override
	public void LoadForm(FormBuilder someForm) {
		super.LoadForm(someForm);
	//	this.add(this.MetaDatatable);
	}
	private int getListIndexForName(String Name)
	{
		Iterator<Widget> i = this.MetaDatatable.iterator();
		while(i.hasNext())
		{
			DraggableCellWidget tmpItem = (DraggableCellWidget)i.next();
			if(tmpItem.getName().equals(Name))
			{
				return tmpItem.getID();
			}
		}
		return 0;
	}
	@Override
	public Boolean DragItem(IPojo someItem) {
		int Index = this.getListIndexForName(someItem.getFieldVale(pojoMetaData.MetaDataNameField));
		if(Index==0)
		{
			return false;
		}
		if(this.MetaDatatable.getRowCount()< Index-1)
		{
			return false;
		}
		else
		{
			this.MetaDatatable.removeRow(Index);
			int row=Index;
			//For every item that was added after the removed item - change its id by 1 
			AddedRowIndex--;
			//Correct Indices
			while(row < AddedRowIndex)
			{
				DraggableCellWidget tmpWidget = (DraggableCellWidget)this.MetaDatatable.getWidget(row,0);
				//SetID
				tmpWidget.setID(tmpWidget.getID()-1);
				row++;
			}
			return true;
			
		}
	}

	@Override
	public Boolean DroptItem(IPojo someItem) {
		// TODO Auto-generated method stub
		DraggableCellWidget tmpWidget = new DraggableCellWidget(AddedRowIndex,false,someItem);  
		tmpWidget.setName(someItem.getFieldVale(pojoMetaData.MetaDataNameField));
		tmpWidget.setPresenter((IPresenter) Presenter);
		tmpWidget.setWidth("180px");
		
		this.MetaDatatable.setWidget(AddedRowIndex, 0, tmpWidget);
		
		//this.MetaDataTable.setWidget(AddedRowIndex, 1, tmpWidget);
		//this.MetaDataTable.add(tmpWidget);
		AddedRowIndex ++;
		return true;
	}
}
