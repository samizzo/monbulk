package daris.Monbulk.shared.Form;

import daris.Monbulk.shared.Form.iFormField.iFormFieldValidation;

public class IntegerValidation implements iFormFieldValidation
{
	private String InvalidReason;
	private String FieldName;
	
	public IntegerValidation(String fName)
	{
		FieldName = fName;
	}
	@Override
	public boolean isValueValid(String value) {
		// TODO Auto-generated method stub
		try
		{
			Integer.parseInt(value);
			return true;
		}
		catch(NumberFormatException ex)
		{
			InvalidReason = this.FieldName + ": Not a valid number";
			return false;
		}
		
	}

	@Override
	public String getInvalidReason() {
		// TODO Auto-generated method stub
		return InvalidReason;
	}
	
}
	
