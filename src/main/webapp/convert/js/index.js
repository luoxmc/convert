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

/**初始化css->scss/less**/
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
		$("#mode_one #covert_type").attr("ct_type",$(this).attr("id").substring(0,$(this).attr("id").indexOf("_box")));
		//类型切换移除之前加的变量
		$("#mode_one #vars li").remove();
		$("#mode_one .opreator_area .no_var_tip").show();
		$("#mode_one #vars").hide();
		
		var type = "";
		$("#mode_one li .ckBox input").each(function(){
			if($(this).is(":checked")) {
			    type = $(this).attr("id").substring(0,$(this).attr("id").indexOf("_box"));
			}
		});
		setOtherEditor(type,"one");
	});
	/***绑定变量新增事件***/
	$("#mode_one #doAdd").bind("click",function(){
		var name = $("#mode_one #varName").val().replace(/[\n;]/,"");
		var value = $("#mode_one #varValue").val().replace(/[\n;]/,"");
		if(!name){
			$("#mode_one #varName").focus();
			return;
		}
		if(!value){
			$("#mode_one #varValue").focus();
			return;
		}
		var var_tag = $("#mode_one #covert_type").attr("ct_type") == "scss" ? "$" : "@";
		var var_one = var_tag + name +":"+value+";"
		$("#mode_one #vars").append("<li var_one='"+var_one+"'><span>"+var_one+"</span><em>x<em></li>").show();
		$("#mode_one #vars li em").unbind();
		$("#mode_one #vars li em").bind("click",function(){//删除变量绑定事件
			$(this).parent().remove();
			if($("#mode_one #vars li").length<=0){
				$("#mode_one .opreator_area .no_var_tip").show();
				$("#mode_one #vars").hide();
			}
		});
		$("#mode_one #varName").val("");
		$("#mode_one #varValue").val("");
		$("#mode_one .opreator_area .no_var_tip").hide();
	});
	/***绑定转换事件***/
	$("#mode_one #do_convert").bind("click",function(){
		parseInput();
	});
	/**绑定演示代码按钮点击事件**/
	$("#mode_one #cssDemo").bind("click",function(){
		one_css_editor.setValue($("#cssDemoCode").html());
		one_css_editor.gotoLine(1);
	});
}

/**初始化scss/less->css**/
function initTabTwo(){
	
}

/**初始化批量scss/less->css**/
function initTabThree(){
//	$("#submit").bind("click",function(){
//	$.ajax({
//		url: "/api/convert", 
//		type: "post",
//		dataType: "json",
//		data: {
//			"code": one_css_editor.getValue(),
//			"type": "0"
//		}, 
//		success: function(res){
//			alert(res);
//		}
//	
//	});
//});
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



/***********css转换js开始*********/
var indentS = "  ";
var openingBracket;
var closingBracket;
var semiColumn;
var eol;
var keepOutsideComments;
var keepinsideComments;
var removeHacks;
var vars = {};
var comments = {};
var dataURLs = {};

function log(s) {
	if (console) console.log(s);
}
function tr(s) {
	return $.trim(s.replace(/\t+/, ' '));
}

function trimColor(str){
	return str.replace(/#([0-9a-f]{3}|[0-9a-f]{6})\b/ig, function (a) {
		return a.toLowerCase()
	})
}

