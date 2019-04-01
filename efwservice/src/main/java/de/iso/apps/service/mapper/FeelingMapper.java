package de.iso.apps.service.mapper;

import de.iso.apps.domain.Feeling;
import de.iso.apps.service.dto.FeelingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Feeling and its DTO FeelingDTO.
 */
@Mapper(componentModel = "spring", uses = {FeelWheelMapper.class}) public interface FeelingMapper
    extends EntityMapper<FeelingDTO, Feeling> {
    
    @Override
    @Mapping(source = "feelwheelId", target = "feelwheel")
    Feeling toEntity(FeelingDTO feelingDTO);
    
    @Override
    @Mapping(source = "feelwheel.id", target = "feelwheelId")
    @Mapping(source = "feelwheel.subject", target = "feelwheelSubject")
    FeelingDTO toDto(Feeling feeling);
    
    default Feeling fromId(Long id) {
        if (id == null) {
            return null;
        }
        Feeling feeling = new Feeling();
        feeling.setId(id);
        return feeling;
    }
}
