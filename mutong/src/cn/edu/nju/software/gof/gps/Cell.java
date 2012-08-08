package cn.edu.nju.software.gof.gps;

public class Cell{
	
	/*TelephonyManager tm = (TelephonyManager) context
			.getSystemService(Context.TELEPHONY_SERVICE);
	GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
	int cid = gcl.getCid();
	int lac = gcl.getLac();
	int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0,
			3));
	int mnc = Integer.valueOf(tm.getNetworkOperator().substring(3,
			5));*/
	
	private int cell_id;
	private int location_area_code;
	private int mobile_country_code;
	private int mobile_network_code;
	
	public Cell(int cell_id, int location_area_code,
			int mobile_country_code, int mobile_network_code) {
		this.cell_id = cell_id;
		this.location_area_code = location_area_code;
		this.mobile_country_code = mobile_country_code;
		this.mobile_network_code = mobile_network_code;
	}

	public int getCell_id() {
		return cell_id;
	}

	public void setCell_id(int cell_id) {
		this.cell_id = cell_id;
	}

	public int getLocation_area_code() {
		return location_area_code;
	}

	public void setLocation_area_code(int location_area_code) {
		this.location_area_code = location_area_code;
	}

	public int getMobile_country_code() {
		return mobile_country_code;
	}

	public void setMobile_country_code(int mobile_country_code) {
		this.mobile_country_code = mobile_country_code;
	}

	public int getMobile_network_code() {
		return mobile_network_code;
	}

	public void setMobile_network_code(int mobile_network_code) {
		this.mobile_network_code = mobile_network_code;
	}
}