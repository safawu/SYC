package org.syc.music_trading_system.Item;

import java.io.Serializable;
import java.util.Date;


import org.syc.framework.Item.*;
import org.syc.framework.Kernel.*;

public class ExTransItem extends TransactionItem implements Serializable
{

	private static final long serialVersionUID = -6354275122703124933L;
	private long puchasePrice;
	private Date putOnTime;
	
	public ExTransItem()
	{
		super();
		puchasePrice = 0L;
		putOnTime = new Date();
	}
	
	public ExTransItem(ExTransItem eItem)
	{
		this.id = eItem.id;
		this.sellerId = eItem.sellerId;
		this.item = new Item(eItem.getItem());
		this.price = eItem.price;
		this.puchasePrice = eItem.puchasePrice;
		//this.putOnTime = eItem.putOnTime;
		this.putOnTime = new Date(eItem.getPutOnTime().getTime());
	}
	
	public ExTransItem(long id, long sellerId, Item item, long price, long pPrice, Date pTime)
	{
		super(id, sellerId, item, price);
		this.puchasePrice = pPrice;
		this.putOnTime = new Date(pTime.getTime());
	}
		
	public long getPuchasePrice()
	{
		return puchasePrice;
	}
	
	public void setPuchasePrice(long pPrice)
	{
		this.puchasePrice = pPrice;
	}
	
	public Date getPutOnTime()
	{
		return putOnTime;
	}
	
	public void setPutOnTime(Date date)
	{
		this.putOnTime = new Date(date.getTime());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		else if(((ExTransItem)o).getId() == this.getId())
			return true;
		return false;
	}

}
