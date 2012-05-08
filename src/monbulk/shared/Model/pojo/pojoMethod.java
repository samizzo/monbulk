package monbulk.shared.Model.pojo;

import java.util.Date;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;



import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.util.HtmlFormatter;

public class pojoMethod implements IPojo {

	private String MethodName;
	private String MethodNameSpace;
	private String MethodDescription;
	private String MethodAuthor;
	private String DateCreated;
	private String KeyWords;
	private String MethodID;
	
	public  static final String FormName = "MethodDetails";
	public static final String MethodNameField = "Title";
	public static final String MethodDescriptionField = "Description";
	public static final String MethodKeywordsField = "Keywords";
	public static final String MethodDateCreatedField = "DateCreated";
	public static final String MethodNamespaceField = "Namespace";
	public static final String MethodAuthorField = "Author";
	public static final String MethodIDField = "ID";
	
	private FormBuilder MethodForm = new FormBuilder();
	public pojoMethod()
	{
		this.MethodNameSpace ="pssd/methods";
		MethodForm = new FormBuilder();
		this.buildForm();
	}
	
	public String writeTCL() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
					String Output="";
					
					String MethodName = "" + this.MethodName;
					String MethodDescription = "" + this.MethodDescription;
					
					String MethodID = MethodName.replace(" ", "_");
					
					//Eventually will be xsd transform
					
						Output = Output +"proc " + MethodID + " { { action 0 } } {" + HtmlFormatter.GetHTMLNewline(); 
						Output = Output + HtmlFormatter.GetHTMLTab() + "set name \"" + MethodName + "\"" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + "set description \"" + MethodDescription + "\"" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + "set margs \"\"" + HtmlFormatter.GetHTMLNewline(); 	
						Output = Output + HtmlFormatter.GetHTMLTab() + "set id [getMethodId $name]" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + "set margs [setMethodUpdateArgs $id $action]" + HtmlFormatter.GetHTMLNewline();	    
						Output = Output + HtmlFormatter.GetHTMLTab() + "if { $margs == \"quit\" } {]" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + HtmlFormatter.GetHTMLTab() + "return" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + "}" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + "set args \"${margs} \\" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + HtmlFormatter.GetHTMLTab() + ":namespace " + this.MethodNameSpace +  " \\" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + HtmlFormatter.GetHTMLTab() + ":name ${name} \\" + HtmlFormatter.GetHTMLNewline();
						Output = Output + HtmlFormatter.GetHTMLTab() + HtmlFormatter.GetHTMLTab() + ":description ${description} \\" + HtmlFormatter.GetHTMLNewline();
					
