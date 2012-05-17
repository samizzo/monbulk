package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import monbulk.MethodBuilder.shared.IMethodsView;
import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoSubjectProperties;
import monbulk.shared.util.GWTLogger;

public class MethodDetailsView extends Composite implements IMethodsView {

	@UiField
	DockLayoutPanel LayoutPanel;
	
	@UiField
	HTML MethodDetailsSummary;
	
	@UiField
	HTML SubjectPropertiesSummary;
	
	@UiField
	StackLayoutPanel MethodNavigationStack;
	
	@UiField
	PushButton btnNext;
	
	@UiField
	PushButton btnPrev;
	
	@UiField
	HTMLPanel FormDetails;
	//@UiField
	public class CustomTab
	{
		private int TabIndex;
		private HTML bodyWidget;
		private CustomStackHeader headerWidget;
		
		public CustomTab(int Index, String Title, String FormName)
		{
			this.headerWidget = new CustomStackHeader(Title,FormName);
			TabIndex = Index;
			bodyWidget = new HTML();
		}
		public void setHTML(String html){this.bodyWidget.setHTML(html);	}
		public void setTitle(String Title){this.headerWidget.setTitle(Title);}
		public Widget getHeader(){return this.headerWidget.asWidget();}
		public Widget getBody(){return this.bodyWidget.asWidget();}
		public int getIndex(){return this.TabIndex;}
		public void DecrementIndex(){
			int newTabIndex =TabIndex-1; 
			this.setTitle(this.headerWidget.getTitle().replace("Step "+this.TabIndex, "Step " + newTabIndex));
			this.TabIndex--;	
		}
	}
	private LinkedHashMap<String,CustomTab> CurrentSteps;
	private FormPresenter thisPresenter;
	private int ChildContainerIndex;
	private int StepIndex;
	private String currentForm;
	private static MethodDetailsViewUiBinder uiBinder = GWT
			.create(MethodDetailsViewUiBinder.class);

	interface MethodDetailsViewUiBinder extends
			UiBinder<Widget, MethodDetailsView> {
	}

