# LedConsole

VRC点阵屏幕

## 项目的启动流程

- 任意`Launcher`的主入口点
- 启动`top.lolosia.vrc.led.boot.Boot`工具，该工具仅为通用启动接口。
- 启动LaunchWrapper，位于`top.lolosia.vrc.led.boot.wrapper.Launch`
- 启动[SpongePowered Mixin](https://github.com/SpongePowered/Mixin)服务
- 由`top.lolosia.vrc.led.boot.LedClassLoader`在`plugins`目录下搜索所有可用的插件
- 向SpongePowered Mixin服务注册支持Mixin技术的插件配置信息
- 由LaunchWrapper启动`Launcher`所指定的程序原始入口点
- 启动SpringBoot Application
- 由SpringBoot将应用程序中的内置插件注册为Bean
- 由`top.lolosia.vrc.led.config.ClassPluginRegistrationConfig`将用户在SpringBoot启动前指定的插件类名注册为Bean。
- 由`top.lolosia.vrc.led.config.JarPluginRegistrationConfig`在`plugins`目录下搜索所有可用jar类型插件，并将入口点注册为Bean。
- 由`top.lolosia.vrc.led.config.PluginBeanPostProcessor`对所有SpringBoot通过注册信息创建的Led插件对象进行处理，并初始化插件的系统内部依赖。
- 由SpringBoot启动其他LedCore内置服务
- 此时程序切换到主界面。
- VRC点阵屏幕应用程序启动完成
