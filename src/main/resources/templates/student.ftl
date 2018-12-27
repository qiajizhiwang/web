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
    
    
<style>.datagrid-cell{line-height:33px}</style>
    
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
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true" data-options="autoRowHeight:true,singleSelect:true,collapsible:true,url:'/student/studentList',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">学生ID</th>
<!-- 				<th field="phone" width="70" editor="{type:'validatebox',options:{required:true}}">手机号</th> -->
				<th data-options="field:'_operate0',width:'10%',formatter:nameFormatter">姓名</th>
				<th data-options="field:'_operate1',width:'10%',formatter:classFormatter">报读班级</th>
				<th data-options="field:'_operate2',width:'10%',formatter:productFormatter">作品档案</th>
				<th field="allCount" width="50" editor="text">购买课时</th>
				<th field="sendCount" width="50" editor="text">赠送课时</th>
				<th field="usedCount" width="50" editor="text">已上课时</th>
				<th field="remainCount" width="50" editor="text">剩余课时</th>
<!-- 				<th field="showBirthday" width="70" editor="text">生日</th>
				<th field="gender" width="50" editor="text">性别</th>
				<th field="nation" width="50" editor="text">民族</th>
				<th field="state" width="50" editor="text">国家</th>
				<th field="major" width="50" editor="text">专业</th>
				<th field="grade" width="50" editor="text">年级</th>
				<th field="classGrade" width="50" editor="text">班级</th>
				<th field="houseAddress" width="50" editor="text">家庭地址</th>
				<th field="homeTelephone" width="70" editor="text">父母电话</th>
				<th field="idCard" width="50" editor="text">身份证号码</th> 
				<th field="status" width="50" editor="text" data-options="formatter:statusFormatter">审核状态</th>-->
				<th data-options="field:'_operate3',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
		<span>学生姓名：</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newStudent()">新增</a><br/>
	<div>
		<form id="fmExcel" method="post" enctype="multipart/form-data">
		<span>批量导入学生：</span>
			<input id="excelFile" name="file" class="easyui-filebox" data-options="buttonText:'选择',prompt:'选择文件...'" style="width:15%">
			<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="uploadExcel()">导入</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="downloadExcel()">下载导入模板</a>
		</form>
	</div>
	</div>
	
	
	<div id="dlg" class="easyui-dialog" modal="true" style="width:400px;height:500px;padding:10px 20px" data-options="closed: true"
		 buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
			<tr>
				<td>学校</td>
				<td>
				   <@selects.select id="schoolId" datas=schools key="id" text="name"  defaultValue=mySchoolId/> 
                   </td>
			</tr>
			<tr>
				<td>手机号</td>
				<td><input name="phone" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>登录密码</td>
				<td><input id="password" name="password" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>姓名</td>
				<td><input name="name" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>生日</td>
				<td><input name="showBirthday" class="easyui-datebox" required="true"></td>
			</tr
			<tr>
				<td>性别</td>
				<td><input name="gender" class="easyui-textbox" required="true"></td>
			</tr>
			<tr>
				<td>民族</td>
				<td><input name="nation" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>国家</td>
				<td><input name="state" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>专业</td>
				<td><input name="major" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>年级</td>
				<td><input name="grade" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>班级</td>
				<td><input name="classGrade" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>家庭地址</td>
				<td><input name="houseAddress" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>父母电话</td>
				<td><input name="homeTelephone" class="easyui-textbox"></td>
			</tr>
			<tr>
				<td>身份证号码</td>
				<td><input name="idCard" class="easyui-textbox"></td>
			</tr>
		    <tr>
	    			<td>状态:</td>
	    			<td>
                       <select id="status" class="easyui-combobox" name="status" style="width:40%;height:AUTO;padding:5px;">
	    			 <option  selected="selected"  value=1>启用</option>
                      <option value=0>停用</option>
                      </select></td>
	    		</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
	</div>
	
		<div id="apply" class="easyui-dialog" modal="true" style="width:400px;height:500px;padding:10px 20px" data-options="closed: true"
		 buttons="#apply-buttons">
		<form id="fm1" method="post">
		    <input hidden type="text" id ="studentId" name="studentId" data-options="required:true"></input>
		
			<table cellpadding="5">
			<tr>
				<td>学校</td>
				<td>
				      <select id="schoolId1" name="schoolId" style="width:100%;height:AUTO;padding:5px;" class="easyui-combobox" editable=false >

                   </td>
			</tr>
			<tr>
				<td>课程</td>
				<td>      <select id="courseId" name="courseId" style="width:100%;height:AUTO;padding:5px;" class="easyui-combobox" editable=false >
 				</td>
			</tr>
			<tr>
				<td>类型</td>
				<td>      <select id="type" name="type" style="width:100%;height:AUTO;padding:5px;" class="easyui-combobox" editable=false >
				<option value ="1">新签</option>
  				<option value ="2">续费</option>
  				<option value="3">扩科</option>
  				</select>
 				</td>
			</tr>
			</table>
		</form>
	</div>
	<div id="apply-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveApply()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#apply').dialog('close')">关闭</a>
	</div>

	<div id="entryForm" class="easyui-dialog" modal="true" style="width:500px;height:500px;padding:10px 20px" data-options="closed: true"
		 buttons="#entryForm-buttons">
		<form id="entryFormfm" method="post">
			<table cellpadding="5">
			<tr>
				<td>学生姓名</td>
				<td>      
			    <input hidden type="text" id ="id" name="id" data-options="required:true"></input>
				
				<input name="name" id ="name" class="easyui-textbox" editable=false>
 				</td>
			</tr>
			<tr>
				<td>考试</td>
				<td>
				<input id="examId" class="easyui-combobox" name="examId" data-options="valueField:'id',textField:'subjectName',url:'../exam/comboboxData'">
 				</td>
			</tr>
			</table>
		</form>
		<table id="ldg" title="未缴费报名" class="easyui-datagrid"  style="width:100%;height:auto"
	 pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,method:'post'">
		<thead>
			<tr>
				<th field="id" width="5%">ID</th>
				<th field="major" width="20%" editor="{type:'validatebox',options:{required:true}}">专业</th>
				<th field="subjectName" width="20%" editor="{type:'validatebox',options:{required:true}}">科目</th>
				<th field="rank" width="15%" editor="text">考试级别</th>
				<th field="entryTime" width="35%" editor="text">报考时间</th>
				<th data-options="field:'_operate',width:'10%',formatter:examFormatter">操作</th>
			</tr>
		</thead>
	</table>
	</div>
	<div id="entryForm-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveEntryForm()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#entryForm').dialog('close')">关闭</a>
	</div>
	
		<div id="queryProduct" style="width:50%;height:AUTO;padding:10px 20px" class="easyui-window" modal="true"  data-options="closed: true">
		<table id="productTable" style="width:100%;height:AUTO"></table>
		</div>
	<div id="queryApply" style="width:50%;height:AUTO;padding:10px 20px" class="easyui-window" modal="true"  data-options="closed: true">
		<table id="applyTable" style="width:100%;height:AUTO"></table>
	
		</div>
		
		
		
	
		
		
