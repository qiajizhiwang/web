<!DOCTYPE html>
<html>
<head>
<#assign ctx= (request.contextPath)??/>
    <title>相杏</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <meta name="description" content="相杏" />
    <meta content="相杏" name="keywords" />
    <meta charset="utf-8">
    <meta content='text/html;charset=utf-8' http-equiv='Content-Type'>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
    <link href="css/easyui.css" rel="stylesheet" />
    <link href="css/icon.css" rel="stylesheet" />
    <script src="js/jquery.min.js"></script>
     <script src="js/jquery.easyui.min.js"></script>
        <script src="js/easyui-lang-zh_CN.js"></script>
    
</head>

<h2>Basic Layout</h2>
	<p>The layout contains north,south,west,east and center regions.</p>
	<div style="margin:20px 0;"></div>
	<div class="easyui-layout" style="width:700px;height:350px;">
		<div data-options="region:'north'" style="height:50px"></div>
		<div data-options="region:'south',split:true" style="height:50px;"></div>
		<div data-options="region:'east',split:true" title="East" style="width:100px;"></div>
		<div data-options="region:'west',split:true" title="West" style="width:100px;"></div>
		<div data-options="region:'center',title:'Main Title',iconCls:'icon-ok'">
			<table class="easyui-datagrid"
					data-options="url:'datagrid_data1.json',method:'get',border:false,singleSelect:true,fit:true,fitColumns:true">
				<thead>
					<tr>
						<th data-options="field:'itemid'" width="80">Item ID</th>
						<th data-options="field:'productid'" width="100">Product ID</th>
						<th data-options="field:'listprice',align:'right'" width="80">List Price</th>
						<th data-options="field:'unitcost',align:'right'" width="80">Unit Cost</th>
						<th data-options="field:'attr1'" width="150">Attribute</th>
						<th data-options="field:'status',align:'center'" width="60">Status</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>


</body>

</html>