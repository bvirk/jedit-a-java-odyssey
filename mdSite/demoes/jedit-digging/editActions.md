<!-- head=siteJedit.css.html+adjustmargin.css+jquery.html+prettify.html+prettyClassify.js+sitePages()+eventsAndNav.js & body=pageJEdit.md & title=macros+|+startups -->
### Action Sets


	StringBuilder lines = new StringBuilder();	
	String[] permittedAS = {"Built-in Commands","Plugin: TxtConvPlugin","Plugin: Console","Docking Layouts"};
		
	for (ActionSet as : jEdit.getActionSets()) {
		if (Arrays.asList(permittedAS).contains(as.getLabel())) {
			
			//fLines.append("- <a href=\"#").append(as.getLabel()).append("\">").append(as.getLabel()).append("</a>\n");
			fLines.append("- [").append(as.getLabel()).append("]\n");
			
			lines.append("<a id=\"").append(as.getLabel().replaceAll("\\W","")).append("\"></a>\n### " ).append(as.getLabel()).append("\n\n");
			for (EditAction ea : as.getActions()) 
				lines.append("\t<ACTION NAME=\"").append(ea.getLabel()).append("\">\n\t\t<CODE>\n\t\t\t")
					 .append(ea.getCode()).append("\n\t\t</CODE>\n\t</ACTION>\n");
			lines.append("\n---\n");
		}
	}

- [Built-in Commands]
- [Plugin: Console]
- [Docking Layouts]


