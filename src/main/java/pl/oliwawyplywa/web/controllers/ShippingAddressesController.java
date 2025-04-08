package pl.oliwawyplywa.web.controllers;

import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.shippingAddresses.AddressResponseDTO;
import pl.oliwawyplywa.web.dto.shippingAddresses.CreateAddressDTO;
import pl.oliwawyplywa.web.schemas.ShippingAddress;
import pl.oliwawyplywa.web.services.ShippingAddressesService;

@RestController
@RequestMapping("/api/shipping-addresses")
public class ShippingAddressesController {

    private final ShippingAddressesService shippingAddressesService;

    public ShippingAddressesController(ShippingAddressesService shippingAddressesService) {
        this.shippingAddressesService = shippingAddressesService;
    }

    @GetMapping(path = "/{id}")
    public AddressResponseDTO getShippingAddress(@PathVariable(name = "id") int shippingAddressId) {
        ShippingAddress shippingAddress = shippingAddressesService.getShippingAddress(shippingAddressId);
        return returnShippingAddress(shippingAddress);
    }

    @PostMapping(path = "/{id}")
    public AddressResponseDTO createShippingAddress(@RequestBody CreateAddressDTO shippingAddressDTO, @PathVariable(name = "id") int userId) {
        ShippingAddress shippingAddress = shippingAddressesService.createShippingAddress(userId, shippingAddressDTO);
        return returnShippingAddress(shippingAddress);
    }

    //    Update shipping addresses implementation

    private AddressResponseDTO returnShippingAddress(ShippingAddress shippingAddress) {
        return new AddressResponseDTO(
<<<<<<< Updated upstream
            shippingAddress.getIdShippingAddress(), shippingAddress.getCountry(),
            shippingAddress.getVoivodeship(), shippingAddress.getCity(),
=======
<<<<<<< Updated upstream
            shippingAddress.getIdShippingAddress(), shippingAddress.getUser().getIdUser(),
            shippingAddress.getCountry(), shippingAddress.getVoivodeship(), shippingAddress.getCity(),
>>>>>>> Stashed changes
            shippingAddress.getPostcode(), shippingAddress.getStreet()
=======
                shippingAddress.getIdShippingAddress(), shippingAddress.getCountry(),
                shippingAddress.getVoivodeship(), shippingAddress.getCity(),
                shippingAddress.getPostcode(), shippingAddress.getStreet(), shippingAddress.getBuildingNumber()
>>>>>>> Stashed changes
        );
    }

}
