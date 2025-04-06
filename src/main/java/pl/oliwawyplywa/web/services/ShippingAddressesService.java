package pl.oliwawyplywa.web.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.shippingAddresses.CreateAddressDTO;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.ShippingAddressesRepository;
import pl.oliwawyplywa.web.schemas.ShippingAddress;
import pl.oliwawyplywa.web.schemas.User;

@Service
public class ShippingAddressesService {

    private final UsersService usersService;
    private final ShippingAddressesRepository shippingAddressesRepository;

    public ShippingAddressesService(UsersService usersService, ShippingAddressesRepository shippingAddressesRepository) {
        this.usersService = usersService;
        this.shippingAddressesRepository = shippingAddressesRepository;
    }

    public ShippingAddress getShippingAddress(int shippingAddressId) {
        return shippingAddressesRepository.findById(shippingAddressId).orElseThrow(() -> new HTTPException(HttpStatus.NOT_FOUND, "Shipping address not found"));
    }

    public ShippingAddress createShippingAddress(int userId, CreateAddressDTO addressDTO) {
        User user = usersService.getUserById(userId);
        return shippingAddressesRepository.save(new ShippingAddress(
            user, addressDTO.getCountry(), addressDTO.getVoivodeship(),
            addressDTO.getCity(), addressDTO.getPostcode(), addressDTO.getStreet()
        ));
    }

    public void deleteShippingAddress(int shippingAddressId) {
        shippingAddressesRepository.delete(getShippingAddress(shippingAddressId));
    }
}
