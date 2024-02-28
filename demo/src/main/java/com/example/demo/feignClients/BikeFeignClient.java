package com.example.demo.feignClients;

import com.example.demo.model.Bike;
import com.example.demo.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bike-service")
public interface BikeFeignClient {

    @PostMapping("/bike")
    Bike save(@RequestBody Bike bike);

    @GetMapping("/bike/byUser/{userId}")
    List<Bike> getBikes(@PathVariable("userId") int userId);
}
