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
	
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/student/studentList?id='+${student.id}+'',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="15%" editor="{type:'validatebox',options:{required:true}}">学生ID</th>
<!-- 				<th field="phone" width="70" editor="{type:'validatebox',options:{required:true}}">手机号</th> -->
				<th field="name"  width="10%">姓名</th>
				<th field="phone" width="15%" editor="text">联系方式</th>
				<th data-options="field:'_operate1',width:'10%',formatter:signFormatter">签到记录</th>
				<!-- <th data-options="field:'_operate2',width:'10%',formatter:productFormatter">沟通记录</th> -->
			</tr>
		</thead>
	</table>
	<div id="toolbar">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="returnIndex()">返回</a>
	</div>
	
	<div id="querySign" style="width:50%;height:AUTO;padding:10px 20px" class="easyui-window" modal="true"  data-options="closed: true">
		<table id="signTable" class="easyui-datagrid" style="width:100%;height:AUTO"></table>
	
		</div>

</body>
<script type="text/javascript">

 $(document).ready(function(){  
 
 
 
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
    //  $('.myedit').linkbutton({text:'签到记录',plain:true,iconCls:'icon-search'});
    
      }
      
  })
  })
  
  function returnIndex(){
	 //window.location.href = document.referrer;
	 window.history.back(-1);
 }

	
	function querySign(index){
	
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
		  $("#signTable").datagrid({
    url:'/courseSign/courseSignList?studentId='+row.id,
    rownumbers:true,
     pagination: true ,
       fitColumns:true,
       singleSelect:true,
    columns:[[
    	{field:'id',title:'Id'},
		{field:'showSignTime',title:'点名时间'},
		{field:'schoolTime',title:'上课时间'},
		{field:'courseName',title:'班级名称'},
		{field:'teacherName',title:'老师'},
		{field:'_operate1',title:'到课状态',formatter:singFlagFormatter},
		{field:'_operate2',title:'操作',width:'30%',formatter:updSingFormatter}
    ]]
});
		}
		$('#querySign').dialog('open').dialog('setTitle','课程');
		
	}
	
	function editCourseSign(index,signFlag){
	
	$('#signTable').datagrid('selectRow',index);
		var row = $('#signTable').datagrid('getSelected');
		if (row){
			$.messager.confirm('修改','您确认要修改该数据吗?',function(r){
				if (r){
					$.post('../courseSign/editcourseSignFlag',{id:row.id,signFlag:signFlag},function(result){
						if (result.code==10000){
							$('#signTable').datagrid('reload');	// reload the user data
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

	
	function signFormatter(value,row,index){  
               return "<a  class='myedit' onclick='querySign("+index+")' href='javascript:void(0)' >签到记录</a>";
 } 
 
 
 
 function singFlagFormatter(value,row,index){  
			if(row.signFlag == 0 )  
     			return "缺勤"
 			else if (row.signFlag == 1 )   
 				return "正常"
 			else if (row.signFlag == 2 )   
 				return "请假"
 			else if (row.signFlag == 3 )   
 				return "旷课"
 } 
 
	function updSingFormatter(value,row,index){  
			if(row.signFlag == 0 )  
     			return "<a  class='myedit' onclick='editCourseSign("+index+",1)' href='javascript:void(0)' >签到</a> <a  class='myedit' onclick='editCourseSign("+index+",2)' href='javascript:void(0)' >请假</a> <a  class='myedit' onclick='editCourseSign("+index+",3)' href='javascript:void(0)' >旷课</a>";
 			else if (row.signFlag == 1 )   
 				return "<a  class='myedit' onclick='editCourseSign("+index+",0)' href='javascript:void(0)' >缺勤</a> <a  class='myedit' onclick='editCourseSign("+index+",2)' href='javascript:void(0)' >请假</a> <a  class='myedit' onclick='editCourseSign("+index+",3)' href='javascript:void(0)' >旷课</a>";
 			else if (row.signFlag == 2 )   
 				return "<a  class='myedit' onclick='editCourseSign("+index+",0)' href='javascript:void(0)' >缺勤</a> <a  class='myedit' onclick='editCourseSign("+index+",1)' href='javascript:void(0)' >签到</a> <a  class='myedit' onclick='editCourseSign("+index+",3)' href='javascript:void(0)' >旷课</a>";
 			else if (row.signFlag == 3 )   
 				return "<a  class='myedit' onclick='editCourseSign("+index+",0)' href='javascript:void(0)' >缺勤</a> <a  class='myedit' onclick='editCourseSign("+index+",1)' href='javascript:void(0)' >签到</a> <a  class='myedit' onclick='editCourseSign("+index+",2)' href='javascript:void(0)' >请假</a>";
 } 
	
 
</script>


</html>