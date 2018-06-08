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
    
</head>

<body>
<div class="easyui-layout" >
      <div id="north" region="north"  style="height:5%">
      </div>
	<div region="west" split="true" title="菜单" style="width:15%">
		<ul id="tt" class="easyui-tree"
			url="data.json " method='get'><!--/system/showMenu-->
	</ul>
	</div>
	<div id="content" region="center"  >
	<div class="easyui-tabs" style="height:100%"></div>
	</div>
	 <div id="south" region="south"  style="height:5%">
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
		$('.easyui-tabs').tabs('select', title);
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