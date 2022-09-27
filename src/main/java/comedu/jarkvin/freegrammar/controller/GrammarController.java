package comedu.jarkvin.freegrammar.controller;

import comedu.jarkvin.freegrammar.model.Grammar;
import comedu.jarkvin.freegrammar.service.GrammarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/grammar")
public class GrammarController {

    @Autowired
    private GrammarService service;

    @PostMapping("/strings/{n}")
    public ResponseEntity<List<String>> getStrings(@PathVariable Integer n , @RequestBody Grammar grammar){
        return new ResponseEntity<>(service.generateStrings(n, grammar), HttpStatus.CREATED);
    }
}
