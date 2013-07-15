//////////////////////////////////////////////////////
///sever sync necessary
//////////////////////////////////////////////////////
package org.syc.framework.Item;

import java.io.Serializable;

public class Item implements Serializable
{
	private static final long serialVersionUID = 2134656560169373104L;
	protected final long id;
	protected final String name;
	protected boolean isAuction;
	
	public Item(Item i)
	{
		this.id = i.id;
		this.name = i.name;
		isAuction = i.isAuction;
	}
	public Item(long id, String name, boolean isAuction)
	{
		this.id = id;
		this.name = name;
		this.isAuction = isAuction;
	}
	public Item()
	{
		this.id = 0;
		this.name = "";
		this.isAuction = false;
	}
	public long getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isAuction()
	{
		return isAuction;
	}
	
	public void setAuction()
	{
		isAuction = true;
	}
	
	public void resetAuction()
	{
		isAuction = false;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		else if(((Item)o).id == this.id)
			return true;
		return false;
	}
}
