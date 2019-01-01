<!DOCTYPE html>
<html>
<head>
<#assign ctx= (request.contextPath)??/> <#import "/selected.ftl" as
selects/>

<link href="../css/easyui.css" rel="stylesheet" />
<link href="../css/icon.css" rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.easyui.min.js"></script>
<script src="../js/easyui-lang-zh_CN.js"></script>
<script>
	$.parser.onComplete = function() {
		closes();
	}

	function closes() {
		$('#loading').fadeOut('normal', function() {
			$(this).remove();
		});
	}
</script>
</head>

<body>
	<div id="loading"
		style="position: absolute; z-index: 1000; top: 0px; left: 0px; width: 100%; height: 100%; background: #DDDDDB; text-align: center; padding-top: 20%;">
		<h1>
			<font color="#15428B">加载中....</font>
		</h1>
	</div>


	<table id="dg" class="easyui-datagrid"
		style="width: 100%; height: auto" toolbar="#toolbar" rownumbers="true"
		pagination="true" fitColumns="true"
		data-options="singleSelect:true,collapsible:true,url:'/statistics/teacherPeriodData',method:'post'">

		<thead>
			<tr>

				<th field="teacherName" width="15%">教师</th>
				<th field="weekPeriod" width="15%">本周课时</th>
				<th field="thisMonthPeriod" width="10%">本月课时</th>
				<th field="lastMonthPeriod" width="15%">上月课时</th>

			</tr>
		</thead>
	</table>
	<div id="toolbar">
	<form id="formId">
		<input id="startDate" class="easyui-datebox"> <input
			id="endDate" class="easyui-datebox"> <a href="#"
			class="easyui-linkbutton" iconCls="icon-search" plain="true"
			onclick="doSearch()">查询</a>
	</form>		
	</div>

	<div id="querySign"
		style="width: 50%; height: AUTO; padding: 10px 20px"
		class="easyui-window" modal="true" data-options="closed: true">
		<table id="signTable" style="width: 100%; height: AUTO"></table>

	</div>

</body>
<script type="text/javascript">
	function doSearch() {
		
		$("#signTable").datagrid({
    url:'/statistics/teacherPeriodData?startDate='+$('#startDate').datebox('getValue')+'&endDate='+$('#endDate').datebox('getValue'),
    rownumbers:true,
     pagination: true ,
       fitColumns:true,
       singleSelect:true,
    columns:[[
		{field:'teacherName',title:'教师'},
		{field:'period',title:'课时'}
    ]]
});
	$('#querySign').dialog('open').dialog('setTitle','课时');

	}
	
</script>


</html>