package daris.Monbulk.MethodBuilder.shared;

public class EnumDefinitions {

		public enum SearchFilters{
				GREATER, LESS, EQUAL, NOTEQUAL,LIKE,BEFORE,AFTER,ONTHISDAY,CONTAINS
		};
		public enum ServiceTypes{
			MEDIAFLUX, GWT, REST, SOAP, JSON, XML, EXTERNAL
		};
		public enum ServiceNames{
			Dictionary_Modality,Dictionary_StepTypes,Dictionary_Species, Methods,MetaData  
		};
		public enum ServiceActions{
			GET_UNIQUE, GET_LIST, CREATE, UPDATE, SAVE_AS_FILE, DELETE, QUERY
		};
}
