package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;
import java.util.Iterator;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Architecture.IPresenter;

import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Form.FormBuilder;

import monbulk.shared.Form.FormWidget;
import monbulk.shared.Form.iFormField;
import monbulk.shared.util.GWTLogger;

/**
 * This is the baseForm widget which takes a FormBuilder data structure and renders it in order
 * NB: There is not currently Support for merged forms.
 * 
 * @author Andrew Glenn
 *
 */

public class baseForm extends VerticalPanel implements IFormView {

	protected FormBuilder generalForm;
	public final FormPresenter Presenter;
	protected ArrayList<FormWidget> allFormItems;
	protected ArrayList<String> ErrorSummary;
	protected Label _errors;
	protected PushButton _completeForm;
	protected PushButton _recedeForm;
	protected ArrayList<HorizontalPanel> widgetStructure;
	protected VerticalPanel tmpPanel = new VerticalPanel();
	protected ScrollPanel _scrollSection = new ScrollPanel();
	protected VerticalPanel _formItems = new VerticalPanel();
	
	private Boolean isLoaded;
	public baseForm(FormPresenter presenter)
	{
		super();
		this.Presenter = presenter;
		this.allFormItems = new ArrayList<FormWidget>();
		this.widgetStructure = new ArrayList<HorizontalPanel>();
		
		VerticalPanel tmpPanel = new VerticalPanel();
		isLoaded = false;
	}
	
