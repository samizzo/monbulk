package monbulk.MethodBuilder.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Architecture.IView;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.iFormField;

public interface IMethodsView extends IView{
	 public void setMethodData(ArrayList<iFormField> StaticFieldList);
	 public void setSubjectData(ArrayList<iFormField> StaticFieldList);
	 public void setStepData(ArrayList<iFormField> StaticFieldList);
	 public HasWidgets GetChildContainer();
	 public void setChild(Widget w);
	 public void clearChild();
	 public void setData(FormBuilder anyBuilder);
	 public void SetMenuIndex(String IndexName);
	 public void RemoveListItem(String ListItemName);
}
