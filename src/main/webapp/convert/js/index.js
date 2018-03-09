window.onload=function(){ 
	init();
}

function init(){
	$("#convert").bind("click",function(){
		$.ajax({
			url: "/api/convert", 
			type: "post",
			dataType: "json",
			data: {}, 
			success: function(res){
				alert(res);
			}
		
		});
	});
}