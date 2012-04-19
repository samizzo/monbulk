package daris.Monbulk.MethodBuilder.client.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import daris.Monbulk.MethodBuilder.shared.IMethodsView;
import daris.Monbulk.shared.Architecture.IPresenter;
import daris.Monbulk.shared.Architecture.IPresenter.FormPresenter;
import daris.Monbulk.shared.Form.FormBuilder;
import daris.Monbulk.shared.Form.FormField;
import daris.Monbulk.shared.Form.iFormField;

public class MethodDetailsView extends Composite implements IMethodsView {

	@UiField
	DockLayoutPanel LayoutPanel;
	
	@UiField
	HTML MethodDetailsSummary;
	
	@UiField
	HTML SubjectPropertiesSummary;
	
	@UiField
	StackLayoutPanel MethodNavigationStack;
	
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
		if(IndexName == "MethodDetails")
		{
			this.MethodNavigationStack.showWidget(0);
			
		}
		else if(IndexName == "SubjectProperties")
		{
			this.MethodNavigationStack.showWidget(1);
		}
		else if(IndexName.contains("StepDetails"))
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
		// TODO Auto-generated method stub
		String html ="<table>";
		//while(anyBuilder.)
		//How do we know which step we are on? - form Name
		CustomStackHeader tmpHeader = null;
		HTML tmpBody = null;
		Boolean isNewStep = false;
		if(anyBuilder.getFormName().contains("StepDetails"))
		{
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
					if(tmpField.hasValue())
					{
						html = html + "<tr><td class='fieldName'>" + tmpField.GetFieldName() + ":</td>";
						html = html + "<td class='fieldValue'>" + (String)tmpField.GetFieldValue() + "</td></tr>";
					}
					
				}
				else if(tmpField.isTitle() && tmpField.hasValue() && anyBuilder.getFormName().equals("MethodDetails"))
				{
					//this.MethodTitle.setText((String) tmpField.GetFieldValue());
					
					this.MethodNavigationStack.setHeaderHTML(0,(String) tmpField.GetFieldValue());
				}
				else if(tmpField.isTitle() && tmpField.hasValue() && anyBuilder.getFormName().contains("StepDetails"))
				{
					//Window.alert(tmpField.GetFieldValue().toString());
					//this.MethodTitle.setText((String) tmpField.GetFieldValue());
					String Title = "Step " + this.CurrentSteps.get(anyBuilder.getFormName()).getIndex() + ": " + tmpField.GetFieldValue().toString();
					tmpHeader.setTitle(Title);
					//tmpHeader.setTitle(tmpField.GetFieldValue().toString());
					//this.MethodNavigationStack.add(, header, 41);
				}
			}
		}
		html = html +"</<table>";
		if(anyBuilder.getFormName().equals("MethodDetails"))
		{
			this.MethodNavigationStack.showWidget(0);
			MethodDetailsSummary.setHTML(html);
			
		}
		else if(anyBuilder.getFormName().equals("SubjectProperties"))
		{
			this.MethodNavigationStack.showWidget(1);
			SubjectPropertiesSummary.setHTML(html);
		}
		else if(anyBuilder.getFormName().contains("StepDetails"))
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
	@Override
	public void setChild(Widget someContainer) {
		this.LayoutPanel.addWest(someContainer,480);
		this.ChildContainerIndex = this.LayoutPanel.getWidgetIndex(someContainer);
	}
	@Override
	public void clearChild() {
		//this.LayoutPanel. .addWest(someContainer,480);
		if(this.ChildContainerIndex != 0)
		{
			this.LayoutPanel.remove(this.ChildContainerIndex);
			this.ChildContainerIndex = 0;
		}
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
			this.thisPresenter.FormComplete("MethodDetails", "Edit");
		}
		else if(this.MethodNavigationStack.getVisibleIndex() == 1)
		{
			this.thisPresenter.FormComplete("SubjectProperties", "Edit");
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
				ShuffleIndices(Index);
				GWT.log(this.GetIndexList());
				this.MethodNavigationStack.remove(Index+1);
				this.MethodNavigationStack.showWidget(Index);
				this.StepIndex = Index;
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
	
	/*TODO Implement the handler for navigating from this view
	 * @UiHandler("btnMethodApp")
	public void onClick(ClickEvent e) {
	    
		if (presenter != null) {
		      presenter.onLauncherClicked("Method Builder");
		    }
	  }*/
	
	//TODO - This needs to return a content container for adding other details into

}
