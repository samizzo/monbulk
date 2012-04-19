package monbulk.shared.view;

import monbulk.MethodBuilder.shared.EnumDefinitions;

public interface ISearchFilter {
	public String GetFieldName();
	public Object GetFieldValue();
	public EnumDefinitions.SearchFilters GetSearchFilter();
	
}
