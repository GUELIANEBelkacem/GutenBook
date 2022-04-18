package com.gutenbook.gutenbook.Controllers;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutenbook.gutenbook.models.GutenProvider;


@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class GutenRouter {
		
	  	@GetMapping("/simplesearch")
	    public String search(@RequestParam(value = "word") String word) throws Exception {

	        return GutenProvider.simpleSearch(word).toString();
	    }

	    @GetMapping("/multiplesearch")
	    public String multiplesearch(@RequestParam(value = "words") String words) throws Exception {

	        String[] p = Pattern.compile(" ").split(words);
	        List<String> wordList = Arrays.asList(p);

	        return GutenProvider.multipleSearch(wordList).toString();
	    }

	    @GetMapping("/regexsearch")
	    public String regexsearch(@RequestParam(value = "regex") String rx) throws Exception {

	        return GutenProvider.regExSearch(rx).toString();
	    }

}
