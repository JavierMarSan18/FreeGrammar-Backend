package comedu.jarkvin.freegrammar;

import comedu.jarkvin.freegrammar.model.Rule;
import org.apache.tomcat.util.digester.Rules;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Rule rule = new Rule();
        rule.setVariable("S");
        rule.setString("(aAb|aAc|aBa)");

        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        rules.forEach(System.out::println);


        List<Rule> trimmedRules = trimRulesByOr(rules);

        trimmedRules.forEach(System.out::println);
    }

    private static List<Rule> trimRulesByOr(List<Rule> rules) {
        String left;
        String right;

        List<Rule> rulesWithOr;

        Rule ruleWithOr;

        while (rules.stream().anyMatch(r -> r.getString().contains("|"))){
            //Se filtran todas las reglas que contengan OR.
            rulesWithOr = rules.stream().filter(r -> r.getString().contains("|")).toList();
            //Se encuentra la primera regla con OR.
            ruleWithOr = rulesWithOr.stream().findFirst().orElse(null);

            //Se consiguen los componentes del OR
            left = ruleWithOr.getString().substring(0, ruleWithOr.getString().indexOf("|"));
            right = ruleWithOr.getString().substring(ruleWithOr.getString().indexOf("|") + 1);

            //Se crea una nueva regla con el componente a la izquierda del OR.
            Rule trimmedRuleLeft= new Rule();
            trimmedRuleLeft.setVariable(ruleWithOr.getVariable());
            trimmedRuleLeft.setString(left);

            //Se crea una nueva regla con el componente a la derecha del OR.
            Rule trimmedRuleRight = new Rule();
            trimmedRuleRight.setVariable(ruleWithOr.getVariable());
            trimmedRuleRight.setString(right);

            rules.add(trimmedRuleLeft);
            rules.add(trimmedRuleRight);
            rules.remove(ruleWithOr);
        }
        return rules;
    }
}
