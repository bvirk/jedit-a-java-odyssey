<!-- head=sitev1.css.html+jquery.html+pictoLinks.css+pictoLinks.js&body=pagev1.html -->
### The renering mechanism

- ... uses a querystring formatted parameterlist in a html comment in line 0 which lists adjacent sibling of head and body elements - siblings which value is content of their-by named files
- file names with suffix .html used as siblings must contain valid html and consists of a list of one or more elements at top level
- extern referenced css and js lays in basedir or remote as opposed to 'siblings' that lays in Jedit plugin directory
- file names with suffix .css or .js used as siblings do not have their html tags surroundings.
- about file names with suffix .md used as sibling(s)
	- must only be html body element childs.
	- is meant to contain markdown source
	- comes after the source that becomes first child of the body element
	- is only meant to be used when source isn't included by other files.
- ... uses include with includes directives mechanism to include file content in file content. 
- the final html file can consist of more additional blocks to the source than listed in querystring in line 0, due to the include directive
- the include directive can contain aliases for filenames
- one alias is sourcefile
- detection in the inclusion mechanism for use of sourcefile gives the possibilty to select between placing markdown conversioned html deep in a html structure or as sole body child. 
- a querystring parameter: ´mdLead´ can contain a name of a global BeanShell method which signature is String ()
- within basedir, the settings of the 'markdown lead function' is the default value of mdLead if it isn't overridden by a litteral appearance in line 0.
- The string returned by 'running mdLead' is placed before the markdown from source.
