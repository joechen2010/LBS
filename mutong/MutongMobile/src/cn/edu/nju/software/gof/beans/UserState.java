package cn.edu.nju.software.gof.beans;

public class UserState {

	public static final int OFF_LINE = 0;
	public static final int HIDE = 1;
	public static final int ON_LINE = 2;
	
	public static int parseParam(String stateInString) {
		if(stateInString.equals("ON_LINE")) {
			return 2;
		} else if(stateInString.equals("HIDE")) {
			return 1;
		} else {
			return 0;
		}
	}
}
