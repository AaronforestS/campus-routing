package com.ogoqjl.campus.routing.controller;

import com.ogoqjl.campus.routing.dataTransferObject.SearchBarRow;
import com.ogoqjl.campus.routing.model.SearchBar;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/searchbar")
public class SearchBarController {
    private SearchBar sb;
    public SearchBarController(SearchBar sb){
        this.sb = sb;
    }

    @GetMapping("/getNodes/{searchTerm}")
    public ResponseEntity<List<SearchBarRow>> getEntitiesBySearchTerm(@PathVariable String searchTerm) {
        try {
            List<SearchBarRow> response = sb.getSearchBarResult(searchTerm);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

}
