<!DOCTYPE html>
<html>
<head>
<#assign ctx= (request.contextPath)??/>
<#import "/selected.ftl" as selects/>

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
				<th field="status" width="50" editor="text" data-options="formatter:statusFormatter">审核状态</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>学生姓名</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newStudent()">新增</a>
	
	</div>
	
	<div id="dlg" class="easyui-dialog" modal="true" style="width:400px;height:500px;padding:10px 20px" data-options="closed: true"
		 buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
				<td>学校</td>
				<td>
				   <@selects.select id="schoolId" datas=schools key="id" text="name"  defaultValue=mySchoolId/> 
                   </td>
			</tr>
			<tr>
				<td>手机号</td>
				<td><input name="phone" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>登录密码</td>
				<td><input id="password" name="password" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>姓名</td>
				<td><input name="name" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>性别</td>
				<td><input name="gender" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>民族</td>
				<td><input name="nation" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>国家</td>
				<td><input name="state" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>专业</td>
				<td><input name="major" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>级别</td>
				<td><input name="grade" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>家庭地址</td>
				<td><input name="houseAddress" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>父母电话</td>
				<td><input name="homeTelephone" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>身份证号码</td>
				<td><input name="idCard" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>审核状态</td>
				<td><input name="status" class="easyui-textbox"></td>
			</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
	</div>
	
		<div id="apply" class="easyui-dialog" modal="true" style="width:400px;height:500px;padding:10px 20px" data-options="closed: true"
		 buttons="#apply-buttons">
		<form id="fm1" method="post">
		    <input hidden type="text" id ="studentId" name="studentId" data-options="required:true"></input>
		
			<table cellpadding="5">
			<tr>
				<td>学校</td>
				<td>
				      <select id="schoolId1" name="schoolId" style="width:100%;height:AUTO;padding:5px;" class="easyui-combobox" editable=false >

                   </td>
			</tr>
			<tr>
				<td>课程</td>
				<td>      <select id="courseId" name="courseId" style="width:100%;height:AUTO;padding:5px;" class="easyui-combobox" editable=false >
 </td>
			</tr>
			</table>
		</form>
	</div>
	<div id="apply-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveApply()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#apply').dialog('close')">关闭</a>
	</div>

</body>
<script type="text/javascript">

 $(document).ready(function(){  
 
// $("#apply #schoolId").combo({
// onChange: function(param){
 //    $("#apply #courseId").combobox({
  //  url:'/course/validCourses?schoolId='+$("#apply #schoolId").val()}) 
   // }
// }) 
 
 
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
      $('.myapply').linkbutton({text:'报名',plain:true,iconCls:'icon-add'});
      
      }
      
  })
  })
	
	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val()
		});
	}

	function newStudent(){
			$("#password").textbox({ required:true  });
		$('#dlg').dialog('open').dialog('setTitle','新增');
		var defaultId = $('#dlg #schoolId').val();
		$('#fm').form('clear');
		$("#dlg #schoolId").combobox('select', defaultId);
		url = 'saveStudent';
	}
	
	function editRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
		$("#password").textbox({ required:false  });
			$('#dlg').dialog('open').dialog('setTitle','Edit User');
			$('#fm').form('load',row);
			url = 'editStudent?studentId='+row.id;
		}
	}
	
	function apply(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
		$('#apply').dialog('open').dialog('setTitle','报名');
		$('#apply #studentId').val(row.id);
		$('#apply #schoolId1').combobox('loadData',[{
			value: row.schoolId,
			text: row.schoolName,
			selected:true
		}]);
		
		$('#apply #courseId').combobox({url:'/course/validCourses?schoolId='+row.schoolId});
		
		}
	}
	
	function destroyStudent(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyStudent',{studentId:row.id},function(result){
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
					$('#apply').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
			
			
	}
	
	function saveApply(){
		$('#fm1').form('submit',{
			url: "saveApply",
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.status != 0){
					$.messager.show({
						title: 'Error',
						msg: result.memo
					});
				} else {
					$('#dlg').dialog('close');		// close the dialog
				$.messager.show({
						title: '报名成功'
					});
				}
			}
		});
	}
	
	function rowFormatter(value,row,index){  
               return "<a  class='myedit' onclick='editRow("+index+")' href='javascript:void(0)' >编辑</a>"+
               "<a  class='myapply' onclick='apply("+index+")' href='javascript:void(0)' >报名</a>";  
 } 
 
 
  function statusFormatter(value,row,index){
 if(value == 0 )  
     return "停用"
 else if (value == 1 )   return "启用"
 } 
 
</script>


</html>