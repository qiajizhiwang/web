<#macro select id datas value="" defaultValue="" key="" text="">
    <select style="width:100%;height:AUTO;padding:5px;" class="easyui-combobox" id="${id}" name="${id}">
      <#if datas??>
        <#--判断对象是否为map-->
        <#if datas?is_hash_ex>
            <#--循环map的key值-->
            <#list datas?keys as key>
                <#--如果传进来的key值和默认的值相等，则选中这个值-->
                <#if key==value>
                    <option value="${key}" selected>${datas[key]}</option>
                <#else>
                    <option value="${key}">${datas[key]}</option>
                </#if>
            </#list>    
        <#else>
            <#list datas as data>
                <#--如果key值不为空-->
                <#if key!="">
                    <#--传进来的默认value和通过data的key取出来的值相等，则选中-->
                    <#if value==data[key]?string>
                        <option value="${data[key]}" selected>${data[text]}</option>
                    <#else>
                        <option value="${data[key]}" >${data[text]}</option>
                    </#if>
                <#else>
                    <#if data==value>
                        <option value="${data}" selected>${data}</option>
                    <#else>
                        <option value="${data}">${data}</option>
                    </#if>
                </#if> 
            </#list>
        </#if>
        </#if>
    <select>
</#macro>