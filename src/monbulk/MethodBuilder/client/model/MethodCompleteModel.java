package monbulk.MethodBuilder.client.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import monbulk.MethodBuilder.shared.iMBModel;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.iModel.iFormModel;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormWidget;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoMethodComplete;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoSubjectProperties;
import monbulk.shared.Services.MethodService;
import monbulk.shared.Services.ServiceRegistry;
import monbulk.shared.Services.MethodService.MethodServiceHandler;
import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.util.MonbulkEnums.ServiceNames;
import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;

public class MethodCompleteModel extends baseModel implements iMBModel, MethodService.MethodServiceHandler{

	private pojoMethodComplete CompleteModel;
	//private FormBuilder formCompleteModel;
	private FormPresenter Presenter; 
	
	private Boolean isLoaded;
	private int StepCount;
	private int CurrentStep;
	public MethodCompleteModel()
	{
		isLoaded = false;
		
		//this.allSteps.RemoveStep(0);
		this.CompleteModel = new pojoMethodComplete();
		super.formData = this.CompleteModel.getFormStructure();
		isLoaded = false;
		CurrentStep=0;
		StepCount=0;
	
		
		
	}
	public void removeStep(String StepFormName)
	{
		String strFormIndex = StepFormName.replace(pojoStepDetails.FormName, "");
		if(strFormIndex.length()>0)
		{
			int FormIndex = Integer.parseInt(strFormIndex);
			
			if(FormIndex < this.StepCount)
			{
				this.CompleteModel.removeStep(FormIndex);
				this.StepCount++;
				this.CurrentStep = 0;
				
			}
			else
			{
				this.CompleteModel.removeStep(this.CurrentStep);
				this.StepCount++;
				this.CurrentStep = 0;
			}
		}
		
		
	}
	public void addStep()
	{
		this.CompleteModel.addStep(new pojoStepDetails(this.StepCount));
		
		this.StepCount++;
	}
	public void addStep(pojoStepDetails stepIn)
	{
		this.CompleteModel.addStep(stepIn);
		this.StepCount++;
	}
	public MethodCompleteModel(String ID, FormPresenter presenter)
	{
		isLoaded = true;
		this.Presenter = presenter;
		this.loadData(ID);
	
	}
	public Boolean isLoaded()
	{
		return this.isLoaded;
	}
	@Override
	public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveData() {
		//this.CompleteModel.saveForm(input)
		
	}
	
	@Override
	public void loadData(String ID) {
		if(ID!="")
		{
			try
			{
				MethodService tmpSvc = MethodService.get();
				if(tmpSvc != null)
				{
					tmpSvc.getMethod(ID, this);
						
				}
				else
				{
					throw new ServiceRegistry.ServiceNotFoundException(ServiceNames.Methods);
				}
			}
			catch (ServiceRegistry.ServiceNotFoundException e)
			{
				GWT.log("Couldn't find Method service");
			}
		}
		else
		{
			this.CompleteModel = new pojoMethodComplete(); 
		}
		
	}

	@Override
	public FormBuilder getFormData() {
		// TODO Auto-generated method stub
		return this.formData;
	}
	public FormBuilder getFormData(String FormName)
	{
		try
		{
		//Window.alert("Loading form" + FormName);
		if(FormName==pojoMethod.FormName)
		{
			return this.CompleteModel.getMethodDetails().getFormStructure();
		}
		else if(FormName==pojoSubjectProperties.FormName)
		{
			return this.CompleteModel.getSubjectProperties().getFormStructure();
			
		}
		else if(FormName.contains(pojoStepDetails.FormName))
		{
			//return this.CompleteModel.`.getFormStructure();
			String strFormIndex = FormName.replace(pojoStepDetails.FormName, "");
			GWT.log("Get here" + strFormIndex);
			if(strFormIndex.length()>0)
			{
				int FormIndex = Integer.parseInt(strFormIndex);
				
				if(FormIndex < this.StepCount)
				{
					GWT.log("FormIndex" + FormIndex);
					this.CurrentStep = FormIndex;
					return this.CompleteModel.getSteps().get(FormIndex).getFormStructure();
					
				}
				else
				{
					GWT.log("FormIndex" + FormIndex + FormName);
					return this.CompleteModel.getSteps().get(this.CurrentStep).getFormStructure();
				}
			}
			else
			{
				pojoStepDetails tmpStep = this.CompleteModel.getSteps().get(this.CurrentStep);
				if(tmpStep!=null)
				{
					GWT.log("Any details" +tmpStep.toString());
					return tmpStep.getFormStructure();
				}
				else
				{
					pojoStepDetails tmpItem = new pojoStepDetails(this.StepCount);
					this.addStep(tmpItem);
					GWT.log("Step Path B");
					return tmpItem.getFormStructure();
				}
			}
		}
		else
		{
			return this.formData;
		}
		}
		catch(Exception ex)
		{
			GWT.log("Error @ MethodCompleteModel.getFormData(String)" + ex.getMessage());
		}
		return formData;
	}

	@Override
	public void setPresenter(FormPresenter presenter) {
		this.Presenter = presenter;
		
	}

	@Override
	public void onReadMethodList(ArrayList<pojoMethod> arrMethods) {
		return;
		
	}
	@Override
	public String ValidateForm() {
		//super.formData = this.formCompleteModel;
		return super.ValidateForm();
	}
	@Override
	public String ValidateForm(String FormName) {
		super.formData = this.getFormData(FormName);
		return super.ValidateForm();
	}
	@Override
	public void onReadMethod(pojoMethodComplete method) {
		
		GWT.log("onReadMethod starts");
		try
		{
			
		this.CompleteModel = method;
		this.StepCount = this.CompleteModel.getStepCount(); 
		//this.formCompleteModel = this.CompleteModel.getFormStructure();
		//this.Presenter.ModelUpdate("GetMethod");
		GWT.log("thus far A");
		isLoaded = true;
		
		this.Presenter.ModelUpdate("GetMethod");
		GWT.log("thus far");
		}
		catch(Exception ex)
		{
			GWT.log("Error occurs @ MethodCompleteModel.onReadMethod" + ex.getMessage());
		}
		
		
	}
	@Override
	public void loadData(IPojo someDataObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void Update(FormBuilder formData) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getStringRpresentation(String Format) {
		
		return this.CompleteModel.writeOutput(Format);
	}
	
	
	

}
