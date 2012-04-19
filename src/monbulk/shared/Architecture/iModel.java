package daris.Monbulk.shared.Architecture;

import java.util.ArrayList;


import daris.Monbulk.shared.Architecture.IPresenter.FormPresenter;
import daris.Monbulk.shared.Form.FormBuilder;
import daris.Monbulk.shared.Form.FormField;
import daris.Monbulk.shared.Form.iFormField;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Model.StackedCategories;
import daris.Monbulk.shared.view.IResult;
import daris.Monbulk.shared.view.ISearchFilter;

public interface iModel {
	public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) ;

	//Should perform the save process via service
	public void saveData();
	
	//Loads Model data
	public void loadData(String ID);
	public void loadData(IPojo someDataObject);
	
	public interface iFormModel extends iModel
	{
		
		public FormBuilder getFormData();
		public void Update(FormBuilder formData);
		
		
		//Deprecate
		//public Object getFieldValueByName(String FieldName);
		//public void setFieldValue(Object Value, String FieldName);
		//public iFormField getFormItem(String FormName);
		public String ValidateForm();
		
		
		
		public void setPresenter(FormPresenter presenter);
	}
	public interface iListModel
	{
		public ArrayList<IPojo> getFormList(String ListName);
		public StackedCategories getStructuredList(String ListName);
	}
}
