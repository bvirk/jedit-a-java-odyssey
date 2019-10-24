/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */ 
package home.txtconv;
//{{{ import
import org.xml.sax.Attributes;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.ext.DefaultHandler2;
import java.io.FileReader;

import org.gjt.sp.jedit.jEdit;
import static home.txtconv.util.Util.*;
//}}}


public class ModesOfGlob extends DefaultHandler2 {

    public static String getNameOfSuffix(String fileSuffix) {
		return new ModesOfGlob().getName(fileSuffix);
	}
    private ModesOfGlob() {
		super();
	}
	public void startElement (String uri, String name, String qName, Attributes atts) {
		if (qName.equals("MODE")) {
			String mName=null;
			String file_name_glob=null;
			for (int i=0; i< atts.getLength();i++)
				switch (atts.getLocalName(i)) {
					case "NAME":
						mName= atts.getValue(i);
						break;
					case "FILE_NAME_GLOB":
						file_name_glob=atts.getValue(i);
				}
			if ((null != mName) && sGlob.equals(file_name_glob))
				this.name=mName;
		}
	}
	String getName(String fileSuffix) {
		try ( FileReader r = new FileReader(jEdit.getJEditHome()+"/modes/catalog")) {
			sGlob="*."+fileSuffix;
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			xr.setContentHandler(this);
			xr.setErrorHandler(this);
			xr.parse(new InputSource(r));
		} catch (Exception e) { BeanShellErrorDialog(e); }
		return name;
	}
	private String sGlob;
    private String name;
}
