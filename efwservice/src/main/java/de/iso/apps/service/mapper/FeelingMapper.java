package de.iso.apps.service.mapper;

import de.iso.apps.domain.*;
import de.iso.apps.service.dto.FeelingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Feeling and its DTO FeelingDTO.
 */
@Mapper(componentModel = "spring", uses = {FeelWheelMapper.class})
public interface FeelingMapper extends EntityMapper<FeelingDTO, Feeling> {

    @Mapping(source = "feelwheel.id", target = "feelwheelId")
    @Mapping(source = "feelwheel.subject", target = "feelwheelSubject")
    FeelingDTO toDto(Feeling feeling);

    @Mapping(source = "feelwheelId", target = "feelwheel")
    Feeling toEntity(FeelingDTO feelingDTO);

    default Feeling fromId(Long id) {
        if (id == null) {
            return null;
        }
        Feeling feeling = new Feeling();
        feeling.setId(id);
        return feeling;
    }
}
