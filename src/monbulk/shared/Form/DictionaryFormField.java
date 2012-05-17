package monbulk.shared.Form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ListBox;

import monbulk.shared.Model.pojo.pojoStudy;
import monbulk.shared.Services.Dictionary;
import monbulk.shared.Services.DictionaryService;
import monbulk.shared.Services.Dictionary.Entry;
import monbulk.shared.Services.DictionaryService.GetDictionaryHandler;
import monbulk.shared.util.GWTLogger;

public class DictionaryFormField extends ListField implements GetDictionaryHandler {

	private String DictionaryName;
	
	public DictionaryFormField(String FormName,String DictionaryName) {
		super(FormName,"Not Selected");
		GWTLogger.Log("Running", "DictionaryFormField", "getFormStructure", "21");
		ArrayList<String> items = new ArrayList<String>();
		items .add("Not Selected");
		GWTLogger.Log("Running", "DictionaryFormField", "getFormStructure", "23");
		super.loadList(items, "Not Selected");
		GWTLogger.Log("Running", "DictionaryFormField", "getFormStructure", "25");
		this.FieldType="Dictionary";
		GWTLogger.Log("Running", "DictionaryFormField", "getFormStructure", "27");
		DictionaryService service = DictionaryService.get();
		GWTLogger.Log("Running", "DictionaryFormField", "getFormStructure", "29");
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
			}
		}
		
	}

}
