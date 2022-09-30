package edu.jarkvin.freegrammar.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Grammar {
    @NotBlank(message = "La variable inicial no puede estar vacía.")
    private String initVar;
    @NotEmpty(message = "Las reglas no puede estar vacías.")
    private List<Rule> rules;

    public Grammar() {
        rules = new ArrayList<>();
    }

    public Grammar(String initVar, List<Rule> rules) {
        this.initVar = initVar;
        this.rules = rules;
    }


    public String getInitVar() {
        return initVar;
    }

    public void setInitVar(String initVar) {
        this.initVar = initVar;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule){
        this.rules.add(rule);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grammar grammar = (Grammar) o;
        return Objects.equals(initVar, grammar.initVar) && Objects.equals(rules, grammar.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initVar, rules);
    }
}
