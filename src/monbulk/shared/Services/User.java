package monbulk.shared.Services;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a user of the MediaFlux system.
 */
public class User
{
	private String m_name;
	private String m_domain;
	private ArrayList<String> m_roles = new ArrayList<String>();

	public User(String name, String domain)
	{
		m_name = name;
		m_domain = domain;
	}
	
	/**
	 * Returns the name of this user.
	 * @return
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * Returns the domain of this user.
	 * @return
	 */
	public String getDomain()
	{
		return m_domain;
	}
	
	/**
	 * Adds a role to this user.
	 * @param role
	 */
	public void addRoles(List<String> roles)
	{
		if (roles != null)
		{
			m_roles.addAll(roles);
		}
	}
	
	/**
	 * Returns true if the user has the specified role.
	 * Note: Roles are case-sensitive.
	 * @param role
	 * @return
	 */
	public boolean hasRole(String role)
	{
		return m_roles.contains(role);
	}
}
