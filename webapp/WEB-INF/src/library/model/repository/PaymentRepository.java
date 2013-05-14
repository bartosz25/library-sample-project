package library.model.repository;

import library.model.entity.Payment;

import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository  extends CrudRepository<Payment, Long>
{
}