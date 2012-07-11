package com.model;

import java.util.ArrayList;
import java.util.Iterator;



public class Group {
	ArrayList<KNote> notes = null;
	KPosition position = null;
	
	public Group(){
		notes = new ArrayList<KNote>();
	}


	private KPosition updatePosition(){
		if(notes.size()==0) return null;
		float latitude=0, longitude=0;
		for(int i=0;i<notes.size();i++){
			latitude+=notes.get(i).getPosition().getLatitudeFloat();
			longitude+=notes.get(i).getPosition().getLongitudeFloat();
		}
		KPosition p = new KPosition();
		p.setLatitude(latitude/notes.size());
		p.setLongitude(longitude/notes.size());
		position=p;
		return getPosition();
	}
	
	public KPosition getPosition(){
		return position;
	}
	
	public void addKNote(KNote e){
		notes.add(e);
		updatePosition();
	}
	
	
	public boolean isGroup()
	{
		if(notes.size()>1)
			return true;
		return false;
		
	}
	
	public String getName(){
		if(notes.size()>1){
			return notes.size() + " notes" ;
		}else{
			return notes.get(0).getNote();
		}
	}
	
	public String getMessage(){
		return null;
	}
	
	public Iterator<KNote> iterator()
	{
		return notes.iterator();
	}
	
	public boolean hasPhoto()
	{
		if(!isGroup())
		{
			return notes.get(0).getHasPhoto();
		}
		return false;
	}
}
