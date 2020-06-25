package com.manifesting.fileConvertor.database.tcda.service;

import com.manifesting.fileConvertor.database.tcda.entity.TcdaModel;
import com.manifesting.fileConvertor.database.tcda.repository.TcdaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TcdaService {
    @Autowired
    private TcdaRepository tcdaRepository;


    public TcdaModel saveObject ( TcdaModel tcdaModel ) {
        return tcdaRepository.save(tcdaModel);
    }

    public List<TcdaModel> saveObjects ( List<TcdaModel> list ) {
        return tcdaRepository.saveAll(list);
    }

    public List<TcdaModel> getTcdaModels () {
        return tcdaRepository.findAll();
    }


}
