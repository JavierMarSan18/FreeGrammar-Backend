package edu.jarkvin.freegrammar.util;

import edu.jarkvin.freegrammar.model.Rule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListUtil <T> {
    public ListUtil(){
    }
    //Elimina items repetidos
    public List<T> removeRepeatedItems(List<T> items) {
        Set<T> set = new HashSet<>(items);
        return set.stream().collect(Collectors.toList());
    }
}
