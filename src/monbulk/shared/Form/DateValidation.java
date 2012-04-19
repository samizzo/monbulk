package monbulk.shared.Form;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import monbulk.shared.Form.iFormField.iFormFieldValidation;

public class DateValidation implements iFormFieldValidation
{
	private String InvalidReason;
	private String FieldName;
	
	public DateValidation(String fName)
	{
		FieldName = fName;
	}
	
	@Override
	public boolean isValueValid(String value) {
		// TODO Auto-generated method stub
		try
		{
			Date newValue = DateTimeFormat.getFormat("dd/mm/yyyy").parse(value);
			Date today = new Date();
			if(newValue.after(today))
			{
				InvalidReason = this.FieldName + ": Can't set a date after today's date";
				return false;
			}
			return true;
		}
		catch(IllegalArgumentException ex)
		{
			InvalidReason = this.FieldName + ": Not a valid date";
			return false;
		}
	}

	@Override
	public String getInvalidReason() {
		// TODO Auto-generated method stub
		return InvalidReason;
	}
}
