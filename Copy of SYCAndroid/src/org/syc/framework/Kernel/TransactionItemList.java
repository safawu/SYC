//////////////////////////////////////////////////////
///sever sync necessary
//////////////////////////////////////////////////////
package org.syc.framework.Kernel;

import java.io.Serializable;
import java.util.Vector;

import org.syc.framework.Const.*;


/**ATTENTION: this class is a ReadOnly class**/
public class TransactionItemList implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2581597185630169481L;
	protected Vector<TransactionItem> transactionList;
	
	public TransactionItemList()
	{
		transactionList = new Vector<TransactionItem>();
	}
	public TransactionItemList(TransactionItemList tL) 
	{
		transactionList = new Vector<TransactionItem>(tL.transactionList);
	}	
	public int size()
	{
		return transactionList.size();
	}
	public ReturnInfo add(TransactionItem tItem)
	{
		if(transactionList.add(tItem))
			return ReturnInfo.SUCCESS;
		else 			
			return ReturnInfo.UNKNOWN_EXPECTION;
	}
	public TransactionItem get(int index)
	{
		return transactionList.get(index);
	}
	public boolean isEmpty()
	{
		return transactionList.isEmpty();
	}
}
