package de.gfred.lbbms.service.util;

import java.util.List;

/**
 * Calculate the distance and degree from GPS coordinates.
 * @author Frederik G�tz
 */
public class LocationCalculator {
        private static final String TAG = "de.gfred.lbbms.service.util.LocationCalculator";
        private static final boolean DEBUG = false;

        private static final double EARTH_RADIUS = 6371.01;

      	private static final double MIN_LAT = Math.toRadians(-90d);  // -PI/2
	private static final double MAX_LAT = Math.toRadians(90d);   //  PI/2
	private static final double MIN_LON = Math.toRadians(-180d); // -PI
	private static final double MAX_LON = Math.toRadians(180d);  //  PI


        /*
	 * Anfangspunkt muss nur einmal Angegeben werden, letzer Punkt verbindet zur�ck zum anfangspunkt
	 * BSP Punkte 1,2,3:
	 * 		Distanz Punkt 1 + 2;
	 * 		Distanz Punkt 2 + 3;
	 * 		Distanz Punkt 3 + 1; => Strecken Entfernung!
	 * 
	 * Andere M�glichkeit 1
	 * Distanz Punkt 1+2;
	 * Distanz Punkt 2+3; => Ende
	 * 
	 * Andere M�glichkeit 1
	 * Distanz Punkt 1+2;
	 * Distanz Punkt 2+3; => Distanz*2; => Gleicher R�ckweg
	 * 
	 * 
	 * Luftlinienberechnung
	 */
	
	public static double calculateLoopRouteDistance(List<Double> latitude,List<Double> longitude){
		return calculateRouteDistance(latitude,longitude,true); 
	}
	/**
	 * Calculate the distance of the Route (Single Way)
	 * 
	 * @param latitude List of latitude GPS coordinates
	 * @param longitude List of longitude GPS coordinates
	 * @return distance in meter
	 */
	public static double calculateRouteDistance(List<Double> latitude,List<Double> longitude){
		return calculateRouteDistance(latitude,longitude,false); 
	}
	
	public static double calculateRouteDistance(List<Double> latitude, List<Double> longitude,boolean loopRoad) throws IllegalArgumentException{
		double distance=0;
		
		if(latitude.size()==longitude.size() && latitude.size()>1 && longitude.size()>1){
			for(int i=1;i<=latitude.size()-1;i++){
				distance+=calculateDistance(latitude.get(i-1), longitude.get(i-1), latitude.get(i), longitude.get(i));
			}
			
			if(loopRoad){
				distance+=calculateDistance(latitude.get(0), longitude.get(0), latitude.get(latitude.size()-1), longitude.get(longitude.size()-1));
			}
			
			return distance;
		}else{
			return 0;
		}
		
//		throw new IllegalArgumentException("longitude and latitude not at the same size or Size is smaller then 2");
	}
	
	/**
	 * Calculate the distance between two GPS coordinates (WGS-84 ellipsiod)
	 * 
	 * @param latitude1 GPS Latitude Parameter from first point
	 * @param longitude1 GPS Longitude Parameter from the first point
	 * @param latitude2 GPS Latitude Parameter from second point
	 * @param longitude2 GPS Longitude Parameter from second point
	 * @return Distance between the two points
	 */
    public static double calculateDistance(double latitude1, double longitude1,double latitude2,double longitude2){
	    double a = 6378137, b = 6356752.3142,  f = 1/298.257223563;  // WGS-84 ellipsiod
	    double L = Math.toRadians(longitude2-longitude1);
	    double U1 = Math.atan((1-f)*Math.tan(Math.toRadians(latitude1)));  
	    double U2 = Math.atan((1-f)*Math.tan(Math.toRadians(latitude2))); 
	    double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
	    double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);
	    double lambda = L, lambdaP, iterLimit = 20;
	    double cosSqAlpha, sinAlpha;
	    double cosSigma, sinSigma;
	    double sinLambda,cosLambda;
	    double sigma;
	    double cos2SigmaM;
	
