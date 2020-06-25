package com.manifesting.fileConvertor.database.tcda.controller;

import com.manifesting.fileConvertor.database.tcda.entity.Item;
import com.manifesting.fileConvertor.database.tcda.entity.TcdaModel;
import com.manifesting.fileConvertor.database.tcda.service.ItemService;
import com.manifesting.fileConvertor.database.tcda.service.TcdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TcdaController {

    @Autowired
    private TcdaService tcdaService;
    @Autowired
    private ItemService itemService;


    @PostMapping("/addOne/tcda/{itemId}")
    public TcdaModel addObject ( @RequestBody TcdaModel tcdaModel, @PathVariable(value="itemId") Long itemId ) {
        Item item = itemService.findOne(itemId);
        tcdaModel.getItems().add(item);
        return tcdaService.saveObject(tcdaModel);

    }

    @PostMapping("/addMany/tcda")
    public List<TcdaModel> addObjects ( @RequestBody List<TcdaModel> list ) {
        return tcdaService.saveObjects(list);
    }

    @GetMapping("/getAll/tcda")
    public List<TcdaModel> getAllObject () {
        return tcdaService.getTcdaModels();
    }


}
