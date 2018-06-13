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
		url="searchCourses"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/course/courseList',method:'post'">
	
		<thead>
			<tr>
				<th field="name" width="50" editor="{type:'validatebox',options:{required:true}}">课程名称</th>
				<th field="schoolName" width="50" editor="{type:'validatebox',options:{required:true}}">学校名称</th>
				<th field="teacherName" width="50" editor="{type:'validatebox',options:{required:true}}">教师名称</th>
				<th field="showCurriculumTime" width="50" editor="text">开课时间</th>
				<th field="schoolTime" width="50" editor="text">上课时间</th>
				<th field="imageUrl" width="50" editor="img">图片</th>
				<th field="comment" width="50" editor="text">课程介绍</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>学校</span>
		<input id="searchrSchoolId" class="easyui-combobox" name="searchrSchoolId" data-options="valueField:'id',textField:'name',url:'../school/comboboxData'">
		<span>课程姓名</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCourse()">新增</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editCourse()">编辑</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyCourse()">删除</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellpadding="5">
			<tr>
				<td>课程名称</td>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>
			
			<tr>  
	            <td align="right">学校</td>  
<td>  
    <input id="schoolIdCombox" class="easyui-combobox" />  
    <input id="schoolId" name="schoolId" qr="schoolId" required="true" type="hidden" class="queryRequired" />  
</td>   
	        </tr>  
	        <tr>  
	            <td align="right" >教师ID</td>  
<td>  
    <input id="teacherCombox" class="easyui-combobox" />  
    <input id="teacherId" name="teacherId" qr="teacherId" required="true" type="hidden" class="queryRequired" />  
</td>   
	        </tr>
	        
			<tr>
				<td>开课时间</td>
				<td><input name="showCurriculumTime" class="easyui-datebox"></td>
			</tr>
			<tr>
				<td>上课时间</td>
				<td><input name="schoolTime" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>照片</td>
				<td><input name="file" class="easyui-filebox" data-options="prompt:'Choose a file...'" style="width:100%"></td>
			</tr>
			<tr>
				<td>课程介绍</td>
				<td><input name="comment" class="easyui-textbox" data-options="multiline:true" style="width:100%;height:100px"></td>
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
	
	$(function(){ 
	
	$('#schoolIdCombox').combobox({  
    url: "../school/comboboxData",  
    editable:false,  
    valueField:'id',   
    textField:'name',  
    panelHeight:'auto',  
    onSelect : function(data){  
        $('#schoolId').val(data.id);  
        //查询类型  
        $('#teacherId').val('');  
        $('#teacherCombox').combobox({  
            url: "../teacher/comboboxData?schoolId="+$('#schoolId').val(),  
            editable:false,  
            valueField:'id',   
            textField:'name',  
            panelHeight:'auto',  
            width:100,  
        }).combobox('clear');  
    }  
});  

$('#teacherCombox').combobox({  
    onSelect : function(data){  
        $('#teacherId').val(data.id);  
    }  
}); 
 
    });
    
	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val()
		});
	}

	function newCourse(){
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'saveCourse';
	}
	
	function editCourse(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','Edit User');
			$('#fm').form('load',row);
			url = 'editCourse?id='+row.id;
		}
	}
	
	function destroyCourse(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyCourse',{id:row.id},function(result){
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