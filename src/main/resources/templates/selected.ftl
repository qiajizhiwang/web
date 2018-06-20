<#macro select id datas  defaultValue="" key="" text="">
    <select style="width:100%;height:AUTO;padding:5px;" class="easyui-combobox" id="${id}" name="${id}" editable=false >
      <#if datas??>
        <#--判断对象是否为map-->
        <#if datas?is_hash_ex>
            <#--循环map的key值-->
            <#list datas?keys as key>
                <#--如果传进来的key值和默认的值相等，则选中这个值-->
                <#if key==defaultValue>
                    <option value="${key?c}" selected>${datas[key]}</option>
                <#else>
                    <option value="${key?c}">${datas[key]}</option>
                </#if>
            </#list>    
        <#else>
            <#list datas as data>
                <#--如果key值不为空-->
                <#if key!="">
                    <#--传进来的默认value和通过data的key取出来的值相等，则选中-->
                    
                    <#if defaultValue?string==data[key]?string>
                        <option value="${data[key]?c}" selected>${data[text]}</option>
                    <#else>
                        <option value="${data[key]?c}" >${data[text]}</option>
                    </#if>
                <#else>
                    <#if data==defaultValue>
                        <option value="${data?c}" selected>${data}</option>
                    <#else>
                        <option value="${data?c}">${data}</option>
                    </#if>
                </#if> 
            </#list>
        </#if>
        </#if>
    <select>
</#macro>