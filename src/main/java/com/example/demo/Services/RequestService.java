package com.example.demo.Services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.RequestRepository;
import com.example.demo.model.Request;

@Service
public class RequestService {
    
    private final RequestRepository requestRepository; 

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public String generateAccountNumber() {
        // Mfano: ACC + mwaka + nambari nasibu ya 5 digits
        return "ACC" + LocalDate.now().getYear() + String.format("%05d", (int)(Math.random() * 100000));
    }

    public Request createRequest(Request request) {
        request.setAccountNumber(generateAccountNumber());
        request.setMessage(null);  // default message none
        if (request.getDate() == null) {
            request.setDate(LocalDate.now());
        }
        return requestRepository.save(request);
    }
}
