package edu.jarkvin.freegrammar.service;
import edu.jarkvin.freegrammar.exception.BadRequestException;
import edu.jarkvin.freegrammar.model.Grammar;
import edu.jarkvin.freegrammar.exception.TimeOutException;
import edu.jarkvin.freegrammar.model.Rule;

import org.springframework.stereotype.Service;

import java.util.*;

import static edu.jarkvin.freegrammar.util.IntUtils.getRandomValue;
import static edu.jarkvin.freegrammar.util.StringUtils.trimByChar;

@Service
public class GrammarServiceImpl implements GrammarService{
    private Long startTime;
    private static final long MAX_TIME_IN_MILLS = 50000;
    private static final String TIMEOUT_EXCEPTION = "La petición ha tardado demasiado tiempo.";

    //Genera n cadenas dependiendo de las reglas que se proporcionen.
    @Override
    public List<String> generateStrings(Integer quantity, Grammar grammar) {
        Set<String> generatedStrings = new HashSet<>();
        String concatString;

        //Obtener el tiempo actual en milisegundos.
        startTime = System.currentTimeMillis();

        //Se valida que todas las reglas tengan el formato correcto
        grammar.getRules().forEach(r -> isValid(r.getString()));
        //Corta las reglas que contengan OR (S --> aAb|aAc = S --> aAb, S --> aAc)
        grammar.setRules(trimRulesByOr(grammar.getRules()));

        //Itera hasta generar 'n' cadenas diferentes.
        while (generatedStrings.size() < quantity){
            concatString = getStringFromGrammar(grammar);
            generatedStrings.add(concatString);
        }
        //Se ordena la lista de cadenas
        return generatedStrings.stream().sorted(Comparator.reverseOrder()).toList();
    }

    //Se obtiene una cadena a partir de la gramática
    private String getStringFromGrammar(Grammar grammar) {
        String concatString = getInitialString(grammar.getInitVar(), grammar.getRules());

        //Verifica si existe un no terminal en la cadena.
        //Si existe lo reemplaza por una regla.
        while (existsNoTerminal(concatString, grammar.getRules())){
            concatString = getStringFromRules(concatString, findRulesByString(concatString, grammar.getRules()));
            //Si se alcanza el tiempo máximo por consulta lanza un timeout.
            if((System.currentTimeMillis() - startTime) >= MAX_TIME_IN_MILLS) throw new TimeOutException(TIMEOUT_EXCEPTION);
        }

        return replaceVoid(concatString);
    }

    //Devuelve una regla aleatoria asociada a una variable inicial.
    private String getInitialString(String variable, List<Rule> rules) {
        if(!existsInitVar(variable, rules)){
            throw new BadRequestException("La variable inicial no existe en las reglas.");
        }
        List<Rule> initialRules = getRules(variable, rules);
        Rule rule = initialRules.get(getRandomValue(initialRules.size()));

        return isValid(rule.getString());
    }

    //Corta las reglas que contengan OR (S --> aAb|aAc = S --> aAb, S --> aAc)
    private List<Rule> trimRulesByOr(List<Rule> rules) {
        List<Rule> rulesWithOr;
        Rule ruleWithOr;
        String left;
        String right;

        while (rules.stream().anyMatch(r -> r.getString().contains("|"))){
            //Se filtran todas las reglas que contengan OR.
            rulesWithOr = rules.stream().filter(r -> r.getString().contains("|")).toList();
            //Se encuentra la primera regla con OR.
            ruleWithOr = rulesWithOr.stream().findFirst().orElse(new Rule());

            //Se consiguen los componentes del OR
            left = ruleWithOr.getString().
                    substring(0, ruleWithOr.getString().indexOf("|"));
            right = ruleWithOr.getString()
                    .substring(ruleWithOr.getString().indexOf("|") + 1);

            //Se crea una nueva regla con el componente a la izquierda del OR.
            Rule trimmedLeftRule= new Rule(ruleWithOr.getVariable(), left);
            //Se crea una nueva regla con el componente a la derecha del OR.
            Rule trimmedRightRule = new Rule(ruleWithOr.getVariable(), right);

            //Se agregan las reglas cortadas y se elimina la original
            //Add(S --> aAb)
            //Add(S --> aAc)
            //Remove(S --> aAb|aAc)
            rules.add(trimmedLeftRule);
            rules.add(trimmedRightRule);
            rules.remove(ruleWithOr);
        }

        return rules;
    }

    //Verifica si la variable inicial existe en las reglas
    private boolean existsInitVar(String variable, List<Rule> rules) {
        return rules.stream().anyMatch(r -> variable.equals(r.getVariable()));
    }

    //Devuelve un regla asociada a una variable.
    private String getStringFromRules(String str, List<Rule> rules) {
        Rule rule = rules.get(getRandomValue(rules.size()));
        str = str.replaceFirst(rule.getVariable(), rule.getString());
        return isValid(str);
    }

    //Devuelve todas las reglas asociadas a una variable no terminal.
    private List<Rule> getRules(String variable, List<Rule> rules) {
        return rules.stream().filter(r -> variable.equals(r.getVariable())).toList();
    }
    //Devuelve todas las reglas asociadas a una variable no terminal
    // que se encuentra en una cadena.
    private List<Rule> findRulesByString(String str, List<Rule> rules) {
        return rules.stream().filter(r -> str.contains(r.getVariable())).toList();
    }

    //Verifica si existe una variable no terminal en una cadena.
    private boolean existsNoTerminal(String str, List<Rule> rules) {
        return !findRulesByString(str, rules).isEmpty();
    }

    //Elimina el elemento vacío 'ε' de la cadena.
    private String replaceVoid(String str) {
        return str.replace("ε","");
    }

    //Verificar si la cadena es válida.
    private String isValid(String str) {
        if(!(str.equals(trimByChar(trimByChar(str, "("),")")))){
            throw new BadRequestException("El formato de '"+str+"' no es soportado.");
        }
        if(!str.equals(trimByChar(str,"|"))){
            throw new BadRequestException("El formato de '"+str+"' no es soportado.");
        }
        if (str.equals("")){
            throw new BadRequestException("La regla no puede estar vacía.");
        }

        return str;
    }
}
