package com.phamhieu.bookapi.domain.user;

import com.phamhieu.bookapi.domain.auth.AuthsProvider;
import com.phamhieu.bookapi.error.BadRequestException;
import com.phamhieu.bookapi.error.NotFoundException;
import com.phamhieu.bookapi.persistence.user.UserStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Optional;

import static com.phamhieu.bookapi.fakes.UserAuthenticationTokenFakes.buildAdmin;
import static com.phamhieu.bookapi.fakes.UserAuthenticationTokenFakes.buildContributor;
import static com.phamhieu.bookapi.fakes.UserFakes.buildUser;
import static com.phamhieu.bookapi.fakes.UserFakes.buildUsers;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RequiredArgsConstructor
class UserServiceTest {

    @Mock
    private UserStore userStore;

    @Mock
    private AuthsProvider authsProvider;

    @InjectMocks
    private UserService userService;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldFindAll_OK() {
        final var expected = buildUsers();

        when(userStore.findAll())
                .thenReturn(expected);

        final var actual = userService.findAll();

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getUsername(), actual.get(0).getUsername());
        assertEquals(expected.get(0).getPassword(), actual.get(0).getPassword());
        assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(expected.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(expected.get(0).isEnabled(), actual.get(0).isEnabled());
        assertEquals(expected.get(0).getAvatar(), actual.get(0).getAvatar());
        assertEquals(expected.get(0).getRoleId(), actual.get(0).getRoleId());

        verify(userStore).findAll();
    }

    @Test
    void shouldFindUserById_OK() {
        final var expected = buildUser();
        when(userStore.findById(expected.getId()))
                .thenReturn(Optional.of(expected));

        assertEquals(expected, userService.findById(expected.getId()));
        verify(userStore).findById(expected.getId());
    }

    @Test
    void shouFindUserById_Throw() {
        final var id = randomUUID();
        when(userStore.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(id));
        verify(userStore).findById(id);
    }

    @Test
    void shouldFind_OK() {
        final var user = buildUser();
        final var expected = buildUsers();

        when(userStore.find(anyString())).thenReturn(expected);

        final var actual = userService.find(user.getUsername());

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getUsername(), actual.get(0).getUsername());
        assertEquals(expected.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(expected.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(expected.get(0).isEnabled(), actual.get(0).isEnabled());
        assertEquals(expected.get(0).getRoleId(), actual.get(0).getRoleId());
        assertEquals(expected.get(0).getAvatar(), actual.get(0).getAvatar());
    }

    @Test
    void shouldFind_Thrown() {
        final var name = randomAlphabetic(3, 10);

        when(userStore.find(name)).thenReturn(Collections.emptyList());

        assertTrue(userService.find(name).isEmpty());
        verify(userStore).find(name);
    }

    @Test
    void shouldFindProfile_OK() {
        final var user = buildUser();
        user.setId(buildContributor().getUserId());
        user.setUsername(buildContributor().getUsername());
        user.setFirstName(buildContributor().getFistName());
        user.setLastName(buildContributor().getLastName());
        user.setAvatar(buildContributor().getAvatar());

        when(authsProvider.getCurrentAuthentication()).thenReturn(buildContributor());

        final var actual = userService.findProfile();

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getAvatar(), actual.getAvatar());
    }

    @Test
    void shouldCreate_OK() throws NoSuchAlgorithmException {
        final var user = buildUser();
        user.setPassword(randomAlphabetic(6, 10));

        when(userStore.create(any())).thenReturn(user);

        final var actual = userService.create(user);

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.isEnabled(), actual.isEnabled());
        assertEquals(user.getAvatar(), actual.getAvatar());
        assertEquals(user.getRoleId(), actual.getRoleId());

