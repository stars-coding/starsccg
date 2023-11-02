import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author stars
 * @version 1.0
 */
public class FreeMarkerTest {

    @Test
    public void test() throws IOException, TemplateException {
        // 创建 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 指定模板文件所在的路径
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");

        // 创建模板对象，加载指定模板
        Template template = configuration.getTemplate("myweb.html.ftl");

        // 创建数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("currentYear", 2023);
        List<Map<String, Object>> menuItems = new ArrayList<>();
        Map<String, Object> menuItem1 = new HashMap<>();
        menuItem1.put("url", "https://www.baidu.com");
        menuItem1.put("label", "百度搜索");
        Map<String, Object> menuItem2 = new HashMap<>();
        menuItem2.put("url", "https://www.bing.com");
        menuItem2.put("label", "必应搜索");
        menuItems.add(menuItem1);
        menuItems.add(menuItem2);
        dataModel.put("menuItems", menuItems);

        // 生成文件，指定最终的文件名称
        Writer out = new FileWriter("myweb.html");
        template.process(dataModel, out);

        // 关闭输出流资源
        out.close();
    }
}
