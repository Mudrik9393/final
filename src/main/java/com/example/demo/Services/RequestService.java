package com.example.demo.Services;

import java.time.LocalDate;

import com.example.demo.Repository.RequestRepository;
import com.example.demo.model.Request;

public class RequestService {
    
    private final RequestRepository requestRepository; 

      public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }
    public String generateAccountNumber() {
    // Example: ACC + year + random number
    return "ACC" + LocalDate.now().getYear() + (int)(Math.random() * 100000);
}

public Request createRequest(Request request) {
    request.setAccountNumber(generateAccountNumber());
    request.setMessage(null);  // no message on creation
    request.setDate(LocalDate.now());
    return requestRepository.save(request);
}

}
