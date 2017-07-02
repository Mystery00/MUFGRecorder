package com.janyo.mufgrecorder.`class`

import org.litepal.crud.DataSupport

class MUFG(var id:String):DataSupport()
{
	var contentMap:Map<String,Int> ?=HashMap()
}