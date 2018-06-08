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
    
</head>

<body>
	<!-- <table id="dg" class="easyui-datagrid" style="width:100%;height:500px"
		url="searchSchools"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/school/schoolList',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">学校ID</th>
				<th field="code" width="50" editor="{type:'validatebox',options:{required:true}}">自定义编码</th>
				<th field="name" width="50" editor="text">学校名称</th>
				<th field="adress" width="50" editor="{type:'validatebox',options:{validType:'email'}}">地址</th>
				<th field="linkman" width="50" editor="text">联系人</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>学校名称</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newSchool()">新增</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editSchool()">编辑</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroySchool()">删除</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:1000px;height:600px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
				<td>学校代码</td>
				<td><input name="code" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td><label>学校名称</label>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td><label>地址</label></td>
				<td><input name="adress" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td><label>联系人</label></td>
				<td><input name="linkman" class="easyui-textbox"></td>
			</tr>
			<!-- 
			<tr>
				<td><label>学校介绍</label></td>
				<td><input name="comment" class="easyui-textbox" data-options="multiline:true" style="width:500px;height:200px"></td>
			</tr>
			-->
			<tr>
				<td><label>学校介绍</label></td>
				<td>
				<textarea rows="3" style="width:800px;height:400px;" id="comment" name="comment" class="easyui-validatebox" data-options="required:true,validType:'length[1,1000000]'" invalidMessage="最大长度不能超过1000000""></textarea>
				</td>
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
    $(function() {  
          editor = KindEditor.create('textarea[name="comment"]',{resizeType : 1,width:"100%",height:"400px",afterChange:function(){  
              this.sync();  
           },afterBlur:function(){  
               this.sync();  
           }});  
    });
    
		
	KindEditor.ready(function(K) {
            K.create('textarea[name="comment"]', {
                cssPath : '/kindeditor/plugins/code/prettify.css',
                uploadJson : '../uploadJson',
                fileManagerJson : '../fileManagerJson',
                allowFileManager : true,//是否允许浏览服务器上传文件
                //resizeType : 0,是否可改变编辑器大小0不可以，1可改高度，2都可以。默认为2
                afterCreate : function(msg) {
                    var self = this;
                    K.ctrl(document, 13, function() {
                        self.sync();
                        document.forms['example'].submit();
                    });
                    K.ctrl(self.edit.doc, 13, function() {
                        self.sync();
                        document.forms['example'].submit();
                    });
                }
            });
            prettyPrint();
        });
		
	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val()
		});
	}

	function newSchool(){
		$('#dlg').dialog('open').dialog('setTitle','新增');
		$('#fm').form('clear');
		url = 'saveSchool';
	}
	
	function editSchool(){
		var row = $('#dg').datagrid('getSelected');
		
		KindEditor.html('#comment',row.comment);  
		
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','编辑');
			$('#fm').form('load',row);
			url = 'editSchool?id='+row.id;
		}
	}
	
	function destroySchool(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroySchool',{id:row.id},function(result){
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