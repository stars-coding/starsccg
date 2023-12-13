package com.stars.maker.template;

import com.stars.maker.meta.Meta;
import com.stars.maker.template.model.TemplateMakerFileConfig;
import com.stars.maker.template.model.TemplateMakerModelConfig;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 模板制作测试
 *
 * @author stars
 * @version 1.0.0
 */
public class TemplateMakerTest {

    @Test
    public static void testMakeTemplateBug1() {
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");

        String projectPath = System.getProperty("user.dir");
        // 源项目路径，用户传入的源项目的路径
        String originProjectPath = new File(projectPath).getParent() + File.separator + "starsccg-demo-projects/springboot-init";
        // 输入文件路径，在源项目路径下的相对路径
        String inputFilePath1 = "src/main/java/com/stars/springbootinit/common";

        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        // 文件过滤
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1));

        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 100L);
        System.out.println(id);
    }
}
