<!-- head=sitev1.css.html+jquery.html+pictoLinks.css+pictoLinks.js&body=pagev1.html -->
### Handsome jEdit macros and a plugin
This conseptional description don't deals with all those dependencies that would be demanding to describe, but all macros and methods defined in $JEDIT_SETTINGS/startup directory is well documented.
 
Albeit this, [my type of use jEdit][topdependencies] descibes some requirements for being able to use macros and 'start methods'.

### Macro startup
Macro startup deals with BeanShell global methods - those which litteral source is placed in $JEDIT\_SETTINGS/startup. It offers:

- alphabetic sorted clickable buttonraster for
	- pasting to carret
	- [swiching to definition for][navigateToStartup]
		- documentation
		- editing, removal or adding new methods in same startup file
- [update methods][sourcing]

### Macro cyberkiss
The [kisses][nameCyberkiss] are functionality of some text which result is often viewed in webbrowser. The functionality is chosen by a shortcut for a label in a dialog's list. Following is a list of explained kisses, invokeable with the word left to carret, selected or in clipboard, as documented in macro cyberkiss

- [import][jimport]
	- insert java import statement of a class given its simpleName
<br><br>
- [api-help][jimport]
	- opens javadoc of a class in browser, given the class's simpleName 
<br><br>	
- beanshell source
	- navigate to global Beanshell method definition
<br><br>
- java keyword
	- open web page describing java keyword
<br><br>
- duckduckgo
	- open duckduckgo search result in browse
<br><br>
- google
	- open google search result in browser

### Macro actionsDialog
File type context actions dialog that uses same method as macro cyberkiss.

- void actionsDialog(XThis callBack, XThis menuSeq, View view) 

The types of actions is, dependt of suffix of current buffer file name. It is easy configured in top file of macro actionsDialog. Some examples that applies to certain suffixes.

- runs ant target (clean,compile,run,pack to jar,document)
- runs extern interpreter (eg. node for .js files)
- calls methods in plugins (eg. set printUrgency of some plugins debugging utility)
- run current buffer as BeanShell script

### Plugin TxtConv
is a markdown plugin, using [txtmark](https://github.com/rjeschke/txtmark) and [jsoup](https://jsoup.org/) for various purposes. Characteristics for plugin TxtConv

- does only makes html from markdown despite its name.
- has javadoc in source.
- converts a .md file to a .html file in same directory using conversion from markdown to html.
- deal with a [basedir][basedir] in which internal links are managed 
- can convert current buffer to a new Untitled buffer
- has a spinner in status bar to show progress
- convenient 'shift to browser' functionality made as an option to minimize jedit on exit of markdown conversion. This option must not be selected when multible markdown conversions will occur, such as one or more generates of some .bsh files not being uptodate 
- uses some [dirty tricks][dirtyTricks] to coop with markdown inside html block elements, somthing than txtmark in itself would not convert.
- has a [rendering mechanism][rendering] for including css and js and offers a choice of letting the converted markdown have one of following of relations to the body element:
	- a child of
	- a child of some element in an unlimmited complex child of

