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



	

	
    <form id="ff" method="post" action="/system/saveVersion">
	    	<table cellpadding="5">
	    		<tr>
	    			<td>版本:</td>
	    			<td><input value="${version}" ="easyui-textbox" type="number" name="version" data-options="required:true"></input></td>
	    			<td>	<input type="submit" class="easyui-linkbutton"></td>
	    		</tr>
	    		
	    	</table>
	    
	    </form>
	      <div >
	      	<ul id="tt1"></ul>
	      </div>
	    
	     		
	     
	    
	 






</body>
<script type="text/javascript">  
 $(document).ready(function(){  


 });



 

 </script>
</html>