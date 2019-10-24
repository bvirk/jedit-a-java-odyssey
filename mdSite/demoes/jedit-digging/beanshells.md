<!-- head=siteJedit.css.html+jquery.html+prettify.html+prettyClassify.js+sitePages()+eventsAndNav.js & body=pageJEdit.md & title=macros+|+startups -->
###&nbsp;&nbsp;&nbsp;&nbsp;Macroes and startups


		anId(String funcDesc) {
			String[] fParts = funcDesc.split("\\s");
			return fParts.length >= 3
				? "id='"+fParts[2].replaceAll("\\(.+$","")+"'"
				: "";
		}
		listFiles(String path,String crawled) {
			for (File f : new File(path).listFiles()) 
				if (f.isDirectory())
					listFiles(path+"/"+f.getName(),crawled+f.getName()+"/");
				else 
					if (!f.getName().endsWith("#")) {
						String pathN = f.getPath();
						String macroType=pathN.substring(sDirLen,pathN.length()-f.getName().length()-1);
						lines
							.append("<"+"p class='macro'><a class='pop' href'#' id='"+ f.getName().replaceAll("\\.bsh$","") +"'>&nbsp;&#9656;&nbsp;</a>&nbsp;")
							.append(crawled+f.getName()+" <span class='macroType'>("+macroType+")</span><"+"/p>\n")
							.append("<d"+"iv class=\"desc\">\n"); 
						for  (line: toArray(readAllLines(f.getPath()))) { 
							if (line.trim().startsWith("//{{{"))
								lines
									.append("<"+"p class='function'><a class='pop' href='#' "+anId(line.trim()) +">&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;"+line)
									.append("<d"+"iv class=\"subdesc\">\n");
							else
								lines.append("\t\t"+line+"\n");
							if (line.trim().endsWith("//}}}"))
								lines.append("</d"+"iv>\n");
						}
						lines.append("</d"+"iv>\n");
						lines.append("\n\n");
					} 
		}
		int sDirLen;
		void run() {
			sDirLen= sDir().length();
			
			for (f: new String[]{"/macros","/startup"})
				listFiles(sDir()+f,"");
			String outFile = scriptPath.replaceAll("\\.\\w+$",".md");
			toFile(outFile,lines.toString());
			  
		}

<p class='macro'><a class='pop' href'#' id='cdCurrent'>&nbsp;&#9656;&nbsp;</a>&nbsp;cdCurrent.bsh <span class='macroType'>(/macros)</span></p>
<div class="desc">
		// Action bar tip: cd
		/**
		 * Change Console's System current directory to that of current buffer.
		 * Needed for antTask getting the right build.xml, and convenient for some working in plugin console with 
		 * command line tools
		 *
		 * @library ux()
		 */
		runCommandInConsole(view,"System","cd "+ux(buffer.getPath()).replaceAll("[^/]+$",""));
		view.goToBuffer(buffer);
</div>


<p class='macro'><a class='pop' href'#' id='openScratchpad'>&nbsp;&#9656;&nbsp;</a>&nbsp;openScratchpad.bsh <span class='macroType'>(/macros)</span></p>
<div class="desc">
		// da-key tip: A+s
		/**
		 *Opens scratchpad macro for editing
		 */
		jEdit.openFile(view,jEdit.getSettingsDirectory()+"/macros/scratchpad.bsh");
</div>


<p class='macro'><a class='pop' href'#' id='makeMdLink'>&nbsp;&#9656;&nbsp;</a>&nbsp;Text/makeMdLink.bsh <span class='macroType'>(/macros/Text)</span></p>
<div class="desc">
		//Actionbar tip: emd (extra md, not: electric motoric drive)
		/**
		 * Make a link destination and insert a link to it. Links can be inserted inline in
		 * text. The format is, as title becomes: ´some text in title´
		 *
		 * 		[link text]:url some text in title
		 * 
		 * If caret is after 't' in text on following line
		 *
		 * 		before [link text]:url some text in title
		 *
		 * The markdown becomes
		 * 
		 *		before [link text][url] some text in title
		 *
		 * And the html becomes
		 *
		 * 		before <a href="´baseUrl´/´folder´/url.html" title="words in title">link text</a> after
		 *
		 * where baseurl is return value of mdbaseUrl() and folder is the folder where the
		 * md file in which link is inserted is placed. 
		 * 
		 * file with name ´url´.md is created and opened as current buffer. The file can
		 * be saved to another folder within mdBaseDir - any markdown (re)compilation is
		 * done on a remade link reference definition list.
		 * 
		 * If the clipboard contained a unicode entity during making of a new link, it
		 * affects the title, eg. &#x1F301; in clipboard would make a title="1F301;words in title"
		 * 
		 * The md file must include "jquery.html+pictoLinks.css+pictoLinks.js" in the
		 * querystring item named head to make the unicoded pictographs links appear as intended.  
		 *
		 * The titles are save in file linksreferencesTitlesFileName()
		 */
		void makeMdLinkDest() {
			String[] args =toArray( backWordsOrSelected(view,true,".*\\[[^]]+\\]:\\w+.+"));
			if (args[1].length() >0) {
				while (textArea.getSelectedText() == null || !textArea.getSelectedText().startsWith(":"))
					textArea.goToPrevCharacter(true);
				textArea.backspace();
				//int caretPos=textArea.getCaretPosition();
				int cItem=1;
				int cIndex;
				String title="";
				while ((cIndex = args[cItem].indexOf(":")) == -1) 
					cItem++;
				String url=args[cItem].substring(cIndex+1);
				textArea.replaceSelection("["+args[cItem].substring(cIndex+1)+"]");
				//removeFromSelection(caretPos);
				if (cItem > 1) {
					String ucode=getClipboard().trim();
					title=ucode.matches("&#x[0-9A-Fa-f]{5};") ? ucode.substring(3,9) : "";
					while (--cItem > 0)
						title += args[cItem]+" ";
					title = title.trim();
					appendToFile(linksreferencesTitlesFileName(),asList(url+"="+title));
				}
				buffer.save(view,null);
				jEdit.openFile(view,buffer.getPath().replaceAll("/[^/]+$","/")+url+".md");
				
			} else
				view.getStatus().setMessage("no '[link text]:url (title )*' pattern left to caret");
		}
		makeMdLinkDest();
</div>


<p class='macro'><a class='pop' href'#' id='formatDSV'>&nbsp;&#9656;&nbsp;</a>&nbsp;Text/formatDSV.bsh <span class='macroType'>(/macros/Text)</span></p>
<div class="desc">
		// Action bar tip: dsv+TAB
		/**
		 * Format current buffer - the column space is leftcaretParm.
		 *
		 * This macro makes it easy to deal with dsv data in a way where the overview has the properties of
		 * presentation in datasheet view or other clearly columned view
		 * By presenting dsv data in a column ligned up way, pure textual editing can combined with overview.
		 *
		 * When editing such presented dsv data, it could be a mess to keeping up columns manually. With this macro,
		 * the only thing to remember is keeping a constant number of separators. In other words, don't care about  
		 * changing lignupness rigth to editing. Final or during editing this macro is invoked to lign up columns.
		 *
		 * This facilty is meant to deal with blocks of dsv data as tables, a sort of pure text database in a single file.
		 */
		void formatDSV() {
			dsvFormatBlocks(numLeft2Caret(textArea,true,1));
		}
		
		/** 
		 * Formats delimiter seperated values block to lign up columns. All blocks in current buffer is column ligned up
		 * formatted with the sammme columnspace - the parameter to dsvFormat, but coulumns width is individual to each block. 
		 * 
		 * A block is surrounded between following start and end pattern, hvor ',' and 'foo' is any repspective single
		 * char and string
		 *
		 * 		/***,sv,foo
		 * 		blockline1
		 *		blockline2
		 *		...
		 *		blockline n
		 *		***/
		 /**
		 * Each blockline has delimiter seperated values - the text quantifier double quotes and infields  
		 * double quotes escaping as double quotes double quotes is allowed but values can not contain lineshifts
		 *
		 * @param columnSpace the space between columns
		 * @library textAreaBlocksRanges
		 * @library Beam
		 */
		void dsvFormatBlocks(int columnSpace) {
			for (List block : textAreaBlocksRanges(textArea, "/\\*\\*\\*.sv.+", "\\*\\*\\*/")) {
				String delim = textArea.getLineText(block.get(0)-1).replaceAll("^/\\*\\*\\*(.)sv.*","$1");
				int[] maxLengths = new int[textArea.getLineText(block.get(0)).split(delim).length()];
				
				List eBlock = Beam(block)
					.map(delim,textArea,maxLengths,"item->{ "+
						"String[] items= arg2.getLineText(item).split(arg1+´(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)´);"+
						"for (int i=0; i < items.length; i++) {"+
							"items[i]=items[i].trim();"+
							"if (items[i].length() > arg3[i])"+
								"arg3[i] = items[i].length();"+
						"}"+
						"return items;}")
					.toList();
				
				int getIx=0;
				for (int taLine : block) {
					String[] items=eBlock.get(getIx++);
					String joined="";
					for (int i=0; i<items.length; i++) 
						joined += (i>0 ? delim : "")+items[i]+ ( i < items.length-1 
							? new String(new char[columnSpace+maxLengths[i]-items[i].length()]).replace('\0',' ')
							: "");
					textArea.setCaretPosition(textArea.getLineStartOffset(taLine));
					textArea.selectLine();
					textArea.setSelectedText(joined);
				}
			}
		}
		formatDSV();
</div>


<p class='macro'><a class='pop' href'#' id='openBrowsedSource'>&nbsp;&#9656;&nbsp;</a>&nbsp;Text/openBrowsedSource.bsh <span class='macroType'>(/macros/Text)</span></p>
<div class="desc">
		// Action bar tip: nb (notabene - what was the markdown? )
		/**
		 * Opens md file of same name and placement as sqlite lookedup latest html file in mozilla browser
		 * Not any latest, but that latest which starts with startsWith parameter is selected.
		 * Purpose is to open the markdown file which was source for the html prior switcing 
		 * from browser to jEdit.
		 * startwith is mdBaseUrl(), plugin txtConv's 'site' - a unification of interlinked bunches 
		 * of files like browsers can save "Save complete webpage" archives
		 */
		void openBrowsedSource() {
			String fileN = lastestBrowsedIfStartsWith("Profile0",".mozilla/seamonkey",mdBaseUrl());
			if (null != fileN  &&  fileN.matches(".+\\.html")) 
				jEdit.openFile(view,mdBaseDir()+"/"+fileN.replaceAll("\\.html",".md"));
		}
		
		/**
		 * Retrieves latest url that starts with startsWith argument. Mozilla, linux and sqlite3 specific 
		 *
		 * @param mozProfileIniSectionLabel is the square surrounded ini file section name
		 * @param mozAtUserHomeProfilesIniDir is the relative part of path to directory of profiles.ini from user.home (~)
		 * @param startsWith is url, eg. "http://localhost/cyberkiss/"
		 * @return full url of latest browsed or null if no records in table moz_places has aa value in field Url that startswith startsWith
		 * @library readAllLines 
		 * @library externCmdOutput
		 */
		String lastestBrowsedIfStartsWith(String mozProfileIniSectionLabel, String mozAtUserHomeProfilesIniDir, String startsWith) {
			String mozPlacesSqliteFileName() {
				boolean profileChosen;
				String fullMozProfilesIniDir=System.getProperty("user.home")+"/"+mozAtUserHomeProfilesIniDir;
				for ( s: readAllLines(fullMozProfilesIniDir+"/profiles.ini")) {
					if (profileChosen) {
						String[] kv = s.split("=");
						if (kv[0].equals("Path")) {
							return fullMozProfilesIniDir+"/"+kv[1]+"/places.sqlite";
						}
					} else
						if (s.trim().equals("["+mozProfileIniSectionLabel+"]"))
							profileChosen=true;
				}
				return null;
			}
			if (!OperatingSystem.isUnix())
				return null;
			String dbFile = mozPlacesSqliteFileName();
			if (null == dbFile)
				return null;
			String query = "SELECT url FROM moz_places order by last_visit_date DESC LIMIT 1";
			List inp =externCmdOutput(new String[] {"sqlite3",dbFile,query});
			if (inp.size() > 0 ) 
				if ( inp.get(0).startsWith(startsWith)) 
					return inp.get(0).substring(startsWith.length());
				else
					view.getStatus().setMessage(inp.get(0));
			return null;
		}
		
		openBrowsedSource();
</div>