<a id="BuiltinCommands"></a>
### Built-in Commands

	<ACTION NAME="Toggle Page Break Marker">
		<CODE>
			jEdit.setBooleanProperty("view.pageBreaks", !jEdit.getBooleanProperty("view.pageBreaks"));
	EditBus.send(new PropertiesChanged(null));
		</CODE>
	</ACTION>
	<ACTION NAME="$About jEdit...">
		<CODE>
			new AboutDialog(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$Update Activity Log on Disk">
		<CODE>
			Log.flushStream();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Text Area">
		<CODE>
			editPane.focusOnTextArea();
		</CODE>
	</ACTION>
	<ACTION NAME="Select Previous Paragraph">
		<CODE>
			textArea.goToPrevParagraph(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Matching Bracket">
		<CODE>
			textArea.goToMatchingBracket();
		</CODE>
	</ACTION>
	<ACTION NAME="Select to Last Visible Line">
		<CODE>
			textArea.goToLastVisibleLine(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Delete Next Word (Eat Whitespace)">
		<CODE>
			textArea.deleteWord(true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Task Monitor">
		<CODE>
			view.getDockableWindowManager().showDockableWindow("task-monitor");
		</CODE>
	</ACTION>
	<ACTION NAME="Delete Previous Word (Eat Whitespace after word)">
		<CODE>
			textArea.backspaceWord(true, true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select $Paragraph">
		<CODE>
			textArea.selectParagraph();
		</CODE>
	</ACTION>
	<ACTION NAME="Overwrite Mode">
		<CODE>
			textArea.toggleOverwriteEnabled();
		</CODE>
	</ACTION>
	<ACTION NAME="Paste $Register">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getRegisterStatusPrompt("paste-string-register"),
			"Registers.paste(textArea,__char__,false);");
		</CODE>
	</ACTION>
	<ACTION NAME="Plugin $Options...">
		<CODE>
			new org.jedit.options.CombinedOptions(view, 1);
		</CODE>
	</ACTION>
	<ACTION NAME="$Select Fold">
		<CODE>
			textArea.selectFold();
		</CODE>
	</ACTION>
	<ACTION NAME="Na$rrow to Fold">
		<CODE>
			textArea.narrowToFold();
		</CODE>
	</ACTION>
	<ACTION NAME="$Buffer Options...">
		<CODE>
			new BufferOptions(view,buffer);
		</CODE>
	</ACTION>
	<ACTION NAME="Line Scroll $Down">
		<CODE>
			textArea.scrollDownLine();
		</CODE>
	</ACTION>
	<ACTION NAME="$Invert Selection">
		<CODE>
			textArea.invertSelection();
		</CODE>
	</ACTION>
	<ACTION NAME="HyperSearch Results (New Floating Instance)">
		<CODE>
			view.getDockableWindowManager().floatDockableWindow("hypersearch-results");
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Next Character">
		<CODE>
			textArea.goToNextCharacter(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Select $Line">
		<CODE>
			textArea.selectLine();
		</CODE>
	</ACTION>
	<ACTION NAME="Expand Fo$lds With Level">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getProperty("view.status.expand-folds"),
			"textArea.getDisplayManager().expandFolds(__char__);\n"
			+ "textArea.scrollToCaret(false);");
		</CODE>
	</ACTION>
	<ACTION NAME="$Cut">
		<CODE>
			Registers.cut(textArea,'$');
		</CODE>
	</ACTION>
	<ACTION NAME="Select to Start of Line">
		<CODE>
			textArea.goToStartOfLine(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Narro$w to Selection">
		<CODE>
			textArea.narrowToSelection();
		</CODE>
	</ACTION>
	<ACTION NAME="Select to Start of Buffer">
		<CODE>
			textArea.goToBufferStart(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Previous Fold">
		<CODE>
			textArea.goToPrevFold(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Tabs to Spaces">
		<CODE>
			textArea.tabsToSpaces();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to First Visible Line">
		<CODE>
			textArea.goToFirstVisibleLine(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Set view title...">
		<CODE>
			view.showUserTitleDialog();
		</CODE>
	</ACTION>
	<ACTION NAME="New in $Mode">
		<CODE>
			Mode[] modes = jEdit.getModes();
		Mode currentMode = buffer.getMode();
		String dialogTitle = jEdit.getProperty("new-file-in-mode.dialog.title");
		String dialogMessage = jEdit.getProperty("new-file-in-mode.dialog.message");
		Mode choice = (Mode)JOptionPane.showInputDialog(
			view,
			dialogMessage,
			dialogTitle,
			JOptionPane.QUESTION_MESSAGE,
			null,
			modes,
			currentMode);
		if(choice != null) {
			Buffer newBuffer = jEdit.newFile(view);
			view.goToBuffer(newBuffer);
			newBuffer.setMode(choice);
		}
		</CODE>
	</ACTION>
	<ACTION NAME="null">
		<CODE>
			textArea.goToPrevMarker(true);
		</CODE>
	</ACTION>
	<ACTION NAME="$File System Browser (Toggle)">
		<CODE>
			view.getDockableWindowManager().toggleDockableWindow("vfs.browser");
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle $Buffer Switcher">
		<CODE>
			b = !jEdit.getBooleanProperty("view.showBufferSwitcher");
    jEdit.setBooleanProperty("view.showBufferSwitcher", b);
    view.updateBufferSwitcherStates();
    if (b) editPane.focusBufferSwitcher();
		</CODE>
	</ACTION>
	<ACTION NAME="Load docking layout of current mode">
		<CODE>
			DockingLayoutManager.loadCurrentModeLayout(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Fi$nd Next">
		<CODE>
			SearchAndReplace.setReverseSearch(false);
		SearchAndReplace.find(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Previous Line">
		<CODE>
			textArea.goToPrevLine(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Resca$n Macros">
		<CODE>
			Macros.loadMacros();
		</CODE>
	</ACTION>
	<ACTION NAME="$Go to Marker">
		<CODE>
			view.getInputHandler().readNextChar(
			buffer.getMarkerStatusPrompt("goto-marker"),
			"editPane.goToMarker(__char__,false);");
		</CODE>
	</ACTION>
	<ACTION NAME="Insert Newline and Indent">
		<CODE>
			textArea.insertEnterAndIndent();
		</CODE>
	</ACTION>
	<ACTION NAME="Select Previous Word">
		<CODE>
			textArea.goToPrevWord(true,false);
		</CODE>
	</ACTION>
	<ACTION NAME="Collapse $All Folds">
		<CODE>
			textArea.getDisplayManager().expandFolds(1);
		</CODE>
	</ACTION>
	<ACTION NAME="New Macro">
		<CODE>
			dialog = new VFSFileChooserDialog(view,
			jEdit.getSettingsDirectory()+File.separator+"macros"+
			File.separator+"New_Macro.bsh", VFSBrowser.SAVE_DIALOG,
			false);
		files = dialog.getSelectedFiles();
		if (files == null || files.length != 1) return;
		b = jEdit.newFile(view);
		b.save(view, files[0]);
		</CODE>
	</ACTION>
	<ACTION NAME="null">
		<CODE>
			textArea.goToNextMarker(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle Word Wrap">
		<CODE>
			buffer.toggleWordWrap(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Add Marker $With Shortcut">
		<CODE>
			view.getInputHandler().readNextChar(
			buffer.getMarkerStatusPrompt("add-marker"),
			"buffer.addMarker(__char__,textArea.getCaretPosition());");
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Next Word">
		<CODE>
			textArea.goToNextWord(false,false);
		</CODE>
	</ACTION>
	<ACTION NAME="Select $All">
		<CODE>
			textArea.selectAll();
		</CODE>
	</ACTION>
	<ACTION NAME="Delete $Paragraph">
		<CODE>
			textArea.deleteParagraph();
		</CODE>
	</ACTION>
	<ACTION NAME="$Add/Remove Marker">
		<CODE>
			editPane.addMarker();
		</CODE>
	</ACTION>
	<ACTION NAME="Record $Temporary Macro">
		<CODE>
			Macros.recordTemporaryMacro(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Expand $Fold Fully">
		<CODE>
			textArea.expandFold(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle $Line Numbers">
		<CODE>
			boolean shown = jEdit.getBooleanProperty("view.gutter.lineNumbers");
		jEdit.setBooleanProperty("view.gutter.lineNumbers", !shown);
		if (! shown)
			jEdit.setBooleanProperty("view.gutter.enabled", !shown);
		jEdit.propertiesChanged();
		</CODE>
	</ACTION>
	<ACTION NAME="C$opy Append">
		<CODE>
			Registers.append(textArea,'$',"\n",false);
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle S$tatus Bar">
		<CODE>
			boolean showing = view.getStatus().isShowing();
		if (view.isPlainView())
		{
			jEdit.setBooleanProperty("view.status.plainview.visible", !showing);
		}
		else
		{
			jEdit.setBooleanProperty("view.status.visible", !showing);
		}
		jEdit.propertiesChanged();
		</CODE>
	</ACTION>
	<ACTION NAME="$Multiple Selection">
		<CODE>
			textArea.toggleMultipleSelectionEnabled();
		</CODE>
	</ACTION>
	<ACTION NAME="$Open...">
		<CODE>
			GUIUtilities.showVFSFileDialog(view,null,VFSBrowser.BROWSER_DIALOG,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Previous Marker">
		<CODE>
			editPane.goToPrevMarker(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Delete Lines">
		<CODE>
			textArea.deleteLine();
		</CODE>
	</ACTION>
	<ACTION NAME="Page Scroll U$p">
		<CODE>
			textArea.scrollUpPage();
		</CODE>
	</ACTION>
	<ACTION NAME="$Global Scope BufferSets">
		<CODE>
			bufferSetManager = jEdit.getBufferSetManager();
		bufferSetManager.setScope(BufferSet.Scope.global);
		</CODE>
	</ACTION>
	<ACTION NAME="$Go to Line...">
		<CODE>
			textArea.showGoToLineDialog();
		</CODE>
	</ACTION>
	<ACTION NAME="$Load docking layout ...">
		<CODE>
			DockingLayoutManager.load(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$File System Browser (New Floating Instance)">
		<CODE>
			view.getDockableWindowManager().floatDockableWindow("vfs.browser");
		</CODE>
	</ACTION>
	<ACTION NAME="Select to Start of White Space">
		<CODE>
			textArea.goToStartOfWhiteSpace(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select to End of Buffer">
		<CODE>
			textArea.goToBufferEnd(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Replace $All">
		<CODE>
			SearchAndReplace.replaceAll(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$Spaces to Tabs">
		<CODE>
			textArea.spacesToTabs();
		</CODE>
	</ACTION>
	<ACTION NAME="E$xpand All Folds">
		<CODE>
			textArea.getDisplayManager().expandAllFolds();
		textArea.scrollToCaret(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Run $Other Macro...">
		<CODE>
			Macros.showRunScriptDialog(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Ignore $Case">
		<CODE>
			SearchAndReplace.setIgnoreCase(!SearchAndReplace.getIgnoreCase());
		</CODE>
	</ACTION>
	<ACTION NAME="$Vertical Paste">
		<CODE>
			Registers.paste(textArea,'$',true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Activity Log (New Floating Instance)">
		<CODE>
			view.getDockableWindowManager().floatDockableWindow("log-viewer");
		</CODE>
	</ACTION>
	<ACTION NAME="I$ncremental Search for Word">
		<CODE>
			view.quickIncrementalSearch(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Previous Paragraph">
		<CODE>
			textArea.goToPrevParagraph(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Delete Previous Word">
		<CODE>
			textArea.backspaceWord();
		</CODE>
	</ACTION>
	<ACTION NAME="$Evaluate BeanShell Expression...">
		<CODE>
			BeanShell.showEvaluateDialog(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Co$py to Register">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getRegisterStatusPrompt("copy-string-register"),
			"Registers.copy(textArea,__char__);");
		</CODE>
	</ACTION>
	<ACTION NAME="Scroll $to Current Line">
		<CODE>
			view.closeAllMenus();
		if(!textArea.getDisplayManager().isLineVisible(
			textArea.getCaretLine()))
		{
			textArea.getDisplayManager().expandFold(
				textArea.getCaretLine(),true);
		}
		textArea.scrollToCaret(true);
		textArea.requestFocus();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Last Visible Line">
		<CODE>
			textArea.goToLastVisibleLine(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Activity Log">
		<CODE>
			view.getDockableWindowManager().showDockableWindow("log-viewer");
		</CODE>
	</ACTION>
	<ACTION NAME="Sele$ct Line Range...">
		<CODE>
			new SelectLineRange(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Vie$w Registers (Toggle)">
		<CODE>
			view.getDockableWindowManager().toggleDockableWindow("view-registers");
		</CODE>
	</ACTION>
	<ACTION NAME="$Format Paragraph">
		<CODE>
			textArea.formatParagraph();
		</CODE>
	</ACTION>
	<ACTION NAME="Paste Previo$us...">
		<CODE>
			new PasteFromListDialog("paste-previous",view,
			HistoryModel.getModel("clipboard"));
		</CODE>
	</ACTION>
	<ACTION NAME="$Reload Edit Modes">
		<CODE>
			jEdit.reloadModes();
		</CODE>
	</ACTION>
	<ACTION NAME="Delete to $End of Line">
		<CODE>
			textArea.deleteToEndOfLine();
		</CODE>
	</ACTION>
	<ACTION NAME="$Options...">
		<CODE>
			return org.jedit.options.CombinedOptions.combinedOptions(view);
		</CODE>
	</ACTION>
	<ACTION NAME="H$yperSearch for Word">
		<CODE>
			view.quickHyperSearch(true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Paste">
		<CODE>
			EditBus.send(new PositionChanging(editPane));
		Registers.paste(textArea,'$',false);
		</CODE>
	</ACTION>
	<ACTION NAME="Page Scroll D$own">
		<CODE>
			textArea.scrollDownPage();
		</CODE>
	</ACTION>
	<ACTION NAME="$Task Monitor (Toggle)">
		<CODE>
			view.getDockableWindowManager().toggleDockableWindow("task-monitor");
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Top Docking Area">
		<CODE>
			wm.getTopDockingArea().showMostRecent();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Next Text Area">
		<CODE>
			view.nextTextArea();
		</CODE>
	</ACTION>
	<ACTION NAME="Reloa$d All">
		<CODE>
			jEdit.reloadAllBuffers(view,true);
		</CODE>
	</ACTION>
	<ACTION NAME="HyperSearch Results (Toggle)">
		<CODE>
			view.getDockableWindowManager().toggleDockableWindow("hypersearch-results");
		</CODE>
	</ACTION>
	<ACTION NAME="$Undo">
		<CODE>
			buffer.undo(textArea);
		</CODE>
	</ACTION>
	<ACTION NAME="Delete Previous Character">
		<CODE>
			textArea.backspace();
		</CODE>
	</ACTION>
	<ACTION NAME="$New">
		<CODE>
			jEdit.newFile(view);
		</CODE>
	</ACTION>
	<ACTION NAME="End">
		<CODE>
			textArea.end(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Search in $Directory...">
		<CODE>
			SearchDialog.showSearchDialog(view,textArea.getSelectedText(),
			SearchDialog.DIRECTORY);
		</CODE>
	</ACTION>
	<ACTION NAME="Shift Indent $Right">
		<CODE>
			textArea.shiftIndentRight();
		</CODE>
	</ACTION>
	<ACTION NAME="Swa$p Caret and Marker">
		<CODE>
			view.getInputHandler().readNextChar(
			buffer.getMarkerStatusPrompt("swap-marker"),
			"editPane.swapMarkerAndCaret(__char__);");
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Next Page">
		<CODE>
			textArea.goToNextPage(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Close O$thers">
		<CODE>
			boolean closeAll = true;
		if (jEdit.getBooleanProperty("closeAllConfirm"))
		{
			int answer = GUIUtilities.confirm(view, "closeothers", null, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
			closeAll = answer == javax.swing.JOptionPane.YES_OPTION;
		}
		if (closeAll)
		{
			org.gjt.sp.jedit.bufferset.BufferSet bufferSet = editPane.getBufferSet();
			Buffer[] buffers = bufferSet.getAllBuffers();
			for (Buffer buff:buffers)
			{
				if (buff != buffer)
					jEdit.closeBuffer(editPane,buff);
			}
		}
		</CODE>
	</ACTION>
	<ACTION NAME="Home">
		<CODE>
			textArea.home(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Print...">
		<CODE>
			BufferPrinter1_7.print(view, buffer);
		</CODE>
	</ACTION>
	<ACTION NAME="Shift Indent $Left">
		<CODE>
			textArea.shiftIndentLeft();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Start of White Space">
		<CODE>
			textArea.goToStartOfWhiteSpace(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$HyperSearch Bar">
		<CODE>
			view.quickHyperSearch(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Copy $Append to Register">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getRegisterStatusPrompt("copy-append-string-register"),
			"Registers.append(textArea,__char__,\"\\n\",false);");
		</CODE>
	</ACTION>
	<ACTION NAME="Select Previous Page">
		<CODE>
			textArea.goToPrevPage(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select Next Page">
		<CODE>
			textArea.goToNextPage(true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Word Count...">
		<CODE>
			textArea.showWordCountDialog();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Previous Buffer">
		<CODE>
			editPane.prevBuffer();
		</CODE>
	</ACTION>
	<ACTION NAME="Paste Special...">
		<CODE>
			new PasteSpecialDialog(view, textArea);
		</CODE>
	</ACTION>
	<ACTION NAME="E$xpand Abbreviation">
		<CODE>
			Abbrevs.expandAbbrev(view,true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Go to Parent Fold">
		<CODE>
			textArea.goToParentFold();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Previous Page">
		<CODE>
			textArea.goToPrevPage(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Select to Smart Home Position">
		<CODE>
			textArea.smartHome(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select Next Character">
		<CODE>
			textArea.goToNextCharacter(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Focus on Buffer S$witcher">
		<CODE>
			editPane.focusBufferSwitcher();
		</CODE>
	</ACTION>
	<ACTION NAME="$Close View">
		<CODE>
			jEdit.closeView(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Insert Tab">
		<CODE>
			textArea.userInput('\t');
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Next Bracket">
		<CODE>
			textArea.goToNextBracket(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$View Scope BufferSets">
		<CODE>
			bufferSetManager = jEdit.getBufferSetManager();
		bufferSetManager.setScope(BufferSet.Scope.view);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Next Marker">
		<CODE>
			editPane.goToNextMarker(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Smart End">
		<CODE>
			textArea.smartEnd(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Line Comment">
		<CODE>
			textArea.lineComment();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Previous Word (Eat Whitespace)">
		<CODE>
			textArea.goToPrevWord(false,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Run Tem$porary Macro">
		<CODE>
			Macros.runTemporaryMacro(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Previous Word (Eat Whitespace after word)">
		<CODE>
			textArea.goToPrevWord(false,true,true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Cut Append">
		<CODE>
			Registers.append(textArea,'$',"\n",true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select Previous Line">
		<CODE>
			textArea.goToPrevLine(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Remove all errors">
		<CODE>
			Log.throwables.clear();
		</CODE>
	</ACTION>
	<ACTION NAME="$File System Browser">
		<CODE>
			view.getDockableWindowManager().showDockableWindow("vfs.browser");
		</CODE>
	</ACTION>
	<ACTION NAME="Insert Newline">
		<CODE>
			textArea.userInput('\n');
		</CODE>
	</ACTION>
	<ACTION NAME="S$ave As...">
		<CODE>
			buffer.saveAs(view,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select $Word">
		<CODE>
			textArea.selectWord();
		</CODE>
	</ACTION>
	<ACTION NAME="Find Pre$vious">
		<CODE>
			SearchAndReplace.setReverseSearch(true);
		SearchAndReplace.find(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Show Context Menu">
		<CODE>
			textArea.showPopupMenu();
		</CODE>
	</ACTION>
	<ACTION NAME="$Collapse Fold">
		<CODE>
			textArea.collapseFold();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Previous Text Area">
		<CODE>
			view.prevTextArea();
		</CODE>
	</ACTION>
	<ACTION NAME="$Close Current Docking Area">
		<CODE>
			wm.closeCurrentArea();
		</CODE>
	</ACTION>
	<ACTION NAME="Save docking layout of current mode">
		<CODE>
			DockingLayoutManager.saveCurrentModeLayout(view);
		</CODE>
	</ACTION>
	<ACTION NAME="E$xit">
		<CODE>
			jEdit.exit(view,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Start of Line">
		<CODE>
			textArea.goToStartOfLine(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Save A$ll...">
		<CODE>
			jEdit.saveAllBuffers(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to End of Buffer">
		<CODE>
			textArea.goToBufferEnd(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle $Gutter">
		<CODE>
			boolean showing = jEdit.getBooleanProperty("view.gutter.enabled");
		jEdit.setBooleanProperty("view.gutter.enabled", !showing);
		jEdit.propertiesChanged();
		</CODE>
	</ACTION>
	<ACTION NAME="$Expand Fold One Level">
		<CODE>
			textArea.expandFold(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Regular E$xpressions">
		<CODE>
			SearchAndReplace.setRegexp(!SearchAndReplace.getRegexp());
		</CODE>
	</ACTION>
	<ACTION NAME="Vie$w Markers">
		<CODE>
			view.getDockableWindowManager().showDockableWindow("view-markers");
		</CODE>
	</ACTION>
	<ACTION NAME="Select to End of White Space">
		<CODE>
			textArea.goToEndOfWhiteSpace(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Bottom Docking Area">
		<CODE>
			wm.getBottomDockingArea().showMostRecent();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to End of Line">
		<CODE>
			textArea.goToEndOfLine(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Next Line">
		<CODE>
			textArea.goToNextLine(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Save">
		<CODE>
			buffer.save(view,null,true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Unsplit Current">
		<CODE>
			view.unsplitCurrent();
		</CODE>
	</ACTION>
	<ACTION NAME="$Task Monitor (New Floating Instance)">
		<CODE>
			view.getDockableWindowManager().floatDockableWindow("task-monitor");
		</CODE>
	</ACTION>
	<ACTION NAME="Select to First Visible Line">
		<CODE>
			textArea.goToFirstVisibleLine(true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Center Caret on Screen">
		<CODE>
			textArea.centerCaret();
		</CODE>
	</ACTION>
	<ACTION NAME="$Indent Lines">
		<CODE>
			textArea.indentSelectedLines();
		</CODE>
	</ACTION>
	<ACTION NAME="HyperSearch Results">
		<CODE>
			view.getDockableWindowManager().showDockableWindow("hypersearch-results");
		</CODE>
	</ACTION>
	<ACTION NAME="Select Next Paragraph">
		<CODE>
			textArea.goToNextParagraph(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle $full-screen mode">
		<CODE>
			view.toggleFullScreen();
		</CODE>
	</ACTION>
	<ACTION NAME="$Scroll and Center Caret">
		<CODE>
			view.closeAllMenus();
   	textArea.scrollAndCenterCaret();
		</CODE>
	</ACTION>
	<ACTION NAME="Unsplit $All">
		<CODE>
			view.unsplit();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to End of White Space">
		<CODE>
			textArea.goToEndOfWhiteSpace(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Co$mplete Word">
		<CODE>
			CompleteWord.completeWord(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$Close">
		<CODE>
			jEdit.closeBuffer(editPane,buffer);
		</CODE>
	</ACTION>
	<ACTION NAME="C$opy">
		<CODE>
			Registers.copy(textArea,'$');
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Next Fold">
		<CODE>
			textArea.goToNextFold(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Select Previous Word (Eat Whitespace)">
		<CODE>
			textArea.goToPrevWord(true,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Close All (global)">
		<CODE>
			boolean closeAll = true;
		if (jEdit.getBooleanProperty("closeAllConfirm"))
		{
			int answer = GUIUtilities.confirm(view, "closeall", null, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
			closeAll = answer == javax.swing.JOptionPane.YES_OPTION;
		}
		if (closeAll)
		{
			jEdit.closeAllBuffers(view);
		}
		</CODE>
	</ACTION>
	<ACTION NAME="$Save docking layout ...">
		<CODE>
			DockingLayoutManager.saveAs(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Save a Cop$y As...">
		<CODE>
			buffer.saveAs(view,false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Replace in Selection">
		<CODE>
			SearchAndReplace.replace(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Recent Buffer">
		<CODE>
			view.getEditPane().recentBuffer();
		</CODE>
	</ACTION>
	<ACTION NAME="$Activity Log (Toggle)">
		<CODE>
			view.getDockableWindowManager().toggleDockableWindow("log-viewer");
		</CODE>
	</ACTION>
	<ACTION NAME="jEdit $Help">
		<CODE>
			new HelpViewer();
		</CODE>
	</ACTION>
	<ACTION NAME="Cu$t to Register">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getRegisterStatusPrompt("cut-string-register"),
			"Registers.cut(textArea,__char__);");
		</CODE>
	</ACTION>
	<ACTION NAME="Select Next Word">
		<CODE>
			textArea.goToNextWord(true,false);
		</CODE>
	</ACTION>
	<ACTION NAME="Split $Vertically">
		<CODE>
			view.splitVertically();
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle Line Separator">
		<CODE>
			buffer.toggleLineSeparator(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$Memory Status...">
		<CODE>
			jEdit.showMemoryDialog(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Paste $Deleted...">
		<CODE>
			new PasteFromListDialog("paste-deleted",view,
			KillRing.getInstance());
		</CODE>
	</ACTION>
	<ACTION NAME="Plugin $Manager...">
		<CODE>
			PluginManager.showPluginManager(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$Range Comment">
		<CODE>
			textArea.rangeComment();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Previous Word">
		<CODE>
			textArea.goToPrevWord(false,false);
		</CODE>
	</ACTION>
	<ACTION NAME="Select $None">
		<CODE>
			s = textArea.getSelectionAtOffset(textArea.getCaretPosition());
		if(s == null)
			textArea.selectNone();
		else
			textArea.removeFromSelection(s);
		</CODE>
	</ACTION>
	<ACTION NAME="$EditPane Scope BufferSets">
		<CODE>
			bufferSetManager = jEdit.getBufferSetManager();
		bufferSetManager.setScope(BufferSet.Scope.editpane);
		</CODE>
	</ACTION>
	<ACTION NAME="Vie$w Markers (New Floating Instance)">
		<CODE>
			view.getDockableWindowManager().floatDockableWindow("view-markers");
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle E$xclusive BufferSets">
		<CODE>
			jEdit.setBooleanProperty("buffersets.exclusive",
        !jEdit.getBooleanProperty("buffersets.exclusive"));
		</CODE>
	</ACTION>
	<ACTION NAME="null">
		<CODE>
			textArea.goToNextBracket(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Vie$w Markers (Toggle)">
		<CODE>
			view.getDockableWindowManager().toggleDockableWindow("view-markers");
		</CODE>
	</ACTION>
	<ACTION NAME="Split $Horizontally">
		<CODE>
			view.splitHorizontally();
		</CODE>
	</ACTION>
	<ACTION NAME="Repeat Last Macro">
		<CODE>
			if(Macros.getLastMacro() == null)
			view.getToolkit().beep();
		else
			Macros.getLastMacro().invoke(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Delete Next Word">
		<CODE>
			textArea.deleteWord();
		</CODE>
	</ACTION>
	<ACTION NAME="$Global Options...">
		<CODE>
			new org.jedit.options.CombinedOptions(view, 0);
		</CODE>
	</ACTION>
	<ACTION NAME="Search in Open $Buffers...">
		<CODE>
			SearchDialog.showSearchDialog(view,textArea.getSelectedText(),
			SearchDialog.ALL_BUFFERS);
		</CODE>
	</ACTION>
	<ACTION NAME="$Find...">
		<CODE>
			SearchDialog.showSearchDialog(view,textArea.getSelectedText(),SearchDialog.CURRENT_BUFFER);
		</CODE>
	</ACTION>
	<ACTION NAME="To $Upper Case">
		<CODE>
			textArea.toUpperCase();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Right Docking Area">
		<CODE>
			wm.getRightDockingArea().showMostRecent();
		</CODE>
	</ACTION>
	<ACTION NAME="Select Next Line">
		<CODE>
			textArea.goToNextLine(true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Incremental Search Bar">
		<CODE>
			view.quickIncrementalSearch(false);
		</CODE>
	</ACTION>
	<ACTION NAME="null">
		<CODE>
			jEdit.newView(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Previous Character">
		<CODE>
			textArea.goToPrevCharacter(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Edit syntax style of token under caret">
		<CODE>
			StyleEditor.invokeForCaret(textArea);
		</CODE>
	</ACTION>
	<ACTION NAME="$Keyboard Tester...">
		<CODE>
			new GrabKeyDialog(view,null,null,jEdit.newFile(view));
		</CODE>
	</ACTION>
	<ACTION NAME="null">
		<CODE>
			buffer.removeMarker(textArea.getCaretLine());
		</CODE>
	</ACTION>
	<ACTION NAME="$Restore Split">
		<CODE>
			view.resplit();
		</CODE>
	</ACTION>
	<ACTION NAME="$Select Code Block">
		<CODE>
			textArea.selectBlock();
		</CODE>
	</ACTION>
	<ACTION NAME="Select to Smart End Position">
		<CODE>
			textArea.smartEnd(true);
		</CODE>
	</ACTION>
	<ACTION NAME="$Action Bar">
		<CODE>
			view.actionBar();
		</CODE>
	</ACTION>
	<ACTION NAME="Add Buffer to Favorites">
		<CODE>
			FavoritesVFS.addToFavorites(buffer.getPath(),VFSFile.FILE);
		</CODE>
	</ACTION>
	<ACTION NAME="null">
		<CODE>
			textArea.goToPrevBracket(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Insert Tab and Indent">
		<CODE>
			textArea.insertTabAndIndent();
		</CODE>
	</ACTION>
	<ACTION NAME="Select Previous Character">
		<CODE>
			textArea.goToPrevCharacter(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select Next Word (Eat Whitespace)">
		<CODE>
			textArea.goToNextWord(true,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Left Docking Area">
		<CODE>
			wm.getLeftDockingArea().showMostRecent();
		</CODE>
	</ACTION>
	<ACTION NAME="Re$place and Find Next">
		<CODE>
			if(SearchAndReplace.replace(view))
			SearchAndReplace.find(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$Select to Marker">
		<CODE>
			view.getInputHandler().readNextChar(
			buffer.getMarkerStatusPrompt("select-marker"),
			"editPane.goToMarker(__char__,true);");
		</CODE>
	</ACTION>
	<ACTION NAME="$Record Macro...">
		<CODE>
			Macros.recordMacro(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Cut Appe$nd to Register">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getRegisterStatusPrompt("cut-append-string-register"),
			"Registers.append(textArea,__char__,\"\\n\",true);");
		</CODE>
	</ACTION>
	<ACTION NAME="Select to End of Line">
		<CODE>
			textArea.goToEndOfLine(true);
		</CODE>
	</ACTION>
	<ACTION NAME="Select Previous Word (Eat Whitespace after word)">
		<CODE>
			textArea.goToPrevWord(true,true,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Delete Previous Word (Eat Whitespace)">
		<CODE>
			textArea.backspaceWord(true);
		</CODE>
	</ACTION>
	<ACTION NAME="V$ertical Paste Register">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getRegisterStatusPrompt("vertical-paste-string-register"),
			"Registers.paste(textArea,__char__,true);");
		</CODE>
	</ACTION>
	<ACTION NAME="Toggle Docked $Areas">
		<CODE>
			wm.toggleDockAreas();
		</CODE>
	</ACTION>
	<ACTION NAME="Evaluate $Selection">
		<CODE>
			BeanShell.evalSelection(view,textArea);
		</CODE>
	</ACTION>
	<ACTION NAME="$Redo">
		<CODE>
			buffer.redo(textArea);
		</CODE>
	</ACTION>
	<ACTION NAME="Delete Next Character">
		<CODE>
			textArea.delete();
		</CODE>
	</ACTION>
	<ACTION NAME="$Stop Recording">
		<CODE>
			Macros.stopRecording(view);
		</CODE>
	</ACTION>
	<ACTION NAME="$Reload">
		<CODE>
			buffer.reload(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Previous Bracket">
		<CODE>
			textArea.goToPrevBracket(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Add Directory to Favorites">
		<CODE>
			FavoritesVFS.addToFavorites(buffer.getDirectory(),VFSFile.DIRECTORY);
		</CODE>
	</ACTION>
	<ACTION NAME="$Join Lines">
		<CODE>
			textArea.joinLines();
		</CODE>
	</ACTION>
	<ACTION NAME="Close (global)">
		<CODE>
			jEdit.closeBuffer(view,buffer);
		</CODE>
	</ACTION>
	<ACTION NAME="$Whole Word">
		<CODE>
			SearchAndReplace.setWholeWord(!SearchAndReplace.getWholeWord());
		</CODE>
	</ACTION>
	<ACTION NAME="Line Scroll $Up">
		<CODE>
			textArea.scrollUpLine();
		</CODE>
	</ACTION>
	<ACTION NAME="To $Lower Case">
		<CODE>
			textArea.toLowerCase();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Next Paragraph">
		<CODE>
			textArea.goToNextParagraph(false);
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Start of Buffer">
		<CODE>
			textArea.goToBufferStart(false);
		</CODE>
	</ACTION>
	<ACTION NAME="New $View">
		<CODE>
			bsm = jEdit.getBufferSetManager();
	if (bsm.getScope() == BufferSet.Scope.global) {
		jEdit.newView(view);
	}
	else {
		View.ViewConfig config = new View.ViewConfig();
		config.docking = view.getViewConfig().docking;
		jEdit.newView(view,bsm.createUntitledBuffer(),config);
	}
		</CODE>
	</ACTION>
	<ACTION NAME="Vie$w Registers (New Floating Instance)">
		<CODE>
			view.getDockableWindowManager().floatDockableWindow("view-registers");
		</CODE>
	</ACTION>
	<ACTION NAME="Insert Next Character Literally">
		<CODE>
			view.getInputHandler().readNextChar(
			jEdit.getProperty("view.status.insert-literal"),
			"textArea.setSelectedText(String.valueOf(__char__));");
		</CODE>
	</ACTION>
	<ACTION NAME="Remove Trailing $Whitespace">
		<CODE>
			textArea.removeTrailingWhiteSpace();
		</CODE>
	</ACTION>
	<ACTION NAME="Evaluate $For Selected Lines...">
		<CODE>
			BeanShell.showEvaluateLinesDialog(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Re$peat Last Action">
		<CODE>
			view.getInputHandler().invokeLastAction();
		</CODE>
	</ACTION>
	<ACTION NAME="$Rectangular Selection">
		<CODE>
			textArea.toggleRectangularSelectionEnabled();
		</CODE>
	</ACTION>
	<ACTION NAME="Clos$e All">
		<CODE>
			boolean closeAll = true;
		if (jEdit.getBooleanProperty("closeAllConfirm"))
		{
			int answer = GUIUtilities.confirm(view, "closeall", null, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
			closeAll = answer == javax.swing.JOptionPane.YES_OPTION;
		}
		if (closeAll)
		{
			org.gjt.sp.jedit.bufferset.BufferSet bufferSet = editPane.getBufferSet();
			Buffer[] buffers = bufferSet.getAllBuffers();
			for (Buffer buff: buffers) {
				jEdit.closeBuffer(editPane,buff);
			}
		}
		</CODE>
	</ACTION>
	<ACTION NAME="Go to $Next Buffer">
		<CODE>
			editPane.nextBuffer();
		</CODE>
	</ACTION>
	<ACTION NAME="A$dd Explicit Fold">
		<CODE>
			textArea.addExplicitFold();
		</CODE>
	</ACTION>
	<ACTION NAME="Smart Home">
		<CODE>
			textArea.smartHome(false);
		</CODE>
	</ACTION>
	<ACTION NAME="$Tip of the Day">
		<CODE>
			new TipOfTheDay(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Vie$w Registers">
		<CODE>
			view.getDockableWindowManager().showDockableWindow("view-registers");
		</CODE>
	</ACTION>
	<ACTION NAME="Delete to $Start of Line">
		<CODE>
			textArea.deleteToStartOfLine();
		</CODE>
	</ACTION>
	<ACTION NAME="Edit Favorites">
		<CODE>
			VFSBrowser.browseDirectory(view,"favorites:");
		</CODE>
	</ACTION>
	<ACTION NAME="Ne$w Plain View">
		<CODE>
			jEdit.newView(view,buffer,true);
		</CODE>
	</ACTION>
	<ACTION NAME="Re$move All Markers">
		<CODE>
			buffer.removeAllMarkers();
		</CODE>
	</ACTION>
	<ACTION NAME="Go to Next Word (Eat Whitespace)">
		<CODE>
			textArea.goToNextWord(false,true);
		</CODE>
	</ACTION>

---
<a id="PluginConsole"></a>
### Plugin: Console

	<ACTION NAME="Console (Toggle)">
		<CODE>
			view.getDockableWindowManager().toggleDockableWindow("console");
		</CODE>
	</ACTION>
	<ACTION NAME="Console">
		<CODE>
			view.getDockableWindowManager().showDockableWindow("console");
		</CODE>
	</ACTION>
	<ACTION NAME="Browse Commando Directory">
		<CODE>
			directory = console.ConsolePlugin.getUserCommandDirectory();
			if(directory == null)
				GUIUtilities.error(view,"no-settings",null);
			else
				VFSBrowser.browseDirectory(view,directory);
		</CODE>
	</ACTION>
	<ACTION NAME="Console (New Floating Instance)">
		<CODE>
			view.getDockableWindowManager().floatDockableWindow("console");
		</CODE>
	</ACTION>
	<ACTION NAME="Run Last Command">
		<CODE>
			view.getDockableWindowManager().addDockableWindow("console");
			console = view.getDockableWindowManager().getDockable("console");
			console.runLastCommand();
		</CODE>
	</ACTION>
	<ACTION NAME="Commando...">
		<CODE>
			new console.commando.CommandoDialog(view,null);
		</CODE>
	</ACTION>
	<ACTION NAME="Clear console">
		<CODE>
			console = view.getDockableWindowManager().getDockable("console");
          console.clear();
		</CODE>
	</ACTION>
	<ACTION NAME="CD to Selected Node">
		<CODE>
			changeToPvSelected(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Run Current Buffer...">
		<CODE>
			console.ConsolePlugin.run(view,buffer);
		</CODE>
	</ACTION>
	<ACTION NAME="Compile Current Buffer...">
		<CODE>
			console.ConsolePlugin.compile(view,buffer);
		</CODE>
	</ACTION>
	<ACTION NAME="CD to Root">
		<CODE>
			changeToPvRoot(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Regen dynamic menus">
		<CODE>
			console.ConsolePlugin.rescanCommands();
		</CODE>
	</ACTION>
	<ACTION NAME="Run Project">
		<CODE>
			console.ConsolePlugin.runProject(view);
		</CODE>
	</ACTION>
	<ACTION NAME="Compile Project">
		<CODE>
			console.ConsolePlugin.compileProject(view);
		</CODE>
	</ACTION>

---
<a id="DockingLayouts"></a>
### Docking Layouts

	<ACTION NAME="load-perspective-view0">
		<CODE>
			jEdit.getAction(load-perspective-view0).invoke(view); 
		</CODE>
	</ACTION>

---
