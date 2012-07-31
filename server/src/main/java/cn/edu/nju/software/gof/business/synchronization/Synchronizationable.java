package cn.edu.nju.software.gof.business.synchronization;

import com.google.appengine.api.datastore.Key;

public interface Synchronizationable {
	
	public void synchronize(Key personID, String placeName);
	
}