function parseVars () {
	vars = {};
	var variables = "";
	$("#mode_one #vars li").each(function(){
		variables += $(this).attr("var_one");
	});
	variables = variables.split(/[\n;]/);
	$(variables).each(function(){
		var pair = this.replace(/\/\*[\s\S]*?\*\//gm,'')
		.replace(/\/\/[\s\S]*$/gm,'');

		pair = pair.split(/[:=]/);
		if(pair.length > 1){
			vars[trimColor($.trim(pair[1]))] = $.trim(pair[0]);
		}
	})
}

function parseInput() {
	var langVal=$("#mode_one #covert_type").attr("ct_type");
	if (langVal=='less') {
		eol=';';
		openingBracket='{';
		closingBracket='}';
		semiColumn=':';
	} else if (langVal=='scss') {
		eol=';';
		openingBracket='{';
		closingBracket='}';
		semiColumn=':';
	} else if (langVal=='stylus') {
		eol='';
		openingBracket='';
		closingBracket='';
		semiColumn='';
	}
	parseVars();

	keepOutsideComments = true;
	keepInsideComments = true;
	removeHacks = $("#removeHacks").is(":checked");

	one_other_editor.setValue(parseCSS(one_css_editor.getValue()));
	one_other_editor.gotoLine(1);
}

function makeMark (num) {
	var str="";
	str += '\t\t\t\t\t\t\t\t\t\t\t';
	while (num--){
		str += ' '
	}
	str += '\t\t\t\t\t\t\t\t\t\t\t';
	return str
}

function decodeMark (str) {
	var num = 0;
	str.replace(/ /g, function () {
		num ++
	})
	return num
}

function parseCSS(s) {
	var least={children:{}};

	comments = {};
	var i = 1;

	//replace comments with marks;
	s=s.replace(/\/\*[\s\S]*?\*\//gm, function (a){
		comments[++i] = a;
		return makeMark(i);
	});

	//inside comments to fake declarations;
	s=s.replace(/([^{]+)\{([^}]+)\}/g, function(group, selector, declarations) {

		return selector + '{' +declarations.replace(/\t{10} +\t{10}/g, function (a){
			a = decodeMark(a);
			return 'comment__-'+a+': '+a+';'
		})+'}';
	});

	//outside comments to fake selectors;
	s=s.replace(/\t{10} +\t{10}/g, function (a){
		a = decodeMark(a);
		return '.__comment__-'+a+' { index: '+a+';}'
	});

	dataURLs = {};

	//dataURL to fake url
	s=s.replace(/url\((data:[^\)]+)\)/gm, function (a, dataURL){
		dataURLs[++i] = dataURL;
		return 'url(__data__'+ i +')';
	})

	s.replace(/([^{]+)\{([^}]+)\}/g, function(group, selector, declarations) {
		var o={};
		o.source=group;
		o.selector=tr(selector);

		var path=least;

		if (o.selector.indexOf(',')>-1) {
			// Comma: grouped selector, we skip
			var sel=o.selector;
			if (!path.children[sel]) {
				path.children[sel]={children:{}, declarations:[]};
			}
			path = path.children[sel];
		} else {
			// Fix to prevent special chars to break into parts
			o.selector=o.selector.replace(/\s*([>\+~])\s*/g, ' &$1');
			o.selector=o.selector.replace(/(\w)([:\.])/g, '$1 &$2');

			o.selectorParts=o.selector.split(/[\s]+/);
			for (var i=0; i<o.selectorParts.length; i++) {
				var sel=o.selectorParts[i];
				// We glue the special chars fix
				sel=sel.replace(/&(.)/g, '& $1 ');
				sel=sel.replace(/& ([:\.]) /g, '&$1');

				if (!path.children[sel]) {
					path.children[sel]={children:{}, declarations:[]};
				}
				path = path.children[sel];
			}
		}


		declarations.replace(/([^:;]+):([^;]+)/g, function(decl, prop, val) {

			//remove hacks
			if(removeHacks){
				decl = decl
				.replace(/\s*[\*_].*/g, '')
				.replace(/.*(-webkit-|-o-|-moz-|-ms-|-khtml-|DXImageTransform|\\9|\\0|expression|opacity\s*=).*/g, '')
				if (!$.trim(decl)) {
					return
				}
			};

			val = trimColor(val);
			if (vars[val]){
				val = vars[val];
			} else {
				var a = [];
				$.each(val.split(' '), function(){
					a.push(vars[this] || this)
				});
				val = a.join(' ');
			}
			
			var declaration={
				source:decl,
				property:tr(prop),
				value:tr(val)
			};
			path.declarations.push(declaration);
		});
	});

	return exportObject(least);
}
var depth=0;
var s='';
function exportObject(path) {
	var s='';
	$.each(path.children, function(key, val) {
		if(key.slice(0,12) == '.__comment__'){
			keepOutsideComments && (s+=getIndent() + comments[val.declarations[0].value]+'\n');
			return
		} else {
			s+=getIndent()+key+' '+openingBracket+'\n';
		}
		depth++;
		for (var i=0; i<val.declarations.length; i++) {
			var decl=val.declarations[i];
			if (decl.property.slice(0,9) == 'comment__'){
				keepInsideComments && (s+=getIndent() + comments[decl.value]+'\n');
			} else {
				s+=getIndent()+decl.property+semiColumn+' '+decl.value+eol+'\n';
			}
			
		}
		s+=exportObject(val);
		depth--;
		s+=getIndent()+closingBracket+'\n';
	});

	// Remove blank lines - http://stackoverflow.com/a/4123442
	s=s.replace(/^\s*$[\n\r]{1,}/gm, '');


	s=s.replace(/url\(__data__(\d+)\)/gm, function (a, i){
		return 'url('+dataURLs[i]+')';
	})

	return s;
}
function getIndent() {
	var s='';
	for (var i=0; i<depth; i++) {
		s+=indentS;
	}
	return s;
}
/***********css转换js结束*********/



