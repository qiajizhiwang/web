<!DOCTYPE html>
<html>
<head>
<#assign ctx= (request.contextPath)??/>
    <link href="../css/easyui.css" rel="stylesheet" />
    <link href="../css/icon.css" rel="stylesheet" />
    <script src="../js/jquery.min.js"></script>
    <script src="../js/jquery.easyui.min.js"></script>
    <script src="../js/easyui-lang-zh_CN.js"></script>
    
</head>

<body>
	<!-- <table id="dg" class="easyui-datagrid" style="width:100%;height:500px"
		url="searchStudents"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/student/studentList',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">学生ID</th>
				<th field="phone" width="50" editor="{type:'validatebox',options:{required:true}}">手机号</th>
				<th field="name" width="50" editor="text">姓名</th>
				<th field="gender" width="50" editor="text">性别</th>
				<th field="nation" width="50" editor="text">民族</th>
				<th field="state" width="50" editor="text">国家</th>
				<th field="major" width="50" editor="text">专业</th>
				<th field="grade" width="50" editor="text">级别</th>
				<th field="houseAddress" width="50" editor="text">家庭地址</th>
				<th field="homeTelephone" width="50" editor="text">父母电话</th>
				<th field="idCard" width="50" editor="text">身份证号码</th>
				<th field="status" width="50" editor="text">审核状态</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>老师姓名</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newStudent()">新增</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editStudent()">编辑</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyStudent()">删除</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<div class="fitem">
				<label>手机号</label>
				<input name="phone" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>登录密码</label>
				<input name="password" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>姓名</label>
				<input name="name">
			</div>
			<div class="fitem">
				<label>性别</label>
				<input name="gender">
			</div>
			<div class="fitem">
				<label>民族</label>
				<input name="nation">
			</div>
			<div class="fitem">
				<label>国家</label>
				<input name="state">
			</div>
			<div class="fitem">
				<label>专业</label>
				<input name="major">
			</div>
			<div class="fitem">
				<label>级别</label>
				<input name="grade">
			</div>
			<div class="fitem">
				<label>家庭地址</label>
				<input name="houseAddress">
			</div>
			<div class="fitem">
				<label>父母电话</label>
				<input name="homeTelephone">
			</div>
			<div class="fitem">
				<label>身份证号码</label>
				<input name="idCard">
			</div>
			<div class="fitem">
				<label>审核状态</label>
				<input name="status">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
	</div>
</body>
<script type="text/javascript">
	
	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val()
		});
	}

	function newStudent(){
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'saveStudent';
	}
	
	function editStudent(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','Edit User');
			$('#fm').form('load',row);
			url = 'editStudent?studentId='+row.studentId;
		}
	}
	
	function destroyStudent(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyStudent',{studentId:row.studentId},function(result){
						if (result.code==10000){
							$('#dg').datagrid('reload');	// reload the user data
						} else {
							$.messager.show({	// show error message
								title: 'Error',
								msg: result.errorMsg
							});
						}
					},'json');
				}
			});
		}
	}
	
	function saveUser(){
		$('#fm').form('submit',{
			url: url,
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.errorMsg){
					$.messager.show({
						title: 'Error',
						msg: result.errorMsg
					});
				} else {
					$('#dlg').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
	}
</script>
</html>