						//We may add metaData to set keywords, data created and author
					return Output;
	}

	private void buildForm()
	{

		MethodForm.SetFormName(FormName);
		if(this.MethodID==null)
		{
			
			MethodForm.AddItem(MethodIDField,"String","Test Value");
		}
		else
		{
			
			MethodForm.AddItem(MethodIDField,"String",MethodID);
		}
		if(this.MethodAuthor==null)
		{	
			MethodForm.AddSummaryItem(MethodAuthorField, "String","Test Author");	
		}
		else
		{
			MethodForm.AddSummaryItem(MethodAuthorField,"String", this.MethodAuthor);
		}
		if(this.MethodDescription== null)
		{
			MethodForm.AddSummaryItem(MethodDescriptionField, "String");	
		}
		else
		{
			MethodForm.AddSummaryItem(MethodDescriptionField, "String", this.MethodDescription);
		}
		if(this.MethodName==null)
		{
			MethodForm.AddTitleItem(MethodNameField, "String");	
		}
		else
		{
			
			MethodForm.AddTitleItem(MethodNameField, "String",this.MethodName);
		}
		if(this.DateCreated==null)
		{
			Date today = new Date();
			this.setDateCreated(today.toString());
			MethodForm.AddTitleItem(MethodDateCreatedField, "String",today.toString());	
		}
		else
		{
			MethodForm.AddTitleItem(MethodDateCreatedField, "String",this.DateCreated);
		}
		if(this.KeyWords== null)
		{
			MethodForm.AddTitleItem(MethodKeywordsField, "String");	
		}
		else
		{
			MethodForm.AddTitleItem(MethodKeywordsField, "String",this.KeyWords);
		}

	}
	@Override
	public FormBuilder getFormStructure() {
		// if we haven't loaded a form - set defaults
				
		//MethodForm.AddItem("DataUsage", "String");
		return MethodForm;
	}

	@Override
	public void deserialise() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deserialiseFromList(String XML) {
		// TODO Auto-generated method stub
		
	}
	public String getMethodAuthor() {
		return MethodAuthor;
	}
	public void setMethodAuthor(String methodAuthor) {
		MethodAuthor = methodAuthor;
		this.MethodForm.getFieldItemForName(this.MethodAuthorField).SetFieldValue(methodAuthor);
	}
	public String getDateCreated() {
		return DateCreated;
	}
	public void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}
	public String getKeyWords() {
		return KeyWords;
	}
	public void setKeyWords(String keyWords) {
		KeyWords = keyWords;
	}
	public String getMethodID() {
		return MethodID;
	}
	public void setMethodID(String methodID) {
		MethodID = methodID;
		try
		{
			if(this.MethodForm.getFieldItemForName(this.MethodIDField)!=null)
			{
				this.MethodForm.getFieldItemForName(this.MethodIDField).SetFieldValue(methodID);
			}
		}
		catch(Exception ex)
		{
			GWT.log("Exception caught @ pojoMetod.setMethodId" + ex.getMessage() +ex.getCause());
		}
	}
	public String getMethodName() {
		return MethodName;
	}
	public void setMethodName(String methodName) {
		MethodName = methodName;
		this.MethodForm.getFieldItemForName(this.MethodNameField).SetFieldValue(methodName);
	}
	@Override
	public void saveForm(FormBuilder input) {
		Iterator<iFormField> i = input.getFormDetails().iterator();
		
		while(i.hasNext())
		{
			iFormField tmpItem = i.next();
			if(tmpItem.hasValue())
			{
				setFieldVale(tmpItem.GetFieldName(),tmpItem.GetFieldValue());
				
			}
		}
		
	}

	@Override
	public String writeOutput(String Format) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setFieldVale(String FieldName, Object FieldValue) {
		if(!FieldValue.equals(null))
		{
			if(FieldName == MethodAuthorField)
			{
				this.MethodAuthor = FieldValue.toString();
			}
			else if(FieldName == MethodIDField)
			{
				this.MethodID = FieldValue.toString();
			}
			else if(FieldName == MethodKeywordsField)
			{
				this.KeyWords = FieldValue.toString();
			}
			else if(FieldName == MethodNameField)
			{
				this.MethodName = FieldValue.toString();
			}
			else if(FieldName == MethodDescriptionField)
			{
				this.MethodDescription = FieldValue.toString();
				this.MethodForm.getFieldItemForName(this.MethodDescriptionField).SetFieldValue(FieldValue.toString());
			}
			else if(FieldName == MethodDateCreatedField)
			{
				this.DateCreated = FieldValue.toString();
			}
		}
		
	}
	
	@Override
	public String getFieldVale(String FieldName) {
		// TODO Auto-generated method stub
		if(FieldName == MethodAuthorField)
		{
			return this.MethodAuthor;
		}
		else if(FieldName == MethodIDField)
		{
			return this.MethodID;
		}
		else if(FieldName == MethodKeywordsField)
		{
			return this.KeyWords;
		}
		else if(FieldName == MethodNameField)
		{
			return this.MethodName;
		}
		else if(FieldName == MethodDescriptionField)
		{
			return this.MethodDescription;
		}
		else if(FieldName == MethodDateCreatedField)
		{
			return this.DateCreated;
		}
		else return "";
	}

	@Override
	public void readInput(String Format, Object Input) {
		// TODO Auto-generated method stub
		
	}

}
