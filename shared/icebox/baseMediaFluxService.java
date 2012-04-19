package daris.Monbulk.shared.icebox;

import java.util.ArrayList;
import java.util.List;

import arc.mf.client.Output;
import arc.mf.client.ServiceContext;
import arc.mf.client.ServiceRequest;
import arc.mf.client.xml.XmlElement;
import arc.mf.client.xml.XmlStringWriter;
import arc.mf.session.ServiceResponseHandler;
import arc.mf.session.Session;

import daris.Monbulk.shared.Services.baseService;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceTypes;
import daris.Monbulk.shared.Architecture.iModel;
import daris.Monbulk.shared.Form.iFormField;
import daris.Monbulk.shared.Model.IPojo;


public abstract class baseMediaFluxService extends baseService{

	private ServiceRequest _request;
	protected XmlStringWriter w; 
	private int requestStatus;
	protected ArrayList<IPojo> returnValues;
	public baseMediaFluxService(ServiceActions action) {
		super(action);
		w= new XmlStringWriter();
		
	}
	//must override
	public void SerialisePojo(IPojo tmpDObject)
	{
		//Take a Pojo and convert to XMLStringWriter
		return;
	}
	public boolean makeRequest(final ServiceActions action, final int numOutputs,final iModel modelRef )
	{
		requestStatus = 0;
		final String ServiceName = GetServiceNameForAction(action);
		final ServiceActions finalAction = action;
		_request = Session.execute(new ServiceContext(ServiceName),ServiceName, w.document(), null, numOutputs, new ServiceResponseHandler() {
			public void processResponse(XmlElement xe, List<Output> outputs) throws Throwable {
				_request = null;
				
				if ( xe == null ) 
				{
					return;
				}
				// Use the right API depending on whether we requested outputs or not.
				ArrayList<IPojo> outPut;
				if ( numOutputs == 0 ) {
					outPut = instantiate(xe,finalAction);
				} else {
					outPut = instantiate(xe,outputs,finalAction);
				}
				//modelRef.ServiceUpdate(ServiceName, outPut);
			}
		},true);
		_request.execute();
		return true;
				
	}
	//must override - takes input fields and converts to xml Request fields
	abstract protected void CreateRequestArguments(ArrayList<iFormField> Parameters,ServiceActions ActionType);
	//Must override
	abstract protected ArrayList<IPojo> instantiate(XmlElement xe,ServiceActions action) throws Throwable;
	//	Must override
	abstract protected ArrayList<IPojo> instantiate(XmlElement xe, List<Output> Outputs, ServiceActions action) throws Throwable;
	
/*
	@Override
	public boolean sendUpdateRequest(ArrayList<IPojo> newData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int sendCreateRequest(ArrayList<IPojo> newData) {
		// TODO Auto-generated method stub
		this.SerialisePojo(newData.get(0));
		this.makeRequest(getServiceName(), 1);
		return this.requestStatus;
	}

	@Override
	public boolean sendDeleteRequest(String ID) {
		// TODO Auto-generated method stub
		return false;
	}
*/
	protected abstract String GetServiceNameForAction(ServiceActions thisAction);
	
	public void sendMFServiceRequest(ArrayList<iFormField> Parameters,ServiceActions action, iModel responseModel) {
		// 
		CreateRequestArguments(Parameters,action);
		this.makeRequest(action, 1,responseModel);
		
	}
	
}
