package daris.Monbulk.shared.Form;

import daris.Monbulk.shared.Form.iFormField.iFormFieldValidation;

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
				InvalidReason = this.FieldName + ": Illegal character usage, check you haven't used >, < or & ";
				return false;
			}
			else if(value.length() > 256)
			{
				InvalidReason = this.FieldName + ": String is too long. A Maximum of 256 characters are permitted";
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
