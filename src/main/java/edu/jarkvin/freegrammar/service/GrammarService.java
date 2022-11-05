package edu.jarkvin.freegrammar.service;

import edu.jarkvin.freegrammar.model.Grammar;

import java.util.List;

public interface GrammarService {
    List<String> generateStrings(Integer quantity, Grammar grammar);
}
