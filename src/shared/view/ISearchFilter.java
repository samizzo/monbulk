package daris.Monbulk.shared.view;

import daris.Monbulk.MethodBuilder.shared.EnumDefinitions;

public interface ISearchFilter {
	public String GetFieldName();
	public Object GetFieldValue();
	public EnumDefinitions.SearchFilters GetSearchFilter();
	
}
