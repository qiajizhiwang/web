<!DOCTYPE html>
<html>

<head>
<#assign ctx= (request.contextPath)??/>
    <link href="/css/easyui.css" rel="stylesheet" />
    <link href="/css/icon.css" rel="stylesheet" />
    <script src="/js/jquery.min.js"></script>
    <script src="/js/jquery.easyui.min.js"></script>
     <script src="/js/easyui-lang-zh_CN.js"></script>
</head>
<body>



<div id="tb" style="padding:3px">
	<span>账号</span>
	<input id="name" style="line-height:26px;border:1px solid #ccc">
	<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">Search</a>
</div>

	
	<table id="tt" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#tb"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/system/userList',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'id',width:80">ID</th>
				<th data-options="field:'loginName',width:100">登录名</th>
				<th data-options="field:'password',width:80,align:'right'">密码</th>
			</tr>
		</thead>
	</table>

</body>
<script type="text/javascript">  
 function doSearch(){
	$('#tt').datagrid('load',{
		name: $('#name').val()
	});
}
 </script>
</html>