package gm.shared.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Helps serialize/deserialize Objects to strings
 * and vice versa.
 * 
 * @author voidStern
 * @author stefan
 */
public class Serializer {
	
	/**
	 * Uses the Android port of the XStream Lib
	 * See http://xstream.codehaus.org/tutorial.html
	 */
	private static XStream xstream = null;
	
	private static void init() {
		if (xstream == null) {
			DomDriver dd = new DomDriver();
			xstream = new XStream(dd);
		}
	}
	
	/**
	 * Serializes an object to a string
	 * 
	 * @param o Object
	 * @return Serialized Object
	 * @throws IOException
	 */
	public static String serialize(Serializable o) throws IOException{
		init();
		return xstream.toXML(o);
	}
	
	/**
	 * Deserializes a string to an object
	 * 
	 * @param s String
	 * @return First Object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Serializable deserialize(String s) throws IOException, ClassNotFoundException{
		init();
		return (Serializable)xstream.fromXML(s);
	}
}
