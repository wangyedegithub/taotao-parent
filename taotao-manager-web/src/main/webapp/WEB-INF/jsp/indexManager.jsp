<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="importAll()">一键导入商品数据到索引库</a>
</div>
<script type="text/javascript">
function importAll() {
	$.post("/index/importall",null,function(data){
		if (data.status==200) {
			$.messager.alert('提示','商品数据导入成功！');
		} else {
			$.messager.alert('提示','商品数据导入失败！');
		}
		
	});	
}
</script>
 