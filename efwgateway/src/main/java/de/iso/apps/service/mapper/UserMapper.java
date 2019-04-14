package de.iso.apps.service.mapper;

import de.iso.apps.contracts.BaseUser;
import de.iso.apps.contracts.TimeMeasure;
import de.iso.apps.domain.Authority;
import de.iso.apps.domain.User;
import de.iso.apps.service.dto.UserDTO;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity User and its DTO called UserDTO.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct support is still in beta, and
 * requires a manual step with an IDE.
 */
@Service public class UserMapper {
    
    public List<UserDTO> usersToUserDTOs(@NonNull List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
    }
    
    public UserDTO userToUserDTO(Optional<User> opuser) {
        return opuser.map(this::userToUserDTO).orElse(null);
    }
    public UserDTO userToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        map(userDTO, user);
        mapTimeMeasures(userDTO, user);
        userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        return userDTO;
    }
    
    private void mapTimeMeasures(TimeMeasure user, TimeMeasure userDTO) {
        user.setCreatedDate(userDTO.getCreatedDate());
        user.setLastModifiedBy(userDTO.getLastModifiedBy());
        user.setLastModifiedDate(userDTO.getLastModifiedDate());
        user.setCreatedBy(userDTO.getCreatedBy());
    }
    
    private void map(BaseUser user, BaseUser userDTO) {
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setActivated(userDTO.isActivated());
        user.setLangKey(userDTO.getLangKey());
        user.setImageUrl(userDTO.getImageUrl());
    }
    
    public List<User> userDTOsToUsers(@NonNull List<UserDTO> userDTOs) {
        return userDTOs.stream()
                       .filter(Objects::nonNull)
                       .map(this::userDTOToUser)
                       .collect(Collectors.toList());
    }
    
    public User userDTOToUser(UserDTO userDTO) {
        return userDTO != null ?
               mapToUser(userDTO) :
               null;
    
    }
    
    protected User mapToUser(UserDTO userDTO) {
        User user = new User();
        map(user, userDTO);
        mapTimeMeasures(userDTO, user);
        Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
        user.setAuthorities(authorities);
        return user;
    }
    
    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();
        
        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(s -> {
                Authority auth = new Authority();
                auth.setName(s);
                return auth;
            }).collect(Collectors.toSet());
        }
        
        return authorities;
    }
    
    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
    
}
