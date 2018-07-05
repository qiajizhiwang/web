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
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/contact/contactList',method:'post'">
	
		<thead>
			<tr>
				<th field="name" width="50" editor="{type:'validatebox',options:{required:true}}">姓名</th>
				<th field="phone" width="50" editor="{type:'validatebox',options:{required:true}}">电话</th>
				<th field="text" width="50">内容</th>
				<th field="createTime" width="50">时间</th>
			</tr>
		</thead>
	</table>
	
	
</body>
<script type="text/javascript">
	
	
</script>
</html>