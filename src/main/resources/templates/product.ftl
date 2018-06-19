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
	<!-- <table id="dg" class="easyui-datagrid" style="width:100%;height:500px"
		url="searchStudents"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/product/productList',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">作品ID</th>
				<th field="name" width="50" editor="text">名称</th>
				<th field="courseName" width="50" editor="text">课程</th>
				<th field="studentName" width="50" editor="text">学生</th>
				<th field="createTime" width="50" editor="text">上传时间</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
	   <span>  <@selects.select id="courseId" datas=courses key="id" text="name"  /> </span>
	  
		<span>作品名</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newStudent()">新增</a>
	
	</div>
	
	<div id="dlg" class="easyui-dialog" modal="true" style="width:400px;height:500px;padding:10px 20px" data-options="closed: true"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
				<td>手机号</td>
				<td><input name="phone" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>登录密码</td>
				<td><input name="password" class="easyui-textbox" required="true"></td>
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

</body>
<script type="text/javascript">

 $(document).ready(function(){  
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
      }
      
  })
  })
	
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
	
	function editRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','Edit User');
			$('#fm').form('load',row);
			url = 'editStudent?studentId='+row.id;
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
					$('#dlg').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
	}
	
	function rowFormatter(value,row,index){  
               return "<a  class='myedit' onclick='editRow("+index+")' href='javascript:void(0)' >编辑</a>";  
 } 
 
 
  function statusFormatter(value,row,index){
 if(value == 0 )  
     return "停用"
 else if (value == 1 )   return "启用"
 } 
 
</script>


</html>