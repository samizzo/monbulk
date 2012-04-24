package monbulk.MethodBuilder.client.model;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.iModel.iFormModel;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Model.pojo.pojoMethodComplete;
import monbulk.shared.Services.MethodService;
import monbulk.shared.Services.ServiceRegistry;
import monbulk.shared.Services.MethodService.MethodServiceHandler;
import monbulk.shared.util.MonbulkEnums;
import monbulk.shared.util.MonbulkEnums.ServiceNames;
import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;

public class MethodCompleteModel implements iFormModel, MethodService.MethodServiceHandler{

	private pojoMethodComplete CompleteModel;
	private FormBuilder formCompleteModel;
	private FormPresenter Presenter; 
	
	public MethodCompleteModel()
	{
		
	}
	public MethodCompleteModel(String ID)
	{
		this.loadData(ID);
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

	@Override
	public FormBuilder getFormData() {
		// TODO Auto-generated method stub
		return this.formCompleteModel;
	}

	@Override
	public String ValidateForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPresenter(FormPresenter presenter) {
		this.Presenter = presenter;
		
	}

	@Override
	public void onReadMethodList(ArrayList<pojoMethod> arrMethods) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReadMethod(pojoMethodComplete method) {
		
		this.CompleteModel = method;
		this.formCompleteModel = this.CompleteModel.getFormStructure();
		this.Presenter.ModelUpdate("GetMethod");
		
		
	}
	@Override
	public void loadData(IPojo someDataObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void Update(FormBuilder formData) {
		// TODO Auto-generated method stub
		
	}
	
	

}
