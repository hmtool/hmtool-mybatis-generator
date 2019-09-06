package tech.mhuang.mybatis.generate.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import java.util.Set;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

/**
 *
 * mybatis注释生成器
 *
 * @author mhuang
 * @since 1.0.0
 */
public class MybatisCommentGenerator implements CommentGenerator {
    private Properties properties = new Properties();
    private Properties systemPro = System.getProperties();
    private boolean suppressDate = false;
    private boolean suppressAllComments = false;
    private boolean javaFileEncoding = false;
    private String currentDateStr = LocalDate.now().toString() + LocalTime.now().toString();

    public MybatisCommentGenerator() {
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {

    }

    @Override
    public void addComment(XmlElement xmlElement) {
    }

    @Override
    public void addRootComment(XmlElement rootElement) {
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.javaFileEncoding = StringUtility.isTrue(properties.getProperty("javaFileEncoding"));
        this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
        this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
    }

    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append("@mbg.generated");
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }

        String s = this.getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }

        javaElement.addJavaDocLine(sb.toString());
    }

    protected String getDateString() {
        String result = null;
        if (!this.suppressDate) {
            result = this.currentDateStr;
        }

        return result;
    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerClass.addJavaDocLine("/**");
            sb.append(" * ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            sb.append(introspectedTable.getRemarks());
            sb.append(" ");
            sb.append(this.getDateString());
            sb.append(" * @author ");
            sb.append(this.systemPro.getProperty("user.name"));
            innerClass.addJavaDocLine(sb.toString().replace("\n", " "));
            innerClass.addJavaDocLine(" */");
        }
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerEnum.addJavaDocLine("/**");
            sb.append(" * ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            sb.append(introspectedTable.getRemarks());
            innerEnum.addJavaDocLine(sb.toString().replace("\n", " "));
            innerEnum.addJavaDocLine(" */");
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            field.addJavaDocLine("/**");
            sb.append(" * ");
            sb.append(introspectedColumn.getRemarks());
            field.addJavaDocLine(sb.toString().replace("\n", " "));
            field.addJavaDocLine(" */");
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            field.addJavaDocLine("/**");
            sb.append(" * ");
            sb.append(introspectedTable.getRemarks());
            field.addJavaDocLine(sb.toString().replace("\n", " "));
            field.addJavaDocLine(" */");
        }
    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            this.addJavadocTag(method, false);
            method.addJavaDocLine(" */");
        }
    }

    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            StringBuilder sb = new StringBuilder();
            sb.append(" * ");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString().replace("\n", " "));
            sb.setLength(0);
            sb.append(" * @return ");
            sb.append(introspectedColumn.getActualColumnName());
            sb.append(" ");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString().replace("\n", " "));
            method.addJavaDocLine(" */");
        }
    }

    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            StringBuilder sb = new StringBuilder();
            sb.append(" * ");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString().replace("\n", " "));
            Parameter parm = (Parameter)method.getParameters().get(0);
            sb.setLength(0);
            sb.append(" * @param ");
            sb.append(parm.getName());
            sb.append(" ");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString().replace("\n", " "));
            method.addJavaDocLine(" */");
        }
    }

    @Override
    public void addModelClassComment(TopLevelClass innerClass, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerClass.addJavaDocLine("/**");
            sb.append(" * ");
            sb.append(introspectedTable.getRemarks());
            innerClass.addJavaDocLine(sb.toString());
            innerClass.addJavaDocLine("* @Description:TODO(这里用一句话描述这个类的作用)   ");
            innerClass.addJavaDocLine("* @author:" + this.systemPro.getProperty("user.name"));
            innerClass.addJavaDocLine("* @date:   " + this.currentDateStr);
            innerClass.addJavaDocLine(" */");
        }
    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean arg2) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerClass.addJavaDocLine("/**");
            sb.append(" * ");
            sb.append(introspectedTable.getRemarks());
            sb.append(" * @author ");
            sb.append(this.systemPro.getProperty("user.name"));
            sb.append(" ");
            sb.append(this.currentDateStr);
            innerClass.addJavaDocLine(sb.toString().replace("\n", " "));
            sb.setLength(0);
            innerClass.addJavaDocLine(" */");
        }
    }

    @Override
    public void addClassAnnotation(InnerClass arg0, IntrospectedTable arg1, Set<FullyQualifiedJavaType> arg2) {
    }

    @Override
    public void addFieldAnnotation(Field arg0, IntrospectedTable arg1, Set<FullyQualifiedJavaType> arg2) {
    }

    @Override
    public void addFieldAnnotation(Field arg0, IntrospectedTable arg1, IntrospectedColumn arg2, Set<FullyQualifiedJavaType> arg3) {
    }

    @Override
    public void addGeneralMethodAnnotation(Method arg0, IntrospectedTable arg1, Set<FullyQualifiedJavaType> arg2) {
    }

    @Override
    public void addGeneralMethodAnnotation(Method arg0, IntrospectedTable arg1, IntrospectedColumn arg2, Set<FullyQualifiedJavaType> arg3) {
    }
}
