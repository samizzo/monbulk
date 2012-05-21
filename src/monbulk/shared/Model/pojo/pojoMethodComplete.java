package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Services.MethodService;
import monbulk.shared.util.HtmlFormatter;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.impl.DOMParseException;
public class pojoMethodComplete implements IPojo{

	private pojoMethod MethodDetails;
	private pojoSubjectProperties SubjectProperties;
	private HashMap<String,pojoStepDetails> allSteps;
	
	private String MethodID;
	private String _XML; //So we would hope to be bale to add a schema and hope they match
	private int stepsAdded;
	private int stepsDeleted;
	
	public pojoMethodComplete()
	{
		
		this.MethodDetails = new pojoMethod();
		this.SubjectProperties = new pojoSubjectProperties();
		this.allSteps = new HashMap<String,pojoStepDetails>();
		stepsAdded=0;
		stepsDeleted=0;
	}
	public pojoMethodComplete(String ID)
	{
		
		this.MethodID = ID;
		this.MethodDetails = new pojoMethod();
		this.MethodDetails.setMethodID(ID);
		this.SubjectProperties = new pojoSubjectProperties();
		this.allSteps = new HashMap<String,pojoStepDetails>();
	}
	
	public void addStep(pojoStepDetails tmpStep,String StepFormName)
	{
		//String formName = pojoStepDetails.FormName+ this.getStepCount();
		this.allSteps.put(StepFormName, tmpStep); //.add(StepFormName,tmpStep);
		this.stepsAdded++;
	}
	public void addStep()
	{
		String formName = pojoStepDetails.FormName+ this.getStepCount();
		pojoStepDetails tmpStep = new pojoStepDetails(this.stepsAdded);
		this.allSteps.put(formName,tmpStep); //.add(StepFormName,tmpStep);
		this.stepsAdded++;
	}
	/**
	 * In case we need to change the ordering of steps we need a sort algorithm, a setter function and an update of all Form Indices 
	 */
	public void sortSteps()
	{
		HashMap<String,pojoStepDetails> sortedMap = new HashMap<String,pojoStepDetails>();
		Iterator<java.util.Map.Entry<String,pojoStepDetails>> i = allSteps.entrySet().iterator();
		int index=0;
		//find a sorting algorithm - selsort?
	}
	public void removeStep(String StepFormName)
	{
		this.allSteps.remove(StepFormName);  //.remove(StepIndex);
		this.stepsDeleted++;
	}
	@Override
	public String writeOutput(String Format) {
		
		StringBuilder Output = new StringBuilder();
		if(Format=="TCL")
		{
			Output.append(this.MethodDetails.writeTCL());
			Output.append(this.SubjectProperties.writeOutput("TCL"));
			return this.MethodDetails.writeTCL();
		}
		
		return Output.toString();
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
			Iterator<Entry<String,pojoStepDetails>> i = this.allSteps.entrySet().iterator();
			while(i.hasNext())
			{
				Entry<String, pojoStepDetails> in = i.next();
				
				pojoStepDetails step = in.getValue();
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
	public HashMap<String,pojoStepDetails> getSteps()
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
			name.pehl.totoe.xml.client.XmlParser tmpParser = new name.pehl.totoe.xml.client.XmlParser(); 
			name.pehl.totoe.xml.client.Document document = new name.pehl.totoe.xml.client.XmlParser().parse(Input.toString());

			
			if(tmpXML!=null)
			{
				NodeList tmpList1 = tmpXML.getElementsByTagName("method");
				
				NodeList tmpList = tmpList1.item(0).getChildNodes();
				int i=0;
				int j=0;
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
					if(tmpNode.getNodeName().contains("subject"))
					{
						this.SubjectProperties.readInput("XML", Input);						
						//Window.alert(document.selectNodes("/response/method/subject/project").toString());
					}
					if(tmpNode.getNodeName().contains("step"))
					{
						pojoStepDetails tmpStep = new pojoStepDetails(allSteps.size());
						tmpStep.readInput("XML", tmpNode.toString());
						if(this.allSteps!=null)
						{
							String tmpFormName = pojoStepDetails.FormName + j;
							this.allSteps.put(tmpFormName,tmpStep);
							j++;
							
						}
						
					}
					List = List + " Node: " + tmpNode.getNodeName();
					
					i++;
				}
		
				//Window.alert(List);
			}
			
		}
		catch(Exception ex)
		{
			GWT.log("DOMParse Error @ pojoMethodComplete.readInput" + ex.getMessage());
		}
		
	}
	
}
