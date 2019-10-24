/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */
package home.txtconv;
//{{{ imports
import org.gjt.sp.jedit.OperatingSystem;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import static home.txtconv.util.Util.*;
//}}}

/**
 * Reads all lines, in possible that chain of files, inclusion can lead to.
 */
public class FilesLinesReader {

	//{{{ public methods
	
	//{{{ List<String> filesLinesList(String fileName,String beforeFirst,String afterLast)
	/**
	 * Return file lines of one or more files. Called by HtmlText for each file being added to head or body element.
	 * <p>
	 * Through recursive call to void addLines(...), filesLinesList(...) return Lines. It is &lt;!--fileName--&gt; in the single file that
	 * makes the chain.
	 * File names can be EL expressed like eg."prefix/${someproperty}/postfix.suffix "
	 *
	 * @param fileName which has first line
	 * @param beforeFirst possible string before &lt;-- ... --&gt; at the line that causes call of this method.
	 * @param afterLast possible string after &lt;!-- ... --&gt; at the line that causes call of this method.
	 * @return List 
	 */
	public  List<String> filesLinesList(String fileName,String beforeFirst,String afterLast) {
		print(0,"FilesLinesReader.fileLinesList(...)");
		lines = new ArrayList<>();
		addLines(fileName,beforeFirst,afterLast);
		return lines;
	}
    //}}}
    
    //{{{ FilesLinesReader(Properties properties)
    /**
     * Constructer takes a properties parameter with a property "watchedKey", and other values to be used as filename(parts) lookup   
     *
     * @param properties used for evaluating EL expressed file names
     * @param htmlText is a reference to the html converter instance that has a FilesLinesReader as data 
     */
    public FilesLinesReader(Properties properties,HtmlText htmlText) {
    	wprop= new FilesLinesReader.WatchedProperites(properties);
    	this.htmlText=htmlText;
    }
    //}}} }}}
    
    //{{{ public data
    /**
     * The properties used for filename lookup. It is public to expose wprop.dirty indicating that the markdown source
     * file of an invocation of HtmlText have been used.
     */
    public WatchedProperites wprop;
    //}}}

    //{{{ private methods
    
    //{{{ addLines(String fileName,String beforeFirst,String afterLast)
    private void addLines(String fileName,String beforeFirst,String afterLast)  {
    	
    	for (String[] kv : new String[][]{{".js","script>"},{".css","style>"}})
			if (fileName.endsWith(kv[0])) {
				beforeFirst +="<"+kv[1]+"\n";
				afterLast = "</"+kv[1]+afterLast;
			}
		Adder adder = new FilesLinesReader.Adder(beforeFirst);
		try (MultiSourceReader  in = new MultiSourceReader(fileName,wprop.wasJustSeen(),htmlText)) {
    		String line;
    		while((line=in.readLine()) != null) {
    			print(0,"addLines:line="+line);
				Matcher m = Pattern.compile(".*(<!--include([^-]+)-->).*").matcher(line);
				if (m.find() && m.groupCount() == 2) {
					printf(0,"group(1)=%s,group(2)=%s",m.group(1),m.group(2));
					String iFileName = absPath(fileName,sUtils.elSubst(m.group(2).trim(),wprop));
					print(0,"addlines.iFileName="+iFileName);
					adder.addLast("");
					addLines(iFileName,line.substring(0,m.start(1)),line.substring(m.end(1)));
				}
				else
					adder.add(line);
			}
    		adder.addLast(afterLast);
    		print(0,"afterLast="+afterLast);
    		} catch(FileNotFoundException e) {
    			lines.add(beforeFirst+"<!-- file " + fileName + " not found -->"+afterLast);
			} catch(Exception e) { BeanShellErrorDialog(e); } 
    }
    //}}}
    
    //{{{ String absPath(String current,String incRef)
   	private String absPath(String current,String incRef) { 
		File f=new File(new File(current).getParent());
		
		while (incRef.indexOf("./") == 0)
			incRef = incRef.substring(2);
		while (incRef.indexOf("../") == 0) {
			f=new File(f.getParent());
			incRef = incRef.substring(3);
		}
		if (OperatingSystem.isWindows())
			return 
				(incRef.matches("(\\w:)?/.*")
					? ""
					: ux(f.getPath())+"/" )+incRef;
		return (incRef.startsWith("/") ? "" : ux(f.getPath())+"/" ) + incRef;
	}
    //}}} }}}
   	