        verify(userStore).create(any());
    }

    @Test
    void shouldCreate_ThrowBadRequest() {
        final var expected = buildUser();
        expected.setPassword(null);

        assertThrows(BadRequestException.class, () -> userService.create(expected));
    }

    @Test
    void shouldCreate_Exist() {
        final var user = buildUser();

        when(userStore.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.create(user));
    }

    @Test
    void shouldUpdate_OK() {
        final var user = buildUser();
        final var updatedUser = buildUser();
        updatedUser.setId(user.getId());
        updatedUser.setPassword(randomAlphabetic(6, 10));

        when(userStore.findById((user.getId()))).thenReturn(Optional.of(user));
        when(userStore.update(user)).thenReturn(user);

        final var actual = userService.update(user.getId(), updatedUser);

        assertEquals(updatedUser.getId().toString(), actual.getId().toString());
        assertEquals(updatedUser.getUsername(), actual.getUsername());
        assertEquals(updatedUser.getFirstName(), actual.getFirstName());
        assertEquals(updatedUser.getLastName(), actual.getLastName());
        assertEquals(updatedUser.getAvatar(), actual.getAvatar());
        assertEquals(updatedUser.getRoleId().toString(), actual.getRoleId().toString());
        assertEquals(updatedUser.isEnabled(), actual.isEnabled());

        verify(userStore).update(user);
    }

    @Test
    void shouldUpdateWithoutPassword_OK() {
        final var user = buildUser();
        final var updatedUser = buildUser();
        updatedUser.setId(user.getId());
        updatedUser.setPassword(null);

        when(userStore.findById((user.getId()))).thenReturn(Optional.of(user));
        when(userStore.update(user)).thenReturn(user);

        final var actual = userService.update(user.getId(), updatedUser);

        assertEquals(updatedUser.getId().toString(), actual.getId().toString());
        assertEquals(updatedUser.getUsername(), actual.getUsername());
        assertEquals(updatedUser.getFirstName(), actual.getFirstName());
        assertEquals(updatedUser.getLastName(), actual.getLastName());
        assertEquals(updatedUser.getAvatar(), actual.getAvatar());
        assertEquals(updatedUser.getRoleId().toString(), actual.getRoleId().toString());
        assertEquals(updatedUser.isEnabled(), actual.isEnabled());

        verify(userStore).update(user);
    }

    @Test
    void shouldUpdate_ThrownPasswordLengthException() {
        final var user = buildUser();
        final var userUpdate = buildUser();
        userUpdate.setPassword(randomAlphabetic(1, 5));

        when(userStore.findById((user.getId()))).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.update(user.getId(), userUpdate));
    }

    @Test
    void shouldUpdate_NotFound() {
        final var userUpdate = buildUser();
        final var uuid = randomUUID();
        userUpdate.setPassword(randomAlphabetic(6, 10));

        when(userStore.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(uuid, userUpdate));
        verify(userStore).findById(uuid);
    }

    @Test
    void shouldUpdate_UsernameExits() {
        final var user = buildUser();
        final var userExist = buildUser();
        final var userUpdate = buildUser();
        userExist.setUsername("hieu");

        userUpdate.setUsername(userExist.getUsername());

        when(userStore.findById(user.getId())).thenReturn(Optional.of(user));
        when(userStore.findByUsername(userUpdate.getUsername())).thenReturn(Optional.of(userUpdate));

        assertThrows(BadRequestException.class, () -> userService.update(user.getId(), userUpdate));

        verify(userStore, never()).update(userUpdate);
    }

    @Test
    void shouldUpdateProfile_OK() {
        final var user = buildUser();

        when(authsProvider.getCurrentUserId()).thenReturn(buildAdmin().getUserId());
        when(authsProvider.getCurrentRole()).thenReturn(buildAdmin().getRole());
        user.setPassword(randomAlphabetic(6, 10));
        user.setId(buildAdmin().getUserId());

        when(userStore.update(user)).thenReturn(user);
        when(userStore.findById(user.getId())).thenReturn(Optional.of(user));

        final var actual = userService.updateProfile(user);

        assertEquals(user.getId().toString(), actual.getId().toString());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getAvatar(), actual.getAvatar());
        assertEquals(user.getRoleId().toString(), actual.getRoleId().toString());
        assertEquals(user.isEnabled(), actual.isEnabled());

        verify(userStore).update(user);
        verify(userStore).findById(user.getId());
    }

    @Test
    void shouldDeleteById_Ok() {
        final var user = buildUser();

        userService.delete(user.getId());
        verify(userStore).delete(user.getId());
    }

    @Test
    void shouldDeleteById_NotFound() {
        final var id = randomUUID();

        when(userStore.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(id));
        verify(userStore).findById(id);
    }

}