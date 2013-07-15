package org.syc.music_trading_system.Item;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import org.syc.framework.Item.*;
import org.syc.framework.Kernel.TransactionItemList;
import org.syc.framework.Const.*;

public class ExTransItemList  extends TransactionItemList implements Serializable
{
	private static final long serialVersionUID = 4267965502147689331L;
	private Vector<ExTransItem> list;

	/*public ExTransItemList(ExTransItemList eL) 
	{
		list = new Vector<ExTransItem>(eL.list);
	}*/
	public ExTransItemList(ExTransItemList eL)
	{
		list = new Vector<ExTransItem>();
		ExTransItem exItem;
		for(int i=0; i<eL.size(); i++)
		{
			exItem = new ExTransItem(eL.get(i));
			list.add(exItem);
		}
	}
	
	public ExTransItemList() 
	{
		list = new Vector<ExTransItem>();
	}

	public int size() 
	{
		return list.size();
	}

	public ExTransItem get(int index) 
	{
		return list.get(index);
	}

	public boolean isEmpty() 
	{
		return list.isEmpty();
	}

	public ReturnInfo add(ExTransItem eItem) 
	{
		if (list.add(eItem))
			return ReturnInfo.SUCCESS;
		else
			return ReturnInfo.UNKNOWN_EXPECTION;
	}

	public ReturnInfo delete(ExTransItem eItem) 
	{
		if (list.remove(eItem))
			return ReturnInfo.SUCCESS;
		else
			return ReturnInfo.UNKNOWN_EXPECTION;
	}
	
	public void clear()
	{
		list.clear();
	}
	
	public ExTransItem searchId(long id) 
	{
		//Music mtmp = new Music(id, "", false, 0L);
		ExTransItem i = new ExTransItem(id, 0L, new Item(), 0L, 0L, new Date());
		int findIndex = list.indexOf(i);
		if (findIndex == -1)
			return null;
		else
			return list.get(findIndex);
	}
	
	public static ExTransItemList serchItemsHaveOriId(long id, ExTransItemList list)
	{
		ExTransItemList exl = new ExTransItemList();
		if(list.size() == 0)
			return exl;
		for(int i=0; i<list.size(); i++)
		{
			//if(((Music)(list.get(i).getItem())).getId() == id)
			if(((Music)(list.get(i).getItem())).getOriginMusicId() == id)
			{
				exl.add(list.get(i));
			}
		}
		return exl;
	}
	
	/* need complete */
	public ReturnInfo sort(String cmd) 
	{
		return ReturnInfo.SUCCESS;
	}


}
