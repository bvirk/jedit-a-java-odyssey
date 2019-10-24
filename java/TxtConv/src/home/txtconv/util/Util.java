/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */
package home.txtconv.util;
//{{{ imports
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.util.Log;
import console.Console;
import org.gjt.sp.jedit.gui.DockableWindowManager;
import javax.swing.JComponent;
import home.txtconv.TxtConvPlugin;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.awt.Color;
import java.io.File;
//}}}


/**
 * Embeding this class Util in a project is one of several stragegies to develop java - it is like having System.out.println's
 * that have to be removed when the project is tested and finished.
 * <p>
 * Print methods are inserted among the statement se be able to monitor what happens and catch fails in programming
 * <p>
 * Class Util facilitates printing in a myJedit dockable window. It is a bunch of static function serving all classes in a project.
 * <p>
 * The classes in txtConvPlugin has a commen printUrgency level - it is the static TxtConvPlugin.printUrgency.
 * <p>
 * The level can be changed as needed with setPrintUrgency(int level) of TxtConvPlugin
 * <p>
 * Each inserted print method has its own urgency level - either the deault which is Log.MESSAGE or explicit given as parameter.
 * <p>
 * Log.DEBUG,Log.MESSAGE,Log.NOTICE,Log.WARNING,Log.ERROR which repspective int value is 1,3,5,7,9 is, in printing, prefixed 
 * their level - With that little twist, that urgenecies greater than Log.ERROR not is prefixed its level.
 * <p>
 * Class Util's print methods is hardwired to look at TxtConvPlugin.printUrgency for comparison of every single print occurrence's 
 * urgency parameter.<br>
 * When thread of execution reached the print method a print is done if its urgency is equal or above that of TxtConvPlugin.printUrgency
 * <P>The class do also facilitates printing casts of throwable in try/catch statement.
 * <p>Class Util also contains some jEdit Properties txtConvPlugin shortcuts.
 * <p>Using class Util, links the plugin which use it to print and show exception info on Console's beanshell dockable window. 
 * <p>For using the code of TxtConvPlugin in other environments the functionality of class Util must be removed or adapted.
 */
public class Util {
	
	///{{{ public method

	//{{{ void print(int urgency,T message)
    /**
     * printet when urgency is equal or above TxtConvPlugin.printUrgency
     *
     * @param message to be printet
     * @param urgency equal or above means printing
     * @param <T> any object or simple type
     */
	public static<T> void print(int urgency,T message) {
		if (urgency >= TxtConvPlugin.printUrgency && (urgency & 1) ==1 )
			con(((urgency <= Log.ERROR) ? ""+urgency+": " : "")+message);	
	}
	//}}}
	
	//{{{ void print(T message) 
	/**
	 * printet when TxtConvPlugin.printUrgency is equal or below Log.Message 
	 *
     * @param message to be printed when TxtConvPlugin.printUrgency is equal or below Log.MESSAGE
     * @param <T> any object or simple type
     */
	public static<T> void print(T message) {
		print(defaultPrintUrgency,message); 
	}
	//}}}
	
	//{{{ void printf(int urgency,String message,Object... args)
    /**
     * printet when urgency  is equal or above TxtConvPlugin.printUrgency
     *
     * @param args values to be filled in formatted string mes
     * @param message formatted string
     * @param urgency equal or above means printing
     */
	public static void printf(int urgency,String message,Object... args) {
		if (urgency >= TxtConvPlugin.printUrgency && (urgency & 1) ==1 ) {
			con(
				((urgency <= Log.ERROR) ? ""+urgency+": " : "")
				+String.format(message,args)); 
		}
	}
	//}}}
	
	//{{{ printf(String message,Object... args)
	/**
     * printet when TxtConvPlugin.printUrgency is equal or belov Log.MESSAGE
     *
     * @param args values to be filled in formatted string mes
     * @param message formatted string
     */
	public static void printf(String message,Object... args) {
		printf(defaultPrintUrgency,message,args); 
	}
	//}}}
	
