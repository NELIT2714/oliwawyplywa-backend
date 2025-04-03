package pl.oliwawyplywa.web.utils.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.oliwawyplywa.web.dto.users.PersonalDataDTO;
import pl.oliwawyplywa.web.dto.users.UpdateUserDTO;
import pl.oliwawyplywa.web.schemas.PersonalData;
import pl.oliwawyplywa.web.schemas.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(UpdateUserDTO dto, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonalDataFromDTO(PersonalDataDTO dto, @MappingTarget PersonalData personalData);

}