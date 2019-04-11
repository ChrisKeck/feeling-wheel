package de.iso.apps.service;

import de.iso.apps.EfwgatewayApp;
import de.iso.apps.config.Constants;
import de.iso.apps.domain.User;
import de.iso.apps.repository.UserRepository;
import de.iso.apps.repository.search.UserSearchRepository;
import de.iso.apps.service.dto.UserDTO;
import de.iso.apps.service.util.RandomUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class) @SpringBootTest(classes = EfwgatewayApp.class) @Transactional
public class UserServiceIntTest {
    
    @Mock DateTimeProvider dateTimeProvider;
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    
    /**
     * This repository is mocked in the de.iso.apps.repository.search test package.
     *
     * @see de.iso.apps.repository.search.UserSearchRepositoryMockConfiguration
     */
    private UserSearchRepository mockUserSearchRepository = new UserSearchRepository() {
        @Override
        public void findOneByEmailIgnoreCase(String oldMail) {
        
        }
        
        @Override
        public <S extends User> S index(S s) {
            return null;
        }
        
        @Override
        public Iterable<User> search(QueryBuilder queryBuilder) {
            return null;
        }
        
        @Override
        public Page<User> search(QueryBuilder queryBuilder, Pageable pageable) {
            return null;
        }
        
        @Override
        public Page<User> search(SearchQuery searchQuery) {
            return null;
        }
        
        @Override
        public Page<User> searchSimilar(User user, String[] strings, Pageable pageable) {
            return null;
        }
        
        @Override
        public void refresh() {
        
        }
        
        @Override
        public Class<User> getEntityClass() {
            return null;
        }
        
        @Override
        public Iterable<User> findAll(Sort sort) {
            return null;
        }
        
        @Override
        public Page<User> findAll(Pageable pageable) {
            return null;
        }
        
        @Override
        public <S extends User> S save(S s) {
            return null;
        }
        
        @Override
        public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
            return null;
        }
        
        @Override
        public Optional<User> findById(Long aLong) {
            return Optional.empty();
        }
        
        @Override
        public boolean existsById(Long aLong) {
            return false;
        }
        
        @Override
        public Iterable<User> findAll() {
            return userRepository.findAll();
        }
        
        @Override
        public Iterable<User> findAllById(Iterable<Long> iterable) {
            return null;
        }
        
        @Override
        public long count() {
            return 0;
        }
        
        @Override
        public void deleteById(Long aLong) {
        
        }
        
        @Override
        public void delete(User user) {
        
        }
        
        @Override
        public void deleteAll(Iterable<? extends User> iterable) {
        
        }
        
        @Override
        public void deleteAll() {
        
        }
    };
    @Autowired private AuditingHandler auditingHandler;
    private User user;
    
    @Before
    public void init() {
        user = new User();
        user.setLogin("johndoe");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("en");
        
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }
    
    @Test
    @Transactional
    public void assertThatUserMustExistToResetPassword() {
        userRepository.saveAndFlush(user);
        Optional<UserDTO> maybeUser = userService.requestPasswordReset("invalid.login@localhost");
        assertThat(maybeUser).isNotPresent();
    
        userService.requestPasswordReset(user.getEmail());
        Optional<User> userOptional = userRepository.findOneByLogin(user.getLogin());
    
        assertThat(userOptional).isPresent();
        assertThat(userOptional.orElse(null).getEmail()).isEqualTo(user.getEmail());
        assertThat(userOptional.orElse(null).getResetDate()).isNotNull();
        assertThat(userOptional.orElse(null).getResetKey()).isNotNull();
    }
    
    @Test
    @Transactional
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        user.setActivated(false);
        userRepository.saveAndFlush(user);
    
        Optional<UserDTO> maybeUser = userService.requestPasswordReset(user.getLogin());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }
    
    @Test
    @Transactional
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);
    
        Optional<UserDTO> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }
    
    @Test
    @Transactional
    public void assertThatResetKeyMustBeValid() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey("1234");
        userRepository.saveAndFlush(user);
    
        Optional<UserDTO> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }
    
    @Test
    @Transactional
    public void assertThatUserCanResetPassword() {
        String oldPassword = user.getPassword();
        Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);
    
        userService.completePasswordReset("johndoe2", user.getResetKey());
        Optional<User> userOptional = userRepository.findOneByLogin(user.getLogin());
        assertThat(userOptional).isPresent();
        assertThat(userOptional.orElse(null).getResetDate()).isNull();
        assertThat(userOptional.orElse(null).getResetKey()).isNull();
        assertThat(userOptional.orElse(null).getPassword()).isNotEqualTo(oldPassword);
        
        userRepository.delete(user);
    }
    
    @Test
    @Transactional
    public void testFindNotActivatedUsersByCreationDateBefore() {
        Instant now = Instant.now();
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
        user.setActivated(false);
        User dbUser = userRepository.saveAndFlush(user);
        dbUser.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
        userRepository.saveAndFlush(user);
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isNotEmpty();
        userService.removeNotActivatedUsers();
        users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isEmpty();
        
        // Verify Elasticsearch mock
        verify(mockUserSearchRepository, times(1)).delete(user);
    }
    
    @Test
    @Transactional
    public void assertThatAnonymousUserIsNotGet() {
        user.setLogin(Constants.ANONYMOUS_USER);
        if (!userRepository.findOneByLogin(Constants.ANONYMOUS_USER).isPresent()) {
            userRepository.saveAndFlush(user);
        }
        PageRequest pageable = PageRequest.of(0, (int) userRepository.count());
        Page<UserDTO> allManagedUsers = userService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent()
                                  .stream()
                                  .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin()))).isTrue();
    }
    
    
    @Test
    @Transactional
    public void testRemoveNotActivatedUsers() {
        // custom "now" for audit to use as creation date
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(Instant.now().minus(30, ChronoUnit.DAYS)));
        
        user.setActivated(false);
        userRepository.saveAndFlush(user);
        
        assertThat(userRepository.findOneByLogin("johndoe")).isPresent();
        userService.removeNotActivatedUsers();
        assertThat(userRepository.findOneByLogin("johndoe")).isNotPresent();
        
        // Verify Elasticsearch mock
        verify(mockUserSearchRepository, times(1)).delete(user);
    }
    
}
