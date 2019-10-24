/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */ 
package home.txtconv;
//{{{ imports
import java.util.Properties;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileOutputStream;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.Mode;
import static home.txtconv.util.Util.*;
//}}}
/**
 * Class which method String output() delivers html result as function of markdown content in jEdit current buffer.
 * <p>
 * The markdown is converted through the external com.github.rjeschke.txtmark.Processor. 
 * <p>
 * First line, if present as a html comment - is a parameter list. The format is that of a get request querystring, with the addition 
 * that some spaces, for clarity, is allowed. In it simplest form:
 * <blockquote><pre>
 * 	&lt;!-- title=my+sweet+homepage --&gt;
 * </pre></blockquote>
 * 'title=my+sweet+homepage' must not contain spaces. Tree fields: head, body and title forms the final html page. An example:
 * 	<blockquote><pre>
 * 	&lt;!-- head=siteJedit.css.html+jquery.js.html+prettify.js.html+prettyClassify.js+eventsAndNav.js &amp; // one line with following line
 * 	body=pageJEdit.md &amp; title=macros+|+startups --&gt;
 * </pre></blockquote>
 * The files, in that order they appears in head field, are placed as siblings of first child of html head element - which is the 
 * &lt;meta charset .. tag). Same goes for body snippets - they are placed as childs of body element.
 * At last title is attached as last child of head - absence of its presents in line 1 defaults to filename without suffix
 * <p>
 * Every file, except those with suffix css or js are inserted with their whole content an nothing else. To make it easy to remember, 
 * they have suffix html (every such snippet embed its content in html tag).
 * <p>Files referenced in line 1 are found in plugin directory of Jedit TxtConvPlugin - NOT the place the resulting html file is placed.
 * <p>The created html file is placed in same directory as the markdown file - same name, just with html suffix.
 * <p>
 * An example: the siteJedit.css.html
 * <blockquote><pre>
 * 	&lt;link href="css/site.css" rel="stylesheet" &gt;
 * </pre></blockquote>
 * The content of files with css or js suffix, is, as they would apear in every other web usage - therefore they content is being 
 * embedded in &lt;script&gt;&lt;/script&gt; and &lt;style&gt;&lt;/style&gt; on insertion. An example emphasizes,
 * in other words, absence of &lt;script&gt; in FILE : tooltip.js
 * <blockquote><pre>
 * $(function () {
 *  ...
 * });
 * </pre></blockquote>
 * The snippet that shall be markdown converted must have suffix '.md', any snippets with other suffixes is just inserted. The selection of 
 * being markdown or not is done when attaching as child of body element - futher inclusion of snippets in snippets don't test for suffix 
 * <p>
 * The markdown text can end in one of two places. If an included snippet contains:
 * <blockquote><pre>
 * 	&lt;div class="wrapper"&gt;
 * 	...
 * 	  &lt;article&gt;
 * 	&lt;!--include ${sourcefile} --&gt;
 * 	  &lt;/article&gt;
 * 	...
 * 	&lt;/div&gt;
 * </pre></blockquote>
 * the markdown content ends inside the &lt;article&gt; element. This is an effect of construction FilesLinesReader
 * with properties fetched from:
 * <blockquote><pre>
 * 	private String[][] propText() {
 * 		return new String[][] {
 *       ...
 *      ,{"sourcefile",srcFileName}
 *      ,{"watchedKey","sourcefile"}};
 * 	}
 * </pre></blockquote>
 * Prior attaching to the output buffer, all snippets have been build by FilesLinesReader and due to
 * monitoring of "watchedKey" which is sourcefile, FilesLinesReader can be asked if sourcefile - buffer.getPath() has been used 
 * <br>
 * If sourcefile not have been used the markdown end as first child of body element. In that way it is possible to make simplere
 * layouts with markdown convertion embedded in minimal html. 

 *
 */
public class HtmlText implements Converter {

	//{{{ public methods
	
	//{{{ String getMode()
    /**
     * Mode is here (opposed to editmode) characterized by file sufix.
     * @return String suffix of file
     */
	public String getMode() { return "html"; }
	//}}}
	
