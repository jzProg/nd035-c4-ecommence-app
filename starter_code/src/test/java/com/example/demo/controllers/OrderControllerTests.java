package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class OrderControllerTests {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository" , userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void get_orders_for_user_happy_path() {
        User user = createTestUser();
        UserOrder order = createTestOrder(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        Assert.assertNotNull(orders);
        Assert.assertEquals(1, orders.size());
        Assert.assertEquals(order, orders.get(0));
    }

    @Test
    public void get_orders_for_user_not_found_case() {
        User user = createTestUser();
        UserOrder order = createTestOrder(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        Assert.assertNull(orders);
    }

    @Test
    public void submit_happy_path() {
        User user = createTestUser();
        Cart cart = createTestCart(user);
        user.setCart(cart);
        UserOrder order = createTestOrder(user);
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        List<Item> items = new ArrayList<>(Arrays.asList(item));
        cart.setItems(items);
        order.setItems(items);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        verify(orderRepository, times(1)).save(any());

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        UserOrder ord = response.getBody();
        Assert.assertNotNull(ord);
        Assert.assertEquals(order.getItems(), ord.getItems());
        Assert.assertEquals(order.getUser(), ord.getUser());
        Assert.assertEquals(order.getTotal(), ord.getTotal());
    }

    @Test
    public void submit_not_found_case() {
        User user = createTestUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        verify(orderRepository, times(0)).save(any());

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        UserOrder ord = response.getBody();
        Assert.assertNull(ord);
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("testUser");
        return user;
    }

    private Cart createTestCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotal(new BigDecimal(200));
        return cart;
    }

    private UserOrder createTestOrder(User user) {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
        userOrder.setTotal(new BigDecimal(200));
        return userOrder;
    }

}
