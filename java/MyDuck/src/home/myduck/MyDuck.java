/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */
package home.myduck;
//{{{ imports
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import org.gjt.sp.jedit.gui.DefaultFocusComponent;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DockableWindowManager;

//}}}


/**
 * A JTextArea in a JPanel for use as a jEdit Dockable window. Its use is to print messages and exceptions info under development of
 * java code and beanshell code - the later in macroes and startup defined functions.
 * <p>
 * An instance of this class is fetched by jEdit DockableWindowsManager provided by following in file action.xml 
 * <blockquote><pre>
 *     &lt;?xml version="1.0"?&gt;
 *     &lt;!DOCTYPE DOCKABLES SYSTEM "dockables.dtd"&gt;
 *     &lt;DOCKABLES&gt;
 *     	&lt;DOCKABLE NAME="myduck" MOVABLE="TRUE" &gt;
 *     		new home.myduck.MyDuck(view, position);
 *     	&lt;/DOCKABLE&gt;
 *     &lt;/DOCKABLES&gt;
 * </pre></blockquote>                                                        
 * <p>
 * When jEdit is running a predefined variable wm can be used to retrieve a reference to an open instance of myduck.
 * <blockquote><pre>
 *     wm.addDockableWindow("myduck");
 *     DockableWindow daW = wm.getDockableWindow("myduck");
 * </pre></blockquote>
 * First time, the window is opened, futher calls just returns a reference. This reference can be used to call public methods of class MyDuck.
 * <p>
 * A utility function returning a reference, defined at startup, must get its own DockableWindowsManager reference. The global variable, dAWM, keeps
 * its value after being initialized when dockableW() is used first time. Following is in a .bsh file in startup directory under jEdit settingsdirectory
 * <blockquote><pre>
 *     DockableWindowManager dAWM;
 *     JComponent dockableW() {
 *     	final String plName="myduck";  
 *     	if (null==super.dAWM)
 *     		super.dAWM = jEdit.getActiveView().getDockableWindowManager();
 *     	JComponent dAW = super.dAWM.getDockableWindow(plName);
 *     	if (null == dAW) {
 *     		super.dAWM.addDockableWindow(plName);
 *     		dAW = super.dAWM.getDockableWindow(plName);
 *     	}
 *     	return dAW;
 *     } 
 * </pre></blockquote>
 * <p>                    
 * dockableW() is used in following beanshell functions defined in startup directory of jEdit settings directory. 
 * <blockquote><pre>
	void p(thing) { // print line
		dockableW().appendLine(""+thing);
	}

	void cls() { // clears the screen
		dockableW().clear(); 
	} 
	
	void cls(String heading) { // clears the screen printing a heading
		dockableW().clear();
		p(heading);
	} 
	
 * </pre></blockquote>
 *  
 */
public class MyDuck extends JPanel                                              
    implements DefaultFocusComponent {
    	
	//{{{ public MyDuck(View viev,String position)
    /**
     * Gets called from jEdit dockablewindows mechanisms. Must not be new'ed otherwise.
     * The arguments is  mandatory as indicated in DockableWindowManagerImpl.java of jEdit source code.     
     *
     * @param viev not used in this simple dockable
     * @param position ensures position persistence
     */
    public MyDuck(View viev,String position) {
		super(new BorderLayout());
		floating = position.equals(DockableWindowManager.FLOATING);
		if (floating) this.setPreferredSize(new Dimension(500, 250));
		textArea = new JTextArea();                                          
		JScrollPane pane = new JScrollPane(textArea);
		add(BorderLayout.CENTER, pane);
	}
	//}}}
	
	//{{{ public void focusOnDefaultComponent()
    /**
     * Shold not be called - has to be public for integration purposes
     */
	public void focusOnDefaultComponent() {                       
		textArea.requestFocus();
	}
	//}}}
	
	//{{{ public void addNotify()
	 /**
     * Shold not be called - has to be public for integration purposes
     */
	public void addNotify() {
		super.addNotify();
	}
	//}}}
	
	//{{{ public void removeNotify()
	/**
     * Shold not be called - has to be public for integration purposes
     */
	public void removeNotify() {
		super.removeNotify();
	}
	//}}}
	
	//{{{ public void appendLine(String line)
    /**
     * Append line to  textArea and move the caret position to end of  textarea 
     *
     * @param line the line t be appended
     */
	public void appendLine(String line) {
		textArea.append(line+"\n");
		try {
			textArea.setCaretPosition(textArea.getLineEndOffset(textArea.getLineCount()-1));
		} catch(javax.swing.text.BadLocationException e) {
			textArea.append("Appendline-> textArea.setCaretPosition throwed BadLocationException");
		}
	}
	//}}}
	
	//{{{ public void clear()
    /**
     * Clears the textarea
     *
     */
	public void clear() { textArea.setText(""); }
	//}}}
	
	//{{{
	private static final long serialVersionUID = 64122556928921789L;
    private boolean floating;
    private  JTextArea textArea;
	//}}}
	
}