	//{{{ boolean isToFileAble()
    /**
     * Relevant enabling of useablity as a file
     *
     * @return boolean useablity as a file
     */
	public boolean isToFileAble() { return true; }
	//}}}
	
	//{{{ String output()
    /**
     * Html result as function of content in jEdit current buffer. This content can be markdown text or mix of html snippets and markdown text
     *
     * @return String html output
     */
	public String output() {
		boolean sourceFileIsUsed = attachedNodeSnippetsHasUsedMarkDown();
		printTimed(0,"inOutput, mdLead="+mdLead());
		return 
			(magic > 0 && !srcHasLines) ? 
				 "<!-- show=regexEncoded -->\n"
				+"<!-- show=unDecoded -->\n"
				+"<!-- show=decoded-->"
			:	 htmlSnippets[0]
				+attachedNodeSnippets[0]
				+htmlSnippets[1]
				+getTitle()
				+htmlSnippets[2]
				+(sourceFileIsUsed ? "" : new ReqProcessor(this).markedDownPossiblePrettyfied(mdLead()+mdSource.toString()))+"\n"
				+attachedNodeSnippets[1]
				+htmlSnippets[3];
	}
	//}}}
	
	//{{{ HtmlText(String sourceFilename)
    /**
     * Initialises Nodesnippet StringBuilder and filesLinesReader with properties.
     * It depends on constructer of TxtConvPlugin  having set pluginHome directory (on first run after installation, it's a jEdit property)
     * If pluginHome directory dosn't exists, it is created an filled with the unpacked content of home.txtconv.snippets.zip of TxtConvPlugin jar file.
     *
     * @param srcFileName that contains markdown text
     */
	HtmlText(String srcFileName) { 
		//HtmlText.instance=this;
		printTimed(0,"htmlText srcFileName="+srcFileName);
		Spinner.next(Spinner.ARROWS4);
		this.srcFileName = srcFileName;
        String baseDir = options.txtconv("basedir");
        upDirToBaseDir = srcFileName.startsWith(baseDir)
        	? srcFileName.substring(baseDir.length()).replaceAll("[^/]", "").replaceAll("/","../").replaceAll("/\\.\\./$","")
        	: null;
        line0=null;
		mdSource= new StringBuilder();
		try (MultiSourceReader r = new MultiSourceReader(srcFileName,false,this)) {
        	printTimed(0,"in try"+(r==null));
			String line;
			while (null != (line = r.readLine())) {
        		printTimed(0,"line="+line);
				mdSource.append(line).append("\n");
        		if (null == line0) 
        			line0=line;
        		else
        			srcHasLines=true;
        	}
        } catch( Exception e) { BeanShellErrorDialog(e);}
        						
		
		
        printTimed(0,"in TxtConverter.constructer");
        final int nodeSnippetsCount=3;
        attachedNodeSnippets = new StringBuilder[nodeSnippetsCount];
        for (int i=0; i < nodeSnippetsCount; i++)
        	attachedNodeSnippets[i] = new StringBuilder();
        
        Properties p = new Properties();
		for (String[] sa : propText())
			p.setProperty(sa[0],sa[1]);
		filesLinesReader = new FilesLinesReader(p,this);
		unzip("snippets.zip",options.txtconv("pluginhome"));
	}
	//}}} 
	
	//{{{ String mdLead()
	String mdLead() {
		int index = nodeSnippetsIndex("mdLead");
		printTimed(0,"index of nodeSnippetsIndex(mdLead)="+index);
		int size  = attachedNodeSnippets[nodeSnippetsIndex("mdLead")].length();
		printTimed(0,"size of attachedNodeSnippets["+index+"]="+size);
		String retval="";
		if (size >0) {
			retval=attachedNodeSnippets[nodeSnippetsIndex("mdLead")].append("\n").toString();
			printTimed(0,"mdLead returns: "+retval);
		} else 
			printTimed(0,"mdLead return empty string");
		return retval;
	} //}}} }}}

