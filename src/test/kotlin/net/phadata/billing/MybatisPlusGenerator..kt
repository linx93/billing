package net.phadata.billing

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.config.DataSourceConfig
import com.baomidou.mybatisplus.generator.config.GlobalConfig
import com.baomidou.mybatisplus.generator.config.PackageConfig
import com.baomidou.mybatisplus.generator.config.StrategyConfig
import com.baomidou.mybatisplus.generator.config.rules.DateType
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import org.junit.jupiter.api.Test


/**
 * mybatis-plus代码生成器
 *
 * @author linx
 * @since 2022-06-13 17:38
 */
class MybatisPlusGenerator {

    @Test
    fun run() {

        // 1、创建代码生成器
        val mpg = AutoGenerator()

        // 2、全局配置
        val gc = GlobalConfig()
        val projectPath = System.getProperty("user.dir")
        gc.isKotlin = true //关键点使用kotlin
        gc.outputDir = "$projectPath/src/main/kotlin"
        gc.author = "linx"
        gc.isOpen = false //生成后是否打开资源管理器
        gc.isFileOverride = false //重新生成时文件是否覆盖

        //UserServie
        gc.serviceName = "%sService" //去掉Service接口的首字母I
        gc.idType = IdType.AUTO //主键策略
        gc.dateType = DateType.ONLY_DATE //定义生成的实体类中日期类型
        gc.isSwagger2 = true //开启Swagger2模式
        mpg.globalConfig = gc

        // 3、数据源配置
        val dsc = DataSourceConfig()
        dsc.url = "jdbc:mysql://192.168.1.100:3306/billing_management_system?serverTimezone=GMT%2B8"
        dsc.driverName = "com.mysql.cj.jdbc.Driver"
        dsc.username = "root"
        dsc.password = "123456"
        dsc.dbType = DbType.MYSQL
        mpg.dataSource = dsc

        // 4、包配置
        val pc = PackageConfig()
        //pc.setModuleName("src"); //模块名
        //包  net.phadata.billing
        pc.parent = "net.phadata.billing"
        pc.controller = "controller"
        pc.entity = "model.po"
        pc.service = "service"
        pc.mapper = "mapper"
        mpg.packageInfo = pc

        // 5、策略配置
        val strategy = StrategyConfig()
        val tables = arrayOf("order_records","sys_user")
        strategy.setInclude(*tables)
        strategy.naming = NamingStrategy.underline_to_camel //数据库表映射到实体的命名策略
        //strategy.setTablePrefix(pc.moduleName + "_") //生成实体时去掉表前缀
        strategy.columnNaming = NamingStrategy.underline_to_camel //数据库表字段映射到实体的命名策略
        strategy.isEntityLombokModel = false // lombok 模型 @Accessors(chain = true) setter链式操作
        strategy.isRestControllerStyle = true //restful api风格控制器
        strategy.isControllerMappingHyphenStyle = true //url中驼峰转连字符
        mpg.strategy = strategy


        // 6、执行
        //mpg.execute();
    }

}