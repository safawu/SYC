//////////////////////////////////////////////////////
///sever sync necessary
//////////////////////////////////////////////////////
package org.syc.framework.Kernel;

import java.io.Serializable;
import org.syc.framework.Const.*;
import org.syc.framework.Item.*;
import org.syc.framework.Util.*;

public class TransactionItem implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5010867650931044815L;
	protected long id;
	protected long sellerId;
	protected Item item;
	protected long price;
	
	public TransactionItem(long id, long sellerId, Item item, long price)
	{
		this.id = id;
		this.sellerId = sellerId;
		this.item = item;
		this.price = price;
	}
    public TransactionItem(TransactionItem trans)
    {
    	this.id = trans.id;
    	this.sellerId = trans.sellerId;
    	this.item = new Item(trans.item);
    	this.price = trans.price;
    }
    public TransactionItem()
    {
    	this.id = 0;
    	this.sellerId = 0L;
    	this.item = new Item();
    	this.price = 0;
    }
    public long getId()
    {
    	return id;
    }
    public long getSellerId()
    {
    	return sellerId;
    }
    public void setSellerId(long id)
    {
    	this.sellerId = id;
    }
    public Item getItem()
    {
    	return item;
    }
    public long getPrice()
    {
    	return price;
    }
    public void setPrice(long price)
    {
    	this.price = price;
    }
    public void setId(long id)
    {
    	this.id = id;
    }
}