	//{{{ public data
    /**
     *
     *
     *
     */
	public String upDirToBaseDir;
    //}}}
	
	//{{{ privat methods
	
	//{{{ void unzip(String zipFile, String destDirectory)
	private void unzip(String zipFile, String destDirectory) {
		File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
			try (ZipInputStream zis = new ZipInputStream(this.getClass().getResourceAsStream(zipFile))) {
				ZipEntry entry;
				while (null != (entry = zis.getNextEntry())) {
					extractFile(zis, destDirectory+"/"+entry.getName());
					zis.closeEntry();
				}
			} catch(IOException e) { e.printStackTrace();}
		}
	} //}}}
		
	//{{{ void extractFile(ZipInputStream zipIn, String filePath)
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[512];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    // }}}
	
    //{{{ boolean attachedNodeSnippetsHasUsedMarkDown()
	private boolean attachedNodeSnippetsHasUsedMarkDown() {
    	setQueryWords();
    	for (String parent : new String[]{"mdLead","head","body"}) {
    		if( queryItems.containsKey(parent))  
				for (String word : queryItems.get(parent).split("\\+")) {
					List<String> fileLines = filesLinesReader.filesLinesList(   (word.matches(".+\\(\\)") ? "" : options.txtconv("pluginhome")+"/")+word,   "","");
					attachedNodeSnippets[nodeSnippetsIndex(parent)].append(
						((fileLines.size() > 0) 
							? String.join("\n",fileLines)+"\n" 
							: ""));
				}
		}
		return filesLinesReader.wprop.dirty;
    }
    //}}}
    
    //{{{ int nodeSnippetsIndex(String queryName)
    int nodeSnippetsIndex(String queryName) {
    	return queryName.equals("head")
    		? 0
    		: queryName.equals("body")
    			? 1
    			: 2;
    } //}}}
	
    //{{{ void setQueryWords() 
    private void setQueryWords() {
    	if (line0.matches("\\s*<!--.+-->\\s*"))
			for( String key : line0.replaceAll("\\s*<!--\\s*","").replaceAll("\\s*-->\\s*","")
				.replaceAll("\\s*","").replaceAll("\\s*=\\s*","=").replaceAll("\\t*\\s+\\t*","&").split("&")) { 
				String[] items= key.split("=");
				if (items.length==2)
					queryItems.put(items[0],items[1]);
				else
					magic++;
			}
		if(!queryItems.containsKey("mdLead") && upDirToBaseDir != null) {
			String mdLeadFunc = options.txtconv("mdleadfunction");
			if (null != mdLeadFunc) {
				printTimed(0,"setQueryWords found upDirToBaseDir="+upDirToBaseDir+", addede mdLead="+mdLeadFunc);
				queryItems.put("mdLead",mdLeadFunc);
			}
		}
	}
	//}}}
	
	//{{{ String getTitle() 
	private String getTitle() {
		return queryItems.containsKey("title") 
			? queryItems.get("title").replaceAll("\\+"," ") 
			: srcFileName.replaceAll("^.*/","").replaceAll("\\.[^\\.]+$","");
	}
	//}}} }}}

	//{{{ private data
    protected Map<String,String >queryItems = new Hashtable<>();
    private int magic=0;
    private String srcFileName;
	private StringBuilder mdSource;
	private String line0;
	private boolean srcHasLines=false;
	private StringBuilder[] attachedNodeSnippets;
	private FilesLinesReader filesLinesReader;
	
	private String[][] propText() {
		return new String[][] {
         {"pluginhome.dir",options.txtconv("pluginhome")}
		,{"sourcefile.dir",ux(srcFileName).replaceAll("/[^/]*$","")}
		,{"sourcefile",ux(srcFileName)}
		,{"watchedKey","sourcefile"}};
	}
	
	private final String htmlSnippets[] = {
		"<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"utf-8\">\n"
		,"<title>"
		,"</title>\n</head>\n<body>\n"
		,"</body>\n</html>"
	};
	//}}}

}
