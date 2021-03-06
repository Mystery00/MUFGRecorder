package com.janyo.mufgrecorder.`class`

import org.litepal.crud.DataSupport

class UpdateItems() : DataSupport()
{
	var updateTime: String = ""
	var mufgName: String = ""

	var PortalShield_Common= 0
	var PortalShield_Rare = 0
	var PortalShield_VeryRare= 0

	var LinkAmp_Common = 0
	var LinkAmp_Rare = 0
	var LinkAmp_VeryRare = 0

	var HeatSink_Common = 0
	var HeatSink_Rare = 0
	var HeatSink_VeryRare = 0

	var Multihack_Common = 0
	var Multihack_Rare = 0
	var Multihack_VeryRare = 0

	var PowerCube_1 = 0
	var PowerCube_2 = 0
	var PowerCube_3 = 0
	var PowerCube_4 = 0
	var PowerCube_5 = 0
	var PowerCube_6 = 0
	var PowerCube_7 = 0
	var PowerCube_8 = 0

	var Resonator_1 = 0
	var Resonator_2 = 0
	var Resonator_3 = 0
	var Resonator_4 = 0
	var Resonator_5 = 0
	var Resonator_6 = 0
	var Resonator_7 = 0
	var Resonator_8 = 0

	var XmpBurster_1 = 0
	var XmpBurster_2 = 0
	var XmpBurster_3 = 0
	var XmpBurster_4 = 0
	var XmpBurster_5 = 0
	var XmpBurster_6 = 0
	var XmpBurster_7 = 0
	var XmpBurster_8 = 0

	var UltraStrike_1 = 0
	var UltraStrike_2 = 0
	var UltraStrike_3 = 0
	var UltraStrike_4 = 0
	var UltraStrike_5 = 0
	var UltraStrike_6 = 0
	var UltraStrike_7 = 0
	var UltraStrike_8 = 0

	var Media = 0
	var AXAShield = 0
	var SoftBank = 0
	var ForceAmp = 0
	var Turret = 0
	var PortalKey = 0
	var JarvisVirus = 0
	var ADARefactor = 0
	var LawsonPowerCube = 0
	var CircleKPowerCube = 0

	constructor(mufgName: String) : this()
	{
		this.mufgName = mufgName
	}

	constructor(updateTime: String, mufgName: String) : this()
	{
		this.updateTime = updateTime
		this.mufgName = mufgName
	}
}