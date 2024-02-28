package com.example.demo.feignClients;

import com.example.demo.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//hacemos la conexion con el car service ingresando el nombre de nuestro servicio y su URL
@FeignClient(name = "car-service")
public interface CarFeignClient {

    //llamamos al endpoint del controller de car
    @PostMapping("/car")
    Car save(@RequestBody Car car);

    @GetMapping("/car/byUser/{userId}")
    List<Car> getCars(@PathVariable("userId") int userId);

}
