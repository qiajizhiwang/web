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
	
	
		<table id="dg1" class="easyui-datagrid" title="学校业绩"
		style="width: 100%; height: auto" toolbar="#toolbar1" rownumbers="true"
		pagination="true" fitColumns="true"
		data-options="singleSelect:true,collapsible:true,url:'/statistics/financeAllList',method:'post'">

		<thead>
			<tr>

				<th field="schoolName" width="15%">学校</th>
				<th field="allFinance" width="15%">总收款</th>
				<th field="newFinance" width="10%">新签收款</th>
				<th field="reFinance" width="15%">续费收款</th>
				<th field="usedFinance" width="15%">已上课时费</th>
				<th field="remainFinance" width="15%">剩余课时费</th>

			</tr>
		</thead>
	</table>
	<div id="toolbar1">
	<form id="formId">
		<input id="startDate1" class="easyui-datebox"> <input
			id="endDate1" class="easyui-datebox"> <a href="#"
			class="easyui-linkbutton" iconCls="icon-search" plain="true"
			onclick="doSearch1()">查询</a>
	</form>		
	</div>


	<table id="dg" class="easyui-datagrid" title="校区业绩"
		style="width: 100%; height: auto" toolbar="#toolbar" rownumbers="true"
		pagination="true" fitColumns="true"
		data-options="singleSelect:true,collapsible:true,url:'/statistics/financeList',method:'post'">

		<thead>
			<tr>

				<th field="schoolName" width="15%">校区</th>
				<th field="allFinance" width="15%">总收款</th>
				<th field="newFinance" width="10%">新签收款</th>
				<th field="reFinance" width="15%">续费收款</th>
				<th field="usedFinance" width="15%">已上课时费</th>
				<th field="remainFinance" width="15%">剩余课时费</th>

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
	

	
	





</body>
<script type="text/javascript">
	function doSearch() {
		$('#dg').datagrid('load', {
			//$("#formId").serialize()
			 startDate : $('#startDate').datebox('getValue'),
			endDate : $('#endDate').datebox('getValue') 
		});
	}
	function doSearch1() {
		$('#dg1').datagrid('load', {
			//$("#formId").serialize()
			 startDate : $('#startDate1').datebox('getValue'),
			endDate : $('#endDate1').datebox('getValue') 
		});
	}
</script>


</html>