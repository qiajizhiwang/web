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
		url="searchTeachers"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/teacher/teacherList',method:'post'">
	
		<thead>
			<tr>
				<th field="schoolId" width="50" editor="{type:'validatebox',options:{required:true}}">学校ID</th>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">教师ID</th>
				<th field="name" width="50" editor="text">姓名</th>
				<th field="phone" width="50" editor="text">电话</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>老师姓名</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newTeacher()">新增</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editTeacher()">编辑</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyTeacher()">删除</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:350px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
    			<td>学校:</td>
    			<td>
        			<input id="schoolId" class="easyui-combobox" name="schoolId" data-options="valueField:'id',textField:'name',url:'../school/comboboxData'">
    			</td>
    		</tr>
			<tr>
				<td>姓名</td>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>电话</td>
				<td><input name="phone" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><input name="password" class="easyui-textbox"></td>
			</tr>
			<tr>
    			<td>审核状态:</td>
    			<td>
                   <select class="easyui-combobox" name="status" style="width:100px;">
    			 	<option value=1>不通过</option>
                  	<option value=0>通过</option>
                  </select></td>
    		</tr>
			</table>
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

	function newTeacher(){
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'saveTeacher';
	}
	
	function editTeacher(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','Edit User');
			$('#fm').form('load',row);
			url = 'editTeacher?teacherId='+row.id;
		}
	}
	
	function destroyTeacher(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyTeacher',{teacherId:row.id},function(result){
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