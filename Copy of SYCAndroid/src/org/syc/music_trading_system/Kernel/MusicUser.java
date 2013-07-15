//////////////////////////////////////////////////////
///sever sync necessary
//////////////////////////////////////////////////////
package MusicTradingSystem.Kernel;

import Framework.Kernel.User;
import MusicTradingSystem.Item.MusicList;


public class MusicUser extends User 
{
	private MusicList itemList;
	private UserInfo userInfo;
	
	public MusicUser(MusicUser u)
	{
		super(u);
		itemList = new MusicList(u.itemList);
		userInfo = new UserInfo(u.userInfo);
	}
}


class UserInfo
{
	protected static int gender;
	
	public UserInfo(UserInfo uInf)
	{
		
	}
}