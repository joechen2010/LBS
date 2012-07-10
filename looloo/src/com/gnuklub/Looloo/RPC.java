/*
  Authors 
  Dragan Jovev <dragan.jovev@gmail.com>
  Mladen Djordjevic <mladen.djordjevic@gmail.com>
  
  Released under the GPL, as follows:

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.gnuklub.Looloo;

/*
 * Generic class used to transmit and receive
 * data from server
 */

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.location.Location;
import android.util.Log;

class ConnectionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConnectionException(String msg) {
		super(msg);
	}
}

class ParseException extends Exception {
	private static final long serialVersionUID = 1L;

	public ParseException(String msg) {
		super(msg);
	}
}

public class RPC {

	private String server;
	private String port;
	private String rpc_call;

	public RPC(Context c) {
		server = c.getString(R.string.defaultServer);
		port = c.getString(R.string.defaultPort);
		server += ":" + port;
	}

	public int login(String username, String password)
			throws ConnectionException {

		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>login</methodName>\r\n" + "  <params>\r\n"
				+ "    <param>\r\n" + "      <value><string>" + username
				+ "</string></value>\r\n" + "    </param>\r\n"
				+ "    <param>\r\n" + "      <value><string>" + password
				+ "</string></value>\r\n" + "    </param>\r\n"
				+ "  </params>\r\n" + "</methodCall>\r\n";

		String XMLResponse = callRPC(rpc_call);
		// Log.v("PITANJE", rpc_call);
		// Log.v("ODGOVOR", XMLResponse);
		if (XMLResponse != null) {
			try {
				XMLParser parser = parseXMLResponse(XMLResponse,
						Constants.PARSE_MODE_LOGIN);
				return parser.getLoginResponse();
			} catch (ParseException e) {
				Log.e("DEBUG", "Unable to parse XML", e);
				return -1;
			}
		} else {
			throw new ConnectionException("No Connection");
		}
	}

	public int logout(int sessid) throws ConnectionException {

		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>logout</methodName>\r\n" + "  <params>\r\n"
				+ "    <param>\r\n" + "      <value><int>" + sessid
				+ "</int></value>\r\n" + "    </param>\r\n" + "  </params>\r\n"
				+ "</methodCall>\r\n";

		String XMLResponse = callRPC(rpc_call);
		// Log.v("PITANJE", rpc_call);
		// Log.v("ODGOVOR", XMLResponse);
		if (XMLResponse != null) {
			try {
				XMLParser parser = parseXMLResponse(XMLResponse,
						Constants.PARSE_MODE_LOGOUT);
				return parser.getLogoutResponse();
			} catch (ParseException e) {
				Log.e("DEBUG", "Unable to parse XML", e);
				return -1;
			}
		} else {
			throw new ConnectionException("No Connection");
		}
	}

	public ArrayList<PlacePoint> getPlacesByInterest(int sessid, int bitmask,
			double distance) throws ConnectionException {

		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>getPlacesByInterest</methodName>\r\n"
				+ "  <params>\r\n" + "    <param>\r\n" + "      <value><int>"
				+ sessid + "</int></value>\r\n" + "    </param>\r\n"
				+ "    <param>\r\n" + "      <value><int>" + bitmask
				+ "</int></value>\r\n" + "    </param>\r\n" + "    <param>\r\n"
				+ "      <value><double>" + distance + "</double></value>\r\n"
				+ "    </param>\r\n" + "  </params>\r\n" + "</methodCall>\r\n";

		String XMLResponse = callRPC(rpc_call);
		// Log.v("PITANJE", rpc_call);
		// Log.v("ODGOVOR", XMLResponse);
		if (XMLResponse != null) {
			try {
				XMLParser parser = parseXMLResponse(XMLResponse,
						Constants.PARSE_MODE_PLACES);
				return parser.getPlaces();
			} catch (ParseException e) {
				Log.e("DEBUG", "Unable to parse XML", e);
				return null;
			}
		} else {
			throw new ConnectionException("No Connection");
		}
	}

