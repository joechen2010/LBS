package cn.edu.nju.software.gof.gps;

import cn.edu.nju.software.gof.gps.GpsTask.GpsData;

public interface GpsTaskCallBack {

	public void gpsConnected(GpsData gpsdata);
	
	public void gpsConnectedTimeOut();
	
}
