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

<div id="tb" style="padding:3px">
	<span>账号</span>
	<input id="name" style="line-height:26px;border:1px solid #ccc">
	<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">查询</a>
	<#if userType == 0>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$('#addUser').window('open')">新增学校账号</a>
		</#if>
	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="$('#win').window('open')">新增校区账号</a>
</div>

	
	<table id="tt" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#tb"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/system/userList',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'id',width:'10%'">ID</th>
				<th data-options="field:'name',width:'30%'">登录名</th>
				<th data-options="field:'status',width:'10%',formatter:statusFormatter">状态</th>
				<th data-options="field:'schoolId',width:'10%'">分校id</th>
				<th data-options="field:'headquartersId',width:'10%'">学校id</th>
				<th data-options="field:'type',width:'10%',formatter:typeFormatter">类型</th>
				<th data-options="field:'_operate',width:'30%',formatter:rowFormatter">操作</th>
			</tr>
		</thead>
	</table>
	
	<div id="win" class="easyui-window" modal="true" title="新增用户"   closed="true"  style="width:40%;height:AUTO;padding:5px;">
    <form id="ff" method="post">
	    	<table cellpadding="5">
	    		<tr>
	    			<td>账号:</td>
	    			<td><input class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input class="easyui-textbox" type="text" name="password" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>状态:</td>
	    			<td>
                       <select class="easyui-combobox" name="status" style="width:40%;height:AUTO;padding:5px;">
	    			 <option selected="selected" value=1>开启</option>
                      <option value=0>停用</option>
                      </select></td>
	    		</tr>
	    		<tr>
	    			<td>校区:</td>
	    			<td>
            <@selects.select id="schoolId" datas=schools key="id" text="name"  defaultValue=mySchoolId/> 
	    			</td>
	    		</tr>
	    	
	    	</table>
	    
	    </form>
	      <div >
	      	<ul id="tt1"></ul>
	      </div>
	     <div style="text-align:center;padding:5px">
	     		
	     
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">保存</a>
	    </div>
</div>

<div id="addUser" class="easyui-window" modal="true" title="新增学校用户"   closed="true"  style="width:40%;height:AUTO;padding:5px;">
    <form id="addUserF" method="post">
	    	<table cellpadding="5">
	    		<tr>
	    			<td>账号:</td>
	    			<td><input class="easyui-textbox" type="text" name="name" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input class="easyui-textbox" type="text" name="password" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>状态:</td>
	    			<td>
                       <select class="easyui-combobox" name="status" style="width:40%;height:AUTO;padding:5px;">
	    			 <option selected="selected" value=1>开启</option>
                      <option value=0>停用</option>
                      </select></td>
	    		</tr>
	    		<tr>
	    			<td>学校:</td>
	    			<td>
            <@selects.select id="headquartersId" datas=headquarterss key="id" text="name"  defaultValue=myHeadquartersId/> 
	    			</td>
	    		</tr>
	    	
	    	</table>
	    
	    </form>
	     
	     <div style="text-align:center;padding:5px">
	     		
	     
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitaddUserForm()">保存</a>
	    </div>
</div>


<div id="edit" class="easyui-window" title="编辑用户"  modal="true" closed="true"  style="width:40%;height:AUTO;padding:5px;">
    <form id="ff1" method="post">
    <input hidden type="text" id ="userId" name="id" data-options="required:true"></input>
	    	<table cellpadding="5">
	    		<tr>
	    			<td>账号:</td>
	    			<td><input id="userName" class="easyui-textbox"  name="name" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input id="userPassword" class="easyui-textbox"  name="password" ></input></td>
	    		</tr>
	    		<tr>
	    			<td>状态:</td>
	    			<td>
                       <select id="userStatus" class="easyui-combobox" name="status" style="width:40%;height:AUTO;padding:5px;">
	    			 <option selected="selected" value=1>启用</option>
                      <option value=0>停用</option>
                      </select></td>
	    		</tr>
	    		<tr>
	    			<td>校区:</td>
	    			<td>
            <@selects.select id="schoolId"  datas=schools key="id" text="name"  defaultValue=mySchoolId/> 
	    			</td>
	    		</tr>
	    	
	    	</table>
	    
	    </form>
	      <div >
	      	<ul id="tt2"></ul>
	      </div>
	     <div style="text-align:center;padding:5px">
	     		
	     
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm1()">保存</a>
	    </div>
</div>


