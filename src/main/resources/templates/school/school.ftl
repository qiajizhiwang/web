<!DOCTYPE html>
<html>
<head>
    <title>相杏</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <meta name="description" content="相杏" />
    <meta content="相杏" name="keywords" />
    <meta charset="utf-8">
    <meta content='text/html;charset=utf-8' http-equiv='Content-Type'>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <link rel="shortcut icon" href="../images/favicon.ico" type="../image/x-icon" />
    <link href="../css/easyui.css" rel="stylesheet" />
    <link href="../css/icon.css" rel="stylesheet" />
    <script src="../js/jquery.min.js"></script>
    <script src="../js/jquery.easyui.min.js"></script>
    <script src="../js/easyui-lang-zh_CN.js"></script>
    
</head>

<body>
	<table id="dg" class="easyui-datagrid" style="width:100%;height:500px"
		url="searchSchools"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true">
		<thead>
			<tr>
				<th field="schoolId" width="50" editor="{type:'validatebox',options:{required:true}}">学校ID</th>
				<th field="code" width="50" editor="{type:'validatebox',options:{required:true}}">自定义编码</th>
				<th field="name" width="50" editor="text">学校名称</th>
				<th field="adress" width="50" editor="{type:'validatebox',options:{validType:'email'}}">地址</th>
				<th field="linkman" width="50" editor="text">联系人</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">新增</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()">删除</a>
	</div>
</body>
</html>