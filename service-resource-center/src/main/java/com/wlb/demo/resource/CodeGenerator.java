package com.wlb.demo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;
import java.util.Objects;

/**
 * MybatisPlus代码生成工具类
 *
 * @author Eric Wang
 */
public class CodeGenerator {
    private static final String author = "Eric Wang";
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/ms-sample?characterEncoding=utf8&useUnicode=true&useSSL=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private static final String username = "root";
    private static final CharSequence pw = "zjyd@db20201218";

    private static final String[] include = new String[]{"test_user"};

    private static final String[] exclude = new String[]{};

    public static void main(String[] args) {
        // 输出目录
        String outPutDir = getOutPutDir();
        String mapperXmlDir = outPutDir + "/../resources/mapper";
        String parent = CodeGenerator.class.getPackage().getName() + ".app";
        // 数据源配置
        DataSourceConfig.Builder builder = new DataSourceConfig.Builder(jdbcUrl, username, pw.toString());
        AutoGenerator mpg = new AutoGenerator(builder.build());

        // 全局配置
        GlobalConfig.Builder gcBuilder = new GlobalConfig.Builder();
        gcBuilder.disableOpenDir()
                .outputDir(outPutDir)
                // 是否覆盖原文件
                .fileOverride()
                // .enableSwagger()
                .author(author)
        ;
        mpg.global(gcBuilder.build());

        PackageConfig.Builder pcBuilder = new PackageConfig.Builder();
        pcBuilder.parent(parent)
                .controller("controller")
                .entity("model.entity")
                .service("manager")
                .serviceImpl("manager.impl")
                .mapper("dao.mapper")
                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperXmlDir));
        mpg.packageInfo(pcBuilder.build());

        InjectionConfig injectionConfig = new InjectionConfig();
        mpg.injection(injectionConfig);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig.Builder tcBuilder = new TemplateConfig.Builder();
        tcBuilder.disable(TemplateType.CONTROLLER);
        mpg.template(tcBuilder.build());

        // 策略配置
        StrategyConfig.Builder scBuilder = new StrategyConfig.Builder();
        scBuilder
                .addInclude(include)
                .addExclude(exclude)
        ;
        scBuilder.serviceBuilder()
                .formatServiceFileName("%sManager")
                .formatServiceImplFileName("%sManagerImpl");
        scBuilder.entityBuilder()
                .enableLombok()
                .enableChainModel()
                .naming(NamingStrategy.underline_to_camel)
                .idType(IdType.ASSIGN_ID)
        ;
        scBuilder.mapperBuilder()
                .enableBaseColumnList()
                .enableBaseResultMap()
                .enableMapperAnnotation();
        mpg.strategy(scBuilder.build());

        // 执行生成
        mpg.execute();
    }

    private static String getOutPutDir() {
        String location = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String prefix = "/";
        if (location.startsWith(prefix)) {
            location = location.substring(1);
        }
        return location.substring(0, location.indexOf("target")) + "src/main/java";
    }

}
