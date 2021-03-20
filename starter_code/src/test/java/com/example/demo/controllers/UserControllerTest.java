package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository" , userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("test", u.getUsername());
        Assert.assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void create_user_bad_password_case() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPa");
        createUserRequest.setConfirmPassword("testPa");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        verify(bCryptPasswordEncoder, times(0)).encode(anyString());
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNull(u);
    }

    @Test
    public void create_user_not_matching_password_case() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword1");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        verify(bCryptPasswordEncoder, times(0)).encode(anyString());
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNull(u);
    }

    @Test
    public void find_user_by_username_happy_path() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("testUser");

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        Assert.assertNotNull(u);
        Assert.assertEquals(user, u);
    }

    @Test
    public void find_user_by_id_happy_path() throws Exception {
        User user = new User();
        user.setId(2);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(2L);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        Assert.assertNotNull(u);
        Assert.assertEquals(user, u);
    }
}
