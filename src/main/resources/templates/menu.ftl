<!DOCTYPE html>
<html>

<head>
<#assign ctx= (request.contextPath)??/>

<#import "/selected.ftl" as selects/>
    <link href="/css/easyui.css" rel="stylesheet" />
    <link href="/css/icon.css" rel="stylesheet" />
    <script src="/js/jquery.min.js"></script>
    <script src="/js/jquery.easyui.min.js"></script>
     <script src="/js/easyui-lang-zh_CN.js"></script>
</head>
<body>



<div id="tb" style="padding:3px">
	
</div>

	
	<table id="tt" class="easyui-treegrid"  style="width:100%;height:auto"
	rownumbers="true"   fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/system/menuList',method:'post',idField:'id',treeField:'name'">
		<thead>
			<tr>
				<th data-options="field:'id',width:80">ID</th>
				<th data-options="field:'name',width:100">名称</th>
				<th data-options="field:'url',width:80,align:'right'">url</th>
			</tr>
		</thead>
	</table>
	
	

</body>
<script type="text/javascript">  


 </script>
</html>