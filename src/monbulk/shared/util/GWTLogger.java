package monbulk.shared.util;

import com.google.gwt.core.client.GWT;

public class GWTLogger {

	public static Boolean isDebug;
	
	public static void Log(String Message, String ClassName, String MethodName, String LineNumber)
	{
		if(isDebug)
		{
			String logMessage = "Debug.trace on Class:" + ClassName + " @ " + MethodName + "." + LineNumber + "-" + Message;
			GWT.log(logMessage);
		}
	}
}
