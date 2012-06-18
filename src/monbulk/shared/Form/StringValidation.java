package monbulk.shared.Form;

import monbulk.shared.Form.iFormField.iFormFieldValidation;

public class StringValidation implements iFormFieldValidation{
	
		private String InvalidReason;
		private String FieldName;
		
		public StringValidation(String fName)
		{
			FieldName = fName;
		}
		@Override
		public boolean isValueValid(String value) {
			// TODO Auto-generated method stub
			if(value.contains("<") || value.contains(">") || value.contains("&"))
			{
				InvalidReason = "<p>Illegal character usage in " + this.FieldName + " , check you haven't used >, < or & </p>";
				return false;
			}
			else if(value.length() > 256)
			{
				InvalidReason = "<p>The text in field " + this.FieldName + " is too long. A Maximum of 256 characters are permitted</p>";
				return false;
			}
			else if(value.length() == 0)
			{
				InvalidReason = "<p>Please provide a value for the " + this.FieldName + " field</p>";
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
