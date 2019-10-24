<!-- head=sitev1.css.html+jquery.html+pictoLinks.css+pictoLinks.js&body=pagev1.html -->
### Updating global Beanshell methods
The running of 

- BeanShell.runScript(..., boolean ownNamespace) 

with argument ownNameSpace being false is what happends, during start of jEdit and for subsequent changing global methods.

... is context and the habit is keeping it is $JEDIT\_SETTINGS/startup. 

It shall be mentioned that it is a replacement for a given SIGNATURE of a method that takes place - in other words - if parameters and/or return type is changed an new method will exists in conjunction with the old one - until jEdit is restarted.

A trick, sometimes, when a new one is fatter, is reduce the old to a call to the new.

Macro startup starts with updating - after editing a startup file, macro startup must be invoked again. Some attempts to __only__ update occurs some times (look in status line). 