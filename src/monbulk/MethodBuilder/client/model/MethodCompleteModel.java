package daris.Monbulk.MethodBuilder.client.model;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import daris.Monbulk.shared.Architecture.IPresenter.FormPresenter;
import daris.Monbulk.shared.Architecture.iModel.iFormModel;
import daris.Monbulk.shared.Form.FormBuilder;
import daris.Monbulk.shared.Form.iFormField;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Model.pojo.pojoMethod;
import daris.Monbulk.shared.Model.pojo.pojoMethodComplete;
import daris.Monbulk.shared.Services.MethodService;
import daris.Monbulk.shared.Services.ServiceRegistry;
import daris.Monbulk.shared.util.MonbulkEnums;
import daris.Monbulk.shared.view.IResult;
import daris.Monbulk.shared.view.ISearchFilter;

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
			MethodService service = (MethodService)ServiceRegistry.getService(MonbulkEnums.ServiceNames.Methods);
			service.getMethod(ID, this);
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