	public MethodDetailsView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.ChildContainerIndex = 0;
		this.StepIndex = 1;
		CurrentSteps = new LinkedHashMap<String,CustomTab>();
		//MethodNavigationStack.addSelectionHandler(handler)
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		// TODO Auto-generated method stub
		this.thisPresenter = (FormPresenter)presenter;
	}
	@Override
	public void SetMenuIndex(String IndexName)
	{
		currentForm = IndexName;
		if(IndexName == pojoMethod.FormName)
		{
			this.MethodNavigationStack.showWidget(0,false);
			
			
		}
		else if(IndexName == pojoSubjectProperties.FormName)
		{
			this.MethodNavigationStack.showWidget(1,false);
			
		}
		else if(IndexName.contains(pojoStepDetails.FormName))
		{
			//We need to check if this is a new or old stack - FormName is unique
			//Window.alert("Widget Count:" + this.MethodNavigationStack.getWidgetCount() + "Step COunt" + this.StepIndex );
			//NB: STep count drops back to 0!!!
			this.MethodNavigationStack.showWidget(this.CurrentSteps.get(IndexName).getIndex()+1);
		}
	}
	//TODO - need to change this so that a set of static Array data can be added 
	// 		 Specific to DataType
	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		
	}
	public CustomTab StepExists(String FormName)
	{
			
		if(CurrentSteps.get(FormName)!=null)
		{
			return CurrentSteps.get(FormName);
		}
		else
		{
			return null;
			
		}
		
	}
	private String GetIndexList()
	{
		String Output ="";
		Iterator<Entry<String, CustomTab>> it = CurrentSteps.entrySet().iterator();
		while(it.hasNext())
		{
			CustomTab tmpStack = it.next().getValue();
			Output = Output + "Expected Index" + "Actual Index" +  tmpStack.TabIndex + tmpStack;
		}
		return Output;
	}
	@Override
	public void setData(FormBuilder anyBuilder) {
		try
		{
			this.currentForm = anyBuilder.getFormName();
		String html ="<table>";
		//while(anyBuilder.)
		//How do we know which step we are on? - form Name
		CustomStackHeader tmpHeader = null;
		HTML tmpBody = null;
		Boolean isNewStep = false;
		//Window.alert("Setting the following:" + this.currentForm);
		if(anyBuilder.getFormName().contains(pojoStepDetails.FormName))
		{
			GWT.log("Step Path C");
			if(StepExists(anyBuilder.getFormName())!=null)
			{
				//if we have an Index, append to existing item
				tmpHeader =  (CustomStackHeader) this.CurrentSteps.get(anyBuilder.getFormName()).getHeader();
				tmpBody =  (HTML) this.CurrentSteps.get(anyBuilder.getFormName()).getBody();
				
				//Widget tmpStack = this.MethodNavigationStack.getWidget(tmpStepIndex+ 2);
				//this.MethodNavigationStack.getVisibleIndex(); MAY WORK as well
				//this.StepIndex = this.CurrentSteps.get(anyBuilder.getFormName()).getIndex();
			}
			else
			{
				GWT.log("Step Path D");
				
				//tmpHeader = new CustomStackHeader("New Step",anyBuilder.getFormName());
				tmpBody = new HTML();
				isNewStep = true;	
				CustomTab tmpTab = new CustomTab(this.StepIndex,"New Step",anyBuilder.getFormName());
				//If not initial Step
				
				this.CurrentSteps.put(anyBuilder.getFormName(), tmpTab);
				GWT.log(this.GetIndexList());
				tmpHeader =  (CustomStackHeader) this.CurrentSteps.get(anyBuilder.getFormName()).getHeader();
				tmpHeader.setPresenter(thisPresenter);
				this.MethodNavigationStack.add(tmpTab.getBody(), tmpTab.getHeader(), 41);
				
			}
		}
		Iterator<iFormField> i =anyBuilder.getFormDetails().iterator(); 
		while(i.hasNext())
		{
			iFormField tmpField = i.next();
			//Window.alert("FieldName" + tmpField.GetFieldName() + "isSummary?" + tmpField.isSummary() + "-hasValue?" + tmpField.hasValue());
			if(tmpField!=null)
			{
				
				if(tmpField.isSummary())
				{
					if(tmpField.getFieldTypeName()=="List" || tmpField.getFieldTypeName()=="Dictionary" || tmpField.getFieldTypeName()=="Button")
					{
						GWTLogger.Log("isSummary - should not hit", "MethodDetailsView", "setData", "221");
					}
					else if(tmpField.hasValue())
					{
						String FieldName = tmpField.GetFieldName();
						if(tmpField.GetFieldName().contains(anyBuilder.getFormName()))
						{
							FieldName = tmpField.GetFieldName().replace(anyBuilder.getFormName()+".", "");
						}
						//Window.alert(FieldName);
						html = html + "<tr><td class='fieldName'>" + FieldName + ":</td>";
						html = html + "<td class='fieldValue'>" + (String)tmpField.GetFieldValue() + "</td></tr>";
					}
					
				}
				else if(tmpField.isTitle() && tmpField.hasValue() && anyBuilder.getFormName().equals(pojoMethod.FormName))
				{
					//this.MethodTitle.setText((String) tmpField.GetFieldValue());
					
					this.MethodNavigationStack.setHeaderHTML(0,(String) tmpField.GetFieldValue());
				}
				else if(tmpField.isTitle() && tmpField.hasValue() && anyBuilder.getFormName().contains(pojoStepDetails.FormName))
				{
					GWTLogger.Log("isTitle - should not hit" + tmpField.GetFieldName(), "MethodDetailsView", "setData", "244");
					String Title = "Step " + this.CurrentSteps.get(anyBuilder.getFormName()).getIndex() + ": " + tmpField.GetFieldValue().toString();
					tmpHeader.setTitle(Title);
					
				}
			}
		}
		html = html +"</<table>";
		GWTLogger.Log("HTML success", "MethodDetailsView", "setData", "252");
		if(anyBuilder.getFormName().contains(pojoMethod.FormName))
		{
			this.MethodNavigationStack.showWidget(0);
			MethodDetailsSummary.setHTML(html);
			
		}
		else if(anyBuilder.getFormName().contains(pojoSubjectProperties.FormName))
		{
			this.MethodNavigationStack.showWidget(1);
			SubjectPropertiesSummary.setHTML(html);
		}
		else if(anyBuilder.getFormName().contains(pojoStepDetails.FormName))
		{
			
			//We need to check if this is a new or old stack - FormName is unique
			//Window.alert("Widget Count:" + this.MethodNavigationStack.getWidgetCount() + "Step COunt" + this.StepIndex );
			//NB: STep count drops back to 0!!!
			if(tmpBody!=null)
			{
				tmpBody.setHTML(html);
			}
			if(isNewStep)
			{
				this.StepIndex++;
			}
			this.MethodNavigationStack.showWidget(this.CurrentSteps.get(anyBuilder.getFormName()).getIndex()+1);
		}
		}
		catch(Exception ex)
		{
			GWT.log("Error occurs at MethodDetailsView.setData:" + ex.getMessage());
		}
		
	}
	@Override
	public void setChild(Widget someContainer) {
		
		//this.MethodFormStack.add(someContainer, "Form Section", 42);
		FormDetails.clear();
		FormDetails.add(someContainer);
	}
	@Override
	public void clearChild() {
		//this.LayoutPanel. .addWest(someContainer,480);
		FormDetails.clear();
		
	}
	@Override
	public void setMethodData(ArrayList<iFormField> StaticFieldList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSubjectData(ArrayList<iFormField> StaticFieldList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStepData(ArrayList<iFormField> StaticFieldList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasWidgets GetChildContainer() {
		// TODO Auto-generated method stub
		
		return this.LayoutPanel;
	}
	///Potentially this changes to onCLick of a WIDGET
	@UiHandler("MethodNavigationStack")
	public void onSelection(SelectionEvent<Integer> e)
	{
		
		if(this.MethodNavigationStack.getVisibleIndex() == 0)
		{
			this.thisPresenter.FormComplete(pojoMethod.FormName, "Edit");
		}
		else if(this.MethodNavigationStack.getVisibleIndex() == 1)
		{
			this.thisPresenter.FormComplete(pojoSubjectProperties.FormName, "Edit");
		}
		else
		{
			//Trigger Nothing - should be controlled by Edit Button
	//		CustomStackHeader tmpHeader = (CustomStackHeader)this.MethodNavigationStack.getHeaderWidget(e.getSelectedItem());			
		//	this.thisPresenter.FormComplete(tmpHeader.getCommandArgument(),"Edit");
			
			//Should show the selected edit state of ICON
			
		}
		//this.Presenter.FormComplete("StepDetails", "AddAnother");
	}
	@Override
	public void RemoveListItem(String TabName)
	{
		if(TabName!="MethodDetails" && TabName !="SubjectProperties")
		{
			CustomTab removedTab = this.CurrentSteps.get(TabName);
			
			if(removedTab!=null)
			{
	
				int Index = removedTab.getIndex();
				
				this.CurrentSteps.remove(TabName);
				
				//GWT.log(this.GetIndexList());
				this.MethodNavigationStack.showWidget(1,false);
				this.MethodNavigationStack.remove(Index+1);
				ShuffleIndices(Index);
				this.StepIndex = 1;
			}
		}
	}
	
	private void ShuffleIndices(int RemovedIndex)
	{
		Iterator<Entry<String, CustomTab>> it = CurrentSteps.entrySet().iterator();
		while(it.hasNext())
		{
			CustomTab tmpStack = it.next().getValue();
			if(tmpStack.getIndex() > RemovedIndex)
			{
				tmpStack.DecrementIndex();
			}
		}
	}
	@UiHandler("btnNext")
	public void onClick(ClickEvent e)
	{
		
		this.thisPresenter.FormComplete(this.currentForm, "Next");
	}
	/*TODO Implement the handler for navigating from this view
	 * @UiHandler("btnMethodApp")
	public void onClick(ClickEvent e) {
	    
		if (presenter != null) {
		      presenter.onLauncherClicked("Method Builder");
		    }
	  }*/
	
	//TODO - This needs to return a content container for adding other details into

}
