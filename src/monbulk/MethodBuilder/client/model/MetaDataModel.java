package monbulk.MethodBuilder.client.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;


import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Architecture.iModel;
import monbulk.shared.Architecture.iModel.iListModel;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;
import monbulk.shared.Model.StackedCategories.ParentCategory;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;

public class MetaDataModel implements iModel,iListModel,IDraggable {

	public final StackedCategories MetaDataCategoryList;
	public ArrayList<IPojo> AllMetaDataNames;   
	
	public MetaDataModel()
	{
		this.MetaDataCategoryList = new StackedCategories();
		
		this.MetaDataCategoryList.addParent("MD1","Subject Properties");
		this.MetaDataCategoryList.addParent("MD2","Animal Properties");
		this.MetaDataCategoryList.addParent("MD3","Human Properties");
		this.MetaDataCategoryList.addParent("MD4","Dicom MetaData");
		
		
		
		//Call the Web Service to Launch all Names
	}
	public StackedCategories.ParentCategory GetParentForName(String Name)
	{
		return this.MetaDataCategoryList.getParentForName(Name);
	}
	@Override
	public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public ArrayList<IPojo> getFormList(String ListName) {
		// TODO Auto-generated method stub
		return this.AllMetaDataNames;
	}


	@Override
	public Boolean DragItem(IPojo someItem, Widget item) {
		// If the item Contains name X = then traverse list until we find name and set disabled
		String MetaDataName =someItem.getFieldVale(pojoMetaData.MetaDataNameField);
		if(MetaDataName.contains("animal"))
		{
			MetaDataCategoryList.getParentForName("Animal Properties").getChildItemForName(MetaDataName).setID("disabled");
		}
		else if(MetaDataName.contains("human"))
		{
			MetaDataCategoryList.getParentForName("Human Properties").getChildItemForName(MetaDataName).setID("disabled");
		}
		else if(MetaDataName.contains("dicom"))
		{
			MetaDataCategoryList.getParentForName("Dicom MetaData").getChildItemForName(MetaDataName).setID("disabled");
		}
		else
		{
			MetaDataCategoryList.getParentForName("Subject Properties").getChildItemForName(MetaDataName).setID("disabled");
		}
		return true;
	}

	
	public Boolean DroptItem(IPojo someItem,Widget someTable,String FieldName) {
		// If the item Contains name X = then traverse list until we find name and set enabled
		String MDName =someItem.getFieldVale(pojoMetaData.MetaDataNameField);
		if(MDName.contains("animal"))
		{
			MetaDataCategoryList.getParentForName("Animal Properties").getChildItemForName(MDName).setID("enabled");
			return true;
		}
		else if(MDName.contains("human"))
		{
			MetaDataCategoryList.getParentForName("Human Properties").getChildItemForName(MDName).setID("enabled");
			return true;
		}
		else if(MDName.contains("dicom"))
		{
			MetaDataCategoryList.getParentForName("Dicom MetaData").getChildItemForName(MDName).setID("enabled");
			return true;
		}
		else
		{
			MetaDataCategoryList.getParentForName("Subject Properties").getChildItemForName(MDName).setID("enabled");
			return true;
		}
		
		
	}
	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void loadData(String ID) {
		
		Iterator<IPojo> i = AllMetaDataNames.iterator();
		//Window.alert("Array is: " + tmpArray.size());
		ParentCategory Parent = GetParentForName("Subject Properties");
		ParentCategory Parent2 = GetParentForName("Animal Properties");
		ParentCategory Parent3 = GetParentForName("Human Properties");
		ParentCategory Parent4 = GetParentForName("Dicom MetaData");
				//this.MetaDataCategoryList.getParentForName("SubjectProperties");
		
		while(i.hasNext())
		{
			IPojo tmpPojo = i.next();
			String tmpItem = tmpPojo.getFieldVale(pojoMetaData.MetaDataNameField);	
			//GWT.log(tmpItem.MetaDataName + " " + tmpItem.MetaDataID);
			if(tmpItem.contains("animal"))
			{
				if(Parent2!=null)
				{
					Parent2.AddChild(tmpItem, "enabled",tmpPojo);
					
				}
			}
			else if(tmpItem.contains("human"))
			{
				if(Parent3!=null)
				{
					Parent3.AddChild(tmpItem, "enabled",tmpPojo);
				}
			}
			else if(tmpItem.contains("dicom"))
			{
				if(Parent4!=null)
				{
					Parent4.AddChild(tmpItem, "enabled",tmpPojo);
				}
			}
			else
			{
				Parent.AddChild(tmpItem, "enabled",tmpPojo);
			}
			
			/*while(c.hasNext())
			{
				MetaDataCategories tmpC = (MetaDataCategories) c.next();
							
				if(tmpC.equals(MetaDataCategories.SUBJECT_PROPERTY_EXTENSION))
				{
					
				}
				//(c.next().
			}*/	
		}
		
	}
	@Override
	public void loadData(IPojo someDataObject) {
		this.loadData("");
		
	}
	@Override
	public StackedCategories getStructuredList(String ListName) {
		
		return this.MetaDataCategoryList;
	}

}
