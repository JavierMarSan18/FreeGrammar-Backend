package edu.jarkvin.freegrammar.model;

import java.util.Objects;

public class Rule {
    private String variable;
    private String string;

    public Rule() {
    }

    public Rule(String variable, String string) {
        this.variable = variable;
        this.string = string;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(variable, rule.variable) && Objects.equals(string, rule.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, string);
    }

    @Override
    public String toString() {
        return variable + " --> " + string;
    }
}
