package controller;

import model.Position;


/**
 * This class makes the Position object type ready to be sent.
 */
public class SPosition extends Position{
	public SPosition(Position p){
		setLongitude(p.getLongitude());
		setLatitude(p.getLatitude());
		setDate(p.getDate());
	}
}
