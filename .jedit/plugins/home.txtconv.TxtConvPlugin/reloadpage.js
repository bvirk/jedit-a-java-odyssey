(function() {
var blured=false;
window.onblur = (() => { setTimeout(() => blured=true,2000)});
window.onfocus = (() =>  { if (blured) { blured=false; window.location.reload(true); window.location.href="#viewspot";}});
})();
