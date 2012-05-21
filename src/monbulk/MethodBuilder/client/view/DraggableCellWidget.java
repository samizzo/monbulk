package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;

import name.pehl.totoe.xml.client.Document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Events.DragEvent;
import monbulk.shared.Form.FormWidget;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.shared.util.GWTLogger;
import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IView;

/**
 * This widget is used for list items of metaData
 * It implmends the MetaDataServiceHandler to load the required MetaData Details when expanded
 * 
 * TODO Ideally we should create a base widget for anything draggable
 *  - it would check if it is could relate to the table it drops in and offer
 *  a summary view and a detailed view with a FormBuilder.S
 *
 *
 * @author Andrew Glenn
 *
 *
 */
public class DraggableCellWidget extends Composite implements IView,HasValue{

	private static DraggableCellWidgetUiBinder uiBinder = GWT
			.create(DraggableCellWidgetUiBinder.class);

	interface DraggableCellWidgetUiBinder extends
			UiBinder<Widget, DraggableCellWidget> {
	}
	IPresenter presenter;
	
	@UiField PushButton Add;
	@UiField PushButton _btnExpand;	
	@UiField Label Name;
	@UiField Grid grdDragableChild;
	@UiField CheckBox _cbIsMandatory;
	@UiField CheckBox _cbIsPublic;
	
	/*Handlers */
	private ClickHandler handleAddClicked;
	private ClickHandler handleExpandClicked;
	
	private int ID;
	private Boolean canAdd;
	private String OriginalName;
	protected pojoMetaData  _DataObject;
	
	private VerticalPanel _ExpandedPanel;
	
	private name.pehl.totoe.xml.client.Document xmlDoc;
	public DraggableCellWidget(int tmpID, Boolean isAdd, IPojo tmpData) {
		initWidget(uiBinder.createAndBindUi(this));
		ID = tmpID;
		this.canAdd = isAdd;
		//this._cbIsMandatory.setText(text)
		this._DataObject = (pojoMetaData)tmpData;
		//We need to call the describe service
		this.setName(this._DataObject.getFieldVale(this._DataObject.MetaDataNameField)); 
		this.Add.setVisible(true);
		this.Add.setSize("25px", "25px");
		/*if(this.canAdd)
		{
			this.Add.setStyleName("AddButton");
			
		}
		else
		{
			this.Add.setStyleName("RemoveButton");
		}*/
		
	}
	public void setName(String CellName)
	{
		OriginalName = CellName;
		this.Name.setText(OriginalName);
	}
	
	public void enableExpand(Boolean canExpand)
	{
		this._btnExpand.setVisible(canExpand);
	}
	public String getName(){return this.OriginalName;}
	public int getID(){return this.ID;}
	public void setID(int ID){this.ID = ID;}
	
	/**
	 * @deprecated
	 */
	@Override
	public void setData(ArrayList<String> someList) {
		// Auto-generated method stub
		
	}
	@UiHandler("_cbIsMandatory")
	public void onValueChange(ValueChangeEvent<Boolean> event)
	{
		this._DataObject.setFieldVale(pojoMetaData.IsMandatoryField, event.getValue());
		presenter.FireDragEvent(new DragEvent(this.OriginalName, "Update", this.ID, this._DataObject));
	}
	@UiHandler("_cbIsPublic")
	public void onValueChange2(ValueChangeEvent<Boolean> event)
	{
		this._DataObject.setFieldVale(pojoMetaData.IsPublicField, event.getValue());
		presenter.FireDragEvent(new DragEvent(this.OriginalName, "Update", this.ID, this._DataObject));
	}
	public void AttachToggleHandler(ClickHandler newHandler)
	{
		this.Add.addClickHandler(newHandler);
	}
	public void AttachExpandHandler(ClickHandler newHandler)
	{
		this._btnExpand.addClickHandler(newHandler);
	}
	public void disableWidget()
	{
		//this.Add.setEnabled(false);
		//this.grdDragableChild.setStyleName("DraggableChild-disabled");
	}
	public void enableWidget()
	{
		//this.Add.setEnabled(true);
		//this.grdDragableChild.setStyleName("DraggableChild");
	}
	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
		
	}
	
	public pojoMetaData getPojo()
	{
		return this._DataObject;
	}
	
	
	public Object GetFieldValue() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		
		sb.append("<metaData name=\"" + this.OriginalName + "\"");
		sb.append("isMandatory=\"" + this._cbIsMandatory.getValue().toString() + "\"");
		sb.append("isPublic=\"" + this._cbIsPublic.getValue().toString() + "\"");
		sb.append("/>");
		name.pehl.totoe.xml.client.XmlParser xmlMD = new name.pehl.totoe.xml.client.XmlParser();
		try
		{
			xmlDoc = xmlMD.parse(sb.toString());
			return xmlDoc;
		}
		catch(Exception ex)
		{
			GWTLogger.Log("Failed to parse XML", "DraggableCellWidget", "GetFieldValue", "");
			return null;
		}
		
	}	
	public void SetFieldValue(String FieldValue) {
		name.pehl.totoe.xml.client.XmlParser xmlMD = new name.pehl.totoe.xml.client.XmlParser();
		try
		{
			xmlDoc = xmlMD.parse(FieldValue);
			
		}
		catch(Exception ex)
		{
			GWTLogger.Log("Failed to parse XML", "DraggableCellWidget", "GetFieldValue", "");
			
		}
		
	}
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return this.GetFieldValue();
	}
	@Override
	public void setValue(Object value) {
		try
		{
			this.xmlDoc = (Document) value;
		}
		catch(Exception ex)
		{
			this.SetFieldValue(value.toString());
		}
		
	}
	@Override
	public void setValue(Object value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

}
