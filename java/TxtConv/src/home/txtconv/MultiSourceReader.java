/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */ 
package home.txtconv;
//{{{ imports
import org.gjt.sp.jedit.bsh.NameSpace;
import org.gjt.sp.jedit.BeanShell;
import org.gjt.sp.jedit.jEdit;
import java.util.Arrays;
import java.io.IOException;
import java.lang.StringBuilder;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.AutoCloseable;
import java.io.BufferedReader;
import com.github.rjeschke.txtmark.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static home.txtconv.util.Util.*;
//}}}

/**
 * MultiSourceReader is a sort of combining adapter that makes looping lines of files in broad sense easier - broad means that lines could be content of a file or
 * execution of a function being in that beanshells namspace jEdit holds. When all the lines of a specific file should either be 
 * read as they appaers in the file, or all be converted, using MultiSourceReader saves the loop from the complexity af seperate statements blocks.
 * Because it don't matters in its use in txtConvPlugin, the code simplification of just returning all lines on conversion, is the chosen implementation.
 */
public class MultiSourceReader implements AutoCloseable {

	//{{{ MultiSourceReader(String fileName, boolean doConvert, HtmlText htmlText))
    /**
     * Is constructed as either a normal InputStreamReader or as a Reader which input stream is converted.  
     *
     * @param fileName of html or markdown snippet
     * @param doConvert decides of the file content shall be markdown convertet
     * @param htmlText is a reference to the html converter instance that FilesLinesReader passes
     * @throws FileNotFoundException if file don't exists
     * @throws UnsupportedEncodingException if charset decoding is not supoted
     */
    public MultiSourceReader(String fileName, boolean doConvert,HtmlText htmlText) throws FileNotFoundException,UnsupportedEncodingException {
    	this.htmlText=htmlText;
    	this.doConvert=doConvert;
    	if (fileName.matches(".+\\(\\)"))
    		bshFunction = fileName.replaceAll("^.*?(\\w+\\(\\))$","$1");
    	else {
    		bufferedReader= new BufferedReader(new InputStreamReader(new FileInputStream(fileName) ,"utf-8"));
    		if (fileName.startsWith(options.txtconv("pluginhome"))) 
    			startsWithPluginHomeDir=true;
    		print("Reading from file: "+fileName+(startsWithPluginHomeDir ? "(pluginHome)": ""));
    	}
    	
    }
    //}}}
    
    //{{{ void close() 
    /**
     * Implementation of autocloseable
     *
     * @throws Exception on closing BufferedReader
     */
    public void close() throws Exception {
    	if (null != bufferedReader)
    		bufferedReader.close();
    }
    //}}}
    
    //{{{ String readLine()
    /**
     * Mimics BufferedReader.readline . When input is converted, not single lines, but chunk of all lines is returned.
     * When filename ends with '()' it is not a file, but a sourced beanshell function
     *
     * @return String line or all lines if inputstream is converted or is beabshell output.
     * @throws IOException If an I/O error occurs
     * @throws Exception BeanShell._eval: instances are thrown when various BeanShell errors occur
     */
    public String readLine() throws IOException,Exception {
    	if (!doConvert && (bshFunction == null)) {
    		String line = bufferedReader.readLine();
    		return hasUrl(line) ? urlReRelatived(line) : line;
    	}
    	if (sourced)
    		return null;
    	sourced=true;
    	if (null != bshFunction) {
    		NameSpace ns = BeanShell.getNameSpace();
    		if (!(Arrays.asList(ns.getAllNames()).contains(bshFunction.replaceAll("\\(\\)",""))))
    			return "<!-- beanshell "+bshFunction+" not found - perhaps not sourced! -->";
    		Object retval = BeanShell._eval(jEdit.getActiveView(),ns,bshFunction);
    		if (retval instanceof String)
    			return (String)retval;
    		else
    			return "<!-- beanshell "+bshFunction+" don't seem to return string -->";
    	}	
		String line;
		lines = new StringBuilder();
	   	while (null != (line=bufferedReader.readLine()))
   	   		lines.append(line+"\n");
   	   	printTimed(0,"MultisourceReader.readLine() htmlText.mdLead="+htmlText.mdLead());
   	   	return new ReqProcessor(htmlText).markedDownPossiblePrettyfied(htmlText.mdLead()+lines.toString());
   	}
   	private boolean hasUrl(String line) {
   		if (line == null || !startsWithPluginHomeDir || null == htmlText.upDirToBaseDir)
   			return false;
   		return line.matches(".*<"+tagsWithUrls+"[^>]+"+urlAttName+"=[\"']/[/\\w\\.]+[\"'].+");
   	}
   	private String urlReRelatived(String line) {
   		String attValRest = line;

		while (attValRest.matches(".*<"+tagsWithUrls+"[^>]+"+urlAttName+"=[\"']/[/\\w\\.]+[\"'].+")) {
			attValRest=attValRest.replaceAll("^.*?<"+tagsWithUrls+"[^>]+"+urlAttName+"=[\"']/","/");
			String attVal=attValRest.replaceAll("[\"'].+$","");
			line = line.substring(0,line.length()-attValRest.length())+htmlText.upDirToBaseDir+attVal+attValRest.substring(attVal.length());
		}
		return line;
	}
   	//}}}

   	private final String tagsWithUrls="(script|link|audio|embed|source|track|video)";
	private final String urlAttName="(src|href|poster)";
	
   	private StringBuilder lines;
   	private String bshFunction;
   	private boolean sourced=false;
	private boolean doConvert;
	private boolean startsWithPluginHomeDir;
	private BufferedReader bufferedReader;
	private HtmlText htmlText;
   	
}	
