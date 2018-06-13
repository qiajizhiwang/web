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
		url="searchExams"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/exam/examList',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">考试ID</th>
				<th field="subjectName" width="50" editor="{type:'validatebox',options:{required:true}}">科目</th>
				<th field="rank" width="50" editor="text">考试级别</th>
				<th field="money" width="50" editor="text">考试费用</th>
				<th field="examTime" width="50" editor="text">考试时间</th>
				<th field="examAddress" width="50" editor="text">考试地点</th>
				<th field="openFlag" width="50" editor="text">是否开放报名</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>科目</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newExam()">新增</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editExam()">编辑</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyExam()">删除</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
				<td>科目</td>
				<td>
				<input id="subjectId" class="easyui-combobox" name="subjectId" data-options="valueField:'id',textField:'name',url:'../subject/comboboxData'">
				</td>
			</tr>
			<tr>
				<td>考试级别</td>
				<td><input name="rank" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>考试费用</td>
				<td><input name="money" class="easyui-textbox" ></td>
			</tr>
			<tr>
				<td>考试时间</td>
				<td><input name="examTime" class="easyui-textbox" ></td>
			</tr>
			<tr>
				<td>考试地点</td>
				<td><input name="examAddress" class="easyui-textbox" ></td>
			</tr>
			<tr>
				<td>是否开放报名</td>
				<td><input name="openFlag" class="easyui-textbox" ></td>
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

	function newExam(){
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'saveExam';
	}
	
	function editExam(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','Edit User');
			$('#fm').form('load',row);
			url = 'editExam?examId='+row.id;
		}
	}
	
	function destroyExam(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyExam',{examId:row.id},function(result){
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