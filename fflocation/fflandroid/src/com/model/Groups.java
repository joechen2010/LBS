package com.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Groups{
	
	ArrayList<Group> listGroup = new ArrayList<Group>();
	private float distance=0.0f;
	
	public Groups(float d){
		distance=d;
	}
	
	public Iterator<Group> iterator()
	{
		return listGroup.iterator();
	}
	
	public void addKNote(KNote e){
		float minDis=Float.MAX_VALUE;
		Group group = null;
		for(int i=0; i<listGroup.size(); i++){
			float dis = e.getPosition().calcDistance(listGroup.get(i).getPosition());
			if(group == null || minDis>dis){
				minDis=dis;
				group=listGroup.get(i);
			}
		}
		if(group==null || minDis>distance){
			Group ng = new Group();
			ng.addKNote(e);
			listGroup.add(ng);
		}else{
			group.addKNote(e);
		}
	}
	

	public void addKNotes(List<KNote> list) {
		Iterator<KNote> it = list.iterator();
		while(it.hasNext())
			addKNote(it.next());
	}
	
	
	public String toString()
	{
		String t = listGroup.size() + " groups\n";
		for(int i=0;i<listGroup.size();i++){
			t+=listGroup.get(i).toString() + "\n";
		}
		return t;
	}

	public int size()
	{
		return listGroup.size();
	}
	

}
