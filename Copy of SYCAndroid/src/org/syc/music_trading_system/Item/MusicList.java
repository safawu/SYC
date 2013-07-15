//////////////////////////////////////////////////////
///sever sync necessary
//////////////////////////////////////////////////////
package org.syc.music_trading_system.Item;

import java.io.Serializable;
import java.util.Vector;

import org.syc.framework.Item.Item;

public class MusicList extends Item implements Serializable 
{
	private static final long serialVersionUID = 1063745245827961741L;
	private Vector<Music> musicList;
	public MusicList(MusicList mL) 
	{
		musicList = new Vector<Music>(mL.musicList);
	}
}
