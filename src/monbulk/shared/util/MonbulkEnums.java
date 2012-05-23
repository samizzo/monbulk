package monbulk.shared.util;

public class MonbulkEnums {
	public enum SearchFilters{
		GREATER, LESS, EQUAL, NOTEQUAL,LIKE,BEFORE,AFTER,ONTHISDAY,CONTAINS
	};
	public enum ServiceTypes{
		MEDIAFLUX, GWT, REST, SOAP, JSON, XML, EXTERNAL
	};
	public enum ServiceNames{
		Dictionary_Modality,Dictionary_StepTypes,Dictionary_Species, Methods,MetaData, Dictionary  
	};
	public enum ServiceActions{
		GET_UNIQUE, GET_LIST, CREATE, UPDATE, SAVE_AS_FILE, DELETE, QUERY
	};
	public enum WindowStates{
		MINIMIZED, MAXIMIZED, OPEN_DEFAULT, CLOSED
	};
	public enum WindowTypes{
		DEFAULT, STATIC, THEMED, MODAL 
	}
	public enum viewTypes{
		DRAGDROP, DOCKABLE, FORM, WIDGET
	}
}
