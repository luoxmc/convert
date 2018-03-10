var one_css_editor = null;
var two_css_editor = null;
var one_other_editor = null;
var two_css_editor = null;


window.onload=function(){
	bindCommonEvent();
	initTabOne();
}

/**绑定通用事件**/
function bindCommonEvent(){
	//绑定导航栏点击事件
	$(".head_nav li").bind("click",function(){
		var mode = $(this).attr("mode");
		$(this).addClass("active").siblings().removeClass("active");
		$("#mode_"+mode).show().siblings().hide();
	});
}

/**初始化css->scss/sass/less**/
function initTabOne(){
	//初始化编辑器
	one_css_editor = ace.edit("one_css_editor", {
	    mode: "ace/mode/css",
	    selectionStyle: "text"
	});
	one_css_editor.setTheme("ace/theme/monokai");
	setOtherEditor("scss","one");
	//绑定转换类型选择按钮点击事件
	$("#mode_one li .ckBox input").bind("click",function(){
		if($(this).is(":checked")){
			$(this).parent().siblings().find("input").prop("checked",false);
		}else{//取消选中
			$(this).prop("checked",true);
		}
		var type = "";
		$("#mode_one li .ckBox input").each(function(){
			if($(this).is(":checked")) {
			    type = $(this).attr("id").substring(0,$(this).attr("id").indexOf("_box"));
			}
		});
		setOtherEditor(type,"one");
	});
	
	$("#submit").bind("click",function(){
		$.ajax({
			url: "/api/convert", 
			type: "post",
			dataType: "json",
			data: {
				"code": one_css_editor.getValue(),
				"type": "0"
			}, 
			success: function(res){
				alert(res);
			}
		
		});
	});
}

/**初始化scss/sass/less->css**/
function initTabTwo(){
	
}

/**初始化批量scss/sass/less->css**/
function initTabThree(){
	
}

/**
 * 初始化另一个编辑器
 * @param type 编辑器类型
 * @param tab  tab页id
 */
function setOtherEditor(type,tab){
	if(tab == "one"){
		one_other_editor = ace.edit(tab+"_other_editor", {
		    mode: "ace/mode/"+type,
		    selectionStyle: "text"
		});
		one_other_editor.setTheme("ace/theme/monokai");
	}else if(tab == "two"){
		two_other_editor = ace.edit(tab+"_other_editor", {
		    mode: "ace/mode/"+type,
		    selectionStyle: "text"
		});
		two_other_editor.setTheme("ace/theme/monokai");
	}
	
}


