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
        body{  
 text-align:center;  
 } 
 .center{
position: fixed;
left: 30%;
top:20%;
}
        </style>
        </head>
<body>
<div>
<div class="easyui-panel" title="登录" style="width:40%" cls="center">
<form name ="jvForm" action="/login" method="post">
       	<table cellpadding="5">
	    		<tr><td>Name:</td>
	    			<td>

            <input type="text"  class="easyui-textbox"  placeholder="Login name"  name="username">
        </td>
       <tr><td>Name:</td>
	    			<td>
            <input type="password"  class="easyui-password"  placeholder="Password"  name="password" >
        </td>
        <tr><td>
            <a href="javascript:document.jvForm.submit();" class="easyui-linkbutton" id="btnLogin">Login</a></td>
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