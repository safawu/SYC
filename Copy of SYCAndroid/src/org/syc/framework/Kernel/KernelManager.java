package org.syc.framework.Kernel;

import java.util.ArrayList;

import network.NWSystemMessage;

import org.syc.framework.Const.*;
import org.syc.framework.Item.*;
import org.syc.framework.Util.*;
import org.syc.music_trading_system.Item.*;
public class KernelManager
{
	public static User user = new User();
	public static Auction auction = new Auction();
	public static ItemList itemList = new ItemList();
	public static ItemList originalMusicList = new ItemList();
	public static ExTransItemList exList = new ExTransItemList();
	
	
	///交易相关
	public static ReturnInfo getTransItemList(int cmd)
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(cmd, auction.args.toStringArray(), user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		auction.list = (TransactionItemList)(inObj.object);
		
		return rtnInfo;
	}

	/*fin*/
	/**ATTENTION: UI must refresh the List & auction UI after this func**/
	public static ReturnInfo makeTransaction(TransactionItem tItem)
	{
		ReturnInfo returnInfo = NetTool.send(Command.BUY, tItem, user.getId());
		//do Local
		/*if(returnInfo == ReturnInfo.SUCCESS)
			itemList.add(tItem.getItem());*/
		return returnInfo;
	}
	
	public static ReturnInfo buyFromServer(TransactionItem tItem)
	{
		MyObject inObj = new MyObject();
		auction.args.musicSrcID = tItem.getItem().getId();
		ReturnInfo rtnInfo = NetTool.receive(
				Command.BUY_FROM_SERVER, auction.args.toStringArray(), user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		tItem.setId((Long)(inObj.object));
		return rtnInfo;
	}
	/*fin*/
	/**ATTENTION: UI must make sure item.isAuction is false**/
	public static ReturnInfo SellItem(TransactionItem tItem)
	{
		ReturnInfo returnInfo = NetTool.send(Command.SELL, tItem, user.id);
		//do Local
		//if(returnInfo == ReturnInfo.SUCCESS)
			//itemList.searchId(tItem.getItem().getId()).setAuction();
		return returnInfo;
	}
	/*fin*/
	/**ATTENTION: UI must refresh the List & auction UI after this func**/
	public static ReturnInfo cancelSellItem(TransactionItem tItem)
	{
		ReturnInfo returnInfo = NetTool.send(Command.HOLDON, tItem, user.getId());
		if(returnInfo == ReturnInfo.SUCCESS)
			itemList.searchId(tItem.getItem().getId()).resetAuction();
		return returnInfo;
	}
	
	public static ReturnInfo refreshSellingList()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.FETCH_SELLING_LIST, user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		auction.sellingList = (TransactionItemList)(inObj.object);
		return rtnInfo;
	}
	
	public static ReturnInfo refreshExList()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.FETCH_SELLING_LIST, user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		exList = (ExTransItemList)(inObj.object);
		return rtnInfo;
	}
	
	public static ArrayList<NWSystemMessage> refreshSoldoutInfo()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.FETCH_SOLDOUT_LIST, user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return new ArrayList<NWSystemMessage>();
		ArrayList<NWSystemMessage> rtnMsgs =(ArrayList<NWSystemMessage>)(inObj.object);		
		return rtnMsgs;
	}
	
	public static ReturnInfo soldoutInfoRead(ArrayList<NWSystemMessage> msgs)
	{
		long[] msgIds = new long[msgs.size()];
		for(int i=0; i<msgs.size(); i++)
			msgIds[i] = msgs.get(i).MessageID;
		ReturnInfo rtnInfo = NetTool.send(Command.MARK_READ, msgIds, user.getId());
		return rtnInfo;
	}
	///奖励
	public static ReturnInfo getReward()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.GET_REWARD, user.id, inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		user.setProperty((Long)(inObj.object));

		return rtnInfo;
	}
	///=========================================================================
	///User相关
	/*fin*/
	public static ReturnInfo login()
	{
		MyObject inObj = new MyObject();
		inObj.object = 0L;
		ReturnInfo rtnInfo = NetTool.receiveByName(Command.FETCH_ID, user.getName(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		
		if((Long)(inObj.object) == 0L)
			return ReturnInfo.WRONG_PASSWORD;
		
		user.setId((Long)(inObj.object));
		
		rtnInfo = NetTool.send(Command.LOGIN, user.getPassword(), user.getId());
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		
		rtnInfo = refreshProperty();
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		
		rtnInfo = refreshRank();
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;	
		
		rtnInfo = refreshOriginalMusicList();
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;	
		
		rtnInfo = refreshExList();
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		
		return rtnInfo;
	}
	/*fin*/
	public static ReturnInfo logout()
	{
		ReturnInfo returnInfo = NetTool.send(Command.LOGOUT, null, user.getId());
		if(returnInfo == ReturnInfo.SUCCESS)
			user = new User();
			auction = new Auction();
		return returnInfo;
	}
	/*fin*/
	public static ReturnInfo regist()
	{
		ReturnInfo returnInfo = NetTool.send(Command.NEW_ACCOUNT, user, user.getId());		
		return returnInfo;
	}

	/*fin*/
	public static ReturnInfo refreshProperty()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.SHOW_MONEY, user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		user.setProperty((Long)(inObj.object));
		return rtnInfo;
	}
	/*fin*/
	public static ReturnInfo refreshRank()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.SHOW_RANK, user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		user.setRank((Long)(inObj.object));
		return rtnInfo;
	}
	
	public static ReturnInfo refreshItemList()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.FETCH_PROPERTY_LIST, user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		itemList = new ItemList((ItemList)(inObj.object));
		return rtnInfo;
	}
	
	public static ReturnInfo refreshOriginalMusicList()
	{
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receive(Command.FETCH_ORINGIN_LIST, user.getId(), inObj);
		if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		originalMusicList = new ItemList((ItemList)(inObj.object));
		/*if(rtnInfo != ReturnInfo.SUCCESS)
			return rtnInfo;
		long[] list;
		list = (long[])(inObj.object);
		for (int i = 0; i < list.length; i++) 
		{
			originalMusicList.add(new OriginalMusic(list[i], "test", false));
		}*/
		return rtnInfo;
	}
	/*fin*/
	/**ATTENTION: When the server error occurs, the func also return false**/
	public static boolean nameConfliction()
	{
		
		MyObject inObj = new MyObject();
		ReturnInfo rtnInfo = NetTool.receiveByName(Command.NAME_CONFLICT, user.getName(), inObj);
		if (rtnInfo != ReturnInfo.SUCCESS)
			return false;
		return (Boolean)(inObj.object);
	}	
	
	//public static downLoad
	
}
