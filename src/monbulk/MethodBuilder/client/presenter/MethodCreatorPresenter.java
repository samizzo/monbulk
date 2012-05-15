package monbulk.MethodBuilder.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


import arc.gui.gwt.widget.panel.AbsolutePanel;
import arc.gui.gwt.widget.window.Window;
import arc.gui.gwt.widget.window.WindowManager;
import arc.gui.window.WindowProperties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.shared.HandlerManager;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;

import monbulk.MetadataEditor.MetadataList;
import monbulk.MethodBuilder.client.event.ChangeWindowEvent;
import monbulk.MethodBuilder.client.event.ChangeWindowEventHandler;
import monbulk.MethodBuilder.client.model.MethodCompleteModel;
import monbulk.MethodBuilder.client.model.MethodModel;
import monbulk.MethodBuilder.client.model.StepModel;
import monbulk.MethodBuilder.client.model.SubjectPropertiesModel;
import monbulk.MethodBuilder.client.view.AppletStateNavigation;
import monbulk.MethodBuilder.client.view.DockView;
import monbulk.MethodBuilder.client.view.MethodDetailsView;
import monbulk.MethodBuilder.client.view.MethodForm;
import monbulk.MethodBuilder.client.view.StepForm;
import monbulk.MethodBuilder.client.view.SubjectPropertiesForm;
import monbulk.MethodBuilder.client.view.metaDataListIntegrated;
import monbulk.client.event.WindowEvent;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView;
import monbulk.shared.Architecture.IView.IDockView;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Events.DragEvent;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Form.iFormField.iFormFieldValidation;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoMethodComplete;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoSubjectProperties;
import monbulk.shared.Services.MethodService;
import monbulk.shared.Services.MethodService.MethodServiceHandler;
import monbulk.shared.Services.ServiceRegistry;
import monbulk.shared.util.HtmlFormatter;
import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.widgets.Window.appletWindow;
import monbulk.MethodBuilder.shared.IMethodsView;
import monbulk.MethodBuilder.shared.iMBModel;

///I think we can change this just to a view state with a main model that can be queried on each state

public class MethodCreatorPresenter implements FormPresenter{

	private int countLoads;
	//private IView vwMethodCreator;
	//private IDockView vwDockPanel;
	//private ArrayList<PresenterState> AllStates;
	private MethodCreatorStates CurrentState;
	private IDockView ImplementedDockView;
	private IMethodsView ImplementedMethodView;
	private IView NavigationView;
	public enum MethodCreatorStates{
		METHOD_DETAILS,SUBJECT_PROPERTIES,STEP_DETAILS,COMPLETE, INIT
	};
	
