package monbulk.shared.Model;

import monbulk.shared.Form.FormBuilder;

public interface IPojo {

	public String writeOutput(String Format);
	
	//This is for xml or JSON or TCL
	public void readInput(String Format,String Input);
	
	public void saveForm(FormBuilder input);
	public FormBuilder getFormStructure();
	public void deserialise();
	public void deserialiseFromList(String XML);
	public void setFieldVale(String FieldName, Object FieldValue);
	public String getFieldVale(String FieldName);
	//Serialize
}
