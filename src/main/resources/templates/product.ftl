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
	<!-- <table id="dg" class="easyui-datagrid" style="width:100%;height:500px"
		url="searchStudents"
		toolbar="#toolbar"
		rownumbers="true" fitColumns="true" singleSelect="true"> -->
		
	<table id="dg" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#toolbar"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/product/productList',method:'post'">
	
		<thead>
			<tr>
				<th field="id" width="50" editor="{type:'validatebox',options:{required:true}}">作品ID</th>
				<th field="name" width="50" editor="text">名称</th>
				<th field="courseName" width="50" editor="text">课程</th>
				<th field="studentName" width="50" editor="text">学生</th>
				<th data-options="field:'path',width:100, formatter:showImg">图片</th>
				<th field="remark" width="150" editor="text">评价</th>
				<th field="createTime" width="50" >上传时间</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar">
	   <span>  <@selects.select id="courseId" datas=courses key="id" text="name"  /> </span>
	  
		<span>作品名</span>
		<input id="name" style="line-height:26px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">搜索</a>
		
	
	</div>
	


</body>
<script type="text/javascript">

 $(document).ready(function(){  
  $("#dg").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({text:'查看作品',plain:true,iconCls:'icon-tip'});
      $('.mydestroy').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
      }
      
  })
  })
	
	 function doSearch(){
		$('#dg').datagrid('load',{
			name: $('#name').val()
		});
	}

	
	
	
	function rowFormatter(value,row,index){  
               return "<a  class='mydestroy' onclick='destroyRow("+index+")' href='javascript:void(0)' >删除</a>";  
 } 
 
 
  function statusFormatter(value,row,index){
 if(value == 0 )  
     return "停用"
 else if (value == 1 )   return "启用"
 } 
  
  
  function destroyRow(index){
		$('#dg').datagrid('selectRow',index);
			var row = $('#dg').datagrid('getSelected');
			if (row){
				$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
					if (r){
						$.post('product/destroyProduct',{productId:row.id},function(result){
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
 
function showImg(value, row, index){  
	    if(row.path){  
	        return "<img style='width:100px;height:100px;' border='1' src='"+row.path+"'/>";  
	    }  
	}
 
</script>


</html>