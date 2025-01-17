package com.example.car.controllers;

import com.example.car.models.CarResponse;
import com.example.car.services.CarServiceFeignClient;
import com.example.car.services.CarServiceRestTemplate;
import com.example.car.services.CarServiceWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/car")
public class CarController {
    @Autowired
    //private CarServiceRestTemplate carService;
    private CarServiceWebClient carService;
    //private CarServiceFeignClient carService;

    @GetMapping
    public List<CarResponse> findAll() {
        return carService.findAll();
    }

    @GetMapping("/{id}")
    public CarResponse findById(@PathVariable Long id) throws Exception {
        return carService.findById(id);
    }
}