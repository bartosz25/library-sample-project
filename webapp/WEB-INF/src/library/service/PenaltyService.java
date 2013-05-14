package library.service;

import java.util.List;

import library.form.PenaltyForm;
import library.model.entity.Payment;
import library.model.entity.Penalty;
import library.model.entity.Subscriber;

public interface PenaltyService {
    // public List<Penalty> findById();
    // public Penalty save(Penalty penalty);
    public List<Penalty> saveNotValid(PenaltyForm penaltyForm) throws Exception;
    public List<Penalty> addPayment(List<Penalty> penalties, Payment payment);
    public Penalty getPenaltyFromCode(String reference, Subscriber subscriber);
    public double getSumBySubscriber(Subscriber subscriber);
    // public void delete(Penalty penalty);
}