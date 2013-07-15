package org.syc.framework.Util;

import java.util.Calendar;
import java.util.Date;


public class DateManager 
{
	public static String getDeltaTimeTillNow(Date date)
	{
		Calendar now = Calendar.getInstance();
		Calendar yet = Calendar.getInstance();
		yet.setTime(date);
		long delta = now.getTime().getTime()-yet.getTime().getTime();
		long days = (delta)/(1000 * 3600 * 24);
		long hours =(delta - days*1000*3600*24)/(1000 * 3600);
		//hours =  (float)(Math.round(hours*10))/10;
		String rtnStr = "";
		if (days > 0) 
		{
			rtnStr = rtnStr + days + "d" + hours + "h";
		}
		else 
		{
			rtnStr = rtnStr + hours + "h";
		}
		return rtnStr;
	}
}
