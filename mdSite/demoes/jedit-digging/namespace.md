<!-- head=siteJedit.css.html+adjustmargin.css+jquery.html+prettify.html+prettyClassify.js+sitePages()+eventsAndNav.js & body=pageJEdit.md & title=Magic+Ownnamespace -->
### Magic ownnamespace of BeanShell.runScript(...)


	String simpleName(String className) {
		return className.matches(".+?\\[Ljava.+") 
			? "String[]" 
			:className.replaceAll("class ","").replaceAll("(\\w+\\.)+","");
	}
	TreeMap methods = new TreeMap();
	for (org.gjt.sp.jedit.bsh.BshMethod m:BeanShell.getNameSpace().getMethods()) {
		String parms="";
		if (m.getParameterTypes().length >0) {
			String [] parmA = new String[m.getParameterTypes().length];
			
			int i=0;
			for (Class claz: m.getParameterTypes())  {
				parmA[i++]= (claz==null? "uknown" : claz.getSimpleName());
			}
		
			i=0;
			for (s: m.getParameterNames())
				if (null != s && i < parmA.length) {
					parmA[i]=parmA[i] +" "+s;
					i++;
				}
			parms=String.join(",",parmA);
		}
		if (null != m.getReturnType())
			methods.put(simpleName(""+m.getReturnType())+" "+m.getName(),"("+parms+")");
	}
	fLines.append("\n---\n");
	for (String method : methods.keySet()) 
		fLines.append("\t").append(method).append(methods.get(method)).append("\n");
	


---
	JComponent consoleDW()
	String atCarretOrSelected(TextArea textArea,String match)
	String dateFormat(long unixtime,String format)
	String elSubst(String input,Properties properties)
	String getClipboard()
	String implCode(String implFunc,String iFaceMethod,String lambText)
	String linksReferences()
	String linksReferencesAndLinks(boolean repeatRefsAsLinks,boolean includeHashLinks,int maxDepth)
	String linksReferencesFollowedByLinks()
	String linksreferencesTitlesFileName()
	String mEval(String exp)
	String mdBaseDir()
	String mdBaseUrl()
	String password(int length)
	String sDir()
	String sitePages()
	String tabularLines(List stringArraysList,int space,String delim)
	String unicodeEscaped(String nonEscaped)
	String ux(String possibleBackslashed)
	StringBuilder pageheading(String scriptFileName,String begPatt,String endPatt,String topLines,String leadAppend)
	String[] toArray(Object toArrayAble)
	TreeMap sourcedFuntions()
	XThis Beam(File dir,XThis fileVisitor,int maxDepth)
	XThis FileVisitor(uknown arg1,uknown arg2,uknown arg3,uknown lambText)
	XThis Mapper(uknown lambText)
	XThis NumSeq(List menues,String headLine)
	XThis Predicate(uknown arg1,uknown arg2,uknown lambText)
	XThis Runnable(uknown arg1,uknown lambText)
	XThis UniProp(String fileName,String startPattern,String endPattern)
	XThis spacer()
	boolean foundStartupFunction(String funcName,View view)
	boolean isNewerOrSolitary(File file,File compareTo)
	boolean saveAllStartupFiles(View view)
	int numLeft2Caret(View view,boolean delete,int defaultOnNoNumber)
	interface List asList(String i1)
	interface List backWordsOrSelected(View view,boolean reverse,String pattern)
	interface List dsvLines(String fileName,String table)
	interface List externCmdOutput(String[] cmds)
	interface List fileLines(String fileName,String blockId)
	interface List hasSuffixInOrBelow(String dir,String suffix)
	interface List htmlTable(String url,int tableElementsIndex,int maxRows,String[] selFields)
	interface List jEditSWP(String startsWith)
	interface List jimport(String className)
	interface List readAllLines(String fileN)
	interface List startsWithPropertyKeys(String startsWith,Properties prop)
	interface List startupBeanshellFilesLines()
	interface List systemSWP(String startsWith)
	interface List textAreaBlocksRanges(TextArea textArea,String beforeLinesPattern,String afterLinesPattern)
	void actionsDialog(XThis callBack,XThis menuSeq,View view)
	void antTask(String target,View view,Buffer buffer)
	void appendToFile(String fileName,List lines)
	void cls(Object thing)
	void p(Object thing,Object spacer)
	void pa(Object egg)
	void println(Object thing)
	void pt(Object thing)
	void runNode(View view,Buffer buffer)
	void toFile(String fileName,Object thing,Object function)
	void walkFileTree(File dir,XThis visitor)

