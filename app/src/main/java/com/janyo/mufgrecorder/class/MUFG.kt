package com.janyo.mufgrecorder.`class`

import java.io.Serializable

class MUFG(var MUFGID: String) :  Serializable
{
	var contentMap: HashMap<String, Int> = HashMap()
}