<!doctype html>
<body>
<form name="jvForm" action="/login" method="post">
<div class="loginbox round15">
    <dl>
        <dd class="fz24 pt30">User login</dd>
        <dd>
            <input type="text" placeholder="Login name" autocomplete="off" name="username">
        </dd>
        <dd>
            <input type="password" placeholder="Password" autocomplete="off" name="password" title="click enter login">
        </dd>
        <dd>
            <a href="javascript:document.jvForm.submit();" class="login-bt round3" id="btnLogin">Login</a>
        </dd>
         <font color="red" id="errTip">${message!}</font>
    </dl>
</div>
</form>
</body>
</html>