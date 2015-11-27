package com.amaker.mdm.app;

import android.provider.Settings;
import android.content.ContentResolver;


public class MDMModifySettings
{
	// display -> auto
	public void MDMSetDisRotation(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
        	Settings.System.putInt(
			cr,
			Settings.System.ACCELEROMETER_ROTATION,
			flag
			);
	}

	// developer options -> usb debugging
	public void MDMSetDevUSBDebug(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.Secure.putInt(
			cr,
			Settings.Secure.ADB_ENABLED,
			flag
			);
	}

	// wireless&networks -> more -> airplane mode
	public void MDMSetNetPlaneMode(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.System.putInt(
			cr,
			Settings.System.AIRPLANE_MODE_ON,
			flag
			);
	}

	// date&time -> automatic date&time
	public void MDMSetTimeAutoTime(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.System.putInt(
			cr,
			Settings.System.AUTO_TIME,
			flag
			);
	}

	// date&time -> automatic time zone
	/*
	public void MDMSetTimeAutoZone(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.System.putInt(
			cr,
			Settings.System.AUTO_TIME_ZONE,
			flag
			);
	}
    */
	
	// wireless&networks -> more -> mobile networks -> data roaming
	public void MDMSetNetDataRoam(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.Secure.putInt(
			cr,
			Settings.Secure.DATA_ROAMING,
			flag
			);
	}

	// date&time -> choose date format   0:MM/dd/yyyy 1:dd/MM/yyyy 2:yyyy/MM/dd
	public void MDMSetTimeDataFormat(ContentResolver cr, int index)
	{
		String format = "MM/dd/yyyy";

		switch(index)
		{
		case 1:
			format = "dd/MM/yyyy";
			break;
		case 2:
			format = "yyyy/MM/dd";
			break;
		}
		
		Settings.System.putString(
			cr,
			Settings.System.DATE_FORMAT,
			format
			);
	}

	// sound -> vibrate on touch
	public void MDMSetSndVibOnTouch(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.System.putInt(
			cr,
			Settings.System.HAPTIC_FEEDBACK_ENABLED,
			flag
			);
	}

	// security -> unknown sources
	public void MDMSetSecUnkownApp(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.Secure.putInt(
			cr,
			Settings.Secure.INSTALL_NON_MARKET_APPS,
			flag
			);
	}

	// location services   0:none 1:network 2:gps 3:network and gps
	public void MDMSetLocAllow(ContentResolver cr, int index)
	{
		String mode = "";

		switch(index)
		{
		case 1:
			mode = "network";
			break;
		case 2:
			mode = "gps";
			break;
		case 3:
			mode = "network,gps";
			break;
		}
		
		Settings.Secure.putString(
			cr,
			Settings.Secure.LOCATION_PROVIDERS_ALLOWED,
			mode
			);
	}

	// for enable mass storage of usb mode
	public void MDMSetBkgdMassStor(ContentResolver cr, boolean ison)
	{
		int flag;
		if(ison)
			flag = 1;
		else
			flag = 0;
		
		Settings.Secure.putInt(
			cr,
			Settings.Secure.USB_MASS_STORAGE_ENABLED,
			flag
			);
	}

}
