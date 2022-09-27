package comedu.jarkvin.freegrammar.service;

import comedu.jarkvin.freegrammar.model.Grammar;

import java.util.List;

public interface GrammarService {
    List<String> generateStrings(Integer quantity, Grammar grammar);
}
