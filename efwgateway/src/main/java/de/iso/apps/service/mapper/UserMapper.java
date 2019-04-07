package de.iso.apps.service.mapper;

import de.iso.apps.contracts.Userable;
import de.iso.apps.domain.Authority;
import de.iso.apps.domain.User;
import de.iso.apps.service.dto.MailChangingDTO;
import de.iso.apps.service.dto.UserDTO;
import lombok.NonNull;
import lombok.var;
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
        map(user, userDTO);
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setCreatedDate(user.getCreatedDate());
        userDTO.setLastModifiedBy(user.getLastModifiedBy());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        return userDTO;
    }
    
    private void map(Userable userDTO, Userable user) {
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        user.setActivated(userDTO.isActivated());
        user.setLangKey(userDTO.getLangKey());
    }
    
    public List<User> userDTOsToUsers(@NonNull List<UserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
    }
    
    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            map(userDTO, user);
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }
    
    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();
        
        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(string -> {
                Authority auth = new Authority();
                auth.setName(string);
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
    
    public MailChangingDTO userDTOToMailChangingDTO(UserDTO newMailUserDTO, UserDTO oldMailUserDTO) {
        var mailBuilder = MailChangingDTO.builder();
        
        if (newMailUserDTO != null) {
            mailBuilder.newMail(newMailUserDTO.getEmail());
        }
        if (oldMailUserDTO != null) {
            mailBuilder.oldMail(oldMailUserDTO.getEmail());
        }
        return mailBuilder.build();
    }
}
