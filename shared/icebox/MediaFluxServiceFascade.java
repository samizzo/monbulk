package daris.Monbulk.shared.icebox;

import daris.Monbulk.MediaFlux.Services.MediaFluxMetadataService;
import daris.Monbulk.shared.Services.iService;
import daris.Monbulk.shared.Services.iServiceFascade;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceActions;
import daris.Monbulk.shared.util.MonbulkEnums.ServiceNames;


public final class MediaFluxServiceFascade implements iServiceFascade {

	//Not sure what should be in constructor yet - probably server details or Session details
	public MediaFluxServiceFascade()
	{
		
	}
	@Override
	public iService getServiceForNameandAction(ServiceNames serviceName,ServiceActions ServiceAction) {
		// TODO Auto-generated method stub
		switch(serviceName)
		{
			case Dictionary_Modality:
				break;
			case Dictionary_StepTypes:
				break;
			case MetaData:
				return new MediaFluxMetadataService();
		}
		
		return null;
	}

	
}
