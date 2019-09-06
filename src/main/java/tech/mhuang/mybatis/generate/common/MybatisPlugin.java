package tech.mhuang.mybatis.generate.common;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 *
 * mybatis 插件扩展
 *
 * @author mhuang
 * @since 1.0.0
 */
public class MybatisPlugin extends PluginAdapter {

    private final Collection<MybatisPlugin.Annotations> annotations;
    private Map<String, String> insertReplaceMap = new HashMap<>();
    private Map<String, String> updateReplaceMap = new HashMap<>();
    private Map<String, String> updateAllReplaceMap = new HashMap<>();
    private Map<String, String> deleteReplaceMap = new HashMap<>();
    private Map<String, String> getByIdReplaceMap = new HashMap<>();

    private static final String CONTROLLER = "controller";
    private static final String SERVICE = "service";

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        String mapperPackage = this.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
        String entityTargetPackage = this.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();
        String idType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().getShortName();
        Map<String, String> root = new HashMap<>();
        root.put("description", introspectedTable.getRemarks());
        root.put("idType", idType);
        String servicePackage = "";
        String controllerPackage = "";
        if (entityTargetPackage.lastIndexOf(".") > -1) {
            servicePackage = entityTargetPackage.substring(0, entityTargetPackage.lastIndexOf(".")) + SERVICE;
            controllerPackage = entityTargetPackage.substring(0,entityTargetPackage.lastIndexOf(".")) + CONTROLLER;
        } else {
            servicePackage = SERVICE;
            controllerPackage = CONTROLLER;
        }

