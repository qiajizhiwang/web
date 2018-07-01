<!doctype html>
<head>
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
        .login-btn {background:#2299ee;border:0;outline:0;padding:0;color:#fff;font-size: 14px;width:50%;border-radius:3px;margin: 0 auto;display: block;height:50px;line-height: 50px;cursor:pointer;}
.login-btn:hover{background:#187bc2;}
        
        body{  
 text-align:center;  
 } 
 .center{
position: fixed;
left: 30%;
top:20%;
}
        </style>
       <script type="text/javascript"> 
        $(function() {  
          
         var top = getTopWinow(); //获取当前页面的顶层窗口对象  
        if(top != window){  
            top.location.href = location.href; //跳转到登陆页面  
            //document.parent.ReLogin();  
        }    
          
    });  
    /*  
    *这个方法用来获取当前页面的最顶层对象  
    */  
    function getTopWinow(){    
        var p = window;    
        while(p != p.parent){    
            p = p.parent;    
        }    
        return p;    
    }  
    </script>
        </head>
<body background="css/images/login-bg0.jpg">
<div style="opacity:0.9;">
<div class="easyui-panel" title="登录" style="width:40%;height:AUTO" cls="center">
<form name='jvForm' action="/login" method="post">
       	<table cellpadding="10"  style="width:100%;height:AUTO">
	    		<tr>
	    			<td align="center">

            <input type="text" iconCls="icon-man"   style="width:50%;height:45px" class="easyui-textbox"  prompt="账号"  name="username">
        </td>
       <tr>
	    			<td align="center">
            <input type="password"  align="center"   style="width:50%;height:45px" class="easyui-textbox" iconCls="icon-lock" prompt="密码"  name="password" >
        </td>
        <tr><td>
            <input type="submit"  class="login-btn" id="btnLogin" value="登录"></input></td>
        </tr>
        <tr><td>
         <font color="red" id="errTip">
         <#if shiroLoginFailure??> 
账号密码不正确
</#if></td></tr>
        </font>
    </dl>

</form>
</div>
</div>
</body>
</html>