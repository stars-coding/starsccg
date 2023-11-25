package com.stars.maker.meta;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * 元信息管理器
 *
 * @author stars
 * @version 1.0.0
 */
public class MetaManager {

    /**
     * 元信息
     */
    private static volatile Meta meta;

    /**
     * 无参构造器
     */
    private MetaManager() {
        // 私有构造函数，防止外部实例化
    }

    /**
     * 获取元信息对象
     *
     * @return
     */
    public static Meta getMetaObject() {
        if (MetaManager.meta == null) {
            synchronized (MetaManager.class) {
                if (MetaManager.meta == null) {
                    MetaManager.meta = initMeta();
                }
            }
        }
        return MetaManager.meta;
    }

    /**
     * 初始化元信息
     *
     * @return
     */
    private static Meta initMeta() {
        // 读取 JSON 文件
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        // 转化为元信息对象
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        // todo 校验和处理默认值
        return newMeta;
    }
}
