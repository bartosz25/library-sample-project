package com.example.library.model.repository;

import com.example.library.model.entity.Payment;

import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository  extends CrudRepository<Payment, Long>
{
}