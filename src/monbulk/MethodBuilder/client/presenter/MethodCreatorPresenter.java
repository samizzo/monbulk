package monbulk.MethodBuilder.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;




import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

import monbulk.MethodBuilder.client.PreviewWindow;
import monbulk.MethodBuilder.client.PreviewWindow.SupportedFormats;
import monbulk.MethodBuilder.client.event.ChangeWindowEvent;
import monbulk.MethodBuilder.client.event.ChangeWindowEventHandler;
import monbulk.MethodBuilder.client.model.MethodCompleteModel;
import monbulk.MethodBuilder.client.view.AppletStateNavigation;
import monbulk.MethodBuilder.client.view.MethodDetailsView;
import monbulk.MethodBuilder.client.view.MethodForm;
import monbulk.MethodBuilder.client.view.StepForm;
import monbulk.MethodBuilder.client.view.SubjectPropertiesForm;

import monbulk.client.desktop.Desktop;
import monbulk.client.event.WindowEvent;

import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView;
import monbulk.shared.Architecture.IView.IDockView;
import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Events.DragEvent;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoStudy;
import monbulk.shared.Model.pojo.pojoSubjectProperties;
import monbulk.shared.util.GWTLogger;
import monbulk.shared.util.HtmlFormatter;
import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.util.MonbulkEnums.viewTypes;
import monbulk.shared.widgets.Window.OkCancelWindow.OkCancelHandler;
import monbulk.shared.widgets.Window.appletWindow;

import monbulk.MethodBuilder.shared.IMethodsView;
import monbulk.MethodBuilder.shared.iMBModel;

///I think we can change this just to a view state with a main model that can be queried on each state

public class MethodCreatorPresenter implements FormPresenter, ChangeWindowEventHandler{

	private int countLoads;
	private MethodCreatorStates CurrentState;
	private IMethodsView ImplementedMethodView;
	private IView NavigationView;
	
	public enum MethodCreatorStates{
		METHOD_DETAILS,SUBJECT_PROPERTIES,STEP_DETAILS,COMPLETE, INIT
	};
	public enum SupportedMethodCommands{
		NEW,EDIT,NEXT,PREV,ADD_STEP,REMOVE_STEP,PREVIEW,SAVE,PUBLISH,DELETE_METHOD,CANCEL,EXPAND
	}
	
	public final HashMap<String,MethodCreatorStates> stateSelector;
	public final HashMap<MethodCreatorStates,IFormView> _AllStates;
	private final MethodCompleteModel mainModel; 
	private final HandlerManager eventBus; 
	
	
	public MethodCreatorPresenter(HandlerManager evtBus)
	{
		GWTLogger.Log("MCP Construct", "MCP", "Construct", "82");
		
		stateSelector = new HashMap<String,MethodCreatorStates>();
		_AllStates = new HashMap<MethodCreatorStates,IFormView>();
		this.eventBus = evtBus;
		mainModel = new MethodCompleteModel();
		mainModel.setPresenter(this);
		Construct("");
	}
	
