package daris.Monbulk.shared.Architecture;

import java.util.ArrayList;
import com.google.gwt.user.client.ui.Widget;
import daris.Monbulk.shared.Form.FormField;
import daris.Monbulk.shared.Form.FormBuilder;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Model.StackedCategories;




public interface IView {
	

	 Widget asWidget();
	 
	 void setPresenter(IPresenter presenter);
	 void setData(ArrayList<String> someList);
	
	 public interface IDockView extends IView
	 {
		 public void setTabData(StackedCategories someData,String TabName);
		 public void setPojoData(ArrayList<IPojo>someData,String TabName);
		 public void setPresenter(daris.Monbulk.shared.Architecture.IPresenter.DockedPresenter presenter);
	 }
	 public interface IFormView extends IView
	 {
		 //public FormField getFormItem(String FormName);
		 //public void addListItem(String ListName,String ListItemName);
		 //public void RemoveListItem(String ListName,int Index);
		 public void ClearForm();
		 public void LoadForm(FormBuilder someForm);
		 public FormBuilder getFormData();
		 
		 public void setPresenter(daris.Monbulk.shared.Architecture.IPresenter.FormPresenter presenter);
	 }
	 public interface IDraggable
	 {
			public Boolean DragItem(IPojo someItem);
			public Boolean DroptItem(IPojo someItem);
	 }
	 

}
