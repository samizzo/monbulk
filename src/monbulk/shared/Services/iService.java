package monbulk.shared.Services;

import monbulk.shared.util.MonbulkEnums;

/*
 * Services provide an interface to do two things: send data and retrieve data.
 */
public interface iService
{
	// TODO: We could use some kind of rtti-like mechanism
	// where we return a type object instead of an enum.
	public MonbulkEnums.ServiceNames getServiceType();
}
