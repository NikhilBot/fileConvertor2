package com.manifesting.fileConvertor.database.tcda.service;

import com.manifesting.fileConvertor.database.tcda.entity.Item;
import com.manifesting.fileConvertor.database.tcda.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;


    public Item saveObject ( Item item ) {
        return itemRepository.save(item);
    }

    public List<Item> saveObjects ( List<Item> list ) {
        return itemRepository.saveAll(list);
    }

    public List<Item> getItems () {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    public void delete(Item item) {
        itemRepository.delete(item);
    }
}
