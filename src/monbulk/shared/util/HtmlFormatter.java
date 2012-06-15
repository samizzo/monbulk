package monbulk.shared.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import monbulk.shared.Model.pojo.pojoMetaData;

public class HtmlFormatter {

	public static String GetHTMLTab()
	{
		//return "&nbsp;&nbsp;&nbsp;&nbsp;";
		return "\t";
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
		//return "<br/>";
		return "\n";
	}
	public static String GetHTMLMetaData(String FormatType, String MetaDataName)
	{
		
		if(FormatType=="tcl")
		{
			return ":metadata < :definition -requirement mandatory " + MetaDataName +" > \\";
		}
		return "";
	}
	public static String GetHTMLMetaData(String FormatType, String MetaDataName, String isMandatory)
	{
		
		if(FormatType=="tcl")
		{
			if(isMandatory=="true")
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
	public static String GetHTMLMetaDataList(String FormatType, HashMap<String,pojoMetaData> MetaDataNames, int Tabs)
	{
		String Output = "";
		if(MetaDataNames!=null)
		{
			Iterator<Entry<String, pojoMetaData>> i = MetaDataNames.entrySet().iterator();
			
			while(i.hasNext())
			{
				Entry<String,pojoMetaData> tmpStr = i.next();
				pojoMetaData tmpItem = tmpStr.getValue();
				Output = Output + GetHTMLTabs(Tabs) + GetHTMLMetaData(FormatType,tmpItem.getFieldVale(pojoMetaData.MetaDataNameField),tmpItem.getFieldVale(pojoMetaData.IsMandatoryField)) + GetHTMLNewline();
			}
		}
		return Output;
		
	}
	public static String GetHTMLMetaDataList(String FormatType, HashMap<String,pojoMetaData> MetaDataNames, int Tabs,Boolean splitPublic)
	{
		StringBuilder publicOutput = new StringBuilder();
		publicOutput.append(HtmlFormatter.GetHTMLTabs(Tabs)+ ":public < \\" + HtmlFormatter.GetHTMLNewline());
		StringBuilder privateOutput = new StringBuilder();
		privateOutput.append(HtmlFormatter.GetHTMLTabs(Tabs)+ ":private < \\" + HtmlFormatter.GetHTMLNewline());
		String output="";
		if(splitPublic)
		{
			if(MetaDataNames!=null)
			{
				Iterator<Entry<String, pojoMetaData>> i = MetaDataNames.entrySet().iterator();
				int countPublic=0;
				int countPrivate=0;
				while(i.hasNext())
				{
					Entry<String,pojoMetaData> tmpStr = i.next();
					pojoMetaData tmpItem = tmpStr.getValue();
					if(tmpItem.getFieldVale(pojoMetaData.IsPublicField)=="true")
					{
						publicOutput.append(GetHTMLTabs(Tabs+1) + GetHTMLMetaData(FormatType,tmpItem.getFieldVale(pojoMetaData.MetaDataNameField),tmpItem.getFieldVale(pojoMetaData.IsMandatoryField)) + GetHTMLNewline());
						countPublic++;
					}
					else
					{
						privateOutput.append(GetHTMLTabs(Tabs+1) + GetHTMLMetaData(FormatType,tmpItem.getFieldVale(pojoMetaData.MetaDataNameField),tmpItem.getFieldVale(pojoMetaData.IsMandatoryField)) + GetHTMLNewline());
						countPrivate++;
					}
				}
				if(countPublic>0)
				{
					
					//publicOutput.append(HtmlFormatter.GetHTMLTabs(Tabs+1)+ ">\\" + HtmlFormatter.GetHTMLNewline());
					publicOutput.append(HtmlFormatter.GetHTMLTabs(Tabs)+ ">\\" + HtmlFormatter.GetHTMLNewline());
					output = publicOutput.toString();
				}
				if(countPrivate>0)
				{
					
					//privateOutput.append(HtmlFormatter.GetHTMLTabs(Tabs+1)+ ">\\" + HtmlFormatter.GetHTMLNewline());
					privateOutput.append(HtmlFormatter.GetHTMLTabs(Tabs)+ ">\\" + HtmlFormatter.GetHTMLNewline());
					output = output + privateOutput.toString();
				}
				
			}
			return output;
		}
		else
		{
			return GetHTMLMetaDataList(FormatType, MetaDataNames, Tabs);
		}
		
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
	public static String GetUtilityWriteScript()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(GetHTMLTabs(2) + "# Create/update the Method"+ GetHTMLNewline());
		sb.append(GetHTMLTabs(2) + "set id2 [xvalue id [om.pssd.method.for.subject.update $args]]"+ GetHTMLNewline());
		sb.append(GetHTMLTabs(2) + "if { $id2 == \"\" } {" + GetHTMLNewline());
		sb.append(GetHTMLTabs(3) +"return $id" + GetHTMLNewline());
		sb.append(GetHTMLTabs(2) +"} else {" + GetHTMLNewline());
		sb.append(GetHTMLTabs(3) +"return $id2" + GetHTMLNewline());
		sb.append(GetHTMLTabs(2) +"}" + GetHTMLNewline());
		sb.append(GetHTMLTabs(1) +"}" + GetHTMLNewline());
		
		return sb.toString();
		
	}
	public static String ConvertTCLtoXML(StringBuilder strTCL)
	{
		String XML = "";
		
		String TCL = strTCL.toString();
		//Replace XML Element Tags
		XML = TCL.replace(":", "<");
		XML = XML.replace("< \\", "\\>");
		XML = XML.replace("\\ \n", "\n");
		XML = XML.replace("\\\n", "\n");
		XML = XML.replace("-requirement mandatory", "requirement=\"manadatory\"");
		XML = XML.replace("-requirement optional", "requirement=\"optional\"");
		XML = XML.replace("-part p", "part=\"p\"");
		//XML = XML.replaceAll("-.+ .+", ".+");
		//Search Regex is -[a-z]* [a-z]* [a-z,.]*
		
		return XML;
	}
	public static Boolean compareStrings(String Str1, String str2)
	{
		if(Str1.contains(str2) && Str1.length() == str2.length())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static String stripFormFieldName(String FormName,String FieldToChange)
	{
		
		if(FieldToChange.contains(FormName))
		{
			FieldToChange= FieldToChange.replace(FormName, "");
			FieldToChange= FieldToChange.replace(".", "");
		}
		return FieldToChange;
	}
}
