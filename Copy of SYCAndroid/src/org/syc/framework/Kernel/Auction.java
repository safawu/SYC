package org.syc.framework.Kernel;

import android.R.integer;


public class Auction 
{
	public TransactionItemList list;
	public TransactionItemList sellingList;
	public searchArgs args;
	
	public Auction()
	{
		list = new TransactionItemList();
		sellingList = new TransactionItemList();
		args = new searchArgs();		
	}
	
	public class searchArgs
	{
		public long musicSrcID;
		public long lowerBound;
		public long upperBound;
		public int pageNum;
		
		public searchArgs()
		{
			musicSrcID = -1L;
			lowerBound = -1L;
			upperBound = -1L;
			pageNum = -1;		
		}
		
		public void resetArgs()
		{
			musicSrcID = -1L;
			lowerBound = -1L;
			upperBound = -1L;
			pageNum = -1;	
		}
		
		public void setArgs(long msId, long lB, long uB, int pNum)
		{
			musicSrcID = msId;
			lowerBound = lB;
			upperBound = uB;
			pageNum = pNum;
		}
		public String[] toStringArray()
		{
			String[] args = {Long.toString(musicSrcID), Long.toString(lowerBound),
					 Long.toString(upperBound), Integer.toString(pageNum)};
			return args;
		}
	}

}

