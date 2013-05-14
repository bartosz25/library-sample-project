package library.service;

import java.util.List;

import library.model.entity.PaymentMethod;
import library.model.entity.Subscriber;

public interface PaymentMethodService {
    public List<PaymentMethod> getAllMethodsBySubscriber(Subscriber subscriber);
}