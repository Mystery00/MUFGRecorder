package com.janyo.mufgrecorder.util

import android.content.Context
import com.janyo.mufgrecorder.`class`.MUFG
import java.io.*

class FileUtil(var context: Context)
{
	fun saveObject(obj: Any, fileName: String, dirName: String)
	{
		var fileOutputStream: FileOutputStream? = null
		var objectOutputStream: ObjectOutputStream? = null
		val dir = File(context.filesDir.absolutePath + File.separator + dirName)
		if (dir.exists() || dir.mkdirs())
		{
			val file = File(dir.absolutePath + File.separator + fileName)
			if (file.exists() || file.createNewFile())
			{
				fileOutputStream = FileOutputStream(file)  //新建一个内容为空的文件
				objectOutputStream = ObjectOutputStream(fileOutputStream)
				objectOutputStream.writeObject(obj)
			}
		}
		if (objectOutputStream != null)
		{
			objectOutputStream.close()
		}
		if (fileOutputStream != null)
		{
			fileOutputStream.close()
		}
	}

	fun getObjects(dirName: String): ArrayList<MUFG>
	{
		val list = ArrayList<MUFG>()
		var objectInputStream: ObjectInputStream? = null
		var fileInputStream: FileInputStream? = null
		val dir = File(context.filesDir.absolutePath + File.separator + dirName)
		if (dir.exists() || dir.mkdirs())
		{
			for (file in dir.listFiles())
			{
				if (file.exists() || file.createNewFile())
				{
					fileInputStream = FileInputStream(file)
					objectInputStream = ObjectInputStream(fileInputStream)
					list.add(objectInputStream.readObject() as MUFG)
				}
			}
		}
		if (objectInputStream != null)
		{
			objectInputStream.close()
		}
		if (fileInputStream != null)
		{
			fileInputStream.close()
		}
		return list
	}
}