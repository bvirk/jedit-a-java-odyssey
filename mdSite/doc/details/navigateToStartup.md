<!-- head=sitev1.css.html+jquery.html+pictoLinks.css+pictoLinks.js&body=pagev1.html -->
### Ways of switching to definition of a global BeanShell method

- invoking macro startup and shift-click method name
- using macro cyberkiss, menuitem 'beanshell source'

In either ways - it is global method 

- boolean foundStartupFunction(String funcName,View view)

that open the relevant startup file and positions caret at folding start line.
Method are NOT lookud by name in code, but searching the folding start lines. The pattern of having an exact copy of method signature after '//{{{ ' is impotent for foundStartupFunction's ability to find some method.