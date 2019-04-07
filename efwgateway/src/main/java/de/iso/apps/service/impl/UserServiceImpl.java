package de.iso.apps.service.impl;

import de.iso.apps.config.Constants;
import de.iso.apps.domain.Authority;
import de.iso.apps.domain.User;
import de.iso.apps.repository.AuthorityRepository;
import de.iso.apps.repository.UserRepository;
import de.iso.apps.repository.search.UserSearchRepository;
import de.iso.apps.security.AuthoritiesConstants;
import de.iso.apps.security.SecurityUtils;
import de.iso.apps.service.UserService;
import de.iso.apps.service.dto.UserDTO;
import de.iso.apps.service.mapper.UserMapper;
import de.iso.apps.service.util.RandomUtil;
import de.iso.apps.web.rest.errors.EmailAlreadyUsedException;
import de.iso.apps.web.rest.errors.InvalidPasswordException;
import de.iso.apps.web.rest.errors.LoginAlreadyUsedException;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service class for managing users.
 */
@Service @Transactional public class UserServiceImpl implements UserService {
    
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final UserSearchRepository userSearchRepository;
    
    private final AuthorityRepository authorityRepository;
    
    private final CacheManager cacheManager;
    private final UserMapper userMapper;
    
    public UserServiceImpl(UserRepository userRepository,
                           UserSearchRepository userSearchRepository,
                           AuthorityRepository authorityRepository,
                           CacheManager cacheManager,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSearchRepository = userSearchRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }
    
    @Override
    public Optional<UserDTO> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key).map(user -> {
            // activate given user for the registration key.
            user.setActivated(true);
            user.setActivationKey(null);
            userSearchRepository.save(user);
            this.clearUserCaches(user);
            log.debug("Activated user: {}", user);
            return userMapper.userToUserDTO(user);
        });
    }
    
    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }
    
    @Override
    public Optional<UserDTO> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
                             .filter(user -> user.getResetDate()
                                                 .isAfter(Instant.now().minusSeconds(86400)))
                             .map(user -> {
                                 user.setPassword(passwordEncoder.encode(newPassword));
                                 user.setResetKey(null);
                                 user.setResetDate(null);
                                 this.clearUserCaches(user);
                                 return userMapper.userToUserDTO(user);
                             });
    }
    
    @Override
    public Optional<UserDTO> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail).filter(User::isActivated).map(user -> {
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(Instant.now());
            this.clearUserCaches(user);
            return userMapper.userToUserDTO(user);
        });
    }
    
    @Override
    public UserDTO registerUser(UserDTO userDTO, String password) {
        isUserExisting(userDTO);
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        this.clearUserCaches(newUser);
        var dto = userMapper.userToUserDTO(newUser);
        log.debug("Created Information for User: {}", newUser);
        return dto;
    }
    
    private void isUserExisting(UserDTO userDTO) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
    }
    
    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }
    
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream().map(authorityRepository::findById).filter(
                Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        var newUser = userRepository.save(user);
        userSearchRepository.save(user);
        var dto = userMapper.userToUserDTO(newUser);
        
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return dto;
    }
    
    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    @Override
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email.toLowerCase());
            user.setLangKey(langKey);
            user.setImageUrl(imageUrl);
            this.clearUserCaches(user);
            log.debug("Changed Information for User: {}", user);
        });
    }
    
    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    @Override
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId())).filter(Optional::isPresent).map(Optional::get).map(
            user -> {
                var oldMail = userMapper.userToUserDTO(user);
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream().map(authorityRepository::findById).filter(Optional::isPresent).map(
                    Optional::get).forEach(managedAuthorities::add);
                var newUser = userMapper.userToUserDTO(userSearchRepository.save(user));
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            }).map(userMapper::userToUserDTO);
    }
    
    @Override
    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            deleteUser(user);
        });
    }
    
    private void deleteUser(User user) {
        userRepository.delete(user);
        userSearchRepository.delete(user);
        this.clearUserCaches(user);
        log.debug("Deleted User: {}", user);
    }
    
    @Override
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            String currentEncryptedPassword = user.getPassword();
            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                throw new InvalidPasswordException();
            }
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            this.clearUserCaches(user);
            log.debug("Changed password for User: {}", user);
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(userMapper::userToUserDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserWithAuthoritiesByLogin(String login) {
        return Optional.ofNullable(userMapper.userToUserDTO(userRepository.findOneWithAuthoritiesByLogin(login)));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserWithAuthoritiesByEmail(String email) {
        return Optional.ofNullable(userMapper.userToUserDTO(userRepository.findOneByEmailIgnoreCase(email)));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserWithAuthorities(Long id) {
        return Optional.ofNullable(userMapper.userToUserDTO(userRepository.findOneWithAuthoritiesById(id)));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserWithAuthorities() {
        return Optional.ofNullable(userMapper.userToUserDTO(SecurityUtils.getCurrentUserLogin()
                                                                         .flatMap(userRepository::findOneWithAuthoritiesByLogin)));
    }
    
    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS)).forEach(
            user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                deleteUser(user);
            });
    }
    
    /**
     * @return a list of all the authorities
     */
    @Override
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }
    
    @Override
    public List<UserDTO> search(String queryStringQuery) {
        return StreamSupport.stream(userSearchRepository.search(queryStringQuery(queryStringQuery)).spliterator(),
                                    false).map(userMapper::userToUserDTO).collect(Collectors.toList());
    }
}
