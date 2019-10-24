/* :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:  */
package home.txtconv;
//{{{ imports
import java.util.Map;
import com.github.rjeschke.txtmark.Processor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static home.txtconv.util.Util.*;
//}}}

/**
 * The markdown string to string convertion class by use of com.github.rjeschke.txtmark.Processor, and, in case of prior regex manipulation to facilitate mixed html block   
 * inclusion, org.jsoup.Jsoup.*
 *
 */
public class ReqProcessor {
	
    //{{{public ReqProcessor(HtmlText htmlText )
    /**
     *
     *
     *
     * @param htmlText is a reference to the html converter instance that holds query items for seeing some between results in markdown conversions
     */
    public ReqProcessor(HtmlText htmlText ) {
		this.htmlText=htmlText;
	}
	//}}}
			
	//{{{ private methods and data
	
	//{{{ String markedDownPossPtf(String source)
	public String markedDownPossiblePrettyfied(String source) {
		final String blocktags=
		"address|article|aside|blockquote|canvas|dd|div|dl|dt|fieldset|figcaption|figure|footer|form|h1|h2|h3|h4|h5|h6|header|hr|li|main|nav|noscript|ol|p|pre|section|table|tfoot|ul|video|iframe";
		//
		// "(?<!\\n\\t{1,5})" is negative lookbehind
		// no match for blocktag replacement is found if it is preceded med lineshift followed with 1 to 5 tabs
		// 
		// "(( [^>]*)*)>"
		// if anytnig comes after a blocktag et must be a space followed by anything not being '>' char before final '>' 		
    	if (regexEncoded())
			return source.replaceAll("(?<!\\n\\t{1,5})<\\s*([/]{0,1})("+blocktags+")(( [^>]*)*)>","\n<!--<$1$2$3>-->");
		if (unDecoded())
			return Processor.process(source.replaceAll("(?<!\\n\\t{1,5})<\\s*([/]{0,1})("+blocktags+")(( [^>]*)*)>","\n<!--<$1$2$3>-->"));
		if (decoded())
			return Processor.process(source.replaceAll("(?<!\\n\\t{1,5})<\\s*([/]{0,1})("+blocktags+")(( [^>]*)*)>","\n<!--<$1$2$3>-->")).replaceAll("<!--(<[^>]+>)-->","$1");
		
		boolean blockTagsFound = source.matches("[\\s\\S]*<("+blocktags+")(( [^>]*)*)>[\\s\\S]*");
		print("blockTagsFound="+blockTagsFound);
		if (!blockTagsFound)
   	   		return Processor.process(source);
   	   	
    	Document doc = Jsoup.parseBodyFragment(
			 "<body>"
			+Processor.process(source.replaceAll("(?<!\\n\\t{1,5})<\\s*([/]{0,1})("+blocktags+")(( [^>]*)*)>","\n<!--<$1$2$3>-->")).replaceAll("<!--(<[^>]+>)-->","$1")
			+"</body>"
			);
		for (Element element : doc.select("p")) 
			if (!element.hasText() && element.childNodeSize()==0) {
				print(5,"jsoup removes element: "+element.nodeName());
				element.remove();
			}

		doc.outputSettings().prettyPrint(true).indentAmount(4);
		return doc.body().html();
		
    }
	//}}}
	
	//{{{ boolean regexEncoded()
	private boolean regexEncoded() {
		return htmlText.queryItems.containsKey("show")
			? htmlText.queryItems.get("show").equals("regexEncoded") 
				? true 
				: false
			:false; 
	}
	//}}}
	
	//{{{ boolean unDecoded()
	
	private boolean unDecoded() {
		return htmlText.queryItems.containsKey("show")
			? htmlText.queryItems.get("show").equals("unDecoded") 
				? true 
				: false
			:false;
	}
	//}}}
	
	//{{{ boolean decoded()
	private boolean decoded() {
		return htmlText.queryItems.containsKey("show")
			? htmlText.queryItems.get("show").equals("decoded") 
				? true 
				: false
			:false;
	}
	//}}}
	
	private HtmlText htmlText;
	//}}}
}
