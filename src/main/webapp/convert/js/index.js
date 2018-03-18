var one_css_editor = null;
var two_css_editor = null;
var one_other_editor = null;
var two_css_editor = null;


window.onload=function(){
	bindCommonEvent();
	initTabOne();
	initComment();
}

/**绑定通用事件**/
function bindCommonEvent(){
	//绑定导航栏点击事件
	$(".head_nav li").bind("click",function(){
		var mode = $(this).attr("mode");
		$(this).addClass("active").siblings().removeClass("active");
		$("#mode_"+mode).show().siblings().hide();
		if(mode=="one"){
			initTabOne();
		}else if(mode=="two"){
			initTabTwo();
		}else if(mode=="three"){
			initTabThree();
		}
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
	$("#mode_one li .ckBox input").unbind("click");
	$("#mode_one li .ckBox input").bind("click",function(){
		var type = $(this).attr("id").substring(0,$(this).attr("id").indexOf("_box"));
		_hmt.push(['_trackEvent', "tab1类型选择", type]);
		if($(this).is(":checked")){
			$(this).parent().siblings().find("input").prop("checked",false);
		}else{//取消选中
			$(this).prop("checked",true);
		}
		$("#mode_one #covert_type").attr("ct_type",type);
		//类型切换移除之前加的变量
		$("#mode_one #vars li").remove();
		$("#mode_one .opreator_area .no_var_tip").show();
		$("#mode_one #vars").hide();
		
		setOtherEditor(type,"one");
	});
	/***绑定变量新增事件***/
	$("#mode_one #doAdd").unbind("click")
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
		_hmt.push(['_trackEvent', "新增变量", "新增"]);
		var var_tag = $("#mode_one #covert_type").attr("ct_type") == "scss" ? "$" : "@";
		var var_one = var_tag + name +":"+value+";"
		$("#mode_one #vars").append("<li var_one='"+var_one+"'><span>"+var_one+"</span><em>x<em></li>").show();
		$("#mode_one #vars li em").unbind("click");
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
	$("#mode_one #do_convert").unbind("click");
	$("#mode_one #do_convert").bind("click",function(){
		_hmt.push(['_trackEvent', "tab1转换", "点击"]);
		parseInput();
	});
	/**绑定演示代码按钮点击事件**/
	$("#mode_one #cssDemo").unbind("click");
	$("#mode_one #cssDemo").bind("click",function(){
		_hmt.push(['_trackEvent', "tab1演示代码", "点击"]);
		one_css_editor.setValue($("#cssDemoCode").html());
		one_css_editor.gotoLine(1);
	});
}

/**初始化scss/less->css**/
function initTabTwo(){
	//初始化编辑器
	two_css_editor = ace.edit("two_css_editor", {
	    mode: "ace/mode/scss",
	    selectionStyle: "text"
	});
	two_css_editor.setTheme("ace/theme/monokai");
	setOtherEditor("scss","two");
	//绑定转换类型选择按钮点击事件
	$("#mode_two li .ckBox input").unbind("click");
	$("#mode_two li .ckBox input").bind("click",function(){
		var type =$(this).attr("id").substring(0,$(this).attr("id").indexOf("_check"));
		_hmt.push(['_trackEvent', "tab2类型选择", type]);
		if($(this).is(":checked")){
			$(this).parent().siblings().find("input").prop("checked",false);
		}else{//取消选中
			$(this).prop("checked",true);
		}
		$("#mode_two #other_covert_type").attr("ct_type",type);
		setOtherEditor(type,"two");
	});
	/***绑定转换事件***/
	$("#mode_two #two_convert").unbind("click");
	$("#mode_two #two_convert").bind("click",function(){
		var that = $(this);
		if(!that.hasClass("dis")){
			that.addClass("dis").html("转换中...");
			_hmt.push(['_trackEvent', "tab2转换", "点击"]);
			$.ajax({
				url: "/api/main/convert", 
				type: "post",
				dataType: "json",
				data: {
					"code": two_other_editor.getValue(),
					"type": $("#mode_two #other_covert_type").attr("ct_type") == "scss" ? "0" : "1"
				},
				success: function(res){
					that.removeClass("dis").html("转换");
					if(res.error_no == 0){
						_hmt.push(['_trackEvent', "tab2转换", "成功"]);
						var result = res.result;
						if(result.status == "0"){
							two_css_editor.setValue(result.code);
							two_css_editor.gotoLine(1);
						}
					}else{
						_hmt.push(['_trackEvent', "tab2转换", "失败"]);
						errorMsg("转换失败，请检查代码格式",2000);
					}
				}
			});
		}
	});
	/**绑定演示代码按钮点击事件**/
	$("#mode_two #otherDemo").unbind("click");
	$("#mode_two #otherDemo").bind("click",function(){
		_hmt.push(['_trackEvent', "tab2演示代码", "点击"]);
		two_other_editor.setValue($("#"+$("#mode_two #other_covert_type").attr("ct_type")+"DemoCode").html().replace(/&amp;/g, '&'));
		two_other_editor.gotoLine(1);
	});
}

/**
 * 初始化批量scss/less->css
 **/
function initTabThree(){
	//绑定转换类型选择按钮点击事件
	$("#mode_three .ckBox input").unbind("click");
	$("#mode_three .ckBox input").bind("click",function(){
		var type = $(this).attr("id").substring(0,$(this).attr("id").indexOf("_file"));
		_hmt.push(['_trackEvent', "tab3类型选择", type]);
		if($(this).is(":checked")){
			$(this).parent().siblings("div").find("input").prop("checked",false);
		}else{//取消选中
			$(this).prop("checked",true);
		}
		$("#mode_three #three_covert_type").attr("ct_type",type);
	});
	//绑定选择文件事件
	$("#mode_three #upload_file").off("change");
	$("#mode_three #upload_file").on("change",function(){
		var file = $("#mode_three #upload_file")[0].files[0];
		var name = file.name;
		var size = file.size;
		if(size >= 1024*1024){
			size = (size/(1024*1024)).toFixed(2) + "M";
		}else{
			size = (size/1024).toFixed(2) + "KB";
		}
		$("#mode_three #filename").html(name);
		$("#mode_three #filesize").html(size);
		$("#mode_three .file_info").show();
	});
	//绑定上传文件事件
	$("#mode_three #file_convert").unbind("click");
	$("#mode_three #file_convert").bind("click",function(){
		var that = $(this);
		if(!that.hasClass("dis")){
			_hmt.push(['_trackEvent', "tab3转换", "点击"]);
			if($("#download").length>0){
				$("#download").remove();
			}
			that.addClass("dis").html("转换中...");
			var file = $("#mode_three #upload_file")[0].files[0];
			if(file.size>5242880){
				errorMsg("文件大小不能超过5M",1500);
				that.removeClass("dis").html("开始转换");
				return;
			}
			var formData = new FormData();
			formData.append("file", file);
			formData.append("type", $("#mode_three #three_covert_type").attr("ct_type"));
			$.ajax({
				url : "/api/main/file", 
				type : 'POST', 
				data : formData, 
				processData : false, 
				contentType : false,
				success : function(res) { 
					if(res.error_no == 0){
						_hmt.push(['_trackEvent', "tab3转换", "成功"]);
						var url = res.result.url;
						that.removeClass("dis").html("开始转换");
						window.open(url);
						var downHtml = '<p id="download" style="margin-top: 15px;font-size: 14px;color: #5ca7a2;">转换成功，若未自动下载，请 ' +
							'<a style="color: blue;font-style: italic;cursor: pointer;" href="' + url + '">点击此处</a> 重新下载</p>';
						$(".upload_area .file_info").append(downHtml);
					}else{
						_hmt.push(['_trackEvent', "tab3转换", "失败"]);
						errorMsg(res.error_info,2000);
						that.removeClass("dis").html("开始转换");
					}
				}
			});
		}
	});
}

/**初始化评论**/
function initComment(){
	getComments(1,1);
}
//查询评论
function getComments(page_num,article_id){
	$.ajax({
		url: "/api/comment/queryPage", 
		type: "post",
		dataType: "json",
		data: {
			"page_num": page_num,
			"article_id": article_id
		},
		success: function(res){
			if(res.error_no == 0){
				var result = res.result;
				var comments = result.comments;
				var joins = result.joins;
				var _html = buildCommentHtml(comments,joins);
				$(".comment .comment_list").html(_html);
			}else{
				errorMsg(res.error_info,2000);
			}
		}
	});
}
//生成评论html
function buildCommentHtml(comments,joins){
	var _html = "";
	for (var i = 0; i < comments.length; i++) {
		var comment = comments[i];
		var li = '<li id="'+comment.id+'" name="'+comment.name+'"><div class="name_title"><span>'+comment.name+'</span><em>'+comment.create_date+'</em></div><div class="c_content">'+comment.content+'<div class="reply">回复</div></div>';
		if(joins && joins.length>0){
			for (var j = 0; j < joins.length; j++) {
				var join = joins[j];
				if(join.join_id == comment.id){
					li += '<div class="rep_comment" id="'+join.id+'" name="'+join.name+'"><div class="name_title"><span>'+join.name+'</span><em>'+join.create_date+'</em></div><div class="c_content">'+join.content+'<div class="reply">回复</div></div></div>';
				}
			}
		}
		li += '</li>';
		_html += li;
	}
	return _html;
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

//错误信息提示
function errorMsg(msg,time){
	$("#error_msg").html(msg ? msg : "未知错误").show();
	setTimeout(function(){
		$("#error_msg").html("").hide();
	},time ? time : 2000);
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
	var varstr = "";
	$("#mode_one #vars li").each(function(){
		varstr += $(this).attr("var_one") + '\n';
	});
	return varstr + exportObject(least);
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