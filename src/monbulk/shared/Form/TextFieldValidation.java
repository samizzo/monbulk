package daris.Monbulk.shared.Form;

import daris.Monbulk.shared.Form.iFormField.iFormFieldValidation;

public class TextFieldValidation implements iFormFieldValidation
{
	private String InvalidReason;
	private String FieldName;
	
	public TextFieldValidation(String fName)
	{
		FieldName = fName;
	}
	@Override
	public boolean isValueValid(String value) {
		// TODO Auto-generated method stub
		if(value.contains("<script"))
		{
			InvalidReason = this.FieldName + ": Illegal character usage, check you haven't used <Script ";
			return false;
		}
		else if(value.length() > 1024)
		{
			InvalidReason = this.FieldName + ": String is too long. A Maximum of 1024 characters are permitted";
			return false;
		}
		else
		{
			return true;
		}
		
	}

	@Override
	public String getInvalidReason() {
		// TODO Auto-generated method stub
		return InvalidReason;
	}
	
	
}
