package de.iso.apps.service.mapper;

import de.iso.apps.domain.*;
import de.iso.apps.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.email", target = "employeeEmail")
    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "feelWheels", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(source = "employeeId", target = "employee")
    Employee toEntity(EmployeeDTO employeeDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
