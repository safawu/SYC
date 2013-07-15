package org.syc.music_trading_system.Item;

import java.util.Vector;

import org.syc.framework.Item.Item;

public class OriginalMusicList extends Item 
{
	protected Vector<OriginalMusic> itemList;
	
	public OriginalMusicList()
	{
		itemList = new Vector<OriginalMusic>();
	}
	
	public OriginalMusicList(OriginalMusicList oML)
	{
		itemList = new Vector<OriginalMusic>(oML.itemList);
	}
}
