package pl.oliwawyplywa.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oliwawyplywa.web.dto.admins.CreateAdminDTO;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.services.AdminsService;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/admins")
@Validated
public class AdminsController {

    private final AdminsService adminsService;

    public AdminsController(AdminsService adminsService) {
        this.adminsService = adminsService;
    }

    @Operation(
        summary = "Create admin",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Category.class),
                examples = {
                    @ExampleObject(
                        name = "Create example admin",
                        value =
                            """
                            {
                                "username": "Nelit",
                                "password": "Qwerty123!",
                                "password_repeat": "Qwerty123!"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                    {
                      "status": true,
                      "admin": {
                        "username": "Nelit",
                        "password_hash": "$2a$10$PA5XJPoeyPgKXw.uCgyIeeBu9DEFQaLDDpYvpcLECSjEo4omqdOWu",
                        "admin_id": 1
                      }
                    }
                    """
            )
        )
    )
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createAdmin(@Valid @RequestBody CreateAdminDTO createAdminDTO) {
        return adminsService.createAdmin(createAdminDTO)
            .map(admin -> ResponseEntity.status(HttpStatus.OK).body(Map.of("status", true, "admin", admin)));
    }

}
