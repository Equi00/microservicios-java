package com.example.demo;

import com.example.demo.feignClients.BikeFeignClient;
import com.example.demo.feignClients.CarFeignClient;
import com.example.demo.model.Bike;
import com.example.demo.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    Repository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<Usuario> getAll(){
        return userRepository.findAll();
    }

    public Usuario getUserById(int id){
        return userRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario user){
        Usuario userNew = userRepository.save(user);
        return userNew;
    }

    public List<Car> getCars(int userId){
        // le pedimos al restTemplate que nos comunique con el endpoint de Car para que nos pueda
        // devolver los autos por id ingresando la URL
        List<Car> cars = restTemplate.getForObject("http://car-service/car/byUser/" + userId, List.class);
        return cars;
    }

    public List<Bike> getBikes(int userId){
        List<Bike> bikes = restTemplate.getForObject("http://bike-service/bike/byUser/" + userId, List.class);
        return bikes;
    }

    //hacemos el endpoint save de car controller
    public Car saveCar(int userId, Car car){
        car.setUserId(userId);
        Car carNew = carFeignClient.save(car);
        return carNew;
    }

    public Bike saveBike(int userId, Bike bike){
        bike.setUserId(userId);
        Bike bikeNew = bikeFeignClient.save(bike);
        return bikeNew;
    }

    public Map<String, Object> getUserAndVehicles(int userId){
        Map<String, Object> result = new HashMap<>();
        Usuario user = userRepository.findById(userId).orElse(null);

        if(user == null){
            result.put("Mensaje", "No existe el usuario");
            return result;
        }

        result.put("User", user);
        List<Car> cars = carFeignClient.getCars(userId);

        if(cars.isEmpty()){
            result.put("Cars", "Ese user no tiene coches");
        }else{
            result.put("Cars", cars);
        }

        List<Bike> bikes = bikeFeignClient.getBikes(userId);

        if(bikes.isEmpty()){
            result.put("Bikes", "Ese user no tiene motos");
        }else{
            result.put("Bikes", bikes);
        }

        return result;
    }


}
