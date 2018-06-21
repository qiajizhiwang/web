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
		url="searchTeachers"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/teacher/teacherList',method:'post'">
	
		<thead>
			<tr>
				<th field="schoolCode" width="50" editor="{type:'validatebox',options:{required:true}}">学校编码</th>
				<th field="schoolName" width="50" editor="{type:'validatebox',options:{required:true}}">学校</th>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">教师ID</th>
				<th field="name" width="50" editor="text">姓名</th>
				<th field="phone" width="50" editor="text">电话</th>
				<th field="status" width="50" editor="text" data-options="formatter:statusFormatter">审核状态</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>老师姓名</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<span>审核状态</span>
		<select class="easyui-combobox" id="status" style="width:100px;">
		 	<option value=''>--请选择--</option>
		 	<option value=1>停用</option>
	      	<option value=0>启用</option>
	      </select></td>
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newTeacher()">新增</a>
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
				<td><input name="phone" class="easyui-textbox" data-options="prompt:'请输入正确的手机号码。',validType:'phoneNum'"></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><input id="password" name="password" validType="length[6,32]" class="easyui-textbox" required="true" type="password" value=""/></td>
			</tr>
			<tr>
				<td>确认密码</td>
				<td><input type="password" name="repassword" id="repassword" required="true" class="easyui-textbox"  validType="equalTo['#password']" invalidMessage="两次输入密码不匹配"/></td>
			</tr>
			<tr>
    			<td>审核状态:</td>
    			<td>
                   <input type="radio" name="status" value="1"/>停用
                   <input type="radio" name="status" value="0"/>启用
                </td>
    		</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
	</div>


	<div id="dlgeditTeacher" class="easyui-dialog" style="width:400px;height:350px;padding:10px 20px"
		closed="true" buttons="#dlg-buttonseditTeacher">
		<form id="fmeditTeacher" method="post">
			<table cellpadding="5">
			<tr>
				<td>姓名</td>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>电话</td>
				<td><input name="phone" class="easyui-textbox" data-options="prompt:'请输入正确的手机号码。',validType:'phoneNum'"></td>
			</tr>
			<tr>
    			<td>审核状态:</td>
    			<td>
                   <input type="radio" name="status" value="1"/>停用
                   <input type="radio" name="status" value="0"/>启用
                </td>
    		</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttonseditTeacher">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="editTeacherSave()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgeditTeacher').dialog('close')">关闭</a>
	</div>
	
	
	
	<div id="dlgpwd" class="easyui-dialog" style="width:400px;height:350px;padding:10px 20px"
		closed="true" buttons="#dlg-buttonspwd">
		<form id="fmpwd" method="post">
			<table cellpadding="5">
			<tr>
				<td>密码</td>
				<td><input id="passwordupd" name="password" validType="length[6,32]" class="easyui-textbox" required="true" type="password" value=""/></td>
			</tr>
			<tr>
				<td>确认密码</td>
				<td><input type="password" name="repassword" id="repasswordupd" required="true" class="easyui-textbox"  validType="equalTo['#passwordupd']" invalidMessage="两次输入密码不匹配"/></td>
			</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttonspwd">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="updatepwd()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgpwd').dialog('close')">关闭</a>
	</div>
</body>
<script type="text/javascript">
	
	$.extend($.fn.validatebox.defaults.rules, {    
    phoneNum: { //验证手机号   
        validator: function(value, param){ 
         return /^1[3-8]+\d{9}$/.test(value);
        },    
        message: '请输入正确的手机号码。'   
    },
    /*必须和某个字段相等*/
    equalTo: {
        validator:function(value,param){
            return $(param[0]).val() == value;
        },
        message:'字段不匹配'
    }
});


	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val(),
			status: $('#status').val()
		});
	}

	function initSchoolCombobox(){
		$('#schoolId').combobox({
	        // url:'../school/comboboxData',
	        // valueField:'id',
	        // textField:'name',
			onLoadSuccess: function (data) {
			if (data) {
			   $('#schoolId').combobox('setValue',data[0].id);
			}
			}
			});
	}
	
	function newTeacher(){
		initSchoolCombobox();
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'saveTeacher';
	}
	
	function destroyRow(index){
	$('#dg').datagrid('selectRow',index);
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

	function updatepwd(){
		$('#fmpwd').form('submit',{
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
					$('#dlgpwd').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
	}

	function editTeacherSave(){
		$('#fmeditTeacher').form('submit',{
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
					$('#dlgeditTeacher').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
	}
	
	function editRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlgeditTeacher').dialog('open').dialog('setTitle','编辑');
			$('#fmeditTeacher').form('load',row);
			url = 'editTeacher?teacherId='+row.id;
		}
	}

	function editRowPassword(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlgpwd').dialog('open').dialog('setTitle','修改密码');
			$('#fmpwd').form('clear');
			// $('#fmpwd').form('load',row);
			url = 'editTeacher?teacherId='+row.id;
		}
	}
	
	 $(document).ready(function(){  
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({plain:true,iconCls:'icon-edit'});
      $('.mydestroy').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
      }
      
  })
  })
  
	function rowFormatter(value,row,index){  
  //<a class='mydestroy' onclick='destroyRow("+index+")'>删除</a> 
               return "<a class='myedit' onclick='editRow("+index+")'>编辑</a> <a class='myedit' onclick='editRowPassword("+index+")'>修改密码</a>";  
 	} 
 	
 	  function statusFormatter(value,row,index){
 if(value == 1 )  
     return "停用"
 else if (value == 0 )   return "启用"
 } 
 
</script>
</html>