var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket =$.cookie("TT_TOKEN");//从cookie获取数据 token
		if(!$.cookie("TT_TOKEN")){
			return ;
		}
		$.ajax({
			//http://localhost:8084/user/token/123?callback=fun
			url : "http://sso.taotao.com/user/token/" + _ticket,//首先定义个fun(),再发送请求的时候带上?callback=fun
			dataType : "jsonp",
			type : "GET",
			success : function(data){//相当于fun
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到淘淘！<a href=\"javascript:void()\" class=\"link-logout\"  onclick=\"logout()\" >[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});

function logout(){
	var _ticket = $.cookie("TT_TOKEN");
	if(!_ticket){
		return ;
	}
	$.ajax({
		url : "http://sso.taotao.com/user/logout/" + _ticket,
		dataType : "jsonp",
		type : "GET",
		success : function(data){
			if(data.status == 200){
				location="http://www.taotao.com";
			}
		}
	});
	
}