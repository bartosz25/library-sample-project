package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.Payment;

public interface PaymentService {
    public List<Payment> findById();
    public Payment save(Payment payment);
    public void delete(Payment payment);
}