        root.put("servicePackage", servicePackage);
        root.put("mapperPackage",mapperPackage);
        root.put("controllerPackage", controllerPackage);
        root.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        root.put("author", System.getProperties().getProperty("user.name"));
        root.put("entityClassName", introspectedTable.getTableConfiguration().getDomainObjectName());
        root.put("entityClass", entityTargetPackage);
        String targetProject = this.getContext().getJavaModelGeneratorConfiguration().getTargetProject();
        genInterService(targetProject, root);
        genServiceImpl(targetProject, root);
        genController(targetProject, root);
        return null;
    }
    private void genFreemarker(String dirPath, String generatorFile, String ftlPath, Map<String,String> root){
        File dir = new File(dirPath);
        File file = new File(generatorFile);
        if (file.exists()) {
            file.delete();
        } else {
            try {
                dir.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28));


        try {
            Template temp = cfg.getTemplate(ftlPath);
            Writer out = new OutputStreamWriter(new FileOutputStream(file));
            temp.process(root, out);
            out.flush();
        } catch (TemplateNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedTemplateNameException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    private void genController(String targetProject,Map<String,String> root){
        String dirPath = String.format("%s/%s", targetProject, root.get("controllerPackage").replaceAll("\\.", "/"));
        String filePath = targetProject + "/" + root.get("controllerPackage").replaceAll("\\.", "/") + "/" + root.get("entityClassName")
                + "Controller.java";
        String ftl = "GeneratorController.ftl";
        genFreemarker(dirPath,filePath,ftl,root);
    }
    private void genServiceImpl(String targetProject,Map<String,String> root){
        String dirPath = String.format("%s/%s/impl", targetProject, root.get("servicePackage").replaceAll("\\.", "/"));
        String filePath = targetProject + "/" + root.get("servicePackage").replaceAll("\\.", "/") + "/impl/" + root.get("entityClassName")
                + "ServiceImpl.java";
       String ftl = "GeneratorServiceImpl.ftl";
       genFreemarker(dirPath,filePath,ftl,root);
    }
    @SuppressWarnings("deprecation")
    private void genInterService(String targetProject, Map<String, String> root) {
        String dirPath = String.format("%s/%s", targetProject, root.get("servicePackage").replaceAll("\\.", "/"));
        String filePath = targetProject + "/" + root.get("servicePackage").replaceAll("\\.", "/") + "/I" + root.get("entityClassName")
                + "Service.java";
        String ftl = "GeneratorService.ftl";
        genFreemarker(dirPath,filePath,ftl,root);
    }

    public MybatisPlugin() {
        this.insertReplaceMap.put("id", "insert");
        this.updateReplaceMap.put("id", "update");
        this.updateAllReplaceMap.put("id", "updateAll");
        this.deleteReplaceMap.put("id", "delete");
        this.getByIdReplaceMap.put("id", "getById");
        this.annotations = new LinkedHashSet<>(MybatisPlugin.Annotations.values().length);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addSerialVersionUID(topLevelClass, introspectedTable);
        this.addDataAnnotation(topLevelClass);
        topLevelClass.getMethods().clear();
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
        return true;
    }

    private void addSerialVersionUID(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        CommentGenerator commentGenerator = this.context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(new FullyQualifiedJavaType("long"));
        field.setStatic(true);
        field.setFinal(true);
        field.setName("serialVersionUID");
        field.setInitializationString("1L");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XMLElementUtils.replaceAttribute(element, this.updateAllReplaceMap);
        return super.sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XMLElementUtils.replaceAttribute(element, this.updateAllReplaceMap);
        return super.sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XMLElementUtils.replaceAttribute(element, this.getByIdReplaceMap);
        return super.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XMLElementUtils.replaceAttribute(element, this.insertReplaceMap);
        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XMLElementUtils.replaceAttribute(element, this.updateReplaceMap);
        return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
                                                                     IntrospectedTable introspectedTable) {
        XMLElementUtils.replaceAttribute(element, this.updateAllReplaceMap);
        return super.sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XMLElementUtils.replaceAttribute(element, this.deleteReplaceMap);
        return super.sqlMapDeleteByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return super.sqlMapResultMapWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return super.sqlMapResultMapWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            java.lang.reflect.Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    private void addDataAnnotation(TopLevelClass topLevelClass) {
        Iterator var2 = this.annotations.iterator();

        while (var2.hasNext()) {
            MybatisPlugin.Annotations annotation = (MybatisPlugin.Annotations) var2.next();
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.name);
        }

    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.annotations.add(MybatisPlugin.Annotations.DATA);
        this.annotations.add(MybatisPlugin.Annotations.EQUALS_AND_HASHCODE);
        Iterator var2 = properties.entrySet().iterator();

        while (var2.hasNext()) {
            Entry<Object, Object> entry = (Entry) var2.next();
            boolean isEnable = Boolean.parseBoolean(entry.getValue().toString());
            if (isEnable) {
                String paramName = entry.getKey().toString().trim();
                MybatisPlugin.Annotations annotation = MybatisPlugin.Annotations.getValueOf(paramName);
                if (annotation != null) {
                    this.annotations.add(annotation);
                    this.annotations.addAll(MybatisPlugin.Annotations.getDependencies(annotation));
                }
            }
        }

    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addAnnotation("@Mapper");
        interfaze.addSuperInterface(new FullyQualifiedJavaType("BaseMapper<" + introspectedTable.getBaseRecordType() + ",String>"));
        interfaze.addImportedType(new FullyQualifiedJavaType("com.petecat.interchan.core.mapper.BaseMapper"));
        interfaze.getMethods().clear();
        return true;
    }

    private  enum Annotations {
        DATA("data", "@Data", "lombok.Data"),
        EQUALS_AND_HASHCODE("equalsAndHashCode", "@EqualsAndHashCode(callSuper=false)", "lombok.EqualsAndHashCode"),
        BUILDER("builder", "@Builder", "lombok.Builder"),
        ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
        NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
        TO_STRING("toString", "@ToString", "lombok.ToString");

        private final String paramName;
        private final String name;
        private final FullyQualifiedJavaType javaType;

        private Annotations(String paramName, String name, String className) {
            this.paramName = paramName;
            this.name = name;
            this.javaType = new FullyQualifiedJavaType(className);
        }

        private static MybatisPlugin.Annotations getValueOf(String paramName) {
            MybatisPlugin.Annotations[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                MybatisPlugin.Annotations annotation = var1[var3];
                if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                    return annotation;
                }
            }

            return null;
        }

        @SuppressWarnings("unchecked")
        private static Collection<MybatisPlugin.Annotations> getDependencies(MybatisPlugin.Annotations annotation) {
            return (Collection<MybatisPlugin.Annotations>) (annotation == ALL_ARGS_CONSTRUCTOR ? Collections.singleton(NO_ARGS_CONSTRUCTOR) : Collections.emptyList());
        }
    }
}
