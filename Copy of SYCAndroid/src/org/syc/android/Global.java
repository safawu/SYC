package org.syc.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.syc.framework.Kernel.KernelManager;
import org.syc.framework.Util.Config;

import android.graphics.AvoidXfermode.Mode;
import android.os.Environment;

public class Global
{
	public static Config config = new Config();
	public static int mode = Const.LOCAL_MODE;
	
	private static String path = Environment.getExternalStorageDirectory().getPath() + "/SYC/cfg.dat";
	
	public static void loadConfig()
	{
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		File file = null;
		try 
		{
			file = new File(path);
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			config = (Config)ois.readObject();
		} catch (FileNotFoundException e) {
			return;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally{
				try 
				{
					if(fis != null)
						fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try 
				{
					if(ois != null)
						ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void writeConfig()
	{
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		File file = null;
			try
			{
				file = new File(path);
				fos = new FileOutputStream(file);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(config);		
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try 
				{
					if(fos != null)
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try 
				{
					if(oos != null)
						oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	

		}
	}
}
