//////////////////////////////////////////////////////
///sever sync necessary
//////////////////////////////////////////////////////
package org.syc.framework.Item;

import java.io.Serializable;
import java.util.Vector;

import org.syc.framework.Const.*;

public class ItemList implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5097254891148911946L;
	//private static final long serialVersionUID = -5097254891148911946L;
	protected Vector<Item> itemList;
	
	public ItemList(ItemList iL)
	{
		itemList = new Vector<Item>(iL.itemList);
	}
	public ItemList()
	{
		itemList = new Vector<Item>();
	}
	
	public int size()
	{
		return itemList.size();
	}
	
	public void clear()
	{
		itemList.clear();
	}
	public Item get(int index)
	{
		return itemList.get(index);
	}
	public boolean isEmpty()
	{
		return itemList.isEmpty();
	}
	public ReturnInfo add(Item item)
	{
		if(itemList.add(item))
			return ReturnInfo.SUCCESS;
		else 			
			return ReturnInfo.UNKNOWN_EXPECTION;
	}
	public ReturnInfo delete(Item item)
	{
		if(itemList.remove(item))
			return ReturnInfo.SUCCESS;
		else
			return ReturnInfo.UNKNOWN_EXPECTION;
	}
	public Item searchId(long id)
	{
		Item i = new Item(id, null, false);
		int findIndex = itemList.indexOf(i);
		if( findIndex == -1)
			return null;
		else
			return itemList.get(findIndex);
		}
	/* need complete */
	public ReturnInfo sort(String cmd)
	{
		return ReturnInfo.SUCCESS;
	}
}
