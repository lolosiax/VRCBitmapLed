# LedConsole

VRC点阵屏幕

---
## 运行须知
- 要直接运行此项目，请先把 `LedPluginCApi.dll` 放在项目的根目录下，然后运行 `gradlew :compose:run`。  
- 如需开发插件，请 `implementation` 此项目的`api`插件。 
- 如需开发C++插件，请将`LedPluginCApi.lib`和`LedPluginCApi.dll`放入项目，之后将构建结果放置在……（此处TODO）路径下。

有界面程序主类位于插件`compose`的`top.lolosia.vrc.led.ui.Launcher`，无界面程序主类位于插件`core`的`top.lolosia.vrc.led.Launcher`

## 项目结构

**api**: 开发插件所需的api插件。  
**core**: VRC点阵屏幕的核心组件，为各个插件之间进行数据交换  
**compose**: VRC点阵屏幕的UI界面

## 项目的启动流程

- 任意`Launcher`的主入口点
- 启动`top.lolosia.vrc.led.boot.Boot`工具，该工具仅为通用启动接口。
- 启动LaunchWrapper，位于`top.lolosia.vrc.led.boot.wrapper.Launch`
- 启动[SpongePowered Mixin](https://github.com/SpongePowered/Mixin)服务
- 由`top.lolosia.vrc.led.boot.LedClassLoader`在`plugins`目录下搜索所有可用的插件
- 向SpongePowered Mixin服务注册支持Mixin技术的插件配置信息
- 由LaunchWrapper启动`Launcher`所指定的程序原始入口点
- 若使用Kotlin Compose，则此处为Compose初始化流程，位于`top.lolosia.vrc.led.ui.LedCompose`
- 启动SpringBoot Application
- 由SpringBoot将LedCore中的内置插件注册为Bean
- 由`top.lolosia.vrc.led.config.ClassPluginRegistrationConfig`将用户在SpringBoot启动前指定的插件类名注册为Bean。
- 由`top.lolosia.vrc.led.config.JarPluginRegistrationConfig`在`plugins`目录下搜索所有可用jar类型插件，并将入口点注册为Bean。
- 由`top.lolosia.vrc.led.config.NativePluginRegistrationConfig`在`plugins`目录下搜索所有可用dll类型插件，并使用`top.lolosia.vrc.led.plugin.SimpleNativePlugin`包装为Bean。
- 由`top.lolosia.vrc.led.config.PluginBeanPostProcessor`对所有SpringBoot通过注册信息创建的Led插件对象进行处理，并初始化插件的系统内部依赖。
- 由SpringBoot启动其他LedCore内置服务
- 若使用Kotlin Compose，则此时程序切换到主界面。
- LedConsole启动完成

## 项目发布

需要配置 `gradle.config.json`

```json
{
  "publishing": {
    "username": "...",
    "password": "..."
  }
}
```