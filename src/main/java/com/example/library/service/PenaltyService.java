package com.example.library.service;

import java.util.List;

import com.example.library.form.PenaltyForm;
import com.example.library.model.entity.Payment;
import com.example.library.model.entity.Penalty;
import com.example.library.model.entity.Subscriber;

public interface PenaltyService {
    // public List<Penalty> findById();
    // public Penalty save(Penalty penalty);
    public List<Penalty> saveNotValid(PenaltyForm penaltyForm) throws Exception;
    public List<Penalty> addPayment(List<Penalty> penalties, Payment payment);
    public Penalty getPenaltyFromCode(String reference, Subscriber subscriber);
    public double getSumBySubscriber(Subscriber subscriber);
    // public void delete(Penalty penalty);
}