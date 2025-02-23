# ${name}

> ${description}
>
> 作者：${author}
>
> 一个可以在线个性化定制代码生成器的平台

可以通过命令行交互式输入的方式动态生成想要的项目代码

## 使用说明

执行项目根目录下的脚本文件：

```
generator <命令> <选项参数>
```

示例命令：

```
generator generate<#list modelConfig.models as modelInfo><#if modelInfo.abbr??> -${modelInfo.abbr}</#if></#list>
```

## 参数说明

<#list modelConfig.models as modelInfo>
${modelInfo?index + 1}）<#if modelInfo.fieldName??>${modelInfo.fieldName}</#if>

类型：${modelInfo.type}

描述：${modelInfo.description}

默认值：<#if modelInfo.defaultValue??>${modelInfo.defaultValue?c}</#if>

缩写：<#if modelInfo.abbr??>-${modelInfo.abbr}</#if>


</#list>
