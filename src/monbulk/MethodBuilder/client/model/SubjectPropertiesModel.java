package monbulk.MethodBuilder.client.model;


import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.util.HtmlFormatter;
import monbulk.shared.util.MonbulkEnums.ServiceNames;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;
import monbulk.shared.Model.StackedCategories.ParentCategory;
import monbulk.MethodBuilder.shared.iMBModel;
import monbulk.MethodBuilder.shared.iModelAllowMetaData.iModelHasHelpExtendsMetaData;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Form.iFormField;
import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;




public class SubjectPropertiesModel implements iMBModel,IDraggable{

	public final StackedCategories MetaDataCategoryList;
	public final StackedCategories HelpCategories;
	public FormBuilder SubjectPropertiesForm;
	//TODO The following should be implemented in POJO for serialisation - SHould implement isPublic, isMandatory,DefaultValues
	public ArrayList<String> MetaDataAddedList;
	public String SubjectType;
	public String SubjectName;
	private FormPresenter Presenter;
	public SubjectPropertiesModel()
	{
		this.MetaDataCategoryList = new StackedCategories();
		this.HelpCategories = new StackedCategories();
		this.MetaDataAddedList = new ArrayList<String>();
		
		this.MetaDataCategoryList.addParent("MD1","Subject Properties");
		this.MetaDataCategoryList.addParent("MD2","Animal Properties");
		this.MetaDataCategoryList.addParent("MD3","Human Properties");
		this.MetaDataCategoryList.addParent("MD4","Dicom MetaData");
		
		
		SubjectPropertiesForm = new FormBuilder();
		SubjectPropertiesForm.SetFormName("SubjectProperties");
		SubjectPropertiesForm.AddSummaryItem("SubjectName", "String");
		SubjectPropertiesForm.AddSummaryItem("SubjectType", "String");
		//SubjectPropertiesForm.AddSummaryItem("isPublic", "Boolen",);
		SubjectPropertiesForm.AddItem("isPublic", "Boolean", true);
		//MetaDataList tmpObject = new MetaDataList(this);
	}
	
	@Override
	public String ValidateForm() {
		// TODO Auto-generated method stub
		Iterator<iFormField> i = this.SubjectPropertiesForm.getFormDetails().iterator();
		//Assumedly we should validate as well
		
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			if(!tmpItem.hasValue())
			{
				return "Validation Fails: No Value specified for Field:" + tmpItem.GetFieldName();
			}
		}
		return "";
	}
	
	@Override
	public String getStringRpresentation(String Format) {
		String Output ="";
		String strSubjectType ="";
		FormField thisField = (FormField) this.SubjectPropertiesForm.getFieldItemForName("SubjectType");
		if(thisField!=null)
		{
			if(thisField.hasValue())
			{
				strSubjectType = (String) thisField.GetFieldValue();
			}
		}
		
		//String SubjectType= ":metadata < :definition -requirement mandatory hfi.pssd.subject :value < :type constant(" + this.SubjectType + " > >\\";
		if(Format=="tcl")
		{
			
			String tclSubjectType= ":metadata < :definition -requirement mandatory hfi.pssd.subject :value < :type constant(" + strSubjectType + ") > >\\";
			Output = Output + HtmlFormatter.GetHTMLTabs(2) + ":subject < \\" + HtmlFormatter.GetHTMLNewline();
			Output = Output + HtmlFormatter.GetHTMLTabs(3)+ ":project < \\" + HtmlFormatter.GetHTMLNewline();
			Output = Output + HtmlFormatter.GetHTMLTabs(4)+ ":public < \\" + HtmlFormatter.GetHTMLNewline();
			if(thisField.hasValue())
			{
				Output = Output + HtmlFormatter.GetHTMLTabs(5)+ tclSubjectType + HtmlFormatter.GetHTMLNewline();
			}
		
			Output = Output + HtmlFormatter.GetHTMLMetaDataList("tcl", this.MetaDataAddedList, 5);
			Output = Output + HtmlFormatter.GetHTMLTabs(5)+ ">\\" + HtmlFormatter.GetHTMLNewline();
			Output = Output + HtmlFormatter.GetHTMLTabs(4)+ ">\\" + HtmlFormatter.GetHTMLNewline();
			Output = Output + HtmlFormatter.GetHTMLTabs(3)+ ">\\" + HtmlFormatter.GetHTMLNewline();
			
		}
		return Output;
	}
	
	@Override
	public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPresenter(FormPresenter presenter) {
		this.Presenter = presenter;
		
	}
	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void loadData(String ID) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public FormBuilder getFormData() {
		
		return SubjectPropertiesForm;
	}

	@Override
	public void loadData(IPojo someDataObject) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Boolean DragItem(IPojo someItem,Widget inList) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean DroptItem(IPojo someItem,Widget inList) {
		// TODO Auto-generated method stub
		return null;
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
