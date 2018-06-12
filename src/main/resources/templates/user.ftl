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
	<span>账号</span>
	<input id="name" style="line-height:26px;border:1px solid #ccc">
	<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">查询</a>
	<a href="#" class="easyui-linkbutton" plain="true" onclick="$('#win').window('open')">新增</a>
	
</div>

	
	<table id="tt" class="easyui-datagrid"  style="width:100%;height:auto"
	toolbar="#tb"	rownumbers="true" pagination="true"  fitColumns="true"	data-options="singleSelect:true,collapsible:true,url:'/system/userList',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'id',width:80">ID</th>
				<th data-options="field:'loginName',width:100">登录名</th>
				<th data-options="field:'password',width:80,align:'right'">密码</th>
			</tr>
		</thead>
	</table>
	
	<div id="win" class="easyui-window" title="新增用户"   closed="true"  style="width:40%;height:AUTO;padding:5px;">
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
	    			<td>学校:</td>
	    			<td>
            <@selects.select id="schoolId" datas=schools key="id" text="name"  defaultValue=defaultValue/> 
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





</body>
<script type="text/javascript">  
 $(document).ready(function(){  
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
		alert(data)
    }
});


$('#tt1').tree({
    url:'/system/myMenu',
    onlyLeafCheck:true,
    checkbox:true
});
 });

function submitForm(){
  $('#ff').submit();
}

 function doSearch(){
	$('#tt').datagrid('load',{
		name: $('#name').val()
	});
}
 </script>
</html>