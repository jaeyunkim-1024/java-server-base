package com.sample.base.datasource.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class UpperCasePhysicalNamingStrategy implements PhysicalNamingStrategy {
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return name != null ? convertToUpperCaseSnakeCase(name) : null;
    }

    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return name != null ? convertToUpperCaseSnakeCase(name) : null;
    }

    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return convertToUpperCaseSnakeCase(name);
    }

    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return name != null ? convertToUpperCaseSnakeCase(name) : null;
    }

    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return convertToUpperCaseSnakeCase(name);
    }

    /**
     * CamelCase -> SNAKE_CASE 변환 후 대문자로 변경
     */
    private Identifier convertToUpperCaseSnakeCase(Identifier identifier) {
        String newName = identifier.getText()
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2")
                .toUpperCase(Locale.getDefault()); // 대문자로 변환
        return Identifier.toIdentifier(newName);
    }
}
