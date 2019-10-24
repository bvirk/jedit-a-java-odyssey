<!-- head=sitev1.css.html+jquery.html+pictoLinks.css+pictoLinks.js&body=pagev1.html -->
### Trick to fool a markdown converter
Not touching content of html block tags seems resonable when converting markdown, in that way tables can be embedded. If for some reason this is not wanted, the trick could be:

- embed each block tag in a html comment on each own line
- do the markdown conversion
- remove embedning of html tag
- niceify using jsoup because it looks like shit
