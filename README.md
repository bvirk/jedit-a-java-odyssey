## Two plugin for jedit

### Plugin MyDuck
The least possible to be a plugin - simpler than quicknotepad


### Plugin TxtConv
markdown plugin, using [txtmark](https://github.com/rjeschke/txtmark) and [jsoup](https://jsoup.org/) for various purposes. Characteristics for plugin TxtConv

- does only makes html from markdown despite its name.
- has javadoc in source.
- converts a .md file to a .html file in same directory using conversion from markdown to html.
- deal with a _basedir_ in which internal links are managed 
- can convert current buffer to a new Untitled buffer
- has a spinner in status bar to show progress
- convenient 'shift to browser' functionality made as an option to minimize jedit on exit of markdown conversion. This option must not be selected when multible markdown conversions will occur, such as one or more generates of some .bsh files not being uptodate 
- uses some _dirty tricks_ to coop with markdown inside html block elements, somthing than txtmark in itself would not convert.
- has a _rendering mechanism_ for including css and js and offers a choice of letting the converted markdown have one of following of relations to the body element:
	- a child of
	- a child of some element in an unlimmited complex child of
	

late reveiw: a bit complicated


