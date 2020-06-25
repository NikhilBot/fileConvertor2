package com.manifesting.fileConvertor.database.dms.controller;

import com.manifesting.fileConvertor.database.dms.entity.DMSModel;
import com.manifesting.fileConvertor.database.dms.service.DMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DMSController {

    @Autowired
    private DMSService dmsService;

    @PostMapping("/addOne/dms")
    public DMSModel addObject ( @RequestBody DMSModel dmsModel ) {
        return dmsService.saveObject(dmsModel);
    }

    @PostMapping("/addMany/dms")
    public List<DMSModel> addObjects ( @RequestBody List<DMSModel> list ) {
        return dmsService.saveObjects(list);
    }

    @GetMapping("/getDMS/{id}")
    public String getFunction( @PathVariable String id ) {
        return dmsService.getFunctionByAttributeId(id);
    }
}
