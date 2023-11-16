package com.stars.maker.generator;

import com.stars.maker.meta.Meta;
import com.stars.maker.meta.MetaManager;

/**
 * @author stars
 * @version 1.0
 */
public class MainGenerator {

    public static void main(String[] args) {
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta);
    }
}
