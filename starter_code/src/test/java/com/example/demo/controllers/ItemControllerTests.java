package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_item_by_id_happy_path() {
      Item item = createTestItem(1L);
      when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

      final ResponseEntity<Item> response = itemController.getItemById(item.getId());

      Assert.assertNotNull(response);
      Assert.assertEquals(200, response.getStatusCodeValue());

      Item it = response.getBody();
      Assert.assertNotNull(it);
      Assert.assertEquals(item, it);
    }

    @Test
    public void get_item_by_id_not_found_case() {
        Item item = createTestItem(1L);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        final ResponseEntity<Item> response = itemController.getItemById(item.getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Item it = response.getBody();
        Assert.assertNull(it);
    }

    @Test
    public void get_item_by_name_happy_path() {
        Item item = createTestItem(1L);
        when(itemRepository.findByName(item.getName())).thenReturn(Arrays.asList(item));

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        Assert.assertNotNull(itemList);
        Assert.assertFalse(itemList.isEmpty());
        Assert.assertEquals(item, itemList.get(0));
    }

    @Test
    public void get_item_by_name_not_found_case() {
        Item item = createTestItem(1L);
        when(itemRepository.findByName(item.getName())).thenReturn(new ArrayList<>());

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        Assert.assertNull(itemList);
    }

    @Test
    public void get_items_happy_path() {
        Item item = createTestItem(1L);
        Item item2 = createTestItem(2L);
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item, item2));

        final ResponseEntity<List<Item>> response = itemController.getItems();

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        Assert.assertNotNull(itemList);
        Assert.assertFalse(itemList.isEmpty());
        Assert.assertEquals(2, itemList.size());
    }

    private Item createTestItem(Long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("testItem");
        item.setPrice(new BigDecimal(200));
        item.setDescription("a test item");
        return item;
    }
}
