package monbulk.shared.Architecture;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.shared.util.MonbulkEnums;




public interface IView {
	

	 Widget asWidget();
	 
	 void setPresenter(IPresenter presenter);
	 void setData(ArrayList<String> someList);
	
	 public interface IDockView extends IView
	 {
		 public void setTabData(StackedCategories someData,String TabName);
		 public void setPojoData(ArrayList<IPojo>someData,String TabName);
		 public void setPresenter(monbulk.shared.Architecture.IPresenter.DockedPresenter presenter);
	 }
	 public interface IFormView extends IView
	 {
		 //public FormField getFormItem(String FormName);
		 //public void addListItem(String ListName,String ListItemName);
		 //public void RemoveListItem(String ListName,int Index);
		 public void ClearForm();
		 public void LoadForm(FormBuilder someForm);
		 public FormBuilder getFormData();
		 public MonbulkEnums.viewTypes getViewType();
		 public void setPresenter(monbulk.shared.Architecture.IPresenter.FormPresenter presenter);
	 }
	 public interface IDraggable
	 {
			public Boolean DragItem(IPojo someItem,Widget fromList);
			public Boolean DroptItem(IPojo someItem,Widget toList,String FieldName);
			public void BuildList(HashMap<String,pojoMetaData> someList,String FieldName);
	 }
	 
	 

}
