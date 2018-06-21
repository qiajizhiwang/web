<!DOCTYPE html>
<html>
<head>
<#assign ctx= (request.contextPath)??/>
    <link href="../css/easyui.css" rel="stylesheet" />
    <link href="../css/icon.css" rel="stylesheet" />
    <script src="../js/jquery.min.js"></script>
    <script src="../js/jquery.easyui.min.js"></script>
    <script src="../js/easyui-lang-zh_CN.js"></script>
<script>
    $.parser.onComplete = function () {
       closes();
    } 

    function closes() {
        $('#loading').fadeOut('normal', function () {
            $(this).remove();
        });
    }
</script> 
</head>

<body>
<div id="loading" style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#DDDDDB;text-align :center;padding-top:20%;">
     <h1><font color="#15428B">加载中....</font></h1>
</div> 
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
				<th data-options="field:'imageUrl',width:100, formatter:showImg">图片</th>
				<th field="comment" width="50" editor="text">课程介绍</th>
				<th field="status" width="50" editor="text" data-options="formatter:statusFormatter">课程状态</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>课程名称</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<span>学校</span>
		<input id="searchrSchoolId" class="easyui-combobox" name="searchrSchoolId" data-options="valueField:'id',textField:'name',url:'../school/comboboxData'">
		<span>课程状态</span>
		<select class="easyui-combobox" id="status" style="width:100px;">
		 	<option value=''>--请选择--</option>
		 	<option value=1>已完结</option>
	      	<option value=0>正常</option>
	      </select></td>
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCourse()">新增</a>
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


	<div id="dlgedit" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px"
		closed="true" buttons="#dlg-buttonsedit">
		<form id="fmedit" method="post" enctype="multipart/form-data">
			<table cellpadding="5">
			<tr>
				<td>课程名称</td>
				<td><input name="name" class="easyui-textbox" required="true"></td>
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
			<tr>
    			<td>审核状态:</td>
    			<td>
                   <input type="radio" name="status" value="1"/>已完结
                   <input type="radio" name="status" value="0"/>正常
                </td>
    		</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttonsedit">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="editRowSave()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgedit').dialog('close')">关闭</a>
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
			name: $('#name').val(),
			searchrSchoolId: $('#searchrSchoolId').val(),
			status: $('#status').val()
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
	
	function destroyRow(index){
	$('#dg').datagrid('selectRow',index);
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

	function editRowSave(){
		$('#fmedit').form('submit',{
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
					$('#dlgedit').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
	}
	
	function editRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlgedit').dialog('open').dialog('setTitle','编辑');
			$('#fmedit').form('load',row);
			url = 'editCourse?id='+row.id;
		}
	}
	
	 $(document).ready(function(){  
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
      $('.mydestroy').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
      }
      
  })
  })
  
	function rowFormatter(value,row,index){  
               return "<a  class='myedit' onclick='editRow("+index+")' href='javascript:void(0)' >编辑</a> <a class='mydestroy' onclick='destroyRow("+index+")'>删除</a>";  
 	} 
 	
 	function showImg(value, row, index){  
	    if(row.imageUrl){  
	        return "<img style='width:100px;height:100px;' border='1' src='"+row.imageUrl+"'/>";  
	    }  
	}
	
	function statusFormatter(value,row,index){
	 if(value == 1 )  
	     return "已完结"
	 else if (value == 0 )   return "正常"
	 } 
</script>
</html>