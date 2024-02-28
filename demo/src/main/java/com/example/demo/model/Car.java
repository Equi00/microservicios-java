package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    private String brand;
    private String model;
    private int userId;

    public void setUserId(int id){
        userId = id;
    }
}
