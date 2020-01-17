/**
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.MergeConstants;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *li_hq
 */
public class SwaggerPlugin extends PluginAdapter {
    /**
     * This method is called after all the setXXX methods are called, but before
     * any other method is called. This allows the plugin to determine whether
     * it can run or not. For example, if the plugin requires certain properties
     * to be set, and the properties are not set, then the plugin is invalid and
     * will not run.
     *
     * @param warnings add strings to this list to specify warnings. For example, if
     *                 the plugin is invalid, you should specify why. Warnings are
     *                 reported to users after the completion of the run.
     * @return true if the plugin is in a valid state. Invalid plugins will not
     * be called
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        String value = StringUtils.isEmpty(introspectedTable.getRemarks())
                ? introspectedTable.getFullyQualifiedTable().toString()
                : introspectedTable.getRemarks();
        topLevelClass.addAnnotation("@ApiModel(value = \"" + value + "\")");

        StringBuilder classDoc = new StringBuilder();
        topLevelClass.addJavaDocLine("/**");
        classDoc.append(" * ");
        classDoc.append(introspectedTable.getFullyQualifiedTable());
        classDoc.setLength(0);
        classDoc.append(" * @author ");
        classDoc.append("li_hq");
        classDoc.append(" ");
        classDoc.append(getDateString());
        topLevelClass.addJavaDocLine(classDoc.toString());
        topLevelClass.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" * 字段描述:");
        sb2.append(introspectedColumn.getRemarks());
        field.addJavaDocLine(sb2.toString());
        sb.append(" * 字段:");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        field.addJavaDocLine(sb.toString());
        addJavadocTag(field, false);
        field.addJavaDocLine("*/");
        field.addJavaDocLine("@ApiModelProperty(value =\"" + introspectedColumn.getRemarks() + "\")");
        return true;
    }

    protected void addJavadocTag(JavaElement javaElement,
                                 boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *"); 
        StringBuilder sb = new StringBuilder();
        sb.append(" * "); 
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge"); 
        }
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());


    }

    private String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }
}
