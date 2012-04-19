package monbulk.shared.icebox;

import monbulk.MediaFlux.Services.MediaFluxMetadataService;
import monbulk.shared.Services.iService;
import monbulk.shared.Services.iServiceFascade;
import monbulk.shared.util.MonbulkEnums.ServiceActions;
import monbulk.shared.util.MonbulkEnums.ServiceNames;


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
