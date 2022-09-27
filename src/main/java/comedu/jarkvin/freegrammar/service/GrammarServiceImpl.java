package comedu.jarkvin.freegrammar.service;

import comedu.jarkvin.freegrammar.model.Grammar;
import comedu.jarkvin.freegrammar.model.Rule;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrammarServiceImpl implements GrammarService{
    private static final Random random = new Random();
    @Override
    public List<String> generateStrings(Integer quantity, Grammar grammar) {
        Set<String> generatedStrings = new HashSet<>();
        String concatString;

        do {
            concatString = getInitialString(grammar.getInitVar(), grammar.getRules());

            while (existsNoTerminal(concatString, grammar.getRules())){
                concatString = getString(concatString, findRulesByString(concatString, grammar.getRules()));
            }

            concatString = replaceVoid(concatString);
            generatedStrings.add(concatString);
        }while (generatedStrings.size() < quantity);

        return toListAndSort(generatedStrings);
    }

    private List<String> toListAndSort(Set<String> set) {
        List<String> list = new ArrayList<>(set.stream().toList());
        list.sort(Collections.reverseOrder());
        return list;
    }

    private String getInitialString(String variable, List<Rule> rules) {
        List<Rule> initialRules = getRules(variable, rules);
        Rule rule = initialRules.get(random.nextInt(initialRules.size()));
        return rule.getString();
    }

    private String getString(String str, List<Rule> rules) {
        Rule rule = rules.get(random.nextInt(rules.size()));
        str = str.replace(rule.getVariable(), rule.getString());
        return str;
    }

    private List<Rule> getRules(String variable, List<Rule> rules) {
        return rules.stream().filter(r -> variable.equals(r.getVariable())).toList();
    }

    private List<Rule> findRulesByString(String str, List<Rule> rules) {
        return rules.stream().filter(r -> str.contains(r.getVariable())).toList();
    }

    private boolean existsNoTerminal(String str, List<Rule> rules) {
        return !findRulesByString(str, rules).isEmpty();
    }

    private String replaceVoid(String str) {
        return str.replace("ε","");
    }
}
