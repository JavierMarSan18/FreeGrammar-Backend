package edu.jarkvin.freegrammar.util;

import java.util.*;
public class CollectionUtils <T>{
    public CollectionUtils(){

    }

    //Convierte un Collection a un List<T> y la ordena.
    public List<T> toListAndSort(Collection<T> c) {
        List<T> list = toList(c);
        list.sort(Collections.reverseOrder());
        return list;
    }

    //Convierte un Collection a un List<T>
    public List<T> toList(Collection<T> c) {
        return new ArrayList<>(c.stream().toList());
    }
}
