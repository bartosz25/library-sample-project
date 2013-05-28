package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.PaymentMethod;
import com.example.library.model.entity.Subscriber;

public interface PaymentMethodService {
    public List<PaymentMethod> getAllMethodsBySubscriber(Subscriber subscriber);
}