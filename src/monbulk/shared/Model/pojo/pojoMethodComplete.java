package monbulk.shared.Model.pojo;

import java.util.ArrayList;
import java.util.Iterator;


import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Services.MethodService;

public class pojoMethodComplete implements IPojo{

	private pojoMethod MethodDetails;
	private pojoSubjectProperties SubjectProperties;
	private ArrayList<pojoStepDetails> allSteps;
	private String MethodID;
	
	public pojoMethodComplete()
	{
		
	}
	public pojoMethodComplete(String ID)
	{
		this.MethodID = ID;
	}
	@Override
	public String writeOutput(String Format) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readInput(String Format) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveForm(FormBuilder input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FormBuilder getFormStructure() {
		
		//Get the formStructure for each pojo but set to Static
		return null;
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
	
}
