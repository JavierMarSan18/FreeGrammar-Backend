package comedu.jarkvin.freegrammar.service;
import comedu.jarkvin.freegrammar.exception.BadRequestException;
import comedu.jarkvin.freegrammar.exception.InvalidBoundException;
import comedu.jarkvin.freegrammar.model.Grammar;
import comedu.jarkvin.freegrammar.exception.TimeOutException;
import comedu.jarkvin.freegrammar.model.Rule;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

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

        //Se valida que todas las reglas tengan el formato correcto
        grammar.getRules().forEach(r -> isValid(r.getString()));
        //Corta las reglas que contengan OR (S --> aAb|aAc = S --> aAb, S --> aAc)
        grammar.setRules(trimRulesByOr(grammar.getRules()));

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

        Logger logger = Logger.getLogger("rule");
        rules.forEach(r -> logger.info(r.toString()));

        return rules;
    }

    //Verifica si la variable inicial existe en las reglas
    private boolean existsInitVar(String variable, List<Rule> rules) {
        return rules.stream().anyMatch(r -> variable.equals(r.getVariable()));
    }

    //Devuelve un regla asociada a una variable.
    private String getString(String str, List<Rule> rules) {
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

    //Devuelve un entero aleatorio de un rango
    private int getRandomValue(int bound) {
        if(bound < 1){
            throw new InvalidBoundException("El rango debe ser positivo");
        }
        return random.nextInt(bound);
    }

    //Elimina el primero y último caracter indicado.
    private String trimByChar(String str, String s) {
        int minLength = 2;

        //Valida si la longitud de la cadena es mayor a 2 y si contiene a s.
        if(str.length() > minLength && str.contains(s)){
            //Valida si la cadena comienza con s y recorta
            if(str.substring(0, 1).equals(s)){
                str = str.substring(1);
            }
//            //Valida si la cadena termina con s y recorta
            if(str.substring(str.length()-1).equals(s)){
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }
    //Verificar si la cadena es válida.
    private String isValid(String str) {
        if(!str.equals(trimByChar(str,"|"))){
            throw new BadRequestException("El formato de '"+str+"' no es soportado.");
        }
        if (str.equals("")){
            throw new BadRequestException("La regla no puede estar vacía.");
        }
        return str;
    }
}
