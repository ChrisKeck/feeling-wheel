package de.iso.apps.service.mapper;

import de.iso.apps.domain.FeelWheel;
import de.iso.apps.service.dto.FeelWheelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity FeelWheel and its DTO FeelWheelDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class}) public interface FeelWheelMapper
    extends EntityMapper<FeelWheelDTO, FeelWheel> {
    
    @Override
    @Mapping(target = "feelings", ignore = true)
    @Mapping(source = "employeeId", target = "employee")
    FeelWheel toEntity(FeelWheelDTO feelWheelDTO);
    
    @Override
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.email", target = "employeeEmail")
    FeelWheelDTO toDto(FeelWheel feelWheel);
    
    default FeelWheel fromId(Long id) {
        if (id == null) {
            return null;
        }
        FeelWheel feelWheel = new FeelWheel();
        feelWheel.setId(id);
        return feelWheel;
    }
}
