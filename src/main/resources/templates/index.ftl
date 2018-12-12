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
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
    <link href="css/easyui.css" rel="stylesheet" />
    <link href="css/icon.css" rel="stylesheet" />
    <script src="js/jquery.min.js"></script>
     <script src="js/jquery.easyui.min.js"></script>
        <script src="js/easyui-lang-zh_CN.js"></script>
    
    <style>
        .top-content {
        align-items: baseline;
        box-sizing: border-box;
        justify-content: space-between;
        box-sizing: border-box;
        padding: 0 15px;
        height: 100%;
        border-bottom: 1px solid #eee;
        box-shadow: 0 0 8px #eee;
    }
    
        .flex-wrap {
        display: flex;
    }
    </style>
    
</head>

<body>
<div class="easyui-layout" >
      <div id="north" region="north"  style="height:10%">
   <div class="flex-wrap top-content">
      <h3>管理系统</h3>
   
            <div>
                <ul>
                    <li>
                        <div style="margin-right: 16px;display: inline-block;font-size: 14px;"><label class="UserName" style="display:inline-block!important;">${username}</label>，正在使用系统...</div>
                        <a href="logout">退出</a>
                    </li>
                </ul>
            </div>
        </div>
      </div>
	<div region="west" split="true" title="菜单" style="width:15%">
		<ul id="tt" class="easyui-tree"
			url="/system/showMenu " method='get'><!--/system/showMenu-->
	</ul>
	</div>
	<div id="content" region="center"  >
	<div class="easyui-tabs" style="height:100%"></div>
	</div>
	 <div id="south" region="south"  style="height:5%;text-align: center;">
	 ©2018-2020 学校管理系统
	 </div>
</div>


</body>
<script type="text/javascript">  
    $(document).ready(function(){  
        var height1 = $(window).height()-20;  
        $(".easyui-layout").attr("style","width:100%;height:"+height1+"px");  
        $(".easyui-layout").layout("resize",{  
            width:"100%",  
            height:height1+"px"  
        });  
        
        
        $('#tt').tree({
			onClick: function(node){
	 var url=node.attributes.url;
	 if(url != null)
	 addTab(node.text,url)
	}
});
    });  
      
      
    $(window).resize(function(){  
        var height1 = $(window).height()-30;  
        $(".easyui-layout").attr("style","width:100%;height:"+height1+"px");  
        $(".easyui-layout").layout("resize",{  
            width:"100%",  
            height:height1+"px"  
        });  
    });   
    
    function addTab(title, url){
	if ($('.easyui-tabs').tabs('exists', title)){
		  $(".easyui-tabs").tabs("select", title); 
	      var selTab = $('.easyui-tabs').tabs('getSelected'); 
	      $('.easyui-tabs').tabs('update', { 
	        tab: selTab, 
	        options: { 
	          content:'<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'
	        } 
	      }) 
	    
		//$('.easyui-tabs').tabs('select', title);
	} else {
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
		$('.easyui-tabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
}
</script>  
</html>