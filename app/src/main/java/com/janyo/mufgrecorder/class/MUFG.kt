package com.janyo.mufgrecorder.`class`

import org.litepal.crud.DataSupport
import java.io.Serializable

class MUFG(var MUFGID: String) : DataSupport(), Serializable
{
	var contentMap: HashMap<String, Int> = HashMap()
}