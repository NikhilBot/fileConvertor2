package com.manifesting.fileConvertor.database.dms.service;

import com.manifesting.fileConvertor.database.dms.entity.DMSModel;
import com.manifesting.fileConvertor.database.dms.repository.DMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DMSService {
    @Autowired
    private DMSRepository dmsRepository;


    public DMSModel saveObject ( DMSModel dmsModel ) {
        return dmsRepository.save(dmsModel);
    }

    public List<DMSModel> saveObjects ( List<DMSModel> list ) {
        return dmsRepository.saveAll(list);
    }


    /**
     * This method returns the
     * function on passing attributeId
     */
    public String getFunctionByAttributeId ( String attributeId ) {
        return dmsRepository.findByAttributeId(attributeId).get_function();
    }


}