    //{{{ private data
    private List<String> lines;
    private HtmlText htmlText;
    //}}}
    
    //{{{ class Adder
	private class Adder { 
		String[] line = {"",""};
		boolean isFirst=true;
		int lIdx=0;
		String lBeforeFirst;

		public Adder(String beforeF) {
			lBeforeFirst = beforeF;
		}
		public void add(String l) {
			if (isFirst) {
				line[lIdx] = lBeforeFirst+l;
				isFirst=false;
			} else {
				lines.add(line[lIdx]);
				lIdx = (lIdx+1) & 1;
				line[lIdx]=l;
			}
		}
		public void addLast(String last) {
			lines.add(line[lIdx]+last);
			isFirst=true;
		}
		
	}
    //}}}
    
    //{{{ class sUtils
    /**
     * sUtils that is seperated from FilesLinesReader because of it generality
     */
	static private class sUtils {
		
		//{{{
		/**
         * lookup mechanism using properties. EL for the 'JSP Expression Language' - function elSubst replaces ${foo} in "some ${foo} thing" 
         * with getProperty(foo). It's a string substitution princip also used in Apache ant build files.
		 * <p>
         * This simple implementation below translates ${}whatever} to ${whatever} without dereferencing to any property value. 
         *
         * @param properties used of substitution
         * @param input String for being possible substituted
         * @return String possible substituted
         */
		static public String elSubst(String input,WatchedProperites properties) {
			print(0,"FilesLinesReader.sUtil.elSubst.par.input="+input);
			Pattern pat = Pattern.compile("\\$\\{([^}]+)\\}");
			Matcher m = pat.matcher(input);
			while (m.find()) {
				input = input.substring(0,m.start(1)-2)+ properties.getProperty(m.group(1),"${}"+m.group(1)+"}") +input.substring(m.end(1)+1);
				m =	pat.matcher(input);
			}
			print(0,"FilesLinesReader.sUtil.elSubst.par.return="+input.replaceAll("\\$\\{\\}([^}]+)\\}","\\${$1}"));
			
			return input.replaceAll("\\$\\{\\}([^}]+)\\}","\\${$1}");
		}
		//}}}
	}	
    //}}}
    
    //{{{ class WatchedProperites
    /**
     * Extends Properties with watching when a certain key is referenced.
     */
	static public class WatchedProperites extends Properties {
		
		//{{{  WatchedProperites(Properties properties)
        /**
         * Constructs WatchedProperites as an empty property with the specified defaults. 
         * It works as if it has been constructed with setting properties.
         * @param properties that is the default fallback
         */
		public WatchedProperites(Properties properties) {
			super(properties); //default ok
			watchedKey = getProperty("watchedKey");
		}
		//}}}
		
		//{{{ boolean dirty
        /**
         * Is true when key "watchedKey" have been referenced. 
         */
		public boolean dirty=false;
		//}}}
		
		//{{{ boolean wasJustSeen()
        /**
         * Returns true, at first call, after watched keys has been refered.
         * Used to decide for file convertion
         *
         * @return boolean true at first call after watched key refered.
         */
		public boolean wasJustSeen() {
			boolean an = justSeen;
			justSeen=false;
			return an;
		}
		//}}}
		
		//{{{ String getProperty(String key)
        /**
         * Wrapper around super.getProperty(...). Ensures that watchedKey gets set.
         *
         * @param key property
         * @return String value
         */
		public String getProperty(String key) {
			String r = super.getProperty(key);
			if (key.equals(watchedKey)) {
				justSeen=true;
				dirty=true;
			}
			return r;
		}
		//}}}

		//{{{ private data
		private static final long serialVersionUID = 64122556928921789L;
		private String watchedKey;
		private boolean justSeen=false;
		//}}}

	}
	//}}}

}