	//{{{ void printTimed(int urgency,T message)
	/**
     * printet, timed prefixed, when urgency is equal or above TxtConvPlugin.printUrgency
     *
     * @param message to be timed prefixed printet
     * @param urgency equal or above means printing
     * @param <T> any object or simple type
     */
	public static<T> void printTimed(int urgency,T message) {
		if (urgency >= TxtConvPlugin.printUrgency && (urgency & 1) ==1 )
			con(((urgency <= Log.ERROR) ? ""+urgency+": " : "")+hms()+" "+message);	
	}
	//}}}
	
	//{{{ void printTimed(T message) 
	/**
	 * printet, timed prefixed, when TxtConvPlugin.printUrgency is equal of below Log.MESSAGE
     * @param message to be printed time prefixed 
     * @param <T> any object or simple type
     */ 
	public static<T> void printTimed(T message) {
		printTimed(defaultPrintUrgency,message); 
	} 
	//}}}
	
	//{{{ void printfTimed(int urgency,String message,Object... args)
	/**
     * printet, timed prefixed, when urgency is equal or above TxtConvPlugin.printUrgency
     *
     * @param args values to be filled in format string mes
     * @param message formatted string
     * @param urgency equal or above means printing
     */ 
		public static void printfTimed(int urgency,String message,Object... args) {
		if (urgency >= TxtConvPlugin.printUrgency && (urgency & 1) ==1 ) {
			con(
				((urgency <= Log.ERROR) ? ""+urgency+": " : "")
				+hms()+" "+String.format(message,args)); 
		}
	} 
	//}}}

	//{{{ void printfTimed(String message,Object... args)
	/**
     * printet, timed prefixed, when TxtConvPlugin.printUrgency is equal or below Log.MESSAGE
     *
     * @param args values to be filled in format string mes
     * @param message formatted string
     */
	public static void printfTimed(String message,Object... args) {
		printfTimed(defaultPrintUrgency,message,args); 
	}
	//}}}
	
	//{{{ void BeanShellErrorDialog(Throwable throwable)
    /**
     * Shows throwables in org.gjt.sp.jedit.gui.BeanShellErrorDialog
     *
     * @param throwable to be showned in org.gjt.sp.jedit.gui.BeanShellErrorDialog
     */
	public static void BeanShellErrorDialog(Throwable throwable) {
		new org.gjt.sp.jedit.gui.BeanShellErrorDialog(jEdit.getActiveView(),throwable);
	}
	//}}}
	
	//{{{ String ux(String backslashed)
	/**
     * Replaces all backslashes in string with forward slash. Needed for simplicity in multiplatform stringmanipulation
     *
     * @param backslashed to be converted to forward slashed
     * @return String java style multiplatform filename convention
     */
	public static String ux(String backslashed) {
		return backslashed.replaceAll("\\\\","/");
	}
	//}}} }}}
	
	//{{{ private methods
	
	//{{{ <T> void con(T thing)
    private static<T> void con(T thing) {
		while (!(getDockableWindowMan().isDockableWindowVisible("console") && console().getShell().getName().equals("BeanShell"))) 
			new console.Shell.SwitchAction("BeanShell").invoke(jEdit.getActiveView());
		console().getOutput().print(Color.black,""+thing);
	}
	//}}}
	
	//{{{ DockableWindowManager getDockableWindowMan()
	private static DockableWindowManager getDockableWindowMan() {
		if (null==dockableWindowMan)
			dockableWindowMan=jEdit.getActiveView().getDockableWindowManager();
		return dockableWindowMan;
	}
	//}}}
	
	//{{{ Console console()
	private static Console console() {
		final String plName="console"; 
		Console dAW = (Console)(getDockableWindowMan().getDockableWindow(plName));
		if (null == dAW) {
			getDockableWindowMan().addDockableWindow(plName);
			dAW = (Console) (getDockableWindowMan().getDockableWindow(plName));
		}
		return dAW;
	}
	//}}}
	
	//{{{ String hms()
	static private String hms() {
		return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss,SSS"));
	}
	//}}}
	
	//{{{ Constructor disability
	private Util() {} 
	//}}} }}}
	
	//{{{ private data
	private static DockableWindowManager dockableWindowMan;
 	private static final int defaultPrintUrgency=Log.MESSAGE;
	//}}}

}
