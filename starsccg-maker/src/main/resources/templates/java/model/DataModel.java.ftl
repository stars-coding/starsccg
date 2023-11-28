package ${basePackage}.model;

/**
 * 数据模型
 *
<#if author??> * @author ${author}</#if>
<#if version??> * @version ${version}</#if>
 */
public class DataModel {
<#list modelConfig.models as modelInfo>

    <#if modelInfo.description??>
    /**
     * ${modelInfo.description}
     */
    </#if>
    public ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
</#list>
}
