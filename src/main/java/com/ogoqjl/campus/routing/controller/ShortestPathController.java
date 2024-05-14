package com.ogoqjl.campus.routing.controller;

import com.ogoqjl.campus.routing.model.ShortestPath;
import com.ogoqjl.campus.routing.dataTransferObject.ShortestPathResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/shortestpath")
public class ShortestPathController {
    private ShortestPath sp;
    public ShortestPathController(ShortestPath sp){
        this.sp = sp;
    }

    @GetMapping("/getFromTo/{nodeFrom}/{nodeTo}")
    public ResponseEntity<ShortestPathResult> getEntitiesByIds(@PathVariable int nodeFrom, @PathVariable int nodeTo) {
        try {
            ShortestPathResult response = sp.getRouteForTwoPoints(nodeFrom, nodeTo);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }
}
