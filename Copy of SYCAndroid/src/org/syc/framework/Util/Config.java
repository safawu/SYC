package org.syc.framework.Util;

import java.io.Serializable;


public class Config implements Serializable
{
	public boolean remPassword;
	public boolean playWhenScroll;
	public String userName;
	public String password;
	public boolean showDlgAftPurchase;
	public boolean autoLoop;
	public Config()
	{
		remPassword = false;
		playWhenScroll = true;
		userName = "";
		password = "";
		showDlgAftPurchase = false;
		autoLoop = false;
	}
	
}
