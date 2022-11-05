package edu.jarkvin.freegrammar.service;

import edu.jarkvin.freegrammar.model.Grammar;
import edu.jarkvin.freegrammar.model.Rule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GrammarServiceImplTest {

    private final GrammarService service = new GrammarServiceImpl();
    private Grammar grammar;
    private Rule firstRule;
    private Rule secondRule;
    private Rule thirdRule;
    private List<String> words;

    @Before
    public void before(){
        grammar = new Grammar();
        firstRule = new Rule();
        secondRule = new Rule();
        thirdRule = new Rule();
        words = new ArrayList<>();
    }

    @Test
    public void generateStrings_Test(){
        firstRule.setVariable("S");
        firstRule.setString("aAb");

        secondRule.setVariable("A");
        secondRule.setString("b");

        thirdRule.setVariable("A");
        thirdRule.setString("c");

        grammar.setInitVar("S");
        grammar.addRule(firstRule);
        grammar.addRule(secondRule);
        grammar.addRule(thirdRule);

        words.add("acb");
        words.add("abb");

        Assert.assertEquals(words , service.generateStrings(2 , grammar));
    }

    @Test
    public void generateStrings_WhenExistsOr_Test(){
        firstRule.setVariable("S");
        firstRule.setString("aAb");

        secondRule.setVariable("A");
        secondRule.setString("b|c");

        grammar.setInitVar("S");
        grammar.addRule(firstRule);
        grammar.addRule(secondRule);

        words.add("acb");
        words.add("abb");

        Assert.assertEquals(words , service.generateStrings(2 , grammar));
    }
}
