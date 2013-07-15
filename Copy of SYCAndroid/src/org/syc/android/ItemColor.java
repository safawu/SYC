package org.syc.android;

import java.util.ArrayList;

public class ItemColor
{
	public static ArrayList<Integer> colors = new ArrayList<Integer>();
	
	public static int getColor(long id)
	{
		return colors.get(((int)id) % 7);		
	}

}