</body>
<script type="text/javascript">

 $(document).ready(function(){  
 
// $("#apply #schoolId").combo({
// onChange: function(param){
 //    $("#apply #courseId").combobox({
  //  url:'/course/validCourses?schoolId='+$("#apply #schoolId").val()}) 
   // }
// }) 
 
 
 
  $("#applyTable").datagrid({
    onLoadSuccess:function(data){  
           $('.mydelete').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
 
     }
      
  })
  
   $("#productTable").datagrid({
    onLoadSuccess:function(data){  
        $('.mydelete').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});

     }
      
  })
  
    $("#ldg").datagrid({
    onLoadSuccess:function(data){  
           $('.mydelete').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
 
     }
      
  })
  
  
  
  
 
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
      $('.myapply').linkbutton({text:'报课',plain:true,iconCls:'icon-add'});
        $('.queryApply').linkbutton({text:'其他课程',plain:true,iconCls:'icon-search'});
        $('.queryProduct').linkbutton({text:'作品档案',plain:true,iconCls:'icon-search'});
        $('.entryForm').linkbutton({text:'报考',plain:true,iconCls:'icon-add'});
      
      
      }
      
  })
  })
	
	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val()
		});
	}

	function newStudent(){
			$("#password").textbox({ required:true  });
		$('#dlg').dialog('open').dialog('setTitle','新增');
		var defaultId = $('#dlg #schoolId').val();
		$('#fm').form('clear');
		$("#dlg #schoolId").combobox('select', defaultId);
		url = 'saveStudent';
	}
	
	function editRow(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
		$("#password").textbox({ required:false  });
			$('#dlg').dialog('open').dialog('setTitle','编辑');
			$('#fm').form('load',row);
			url = 'editStudent?studentId='+row.id;
		}
	}
	
	function queryApply(index){
	
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
		  $("#applyTable").datagrid({
    url:'/student/applyList?studentId='+row.id,
    rownumbers:true,
     pagination: true ,
       fitColumns:true,
       singleSelect:true,
    columns:[[
    	{field:'id',title:'Id'},
		{field:'name',title:'课程'},
		{field:'showCurriculumTime',title:'开课时间'},
		{field:'schoolTime',title:'上课时间'},
		{field:'teacherName',title:'老师'},
		{field:'_operate',title:'操作',width:'30%',formatter:deleteFormatter}
    ]]
});
		}
		$('#queryApply').dialog('open').dialog('setTitle','课程');
		
	}
	
	
	function queryProduct(index){
		
		$('#dg').datagrid('selectRow',index);
			var row = $('#dg').datagrid('getSelected');
			if (row){
			  $("#productTable").datagrid({
	    url:'/product/productList?studentId='+row.id,
	    rownumbers:true,
	     pagination: true ,
	       fitColumns:true,
	       singleSelect:true,
	    columns:[[
	    	{field:'id',title:'Id'},
			{field:'name',title:'名称'},
			{field:'courseName',title:'课程'},
			{field:'studentName',title:'学生'},
			{field:'createTime',title:'上传时间'},
			{field:'_operate1',title:'图片',formatter:showImg},
			{field:'_operate2',title:'操作',width:'30%',formatter:deleteProductFormatter}
	    ]]
	});
			}
			$('#queryProduct').dialog('open').dialog('setTitle','作品档案');
			
		}
	
	function showImg(value, row, index){  
	    if(row.path){  
	        return "<img style='width:100px;height:100px;' border='1' src='"+row.path+"'/>";  
	    }  
	}
	
	function apply(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
		$('#apply').dialog('open').dialog('setTitle','报课');
		$('#apply #studentId').val(row.id);
		$('#apply #schoolId1').combobox('loadData',[{
			value: row.schoolId,
			text: row.schoolName,
			selected:true
		}]);
		
		$('#apply #courseId').combobox({url:'/course/validCourses?schoolId='+row.schoolId});
		
		}
	}
	
	function deleteRow(){
		var row = $('#applyTable').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('deleteApply',{applyId:row.id},function(result){
						if (result.status == 1){
							$('#applyTable').datagrid('reload');	// reload the user data
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
	
	function deleteProduct(){
		var row = $('#productTable').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('/product/destroyProduct',{productId:row.id},function(result){
						if (result.code==10000){
							$('#productTable').datagrid('reload');	// reload the user data
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
	
	
	function deleteExam(){
		var row = $('#ldg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('/entryForm/deleteExam',{examId:row.id},function(result){
						if (result.status == 1){
							$('#ldg').datagrid('reload');	// reload the user data
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
	
	function destroyStudent(){
		var row = $('#dg').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyStudent',{studentId:row.id},function(result){
						if (result.status != 1){
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
	
	function saveUser(){
		$('#fm').form('submit',{
			url: url,
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.status != 1){
					$.messager.show({
						title: 'Error',
						msg: result.memo
					});
				} else {
					$('#dlg').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
			
			
	}
	
	function saveApply(){
		$('#fm1').form('submit',{
			url: "saveApply",
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.status != 1){
					$.messager.show({
						title: 'Error',
						msg: result.memo
					});
				} else {
					$('#apply').dialog('close');		// close the dialog
				$.messager.show({
						title: '报名成功'
					});
				}
			}
		});
	}
	
	
		function entryForm(index){
	$('#dg').datagrid('selectRow',index);
		var row = $('#dg').datagrid('getSelected');
		if (row){
		$('#entryForm').dialog('open').dialog('setTitle','报考');
		$('#entryFormfm').form('load',row);
		 $("#ldg").datagrid({
			     url: "/entryForm/entryFormListByStudentId?studentId="+row.id,
		 })
			$('#ldg').datagrid('reload')
		}
	}
	
		function saveEntryForm(){
		$('#entryFormfm').form('submit',{
			url: "saveEntryForm",
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.status != 1){
					$.messager.show({
						title: 'Error',
						msg: result.memo
					});
				} else {
					$('#entryForm').dialog('close');		// close the dialog
				$.messager.show({
						title: '报考成功'
					});
				}
			}
		});
	}
	
	function rowFormatter(value,row,index){  
               return "<a  class='myedit' onclick='editRow("+index+")' href='javascript:void(0)' >编辑</a>"+
               "<a  class='myapply' onclick='apply("+index+")' href='javascript:void(0)' >报课</a>"
                +"<a  class='entryForm' onclick='entryForm("+index+")' href='javascript:void(0)' >报考</a>";  
 } 
	
	function nameFormatter(value,row,index){  
        return     " <a  href='/student/studentInfo?id="+row.id+"' >"+row.name+"</a> ";
} 
	
	function classFormatter(value,row,index){  
		if(row.courseName === undefined || row.courseName== null)
	        return  "";
		else
        return     row.courseName+"  <br >  <a  class='queryApply' onclick='queryApply("+index+")' href='javascript:void(0)' >其他课程</a> <br /> ";
} 
	
	function productFormatter(value,row,index){  
        return    "<a  class='queryProduct' onclick='queryProduct("+index+")' href='javascript:void(0)' >作品</a>";
} 

 
 function deleteFormatter(value,row,index){  
               return "<a  class='mydelete' onclick='deleteRow("+index+")' href='javascript:void(0)' >删除</a>";
 
 } 
 
 function deleteProductFormatter(value,row,index){  
     return "<a  class='mydelete' onclick='deleteProduct("+index+")' href='javascript:void(0)' >删除</a>";

} 
 
 function examFormatter(value,row,index){  
     return "<a  class='mydelete' onclick='deleteExam("+index+")' href='javascript:void(0)' >删除</a>";

}
 
 
  function statusFormatter(value,row,index){
 if(value == 0 )  
     return "停用"
 else if (value == 1 )   return "启用"
 } 
 
 
 
	
	function uploadExcel(){
		$('#fmExcel').form('submit',{
			url: "uploadExcel",
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.code == 10000){
					$.messager.show({
						title: '导入成功！',
						msg: '批量导入学生成功！'
					});
				$('#dg').datagrid('reload');
				} else {
					$.messager.show({
						title: 'Error',
						msg: result.msg
					});
				}
				
				$('#excelFile').filebox({
					buttonText:'选择',
			        prompt:'选择文件...'
			    });
			}
		});
	}
	
	function downloadExcel(){
		window.location.href="downloadExcel";
	}
 
</script>


</html>