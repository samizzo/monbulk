package monbulk.MediaFlux.Services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import arc.mf.client.Output;
import arc.mf.client.ServiceContext;
import arc.mf.client.ServiceRequest;
import arc.mf.client.xml.XmlElement;
import arc.mf.session.ServiceResponseHandler;
import arc.mf.session.Session;


import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.iModel;
import monbulk.shared.Form.iFormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.shared.Services.MethodService;


import monbulk.shared.util.MonbulkEnums.ServiceActions;

public class mfMethodService extends MethodService {

	private class MethodResponseHandler implements ServiceResponseHandler
	{
		private MethodServiceHandler m_handler = null;
		private MethodRequest m_request;

		public MethodResponseHandler(MethodServiceHandler handler, MethodRequest request)
		{
			m_handler = handler;
			m_request = request;
		}

		public void processResponse(XmlElement xe, List<Output> outputs) throws Throwable
		{
			ArrayList<pojoMethod> methods = new ArrayList<pojoMethod>();

			switch (m_request)
			{
				case List:
				{
					/*
					 * <response><method name="Animal-MRI-Simple" description="Animal MRI acquisition with simple method." version="1.1">727.1.1</method>
					 * <method name="Animal-MRI-Simple-pvt" description="Animal MRI acquisition with simple method amd some meta in private fields" version="1.1">727.1.2</method>
					 * <method name="Human-MRI-Simple-noRS" description="Human MRI acquisition with simple method and no R-Subject" version="1.1">727.1.3</method>
					 * </response>
					 */
					//Window.alert(xe.elements().);
					List<XmlElement> elemMethods =  xe.elements("method");
					Iterator<XmlElement> i = elemMethods.iterator();
					while(i.hasNext())
					{
						XmlElement tmpItem = i.next();
						String Name = tmpItem.value("@name");
						String ID = tmpItem.value();
						String Description = tmpItem.value("@description");
						
						pojoMethod tmpMethod = new pojoMethod();
						tmpMethod.setMethodID(ID);
						tmpMethod.setFieldVale(pojoMethod.MethodDescriptionField, Description);
						tmpMethod.setFieldVale(pojoMethod.MethodNameField, Name);
						methods.add(tmpMethod);
					}
					//Window.alert(xe.values("method").toString());
					m_handler.onReadMethodList(methods);
					break;
				}
				case Describe:
					break;
				case Create:
					break;
				default:
					break;
			}
		}
	}
	@Override
	public void sendServiceRequest(ArrayList<iFormField> Parameters,
			ServiceActions action, iModel responseModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendServiceActionRequest(ArrayList<iFormField> Parameters,
			ServiceActions action, IPresenter responseModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPojoRequest(IPojo Parameters, ServiceActions action,
			IPresenter responseModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMethodList(MethodServiceHandler handler) {
		Session.execute(new ServiceContext("mfMethodService.getMethodList"), "om.pssd.method.list", "", null, 0, new MethodResponseHandler(handler, MethodRequest.List), true);
		
	}

	@Override
	public void getMethod(String ID, MethodServiceHandler handler) {
		// TODO Auto-generated method stub
		
	}

}