	@Override
	public Widget asWidget() {
		
		//Window.alert(this.getTitle() + "We get here");
		return this;
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		
		
	}

	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ClearForm() {
		
		/*Iterator<FormWidget> i = this.allFormItems.iterator();
		
		while(i.hasNext())
		{
			FormWidget tmpWidg = i.next();
			tmpWidg.clear();
		}*/
		this._formItems.clear();
	}
	protected FormWidget getFormWidgetForName(String Name)
	{
		Iterator<FormWidget> i = this.allFormItems.iterator();
		while(i.hasNext())
		{
			FormWidget tmpWidg = i.next();
			if(tmpWidg.getWidgetName().contains(Name))
			{
				return tmpWidg;
			}
			
		}
		return null;
	}
	protected Widget getFormFieldWidgetForName(String Name)
	{
		Iterator<FormWidget> i = this.allFormItems.iterator();
		while(i.hasNext())
		{
			FormWidget tmpWidg = i.next();
			if(tmpWidg.getWidgetName().equals(Name))
			{
				return tmpWidg.getFormWidget();
			}
			
		}
		return null;
	}
	public int getWidgetIndexForName(String Name)
	{
		Iterator<FormWidget> i = this.allFormItems.iterator();
		int index = 0;
		while(i.hasNext())
		{
			FormWidget tmpWidg = i.next();
			if(tmpWidg.getWidgetName().equals(Name))
			{
				return index*2;///// *2 is for the 2 widgets per row
			}
			index++;
			
		}
		return -1;	
		
	}
	public void hideShowWidget(String WidgetName,Boolean show,Boolean showSpacing)
	{
		if(this._formItems != null)
		{
			int index = this.getWidgetIndexForName(WidgetName);
			if(index>0)
			{
				
				this._formItems.getWidget(index).setVisible(show);
				this._formItems.getWidget(index+1).setVisible(showSpacing);
				//this._formItems.getWidget(resolvedIndex+2).setVisible(showSpacing);
				//this._formItems.getWidget(resolvedIndex+3).setVisible(showSpacing);
				
			}
		}
		
	}
	private void emptyForm()
	{
		try
		{
			this.tmpPanel.clear();
		}
		catch(Exception ex)
		{
			Window.alert("error @ baseForm.emptyForm()" + ex.getMessage());
		}
		
	}
	private void renderForm()
	{
		try
		{	
			emptyForm();
			//HorizontalPanel FormName = new HorizontalPanel();
			//FormName.setHeight("50px");
			//FormName.setStyleName("FormTitle");
			//Label lblFormName = new Label();
			//lblFormName.setText(this.generalForm.getFormName() + "");
			//FormName.add(lblFormName);
			//tmpPanel.add(FormName);
			/*HorizontalPanel errorPanel = new HorizontalPanel();
			errorPanel.setStyleName("FormErrors");
			this._errors = new Label();
			errorPanel.add(_errors);
			tmpPanel.add(errorPanel);
			*/
			widgetStructure = new ArrayList<HorizontalPanel>();
			//widgetStructure.add(errorPanel);
			
			GWTLogger.Log("We start to render the form items - form Items:" + generalForm.getFormDetails().size(), "baseForm", "RenderForm", "165");
			Iterator<iFormField> i = generalForm.getFormDetails().iterator();
			
			while(i.hasNext())
			{
				iFormField item = i.next();
				GWTLogger.Log("item to add" + item.GetFieldName() +item.getFieldTypeName(), "baseForm", "RenderForm", "171");
				
				if(item.getWidgetReference()!=null)
				{
					final FormWidget tmpWidg = item.getWidgetReference();
					if(item.getFieldTypeName()=="List" || item.getFieldTypeName()=="Dictionary")
					{
						
						final ListBox tmpBox = (ListBox)tmpWidg.getFormWidget();
						tmpBox.addChangeHandler(new ChangeHandler(){

							@Override
							public void onChange(ChangeEvent event) {
								
								String Value = tmpBox.getValue(tmpBox.getSelectedIndex());
								String FieldName = tmpWidg.getWidgetName();
								UpdateValue(FieldName, Value);
							}
							
						});
						tmpWidg.setListValue(tmpBox.getSelectedIndex());
					}
					else if(item.getFieldTypeName()=="Button")
					{
						final PushButton tmpButton = (PushButton)tmpWidg.getFormWidget();
						tmpButton.setStyleName("formButtonAdd");
						
					}
					else
					{
						HasValue<Object> tmpWidget = (HasValue<Object>)tmpWidg.getFormWidget();
						tmpWidget.addValueChangeHandler(new ValueChangeHandler<Object>()
								{
							
											@Override
											public void onValueChange(ValueChangeEvent<Object> event) {
												// TODO Auto-generated method stub
												Widget source = (Widget)event.getSource();
												String FieldName = tmpWidg.getWidgetName();
												//String FieldName = source.getTitle();
												UpdateValue(FieldName, event.getValue());
												//event.getValue();
								}
									
								});
						if(item.getFieldTypeName()=="Boolean")
						{
							if(item.GetFieldValue()=="true")
							{
								tmpWidg.setFormValue(true);
							}
							else
							{
								tmpWidg.setFormValue(false);
							}
						}
						else
						{
							tmpWidg.setFormValue(item.GetFieldValue());
						}
					}
	
			
					HorizontalPanel hzPanel = new HorizontalPanel();
					HorizontalPanel spacePanel = new HorizontalPanel();
					hzPanel.add(tmpWidg);
					//hzPanel.setCellWidth(tmpWidg, "500px");
					hzPanel.setHeight("50px");
					//hzPanel.setWidth("100%");
					spacePanel.setStyleName("formSpacing");
					spacePanel.setHeight("10px");
					//hzPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
					widgetStructure.add(hzPanel);
					
					//this._formItems.
					this._formItems.add(hzPanel);
					this._formItems.add(spacePanel);
				}
				
			}
			GWT.log("We end the while loop");
			_formItems.setWidth("420px");
			//_formItems.setHeight("400px");
			_formItems.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
			//this._scrollSection.add(_formItems);
			
			//this._scrollSection.setHeight("480px");
			//this._scrollSection.setWidth("400px");
			tmpPanel.add(_formItems);
			tmpPanel.setStyleName("Form");
			tmpPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
			
			
			if(!isLoaded)
			{
				this.add(tmpPanel);
			}
			isLoaded = true;
			GWTLogger.Log("Widget COunt" + tmpPanel.getWidgetCount(), "baseForm", "RenderForm", "248");
			
		}
		catch(Exception ex)
		{
			Window.alert("Could not render the form, for reference: Exception is" + ex.getMessage() + "Location is baseForm.RenderForm");
		}
	}
	private final void completeForm(String State)
	{
		if(this.Presenter!=null)
		{
			this.Presenter.FormComplete(this.generalForm.getFormName(), State);
		}
	}
	@Override
	public void LoadForm(FormBuilder someForm) {
		try
		{
			
			GWTLogger.Log("Which form is loading" + someForm.getFormName(), "baseForm", "loadForm", "267");
			if(someForm==null)
			{
				GWTLogger.Log("Form Expected @ baseForm.LoadForm(someForm):No form provided", "baseForm", "loadForm", "270");
				
				//Window.alert("No Form");
				renderForm();
				return;
			}
			generalForm = someForm;
			allFormItems.clear();
			Iterator<iFormField> i = generalForm.getFormDetails().iterator();
			while(i.hasNext())
			{
				iFormField item = i.next();
				
				if(item.getWidgetReference()!=null)
				{
					FormWidget tmpWidg = item.getWidgetReference();
					
					//GWT.log("Value" + item.GetFieldValue());
								//tmpWidg.addHandler(handler, type)
					
					allFormItems.add(tmpWidg);
				}
			}
			GWTLogger.Log("we load the form", "baseForm", "loadForm", "267");
			renderForm();
		}
		catch(Exception ex)
		{
			GWT.log("Exception caught @ baseForm.LoadForm: Message -" + ex.getMessage());
		}
	}
	private final void UpdateValue(String FieldName, Object FieldValue)
	{
		//Window.alert(FieldName + ":" + FieldValue);
		iFormField tmpField = this.generalForm.getFieldItemForName(generalForm.getFormName()+ "." + FieldName);
		//Window.alert("IS TmpField null:" + tmpField + "in form:" + generalForm.getFormName()+ "." + FieldName);
		if(tmpField==null)
		{
			tmpField = this.generalForm.getFieldItemForName(FieldName);
		}
		if(tmpField!=null)
		{
			String response = tmpField.Validate(FieldValue.toString());
			if(response == "")
			{
				tmpField.SetFieldValue(FieldValue.toString());
				
			}
			else
			{
				this.ErrorSummary.add(response);
				this._errors.setText(this._errors.getText() + response);
			}
			if(this.Presenter!=null){
				String finalOut = this.Presenter.UpdateValue(this.generalForm);
				
			}
		}
		else
		{
			//Window.alert(FieldName + ":" + FieldValue + generalForm.getFormDetails().get(0).GetFieldName());
		}
	}
	@Override
	public void setPresenter(FormPresenter presenter) {
		//this.Presenter = presenter;
		
	}

	@Override
	public FormBuilder getFormData() {
		// TODO Auto-generated method stub
		return this.generalForm;
	}

	@Override
	public Iterator<Widget> iterator() {
		
		return super.iterator();
	}

	@Override
	public boolean remove(Widget child) {
		// TODO Auto-generated method stub
		return this.allFormItems.remove(child);
		//return false;
	}

}
