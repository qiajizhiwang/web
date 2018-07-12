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
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/entryForm/entryFormList',method:'post'">
	
		<thead>
			<tr>
				<th field="studentId" width="15" editor="{type:'validatebox',options:{required:true}}">学生id</th>
				<th field="studentName" width="50" editor="{type:'validatebox',options:{required:true}}">学生姓名</th>
				<th field="gender" width="20" editor="{type:'validatebox',options:{required:true}}">性别</th>
				<th field="birthday" width="70" editor="{type:'validatebox',options:{required:true}}">出生日期</th>
				<th field="nation" width="30" editor="{type:'validatebox',options:{required:true}}">民族</th>
				<th field="state" width="30" editor="{type:'validatebox',options:{required:true}}">国家</th>
				<th field="major" width="50" editor="{type:'validatebox',options:{required:true}}">专业</th>
				<th field="subjectName" width="50" editor="{type:'validatebox',options:{required:true}}">科目</th>
				<th field="rank" width="50" editor="text">考试级别</th>
				<th field="entryTime" width="70" editor="text">报考时间</th>
				<th field="payFlag" width="50" editor="text">缴费状态</th>
				<th field="payTime" width="50" editor="text">缴费时间</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>学生姓名</span>
		<input id="studentName" style="line-height:26px;border:1px solid #ccc">
		<span>考试科目</span>
		<input id="examId" class="easyui-combobox" name="examId" data-options="valueField:'id',textField:'subjectName',url:'../exam/comboboxData'">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="doExcle()">导出excel</a>
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
				<td>科目</td>
				<td>
				<input id="subjectId" class="easyui-combobox" name="subjectId" data-options="valueField:'id',textField:'name',url:'../subject/comboboxData'">
				</td>
			</tr>
			<tr>
				<td>考试级别</td>
				<td><input name="rank" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>考试费用</td>
				<td><input name="money" class="easyui-textbox" ></td>
			</tr>
			<tr>
				<td>考试时间</td>
				<td><input name="examTime" class="easyui-textbox" ></td>
			</tr>
			<tr>
				<td>考试地点</td>
				<td><input name="examAddress" class="easyui-textbox" ></td>
			</tr>
			<tr>
				<td>是否开放报名</td>
				<td><input name="openFlag" class="easyui-textbox" ></td>
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
	
	 function doSearch(){
		$('#dg').datagrid('load',{
			studentName: $('#studentName').val(),
			examId: $('#examId').val()
		});
	}

	 function doExcle(){
	 
	 	 
		window.location.href="doExcle?examId="+$('#examId').val();
	}

	
</script>
</html>