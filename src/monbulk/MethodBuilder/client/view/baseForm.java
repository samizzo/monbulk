package daris.Monbulk.MethodBuilder.client.view;

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

import daris.Monbulk.shared.Architecture.IPresenter;

import daris.Monbulk.shared.Architecture.IPresenter.FormPresenter;
import daris.Monbulk.shared.Architecture.IView.IFormView;
import daris.Monbulk.shared.Form.FormBuilder;

import daris.Monbulk.shared.Form.FormWidget;
import daris.Monbulk.shared.Form.iFormField;


public class baseForm extends VerticalPanel implements IFormView {

	protected FormBuilder generalForm;
	protected FormPresenter Presenter;
	protected ArrayList<FormWidget> allFormItems;
	protected ArrayList<String> ErrorSummary;
	protected Label _errors;
	protected PushButton _completeForm;
	protected PushButton _recedeForm;
	protected ArrayList<HorizontalPanel> widgetStructure;
	public baseForm()
	{
		super();
		this.allFormItems = new ArrayList<FormWidget>();
		this.widgetStructure = new ArrayList<HorizontalPanel>();
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
			if(tmpWidg.getWidgetName().equals(Name))
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
	private void renderForm()
	{
		VerticalPanel tmpPanel = new VerticalPanel();
		HorizontalPanel FormName = new HorizontalPanel();
		FormName.setHeight("50px");
		FormName.setStyleName("FormTitle");
		Label lblFormName = new Label();
		lblFormName.setText(this.generalForm.getFormName());
		FormName.add(lblFormName);
		tmpPanel.add(FormName);
		HorizontalPanel errorPanel = new HorizontalPanel();
		errorPanel.setStyleName("FormErrors");
		this._errors = new Label();
		errorPanel.add(_errors);
		tmpPanel.add(errorPanel);
		Iterator<FormWidget> i = this.allFormItems.iterator();
		widgetStructure = new ArrayList<HorizontalPanel>();
		widgetStructure.add(errorPanel);
		while(i.hasNext())
		{
			final FormWidget tmpWidg = i.next();
			HasValue<Object> tmpWidget = (HasValue<Object>) tmpWidg.getFormWidget(); 
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
		
			HorizontalPanel hzPanel = new HorizontalPanel();
			hzPanel.add(tmpWidg);
			hzPanel.setHeight("50px");
			hzPanel.setWidth("100%");
			hzPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
			widgetStructure.add(hzPanel);
			tmpPanel.add(hzPanel);
			
		}
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
		
		_completeForm.setText("Next");
		
		_completeForm.setStyleName("btnNext");
		
		_recedeForm = new PushButton();
		_recedeForm.setText("Previous");
		
		_recedeForm.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				completeForm("Prev");
			}
			
		});
		navigation.add(_recedeForm);
		navigation.add(_completeForm);
		navigation.setHeight("75px");
		navigation.setVerticalAlignment(ALIGN_BOTTOM);
		widgetStructure.add(navigation);
		tmpPanel.add(navigation);
		this.add(tmpPanel);
		
	}
	private final void completeForm(String State)
	{
		this.Presenter.FormComplete(this.generalForm.getFormName(), State);
	}
	@Override
	public void LoadForm(FormBuilder someForm) {
		if(someForm==null)
		{
			GWT.log("No form provided");
			renderForm();
			return;
		}
		generalForm = someForm;
		
		Iterator<iFormField> i = generalForm.getFormDetails().iterator();
		while(i.hasNext())
		{
			iFormField item = i.next();
			
			if(item.getWidgetReference()!=null)
			{
				FormWidget tmpWidg = item.getWidgetReference();
				//GWT.log(item.GetFieldName());
							//tmpWidg.addHandler(handler, type)
				
				allFormItems.add(tmpWidg);
			}
		}
		renderForm();
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
