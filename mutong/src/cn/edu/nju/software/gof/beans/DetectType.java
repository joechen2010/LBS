package cn.edu.nju.software.gof.beans;


public enum DetectType {
	GPS("GPS"),VPN("VPN"),WIFI("WIFI");
	
	private String name;

	private DetectType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static DetectType valueOfName(String name) {
		for (DetectType e : DetectType.values()) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.print(DetectType.valueOfName("*"));
	}
}