<p class='macro'><a class='pop' href'#' id='lookupMdLink'>&nbsp;&#9656;&nbsp;</a>&nbsp;Text/lookupMdLink.bsh <span class='macroType'>(/macros/Text)</span></p>
<div class="desc">
		//Action bar tip: upm (up to markdown)
		/**
		 * Make a link to an existing file in mdBaseDir().
		 *
		 * An example
		 * ==========
		 *
		 * If a file, froobonitz.md exists, a link to the comming or already existing froobonitz.html can made by writting:
		 *
		 *		[i am the link text][froobonitz]
		 *
		 * This macro shall be invoked after just have written eg.
		 *
		 *		[i am the link text][fro
		 *
		 *	or another eg.
		 *
		 *		[i am the link text][oni
		 *
		 * thus, some characters without closing square bracket. The result if:
		 *		* no match: nothing happends in current buffer, but a message appears in status line
		 *		* one match: the files barename is filled in
		 *		* more than one match: a list for selection pops up prior filling in.
		 */
		void lookupMdLink() {
			String sPatt;
		
			command(String linkDest) {
				replaceWordBeforeCursor(sPatt.length(),linkDest+"]");
			}
			
			caretPos=textArea.getCaretPosition();
			linecaretPos = caretPos - textArea.getLineStartOffset(textArea.getCaretLine());
			beforeCaret = textArea.getLineText(textArea.getCaretLine()).substring(0,linecaretPos);
			bracketPos = beforeCaret.lastIndexOf('[');
			if (bracketPos != -1 && bracketPos < linecaretPos-1)  {
				sPatt = beforeCaret.substring(bracketPos+1).trim();
				if (sPatt.indexOf(']') != -1) {
					view.getStatus().setMessage("'] found ?");
					return;
				}
				List replacements = withinMdBaseDir(sPatt,"md");
				if (replacements.size() == 0) { 
					view.getStatus().setMessage(sPatt +" ?");
					return;
				}
				if (replacements.size() == 1)
					replaceWordBeforeCursor(sPatt.length(),replacements.get(0)+"]");
				else
					actionsDialog(this,NumSeq(replacements,sPatt),view);
			
			} else
				view.getStatus().setMessage("no '[' before word");
		}
		
		/**
		 * looks up in mdBaseDir(), files with a given suffix and a word as part of the filename
		 *
		 * @param fileNPart the word that selects file that has the word inside their barename  
		 * @param suffix of files selected
		 * @return list of selected barenames of files
		 */
		List withinMdBaseDir(String fileNPart, String suffix) {
			fileVisitor() {
				boolean continueVisitFile(File f) {
					String fName = f.getName();
					if(fName.endsWith("."+suffix) ) {
						String bareFN = fName.replaceAll("\\."+suffix+"$","");
						if (bareFN.indexOf(fileNPart) != -1)
							retval.add(bareFN);
					}
					return true;
				}
				return this;
			}
			List retval=new ArrayList();
			walkFileTree(new File(mdBaseDir()),fileVisitor(),2);
			return retval;
		}
		
		void replaceWordBeforeCursor(int backsteps, String newWord) {
			while (backsteps-- > 0)
				textArea.goToPrevCharacter(true);
			textArea.setSelectedText(newWord);
		}
		
		lookupMdLink();
</div>


<p class='macro'><a class='pop' href'#' id='copyAbove2carret'>&nbsp;&#9656;&nbsp;</a>&nbsp;Text/copyAbove2carret.bsh <span class='macroType'>(/macros/Text)</span></p>
<div class="desc">
		// Action bar tip: e2 (easy 2 caret from above)
		/**
		 * copy from line above until carret.
		 */
		
		txtInsertPos=buffer.getLineStartOffset(textArea.getCaretLine());
		int curVislen=0;
		for (int pos = txtInsertPos,rest=139; pos < textArea.getCaretPosition(); pos++) {
			int jmp = (rest % 4)+1;
			boolean isTab = buffer.getText(pos,1).equals("\t");
			curVislen += isTab ? jmp : 1;
			rest -= isTab ? jmp : 1;
		}
		int removeLen = textArea.getCaretPosition()-txtInsertPos;
		int txtCopyFromPos=buffer.getLineStartOffset(textArea.getCaretLine()-1);
		visCopyFromCnt=curVislen;
		realCopyFromCnt=0;
		while (visCopyFromCnt>0) { 
			visCopyFromCnt -= buffer.getText(realCopyFromCnt+txtCopyFromPos,1).equals("\t") ? ((143-realCopyFromCnt) % 4)+1: 1;
			realCopyFromCnt++;
		}
		String text = buffer.getText(txtCopyFromPos,realCopyFromCnt);
		buffer.remove(txtInsertPos,removeLen);
		buffer.insert(txtInsertPos,text);
		
		
</div>


<p class='macro'><a class='pop' href'#' id='startup'>&nbsp;&#9656;&nbsp;</a>&nbsp;startup.bsh <span class='macroType'>(/macros)</span></p>
<div class="desc">
		// da-key tip A+LESS
		/**
		 * $JEDIT_SETTINGS/startup functions get sourced and then the functions is
		 * displayed in button clickable dialog OR a message telling 'startup is sourced' 
		 * appears in statusline.
		 * The former happends if the startupfile file with the most recent lastModified
		 * value is more than 10 seconds old.
		 *
		 * FAIL! even if Buffer.save(view,null) have returned true when invoked by this macro, a
		 * later, in same execution of this macro, getting of the file's lastmodified 
		 * using new File or buffer.getLastModified() returns sometime an elder stamp.
		 * BUT SOMETIMES IT WORKS???? just keep an eye on statusline when using this
		 * macro.
		 *
		 * In this way this macro contains two actions: just sourcing or 
		 * sourcing and clickable popup.
		 * 
		 * A click insert the function at carret position, a shift+click opens the file 
		 * containing the function for editing.
		
		 * It is importent to follow the folding commenting style - se documentation for
		 * startup function foundStartupFunction(funcName,view).
		 *
		 * After editing a startup file, running this macro sources the startup functions 
		 * - which also pops up errors reporting for convenient repeating the edit->source 
		 * cycle for getting an, at least, correct beanshell syntax.
		 * 
		 * All altered startupfiles, an only those in buffers, is saved if dirty - no need
		 * to save manually. Saving manually and wait 10 sescond before invoking this
		 * macro, skips sourcing (until jedit is restarted, obvious) 
		 * 
		 * Another use: Having the knowledge presents about which startup source file a 
		 * given function is defined in, the file can be opened using this macros shift-click
		 *
		 * @See cyberkiss "beanshell source" , foundStartupFunction(funcName,view)
		 */
		void sourceAndShow() {
			saveAllStartupFiles(view);
			long curLastModf;
			long maxLastModf;
			for (File f: new File(sDir()+"/startup").listFiles())  {
				if (f.getName().indexOf("#") == -1) {
					curLastModf = f.lastModified();
					//p(f.getName()+" "+java.text.DateFormat.getTimeInstance().format(new Date(curLastModf)));
					if (curLastModf > maxLastModf) 
						maxLastModf=curLastModf; 
					BeanShell.runScript(view,f.getPath(),null,false);
				}
			}
			long sinceSaved = System.currentTimeMillis() - maxLastModf;
			if (sinceSaved < 10000)
				view.getStatus().setMessage(sinceSaved+"millis - Just sourcing");
			else
				functionsDialog();
		}
		
		void functionsDialog()
		{
			
			void addButtons() {
				int col=0;
				int row = rows;
				for (String s:sNames.keySet()) {
					JButton j= new JButton(s);
					j.setToolTipText(s+sNames.get(s));
					j.setHorizontalAlignment(SwingConstants.LEFT);
					j.addActionListener(this);
					buttoncolumn[col].add(j);
					if (--row == 0) {
						col++;
						row=rows;
					}
				}
			}
			TreeMap sNames=sourcedFuntions();
			int cols = 1+(sNames.size()-1)/35;
			int rows = sNames.size()/cols+ ( sNames.size() % (sNames.size()/cols) > 0 ? 1 : 0);
			dialog = new JDialog(view,"shift-click for edit"); //, title, false);
			content = new JPanel(new BorderLayout());
			dialog.setContentPane(content);
			JPanel allColumns = new JPanel();
			allColumns.setLayout(new GridLayout(1,cols));
			JPanel[] buttoncolumn = new JPanel[cols];
			for (int i=0; i<cols; i++) {
				buttoncolumn[i] = new JPanel();
				buttoncolumn[i].setLayout(new GridLayout(rows,1));
				allColumns.add(buttoncolumn[i]);
			}
			addButtons();
			content.add(allColumns, "South");
			dialog.pack();
			dialog.setLocationRelativeTo(view);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		
		
			public void actionPerformed(ActionEvent e) {
				String[] buttonParts = e.getActionCommand().split(" ");
				fName=buttonParts[buttonParts.length-1];
				String fArgsList = sNames.get(e.getActionCommand());
				dialog.dispose();
				if (e.getModifiers() == 16) {
					tcp=textArea.getCaretPosition();
					buffer.insert(tcp,fName+fArgsList);
					int fLen = fName.length();
					textArea.setCaretPosition(tcp+fLen+1);
					textArea.setSelection(new Selection.Range(tcp+fLen+1,tcp+fLen+fArgsList.length()-1));
				} else
					foundStartupFunction(fName.replaceAll("\\(.+",""),view);
			}
		}
		sourceAndShow();
</div>


<p class='macro'><a class='pop' href'#' id='cyberkiss'>&nbsp;&#9656;&nbsp;</a>&nbsp;cyberkiss.bsh <span class='macroType'>(/macros)</span></p>
<div class="desc">
		// key tip: S+F10
		// BROWSER TYPE AND INSTALLATION PLACE DEPENDT -LOOK AT void browseThat(String url)
		import java.util.stream.Collectors;
		/**
		 * Package name importer, Api and startup function help and cyberkisses. Place carret 
		 * in front of, or select word(s) to be used. If carret is after a space clipboard 
		 * is used
		
		 * 
		 * This function and meant to be edited together with presentSelection for sending
		 * stupid requests of, in terms of sending dialog resources, simple art.  
		 */
		void cyberkiss() {
			String text;
			List words = backWordsOrSelected(view,true);
			if (words.get(0).endsWith(" ") || words.get(0).length()==0)
				text=getClipboard();
			else
				text = words.get(1);
			if (null != text) {
				presentSelection(Arrays.asList(new String[]{
					 "import"
					,"api-help"
					,"beanshell source"
					,"java keyword"
					,"duckduckgo"
					,"google"
					,"en-da"
					,"da-en"
					,"en.wiki"
					,"da.wiki"
					,"da.glosbe"
					,"en.glosbe"
					,"thesaurus"
					,"findvej"
					,"phone"
					,"md file"
					}),text);
			}
		}
		
		/**
		 * Pops up actionsDialog, which is a shortcut key prefixed list.
		 *
		 * @param menues list to choose an action item from
		 * @param kiss paramter to chosen action
		 */
		void presentSelection(List menues, String kiss) {
			command(String choice) {
				switch (choice) {
					case "import":
						doImport(kiss);
						break;
					case "api-help":
						apiHelp(kiss);
						break;
					case "beanshell source":
						if (!foundStartupFunction(kiss,view))
							view.getStatus().setMessage(funcName+" ?");
						//else
							//p(buffer.getName());
						break;
					case "java keyword":
						url = javaKeywordUrl(kiss);
						if (url.size() > 1)
							if (url.size() == 2) 
								browseThat(url.get(0));
							else {
								actionsDialog(this,numSeqSplitLast(url),view);
							}
						else
							view.getStatus().setMessage(kiss+" ?" );
						break;
					case "duckduckgo":
						browseThat("https://duckduckgo.com/?q="+kiss.replaceAll(" ","+").replaceAll(":","%3a"));
						break;
					case "google":
						browseThat("https://google.dk/search?q="+kiss.replaceAll(" ","+").replaceAll(":","%3a"));
						break;
					case "findvej":
						browseThat("https://findvej.dk/"+kiss);
						break;
					case "phone":
						browseThat("https://118.dk/search/go?what="+kiss);
						break;
					case "en-da":
						browseThat("https://translate.google.com/#en/da/"+kiss.replaceAll(" ","%20"));
						break;
					case "da-en":
						browseThat("https://translate.google.com/#da/en/"+kiss.replaceAll(" ","%20"));
						break;
					case "en.wiki":
						browseThat("https://duckduckgo.com/?q=site%3Aen.wikipedia.org+"+kiss.replaceAll(" ","+"));
						break;
					case "da.wiki":
						browseThat("https://duckduckgo.com/?q=site%3Ada.wikipedia.org+"+kiss.replaceAll(" ","+"));
						break;
					case "da.glosbe":
						browseThat("https://da.glosbe.com/da/en/"+kiss);
						break;
					case "en.glosbe":
						browseThat("https://en.glosbe.com/en/da/"+kiss);
						break;
					case "thesaurus":
						browseThat("https://www.thesaurus.com/browse/"+kiss);
						break;
					case "md file":
						openMdFileInBaseDir(kiss);
						break;
					default:
						browseThat(choice);
				}
			}
			actionsDialog(this,NumSeq(menues,kiss),view);
		}
		
		/**
		 * Alternativ to browser historic
		 *
		 * @param keyword in the java language
		 * @return url descibing the keyword
		 */
		List javaKeywordUrl(String keyword) { 
			List urls = Arrays.stream(new String[] {
				 "if=n0/if"
				,"else=n0/if"
				,"switch=n0/switch"
				,"while=n0/while"
				,"do=n0/while"
				,"for=n0/for"
				,"break=n0/branch"
				,"continue=n0/branch"
				,"return=n0/branch"
				,"default=n0/switch"
				,"case=n0/switch"
				,"instanceof=n0/op2"
				,"catch=e0/catch"
				,"finally=e0/finally"
				,"throw=e0/throwing"
				,"try=e0/try"
				,"assert=https://docs.oracle.com/javase/7/docs/technotes/guides/language/assert"
				,"new=https://docs.oracle.com/javase/tutorial/java/javaOO/objectcreation"
				,"enum=http://tutorials.jenkov.com/java/enums"
				,"enum=https://howtodoinjava.com/java/enum/guide-for-understanding-enum-in-java/"
				}).filter(Predicate(keyword,"item-> item.startsWith(arg1+´=´)")).map(Mapper(
					 "item->item.replaceAll(´^\\w+?=´,´´)"
					+".replaceAll(´^n0/´,´https://docs.oracle.com/javase/tutorial/java/nutsandbolts/´)"
					+".replaceAll(´^e0/´,´https://docs.oracle.com/javase/tutorial/essential/exceptions/´)"
					+".replaceAll(´([^/])$´,´$1.html´)"
					)).collect(Collectors.toList());
				urls.add(keyword);
				return urls;
		}
		
		
		numSeqSplitLast(List keyWTrailedUrls) {
			String keyW = keyWTrailedUrls.remove(keyWTrailedUrls.size()-1);
			return NumSeq(keyWTrailedUrls,keyW); 
		 }
		
		void browseThat(String url) {
			String[] cmd = { 
				OperatingSystem.isUnix()
					? "seamonkey" 
					: OperatingSystem.isWindows()
							? "\"C:\\Programmer\\Mozilla Firefox\\firefox.exe\""
							: null
				,url};
			if (null != cmd[0])
				Runtime.getRuntime().exec(cmd); 
			else
				view.getStatus().setMessage("Unsupported OS");
		}
		
		String OSUrl(String key) {
			String [] linux = { 
			 "rt=http://eee/java8/api/"
			,"nashorn=https://docs.oracle.com/javase/8/docs/jdk/api/nashorn/"
			,"jedit=file:///usr/local/share/jEdit/5.5.0/doc/api/"
			,"jsoup-1.11.3=https://jsoup.org/apidocs/"
			};
			
			String [] windows = { 
			 "rt=file:///D:/home/da/Prtlcls-lang-stands/api/java8/api/"
			,"jedit=file:///C:/Programmer/jEdit/doc/api/"
			,"AbsPath=file:///D:/home/dev/devel/java/AbsPath/doc/"
			};
		
			String scriptIsMissingUrl() {
				File tmpfileName = File.createTempFile("jimpstat",".html");
				tmpfileName.deleteOnExit();
				toFile(tmpfileName.getPath(), 
				 "<!DOCTYPE html><meta charset=\"utf-8\"><title>Script location status</title><p>"
				+"Shell script jimport not found</p><p>"
				+"See the documentation for method jimport in file $JEDIT_SETTINGS/startup/utils.bsh</p>");
				
				return "file://"+(OperatingSystem.isWindows() ? "/" : "")+ux(tmpfileName.getPath());
			}
			Properties p = new Properties();
			p.load( new StringReader(
				String.join("\n",
					OperatingSystem.isUnix()
			 	 		? linux 
			 	 		: OperatingSystem.isWindows()
			 	 			? windows
			 	 			: new String[]{} )));
			 	 
			return key.equals("scriptNotFound") ? scriptIsMissingUrl() : p.getProperty(key);
		}
		
		void doImport(String  className) {
			final int atLineToInsert=5;
			void command(jarClass) {
				String[] packPath = jarClass.split(":");
				String pacName = packPath[1].replaceAll("/",".").replaceAll("\\.class\\s*$","");
				if (pacName.length() > 1) {
					buffer.insert(buffer.getLineEndOffset(atLineToInsert-2),"import "+pacName+";\n");
					view.getStatus().setMessage(jarClass.replaceAll("\\.class\\s*$",""));
				} else
					view.getStatus().setMessage("Nothing inserted");
			}
			List classes = jimport(className);
			if (classes.size() > 0 )
				if (classes.size() > 1)
					actionsDialog(this, NumSeq(classes,"Classes"),view);
				else
					command(classes.get(0));	
			else
				view.getStatus().setMessage(className+" ?");
		}
		
		void openMdFileInBaseDir(String mdBareFile) {
			fileVisitor() {
				boolean continueVisitFile(File f) {
					if(f.getName().equals(mdBareFile+".md"))
						pathName = f.getPath();
					return true;
				}
				return this;
			}
			String pathName;
			walkFileTree(new File(mdBaseDir()),fileVisitor(),2);
			if (null==pathName)
				view.getStatus().setMessage(mdBareFile+" ?");
			else
				jEdit.openFile(view,pathName);
		}
		
		void apiHelp(String word) {
			
			void command(jarClass) {
				String[] packPath = jarClass.split(":");
				browseThat(OSUrl(packPath[0])+packPath[1].replaceAll("\\.class\\s*$",".html"));
			}
			List classes = jimport(word);
			if (classes.size() > 0 )
				if (classes.size() > 1) 
					actionsDialog(this, NumSeq(classes,"Classes"),view);
				else
					command(classes.get(0));
			else {
				view.getStatus().setMessage(word+" ?");
			}
		}
		
		cyberkiss();
			
			
