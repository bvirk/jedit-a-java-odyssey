/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */
package home.txtconv;
//{{{ imports
import org.gjt.sp.jedit.jEdit;
import java.util.Properties;
//}}}

/**
 * shorter, and exposing full property key, relevant version of get/setter of jEdit Properties
 *
 */
public class options {

	//{{{ static String txtconv(String key)
	/**
     * Gets value of property
     *
     * @param key of property
     * @return String property value
     */
	public static String txtconv(String key) {
		return jEdit.getProperty(TxtConvPlugin.OPTION_PREFIX+key);
	}
	//}}}
	
	//{{{ static <V> String txtconv(String key,V value)
    /**
     * transiever - returns the result for persistent value assignment
     *
     * @param <V> any simple or toString() value
     * @param key of property
     * @param value of property
     * @return String assigned value
     */
	public static <V> String txtconv(String key,V value) {
		String val = ""+value;
		jEdit.setProperty(TxtConvPlugin.OPTION_PREFIX+key,val);
		return val;
	}
	//}}}
	
	//{{{ static int txtconvInteger(String key)
    /**
     * Gets int value
     *
     * @param key of property
     * @return int of property
     */
	public static int txtconvInteger(String key) {
		return Integer.valueOf(txtconv(key).trim());
	}
	//}}}
	
	//{{{ static boolean txtconvBoolean(String key)
    /**
     * Gets boolean value
     *
     *
     * @param key of property
     * @return boolean of property
     */
	public static boolean txtconvBoolean(String key) {
		return Boolean.valueOf(txtconv(key).trim());
	}
	//}}}
	
	//{{{ static int txtconvInteger(String key, int value)
    /**
     * transiever - returns the int result for persistent value assignment
     *
     * @param value of property as int
     * @param key of property
     * @return int value of property key
     */
	public static int txtconvInteger(String key, int value) {
			return Integer.valueOf(txtconv(key,value));
	}
	//}}}
	
	//{{{ static boolean txtconvBoolean(String key, boolean value)
    /**
     * transiever - returns the boolean result for persistent value assignment
     *
     * @param value of property as boolean
     * @param key of property
     * @return boolean value of property key
     */
	public static boolean txtconvBoolean(String key, boolean value) {
		return Boolean.valueOf(txtconv(key,value));
	}
	//}}}
	
	//{{{ static Properties txtconvProperties(String startsWithKey)
    /**
     * Get Properties of a startsWithKey - syntactic sugar for simplified looping in caller
     *
     * @param startsWithKey leading part of property
     * @return Properties of that branch that startsWithKey contains 
     */
	public static Properties txtconvProperties(String startsWithKey) {
		Properties tProp = new Properties();
		for (String s : jEdit.getProperties().stringPropertyNames())
			if (s.startsWith(startsWithKey))
				tProp.setProperty(s.substring(startsWithKey.length()),jEdit.getProperty(s));
		return tProp;
	}
	//}}}
		
}

