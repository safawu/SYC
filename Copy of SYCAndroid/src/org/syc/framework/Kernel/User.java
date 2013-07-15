//////////////////////////////////////////////////////
///sever sync necessary
//////////////////////////////////////////////////////
package org.syc.framework.Kernel;

import java.io.Serializable;

import org.syc.framework.Item.*;


public class User implements Serializable
{
	private static final long serialVersionUID = 8534826024552740186L;
	protected long id;
	protected String name;
	protected String password;
	protected long property;
	protected long rank;
	
	public User(User u)
	{
		id = u.id;
		name = u.name;
		property = u.property;
		rank = u.rank;
	}
	public User(long id, String name, String password, long property, long rank)
	{
		this.id = id;
		this.name = name;
		this.password = password;
		this.property = property;
		this.rank = rank;
	}
	public User()
	{
		id = 0;
		name = "";
		property = 0;
		rank = 0;		
	}
	public long getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getPassword()
	{
		return password;
	}
	public long getProperty()
	{
		return property;
	}
	public long getRank()
	{
		return rank;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public void setProperty(long value)
	{
		property = value;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public void setRank(long rank)
	{
		this.rank = rank;
	}
}
