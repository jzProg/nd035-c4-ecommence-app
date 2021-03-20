package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository" , userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_happy_path() throws Exception {
        User user = createTestUser();
        Item item = createTestItem();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();

        Assert.assertNotNull(c);
        Assert.assertEquals(Long.valueOf(1), c.getItems().get(0).getId());
        Assert.assertEquals(item.getPrice().multiply(new BigDecimal(2)), c.getTotal());
    }

    @Test
    public void add_to_cart_user_not_found_case() throws Exception {
        User user = createTestUser();
        Item item = createTestItem();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart c = response.getBody();
        Assert.assertNull(c);
    }

    @Test
    public void add_to_cart_item_not_found_case() throws Exception {
        User user = createTestUser();
        Item item = createTestItem();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart c = response.getBody();
        Assert.assertNull(c);
    }

    @Test
    public void remove_from_cart_ser_not_found_case() throws Exception {
        User user = createTestUser();
        Item item = createTestItem();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(Arrays.asList(item)));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart c = response.getBody();
        Assert.assertNull(c);
    }

    @Test
    public void remove_from_cart_item_not_found_case() throws Exception {
        User user = createTestUser();
        Item item = createTestItem();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(Arrays.asList(item)));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart c = response.getBody();
        Assert.assertNull(c);
    }

    @Test
    public void remove_from_cart_happy_path() throws Exception {
        User user = createTestUser();
        Item item = createTestItem();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(Arrays.asList(item)));
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();

        Assert.assertNotNull(c);
        Assert.assertTrue(c.getItems().isEmpty());
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setCart(new Cart());
        return user;
    }

    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setPrice(new BigDecimal(200));
        item.setDescription("a test item");
        return item;
    }
}