	public ArrayList<PersonPoint> getPeopleByInterest(int sid, int interest,
			int bitmask, double distance) throws ConnectionException {

		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>getPeopleByInterest</methodName>\r\n"
				+ "  <params>\r\n" + "    <param>\r\n" + "      <value><int>"
				+ sid + "</int></value>\r\n" + "    </param>\r\n"
				+ "    <param>\r\n" + "      <value><int>" + interest
				+ "</int></value>\r\n" + "    </param>\r\n" + "    <param>\r\n"
				+ "      <value><int>" + bitmask + "</int></value>\r\n"
				+ "    </param>\r\n" + "    <param>\r\n"
				+ "      <value><double>" + distance + "</double></value>\r\n"
				+ "    </param>\r\n" + "  </params>\r\n" + "</methodCall>\r\n";

		String XMLResponse = callRPC(rpc_call);

		// Log.v("PITANJE", rpc_call);
		// Log.v("ODGOVOR", XMLResponse);

		if (XMLResponse != null) {
			try {
				XMLParser parser = parseXMLResponse(XMLResponse,
						Constants.PARSE_MODE_PEOPLE);
				return parser.getPeople();
			} catch (ParseException e) {
				Log.e("DEBUG", "Unable to parse XML", e);
				return null;
			}
		} else {
			throw new ConnectionException("No Connection");
		}
	}

	public PlacePoint getPlaceDetails(int sid, int pid)
			throws ConnectionException {

		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>getPlaceDetails</methodName>\r\n"
				+ "  <params>\r\n" + "    <param>\r\n" + "      <value><int>"
				+ sid + "</int></value>\r\n" + "    </param>\r\n"
				+ "    <param>\r\n" + "      <value><int>" + pid
				+ "</int></value>\r\n" + "    </param>\r\n" + "  </params>\r\n"
				+ "</methodCall>\r\n";

		String XMLResponse = callRPC(rpc_call);
		// Log.v("PITANJE", rpc_call);
		// Log.v("ODGOVOR", XMLResponse);
		if (XMLResponse != null) {
			try {
				XMLParser parser = parseXMLResponse(XMLResponse,
						Constants.PARSE_MODE_POINT_INFO);
				return parser.getPlacePointInfo();
			} catch (ParseException e) {
				Log.e("DEBUG", "Unable to parse XML", e);
				return null;
			}
		} else {
			throw new ConnectionException("No Connection");
		}
	}

	public PersonPoint getPersonDetails(int sid, int pid)
			throws ConnectionException {
		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>getPersonDetails</methodName>\r\n"
				+ "  <params>\r\n" + "    <param>\r\n" + "      <value><int>"
				+ sid + "</int></value>\r\n" + "    </param>\r\n"
				+ "    <param>\r\n" + "      <value><int>" + pid
				+ "</int></value>\r\n" + "    </param>\r\n" + "  </params>\r\n"
				+ "</methodCall>\r\n";

		String XMLResponse = callRPC(rpc_call);
		/*
		 * Log.v("PITANJE", rpc_call); Log.v("ODGOVOR", XMLResponse);
		 */
		if (XMLResponse != null) {
			try {
				XMLParser parser = parseXMLResponse(XMLResponse,
						Constants.PARSE_MODE_PERSON_INFO);
				return parser.getPersonPointInfo();
			} catch (ParseException e) {
				Log.e("DEBUG", "Unable to parse XML", e);
				return null;
			}
		} else {
			throw new ConnectionException("No Connection");
		}
	}

	public int updatePosition(int sid, double X, double Y) {
		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>updatePosition</methodName>\r\n"
				+ "  <params>\r\n" + "    <param>\r\n" + "      <value><int>"
				+ sid + "</int></value>\r\n" + "    </param>\r\n"
				+ "    <param>\r\n" + "      <value><double>" + X
				+ "</double></value>\r\n" + "    </param>\r\n"
				+ "    <param>\r\n" + "      <value><double>" + Y
				+ "</double></value>\r\n" + "    </param>\r\n"
				+ "  </params>\r\n" + "</methodCall>\r\n";

		//String XMLResponse = 
		callRPC(rpc_call);

		//Log.v("PITANJE", rpc_call);
		//Log.v("ODGOVOR", XMLResponse);
		return 0;
	}

