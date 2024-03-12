package com.ogoqjl.campus.routing.controller;

import com.ogoqjl.campus.routing.dataAccess.DataAccessObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
public class DAOController {

    private DataAccessObject dao;

    public DAOController(DataAccessObject dao){
        this.dao = dao;
    }

    @GetMapping("/getFromTo/{nodeFrom}/{nodeTo}")
    public ResponseEntity<Double> getEntitiesByIds(@PathVariable int nodeFrom, @PathVariable int nodeTo) {
        try {
            double entities = dao.getRouteForTwoPoints(nodeFrom, nodeTo);
            return ResponseEntity.ok(entities);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }

    }
}
