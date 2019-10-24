String.prototype.format = function () {
  var args = arguments;
  return this.replace(/\{(\d+)\}/g, function (m, n) { return args[n]; });
};

(function( glObj) {
    
	glObj.optionElement = function (value,content) {
		return "<{0} class='optionclass' value='{1}'>{2}</{0}>".format("option",value,content);
	}
}( window.myGlb = window.myGlb || {}));

$(function() {
	function resizeFileSelect() {
		var headerwidth = $("header").css("width");
		$("#fileSelect").css("width",(headerwidth.substring(0,headerwidth.length-2).valueOf()-25)+"px");
		$("#fileSelect").val(window.location.href);
	}
	$('.pop').click(function() {
		var parent =$(this).parent();
		var div = parent.next();
		var vis = div.css('display');
		if (vis == "none") {
			$(this).html('&nbsp;&#9662;&nbsp;');
			div.css('display','block');
		} else {
			div.css('display','none');
			$(this).html('&nbsp;&#9656;&nbsp;');
		}
	});
	$('header').click(function() {
			window.open ("http://jedit.org",'_self',false);
	});
	$(window).on('resize', function() {
		resizeFileSelect();
	});
	$('#fileSelect').change(
		function() {
			var newPage = $("#fileSelect option:selected").attr('value');
			window.open (newPage,'_self',false);
		});
	for (var i in myGlb.pages)
		$('#fileSelect').append(myGlb.optionElement(myGlb.pages[i].split(/ /)[1].replace(/[\(\)]/g,"")+myGlb.pages[i].split(/ /)[0],myGlb.pages[i]));
	$("header").html("jEdit - "+window.location.href.replace(/.+\/([^/]+\.html)$/,"$1"));
	resizeFileSelect();
	
});