	public int updateAccount(int sessid, String password, int gender,
			int birth, String occupation, String email, int interest) {

		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>updateAccount</methodName>\r\n"
				+ "  <params>\r\n" + "    <param>\r\n" + "      <value><int>"
				+ sessid
				+ "</int></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><string>"
				+ password
				+ "</string></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><int>"
				+ gender
				+ "</int></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><int>"
				+ birth
				+ "</int></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><string>"
				+ occupation
				+ "</string></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><string>"
				+ email
				+ "</string></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><int>"
				+ interest
				+ "</int></value>\r\n"
				+ "    </param>\r\n"
				+ "  </params>\r\n"
				+ "</methodCall>\r\n";

		//String XMLResponse = 
		callRPC(rpc_call);

		//Log.v("PITANJE", rpc_call);
		//Log.v("ODGOVOR", XMLResponse);
		return 0;
	}

	public int createAccount(String uname, String password, int gender,
			int birth, String occupation, String email, int interest,
			Location loc) throws ConnectionException {
		rpc_call = "<?xml version=\"1.0\"?>\r\n" + "<methodCall>\r\n"
				+ "  <methodName>createAccount</methodName>\r\n"
				+ "  <params>\r\n" + "    <param>\r\n"
				+ "      <value><string>"
				+ uname
				+ "</string></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><string>"
				+ password
				+ "</string></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><int>"
				+ gender
				+ "</int></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><int>"
				+ birth
				+ "</int></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><string>"
				+ occupation
				+ "</string></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><string>"
				+ email
				+ "</string></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><int>"
				+ interest
				+ "</int></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><double>"
				+ loc.getLatitude()
				+ "</double></value>\r\n"
				+ "    </param>\r\n"
				+ "    <param>\r\n"
				+ "      <value><double>"
				+ loc.getLongitude()
				+ "</double></value>\r\n"
				+ "    </param>\r\n" + "  </params>\r\n" + "</methodCall>\r\n";

		String XMLResponse = callRPC(rpc_call);
		// Log.v("PITANJE", rpc_call);
		// Log.v("ODGOVOR", XMLResponse);
		if (XMLResponse != null) {
			try {
				XMLParser parser = parseXMLResponse(XMLResponse,
						Constants.PARSE_MODE_CREATE_ACCOUNT);
				return parser.getCreateAccountResponse();
			} catch (ParseException e) {
				Log.e("DEBUG", "Unable to parse XML", e);
				return -1;
			}
		} else {
			throw new ConnectionException("No Connection");
		}

	}

	private String callRPC(String callXML) {
		try {
			URL u = new URL(server);
			try {
				URLConnection uc = u.openConnection();
				HttpURLConnection connection = (HttpURLConnection) uc;
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				OutputStream out = connection.getOutputStream();
				OutputStreamWriter wout = new OutputStreamWriter(out, "UTF-8");
				wout.write(callXML);
				wout.flush();
				out.close();
				InputStream in = connection.getInputStream();
				String output = new String();
				int c;
				while ((c = in.read()) != -1) {
					output += (char) c;
				}
				in.close();
				out.close();
				connection.disconnect();
				return output;
			} catch (IOException e) {
				return null;
			}

		} catch (IOException e) {
			Log.e("ERROR", e.toString());
			return null;
		}
	}

	private XMLParser parseXMLResponse(String XMLResponse, int parserMode)
			throws ParseException {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XMLParser parser = new XMLParser();
			parser.setParserMode(parserMode);
			xr.setContentHandler(parser);
			Reader reader = new CharArrayReader(XMLResponse.toCharArray());
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			xr.parse(is);
			return parser;
		} catch (Exception e) {
			Log.e("DEBUG", "No valid XML found", e);
			throw new ParseException("Unable to parse XML");
		}
	}
}