	public MethodCreatorPresenter(HandlerManager evtBus, String ID)
	{
		GWTLogger.Log("MCP Construct with ID", "MCP", "Construct", "117");
		//Load Method From ID;
		stateSelector = new HashMap<String,MethodCreatorStates>();
		_AllStates = new HashMap<MethodCreatorStates,IFormView>();
		this.eventBus = evtBus;
		mainModel = new MethodCompleteModel(ID,this);
		mainModel.setPresenter(this);
		Construct(ID);
	}
	/*Section Initializing */
	/**
	 * Generalized constructor
	 * @param ID
	 */
	public void Construct(String ID)
	{	
		//METHOD_DETAILS,SUBJECT_PROPERTIES,STEP_DETAILS,COMPLETE, INIT
		stateSelector.put("METHOD_DETAILS", MethodCreatorStates.METHOD_DETAILS);
		stateSelector.put("SUBJECT_PROPERTIES", MethodCreatorStates.SUBJECT_PROPERTIES);
		stateSelector.put("STEP_DETAILS", MethodCreatorStates.STEP_DETAILS);
		stateSelector.put("COMPLETE", MethodCreatorStates.COMPLETE);
		stateSelector.put("INIT", MethodCreatorStates.INIT);
		this.ImplementedMethodView = new MethodDetailsView();
		this.ImplementedMethodView.setPresenter(this);
		this.NavigationView = new AppletStateNavigation();
		this.NavigationView.setPresenter(this);
		ArrayList<String> tmpList =  new ArrayList<String>();
		if(ID=="")
		{
			tmpList.add("New");
		}
		else
		{
			tmpList.add("Loaded");
		}
		this.NavigationView.setData(tmpList);
		countLoads = 0;
		SetStates(ID);
		bind();
	}
	/**
	 * Sets up default states
	 * @param ID
	 */
	private void SetStates(String ID)
	{
			try
			{
			//GWT.log(mainModel.getFormData().toString());
			MethodForm tmpForm = new MethodForm(this);
			SubjectPropertiesForm tmpForm2 = new SubjectPropertiesForm(this);
			StepForm tmpForm3 = new StepForm(this);
			
			this._AllStates.put(MethodCreatorStates.METHOD_DETAILS, tmpForm);
			this._AllStates.put(MethodCreatorStates.SUBJECT_PROPERTIES, tmpForm2);
			this._AllStates.put(MethodCreatorStates.STEP_DETAILS, tmpForm3);
			
			
			}
			catch(Exception ex)
			{
				GWT.log("Error Occurs @ MethodCreatorPresentor.SetStates" + ex.getMessage());
			}
	//
		
		
	}
	/**
	 * Binds local Events to this presenter
	 * none implemented yet
	 */
	private void bind()
	{
		 eventBus.addHandler(ChangeWindowEvent.TYPE, this);
		return;
	}
	/**
	 * go(HasWidgets Container) - loads a single panel
	 */
	@Override
	public void go(HasWidgets container) {
		
		container.add(this.ImplementedMethodView.asWidget());
	}
	/**
	 * Loads two panels
	 * Navigation and Body
	 * This is called by a parent class which loads panels into a layout 
	 */
	@Override
	public void go(HasWidgets bodyContainer, HasWidgets navContainer) {
		
		bodyContainer.add(this.ImplementedMethodView.asWidget());
		navContainer.clear();
		navContainer.add(this.NavigationView.asWidget());
		//Load initial state
		this.CurrentState = MethodCreatorStates.INIT;
		this.ChangeState(MethodCreatorStates.METHOD_DETAILS);
		
	}
	
