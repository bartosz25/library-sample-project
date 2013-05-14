package library.model.repository;

import java.util.List;

import library.model.entity.PaymentMethod;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface PaymentMethodRepository  extends CrudRepository<PaymentMethod, Long> {
    @Query("select pm from PaymentMethod pm")
    public List<PaymentMethod> getAllMethods();

}