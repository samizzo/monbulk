package daris.Monbulk.shared.Model;

import daris.Monbulk.shared.Form.FormBuilder;

public interface IPojo {

	public String writeOutput(String Format);
	public void readInput(String Format);
	
	public void saveForm(FormBuilder input);
	public FormBuilder getFormStructure();
	public void deserialise();
	public void deserialiseFromList(String XML);
	public void setFieldVale(String FieldName, Object FieldValue);
	public String getFieldVale(String FieldName);
	//Serialize
}
