package edu.jarkvin.freegrammar.controller;

import edu.jarkvin.freegrammar.model.Grammar;
import edu.jarkvin.freegrammar.service.GrammarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value = "/api/grammar")
public class GrammarController {

    @Autowired
    private GrammarService service;

    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<List<String>> getStrings(@Valid @RequestBody Grammar grammar){
        return new ResponseEntity<>(service.generateStrings(grammar), HttpStatus.CREATED);
    }
}
