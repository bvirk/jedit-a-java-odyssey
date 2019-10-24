$(function() {
	$('.pop').click(function() {
		var parent =$(this).parent();
		var div = parent.next();
		var vis = div.css('display');
		//alert(vis);
		if (vis == "none") {
			$(this).html('&nbsp;&#9662;&nbsp;');
			div.css('display','block');
			parent.get(0).scrollIntoView();
		} else {
			div.css('display','none');
			$(this).html('&nbsp;&#9656;&nbsp;');
		}
	});
});