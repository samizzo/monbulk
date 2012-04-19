package daris.Monbulk.shared.Services;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceNames;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceTypes;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Architecture.iModel;
import daris.Monbulk.shared.Form.iFormField;

/*
 * ServiceFactory
 * NB: We should be able to pass either formfields or POJOs to service Layer TODO 
 * 
 * 
 */
public final class ServiceFactory {

	private static iServiceFascade referencedServiceFascade;
	public ServiceFactory(iServiceFascade impFascade)
	{
		ServiceFactory.referencedServiceFascade = impFascade;
	}
	public static void executeReader(ServiceNames serviceName, ArrayList<iFormField> Parameters, iModel responseModel)
	{
		try
		{
			iService tmpService = referencedServiceFascade.getServiceForNameandAction(serviceName, ServiceActions.GET_UNIQUE);
			tmpService.sendServiceRequest(Parameters, ServiceActions.GET_UNIQUE, responseModel);
		
		}
		catch(Exception ex)
		{
			GWT.log(ex.getMessage(),ex);
		
		}
		
	}
	public static void executeInsert(ServiceNames serviceName, ArrayList<iFormField> Parameters,iModel responseModel)
	{
		try
		{
			iService tmpService = referencedServiceFascade.getServiceForNameandAction(serviceName, ServiceActions.CREATE);
			tmpService.sendServiceRequest(Parameters, ServiceActions.CREATE, responseModel);
		
		}
		catch(Exception ex)
		{
			GWT.log(ex.getMessage(),ex);

		}
		
	}
	public static void executeDelete(ServiceNames serviceName, ArrayList<iFormField> Parameters,iModel responseModel)
	{
		try
		{
			iService tmpService = referencedServiceFascade.getServiceForNameandAction(serviceName, ServiceActions.DELETE);
			tmpService.sendServiceRequest(Parameters, ServiceActions.DELETE, responseModel);
		}
		catch(Exception ex)
		{
			GWT.log(ex.getMessage(),ex);
			
		}
		
	}
	public static void executeFileSave(ServiceNames serviceName, ArrayList<iFormField> newData, iModel responseModel)
	{
		try
		{
			iService tmpService = referencedServiceFascade.getServiceForNameandAction(serviceName, ServiceActions.SAVE_AS_FILE);
			tmpService.sendServiceRequest(newData, ServiceActions.SAVE_AS_FILE, responseModel);
		
		}
		catch(Exception ex)
		{
			GWT.log(ex.getMessage(),ex);
			
		}
		
	}
	
	public static void executeUpdate(ServiceNames serviceName, ArrayList<iFormField> Parameters, iModel responseModel)
	{
		try
		{
			iService tmpService = referencedServiceFascade.getServiceForNameandAction(serviceName, ServiceActions.UPDATE);
			tmpService.sendServiceRequest(Parameters, ServiceActions.CREATE, responseModel);
			
		}
		catch(Exception ex)
		{
			GWT.log(ex.getMessage(),ex);
			
		}
		
	}
}
