package monbulk.MethodBuilder.shared;

import java.util.ArrayList;

import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.iModel;



public interface iModelAllowMetaData extends iModel {
	public monbulk.shared.Model.StackedCategories getMetaDataCategories();
	
	public interface iModelHasHelpExtendsMetaData extends iModelAllowMetaData
	{
		public monbulk.shared.Model.StackedCategories getHelpCategories();	
		public void AddMetaDataItem(String MetaDataName, String MetaDataParent);
		public ArrayList<String> GetMetaDataList();
		public Boolean RemoveMetaDataItem(String MDName);
		public String ValidateForm();
		public ArrayList<String> GetListData(String ListName);
		public String getStringRpresentation(String Format);
		
		public void setPresenter(FormPresenter presenter);
	}
}