	/*#Section State Changing */
	/**
	 * The base change State Function - possibly not necessary any more as state transition are no longer general
	 * @param newState
	 */
	private void ChangeState(MethodCreatorStates newState)
	{
		
			if(this.CurrentState != null)
			{
				try
				{			
					IFormView tmpView = this._AllStates.get(newState);
					
					if(tmpView!=null)
					{	
						this.ImplementedMethodView.clearChild();
						GWT.log("State change 1 - loads a form");
						
						FormBuilder tmpForm = this.mainModel.getFormData(newState.toString());
						if(tmpForm!=null)
						{
							GWT.log("State change 3");			
							tmpView.ClearForm();
							tmpView.LoadForm(tmpForm);
							
						}
						else
						{
								GWT.log("@MethodCreatorPresenter.ChangeState: Could not find a form for:" + newState);
						}
						
						this.ImplementedMethodView.setChild(tmpView.asWidget());
						this.ImplementedMethodView.setPresenter(this);
						this.CurrentState=newState;
						GWT.log("cHILD wIDGET:" + tmpView.getClass());
						GWT.log("cHILD wIDGET:" + tmpView.asWidget().toString());
						
						if(tmpView.getViewType()==MonbulkEnums.viewTypes.DRAGDROP)
						{
							IDraggable vw = (IDraggable)tmpView;
							
							vw.BuildList(this.mainModel.getMetaDataList(pojoSubjectProperties.SubjectMetaDataField),pojoSubjectProperties.SubjectMetaDataField);
							
						}
						return;
					}
				}
				catch(Exception ex)
				{
					GWT.log("Error Occurs @ MethodCreatorPresenter.ChangeState" + ex.getMessage());
				}
		}
		else
		{
			this.CurrentState = MethodCreatorStates.INIT;
			this.ChangeState(MethodCreatorStates.METHOD_DETAILS);
		}
		
	}
	/**
	 * The state transition logic
	 */
	@Override
	public void FormComplete(String FormName,String Command) {
		Desktop d = Desktop.get();
		final PreviewWindow m = (PreviewWindow )d.getWindow("MethodPreviewWindow");
		IFormView currentView =this._AllStates.get(CurrentState);
		
		if(FormName==null)
		{
			//Window.alert("This form is not valid, Please enter some details");
			m.showInvalid("This form is not valid, Please enter some details", "Save Method Details",FormName);
			d.show(m, true);
			return;
		}
		//Transition between states
		if(Command=="Next")
		{
			GWT.log("Next and CurrentState is:" + CurrentState + "New State is: " + FormName);
			//FIX: Validation fails for some formFields
			String Validation = this.mainModel.ValidateForm(FormName);
			
			if(FormName==pojoMethod.FormName)
			{
				
				if(Validation.length()!=0)
				{
					//Window.alert("The Method Details form is not valid:" + Validation);
					m.showInvalid("" + Validation, "Save Method Details",FormName);
					d.show(m, true);
	//				d.show(window, centre)
					return;				
				}
				else
				{
					this.ChangeState(MethodCreatorStates.SUBJECT_PROPERTIES);
					this.ImplementedMethodView.SetMenuIndex(MethodCreatorStates.SUBJECT_PROPERTIES.toString());
					
					GWT.log("complete Load");
				}
			}
			else if(FormName==pojoSubjectProperties.FormName)
			{
				if(Validation.length()!=0)
				{
					m.showInvalid(Validation, "Save Subject Properties",FormName);
					d.show(m, true);
					//Window.alert("The Subject form is not valid:" + Validation);
					return;
				}
				else
				{
					IFormView tmpView = this._AllStates.get(MethodCreatorStates.STEP_DETAILS);
					this.ImplementedMethodView.clearChild();
					pojoStepDetails tmpPojo1 = this.mainModel.getFirstStep();
					if(tmpPojo1!=null)
					{
						FormBuilder tmpForm  = tmpPojo1.getFormStructure();
						if(tmpForm!=null)
						{
							GWT.log("State change 3");			
							tmpView.LoadForm(tmpForm);
							this.ImplementedMethodView.setData(tmpForm);
							this.ImplementedMethodView.setChild(tmpView.asWidget());
							this.CurrentState=this.stateSelector.get(MethodCreatorStates.STEP_DETAILS);
						}
					}
				}
			}
			else if(FormName.contains(pojoStepDetails.FormName))
			{
				if(Validation.length()!=0)
				{
					m.showInvalid(Validation, "Save Step Details",FormName);
					d.show(m, true);
					return;
				}
				else
				{
					IFormView tmpView = this._AllStates.get(MethodCreatorStates.STEP_DETAILS);
					this.ImplementedMethodView.clearChild();
					pojoStepDetails tmpPojo = this.mainModel.getNextStep(FormName); 
					tmpView.ClearForm();
					if(tmpPojo!=null)
					{
						FormBuilder tmpForm  = tmpPojo.getFormStructure();
						if(tmpForm!=null)
						{
							GWT.log("State change 3");			
							//this.ImplementedMethodView.setData(tmpForm);
							tmpView.LoadForm(tmpForm);
							this.ImplementedMethodView.setData(tmpForm);
							
							this.ImplementedMethodView.setChild(tmpView.asWidget());
							this.CurrentState=this.stateSelector.get(MethodCreatorStates.STEP_DETAILS);
						}
					}
					
				}
			}
		}
		//Edits any form
		else if(Command=="Edit")
		{
			if(FormName.contains(pojoStepDetails.FormName))
			{
				this.CurrentState=this.stateSelector.get(MethodCreatorStates.STEP_DETAILS);
				IFormView tmpView = this._AllStates.get(MethodCreatorStates.STEP_DETAILS);
				this.ImplementedMethodView.clearChild();
				pojoStepDetails tmpPojo = this.mainModel.getStep(FormName); 
				tmpView.ClearForm();
				if(tmpPojo!=null)
				{
					FormBuilder tmpForm  = tmpPojo.getFormStructure();
					//com.google.gwt.user.client.Window.alert(tmpPojo.getFormStructure().getFormDetails().toString() + tmpPojo.getFormStructure().Details().size());
					//tmpPojo.getFormStructure().getFormDetails()
					if(tmpForm!=null)
					{
						GWT.log("State change 3");			
						//this.ImplementedMethodView.setData(tmpForm);
						tmpView.LoadForm(tmpForm);
						if(tmpView.getViewType()==MonbulkEnums.viewTypes.DRAGDROP)
						{
							IDraggable vw = (IDraggable)tmpView;
							
							vw.BuildList(tmpPojo.getMetaData(pojoStepDetails.SubjectMetaDataField), pojoStepDetails.SubjectMetaDataField);
							vw.BuildList(tmpPojo.getMetaData(pojoStudy.STUDY_METADATA), pojoStudy.STUDY_METADATA);
						}
						this.ImplementedMethodView.setData(tmpForm);						
						this.ImplementedMethodView.setChild(tmpView.asWidget());
					}
				}
				
				
			}
			else
			{
				GWT.log("Editing" + this.stateSelector.get(FormName));
				FormBuilder tmpForm = this.mainModel.getFormData(FormName);
				this.ImplementedMethodView.clearChild();
				
				IFormView tmpView = this._AllStates.get(this.stateSelector.get(FormName));
				tmpView.ClearForm();
				tmpView.LoadForm(tmpForm);
				if(tmpView.getViewType()==MonbulkEnums.viewTypes.DRAGDROP)
				{
					IDraggable vw = (IDraggable)tmpView;
					
					vw.BuildList(this.mainModel.getMetaDataList(pojoSubjectProperties.SubjectMetaDataField),pojoSubjectProperties.SubjectMetaDataField);
					
				}
				//tmpView.LoadForm(tmpForm);				
				//GWT.log("Pre-SetChild" + this.CurrentState);
				this.ImplementedMethodView.setChild(tmpView.asWidget());
				//GWT.log("Post-SetChild" + newState);
				this.ImplementedMethodView.setPresenter(this);
				this.CurrentState=this.stateSelector.get(FormName);
				
			}
				
		}
		//Delete a Step
		else if(Command=="Delete")
		{
			MethodCompleteModel delModel = this.mainModel;
			delModel.removeStep(FormName);
			this.ImplementedMethodView.RemoveListItem(FormName);
			this.ImplementedMethodView.clearChild();
			this.ChangeState(MethodCreatorStates.SUBJECT_PROPERTIES);
			this.ImplementedMethodView.SetMenuIndex(pojoSubjectProperties.FormName);
		}
		//Saves a Method into MFlux
		else if(Command=="Save")
		{
			//Validate
			StringBuilder strTCL = new StringBuilder();
			strTCL.append(this.mainModel.getStringRpresentation("XML"));
			String valid = this.mainModel.ValidateForm();
			String valid2 = valid.replaceAll("<br/>", "");
			if(valid2.length()>0)
			{	
				m.showInvalid(valid, "Save Method",FormName);
			}
			else
			{
				m.loadPreview(this.mainModel.getStringRpresentation("XML"), SupportedFormats.XML,this.mainModel.getMethodID());
			}
			d.show(m, true);
		}
		else if(Command=="Clone")
		{
			if(this.mainModel.getMethodID()==null)
			{	
				m.getWindowSettings().windowTitle = "ERROR";
				m.showInvalid("No Method Selected", "Clone Method",FormName);
			}
			else
			{
				String valid = this.mainModel.ValidateForm();
				String valid2 = valid.replaceAll("<br/>", "");
				if(valid2.length()>0)
				{	
					m.showInvalid(valid, "Save Method",FormName);
				}
				else
				{
					StringBuilder strTCL = new StringBuilder();
					this.mainModel.setAsClone();
					strTCL.append(this.mainModel.getStringRpresentation("XML"));
					m.cloneMethod(this.mainModel.getStringRpresentation("XML"), SupportedFormats.XML,this.mainModel.getMethodName());
				}
			}
			d.show(m, true);
		}
		else if(Command=="DeleteMethod")
		{
			if(this.mainModel.getMethodID()==null)
			{
				m.getWindowSettings().windowTitle = "ERROR";
				m.showInvalid("No Method Selected", "Delete Method",FormName);
			}
			else
			{
				m.getWindowSettings().windowTitle = "DELETE";
				m.confirmDelete(this.mainModel.getMethodID());
			}
			d.show(m, true);
		}
		else if(Command=="Publish")
		{
			StringBuilder strTCL = new StringBuilder();
			strTCL.append(HtmlFormatter.GetHTMLUtilityScript("TCL"));
			strTCL.append(this.mainModel.getStringRpresentation("TCL"));
			strTCL.append(HtmlFormatter.GetUtilityWriteScript());
			m.loadPreview(strTCL, SupportedFormats.TCL);
		    d.show(m, true);
		}
		else if(Command=="Cancel")
		{
			eventBus.fireEvent(new WindowEvent("MethodBuilder", WindowEvent.EventType.CloseWindow));
		}
	
	}
	@Override
	public void FireDragEvent(DragEvent e) {
		String DataObjectType = e.getName();
		String DataFieldName = e.getId();
		int Action = e.getIndex();
		//0 = Add
		//1= remove
		IPojo relatedPOJO = e.getPojo();
		
			if(Action ==0)
			{
				if(DataObjectType==pojoMetaData.FormName)
				{
					this.mainModel.loadMetaData(DataFieldName, (pojoMetaData) relatedPOJO);
					///this.ImplementedMethodView.setData(anyBuilder)
				}
			}
			if(Action==1)
			{
				if(DataObjectType==pojoMetaData.FormName)
				{
					this.mainModel.removeMetaData(DataFieldName, (pojoMetaData) relatedPOJO);
					//this.mainModel.loadMetaData(DataFieldName, relatedPOJO);
				}
			}
			if(Action==2)
			{
				this.mainModel.loadMetaData(DataFieldName, (pojoMetaData) relatedPOJO);
			}
		
		//this.mainModel.
		
	}
	
	
	@Override
	public void ModelUpdate(String ServiceName) {
		
		GWTLogger.Log("Model Updates", "MCP", "ModelUpdate", "594");
		if(ServiceName=="GetMethod")
		{
			
			Iterator<Entry<MethodCreatorStates,IFormView>> i = this._AllStates.entrySet().iterator();
			
			while(i.hasNext())
			{
				Entry<MethodCreatorStates, IFormView> tmpView = i.next();
				String formName = tmpView.getKey().toString();
				if(tmpView!=null)
				{
					
					if(tmpView.getKey().toString().contains(pojoStepDetails.FormName))
					{
						int j=0;
						while(j<this.mainModel.getStepCount())
						{
							String tmpformName = tmpView.getKey().toString() + j;
							
							FormBuilder tmpBuilder = this.mainModel.getFormData(tmpformName);
							if(tmpBuilder!=null)
							{
								this.ImplementedMethodView.setData(tmpBuilder);
							}
							j++;
						}
					}
					else
					{
						FormBuilder tmpBuilder = this.mainModel.getFormData(formName);
						if(tmpView!=null)
						{
							
							this.ImplementedMethodView.setData(tmpBuilder);
							tmpView.getValue().LoadForm(tmpBuilder);
							if(tmpView.getValue().getViewType()==viewTypes.DRAGDROP)
							{
								IDraggable vw = (IDraggable)tmpView.getValue();
								
								vw.BuildList(this.mainModel.getMetaDataList(pojoSubjectProperties.SubjectMetaDataField), pojoSubjectProperties.SubjectMetaDataField);
							}
							
						}
					}
				}
			}
			
			//this.getCurrentPresenterState().getView().LoadForm(this.mainModel.getFormData(this.getCurrentPresenterState().presenterState.toString()));
			GWT.log("We get here...MU");
			this.ChangeState(MethodCreatorStates.METHOD_DETAILS);
			this.ImplementedMethodView.SetMenuIndex(pojoMethod.FormName);
		}
		if(ServiceName=="CheckUse")
		{
			if(this.mainModel.checkInUse())
			{
				ArrayList<String> tmpList = new ArrayList<String>();
				tmpList.add("InUse");
				this.NavigationView.setData(tmpList);
			}
		}
	}
	/**
	 * Update the form when an individual item is modified	
	 */
	@Override
	public String UpdateValue(FormBuilder someFormData) {
		
		this.mainModel.Update(someFormData);
		//FormBuilder tmpBuilder = this.mainModel.getFormData(this.CurrentState.toString());
		//GWT.log(someFormDa)
		IFormView tmpView = this._AllStates.get(this.CurrentState);
		
		if(tmpView!=null)
		{
			GWT.log("Not null");
			this.ImplementedMethodView.setData(someFormData);
			
		}
		else
		{
			
			this.ImplementedMethodView.setData(someFormData);
		}
		return "";
		
	}

	@Override
	public void onChangeWindow(ChangeWindowEvent event) {
		String CurrentForm = this.ImplementedMethodView.getCurrentForm();
		if(CurrentForm.contains(pojoStepDetails.FormName))
		{
			this.FormComplete(CurrentForm, "Edit");
		}
		else
		{
			this.ChangeState(this.CurrentState);
		}
		
	}
}

