package ${basePackage}.maker.model;

import lombok.Data;

/**
 * 数据模型
 *
<#if author??> * @author ${author}</#if>
<#if version??> * @version ${version}</#if>
 */
@Data
public class DataModel {
<#list modelConfig.models as modelInfo>

    <#if modelInfo.description??>
    /**
     * ${modelInfo.description}
     */
    </#if>
    private ${modelInfo.type} ${modelInfo.fieldName} <#if modelInfo.defaultValue??>=</#if> ${modelInfo.defaultValue?c};
</#list>
}