</div>


<p class='macro'><a class='pop' href'#' id='insertDoc'>&nbsp;&#9656;&nbsp;</a>&nbsp;Java/insertDoc.bsh <span class='macroType'>(/macros/Java)</span></p>
<div class="desc">
		/**
		 * Inserts appropriately indented javadoc comment when caret is placed straight before first word in line. 
		 * Things like methods, with, in regex lingo, '\(.*\)' inside, is interpreted with potentially @param's and 
		 * subsequent @return if method seems to be a non constructer or not with return type void.
		 */
		void insertDoc() {
			String preStr="";
			int finalUp=3;
			int caretPos = textArea.getCaretPosition();
			for (int i=buffer.getLineEndOffset(textArea.getCaretLine()-1) ; i < caretPos;i++)
				preStr += (buffer.getText(i,1).getBytes()[0]==9) ? "    " : " ";
			String docStars = preStr+"/**\n"+preStr+" *\n"+preStr+" *\n";
			
			textArea.goToEndOfWhiteSpace(true);
			String str =textArea.getSelectedText();
			textArea.setCaretPosition(caretPos,true);
			
			if (str.indexOf("(") != -1) {
				String args = (str.replaceAll("^[^\\(]+\\(([^\\)]*)\\).*$","$1"));
				String[] begEnd = str.split("\\("+args+"\\)");
				
				if (args.length() > 0) 
					for (ta:args.trim().split(",")) {
						docStars += preStr+ " * @param "+ta.trim().split(" ")[1]+"\n";
						finalUp++;
					}
				String[] beg= begEnd[0].trim().replaceAll("\\s+"," ").split(" ");
				
				if (beg.length  > 1) {
					String retType = beg[beg.length-2].trim();
					if (!retType.equals("void") && !retType.equals("private") && !retType.equals("public")) {
						docStars += preStr+ " * @return \n";
						finalUp++;
					}
				}
			}
			docStars += preStr + " */\n";
			buffer.insert(buffer.getLineEndOffset(textArea.getCaretLine()-1),docStars);
			textArea.setCaretPosition(buffer.getLineStartOffset(textArea.getCaretLine()-finalUp));
			textArea.goToEndOfLine(false);
			buffer.insert(textArea.getCaretPosition(), " ");
		}
		insertDoc();
</div>


<p class='macro'><a class='pop' href'#' id='actionsDialog'>&nbsp;&#9656;&nbsp;</a>&nbsp;actionsDialog.bsh <span class='macroType'>(/macros)</span></p>
<div class="desc">
		// da-key tip: S+Backspace
		// EDIT BELOW FOR FILE MODE CONTEXT DECIDED MENU ITEMS
		/*//
		label02.*.<html><br></html>=label
		label10.bsh.<html><b><u>Beanshell</u></b></html>=label
		label11.bsh._execute=runBsh
		label19.bsh.<html><br></html>=label
		label20.java.xml.props.<html><b><u>Ant Targets<u></b></html>=label
		label21.java.xml.props.clea_n=runAnt
		label22.java.xml.props._compile=runAnt
		label23.java.xml.props._jar=runAnt
		label24.java.xml.props._run=runAnt
		label25.java.xml.props._doc=runAnt
		label31.java.xml.props.bsh.<html><b>Print urgency</b></html>=label
		label33.java.xml.props.bsh._1Debug=setUrgency
		label34.java.xml.props.bsh._3Message=setUrgency
		label35.java.xml.props.bsh._5Notice=setUrgency
		label36.java.xml.props.bsh._7Warning=setUrgency
		label36.java.xml.props.bsh._9Error=setUrgency
		label40.js.<html><b><u>Node_js<u></b></html>=label
		label41.js.r_un=runNode
		action.anttarget.latest=${jedit.options.actionthatbuffer}
		*///
		/**
		 * File associated actions dialog with invocation by shortcut key.
		 * 
		 * The shortcut key is remembered and might on next opening of the dialog be
		 * default action which can be chosen by return key og space key.
		 * 
		 * The properties list above desides the dialog labels. Labels are shown if current
		 * buffer's file suffix is one of the dot delimited words between 'labelxx' and
		 * last word in a property key. That last word is the label text. character '*'
		 * means any file.
		 * 
		 * Command(s) are grouped with a headerline - when disabling all commands in group,
		 * the header should also be disabled. Disabling can be done be just removing
		 * leadning 'l' in 'label' Leading underscore desides key for invoking action.
		 * 
		 * Only one global key is persistent, which means that every action must own a key.
		 * In 'labelxx' xx is only used as sorting order. 
		 *
		 * The Print urgency targets debug printing from plugins, but because developing
		 * and testing java often happens with a .bsh file in current buffer it is
		 * convenient to have these menues associated with .bsh too.
		 *
		 * @see UniProp(String fileName,String startPattern,String endPattern)
		 *
		 * @library UniProp
		 * @library antTask 
		 * @library runNode
		 */
		void actionsDlg(String suffix) {
			void command(String ch) {
				if (ch.length() > 0)
					up.setProperty(keyLatestActionLetter,""+ch);
			switch (cmds.get(up.getProperty(keyLatestActionLetter))) {
					case "runAnt":
						antTask(parm.get(up.getProperty(keyLatestActionLetter)),view,buffer);
						break;
					case "runBsh":
						Macros.runScript(view,buffer.getPath(),true);
						view.goToBuffer(buffer);
						break;
					case "runNode":
						runNode(view,buffer);
						break;
					case "setUrgency":
						jEdit.getPlugin("home.txtconv.TxtConvPlugin",true).setPrintUrgency(
							Integer.parseInt(parm.get(up.getProperty(keyLatestActionLetter)).substring(0,1)));
						break;
					default:
						Macros.message(view,"uknown command: "+cmds.get(up.getProperty(keyLatestActionLetter)));
				}
			}
			XThis MenuesObj(List menues,String sCKeys, int defaultIndex) {
				int ix;
				String heading() { return null; }
				String shCKeys() { return sCKeys; } 
				String readLabelText() {
					return ix < menues.size()
						? menues.get(ix++)
						: null;
				}
				int defIndex() { return defaultIndex; }
				String menuItem(int index) {
					return ""+sCKeys.charAt(index);
				}
				return this;
			}
			void buildMenues() {
				List matKeys=up.matchingSortedKeys("label\\d\\d\\.(\\w+\\.)*("+suffix+"\\.|\\*\\.).+");
				for (String s:matKeys) 
					if (up.getProperty(s).equals("label"))
						menuItems.add(s.replaceAll(".*\\.(.+)","$1"));
					else {
						menuItems.add(s.replaceAll(".*\\.(\\w*)_(\\w)(.*)","$1<u>$2</u>$3").replaceAll("(.+)","<html>$1</html>"));
						String us = s.replaceAll(".*_(\\w).*","$1");
						sCKeys += us;
						parm.put(us,s.replaceAll("^label\\d\\d.+\\.","").replaceAll("_","")); 
						cmds.put(us,up.getProperty(s));
						if (us.equals(up.getProperty(keyLatestActionLetter))) 
							defActionIx = menuItems.size()-1;
					}
			}
			up=UniProp(scriptPath,"/*//","*///");
			List menuItems = new ArrayList();
			HashMap cmds = new HashMap();
			HashMap parm = new HashMap();
			String sCKeys="";
			int defActionIx=-2;
			final String keyLatestActionLetter = "action.anttarget.latest";
			buildMenues();
			actionsDialog(this,MenuesObj(menuItems,sCKeys,defActionIx),view);
		}
		
		actionsDlg(buffer.getName().replaceAll(".+\\.",""));
</div>


<p class='macro'><a class='pop' href'#' id='closeUntitled'>&nbsp;&#9656;&nbsp;</a>&nbsp;closeUntitled.bsh <span class='macroType'>(/macros)</span></p>
<div class="desc">
		// key tip: A+d 
		/**
		 * Close buffer, dirty or not, without saving if it's name is "Untitled"
		 */
		if (buffer.getName().startsWith("Untitled"))
			jEdit._closeBuffer(view,buffer);
</div>


<p class='macro'><a class='pop' href'#' id='scratchpad'>&nbsp;&#9656;&nbsp;</a>&nbsp;scratchpad.bsh <span class='macroType'>(/macros)</span></p>
<div class="desc">
		/** 
		 * playground for experimenting and seldom starting place for making a new macro.
		 * The OUTSIDE OF COMMENT BLOCK status of line 5 is intentional
		 * (placement of import insert's by macro cyberkiss) **/
		
		import org.gjt.sp.jedit.bsh.XThis;
		
		/**
		 * Using this macro is meant to be convenient using two other macroes:
		 *
		 *     1. 'open_scratchpad'
		 *     2. 'execute' (macro actionsDialog.bsh)
		 * 
		 * ad 1.
		 * A shortcut for 'open_scratchpad', moving opening of scratchpad.bsh from the easy of opening it using File Browser, 
		 * command bar, console or menues->macro to something as reflectoric ad typing a space, is the justification for 
		 * having a open_scratchpad macro.
		 *
		 * ad 2.
		 * Added to the de facto ways of executing macroes using menues->macros or command bar is a file mode depent action 
		 * chosen from macro actionsdialog. For beanshell files (suffix .bsh), executing is a made an option. Subsequent running  
		 * actionsDialog has prior chosen action for a mode as default - a return key repeats.
		 * The fuss is about having visual present in beanshell code what's getting executed - in connection with a print facility
		 * to a docked window other messy activity log.
		 *
		 * The print facility, documented in $JEDIT_SETTINGS/startup/bshConsole.bsh, tells the purphose of below cls(0). 
		 ***/
		cls(0);  //meant to stay permanent
		
		
		//View
		/***,sv,persons
		Emp ID ,Name Prefix ,First Name ,Middle Initial ,Last Name ,Gender ,E Mail 
		677509 ,Drs.        ,Lois       ,H              ,Walker    ,F      ,lois.walker@hotmail.com
		940761 ,Ms.         ,Brenda     ,S              ,Robinson  ,F      ,brenda.robinson@gmail.com
		428945 ,Dr.         ,Joe        ,W              ,Robinson  ,M      ,joe.robinson@gmail.com
		408351 ,Drs.        ,Diane      ,I              ,Evans     ,F      ,diane.evans@yahoo.com
		193819 ,Mr.         ,Benjamin   ,R              ,Russell   ,M      ,benjamin.russell@charter.net
		499687 ,Mr.         ,Patrick    ,F              ,Bailey    ,M      ,patrick.bailey@aol.com
		539712 ,Ms.         ,Nancy      ,T              ,Baker     ,F      ,nancy.baker@bp.com
		380086 ,Mrs.        ,Carol      ,V              ,Murphy    ,F      ,carol.murphy@gmail.com
		477616 ,Hon.        ,Frances    ,B              ,Young     ,F      ,frances.young@gmail.com
		162402 ,Hon.        ,Diana      ,T              ,Peterson  ,F      ,diana.peterson@hotmail.co.uk
		231469 ,Mr.         ,Ralph      ,L              ,Flores    ,M      ,ralph.flores@yahoo.com
		153989 ,Prof.       ,Jack       ,C              ,Alexander ,M      ,jack.alexander@gmail.com
		
		386158 ,Mrs.        ,"Me,""lis""sa"    ,Q              ,King      ,F      ,melissa.king@comcast.net
		***/
		
		//List l =dsvLines(scriptPath,"persons");
		//p(l);
		p(System.getProperty("user.home"));
		
		
		//* Copyright (C) 2003 Ollie Rutherfurd <oliver@jedit.org>
		
