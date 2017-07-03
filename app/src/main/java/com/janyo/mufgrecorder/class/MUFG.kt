package com.janyo.mufgrecorder.`class`

import java.io.Serializable

class MUFG(var MUFGID: String) : Serializable
{
	var content = ArrayList<HashMap<String, Any>>()
}