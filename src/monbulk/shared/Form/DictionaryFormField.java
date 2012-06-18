package monbulk.shared.Form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ListBox;

import monbulk.shared.Form.iFormField.iFormFieldValidation;
import monbulk.shared.Model.pojo.pojoStudy;
import monbulk.shared.Services.Dictionary;
import monbulk.shared.Services.DictionaryService;
import monbulk.shared.Services.Dictionary.Entry;
import monbulk.shared.Services.DictionaryService.GetDictionaryHandler;
import monbulk.shared.util.GWTLogger;

public class DictionaryFormField extends ListField implements GetDictionaryHandler {

	public class DictionaryValidation implements iFormFieldValidation{
	private String InvalidReason;
	private String FieldName;
	
	public DictionaryValidation(String fName)
	{
		FieldName = fName;
	}
	@Override
	public boolean isValueValid(String value) {
		// TODO Auto-generated method stub
		
		if(value.length() == 0)
		{
			InvalidReason = "Please provide a value for the " + this.FieldName + " field";
			return false;
		}
		else if(value.contains("null"))
		{
			InvalidReason = "Please provide a value for the " + this.FieldName + " field";
			return false;
		}
		else if(value.contains(defaultField))
		{
			InvalidReason = "Please provide a value for the " + this.FieldName + " field";
			return false;
		}
		else
		{
			return true;
		}
		
	}

	@Override
	public String getInvalidReason() {
		
		return InvalidReason;
	}
}
	private String DictionaryName;
	public static String defaultField = "Not Selected";
	public DictionaryFormField(String FormName,String DictionaryName) {
		super(FormName,defaultField);
		this.fieldValidator = new DictionaryValidation(this.FieldName);
		ArrayList<String> items = new ArrayList<String>();
		items.add(defaultField);
		this.hasValue=false;
		super.loadList(items, defaultField);
		this.DictionaryName = DictionaryName;
		this.FieldType="Dictionary";
		this.FieldValue="";
		
		DictionaryService service = DictionaryService.get();
		
		isSummaryField = false;
		isTitleField = false;
		if (service != null)
		{
			
			service.getDictionary(DictionaryName, this);
			GWTLogger.Log("Running", "DictionaryFormField", "getFormStructure", "34");
		}
	}
	
	@Override
	public void onGetDictionary(Dictionary dictionary) {
		if(dictionary.getName()==DictionaryName)
		{
			ListBox tmpBox = (ListBox)this.getWidgetReference().getFormWidget().asWidget();
			
			Collection<Entry> tmpList = dictionary.getEntries();
			Iterator<Entry> i = tmpList.iterator();
			while(i.hasNext())
			{
				Entry item = i.next();
				tmpBox.addItem(item.getTerm());
				this.Values.add(item.getTerm());
			}
			
		}
		
	}

}
