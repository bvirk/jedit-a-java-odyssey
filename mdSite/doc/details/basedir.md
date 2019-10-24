<!-- head=sitev1.css.html+jquery.html+pictoLinks.css+pictoLinks.js&body=pagev1.html -->
### How links is managed inside basedir
- a link reference definition block of all .md files is generated for each markdown conversion
	- the link label is the barename of a file - url is ´barename´.html with relative path calculated
- on each compilation, for all files of type .bsh and .md in basedir or below:
	- if, for a .bsh file, there not exists an equal name younger .md file - the .bsh is executed
		- if a .bsh not generates a single equal named .md, it overtakes the yonger than responsibity of its generates - in other words: if a .bsh is changed (or touched) or its generates deleted, that .bsh is runned because in first place there don't exists a equal named .md and when runned perhaps not exists its other named generates. 
	- next, if, for a .md file, there not exists an equal younger named .html file, a markdown conversion is made.
- a baseurl can be chosen to make intern absolute links that fits some virtual directory in webserver

### Try it!
Subdir demoes of the placement of source of this text contains

- demoes/api-like/functions/startup-functions.bsh
- demoes/api-like/macros/macros.bsh

To see the above in action, install txtConv:

- build plugin TxtConv.jar
- build txtmark.jar
- dowload some jsoup jar
- put it $JEDIT\_SETTINGS/jars: TxtConv.jar, txtmark.jar, jsoup-x.xx.x.jar
- check plugins is loadet using Plugins->manager
- configure in Plugins->options->html->txtConv
	- unselect iconify on saved output
	- fill in a basedir
	- fill in a baseurl (../ for relative urls)
- invoke Plugins->convert\_to\_file with a DIRTY .md file, placed in subfolder of baseDir, in current buffer. Observe first time that $JEDIT\_SETTINGS/plugins/home.txtconv.TxtConvPlugin/  dir is created and filled.

I might have forgotten something - these things not just looks complicated under the hood, they are.
