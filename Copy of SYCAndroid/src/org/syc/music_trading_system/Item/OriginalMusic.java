package org.syc.music_trading_system.Item;

import org.syc.framework.Item.*;

public class OriginalMusic extends Item
{
	public OriginalMusic() 
	{
		super();
	}
	public OriginalMusic(long id, String name, boolean isAuction)
	{
		super(id, name, false);
	}
	public OriginalMusic(OriginalMusic oM)
	{
		super(oM);
	}
}
