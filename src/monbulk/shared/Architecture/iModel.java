package monbulk.shared.Architecture;

import java.util.ArrayList;


import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;
import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;

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
		public String ValidateForm(String FormName);
		
		
		public void setPresenter(FormPresenter presenter);
	}
	public interface iListModel
	{
		public ArrayList<IPojo> getFormList(String ListName);
		public StackedCategories getStructuredList(String ListName);
	}
}