	public final HashMap<String,MethodCreatorStates> stateSelector;
	public final HashMap<MethodCreatorStates,IFormView> _AllStates;
	private final MethodCompleteModel mainModel; 
	//private MethodForm methodForm;
	//private MethodModel MethodModel;
	private final HandlerManager eventBus; 
	//private SubjectPropertiesModel SubjectModel;
	private appletWindow tclBox;
	public MethodCreatorPresenter(HandlerManager evtBus)
	{
		stateSelector = new HashMap<String,MethodCreatorStates>();
		_AllStates = new HashMap<MethodCreatorStates,IFormView>();
		this.eventBus = evtBus;
		mainModel = new MethodCompleteModel();
		mainModel.setPresenter(this);
		Construct("");
	
	}
	public void Construct(String ID)
	{	
		//METHOD_DETAILS,SUBJECT_PROPERTIES,STEP_DETAILS,COMPLETE, INIT
		stateSelector.put("METHOD_DETAILS", MethodCreatorStates.METHOD_DETAILS);
		stateSelector.put("SUBJECT_PROPERTIES", MethodCreatorStates.SUBJECT_PROPERTIES);
		stateSelector.put("STEP_DETAILS", MethodCreatorStates.STEP_DETAILS);
		stateSelector.put("COMPLETE", MethodCreatorStates.COMPLETE);
		stateSelector.put("INIT", MethodCreatorStates.INIT);
		this.ImplementedMethodView = new MethodDetailsView();		
		this.ImplementedDockView = new metaDataListIntegrated();		
		this.ImplementedDockView.setPresenter(this);
		countLoads = 0;
		//AllStates = new ArrayList<PresenterState>();
		//tclBox = new appletWindow("TCL Viewer", "TCLViewer", this.eventBus, null);
		
		SetStates(ID);
		bind();
	}
	public MethodCreatorPresenter(HandlerManager evtBus, String ID)
	{
		//Load Method From ID;
		stateSelector = new HashMap<String,MethodCreatorStates>();
		_AllStates = new HashMap<MethodCreatorStates,IFormView>();
		this.eventBus = evtBus;
		mainModel = new MethodCompleteModel(ID,this);
		Construct(ID);
	}
	@Override
	public ArrayList<String> GetDocks() {
		// TODO Auto-generated method stub
		return null;
	}
	private void SetStates(String ID)
	{
			try
			{
			//GWT.log(mainModel.getFormData().toString());
			MethodForm tmpForm = new MethodForm();
			tmpForm.setPresenter(this);			
			SubjectPropertiesForm tmpForm2 = new SubjectPropertiesForm();
			tmpForm2.setPresenter(this);
			StepForm tmpForm3 = new StepForm();
			tmpForm3.setPresenter(this);
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
	//TODO May be redundant code here - CLEAN UP!
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
					//			GWT.log("1");
						if(!this.mainModel.isLoaded())
						{
							tmpView.LoadForm(this.mainModel.getFormData(newState.toString()));
							countLoads++;
							GWT.log("Loaded:" + countLoads);
						}
						
						GWT.log("Pre-SetChild" + this.CurrentState);
						this.ImplementedMethodView.setChild(tmpView.asWidget());
						GWT.log("Post-SetChild" + newState);
						this.ImplementedMethodView.setPresenter(this);
						this.CurrentState=newState;
								//GWT.log("4");
								//this.ImplementedDockView.setTabData(tmpState.getModel().getMetaDataCategories(), "MetaData");
								//GWT.log(tmpState.presenterState.toString());
						GWT.log("cHILD wIDGET:" + tmpView.getClass());
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
	
	///TODO We have to bind custom events for Changing local state
	private void bind()
	{
		 eventBus.addHandler(ChangeWindowEvent.TYPE, 
		    		new ChangeWindowEventHandler(){
		    			public void onChangeWindow(ChangeWindowEvent event){
		    				doChangeWindow(event.getId(),event.getChange());
		    			}   	
		    		});
		return;
	}
	private void doChangeWindow(String WindowName,String Change)
	{
		this.tclBox.hide();
	}
	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub
		//container.add(this.vwMethodCreator.asWidget());
	}

	@Override
	public void go(HasWidgets bodyContainer, HasWidgets dockContainer) {
		// TODO Auto-generated method stub
		//dockContainer.add(this.vwDockPanel.asWidget());
		//this.DockedContainer = Dock;
		//this.BodyContainer= Body;
		
		dockContainer.add(this.ImplementedDockView.asWidget());
		bodyContainer.add(this.ImplementedMethodView.asWidget());
		//bodyContainer.add(w) Add the stack Panel Navigation
		//Creates the New Body Container
		//bodyContainer.add(w)
		this.CurrentState = MethodCreatorStates.INIT;
		this.ChangeState(MethodCreatorStates.METHOD_DETAILS);
		
	}
	

	@Override
	public void FormComplete(String FormName,String Command) {
		IFormView currentView =this._AllStates.get(CurrentState);
		iMBModel tmpModel = this.mainModel;
		if(Command=="Next")
		{
		//	com.google.gwt.user.client.Window.alert("Unable to Save Form.");
			GWT.log("Next and CurrentState is:" + CurrentState + "New State is: " + FormName);
			String Validation = tmpModel.ValidateForm(FormName);
			if(!Validation.equals(""))
			{
				com.google.gwt.user.client.Window.alert("Unable to Save Form." + Validation);
				return;
			}
			
			if(FormName==pojoMethod.FormName)
			{
				//TODO Must ensure next, prev and complete states are submitted to any view/form
				
				this.ChangeState(MethodCreatorStates.SUBJECT_PROPERTIES);
		
				this.ImplementedMethodView.SetMenuIndex(MethodCreatorStates.SUBJECT_PROPERTIES.toString());
				GWT.log("complete Load");
			}
			else if(FormName==pojoSubjectProperties.FormName)
			{
				//TODO Must ensure next, prev and complete states are submitted to any view/form
				this.mainModel.addStep(pojoStepDetails.FormName);
				this.ChangeState(MethodCreatorStates.STEP_DETAILS);
				
			}
		}
		else if(Command=="Prev")
		{
			if(FormName=="SubjectProperties")
			{
				//DONE Must ensure next, prev and complete states are submitted to any view/form
				this.ChangeState(MethodCreatorStates.METHOD_DETAILS);
				this.ImplementedMethodView.SetMenuIndex(MethodCreatorStates.METHOD_DETAILS.toString());
			}	
			if(FormName=="StepDetails")
			{
				//DONE Must ensure next, prev and complete states are submitted to any view/form
				this.ChangeState(MethodCreatorStates.SUBJECT_PROPERTIES);
				this.ImplementedMethodView.SetMenuIndex(MethodCreatorStates.SUBJECT_PROPERTIES.toString());
			}	
		}
		else if(Command=="AddAnother")
		{
			//Current Form Reset
			StepModel revisedModel =(StepModel)tmpModel;
			
			revisedModel.AddNewStep();
			currentView.ClearForm();
			//Current Model - Set New Step
		}
		else if(Command=="Edit")
		{
				GWT.log("Editing" + this.stateSelector.get(FormName));
				this.ChangeState(this.stateSelector.get(FormName));
				//this.ImplementedMethodView.SetMenuIndex("MethodDetails");
			
			
			if(FormName.contains("StepDetails"))
			{
				/*
				this.ChangeState(MethodCreatorStates.STEP_DETAILS);
				PresenterState newState =this.getCurrentPresenterState();
				StepModel newStateModel =(StepModel)newState.getModel();
				
				newStateModel.setCurrentStep(FormName, true);
				newState.getView().LoadForm(newStateModel.getFormData());
				//This works
				this.ImplementedMethodView.SetMenuIndex(FormName);/*/
			}	
		}
		else if(Command=="Delete")
		{
			StepModel revisedModel =(StepModel)tmpModel;
			revisedModel.RemoveStepForFormName(FormName);
			this.ImplementedMethodView.RemoveListItem(FormName);
		}
		else if(Command=="Save")
		{
			
		}
		else if(Command=="Publish")
		{
			
			tclBox.setModal(true);
			tclBox.clear();
			
			HTML tclText = new HTML();
			String strTCL = "";
			strTCL = strTCL + HtmlFormatter.GetHTMLUtilityScript("TCL");
			
			strTCL = strTCL + tmpModel.getStringRpresentation("tcl");
			/*This is in Model
			strTCL = strTCL + HtmlFormatter.GetHTMLTab() + "\"" + HtmlFormatter.GetHTMLNewline();
			strTCL = strTCL + HtmlFormatter.GetHTMLTab() + "set id2 [xvalue id [om.pssd.method.for.subject.update $args]]" + HtmlFormatter.GetHTMLNewline();
			strTCL = strTCL + HtmlFormatter.GetHTMLTab() + "if { $id2 == \"\" } {" + HtmlFormatter.GetHTMLNewline();
			strTCL = strTCL + HtmlFormatter.GetHTMLTabs(2) + "return $id" + HtmlFormatter.GetHTMLNewline();
			strTCL = strTCL + HtmlFormatter.GetHTMLTab() + "} else {" + HtmlFormatter.GetHTMLNewline();
			strTCL = strTCL + HtmlFormatter.GetHTMLTabs(2) + "return $id2" + HtmlFormatter.GetHTMLNewline();
			strTCL = strTCL + HtmlFormatter.GetHTMLTab() + "}" + HtmlFormatter.GetHTMLNewline();
			strTCL = strTCL + "}" + HtmlFormatter.GetHTMLNewline();
			*/
			
			tclText.setHTML(strTCL);
			//tmpPanel2.add(tclText);
			
			//Button ok = new Button("CLOSE WINDOW");
		     // ok.addClickHandler(new ClickHandler() {
		       // public void onClick(ClickEvent event) {
		       // 	tclBox.hide();
		        //}
		      //});
			
			//Should just launch a Window with a Pojo
		      WindowProperties wp = new WindowProperties();
				wp.setModal(true);
				wp.setCanBeResized(true);
				wp.setCanBeClosed(true);
				wp.setCanBeMoved(true);
				wp.setCentered(false);
				wp.setTitle("TCL Viewer");
				wp.setPosition(900, 0);
				wp.setShowFooter(true);
				wp.setShowHeader(true);
				wp.setSize(680, 680);
				wp.setCanBeMaximised(true);
				
				AbsolutePanel tmpWidget = new AbsolutePanel();
				
				Window _win = Window.create(wp);
				_win.setZIndex(1150);
				
				tmpWidget.setOverflow(Overflow.SCROLL);
				tmpWidget.add(tclText);
				//tmpWidget.add(ok);
				//tmpWidget.setWidth(1000);
				//MethodBuilder tmpBuilder= new MethodBuilder(eventBus,(HasWidgets) tmpWidget);
				_win.setContent(tmpWidget);
				_win.bringToFront();
				WindowManager tmpManager = WindowManager.manager();
				tmpManager.add(_win);
				//tmpManager.setDefaultParentWidget(defaultPar)
				_win.show();
				
	
		}
		else if(Command=="Cancel")
		{
			// HACK: This shouldn't be specifying "MethodBuilder" here.  It should
			// know what window it's in, or the type name should be a static that
			// all windows implement.
			eventBus.fireEvent(new WindowEvent("MethodBuilder", WindowEvent.EventType.CloseWindow));
		}
	
	}
	
	@Override
	public void FireDragEvent(DragEvent e) {
		
		
		
		iMBModel RelatedModel = this.mainModel;
		IFormView RelatedView = this._AllStates.get(CurrentState);
		//RelatedModel.
		
		if(RelatedView!=null)
		{
			if(e.getId().equals("true"))
			{
			
				//This takes care of the new list
			///	RelatedModel.AddListItem.AddMetaDataItem(e.getName(),"MetaData");
				
				//This rebuilds the list
			//	this.ImplementedDockView.setTabData(RelatedModel.GetListData("MetaData"), "MetaData");				
				//RelatedView.addListItem(e.getName(),"MetaData");
				this.ImplementedMethodView.setData(RelatedModel.getFormData());

			}
			else
			{
				
				//The Variation is to follow the list index
			//	RelatedModel.RemoveMetaDataItem(e.getName());
				//RelatedView.RemoveListItem("MetaData",e.getIndex());
			//	this.ImplementedDockView.setTabData(RelatedModel.getMetaDataCategories(), "MetaData");
				this.ImplementedMethodView.setData(RelatedModel.getFormData());
				//Option 2
				//this.ImplementedDockView.RemoveListItem("", e.getIndex());

			}
		}
	}
	
	@Override
	public void go(HasWidgets bodyContainer, HasWidgets dockContainer,HasWidgets navContainer) {
			this.NavigationView = new AppletStateNavigation();
			NavigationView.setPresenter(this);
			navContainer.clear();
			navContainer.add(this.NavigationView.asWidget());
			this.go(bodyContainer, dockContainer);
		
	}
	@Override
	public void ModelUpdate(String ServiceName) {
		
	//	Window.alert("Help");//
		if(ServiceName=="GetMethod")
		{
			
			Iterator<Entry<MethodCreatorStates,IFormView>> i = this._AllStates.entrySet().iterator();
			while(i.hasNext())
			{
				Entry<MethodCreatorStates, IFormView> tmpView = i.next();
				
				if(tmpView!=null)
				{
					
					FormBuilder tmpBuilder = this.mainModel.getFormData(tmpView.getKey().toString());
					
					
					if(tmpView!=null)
					{
						
						this.ImplementedMethodView.setData(tmpBuilder);
						tmpView.getValue().LoadForm(tmpBuilder);
						
					}
				}
			}
			//this.getCurrentPresenterState().getView().LoadForm(this.mainModel.getFormData(this.getCurrentPresenterState().presenterState.toString()));
			GWT.log("We get here...MU");
		}
	}
	@Override
	public String UpdateValue(FormBuilder someFormData) {
		
		FormBuilder tmpBuilder = this.mainModel.getFormData(this.CurrentState.toString());
		IFormView tmpView = this._AllStates.get(this.CurrentState);
		
		if(tmpView!=null)
		{
			GWT.log("Not null");
			this.ImplementedMethodView.setData(tmpBuilder);
			//tmpView.LoadForm(tmpBuilder);
			
		}
		else
		{
			return "Form not found";
		}
		return "";
		
	}
	

}

