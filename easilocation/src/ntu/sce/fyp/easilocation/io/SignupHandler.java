package ntu.sce.fyp.easilocation.io;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SignupHandler extends DefaultHandler{
	
	private boolean create = false;
	private boolean status = false;
	private boolean message = false;
	private ParsedLoginDataSet myParsedLoginDataSet = new ParsedLoginDataSet();

	public ParsedLoginDataSet getParsedLoginData() {
		return this.myParsedLoginDataSet;
	}

	@Override
	public void startDocument() throws SAXException {
		this.myParsedLoginDataSet = new ParsedLoginDataSet();
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equals("login")) {
			this.create = true;
		} else if (localName.equals("status")) {
			this.status = true;
		} else if (localName.equals("message")) {
			this.message = true;
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equals("login"))
			this.create = false;
		else if (localName.equals("status"))
			this.status = false;
		else if (localName.equals("message"))
			this.message = false;
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (this.status) {
			myParsedLoginDataSet.setExtractedString(new String(ch, start,
					length));
		} else if (this.message) {
			myParsedLoginDataSet.setMessage(new String(ch, start, length));
		}
	}
}
