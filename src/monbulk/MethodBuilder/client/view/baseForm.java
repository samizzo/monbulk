package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;
import java.util.Iterator;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Architecture.IPresenter;

import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Form.FormBuilder;

import monbulk.shared.Form.FormWidget;
import monbulk.shared.Form.iFormField;

/**
 * This is the baseForm widget which takes a FormBuilder data structure and renders it in order
 * NB: There is not currently Support for merged forms.
 * 
 * @author Andrew Glenn
 *
 */

public class baseForm extends VerticalPanel implements IFormView {

	protected FormBuilder generalForm;
	protected FormPresenter Presenter;
	protected ArrayList<FormWidget> allFormItems;
	protected ArrayList<String> ErrorSummary;
	protected Label _errors;
	protected PushButton _completeForm;
	protected PushButton _recedeForm;
	protected ArrayList<HorizontalPanel> widgetStructure;
	protected VerticalPanel tmpPanel = new VerticalPanel();
	private Boolean isLoaded;
	public baseForm()
	{
		super();
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
		
		Iterator<FormWidget> i = this.allFormItems.iterator();
		
		while(i.hasNext())
		{
			FormWidget tmpWidg = i.next();
			tmpWidg.clear();
		}
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
			HorizontalPanel FormName = new HorizontalPanel();
			FormName.setHeight("50px");
			FormName.setStyleName("FormTitle");
			Label lblFormName = new Label();
			lblFormName.setText(this.generalForm.getFormName() + "");
			FormName.add(lblFormName);
			tmpPanel.add(FormName);
			HorizontalPanel errorPanel = new HorizontalPanel();
			errorPanel.setStyleName("FormErrors");
			this._errors = new Label();
			errorPanel.add(_errors);
			tmpPanel.add(errorPanel);
			
			widgetStructure = new ArrayList<HorizontalPanel>();
			widgetStructure.add(errorPanel);
			GWT.log("We start to render the form items");
			
			Iterator<iFormField> i = generalForm.getFormDetails().iterator();
			
			while(i.hasNext())
			{
				iFormField item = i.next();
				//GWT.log("item to add" + item.GetFieldName() +item.getWidgetReference().toString());
				if(item.getWidgetReference()!=null)
				{
					final FormWidget tmpWidg = item.getWidgetReference();
					HasValue<Object> tmpWidget = (HasValue<Object>)tmpWidg.getFormWidget(); 
					tmpWidg.setFormValue(item.GetFieldValue());
					
					GWT.log("Value rendered should be:" + item.GetFieldValue());
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
					//if(tmpWidget.getValue()!=null){
					//	tmpWidget.setValue(value, fireEvents)
					//}
					HorizontalPanel hzPanel = new HorizontalPanel();
					hzPanel.add(tmpWidg);
					hzPanel.setHeight("50px");
					hzPanel.setWidth("100%");
					hzPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
					widgetStructure.add(hzPanel);
					tmpPanel.add(hzPanel);
				}
				
			}
			GWT.log("We end the while loop");
			tmpPanel.setStyleName("Form");
			
			
			HorizontalPanel navigation = new HorizontalPanel();
			navigation.setStyleName("FormNavigation");
			_completeForm = new PushButton();
			
			_completeForm.addClickHandler(new ClickHandler(){
	
				@Override
				public void onClick(ClickEvent event) {
					
					completeForm("Next");
				}
				
			});
			
			GWT.log("First OnClick");
			_completeForm.setText("Next");
			
			_completeForm.setStyleName("btnNext");
			
			_recedeForm = new PushButton();
			_recedeForm.setText("Previous");
			
			GWT.log("Second OnClick");
			_recedeForm.addClickHandler(new ClickHandler(){
	
				@Override
				public void onClick(ClickEvent event) {
					
					completeForm("Prev");
				}
				
			});
			GWT.log("We add navigation");
			navigation.add(_recedeForm);
			navigation.add(_completeForm);
			navigation.setHeight("75px");
			navigation.setVerticalAlignment(ALIGN_BOTTOM);
			widgetStructure.add(navigation);
			GWT.log("We complete render");
			tmpPanel.add(navigation);
			if(!isLoaded)
			{
				this.add(tmpPanel);
			}
			isLoaded = true;
			GWT.log("Widget COunt" + tmpPanel.getWidgetCount());
		}
		catch(Exception ex)
		{
			Window.alert("Error in Render" + ex.getMessage());
		}
	}
	private final void completeForm(String State)
	{
		this.Presenter.FormComplete(this.generalForm.getFormName(), State);
	}
	@Override
	public void LoadForm(FormBuilder someForm) {
		try
		{
		if(someForm==null)
		{
			GWT.log("Form Expected @ baseForm.LoadForm(someForm):No form provided");
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
		GWT.log("We load the Form");
		renderForm();
		}
		catch(Exception ex)
		{
			GWT.log("Exception caught @ baseForm.LoadForm: Message -" + ex.getMessage());
		}
	}
	private final void UpdateValue(String FieldName, Object FieldValue)
	{
		Window.alert(FieldName + ":" + FieldValue);
		iFormField tmpField = this.generalForm.getFieldItemForName(FieldName);
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
		if(!this.Presenter.equals(null)){
			String finalOut = this.Presenter.UpdateValue(this.generalForm);
			
		}
	}
	@Override
	public void setPresenter(FormPresenter presenter) {
		this.Presenter = presenter;
		
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
