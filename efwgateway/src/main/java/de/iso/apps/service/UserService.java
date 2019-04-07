package de.iso.apps.service;

import de.iso.apps.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDTO> activateRegistration(String key);
    
    Optional<UserDTO> completePasswordReset(String newPassword, String key);
    
    Optional<UserDTO> requestPasswordReset(String mail);
    
    UserDTO registerUser(UserDTO userDTO, String password);
    
    UserDTO createUser(UserDTO userDTO);
    
    void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl);
    
    Optional<UserDTO> updateUser(UserDTO userDTO);
    
    void deleteUser(String login);
    
    void changePassword(String currentClearTextPassword, String newPassword);
    
    Page<UserDTO> getAllManagedUsers(Pageable pageable);
    
    Optional<UserDTO> getUserWithAuthoritiesByLogin(String login);
    
    Optional<UserDTO> getUserWithAuthoritiesByEmail(String email);
    
    Optional<UserDTO> getUserWithAuthorities(Long id);
    
    Optional<UserDTO> getUserWithAuthorities();
    
    void removeNotActivatedUsers();
    
    List<String> getAuthorities();
    
    List<UserDTO> search(String queryStringQuery);
}