<div id="editUser" class="easyui-window" title="编辑用户"  modal="true" closed="true"  style="width:40%;height:AUTO;padding:5px;">
    <form id="ff2" method="post">
    <input hidden type="text" id ="userIdEdit" name="id" data-options="required:true"></input>
	    	<table cellpadding="5">
	    		<tr>
	    			<td>账号:</td>
	    			<td><input id="userNameEdit" class="easyui-textbox"  name="name" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input id="userPasswordEdit" class="easyui-textbox"  name="password" ></input></td>
	    		</tr>
	    		<tr>
	    			<td>状态:</td>
	    			<td>
                       <select id="userStatusEdit" class="easyui-combobox" name="status" style="width:40%;height:AUTO;padding:5px;">
	    			 <option selected="selected" value=1>启用</option>
                      <option value=0>停用</option>
                      </select></td>
	    		</tr>
	    		<tr>
	    			<td>校区:</td>
	    			<td>
            <@selects.select id="headquartersId" datas=headquarterss key="id" text="name"  defaultValue=myHeadquartersId/> 
	    			</td>
	    		</tr>
	    	
	    	</table>
	    
	    </form>
	  
	     <div style="text-align:center;padding:5px">
	     		
	     
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submiteditUserForm()">保存</a>
	    </div>
</div>

</body>
<script type="text/javascript">  
 $(document).ready(function(){  
 
  $("#tt").datagrid({
  
       onLoadSuccess:function(data){  
      $('.myedit').linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
            $('.mydestroy').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
      
      }
      
  })
 
 $('#ff').form({
    url: "addUser",
   onSubmit: function(param){
   var nodes = $('#tt1').tree('getChecked');
   var ids = new Array();
   for(var i = 0;i<nodes.length;i++){
   ids.push(nodes[i].id);
   }
		param.menus = ids;
    },
    success:function(data){
	var data = eval('(' + data + ')');
		if(data.status==1){
		  $('#win').window('close')
		  $('#tt').datagrid('reload')
		}
		else{
		alert("新增失败");
		}
    
    }
});

$('#tt1').tree({
    url:'/system/defaultMenu',
    onlyLeafCheck:true,
    checkbox:true
});


 $('#addUserF').form({
    url: "addHeadquartersUser",
    success:function(data){
	var data = eval('(' + data + ')');
		if(data.status==1){
		  $('#addUser').window('close')
		  $('#tt').datagrid('reload')
		}
		else{
		alert("新增失败");
		}
    
    }
});

$('#ff1').form({
    url: "editUser",
   onSubmit: function(param){
   var nodes = $('#tt2').tree('getChecked');
   var ids = new Array();
   for(var i = 0;i<nodes.length;i++){
   ids.push(nodes[i].id);
   }
		param.menus = ids;
    },
    success:function(data){
        var data = eval('(' + data + ')');
		if(data.status==1){
		  $('#edit').window('close')
		  $('#tt').datagrid('reload')
		}
		else{
		alert("编辑失败");
		}
    }
});

$('#ff2').form({
    url: "editHeadquartersUser",
   
    success:function(data){
        var data = eval('(' + data + ')');
		if(data.status==1){
		  $('#editUser').window('close')
		  $('#tt').datagrid('reload')
		}
		else{
		alert("编辑失败");
		}
    }
});


 });

function submitForm(){
  $('#ff').submit();
}

function submitaddUserForm(){
  $('#addUserF').submit();
}


function submitForm1(){
  $('#ff1').submit();
}

function submiteditUserForm(){
  $('#ff2').submit();
}


 function doSearch(){
	$('#tt').datagrid('load',{
		name: $('#name').val()
	});
}

function rowFormatter(value,row,index){  
               return "<a class='myedit' onclick='editRow("+index+")' href='javascript:void(0)' >编辑</a> <a class='mydestroy' onclick='destroyRow("+index+")'>删除</a>";  
 } 
 
 
 function statusFormatter(value,row,index){
 if(value == 0 )  
     return "停用"
 else if (value == 1 )   return "启用"
 } 
 
  function typeFormatter(value,row,index){
 if(value == 1 )  
     return "校区"
 else if (value == 2 )   return "学校"
 } 
 
 function destroyRow(index){
	$('#tt').datagrid('selectRow',index);
		var row = $('#tt').datagrid('getSelected');
		if (row){
			$.messager.confirm('删除','您确认要删除该数据吗?',function(r){
				if (r){
					$.post('destroyUser',{id:row.id},function(result){
						//var result = eval('('+result+')');
						if (result.status==1){
							$('#tt').datagrid('reload');	// reload the user data
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
 
function editRow(index){
$('#tt').datagrid('selectRow',index);
	var row = $('#tt').datagrid('getSelected');
		if (row){
		if(row.type==1){
        $("#userId").val(row.id);
        $("#userName").textbox("setValue",row.name);
        $("#userStatus").combobox('setValue',row.status);
     
         $("#edit #schoolId").combobox('setValue',row.schoolId);
    $('#tt2').tree({
    url:'/system/myMenu?userId='+row.id,
    onlyLeafCheck:true,
    checkbox:true
});
$('#edit').window('open')
 }
 else if(row.type==2){
        $("#userIdEdit").val(row.id);
        $("#userNameEdit").textbox("setValue",row.name);
        $("#userStatusEdit").combobox('setValue',row.status);
          $("#editUser #headquartersId").combobox('setValue',row.headquartersId);
$('#editUser').window('open')
 }
 }
 }
 

 </script>
</html>