window.onload=function(){ 
	init();
}

function init(){
	$("#submit").bind("click",function(){
		$.ajax({
			url: "/api/convert", 
			type: "post",
			dataType: "json",
			data: {
				code: $("#code").val(),
				type: "0"
			}, 
			success: function(res){
				alert(res);
			}
		
		});
	});
}