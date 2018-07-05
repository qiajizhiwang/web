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

		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/advert/advertList',method:'post'">
	
		<thead>
			<tr>
				<th field="name" width="50" editor="{type:'validatebox',options:{required:true}}">广告名</th>
				<th field="num" width="50" editor="{type:'validatebox',options:{required:true}}">排序</th>
				<th data-options="field:'path',width:100, formatter:showImg">图片</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		  <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newAdvert()">新增</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellpadding="5">
			<tr>
				<td>广告名</td>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>	
	        <tr>
				<td>排序</td>
				<td><input name="num" class="easyui-textbox" required="true"></td>
			</tr>
			
			<tr>
				<td>图片</td>
				<td><input name="file" class="easyui-filebox" required="true" data-options="prompt:'Choose a file...'" style="width:100%"></td>
			</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAdvert()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
	</div>


	<div id="dlgedit" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px"
		closed="true" buttons="#dlg-buttonsedit">
		<form id="fmedit" method="post" enctype="multipart/form-data">
			<table cellpadding="5">
			<tr>
				<td>广告名</td>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>	
	        <tr>
				<td>排序</td>
				<td><input name="num" class="easyui-textbox" required="true"></td>
			</tr>
			
			<tr>
				<td>图片</td>
				<td><input name="file" class="easyui-filebox" required="true" data-options="prompt:'Choose a file...'" style="width:100%"></td>
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
	

 
    
	
	function newAdvert(){
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'advert/saveAdvert';
	}
	
	function editAdvert(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','编辑');
			$('#fm').form('load',row);
			url = 'advert/editAdvert?id='+row.id;
		}
	}
	
	function destroyRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('advert/destroyAdvert',{id:row.id},function(result){
						var result = eval('('+result+')');
						if (result.status==1){
							$('#dg').datagrid('reload');	// reload the user data
						} else {
							$.messager.show({	// show error message
								title: 'Error',
								msg: result.memo
							});
						}
					},'json');
				}
			});
		}
	}
	
	function saveAdvert(){
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
			url = 'editAdvert?id='+row.id;
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
	    if(row.path){  
	        return "<img style='width:100px;height:100px;' border='1' src='"+row.path+"'/>";  
	    }  
	}
	
	function statusFormatter(value,row,index){
	 if(value == 1 )  
	     return "已完结"
	 else if (value == 0 )   return "正常"
	 } 
</script>
</html>