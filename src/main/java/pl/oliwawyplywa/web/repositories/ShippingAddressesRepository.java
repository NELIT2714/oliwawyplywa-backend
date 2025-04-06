package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.ShippingAddress;

@Repository
public interface ShippingAddressesRepository extends JpaRepository<ShippingAddress, Integer> {

}
