package com.example.car.services;

import com.example.car.entities.Car;
import com.example.car.entities.Client;
import com.example.car.models.CarResponse;
import com.example.car.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceWebClient {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String URL = "http://gateway:8888/SERVICE-CLIENT";

    public List<CarResponse> findAll() {
        List<Car> cars = carRepository.findAll();
        Client[] clients = getClientsFromService();

        return cars.stream()
                .map(car -> mapToCarResponse(car, clients))
                .toList();
    }

    private Client[] getClientsFromService() {
        try {
            return webClientBuilder.baseUrl(URL)
                    .build()
                    .get()
                    .uri("/api/client")
                    .retrieve()
                    .bodyToMono(Client[].class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Failed to fetch client data", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching clients", ex);
        }
    }

    private CarResponse mapToCarResponse(Car car, Client[] clients) {
        Optional<Client> foundClient = Arrays.stream(clients)
                .filter(client -> client.getId().equals(car.getClient_id()))
                .findFirst();

        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .client(foundClient.orElse(null))
                .matricue(car.getMatricule())
                .model(car.getModel())
                .build();
    }

    public CarResponse findById(Long id) throws Exception {
        Car car = carRepository.findById(id).orElseThrow(() -> new Exception("Invalid Car Id"));

        Client client = getClientById(car.getClient_id());

        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .client(client)
                .matricue(car.getMatricule())
                .model(car.getModel())
                .build();
    }

    private Client getClientById(Long clientId) {
        try {
            return webClientBuilder.baseUrl(URL)
                    .build()
                    .get()
                    .uri("/api/client/" + clientId)
                    .retrieve()
                    .bodyToMono(Client.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Failed to fetch client data for ID: " + clientId, ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching client with ID: " + clientId, ex);
        }
    }
}