package comedu.jarkvin.freegrammar.service;

import comedu.jarkvin.freegrammar.exception.BadRequestException;
import comedu.jarkvin.freegrammar.exception.InvalidBoundException;
import comedu.jarkvin.freegrammar.model.Grammar;
import comedu.jarkvin.freegrammar.exception.TimeOutException;
import comedu.jarkvin.freegrammar.model.Rule;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrammarServiceImpl implements GrammarService{
    private static final long MAX_TIME_IN_MILLS = 50000;
    private static final String TIMEOUT_EXCEPTION = "La petición ha tardado demasiado tiempo.";
    private static final Random random = new Random();

    //Genera n cadenas dependiendo de las reglas que se proporcionen.
    @Override
    public List<String> generateStrings(Integer quantity, Grammar grammar) {
        Set<String> generatedStrings = new HashSet<>();
        String concatString;

        //Obtener el tiempo actual
        long startTime = System.currentTimeMillis();

        //Itera hasta generar 'n' cadenas diferentes.
        while (generatedStrings.size() < quantity){
            concatString = getInitialString(grammar.getInitVar(), grammar.getRules());

            //Verifica si existe un no terminal en la cadena.
            //Si existe lo reemplaza por una regla.
            while (existsNoTerminal(concatString, grammar.getRules())){
                concatString = getString(concatString, findRulesByString(concatString, grammar.getRules()));

                //Si se alcanza el tiempo máximo por consulta lanza un timeout.
                if((System.currentTimeMillis() - startTime) >= MAX_TIME_IN_MILLS) throw new TimeOutException(TIMEOUT_EXCEPTION);
            }
            concatString = replaceVoid(concatString);
            generatedStrings.add(concatString);
        }


        return toListAndSort(generatedStrings);
    }

    //Convierte el set en una lista y la ordena.
    private List<String> toListAndSort(Set<String> set) {
        List<String> list = new ArrayList<>(set.stream().toList());
        list.sort(Collections.reverseOrder());
        return list;
    }

    //Devuelve una regla aleatoria asociada a una variable inicial.
    private String getInitialString(String variable, List<Rule> rules) {
        if(!existsInitVar(variable, rules)){
            throw new BadRequestException("La variable inicial no existe en las reglas.");
        }
        List<Rule> initialRules = getRules(variable, rules);
        Rule rule = initialRules.get(getRandomValue(initialRules.size()));
        return rule.getString();
    }

    //Verifica si la variable inicial existe en las reglas
    private boolean existsInitVar(String variable, List<Rule> rules) {
        return rules.stream().anyMatch(r -> variable.equals(r.getVariable()));
    }

    //Devuelve un regla asociada a una variable.
    private String getString(String str, List<Rule> rules) {
        Rule rule = rules.get(getRandomValue(rules.size()));
        str = str.replaceFirst(rule.getVariable(), rule.getString());
        return str;
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

    //Devuelve un entero aleatorio de un rango
    private int getRandomValue(int bound) {
        if(bound < 1){
            throw new InvalidBoundException("El rango debe ser positivo");
        }
        return random.nextInt(bound);
    }
}
