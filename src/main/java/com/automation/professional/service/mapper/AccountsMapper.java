package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.AccountsDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Accounts} and its DTO {@link AccountsDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface AccountsMapper extends EntityMapper<AccountsDTO, Accounts> {
    @Mapping(target = "country", source = "country", qualifiedByName = "id")
    AccountsDTO toDto(Accounts s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountsDTO toDtoId(Accounts accounts);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<AccountsDTO> toDtoIdSet(Set<Accounts> accounts);
}
