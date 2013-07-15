package org.syc.framework.Util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.syc.framework.Const.*;



public class NetTool 
{
	public static String serverIpAddr;
	public static int serverPort = 5678;
	public static int timeout = 5000;
	
	public static ReturnInfo receiveByName(int cmd, String userName, MyObject inObj)
	{
		Socket socket=new Socket();
		try
		{	
			socket.connect(new InetSocketAddress(InetAddress.getByName(serverIpAddr), serverPort),timeout);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			//send cmd
			oos.writeObject(cmd);
			oos.flush();
			//send void userId, just for format
			oos.writeObject(0L);
			oos.flush();
			//send userName
			oos.writeObject(userName);
			oos.flush();
			//receive inObj
			inObj.object = ois.readObject();
			/*//send confirmation
			oos.writeObject(ReturnInfo.SUCCESS);
			oos.flush();*/
			return ReturnInfo.SUCCESS;
		} 
		catch (SocketTimeoutException e)
		{
			return ReturnInfo.REQUEST_TIMEOUT;
		}
		catch(IOException e)
		{
			return ReturnInfo.IO_EXCEPTION;
		} 
		catch (Exception e) 
		{
			return ReturnInfo.UNKNOWN_EXPECTION;
		}
		finally
		{
			try
			{
				if(socket != null)
					socket.close();				
			}
			catch(IOException e)
			{
				return ReturnInfo.UNKNOWN_EXPECTION;
			}
		}
	}
	
	public static ReturnInfo receive(int cmd, long userId, MyObject inObj)
	{
		Socket socket=new Socket();
		try
		{	
			socket.connect(new InetSocketAddress(InetAddress.getByName(serverIpAddr), serverPort),timeout);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			//send cmd
			oos.writeObject(cmd);
			oos.flush();
			//send userId
			oos.writeObject(userId);
			oos.flush();
			//receive inObj
			inObj.object = ois.readObject();
			//send confirmation
			/*oos.writeObject(ReturnInfo.SUCCESS);
			oos.flush();*/
			return ReturnInfo.SUCCESS;
		} 
		catch (SocketTimeoutException e)
		{
			return ReturnInfo.REQUEST_TIMEOUT;
		}
		catch(IOException e)
		{
			return ReturnInfo.IO_EXCEPTION;
		} 
		catch (Exception e) 
		{
			return ReturnInfo.UNKNOWN_EXPECTION;
		}
		finally
		{
			try
			{
				if(socket != null)
					socket.close();				
			}
			catch(IOException e)
			{
				return ReturnInfo.UNKNOWN_EXPECTION;
			}
		}
	}
	/**String[]: SourceId, long lowerBound, long upperBund, int pageNum**/
	public static ReturnInfo receive(int cmd, String[] args, long userId ,MyObject inObj)
	{
		Socket socket=new Socket();
		try
		{	
			socket.connect(new InetSocketAddress(InetAddress.getByName(serverIpAddr), serverPort),timeout);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			//send cmd
			oos.writeObject(cmd);
			oos.flush();
			//send userId
			oos.writeObject(userId);
			oos.flush();
			//send args
			oos.writeObject(args);
			oos.flush();
			//receive inObj
			inObj.object = ois.readObject();
			//send confirmation
			/*oos.writeObject(ReturnInfo.SUCCESS);
			oos.flush();*/
			return ReturnInfo.SUCCESS;
		} 
		catch (SocketTimeoutException e)
		{
			return ReturnInfo.REQUEST_TIMEOUT;
		}
		catch(IOException e)
		{
			return ReturnInfo.IO_EXCEPTION;
		} 
		catch (Exception e) 
		{
			return ReturnInfo.UNKNOWN_EXPECTION;
		}
		finally
		{
			try
			{
				if(socket != null)
					socket.close();				
			}
			catch(IOException e)
			{
				return ReturnInfo.UNKNOWN_EXPECTION;
			}
		}
	}
	
	public static ReturnInfo receiveFile(int cmd, long itemId, long userId, String path)
	{
		int length = 0;
		byte[] inputByte = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		int fileSize;
		int bytesRead;
		int current = 0;
		Socket socket = new Socket();
		try 
		{
			socket.connect(	new InetSocketAddress(InetAddress.getByName(serverIpAddr),
							serverPort), timeout);
			InputStream is = socket.getInputStream();
			byte[] fileSizeByteArray = new byte[4];
			is.read(fileSizeByteArray, 0, 4);
			fileSize = byteArrayToInt(fileSizeByteArray);
			inputByte = new byte[fileSize];
			fos = new FileOutputStream(new File(path));
			bos = new BufferedOutputStream(fos);
			bytesRead = is.read(inputByte, 0, fileSize);
			bos.write(inputByte, 0, fileSize);
			bos.flush();
			return ReturnInfo.SUCCESS;
		} 
		catch (IOException e) {
			return ReturnInfo.IO_EXCEPTION;
		}
		catch (Exception e){
			return ReturnInfo.UNKNOWN_EXPECTION;
		}
		finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
		}

	}

	public static ReturnInfo send(int cmd, Object obj, long userId)
	{
		Socket socket=new Socket();
		try
		{	
			socket.connect(new InetSocketAddress(InetAddress.getByName(serverIpAddr), serverPort),timeout);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			//send cmd
			oos.writeObject(cmd);
			oos.flush();
			//send userID
			oos.writeObject(userId);
			oos.flush();
			//send outObj
			oos.writeObject(obj);
			oos.flush();
			//receive confirmation
			ReturnInfo returnInfo = (ReturnInfo)ois.readObject();
			socket.close();
			return returnInfo;
		} 
		catch (SocketTimeoutException e)
		{
			return ReturnInfo.REQUEST_TIMEOUT;
		}
		catch (IOException e)
		{
			return ReturnInfo.IO_EXCEPTION;
		} 
		catch (ClassNotFoundException e) 
		{
			return ReturnInfo.CLASS_NOT_FOUND;
		}
		catch (Exception e)
		{
			return ReturnInfo.UNKNOWN_EXPECTION;
		}
		finally
		{
			try
			{
				if(socket != null)
					socket.close();				
			}
			catch(IOException e)
			{
				return ReturnInfo.UNKNOWN_EXPECTION;
			}
		}
		
	}
	
	/**useful for file transmission*/
	public static int byteArrayToInt(byte[] b) 
	{  
        int s = 0;  
        for (int i = 0; i < 3; i++) {  
            if (b[i] >= 0) {  
                s = s + b[i];  
            } else {  
                s = s + 256 + b[i];  
            }  
            s = s * 256;  
        }  
        if (b[3] >= 0) {  
            s = s + b[3];  
        } else {  
            s = s + 256 + b[3];  
        }  
        return s;  
    }
}
