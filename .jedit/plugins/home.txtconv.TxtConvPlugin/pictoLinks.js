$(function() {
	$("a").hover(function(e) {
			$(this).css({'font-weight': 'normal','text-decoration': 'none'});
			titleAtt=this.title.split(";");
			this.t=undefined;
			if(/^[0-9A-Fa-f]{4,5}$/.test(titleAtt[0]) && titleAtt.length==2) {
				this.t=this.title;
				this.title="";
				mes="<span class='pictograhp'>&#x"+titleAtt[0]+";</span>"+titleAtt[1];
				$("body").append("<p id='uniPicLink'>"+mes+"</p>");
				$("#uniPicLink")
					.css("left",(e.pageX + 20) + "px")
					.css("top",(e.pageY - 20) + "px")
					.fadeIn("fast");
			}
	},function() {
		$(this).css({'font-weight': 'bold','text-decoration': 'underline indianred'});
		if (this.t != undefined) {
			this.title=this.t;
			$("#uniPicLink").remove();
		}
	});
}
);