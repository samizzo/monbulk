package monbulk.shared.util;

import java.util.ArrayList;
import java.util.Iterator;

public class HtmlFormatter {

	public static String GetHTMLTab()
	{
		return "&nbsp;&nbsp;&nbsp;&nbsp;";
	}
	public static String GetHTMLTabs(int numTabs)
	{
		int i=0;
		String Tabs = "";
		while(i< numTabs)
		{
			Tabs = Tabs + GetHTMLTab();
			i++;
		}
		return Tabs;
	}
	public static String GetHTMLNewline()
	{
		return "<br/>";
	}
	public static String GetHTMLMetaData(String FormatType, String MetaDataName)
	{
		
		if(FormatType=="tcl")
		{
			return ":metadata < :definition -requirement mandatory " + MetaDataName +" > \\";
		}
		return "";
	}
	public static String GetHTMLMetaData(String FormatType, String MetaDataName, Boolean isMandatory)
	{
		
		if(FormatType=="tcl")
		{
			if(isMandatory)
			{
				return ":metadata < :definition -requirement mandatory " + MetaDataName +" > \\";
			}
			else
			{
				return ":metadata < :definition " + MetaDataName +" > \\";
			}
		}
		return "";
	}
	public static String GetHTMLMetaDataList(String FormatType, ArrayList<String> MetaDataNames, int Tabs)
	{
		String Output = "";
		if(MetaDataNames!=null)
		{
			Iterator<String> i = MetaDataNames.iterator();
			
			while(i.hasNext())
			{
				String tmpStr = i.next();
				Output = Output + GetHTMLTabs(Tabs) + GetHTMLMetaData(FormatType,tmpStr) + GetHTMLNewline();
			}
		}
		return Output;
		
	}
	public static String GetHTMLUtilityScript(String FormatType)
	{
		String Output ="";
		Output = Output + GetHTMLTabs(1) + "proc getMethodId { methodName } {" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(2) + "set methodId \"\"" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(2) + "set r [om.pssd.method.find :name $methodName]" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(2) + "return [xvalue id $r]" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(1) + "}" + GetHTMLNewline() + GetHTMLNewline();
		Output = Output + GetHTMLTabs(1) + "proc setMethodUpdateArgs { id { action 0 } } {" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(2) + "set margs \"\"" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(2) + "if  { $id != \"\" } {" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(3) + "if { $action == \"0\" } {" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(4) + "return \"quit\"" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(3) + "} elseif { $action == \"2\" } {" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(4) + "set margs \"\"" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(3) + "} elseif { $action == \"1\" } {" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(4) + "set margs \":replace 1 :id ${id}\"" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(3) + "} else" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(4) + "return \"quit\"" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(3) + "}" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(2) + "return ${margs}" + GetHTMLNewline();
		Output = Output + GetHTMLTabs(1) + "}" + GetHTMLNewline() + GetHTMLNewline();
		/*
		 * proc getMethodId { methodName } {
		    set methodId ""
		    set r [om.pssd.method.find :name $methodName]
		    set n [xcount id $r]
		    # If more than one will take first
		    return [xvalue id $r] 
			}
			proc setMethodUpdateArgs { id { action 0 } } {
			    set margs ""
			    if  { $id != "" } {
				if { $action == "0" } {
				    # Do nothing to pre-existing
				    return "quit"
				} elseif { $action == "2" } {
				    # Create new
				    set margs ""
				} elseif { $action == "1" } {
				    # Replace existing
				    set margs ":replace 1 :id ${id}"
				} else {
				    # Return do nothing
				    return "quit"
				}
			    } else {
				# Create new
			    }
			    return ${margs}
		 */
		
		return Output;
	
	}
}
