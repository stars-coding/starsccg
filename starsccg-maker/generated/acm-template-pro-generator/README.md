# acm-template-pro-generator

> ACM 代码生成器
>
> 作者：stars
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
generator generate -l -a -o
```

## 参数说明

1）loop

类型：boolean

描述：是否循环

默认值：true

缩写：-l


2）author

类型：String

描述：作者注释

默认值："stars"

缩写：-a


3）outputText

类型：String

描述：输出信息

默认值："sum = "

缩写：-o


