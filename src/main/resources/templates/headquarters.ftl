<!DOCTYPE html>
<html>
<head>
<#assign ctx= (request.contextPath)??/>
    <link href="../css/easyui.css" rel="stylesheet" />
    <link href="../css/icon.css" rel="stylesheet" />
    <script src="../js/jquery.min.js"></script>
    <script src="../js/jquery.easyui.min.js"></script>
    <script src="../js/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" href="../kindeditor/themes/default/default.css" />  
	<link rel="stylesheet" href="../kindeditor/plugins/code/prettify.css" />
<script src="../kindeditor/kindeditor-all.js"></script>  
<script charset="utf-8" src="../kindeditor/lang/zh-CN.js"></script> 
	<script charset="utf-8" src="../kindeditor/plugins/code/prettify.js"></script>
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
		url="searchSchools"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/headquarters/headquartersList',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">学校ID</th>
				<th field="name" width="50">名称</th>
				<th field="count" width="50">校区数量</th>
				<th field="linkman" width="50" editor="text">联系人</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>学校名称</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newSchool()">新增</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
				<td><label>学校名称</label>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td><label>联系电话</label></td>
				<td><input name="phone" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td><label>联系人</label></td>
				<td><input name="linkman" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td><label>校区数量</label></td>
				<td><input name="count"  required="true" class="easyui-numberbox"></td>
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
	var editor;  
		
		
	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val()
		});
	}

	function newSchool(){
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'saveHeadquarters';
	}
	
	function destroyRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyHeadquarters',{id:row.id},function(result){
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
					$.get("/", function(result){
					    
					  });
				}
			}
		});
	}
	
	
	function editRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','编辑');
			$('#fm').form('load',row);
			url = 'editHeadquarters?id='+row.id;
		}
	}

	
	
	 $(document).ready(function(){  
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
      $('.uploadImg').linkbutton({text:'上传图片',plain:true,iconCls:'icon-edit'});
      $('.mydestroy').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
      }
      
  })
  })
  
	function rowFormatter(value,row,index){  
               return "<a  class='myedit' onclick='editRow("+index+")' href='javascript:void(0)' >编辑</a> <a  class='mydestroy' onclick='destroyRow("+index+")' href='javascript:void(0)' >删除</a>";  
 	} 
</script>
</html>