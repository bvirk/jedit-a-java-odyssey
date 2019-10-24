/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */ 
package home.txtconv;

import org.gjt.sp.jedit.jEdit;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import org.gjt.sp.jedit.AbstractOptionPane;

public class TxtConvHtmlOptionPane extends AbstractOptionPane {
	private JCheckBox iconifyOnSavedOutput;
	private JCheckBox refreshOnFileResidesInBaseDir;
	private JTextField baseDir;
	private JTextField baseUrl;
	private JTextField leadFunction;

	public TxtConvHtmlOptionPane() {
		super("txtconv.html");
	}

	public void _init() {
		addComponent(iconifyOnSavedOutput = new JCheckBox("Iconify on saved output", options.txtconvBoolean("iconifyOnSavedOutput")));
		addComponent(refreshOnFileResidesInBaseDir = new JCheckBox("Refresh on file resides in baseDir", options.txtconvBoolean("refreshOnFileResidesInBaseDir")));
		addComponent("baseDir:",baseDir = new JTextField(options.txtconv("basedir")));
		addComponent("baseUrl:",baseUrl = new JTextField(options.txtconv("baseurl")));
		addComponent("markdown lead function:",leadFunction = new JTextField(options.txtconv("mdleadfunction")));
	}

	public void _save() {
		options.txtconv("basedir",baseDir.getText().replaceAll("/*$","").replaceAll("^~",System.getProperty("user.home")));
		options.txtconv("baseurl",baseUrl.getText().replaceAll("/*$","")+"/");
		options.txtconv("mdleadfunction",leadFunction.getText().replaceAll("\\(|\\)","")+"()");
		((TxtConvPlugin)jEdit.getPlugin("home.txtconv.TxtConvPlugin",true)).setIconifyOnSavedOutput(iconifyOnSavedOutput.isSelected());
		options.txtconvBoolean("refreshOnFileResidesInBaseDir",refreshOnFileResidesInBaseDir.isSelected());
	}
}