package monbulk.MethodBuilder.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import arc.mf.model.dictionary.TermRef;
import arc.mf.object.CollectionResolveHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;


import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;
import monbulk.MethodBuilder.shared.iMBModel;
import monbulk.MethodBuilder.shared.iModelAllowMetaData.iModelHasHelpExtendsMetaData;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Form.iFormField;
import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;

public class StepModel implements iMBModel,IDraggable {

	private ArrayList<StepDetailsModel> allSteps;
	private int CurrentStepIndex;
	private int CurrentStepTotal;
	private final ArrayList<String> Modalities;
	private final ArrayList<String> StudyTypes;
	private FormPresenter Presenter;
	public StepModel()
	{
		Modalities = new ArrayList<String>();
		buildList("Modality");
		StudyTypes = new ArrayList<String>();
		buildList("StudyTypes");
		allSteps = new ArrayList<StepDetailsModel>();
		CurrentStepIndex = 0;
		CurrentStepTotal = 0;
		StepDetailsModel tmpModel = new StepDetailsModel(CurrentStepIndex);
		
		allSteps.add(tmpModel);
		
	}
	private void buildList(String ListName)
	{
		String dictionaryFQN = "";
		
		if(ListName=="Modality")
		{
			dictionaryFQN = "pssd.dicom.modality"; 
		}
		else
		{
			dictionaryFQN = "pssd.study.types"; 
		}
		arc.mf.model.dictionary.EntryCollectionRef mfModalities = new arc.mf.model.dictionary.EntryCollectionRef(dictionaryFQN);
		CollectionResolveHandler<TermRef> tmpResolver = new CollectionResolveHandler<TermRef>()
		{

			@Override
			public void resolved(List<TermRef> o) throws Throwable {
				// TODO Auto-generated method stub
		//		String details = "";
				Iterator<TermRef> i = o.iterator();
				while(i.hasNext())
				{
					TermRef tmpItem = i.next();
			//		details = details + "id:" + tmpItem.idToString();
					if(tmpItem.dictionary()=="pssd.dicom.modality")
					{
						Modalities.add(tmpItem.idToString()); 
					}
					else
					{
						StudyTypes.add(tmpItem.idToString());  
					}
					
				}
				//Window.alert(details);
			}
	
		};
		mfModalities.resolve(0,50,tmpResolver);

	}

	public void AddNewStep()
	{
		CurrentStepIndex++;
		StepDetailsModel tmpModel = new StepDetailsModel(CurrentStepIndex);
		if(this.Presenter!=null)
		{
			tmpModel.setPresenter(this.Presenter);
		}
		allSteps.add(tmpModel);
		CurrentStepTotal++;
		
		
	}
	public void RemoveStepForFormName(String FormName)
	{
		StepDetailsModel tmpStep = this.GetStepForFormName(FormName);
		Window.alert("Removes STep?" + allSteps.remove(tmpStep));
		CurrentStepTotal--;
	}
	public void RemoveStepForStepTitle(String StepTitle)
	{
		StepDetailsModel tmpStep = this.GetStepForStepTitle(StepTitle);
		allSteps.remove(tmpStep);
		CurrentStepTotal--;
	}
	public void RemoveStep(int StepIndex)
	{
		allSteps.remove(StepIndex);
		//allSteps.remove(o);
		CurrentStepTotal--;
	}
	public StepDetailsModel GetStepForIndex(int StepIndex)
	{
		return allSteps.get(StepIndex);
	}
	public String GetNextStepTitle(String CurrentTitle)
	{
		StepDetailsModel tmpStep =  GetStepForStepTitle(CurrentTitle);
		int nextStepIndex = tmpStep.getFormIndex() + 1;
		if(allSteps.get(nextStepIndex).getFormData()!=null)
		{
			if(allSteps.get(nextStepIndex).getFormData().getFieldItemForName("StepName")!=null)
			{
				return allSteps.get(nextStepIndex).getFormData().getFieldItemForName("StepName").GetFieldValue().toString();
			}
		}
		return "";
		
	}
	public void setCurrentStep(String StepTitle,Boolean isFormName)
	{
		StepDetailsModel tmpStep = null;
		if(isFormName)
		{
			tmpStep =  this.GetStepForFormName(StepTitle);
		}
		else
		{
			tmpStep =  GetStepForStepTitle(StepTitle);	
		}
		CurrentStepIndex = tmpStep.getFormIndex();
	}
	public StepDetailsModel GetStepForFormName(String FormName)
	{
		 Iterator<StepDetailsModel> i = allSteps.iterator();
		 while(i.hasNext())
		 {
			 StepDetailsModel tmpStep = i.next();
			 if(tmpStep.getFormData().getFormName().equals(FormName))
			 {
				 return tmpStep;
			 }
		 }
		 return null;
	}
	public StepDetailsModel GetStepForStepTitle(String StepTitle)
	{
		 Iterator<StepDetailsModel> i = allSteps.iterator();
		 while(i.hasNext())
		 {
			 StepDetailsModel tmpStep = i.next();
			 if(tmpStep.getFormData().getFieldItemForName("StepName").GetFieldValue().equals(StepTitle))
			 {
				 return tmpStep;
			 }
		 }
		 return null;
	}


	@Override
	public String ValidateForm() {
		return allSteps.get(CurrentStepIndex).ValidateForm();
	}

	public ArrayList<String> GetListData(String ListName) {
		
		
		ArrayList<String> tmpArray = new ArrayList<String>();
		
		if(ListName=="Modality")
		{
			//NB: Bug with asynchronicity
			//Need to make service call which then calls the update data when ready
			//this.
			return this.Modalities;
		}
		else if(ListName=="StudyTypes")
		{
			return StudyTypes;
		}
		return tmpArray;
	}
	@Override
	public String getStringRpresentation(String Format) {
		String Output ="";
		Iterator<StepDetailsModel> i =  allSteps.iterator();
		while(i.hasNext())
		{
			StepDetailsModel tmpModel = i.next();
			if(tmpModel.getFormData().getFormDetails().size() >0)
			{
				Output = Output + tmpModel.getStringRpresentation(Format);
			}
		}
		
		
		return Output;
	}
	
	@Override
	public void setPresenter(FormPresenter presenter) {
		// TODO Auto-generated method stub
		Iterator<StepDetailsModel> i = this.allSteps.iterator();
		while(i.hasNext())
		{
			StepDetailsModel tmpItem = i.next();
			tmpItem.setPresenter(presenter);
		}
	}
	@Override
	public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void loadData(String ID) {
		// TODO Auto-generated method stub
		
	}
	public FormBuilder getFormData()
	{
		return allSteps.get(CurrentStepIndex).getFormData();
	}
	
	@Override
	public void loadData(IPojo someDataObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Boolean DragItem(IPojo someItem,Widget inList) {
		// TODO Auto-generated method stub
		return allSteps.get(CurrentStepIndex).DragItem(someItem,inList);
	}
	@Override
	public Boolean DroptItem(IPojo someItem,Widget inList) {
		// TODO Auto-generated method stub
		return allSteps.get(CurrentStepIndex).DroptItem(someItem,inList);
	}
	@Override
	public void Update(FormBuilder formData) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String ValidateForm(String FormName) {
		// TODO Auto-generated method stub
		return null;
	}

}