</div>


<p class='macro'><a class='pop' href'#' id='utils'>&nbsp;&#9656;&nbsp;</a>&nbsp;utils.bsh <span class='macroType'>(/startup)</span></p>
<div class="desc">
		/* :tabSize=4:indentSize=4:noTabs=false:
		 * :folding=explicit:collapseFolds=1: 
		The folding style is important for lookup facility of macro startup */ 
		
		import java.util.regex.Pattern;
		import java.util.regex.Matcher;
		import java.nio.file.Files;
		import java.nio.charset.Charset;
		
		nulSA = new String[]{};
		
		
<p class='function'><a class='pop' href='#' id='sDir'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String sDir()<div class="subdesc">
		/**
		 * gets jEdit SettingsDirectory
		 *
		 * @return Jedit SettingsDirectory
		 */
		String sDir() {
			return jEdit.getSettingsDirectory();
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='ux'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String ux(String possibleBackslashed)<div class="subdesc">
		/**
		 * Needed multiplatform workarounds prior certain regex manipulations
		 *
		 * @param possibleBackslashed path (windows)
		 * @return forward slashed path
		 */
		String ux(String possibleBackslashed) { 
			return possibleBackslashed.replaceAll("\\\\","/"); 
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='toArray'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String[] toArray(Object toArrayAble)<div class="subdesc">
		/**
		 * Shortcut for toArrayAble.toArray(String[]), where toArrayAble is a container instance implementing Collection 
		 *  
		 * @param any toArrayAble container eg. ArrayList
		 * @return String[] array
		 */
		String[] toArray(Object toArrayAble) {
		    return toArrayAble.toArray(new String[toArrayAble.size()]);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List asList(String i1,String i2,String i3)<div class="subdesc">
		/**
		 * Beanshells absence of varargs motivated shortcut for Arrays.asList( ...
		 *
		 * @param ix is list items of type String
		 * @return List
		 */
		List asList(String i1,String i2,String i3) {
			return Arrays.asList(new String[]{i1,i2,i3});
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List asList(String i1,String i2)<div class="subdesc">
		/**
		 * Beanshells absence of varargs motivated shortcut for Arrays.asList( ...
		 *
		 * @param ix is list items of type String
		 * @return List
		 */
		List asList(String i1,String i2) {
			return Arrays.asList(new String[]{i1,i2});
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List asList(String i1)<div class="subdesc">
		/**
		 * Beanshells absence of varargs motivated shortcut for Arrays.asList( ...
		 *
		 * @param ix is list items of type String
		 * @return List
		 */
		List asList(String i1) {
			return Arrays.asList(new String[]{i1});
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='mEval'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String mEval(String expressionList)<div class="subdesc">
		/**
		 * Convenient avoidance of 'Math' prefixing matematically functions, and build expression as serie af expression of which the value of rightmost 
		 * is returned. An example illustates:
		 *
		 * A spot, with a beam angle of 36 degreese is places 1.3 meter above a table. What is the diameter of the beam at table? 
		 * mEval("angle=36;height=1.3;2*height*tan(pi*angle/2/180)")
		 *
		 * @param semicolon delimited lists of numeric expressions or assigments. Space isn't allowed
		 * @return value of rightmost expression
		 */
		String mEval(String exp) {
			return ""+BeanShell.eval(jEdit.getActiveView(),BeanShell.getNameSpace(),
				exp.replaceAll("(tan|cos|sin|sqrt|atan|acos|asin|sqrt|pow|exp|log)\\(","Math.$1(").replaceAll("(?i)PI","Math.PI"));
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List readAllLines(File file)<div class="subdesc">
		/**
		 * Iterabel, comparable, toArrayAble, random accessible list of utf-8 encoded file lines
		 *
		 * @param file to read from
		 * @return List of utf-8 encoded lines of file or an empty list on IO. If supressIOException
		 * is true an IOException just return an empty List.
		 */
		List readAllLines(File file, boolean supressIOException) {
			List retval;
			try {
				retval = Files.readAllLines(file.toPath(),Charset.forName("UTF-8"));
			} catch(IOException e) {
				if (supressIOException) 
					retval = new ArrayList();
				else
					throw e;
			}
			return retval;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List readAllLines(String fileName)<div class="subdesc">
		/**
		 * Iterabel, comparable, toArrayAble, random accessible list of utf-8 encoded file lines
		 *
		 * @param file name of file to read from
		 * @return  List of utf-8 encoded lines of file
		 */
		List readAllLines(String fileN) {
			return readAllLines(new File(fileN),false);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List  fileLines(String fileName, String startPattern, String endPattern)<div class="subdesc">
		/**
		 * Lines of pattern delimited block. The pattern lines is not inclusive the block 
		 *
		 * @param fileName with lines
		 * @param startPattern matches the line before returned line 0
		 * @param endPattern matches the line after last line in returned list
		 * @return lines in pattern delimited block
		 */
		List  fileLines(String fileName, String startPattern, String endPattern) {
			return Beam(Files.lines(new File(fileName).toPath()))
				.filter(false,"item-> {"+
					"if ( arg1 == true )"+
					"    return item.matches(´"+endPattern+"´)  ? (arg1=false) : true;"+
					"arg1 = item.matches(´"+startPattern+"´);"+
					"return false; }")
				.toList();	
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='fileLines'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface fileLines(String fileName, String blockId)<div class="subdesc">
		/**
		 * Lines of pattern delimited block. The pattern lines is not inclusive the block 
		 *
		 * @param fileName with lines
		 * @param blockId forms parameter startPattern for calling fileLines(String fileName, String startPattern, String endPattern) 
		 * @return lines in pattern delimited block
		 */
		List fileLines(String fileName, String blockId) {
			return fileLines(fileName,"/\\*\\*\\*"+blockId,"\\s*\\*\\*\\*/");
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List startupBeanshellFilesLines()<div class="subdesc">
		/**
		 * All lines in  jEdit settings directory startup directory files
		 *
		 * @return List of lines in all jEdit settings directory startup directory files 
		 */
		public List startupBeanshellFilesLines() {
			List lines;
			for (File f : new File(sDir()+"/startup").listFiles())
				if (null==lines) 
					lines = readAllLines(f);
				else
					lines.addAll(readAllLines(f));
			return lines;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='saveAllStartupFiles'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void saveAllStartupFiles(View view)<div class="subdesc">
		/**
		 * saves all dirty files in jeditSettings/startup
		 *
		 * @param view context
		 */
		boolean saveAllStartupFiles(View view) {
			boolean wasDirty;
			for (Buffer b: jEdit.getBuffers()) 
				if (ux(b.getPath()).startsWith(sDir()+"/startup/"))
					if(b.isDirty()) {
						wasDirty=true;
						while(!b.save(view,null));
					}
			return wasDirty;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='toFile'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void toFile(String fileName,thing) <div class="subdesc">
		/**
		 * Things being possible an array or iterable saved as file in charset utf-8
		 *
		 * @param fileName to hold thing
		 * @param thing to be saved as file in line broken toString() occurrence
		 */
		void toFile(String fileName,Object thing) {
			toFile(fileName,thing,null);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='toFile'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void toFile(String fileName,Object thing,Object filter) <div class="subdesc">
		/**
		 * Things being possible an array or iterable, converted saved as a file in charset utf-8
		 *
		 * @param fileName to hold thing
		 * @param thing to be saved as file in line broken toString() occurrence
		 * @param filter to transfer each line to something
		 */
		void toFile(String fileName,Object thing,Object function) {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileName),"utf-8"); 
			if (thing.getClass().isArray() || thing instanceof AbstractCollection )
				for (Object line : thing)
					osw.write(""+(null==function ? line : function.apply(line))+"\n");
			else osw.write(""+(null==function ? thing : function.apply(thing)));
			osw.close();
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='appendToFile'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void appendToFile(String fileName, List lines)<div class="subdesc">
		/**
		 *	Appends lines to file, charset utf-8
		 *
		 * @param fileName of file to which lines are appended
		 * @param lines to be appended
		 */
		void appendToFile(String fileName, List lines) {
			List oldLines = readAllLines(new File(fileName),true);
			oldLines.addAll(lines);
			toFile(fileName,oldLines);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List jimport(String className)<div class="subdesc">
		
		// OS: Linux | Windows 
		/**
		 * Get package name of class. It is used for two purposes.
		 *  1. Insertion of import declaration
		 *  2. Getting api help from Oracle's api documentation
		 *
		 * It uses an extern script, 'jimport' or 'jimport.bat'
		 * A prioritized list of jars can be searched - shown below facilitates jedit plugin development
		 * The script shall be reachable from a system shell - being in system environment PATH.
		 *
		 * NOTE (for the uninitiated and forgetful)
		 * on linux, chmod +x jimport
		 * on windows, grep and tr from unxutils required (or their equals - test!)
		 */ 
		 // Linux - (change jeditSettings or perhaps other directories)
		 
		 // | #!/bin/bash
		 // | if [ -z $1 ]; then
		 // |	exit 2
		 // | fi
		 // | echoImport() { 
		 // | 	if [ -f $2 ];then
		 // | 		class=`jar -tf $2|grep /${1}.class`
		 // | 		if [ -n "${class}" ];then
		 // | 			jarFile=${2##*/}
		 // | 			echo ${jarFile%.*}:${class}
		 // | 		fi
		 // | 			return 0
		 // | 	fi
		 // | }
		 // | 
		 // | jeditSettings=/home/bvirk/.jedit
		 // | 
		 // | echoImport $1 /usr/lib/java/jre/lib/rt.jar
		 // | echoImport $1 /usr/local/share/jEdit/5.5.0/jedit.jar
		 // | # echoImport $1 ${jeditSettings}/jars/TxtConvPlugin.jar
		 // | # echoImport $1 ${jeditSettings}/jars/txtmark.jar
		 // | # echoImport $1 ${jeditSettings}/jars/jsoup-1.11.3.jar 
		 
		 
		 
		 
		 
		 
		 // Windows with unxUtils installed (grep tr), .bat file
		 
		 // | @echo off
		 // | if n%1==n exit 2
		 // | setlocal
		 // | goto main
		 // | 
		 // | :invokejar
		 // | if not exist %1 exit /b 1
		 // | FOR /F "usebackq tokens=*" %%b IN (`jar -tf %1 ^|grep /%2.class ^|tr -s "\r\n" " "`) DO set classes=%%b
		 // | IF NOT "%classes%"=="" ( 
		 // | 	echo %~n1:%classes%
		 // | 	set classes=
		 // | )
		 // | exit /b 0                                    
		 // | 
		 // | 
		 // | :main
		 // | if n%1==n goto endofprogram
		 // | call :invokejar C:\Programmer\java\jdk1.8.0\jre\lib\rt.jar %1
		 // | call :invokejar C:\Programmer\jEdit\jedit.jar %1
		 // | rem call :invokejar D:\home\dev\APPLIC~1\jEdit\jars\TxtConvPlugin.jar %1
		 // | rem call :invokejar D:\home\dev\APPLIC~1\jEdit\jars\txtmark.jar %1
		 // | rem call :invokejar D:\home\dev\APPLIC~1\jEdit\jars\jsoup-1.11.3.jar %1
		 // | 
		 // | :endofProgram
		 // | endlocal
		 
		/**
		 * @param onlyOracle limits returned packages for those in rt.jar
		 * @param className 
		 * @return package name of class
		 */
		List jimport(String className) {
			List classes = new ArrayList();
			int scriptIsMissingRetval() {
				return OperatingSystem.isUnix() ?  127 : 1;
			}
			if(OperatingSystem.isUnix() ||OperatingSystem.isWindows() ) {
				String[] cmds = {
					OperatingSystem.isUnix()
						? "/bin/bash"
						:"cmd.exe" 
					,OperatingSystem.isUnix()
						?"-c"
						: "/c"
					,"jimport "+className};
				Process proc = Runtime.getRuntime().exec(cmds); 
				BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((line = br.readLine()) != null) {
					String[] jarPacks = line.split(":");
						for (String s: jarPacks[1].split(" "))
							classes.add(jarPacks[0]+":"+s);
				}
				br.close();
				int retval;
				try {
					proc.waitFor();
				} catch (InterruptedException e ) {
					retval=scriptMissingRetval();
				}	
				if (retval == 0)
					retval = proc.exitValue();
				if (retval == scriptIsMissingRetval())
					classes.add("scriptNotFound: ");
				return classes;
			}
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='elSubst'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String elSubst(String input,Properties properties) <div class="subdesc">
		/**
		 * el for the 'JSP Expression Language' - function elSubst replaces ${foo} in "some ${foo} thing" with the value of foo in some properties 
		 * It's a string substitution princip also used in Apache ant build files.
		 * This simple implementation below makes ${}whatever} to ${whatever} without dereferencing to any property value. 
		 *
		 * @param properties used to look values
		 * @param input string
		 * @return String being EL substituted
		 */
		String elSubst(String input,Properties properties) {
			Pattern pat = Pattern.compile("\\$\\{([^}]+)\\}");
			Matcher m = pat.matcher(input);
			while (m.find()) {
				input = input.substring(0,m.start(1)-2)+ properties.getProperty(m.group(1),"${}"+m.group(1)+"}") +input.substring(m.end(1)+1);
				m =	pat.matcher(input);
			}
			return input.replaceAll("\\$\\{\\}([^}]+)\\}","\\${$1}");
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='UniProp'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis UniProp(String fileName, String startPattern, String endPattern) <div class="subdesc">
		/**
		 * Reads properties from startpattern/endpattern lines delimited block in file. Return a Properties like object which getProperty/setProperty 
		 * methods gives indirect access to jEdit properties. Eg. if a value is ${jedit.<jedit property>}, where <jedit property>, should 
		 * be read as a placeholder for a jedit property, it is the values of that Jedit property that is accessed. 
		 * Note, too, the convenient mathingSortedKeys method.
		 *
		 * @param endPattern 
		 * @param startPattern
		 * @param fileName
		 * @return org.gjt.sp.jedit.bsh.XThis object
		 *
		 */
		org.gjt.sp.jedit.bsh.XThis UniProp(String fileName, String startPattern, String endPattern) {
			String getProperty(String key) {
				String val = prop.getProperty(key);
				if (val != null && val.matches("\\$\\{jedit\\.[^\\}]+}"))
					val = jEdit.getProperty(val.replaceAll("\\$\\{jedit\\.([^\\}]+)}","$1"));
				return val;
			}
		 	void setProperty(String key,String val) {
		 		String fval = prop.getProperty(key);
				if (fval != null && fval.matches("\\$\\{jedit\\.[^\\}]+}")) {
					jEdit.setProperty(fval.replaceAll("\\$\\{jedit\\.([^\\}]+)}","$1"),val);
				} else
					prop.setProperty(key,val); 
		 	}
			List matchingSortedKeys(String regex) {
				ArrayList mk = new ArrayList();
				for (s : prop.keySet())
					if (s.matches(regex))
						mk.add(s);
				Collections.sort(mk);
				return mk;
			}
			Properties properties() { return prop; }
			Properties prop= new Properties();
			boolean inBlk=false;
			for (String line: readAllLines(fileName))
				if (inBlk)
					if (line.startsWith(endPattern))
						inBlk=false;
					else {
						String [] kv =line.trim().split("=");
						prop.setProperty(kv[0],kv[1]);
					}
				else
					if (line.startsWith(startPattern))
						inBlk=true;
			return this;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List startsWithPropertyKeys(String startsWith,Properties prop) <div class="subdesc">
		/**
		 * startswith key filtered list of properties key=value strings 
		 *
		 * @param startsWith common leftmost part of set of property key
		 * @param properties from which extracting those with common startswith keys
		 * @return list of key=value strings
		 */
		List startsWithPropertyKeys(String startsWith,Properties prop) {
			List ar = new ArrayList();
			for (s:prop.stringPropertyNames())
				if (s.startsWith(startsWith))
					ar.add(s+"="+prop.getProperty(s));
			Collections.sort(ar);
			return ar;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List jEditSWP(String startsWith)<div class="subdesc">
		/**
		 * startswith key filtered list of jEdit properties key=value strings 
		 *
		 * @param startsWith common leftmost part of set of property key
		 * @return list of key=value strings
		 */
		List jEditSWP(String startsWith) {
			return startsWithPropertyKeys(startsWith,jEdit.getProperties());
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List systemSWP(String startsWith)<div class="subdesc">
		/**
		 * startswith key filtered list of system properties key=value strings 
		 *
		 * @param startsWith common leftmost part of set of property key
		 * @return list of key=value strings
		 */
		List systemSWP(String startsWith) {
			return startsWithPropertyKeys(startsWith,System.getProperties());
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='foundStartupFunction'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void foundStartupFunction(String funcName,View view)<div class="subdesc">
		/**
		 * foundStartupFunction opens file containing startup function and position carret at folding heading
		 * The function is identified by the commentline having the folding start pattern -
		 * if this doesn't match exactly the function isn't found.
		 * buffer gets locked - thus live with the inconvenient of unlocking and SOURCE IMMEDIATE! 
		 * using macro startup - thus, without manually saving.
		 *
		 * Resourcing a startup functions reveals the difference between updating and
		 defining - if the signature of a function is changed, two function exists 
		 until next time jEdit is started. Just remember to change all places where
		 function is used! in console use grep -nro in $JEDIT_SETTINGS and other *.bsh places
		 * (unxUtils in path in windows)
		 *
		 * @param funcname without signature (return type, parentheses or arguments) 
		 * @param view is 'predefined' view 
		 * @return boolean true if function name is found
		 */
		boolean foundStartupFunction(String funcName,View view) {
			for (File f : new File(sDir()+"/startup").listFiles())
				if (String.join(" ",readAllLines(f.getPath())).matches(".+?//\\{\\{\\{ ([\\w\\[\\]]+\\s+?)+"+funcName+"\\(.+")) {
					if (jEdit.openFile(view, f.getPath()) != null) {
						Buffer buffer = view.getBuffer();
						if (!buffer.isLocked())
							buffer.toggleLocked(view);
						p(view.getBuffer().getName()); 
						jEdit.setBooleanProperty("search.hypersearch.toggle",false);
						jEdit.setBooleanProperty("search.keepDialog.toggle",false);
						org.gjt.sp.jedit.search.SearchAndReplace sap = new org.gjt.sp.jedit.search.SearchAndReplace(); 
						sap.setIgnoreCase(false);
						sap.setRegexp(true);
						sap.setAutoWrapAround(true);
						sap.setSearchString("//\\{\\{\\{ ([\\w\\[\\]]+\\s+?)+"+funcName+"\\(.+");
						sap.find(view);
					}
					return true;
				}
			return false; 
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='sourcedFuntions'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ TreeMap sourcedFuntions()<div class="subdesc">
		/**
		 * List of function made available by running BeanShell.runScript with parameter ownNamespace beeing false as during startup 
		 *
		 * @return Treemap which (key,value) = (returntype methodname, ([method parameters,...]))
		 */
		TreeMap sourcedFuntions() {
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
			return methods;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='dateFormat'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String dateFormat(long unixtime, String format)<div class="subdesc">
		/**
		 * wrapper around java.text.SimpleDateFormat
		 *
		 * @param unixtime milliseconds since the epoch (00:00:00 GMT, January 1, 1970)
		 * @param format of SimpleDateFormat
		 * @return String of date representation.
		 */
		String dateFormat(long unixtime, String format) {
			return new java.text.SimpleDateFormat(format).format(new Date(unixtime)).toString();
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List hasSuffixInOrBelow(String dir,String suffix)<div class="subdesc">
		/**
		 * Get list that list of files in dir or below that has a given extension. 
		 *
		 * @param dir is top dir where files with suffix recides  
		 * @param suffix is the filter that includes files
		 * @return List of File objects
		 */
		List hasSuffixInOrBelow(String dir,String suffix) {
			List files = new ArrayList();
			walkFileTree(new File(dir),FileVisitor(files,suffix,"item -> { if (item.getName().endsWith(´.´+arg2)) arg1.add(item); return true; }"));
			return files;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='isNewerOrSolitary'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ boolean isNewerOrSolitary(File file,String compareDir, String anotherSuffix)<div class="subdesc">
		/**
		 * isNewerOrSolitary is used for decision for updating or creating a file with same barename as parameter file, but receding
		 * in compareDir and having suffix anotherSuffix
		 *
		 * @param file to compare
		 * @param compareDir of matching file 
		 * @param anotherSuffix than that of file 
		 * @return true if file is newer or there not exists some matching file in compareDir  
		 */
		boolean isNewerOrSolitary(File file,String compareDir, String anotherSuffix) {
			return isNewerOrSolitary(file,new File(compareDir,file.getName().replaceAll("\\.\\w+$","."+anotherSuffix)));
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='isNewerOrSolitary'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ boolean isNewerOrSolitary(File file,File compareTo)<div class="subdesc">
		
		/**
		 * isNewerOrSolitary is used for decision for updating or creating file compareTo
		 *
		 * @param file to compare
		 * @param compareTo of matching file
		 * @return true if file is newer than compareTo or compareTo don't exists.
		 */
		boolean isNewerOrSolitary(File file,File compareTo) {
			return !compareTo.exists() || file.lastModified() > compareTo.lastModified();
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List externCmdOutput(String[] cmds)<div class="subdesc">
		/**
		 *  Get a list of lines from an extern process
		 *
		 * @param cmds eg. {"sqlite3","/tmp/myDB.sqlite", "select name from persons"}
		 * @return List of lines from extern process
		 */
		List externCmdOutput(String[] cmds) {
			List lines = new ArrayList();
			Process proc = Runtime.getRuntime().exec(cmds); 
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((line = br.readLine()) != null) 
				lines.add(line);
			br.close();
			return lines;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='atCarretOrSelected'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String atCarretOrSelected(TextArea textArea)<div class="subdesc">
		/**
		 * Gets word or string from textarea
		 * 
		 * @param textarea of activeView
		 * @return selected string, rightmost word to carret or null
		 */
		String atCarretOrSelected(TextArea textArea) {
			return atCarretOrSelected(textArea,"\\w+\\b");
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='atCarretOrSelected'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String atCarretOrSelected(TextArea textArea, String match)<div class="subdesc">
		/**
		 * Gets word or string from textarea
		 * 
		 * @param textarea of activeView
		 * @param match to select eg. one or several words should be returned
		 * @return selected string, rightmost word to carret or null
		 */
		String atCarretOrSelected(TextArea textArea,String match) {
			String text = textArea.getSelectedText();
			if (null == text) {
				int carretPos = textArea.getCaretPosition();
				textArea.goToEndOfWhiteSpace(true); 
				text = textArea.getSelectedText();
				textArea.setCaretPosition( carretPos,true);
				if (null != text) {
					Matcher m = Pattern.compile(match).matcher(text);
					if (m.find()) 
						text = m.group();
				}
			}
			return text;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='unicodeEscaped'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String unicodeEscaped(String nonEscaped)<div class="subdesc">
		/**
		 * Convert String to their unicode ecaped representation - it use is to make unambiguous strings to put in code 
		 * that otherwise spooks with a compiler.
		 *
		 * @param nonEscaped is a normal string - eg. "I ♥ jEdit" 
		 * @return a unicode escaped sting - eg. "\u0049\u0020\u2665\u0020\u006a\u0045\u0064\u0069\u0074"
		 */
		String unicodeEscaped(String nonEscaped) {
			backslashUEscaped4digit(String hex) { 
				hex = "000"+hex; 
				return "\\u"+hex.substring(hex.length()-4,hex.length()); 
			}
			String result= "";
			for (int pos=0; pos < nonEscaped.length(); pos++)
				result +=backslashUEscaped4digit(Integer.toUnsignedString((int)nonEscaped.charAt(pos),16)); 
			return "\""+result+"\"";
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='walkFileTree'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void walkFileTree(File dir, XThis visitor, int maxDepth)<div class="subdesc">
		/**
		 * Inspired by Files.walkFileTree - traverse a directory recursive
		 *
		 * @param dir is directory where walking starts
		 * @param visitor is a function object that has a method boolean continueVisitFile(File f)
		 * @param maxDepth is depth of recursion, -1 is unlimited
		 */
		void walkFileTree(File dir, XThis visitor, int maxDepth) {
			for (File file: dir.listFiles()) 
				if (file.isDirectory()) { 
					if (maxDepth != 0 )
						walkFileTree(file,visitor,maxDepth-1);
				} else 
					if (!visitor.continueVisitFile(file))
						break;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='walkFileTree'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void walkFileTree(File dir, XThis visitor)<div class="subdesc">
		/**
		 * Inspired by Files.walkFileTree - traverse a directory recursive to unlimited depth
		 *
		 * @param dir is directory where walking starts
		 * @param visitor is a function object that has a method boolean continueVisitFile(File f)
		 */
		void walkFileTree(File dir, XThis visitor) {
			walkFileTree(dir, visitor,-1);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='getClipboard'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String getClipboard()<div class="subdesc">
		/**
		 * Gets clipboard textual content, if present
		 *
		 * @return String clipbord content or null
		 */
		String getClipboard() {
			java.awt.datatransfer.Transferable tAble = Toolkit.getDefaultToolkit().getSystemClipboard().getContents( null );
			import java.awt.datatransfer.DataFlavor;
			return tAble.isDataFlavorSupported(DataFlavor.stringFlavor)
				? (String)tAble.getTransferData( DataFlavor.stringFlavor )
				: null; 
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='numLeft2Caret'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ int numLeft2Caret(View view, boolean delete, int defaultOnNoNumber)<div class="subdesc">
		/**
		 * Get number left to caret
		 *
		 * @param view of JComponent in use
		 * @param delete desides buffer editing behavior
		 * @param defaultOnNoNumber return value if no number is found.
		 * @return integer left to caret, with any number af spaces between caret and
		 * number or defaultOnNoNumber if no number is found. Following happends to buffer
		 *	
		 *	input										result
		 *	---------------------------------------		--------------------
		 *	spaces follows number		delete=true		spaces are deleted
		 *	no spaces follows number	delete=true		number are deleted
		 *	spaces follows number		delete=false	spaces and number is selected
		 *	nospaces follows number		delete=false	spaces and number is selected
		 */ 
		int numLeft2Caret(View view, boolean delete, int defaultOnNoNumber) {
			TextArea textArea = view.getTextArea();
			int caretLinePos =textArea.getCaretPosition()-textArea.getLineStartOffset(textArea.getCaretLine());
			String toCaret =textArea.getLineText(textArea.getCaretLine()).substring(0,caretLinePos);
			Matcher m=Pattern.compile("\\d{1,2}\\s*$").matcher(toCaret);
			if (m.find())  {
				if (delete)
					textArea.backspaceWord(true);
				else
					for (int i=0; i< m.group().length(); i++)
						textArea.goToPrevCharacter(true);
				return Integer.parseInt( m.group().trim());
			} else
				view.getStatus().setMessage("default: "+ defaultOnNoNumber+" returned");
			return defaultOnNoNumber;
		
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='backWordsOrSelected'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ List backWordsOrSelected(View view, boolean reverse)<div class="subdesc">
		/**
		 * Get parameter from current line. In the following the string from start of 
		 * caretline to caret is called toCaretLine
		 *
		 * @param view of JComponent in use.
		 * @param reverse makes it possible 'swallow words' from caret moving towards
		 * start of line. In that way, words at start of line don't disturb - a function
		 * just uses the number of parameters it demands. 
		 * @return List with at least two items. Two main categories:
		 * 1. With selected text
		 * 	Two items - both being the selected text
		 * 
		 * 2. With no text selected
		 * 	  - Words of line, space delimited, becomes items - suitable as reversed.
		 * 	  - Delimiting with more than one space is permitted and only count as one.
		 * 	  - Double quotes surrounded strings counts as one item and inside such 
		 *	    strings no space reduction occurs 
		 * 	  - Single quote is just a character
		 *
		 * @see macro cyberkiss 
		 */
		List backWordsOrSelected(View view, boolean reverse) {
			return backWordsOrSelected(view, reverse,null);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='backWordsOrSelected'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ List backWordsOrSelected(View view, boolean reverse,String pattern)<div class="subdesc">
		/**
		 * Get parameter from current line. In the following the string from start of 
		 * caretline to caret is called toCaretLine
		 *
		 * @param view of JComponent in use.
		 * @param reverse makes it possible 'swallow words' from caret moving towards
		 * start of line. In that way, words at start of line don't disturb - a function
		 * just uses the number of parameters it demands. 
		 * @param pattern is used for identication of correct syntax so functions not get 
		 * input from lines they shouldn't have being invoked on.
		 * @return List with at least two items. Two main categories:
		 * 1. With selected text
		 * 	Two items - both being the selected text
		 * 
		 * 2. With no text selected
		 * 	Item 0 is toCaretLine
		 * 	Item 1 and futher items depends on result of toCaretLine.matches(pattern)
		 and whether toCaretline is empty
		 * 		false or empty caretLine
		 * 			Item1="" - no futher items
		 * 		true: 
		 * 			- Words of line, space delimited, becomes items - suitable as reversed.
		 * 			- Delimiting with more than one space is permitted and only count as one.
		 * 			- Double quotes surrounded strings counts as one item and inside such 
		 *			  strings no space reduction occurs 
		 * 			- Single quote is just a character
		 *
		 * @see macro cyberkiss 
		 */
		List backWordsOrSelected(View view, boolean reverse,String pattern) {
			TextArea textArea = view.getTextArea();
			String argText = textArea.getSelectedText();
			List retval;
			if (null == argText) {
				int caretLinePos =textArea.getCaretPosition()-textArea.getLineStartOffset(textArea.getCaretLine());
				argText =textArea.getLineText(textArea.getCaretLine()).substring(0,caretLinePos);
				
				if (pattern==null || argText.matches(pattern)) {
					String line = argText.trim().replace("\t"," ").replaceAll(" +(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"," ");
					retval = new ArrayList(Arrays.asList(line.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")));
					if (reverse)
						Collections.reverse(retval);
				} else
					retval = new ArrayList(asList(""));
			} else
				retval = new ArrayList(asList(argText));
			retval.add(0,argText);
			return retval;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='password'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String password()<div class="subdesc">
		/**
		 * Calls password(14) with length default to 14
		 *
		 * @return password which length is 14.
		 */
		String password() {
			password(14);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='password'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String password(int length)<div class="subdesc">
		/**
		 * Makes a password, using random ASCII values between and inclusive from 33 to 95 
		 * The password is also copied to clipboard
		 * @param length of password
		 * @return password
		 */
		String password(int length) {
			String pw="";
			for (int i=0; i<length; i++) {
				byte b =33 + (93*Math.random()).intValue();
				pw += (char)b;
			}
			Registers.getRegister('$').setTransferable(new java.awt.datatransfer.StringSelection(pw));
			return pw;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='pa'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void pa(Object egg) <div class="subdesc">
		/**
		 * This, as ... filtered best used, activity log print chicken, is the naken system bootstrap for writting a print utiliy. 
		 */
		public void pa(Object egg) { print("..."+egg); 
		} //}}}
</div>
</div>


<p class='macro'><a class='pop' href'#' id='txtConvFunctions'>&nbsp;&#9656;&nbsp;</a>&nbsp;txtConvFunctions.bsh <span class='macroType'>(/startup)</span></p>
<div class="desc">
		/* :tabSize=4:indentSize=4:noTabs=false:
		 * :folding=explicit:collapseFolds=1: 
		 * The folding style is important for lookup facility of macro startup */ 
		
		 
		/**
		 * Utilities use by plugin TxtConv, direct or by beanshell script files.
		 */
		
<p class='function'><a class='pop' href='#' id='sitePages'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String sitePages()<div class="subdesc">
		/**
		 * javascript implementation of Jedit bufferswitcher like dropdown list data.
		 *
		 * @return String of javascript of bufferswitcher data
		 */
		String sitePages() {
			String sitePagesImpl(String baseDir, String baseUrl) { 
				List files = Beam(new File(baseDir),FileVisitor(new ArrayList(),baseDir,baseUrl,
					"f->{ "+
					"if (f.getName().endsWith(´.html´)) {"+
						"String relative = f.getPath().substring(arg2.length());"+
						"arg1.add(relative.replaceAll(´^.+/´,´´)+´ (´+arg3+relative.replaceAll(´/[^/]+$´,´´)+´/)´);"+
					"}"+
					"return true; }"),1).toList(); 
				
				return
					 "<script>\n"
					+"(function( glObj) {\n"
					+"\tglObj.pages = [\n"
					+"\t \""+String.join("\"\n\t,\"",toArray(files))+"\"\n"
					+"\t];\n"
					+"}( window.myGlb = window.myGlb || {}));\n"
					+"</script>";
			} 
			return sitePagesImpl(mdBaseDir(),mdBaseUrl());
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='pageheading'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ StringBuilder pageheading(String scriptFileName, String begPatt, String endPatt, String topLines, String leadAppend)<div class="subdesc">
		/**
		 * Markdown page heading with included, pattern delimited, block of lines. Used for presenting code as markdown.   
		 *
		 * @param scriptFileName of file which pattern surounded section shall be returned 
		 * @param begPatt of lines to be returned
		 * @param endPatt of lines to be retuned
		 * @param topLines initial lines to be returned.
		 * @param leadAppend for every line - eg. use "\t" if some lines isn't prefixed   
		 * @return StringBuilder of lines of code to be shown
		 */
		StringBuilder pageheading(String scriptFileName, String begPatt, String endPatt, String topLines, String leadAppend) {
					
			StringBuilder lines = new StringBuilder().append(topLines).append('\n');
			boolean inBlk=false;
			for (String line: readAllLines(scriptFileName)) 
				if (inBlk)
					if (line.trim().startsWith(endPatt)) {
						inBlk=false;
						break;
					} else 
						lines.append(leadAppend).append(line).append('\n');
				else
					if (line.trim().startsWith(begPatt))
						inBlk=true;
			return lines.append("\n");
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='mdBaseDir'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String mdBaseDir()<div class="subdesc">
		/**
		 * Single source of plugin txtConv basedir for markdown based site
		 *
		 * @return filesystem root path of basedir
		 */
		String mdBaseDir() { 
			return jEdit.getProperty("options.txtconv.basedir");
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='mdBaseUrl'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String mdBaseUrl()<div class="subdesc">
		/**
		 * Single source of plugin txtConv baseurl for markdown based site
		 *
		 * @return filesystem root path of basedir
		 */
		String mdBaseUrl() { 
			return jEdit.getProperty("options.txtconv.baseurl");
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='linksReferencesAndLinks'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String linksReferencesAndLinks(boolean repeatRefsAsLinks,boolean includeHashLinks, int maxDepth)<div class="subdesc">
		/**
		 * First a block of Markdown link reference definitions created by traversing baseDir to a depth of parameter maxDepth (-1=unlimited). 
		 * All files with suffix md|jpg|png|gif   
		 * becomes a link reerence definition which label is the file's barename without suffix and its destination link 
		 * a baseUrl prefix to baseDir relative reference. The definitions is trailed with a link title if it appears in file 
		 * linksreferencesTitlesFileName() 
		 * After that, depent on repeatRefsAsLinks, an alhpabetic ordered markdown list of those links
		 *
		 * @param repeatRefsAsLinks makes list of markdown links follow list referenes block
		 * @param recDepthMax limits depth of recursion
		 * @return String, beeing first, lines of a  block of markdown link reference definitions, 
		 * and thereafter, depent on repeatRefsAsLinks, an alhpabetic ordered markdown list of those links  
		 */
		String linksReferencesAndLinks(boolean repeatRefsAsLinks,boolean includeHashLinks, int maxDepth) {
			
			fileVisitor(TreeMap files, int relpartPos,String baseUrl, boolean includeHashLinks) {
				boolean continueVisitFile(File f) {
					if (f.getName().matches(".+\\.(md|jpg|png|gif)")) {
						String relative = f.getPath().substring(relpartPos);
						boolean isMd = f.getName().endsWith(".md"); 
						files.put(f.getName().replaceAll("\\.\\w+$",""),baseUrl
							+ (isMd
								? relative.replaceAll("\\.\\w+$",".html")
								: relative));
							if (isMd && includeHashLinks) {
								Pattern anchors = Pattern.compile("<a[^>]+id=['\"](\\S+)['\"]");
								Matcher m = anchors.matcher(String.join(" ",toArray(readAllLines(f.getPath()))));
									while (m.find())
										files.put(m.group(1),baseUrl+relative.replaceAll("\\.\\w+$",".html")+"#"+m.group(1));
							}
					}
					return true;
				}
				return this;
			}
			String linkTitle(String propertyKey) {
				String title = titles.getProperty(propertyKey);
				return null==title 
					? ""
					: " '"+title+"'";
			}
			
			String baseDir = mdBaseDir();
			if (null==baseDir) {
				jEdit.getActiveView().getStatus().setMessage("Basedir not set");
				return "";
			}
			
			
			TreeMap files = new TreeMap();
			Properties titles = new Properties();
			File titlesFile = new File(linksreferencesTitlesFileName());
			if (titlesFile.exists())
				titles.load(new FileInputStream(titlesFile));
			
			walkFileTree(
				 new File(baseDir)
				,fileVisitor(files,baseDir.length()+1,mdBaseUrl(),includeHashLinks)
				,maxDepth);
			
			StringBuilder links = new StringBuilder();
			StringBuilder useList = new StringBuilder().append("\n\n");
			for (String s : files.keySet()) {
				links.append("[").append(s).append("]: ").append(files.get(s)).append(linkTitle(s)).append("\n");
				useList.append("- [").append(s).append("]\n");
			}
			return links.toString()+ (repeatRefsAsLinks ? useList.append("\n").toString() : "");
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='linksReferencesFollowedByLinks'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String linksReferencesFollowedByLinks()<div class="subdesc">
		/**
		 * First a block of Markdown link reference definitions created by traversing baseDir to a depth of 1. 
		 * All files with suffix md|jpg|png|gif becomes a link reerence definition which label is the file's barename 
		 * without suffix and its destination link a baseUrl prefix to baseDir relative reference. 
		 * The definitions is trailed with a link title if it appears in file linksreferencesTitlesFileName() 
		 * After that, an alhpabetic ordered markdown list of those links
		 *
		 * @return String, beeing first, lines of a  block of markdown link reference definitions, 
		 * and thereafter an alhpabetic ordered markdown list of those links  
		 */
		String linksReferencesFollowedByLinks() {
			return linksReferencesAndLinks(true,false,1);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='linksReferences'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String linksReferences()<div class="subdesc">
		/**
		 * Block of Markdown link reference definitions created by traversing baseDir to a depth of 3. 
		 * All files with suffix md|jpg|png|gif becomes a link reerence definition which label is the file's barename 
		 * without suffix and its destination link a baseUrl prefix to baseDir relative reference. 
		 * The definitions is trailed with a link title if it appears in file linksreferencesTitlesFileName() 
		 * 
		 * @return String, beeing lines of a block of markdown link reference definitions. 
		 */
		String linksReferences() {
			return linksReferencesAndLinks(false,false,3);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='linksreferencesTitlesFileName'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String linksreferencesTitlesFileName()<div class="subdesc">
		/**
		 * Single source of truth demanded lookup, for adding link titles in the link references definition list
		 * generated on each markdown compilation.
		 *
		 * @return file name of titles to links in the link references definition list
		 */
		String linksreferencesTitlesFileName() {
			return mdBaseDir()+"/linksreferencestitles.properties";
		} //}}}
</div>
</div>


<p class='macro'><a class='pop' href'#' id='functional'>&nbsp;&#9656;&nbsp;</a>&nbsp;functional.bsh <span class='macroType'>(/startup)</span></p>
<div class="desc">
		/* :tabSize=4:indentSize=4:noTabs=false:
		 * :folding=explicit:collapseFolds=1: 
		The folding style is important for lookup facility of macro startup */ 
		
		import org.gjt.sp.jedit.bsh.XThis;
		import java.util.stream.Collectors;
		import java.util.stream.Stream;
		
<p class='function'><a class='pop' href='#' id='implCode'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String implCode(String  implFunc, String iFaceMethod, String lambText)<div class="subdesc">
		/**
		 * Functionality being used in implemented interface functions objects of 
		 * Predicate, Mapper, Runnable, FileVisitor
		 * Can be used, under development to inspect the sourced implCode being made on run.
		 *
		 * Following two transistion makes it easier to represent Strings and embed regular expressions.
		 *
		 *  1. The "´" char is captured to represent double quotes. A statement, in normally code 
		 * 		str= "i am a string"
		 * becomes in stringified code
		 *		"str=´i am a string´"
		 * 
		 *  2. Backslaches becomes temporarily doubled to counteract the reduction when stringing strings.
		 *     In that way they can be written like in normal code.
		 *
		 * @param implFunc is the name of the function being runned from StringReader stream.
		 * @param iFaceMethod is signature of the interface function that java 8+ has to find, eg. boolean test(item) for predicates
		 * @param lambText is like the lambda text af java 8+, perhaps with some limitation. Parentheses around call parameter is optional.
		 * as in java 8+ a single expression does not need return statement, trailed semicolon or curly brackets sourroundings.
		 * This simple emulating of lambda has no mechanism for access to variables by name of called place - those 'effective finales' in java8+
		 * The woraround is five arguments, arg1,arg2,arg3,arg4,arg5 and overloading in those implementing function definitions to convenient numbers of parameters.
		 * In other words - inside the code block of lambText, arg1-arg5 can be used to access the parameters implFunc is called with.
		 * If such a parameter is an object, it can be accessed inside the lambText code - eg. method append() of StringBuffer to return 
		 * a String value. Assignment with arg1-arg5 as lvalue won't work - the inner mechanism of references must be taken in account.
		 * @return String occurence of method which name is given from parameter implFunc
		 */
		String implCode(String  implFunc, String iFaceMethod, String lambText) {
			String[] code = lambText.replaceAll("\\s*\\((.*)\\)\\s*->","$1->").split("->");
			boolean hasCurlies= code[1].matches("\\s*\\{.+\\}\\s*");
			return implFunc+"(arg1,arg2,arg3,arg4,arg5) { arg1() { return arg1; } " + iFaceMethod + "("+ code[0] + ") "
				+ ( hasCurlies ? "" :"{ return " )
				+ code[1].replaceAll("\\\\","\\\\\\\\").replaceAll("\"","\\\\\"").replaceAll("´","\"")
				+ ( hasCurlies ? "": ";}" ) + " return this; }";
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='FileVisitor'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis FileVisitor(arg1,arg2,arg3,arg4,arg5,lambText)<div class="subdesc">
		/**
		 * Returns a FileVisitor object  which implements boolean continueVisitFile(File file) 
		 *
		 * @param argX is five arguments that also can be used as persistent values among calls of interface function 
		 * @param lambText is the interface method parmeter list and method body source with a notation of java 8  -> operation
		 */
		XThis FileVisitor(arg1,arg2,arg3,arg4,arg5,lambText) {
			BeanShell.runScript(jEdit.getActiveView(),null
				,new StringReader(implCode("visitorImpl","boolean continueVisitFile", lambText)),false);
			return visitorImpl(arg1,arg2,arg3,arg4,arg5);
		}
		XThis FileVisitor(lambText) { return FileVisitor(0,0,0,0,0,lambText); }
		XThis FileVisitor(arg1,lambText) { return FileVisitor(arg1,0,0,0,0,lambText); }
		XThis FileVisitor(arg1,arg2,lambText) { return FileVisitor(arg1,arg2,0,0,0,lambText); } 
		XThis FileVisitor(arg1,arg2,arg3,lambText) { return FileVisitor(arg1,arg2,arg3,0,0,lambText); } //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Predicate'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Predicate(arg1,arg2,arg3,arg4,arg5,lambText)<div class="subdesc">
		/**
		 * Return a Predicate object  which implements boolean test(T t) 
		 *
		 * @param argX is five arguments that also can be used as persistent values among calls of interface function 
		 * @param lambText is the interface method parmeter list and method body source with a notation of java 8  -> operation
		 * @return function object
		 */
		XThis Predicate(arg1,arg2,arg3,arg4,arg5,lambText) {
			BeanShell.runScript(jEdit.getActiveView(),null
				,new StringReader(implCode("predImpl","boolean test", lambText)),false);
			return predImpl(arg1,arg2,arg3,arg4,arg5);
		}
		// overloadet
		XThis Predicate(lambText) { return Predicate(0,0,0,0,0,lambText); }
		XThis Predicate(arg1,lambText) { return Predicate(arg1,0,0,0,0,lambText); }
		XThis Predicate(arg1,arg2,lambText) { return Predicate(arg1,arg2,0,0,0,lambText); } //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Mapper'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Mapper(arg1,arg2,arg3,arg4,arg5,lambText)<div class="subdesc">
		/**
		 * Returns a Mapper object which implements T apply(T t)
		 *
		 * @param argX is five arguments that also can be used as persistent values among calls of interface function 
		 * @param lambText is the interface method parmeter list and method body source with a notation of java 8  -> operation
		 * @return function object to be used for mapping Stream items to converted items.
		 */
		XThis Mapper(arg1,arg2,arg3,arg4,arg5,lambText) {
			BeanShell.runScript(jEdit.getActiveView(),null
				,new StringReader(implCode("mapImpl","apply", lambText)),false);
			return mapImpl(arg1,arg2,arg3,arg4,arg5);
		}
		
		// overloadet
		XThis Mapper(lambText) { return Mapper(0,0,0,0,0,lambText); } //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Runnable'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Runnable(implCode)<div class="subdesc">
		/**
		 * Returns an object which implement void run() 
		 *
		 * @param argX is five arguments that also can be used as persistent values among calls of interface function 
		 * @param lambText is the interface method parmeter list and method body source with a notation of java 8  -> operation
		 * @return function object
		 */
		XThis Runnable(arg1,arg2,arg3,arg4,arg5,lambText) {
			BeanShell.runScript(jEdit.getActiveView(),null
				,new StringReader(implCode("runnableImpl","void run", lambText)),false);
			return runnableImpl(arg1,arg2,arg3,arg4,arg5);
		}
		// overloadet
		XThis Runnable(lambText) { return Runnable(0,0,0,0,0,lambText); }
		XThis Runnable(arg1,lambText) { return Runnable(arg1,0,0,0,0,lambText); } //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Beam'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Beam(String[] strArr)<div class="subdesc">
		/**
		 * Short containment of Stream for reduced typing
		 *
		 * @param strArr constructs contained Stream
		 * @return Function object containing a java.util.stream.Stream reference as the parameter it is called with. 
		 */
		XThis Beam(String[] strArr) {
			return Beam(Arrays.asList(strArr));
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Beam'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Beam(List list)<div class="subdesc">
		/**
		 * Short containment of Stream for reduced typing
		 * @param list constructs contained Stream 
		 * @return Function object containing a java.util.stream.Stream reference as the parameter it is called with. 
		 */
		XThis Beam(List list) {
			return Beam(list.stream());
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Beam'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Beam(Stream stream)<div class="subdesc">
		/**
		 * Short containment of Stream for reduced typing
		 *
		 * @param stream of type java.util.stream.Stream
		 * @return Function object containing a java.util.stream.Stream reference as the parameter it is called with. 
		 */
		XThis Beam(Stream stream) {
			// Stream stream = list.stream();
			XThis self = this;
			XThis funcObj;
			
			XThis funcParm() { return funcObj; }
			
			XThis filter(arg1,arg2,arg3,arg4,arg5,String lambText) {
				stream = stream.filter(Predicate(arg1,arg2,arg3,arg4,arg5,lambText));
				return self;
			}
		
			XThis filter(arg1,arg2,String lambText) {
				return filter(arg1,arg2,0,0,0,lambText);
			}
			XThis filter(arg1,arg2,arg3,String lambText) {
				return filter(arg1,arg2,arg3,0,0,lambText);
			}
			XThis filter(arg1,String lambText) {
				return filter(arg1,0,0,0,0,lambText);
			}
			
			XThis filter(String lambText) {
				return filter(0,0,0,0,0,lambText);
			}
			
			XThis map(arg1,arg2,arg3,arg4,arg5,lambText) {
				funcObj = Mapper(arg1,arg2,arg3,arg4,arg5,lambText);
				stream = stream.map(funcObj);
				return self;
			}
		
			XThis map(arg1,arg2,arg3,lambText) {
				return map(arg1,arg2,arg3,0,0,lambText);
			}
			XThis map(arg1,arg2,lambText) {
				return map(arg1,arg2,0,0,0,lambText);
			}
			
			XThis map(arg1,lambText) {
				return map(arg1,0,0,0,0,lambText);
			}
		
			XThis map(lambText) {
				return map(0,0,0,0,0,lambText);
			}
			
			XThis sorted() {
				stream = stream.sorted();
				return self;
			}
			long count() {
				return stream.count();
			}
			List toList() {
				return stream.collect(Collectors.toList());
			}
			return this;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Beam'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Beam(File dir)<div class="subdesc">
		/**
		 * stream of java.io.File objects wrapped in a Beam
		 *
		 * @param dir, the root for walking
		 * @return the Beam that holds the stream of java.io.File objects
		 */
		XThis Beam(File dir) {
			return Beam(dir,FileVisitor(new ArrayList(),"item -> { arg1.add(item); return true; }"),-1);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Beam'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Beam(File dir, int maxDepth)<div class="subdesc">
		/**
		 * stream of java.io.File objects wrapped in a Beam
		 *
		 * @param dir, the root for walking
		 * @param maxDepth subdir descend count
		 * @return the Beam that holds the stream of java.io.File objects
		 */
		XThis Beam(File dir, maxDepth) {
			return Beam(dir,FileVisitor(new ArrayList(),"item -> { arg1.add(item); return true; }"),maxDepth);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='Beam'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis Beam(File dir, XThis fileVisitor, int maxDepth)<div class="subdesc">
		/**
		 * stream of java.io.File objects wrapped in a Beam
		 *
		 * @param dir, the root for walking
		 * @param fileVisitorLambda - the source that puts java.io.File objects on the list
		 *                            that becomes the stream.
		 * @param maxDepth subdir descend 
		 * @return the Beam that holds the stream of java.io.File objects
		 */
		XThis Beam(File dir, XThis fileVisitor, int maxDepth) {
			walkFileTree(dir,fileVisitor,maxDepth);
			return Beam(fileVisitor.arg1());
		} //}}}
</div>
</div>


<p class='macro'><a class='pop' href'#' id='svData'>&nbsp;&#9656;&nbsp;</a>&nbsp;svData.bsh <span class='macroType'>(/startup)</span></p>
<div class="desc">
		/* :tabSize=4:indentSize=4:noTabs=false:
		 * :folding=explicit:collapseFolds=1: 
		 * The folding style is important for lookup facility of macro startup */ 
		
		import org.jsoup.nodes.Document;
		import org.jsoup.Jsoup;
		import org.jsoup.select.Elements;
		import org.jsoup.nodes.Element;
		
<p class='function'><a class='pop' href='#' id='tabularLines'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ String tabularLines(List stringArraysList, int space)<div class="subdesc">
		/**
		 * Formats data to pure tabular column align text.
		 *
		 * @param stringArraysList List of String[]
		 * @param space between columns
		 * @return String of lines of column aligned text.
		 */
		String tabularLines(List stringArraysList, int space, String delim) {
			int[] maxColumnWidths = new int[stringArraysList.get(0).length];
			StringBuilder lines = new StringBuilder();
			for (String [] items : stringArraysList)
				for (int i=0; i<items.length;i++)
					if (maxColumnWidths[i] < items[i].length())
						maxColumnWidths[i]= items[i].length();
			for (String [] items : stringArraysList) { 
				String delimCopy="";
				for (int i=0; i<items.length;i++) {
					lines.append(delimCopy).append(items[i]).append(new String(new char[space+maxColumnWidths[i]-items[i].length()]).replace('\0',' '));
					delimCopy=delim;
				}
				lines.append("\n");
			}
			return lines.toString();
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='htmlTable'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface htmlTable(String url, int tableElementsIndex, int maxRows, String[] selFields)<div class="subdesc">
		/**
		 * Retrieves html table content, with and without thead and tbody elements. 
		 * Colums are identified by html content below thead or in row with th tag.
		 * Tables are fetched as one with table tag among potential several, so tables inside tables would proberbly make sense for inner tables - the outer 
		 * would concatenate the inners html text
		 *
		 * @param url of html page - use file:/// for local files. 
		 * @param tableElementsIndex the from 0 indexed number as it apears in DOM tree 
		 * @param maxRows to fetch. 0 means whole tbody IF thead exists - -1 for all rows 
		 * @param selFields contains the field names to select or null if all fields. If thead is absent selFields has no effect.
		 * @return List of String[] representing table rows of table data fields
		 */
		List htmlTable(String url, int tableElementsIndex, int maxRows, String[] selFields) {
			List records = new ArrayList();
			Element trParent;
			Elements trs;
			boolean[] selectedRows;
			String[] rec;
			int selColumnCount;
			
			void iterate_trToMaxRows() { // and init selected rows
				for (Element tr : trs) {
					if (tr.parent() == trParent) {
						if (maxRows-- != 0) {
							Elements tds = tr.children();
							if ((tr.parent().tagName().equalsIgnoreCase("thead") || tr.child(0).tagName().equalsIgnoreCase("th")) && null != selFields) {
								selectedRows = new boolean[tds.size()];
								List flds = Arrays.asList(selFields);
								for (int i=0; i < tds.size(); i++)
									if (flds.contains(tds.get(i).html())) {
										selectedRows[i]=true;
										selColumnCount++;
									}
							}
							rec = new String[null == selFields ? tds.size() : selColumnCount];
							
							int selI=0;
							for (int i=0; i < tds.size(); i++)
								if (null == selectedRows)
									rec[selI++] = tds.get(i).html();
								else
									if (selectedRows[i])
										rec[selI++]= tds.get(i).html();
							records.add(rec);
						} else return;
					}
				}
			}
			
			Document doc;
			if (url.matches("file:///.*"))
				doc= Jsoup.parse(new File(url.replaceAll("^file://","")), "UTF-8");
			else
				doc = Jsoup.connect(url).get();
			
			Elements tables=doc.getElementsByTag("table");
			
			if (tables.size() > tableElementsIndex) {
		
				trParent = tables.get(tableElementsIndex);
				trs = doc.getElementsByTag("tr");
				boolean tableWithoutTBODY=true;
					
				for (Element chld : trParent.children()) {
					if (chld.tagName().equalsIgnoreCase("thead") || chld.tagName().equalsIgnoreCase("tbody")) {
						trParent = chld;
						tableWithoutTBODY=false;
						iterate_trToMaxRows();
					}
				}
				if (tableWithoutTBODY) 
					iterate_trToMaxRows();
			}
			return records;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List List textAreaBlocksRanges(TextArea textArea, String beforeLinesPattern, String afterLinesPattern)<div class="subdesc">
		/**
		 * Get list of list of line numbers in textArea (current buffer).
		 * Line blocks are surrounded by beforeLinesPattern and afterLinesPattern 
		 *
		 * @param beforeLinesPattern
		 * @param afterLinesPattern
		 * @return list of lists of line numbers in textArea of current buffer surrounded by, exclusive, lines with patterns  
		 */
		List textAreaBlocksRanges(TextArea textArea, String beforeLinesPattern, String afterLinesPattern) {
			boolean inBlock=false;
			List range;
			List ranges=new ArrayList();
			int lineCount=textArea.getLineCount();
			for( int line=0; line < lineCount;line++) {
				String text = textArea.getLineText(line);
				if (inBlock) {
					inBlock = !text.matches(afterLinesPattern);
					if (inBlock)
						range.add(line);
					else
						ranges.add(range);
				} else {
					inBlock = text.matches(beforeLinesPattern);
					if (inBlock)
						range = new ArrayList();
				}
			}
			return ranges;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='List'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ interface List dsvLines(String fileName, String table)<div class="subdesc">
		/**
		 * Transforms a block of lines in a file to a list of String[].
		 * The blocks is identified as one, potentially among several, named 'table'
		 * The chosen value (or field) delimiter is part of the pattern that identies the table.  
		 * An example: table foo consists of lines between the, not space prefixed, lines
		 * 		/***,sv,foo
		 * and
		 *		***/
		/** You could say that comma seperated values is ,sv and subsequent separated from the table name with that delimiter. 
		 * Conversion deal with removing text qualifier double quote surroundings and substitude there potential infields 
		 * occurrence as double double quote with single double quote. (the Microsoft invention)
		 * LINESHIFT IN FIELDS is not implemented.
		 *
		 * Empty lines result String [] af length of 1, having a String width a length of 0.
		 *
		 * @param fileName is the file from where the table is read
		 * @param table is the name of table to read
		 * @return List of String[] representing arrays of fields in a list of 'records'
		 */
		List  dsvLines(String fileName, String table) {
			StringBuffer delim= new StringBuffer();
			return Beam(Files.lines(new File(fileName).toPath()))
				.filter(false,delim,"item-> {"+
					"if ( arg1 == true )"+
					"    return !item.startsWith(´***/´) ?  true : (arg1=false);"+
					"if (true == (arg1 = item.matches(´/\\*\\*\\*.sv."+table+"´))) {"+
					"    arg2.append(item.replaceAll(´^/\\*\\*\\*(.)sv.+´,´$1´));}"+
					"return false; }")
				.map(delim,"item-> {"
					+"String[] items= item.split(arg1.toString()+´(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)´);"
					+"for(int i=0; i < items.length; i++) "
					+"    items[i]=items[i].trim().replaceAll(´^\"(.*)\"$´,´$1´).replace(´\"\"´,´\"´);"
					+"return items; }")
				.toList();	
		} //}}}
</div>
</div>


<p class='macro'><a class='pop' href'#' id='bshConsole'>&nbsp;&#9656;&nbsp;</a>&nbsp;bshConsole.bsh <span class='macroType'>(/startup)</span></p>
<div class="desc">
		/* :tabSize=4:indentSize=4:noTabs=false:
		 * :folding=explicit:collapseFolds=1: 
		 * The folding style is important for lookup facility of macro startup */ 
		
		import org.gjt.sp.jedit.bsh.XThis; 
		
		/** 
		 * Printing to dockable window of Beanshell shell of Plugin Console. 
		 */
		
<p class='function'><a class='pop' href='#' >&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{JComponent dockableW()<div class="subdesc">
		DockableWindowManager dockableWindowManager;
		/**
		 *	
		 *
		 * @return JComponent console dockableWindow
		 */
		JComponent consoleDW() {
			final String plName="console"; 
			if (null==super.dockableWindowManager)
				super.dockableWindowManager = jEdit.getActiveView().getDockableWindowManager();
			JComponent dAW = super.dockableWindowManager.getDockableWindow(plName);
			if (null == dAW) {
				super.dockableWindowManager.addDockableWindow(plName);
				dAW = super.dockableWindowManager.getDockableWindow(plName);
			}
			return dAW;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='println'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void println(Object thing)<div class="subdesc">
		/**
		 *  Print a line on plugin console's beanshell shell
		 *
		 * @param thing to print at a line
		 */ 
		void println(Object thing) {
			if (null==super.dockableWindowManager)
				super.dockableWindowManager = jEdit.getActiveView().getDockableWindowManager();
			while (!(dockableWindowManager.isDockableWindowVisible("console") && consoleDW().getShell().getName().equals("BeanShell"))) {
				new console.Shell.SwitchAction("BeanShell").invoke(jEdit.getActiveView());
			}
			consoleDW().getOutput().print(Color.black,""+thing);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='p'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void p(Object thing)<div class="subdesc">
		/**
		 * Print things on plugin console's beanshell shell, showing the decomposition using curly braces
		 *
		 * @param thing to print at a line
		 */ 
		void p(Object thing) {
			if (thing.getClass().isArray() || thing instanceof AbstractCollection )
				p(thing,spacer());
			else
				println(thing);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='spacer'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis spacer()<div class="subdesc">
		/**
		 * Used in the two p(...) methods to indent content as function a level of recursion  
		 *
		 * @return object with methods for indenting lines
		 */
		XThis spacer() {
			String apply(String line) {
				return prefix+line;
			}
			String apply(String line, boolean applyAfter) {
				if (applyAfter) {
					line  = prefix+line;
					prefix += "    ";
				} else 
					line = (prefix = prefix.substring(4))+line;
				return line;
			}
			String prefix="";
			return this;
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='p'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void p(Object thing,Object spacer)<div class="subdesc">
		/**
		 * Print things on plugin console's beanshell shell, showing things decomposition using curly braces
		 * This function is mainly thought of being used by p(thing)
		 *
		 * @param thing to be printed
		 * @param spacer to control indention of recursion
		 */
		void p(Object thing,Object spacer) {
			if (thing.getClass().isArray() || thing instanceof AbstractCollection ) {
				println(spacer.apply("{"+thing.getClass().getName(),true));
				for (Object line : thing)
					p(line,spacer);
				println(spacer.apply("}",false));
			} else 
				println(spacer.apply(""+thing));
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='pt'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void pt(Object thing)<div class="subdesc">
		/**
		 * Print things, timestamp prefixed, on plugin console's beanshell shell
		 *
		 * @param thing to be printet
		 */ 
		void pt(Object thing) {
			println(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss,SSS ")).toString()+thing);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='cls'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void cls() <div class="subdesc">
		/**
		 * Clears 'the screen' which is plugin console's beanshell shell.
		 */ 
		void cls() { 
			consoleDW().clear(); 
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='cls'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void cls(heading) <div class="subdesc">
		/**
		 * Clears 'the screen' which is plugin console's beanshell shell and 'prints' a headline
		 * Often used as cls(0), which is a convenient workaround for timestamp with millisecond resolution. 
		 *
		 * Due to that the thread that repaint content af Jtextarea not get thread of execution before invoked beanshell 
		 * functionality is finished, progress of running might not be visual detectable as latest result. If things have
		 * runned and output 'seems untouched' it is not possible to distinct visual between nothing have runned or invoked
		 * script have runned with same output.
		 * 
		 * A change in headline ensures that things had runned.
		 *
		 * @param thing to printed as headline
		 */ 
		void cls(Object thing) { 
			consoleDW().clear();
			if (thing instanceof Integer && thing == 0)
				println(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("K:mm:ss a,SSS ")).toString());
			else
				println(""+thing);
		} //}}}
</div>
</div>


<p class='macro'><a class='pop' href'#' id='runActionUtils'>&nbsp;&#9656;&nbsp;</a>&nbsp;runActionUtils.bsh <span class='macroType'>(/startup)</span></p>
<div class="desc">
		/* :tabSize=4:indentSize=4:noTabs=false:
		 * :folding=explicit:collapseFolds=1: 
		The folding style is important for lookup facility of macro startup */
		
		import org.gjt.sp.jedit.bsh.XThis;
		
		/**
		 * Utilities for macros actionsDialog, cyberkiss and startup
		 */
		 
<p class='function'><a class='pop' href='#' id='antTask'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void antTask(String task)<div class="subdesc">
		/**
		 * Runs ant target for that build.xml file being in current directory for Console's System shell or above.
		 * A macro - 'cd current' swiches to that of current buffer.
		 *
		 * @param target
		 * @param view
		 * @param buffer
		 */
		void antTask(String target,View view,Buffer buffer) {
			jEdit.saveAllBuffers(view);
			new console.Shell.SwitchAction("System").invoke(jEdit.getActiveView());
			wd=wdFirst=ux(jEdit.getPlugin("console.ConsolePlugin",true).getSystemShellVariableValue(view,"PWD")).replaceAll("/$","");
			while (!new File(wd+"/build.xml").exists() && wd.length()>0)  
				wd = wd.replaceAll("/[^/]+$","");
			if (!wd.equals(wdFirst) && wd.length() > 0)
				runCommandInConsole(view,"System","cd "+wd);
			runCommandInConsole(view,"System","ant common."+target);
			view.goToBuffer(buffer);
		} //}}}       
</div>
		
<p class='function'><a class='pop' href='#' id='runNode'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void runNode(view,buffer)<div class="subdesc">
		/**
		 * Runs Node.js against current buffer. 
		 *
		 * @param view
		 * @param buffer
		 */
		void runNode(View view,Buffer buffer) {
			jEdit.saveAllBuffers(view);	
			runCommandInConsole(view,"System","node "+buffer.getPath());
			view.goToBuffer(buffer);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='actionsDialog'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ void actionsDialog(XThis callBack, XThis menuSeq, View view)<div class="subdesc">
		/**
		 * JLabels grid to recieve key character invoked choice. Used by macro actionsDialog and cyberkiss
		 *
		 * @param callBack for return choice to caller from within keyTyped handler 
		 * @param menuSeq to read labels, shortcut keys and default actions from. 
		 * @param view containing component
		 */
		void actionsDialog(XThis callBack, XThis menuSeq, View view) {
			void addLabels() {
				buttonPanel.add(new JLabel("<html>(Esc to leave)</html>"));
				if (null != headLine) {
					buttonPanel.add(new JLabel("<html><b>"+headLine+"</b></html>"));
					buttonPanel.add(new JLabel("<html><br></html>"));
				}
				
				while ((labelText=menuSeq.readLabelText()) != null) {
					menuSize++;
					JLabel j = new JLabel(labelText);
					if (menuSeq.defIndex() == menuSize-1) {
						j.setBackground(Color.lightGray);
						j.setOpaque(true);
					}
					buttonPanel.add(j);
				}
			}
			dialog = new JDialog(view,true); //, title, false);
			dialog.setUndecorated(true);
		    content = new JPanel(new BorderLayout());
		    dialog.setContentPane(content);
		    dialog.addKeyListener(this);
		    buttonPanel = new JPanel();
			int menuSize;
		    String headLine = menuSeq.heading();
			addLabels();
		    buttonPanel.setLayout(new GridLayout(menuSize+1+(null != headLine ? 2 : 0),1));
			content.add(buttonPanel, "South");
			dialog.pack();
		    dialog.setLocationRelativeTo(view);
		    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		    public void keyTyped(KeyEvent e) {
		    	char c = e.getKeyChar();
		    	if (((byte)c)==27) 
		    		dialog.dispose();
		    	int indexOfKeyTyped = menuSeq.shCKeys().indexOf(c);
		    	if (indexOfKeyTyped > -1 || (menuSeq.defIndex() >= 0 && ( c ==' ' || c == '\n'))) {
		    		dialog.dispose();
		    		callBack.command((c != ' ' && c != '\n')
		    			? menuSeq.menuItem(indexOfKeyTyped)
		    			: "");
		    	}
		    }
		    public void keyPressed(KeyEvent e) {}
		    public void keyReleased(KeyEvent e) {}
		    dialog.setVisible(true);
		} //}}}
</div>
		
<p class='function'><a class='pop' href='#' id='NumSeq'>&nbsp;&#9656;&nbsp;</a>&nbsp;&nbsp;&nbsp;//{{{ XThis NumSeq(List menues,String headLine)<div class="subdesc">
		/**
		 * Skeleton for a parameter object to actionsDialog. This example shows how NumSeq is used.
		 *
		 *  void actionsDialogExample() {
		 *  	command(String chosen) {
		 *  		p(chosen);
		 *  	}
		 *  	actionsDialog(this,NumSeq(Arrays.asList(new String[]{
		 *  		 "abra"
		 *  		,"kadabra"
		 *  		,"pif"
		 *  		,"paf"
		 *  		,"puf"
		 *  		}),"print me"),view);
		 *  }
		 * 
		 * As an example of the flexibility it gives to have some part of actionsDialog's functionality parameterized, 
		 * look at how actionsDialog is used in macro actionsDialog contra in macro Java/cyberkiss
		 *
		 * @param menues is a List of strings that becomes the shorcut key profixed choices to chose among
		 * @param headline shows a context at top as a reminder/check about what the choices has relevance for.
		 * @return XThis object with methods to retrieve headings, shortcut keys, label text and menues. 
		 */
		XThis NumSeq(List menues,String headLine) {
			int ix;
			String sCKeys="";
			String heading() { return headLine; }
			String shCKeys() { return sCKeys; } 
			String readLabelText() {
				if (ix == menues.size())
					return null;
				String sC=Character.toString((char)(ix+49+(ix>8 ? 39 : 0)));
				sCKeys += sC;
				return "<html><u>"+ sC +"</u> "+menues.get(ix++)+"</html>";
			}
			int defIndex() { return -2; }
			String menuItem(int index) {
				return menues.get(index);
			}
			return this;
		}
		//}}}
</div>
</div>