	    do{
	        sinLambda = Math.sin(lambda);
	
	        cosLambda = Math.cos(lambda);
	
	        sinSigma = Math.sqrt((cosU2*sinLambda)*(cosU2*sinLambda)+(cosU1*sinU2-sinU1*cosU2*cosLambda)*
	        		(cosU1*sinU2-sinU1*cosU2*cosLambda));
	
	        if (sinSigma==0)
	            return 0;  // co-incident points
	
	        cosSigma = sinU1*sinU2 + cosU1*cosU2*cosLambda;
	
	        sigma = Math.atan2(sinSigma, cosSigma);
	
	        sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
	
	        cosSqAlpha = 1 - sinAlpha*sinAlpha;
	
	        cos2SigmaM = cosSigma - 2*sinU1*sinU2/cosSqAlpha;
	
	        if (cos2SigmaM==Double.NaN)
	            cos2SigmaM = 0;  // equatorial line: cosSqAlpha=0 (§6)
	
	        double C = f/16*cosSqAlpha*(4+f*(4-3*cosSqAlpha));
	
	        lambdaP = lambda;
	        lambda = L+(1-C)*f*sinAlpha*(sigma + C*sinSigma*(cos2SigmaM+C*cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)));
	    }while(Math.abs(lambda-lambdaP) > 1e-12 && --iterLimit>0);
	
	    if (iterLimit==0)
	        return Double.NaN;  // formula failed to converge
	
	    double uSq = cosSqAlpha*(a*a - b*b)/(b*b);
	    double A = 1 + uSq/16384*(4096+uSq*(-768+uSq*(320-175*uSq)));
	    double B = uSq/1024 * (256+uSq*(-128+uSq*(74-47*uSq)));
	    double deltaSigma = B*sinSigma*(cos2SigmaM+B/4*(cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)-B/6*cos2SigmaM*
	    		(-3+4*sinSigma*sinSigma)*(-3+4*cos2SigmaM*cos2SigmaM)));
	    double distance = b*A*(sigma-deltaSigma);
	
		int dtmp = (int)Math.floor(distance);
		distance = (distance-dtmp)*100;
		int dtmp2 = (int)distance;
		distance = dtmp + (double)(dtmp2)/100d; // Precision: xx,00m
		
	    return distance;
    }

    /**
     *
     * @param longitude
     * @param latitude
     * @param range - in meter
     * @return
     */
    public static double[] calculateBoundingCoordinates(double longitude, double latitude, double range){
        range = range / 1000;
        
        double radLat = Math.toRadians(latitude);
	double radLon = Math.toRadians(longitude);

        if (range < 0d){
            throw new IllegalArgumentException();
        }

        // angular distance in radians on a great circle
        double radDist = range / EARTH_RADIUS;

        double minLat = radLat - radDist;
        double maxLat = radLat + radDist;

        double minLon, maxLon;
        if (minLat > MIN_LAT && maxLat < MAX_LAT) {
                double deltaLon = Math.asin(Math.sin(radDist) /
                        Math.cos(radLat));
                minLon = radLon - deltaLon;
                if (minLon < MIN_LON) minLon += 2d * Math.PI;
                maxLon = radLon + deltaLon;
                if (maxLon > MAX_LON) maxLon -= 2d * Math.PI;
        } else {
                // a pole is within the distance
                minLat = Math.max(minLat, MIN_LAT);
                maxLat = Math.min(maxLat, MAX_LAT);
                minLon = MIN_LON;
                maxLon = MAX_LON;
        }

        return new double[]{Math.toDegrees(minLat), Math.toDegrees(minLon), Math.toDegrees(maxLat), Math.toDegrees(maxLon)};
    }

    /**
     * Calculate the moving degree between two GPS coordinates
     *  
	 * @param latitude1 GPS Latitude Parameter from first point
	 * @param longitude1 GPS Longitude Parameter from the first point
	 * @param latitude2 GPS Latitude Parameter from second point
	 * @param longitude2 GPS Longitude Parameter from second point
     * @return Degree
     */
    static public float calculateDegree(double latitude1,double longitude1,double latitude2,double longitude2){
		double latitude1PI = latitude1*Math.PI/180;
		double latitude2PI = latitude2*Math.PI/180;
		
		double value = Math.sin(latitude1PI)*Math.sin(latitude2PI)+Math.cos(latitude1PI)*Math.cos(latitude2PI)*
		Math.cos((longitude1-longitude2)*Math.PI/180);
		
		double tmp = (longitude1-longitude2<0)?0:360;
		
		double degree = Math.abs(Math.acos(Math.sin(latitude2PI)/Math.sin(Math.acos(value))/Math.cos(latitude1PI)-
				Math.tan(latitude1PI)/Math.tan(Math.acos(value)))*180/Math.PI-tmp);
		
		return (float)degree;
    }	
}
