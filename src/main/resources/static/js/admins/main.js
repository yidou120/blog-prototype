/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function() {
	//选中第一项menu
	$(".blog-menu .list-group-item:first").trigger("click");

	//触发url属性 进行ajax
	$(".blog-menu .list-group-item").click(function() {
		//获取url属性值
		var url = $(this).attr("url");
		//删除active样式
		$(".menu .list-group-item").removeClass("active");
		//给当前的menu设置active样式
		$(this).addClass("active");
		//ajax
		$.ajax(
			{
				url: url,
				success: function(data) {
					$("#rightContainer").html(data);
				},
				error: function () {
					alert("error!")
				}
			}
		);
	});
	

});