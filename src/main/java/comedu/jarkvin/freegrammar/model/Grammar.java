package comedu.jarkvin.freegrammar.model;

import java.util.List;
import java.util.Objects;

public class Grammar {
    private String initVar;
    private List<Rule> rules;

    public Grammar() {
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
