package monbulk.MethodBuilder.client.view;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import monbulk.client.desktop.Desktop;
import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoSubjectProperties;
import monbulk.shared.util.GWTLogger;
import monbulk.shared.widgets.Window.OkCancelWindow;
import monbulk.shared.widgets.Window.OkCancelWindow.OkCancelHandler.Event;
import monbulk.MetadataEditor.MetadataSelectWindow;
import monbulk.MethodBuilder.client.view.DraggableCellWidget;
import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Events.DragEvent;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormWidget;

public class SubjectPropertiesForm extends baseForm implements IFormView,IDraggable{

	
	protected int AddedRowIndex; 
	protected final FlexTable MetaDatatable;
	
	
	
	public SubjectPropertiesForm(FormPresenter presenter) {
		
		super(presenter);
		MetaDatatable = new FlexTable();
		
		AddedRowIndex= 1;
		
	}
	@Override
	public void LoadForm(FormBuilder someForm) {
		super.LoadForm(someForm);
		setMetaDataLocation(this._formItems.getWidgetCount());
		this.linkMetaDataEditor(pojoSubjectProperties.SubjectMetaDataField, this.MetaDatatable);
	//	
	}
	public void LoadForm(FormBuilder someForm,Boolean delayMetaData)
	{
		if(delayMetaData)
		{
			super.LoadForm(someForm);
		}
		else
		{
			this.LoadForm(someForm);
		}
		
	}
	@Override
	public void ClearForm() {
		super.ClearForm();
		this.MetaDatatable.clear();
		
	}
	public void setMetaDataLocation(int locationIndex)
	{
		this._formItems.insert(this.MetaDatatable, locationIndex);
	}
	public void linkMetaDataEditor(String FieldName, final FlexTable table)
	{
		final FormWidget tmpWidg2 = this.getFormWidgetForName(FieldName);
		PushButton tmpButton = (PushButton)tmpWidg2.getFormWidget();
		final FormPresenter p = this.Presenter;
		final String fieldName = FieldName;
		tmpButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				String FieldName = tmpWidg2.getWidgetName();
				//MetadataEditor tmpEditor = new MetadataEditor();
				Desktop d = Desktop.get();
				
				final MetadataSelectWindow ms = (MetadataSelectWindow)d.getWindow("MetadataSelectWindow");
				
				final String SelectedMD = "";
				//m.loadPreview(strTCL, SupportedFormats.TCL);
				OkCancelWindow.OkCancelHandler thisHandler = new OkCancelWindow.OkCancelHandler(){

					@Override
					public void onOkCancelClicked(Event eventType) {
						if(eventType==Event.Ok)
						{
							AddMDRow(ms.getSelectedMetadataName(),table,fieldName);
							p.FireDragEvent(new DragEvent(pojoMetaData.FormName,fieldName,0,new pojoMetaData(ms.getSelectedMetadataName())));
							
						}
						else
						{
							ms.onHide();
						}
						
					}
					
				};
				ms.setOkCancelHandler(thisHandler);
				//PreviewWindow _newWin = new PreviewWindow("MethodPreviewWindow","Preview Method");
				d.show("MetadataSelectWindow", true);
				//tmpEditor.
				
			}

			
			
		});
	}
	public final void AddMDRow(String MDName, FlexTable mdTable,final String FieldName)
	{
		Label tmpLabel = new Label();
		tmpLabel.setText(MDName);
	
		pojoMetaData tmpItem = new pojoMetaData(MDName);
		tmpItem.setFieldVale(pojoMetaData.MetaDataNameField, MDName);
		
		this.DroptItem(tmpItem,mdTable,FieldName);
		
		//mdTable.setWidget(MetaDatatable.getRowCount(), 0, tmpLabel);
			//this._formItems.insert(tmpPanel, 12);
		
	}
	private int getListIndexForName(String Name, Widget FromList)
	{
		try
		{
			FlexTable table = 	(FlexTable)FromList;
			Iterator<Widget> i = table.iterator();
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
		catch(Exception ex)
		{
			return 0;
		}
	}
	@Override
	public final Boolean DragItem(IPojo someItem,Widget fromList) {
		int Index = this.getListIndexForName(someItem.getFieldVale(pojoMetaData.MetaDataNameField),fromList);
		try
		{
			FlexTable tmpTable =(FlexTable) fromList; 
			
			if(Index==0)
			{
				return false;
			}
			if(tmpTable.getRowCount()< Index-1)
			{
				return false;
			}
			else
			{
				tmpTable.removeRow(Index);
				
			}
		}
		catch(Exception ex)
		{
			GWTLogger.Log("Casting error", "SubjectPropertiesForm", "DragItem", "167");
			return false;
		}
		
		return true;
	}

	@Override
	public Boolean DroptItem(IPojo someItem,Widget fromList,final String FieldName) {
		// TODO Auto-generated method stub
		DraggableCellWidget tmpWidget = new DraggableCellWidget(AddedRowIndex,false,someItem);  
		tmpWidget.enableExpand(false);
		final FormPresenter p = this.Presenter;
		//tmpWidget.setName(someItem.getFieldVale(pojoMetaData.MetaDataNameField));
		tmpWidget.setPresenter((IPresenter) Presenter);
		final pojoMetaData tmpData =(pojoMetaData)someItem;
		final Widget fromWidget = fromList;
		ClickHandler handleRemove = new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event) {
				p.FireDragEvent(new DragEvent(pojoMetaData.FormName,FieldName,1,tmpData));
				DragItem(tmpData, fromWidget);
			}
			
		};
	
		tmpWidget.AttachToggleHandler(handleRemove);
		try
		{
			FlexTable tmpTable = (FlexTable)fromList;
			tmpTable.setWidget(AddedRowIndex, 0, tmpWidget);
			
			//this.MetaDataTable.setWidget(AddedRowIndex, 1, tmpWidget);
			//this.MetaDataTable.add(tmpWidget);
			AddedRowIndex ++;
			
			return true;
		}
		catch(Exception ex)
		{
			GWTLogger.Log("Casting error", "SubjectPropertiesForm", "DragItem", "167");
			return false;
		}
	}
}
