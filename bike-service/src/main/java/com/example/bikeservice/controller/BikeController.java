package com.example.bikeservice.controller;

import com.example.bikeservice.entity.Bike;
import com.example.bikeservice.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    BikeService bikeService;

    @GetMapping
    public ResponseEntity<List<Bike>> getAll(){
        List<Bike> bikes = bikeService.getAll();
        if (bikes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bikes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bike> getCar(@PathVariable("id") int id){
        Bike bike = bikeService.getCarById(id);
        if(bike == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bike);
    }

    @PostMapping()
    public ResponseEntity<Bike> save(@RequestBody Bike car){
        Bike bikeNew = bikeService.save(car);
        return ResponseEntity.ok(bikeNew);
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Bike>> getByUserId(@PathVariable("userId") int userId){
        List<Bike> bikes = bikeService.byUserId(userId);
        return ResponseEntity.ok(bikes);
    }
}
