package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Services.MethodService;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.impl.DOMParseException;
public class pojoMethodComplete implements IPojo{

	private pojoMethod MethodDetails;
	private pojoSubjectProperties SubjectProperties;
	private ArrayList<pojoStepDetails> allSteps;
	private String MethodID;
	private String _XML; //So we would hope to be bale to add a schema and hope they match
	
	
	public pojoMethodComplete()
	{
		
		this.MethodDetails = new pojoMethod();
		this.SubjectProperties = new pojoSubjectProperties();
		this.allSteps = new ArrayList<pojoStepDetails>();
		
	}
	public pojoMethodComplete(String ID)
	{
		
		this.MethodID = ID;
		this.MethodDetails = new pojoMethod();
		this.MethodDetails.setMethodID(ID);
		this.SubjectProperties = new pojoSubjectProperties();
		this.allSteps = new ArrayList<pojoStepDetails>();
	}
	
	@Override
	public String writeOutput(String Format) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveForm(FormBuilder input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FormBuilder getFormStructure() {
		
		FormBuilder returnForm = new FormBuilder();
		returnForm.SetFormName("Method");
		returnForm.MergeForm(this.MethodDetails.getFormStructure());
		returnForm.MergeForm(this.SubjectProperties.getFormStructure());
		if(this.allSteps.size() > 0)
		{
			Iterator<pojoStepDetails> i = this.allSteps.iterator();
			while(i.hasNext())
			{
				pojoStepDetails step = i.next();
				returnForm.MergeForm(step.getFormStructure());				
			}
		}

		
		return returnForm;
	}

	@Override
	public void deserialise() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deserialiseFromList(String XML) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFieldVale(String FieldName, Object FieldValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFieldVale(String FieldName) {
		// TODO Auto-generated method stub
		return null;
	}
	public pojoMethod getMethodDetails()
	{
		return this.MethodDetails;
	}
	public pojoSubjectProperties getSubjectProperties()
	{
		return this.SubjectProperties;
	}
	public ArrayList<pojoStepDetails> getSteps()
	{
		return this.allSteps;
	}
	public int getStepCount()
	{
		return allSteps.size();
	}
	public String getMethodID()
	{
		return this.MethodID;
	}
	
	@Override
	public void readInput(String Format, Object Input) {
		
		try
		{
			Document tmpXML = XMLParser.parse(Input.toString()); 
			if(tmpXML!=null)
			{
				NodeList tmpList1 = tmpXML.getElementsByTagName("method");
				
				NodeList tmpList = tmpList1.item(0).getChildNodes();
				int i=0;
				String List ="";
				while(i<tmpList.getLength())
				{
					Node tmpNode = tmpList.item(i);
					//Window.alert("Node is added" + tmpNode.getNodeName());
					//Have to get attributes
					//Window.alert("Node Value" + tmpNode.getChildNodes());
					if(tmpNode.getNodeName().contains("description"))
					{
						//Window.alert("Node is added" + tmpNode.getNodeName());
						this.MethodDetails.setFieldVale(pojoMethod.MethodDescriptionField, tmpNode.getChildNodes().toString());
						//Window.alert("Exception?");
					}
					if(tmpNode.getNodeName().contains("name"))
					{
						
						this.MethodDetails.setMethodName(tmpNode.getChildNodes().toString());
					}
					List = List + " Node: " + tmpNode.getNodeName();
					
					i++;
				}
		
				//Window.alert(List);
			}
			
		}
		catch(DOMParseException ex)
		{
			GWT.log("DOMParse Error @ pojoMethodComplete.readInput" + ex.getMessage());
		}
		
	}
	
}
