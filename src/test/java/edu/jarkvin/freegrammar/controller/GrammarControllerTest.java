package edu.jarkvin.freegrammar.controller;

import edu.jarkvin.freegrammar.model.Grammar;
import edu.jarkvin.freegrammar.model.Rule;
import edu.jarkvin.freegrammar.service.GrammarService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrammarControllerTest {

    @InjectMocks
    private GrammarController controller;
    @Mock
    private GrammarService service;


    @Test
    public void getStrings_Test(){
        Rule firstRule = new Rule();
        firstRule.setVariable("S");
        firstRule.setString("aAb");

        Rule secondRule = new Rule();
        secondRule.setVariable("A");
        secondRule.setString("b|c");

        Grammar grammar = new Grammar();
        grammar.setInitVar("S");
        grammar.addRule(firstRule);
        grammar.addRule(secondRule);

        List<String> words = new ArrayList<>();
        words.add("acb");
        words.add("abb");

        ResponseEntity<List<String>> response = new ResponseEntity<>(words, HttpStatus.CREATED);

        when(service.generateStrings(2,grammar)).thenReturn(words);
        Assert.assertEquals(response, controller.getStrings(2, grammar));
    }
}
