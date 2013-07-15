package network;

import java.io.Serializable;
import java.util.Date;

public class NWSystemMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8412444960638454674L;
	public long MessageID;
	public Date Timestamp;
	public String Title;
	public String Context;
}
