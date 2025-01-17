package com.example.car.config;

import com.example.car.entities.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-CLIENT", url = "http://localhost:8888/SERVICE-CLIENT")
public interface ClientFeignClient {
    @GetMapping("/api/client")
    Client[] getAllClients();

    @GetMapping("/api/client/{id}")
    Client getClientById(@PathVariable("id") Long id);
}