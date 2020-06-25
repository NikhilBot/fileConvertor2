package com.manifesting.fileConvertor.UtilClasses;

import com.manifesting.fileConvertor.database.tcda.entity.Item;
import com.manifesting.fileConvertor.database.tcda.repository.BatchFileRepository;
import com.manifesting.fileConvertor.database.tcda.repository.TcdaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FunctionUtil implements CommandLineRunner {


    @Autowired
    TcdaRepository tcdaRepository;
    @Autowired
    BatchFileRepository batchFileRepository;

    public String getCity ( String id ) {
        return tcdaRepository.findById(id).get().getCity();
    }

    public String getName ( String id ) {
        return tcdaRepository.findById(id).get().getName();
    }

    public String getZip ( String id ) {
        return tcdaRepository.findById(id).get().getZip();
    }

    public String getCountryName ( String id ) {
        return tcdaRepository.findById(id).get().getCountryName();
    }

    public String getCountryCode ( String id ) {
        return tcdaRepository.findById(id).get().getCountryCode();
    }

    public String getContact ( String id ) {
        return tcdaRepository.findById(id).get().getContactPhone();
    }

    public String getLength ( String id ) {
        return tcdaRepository.findById(id).get().getLength();
    }

    public String getHeight ( String id ) {
        return tcdaRepository.findById(id).get().getHeight();
    }

    public String findAllTrackId ( String id ) {
        return tcdaRepository.findById(id).get().getTrackingId();
    }

    public List<?> findAll () {
        return tcdaRepository.findAllTrackingId();
    }

    public List<Item> getItems ( String id ) {
        return tcdaRepository.findById(id).get().getItems();
    }

    public List<String> getAllPFTrackingId () {
        return tcdaRepository.findAllTrackingId();
    }

    public List<String> getAllBFTrackingId () {
        return batchFileRepository.findAllId();
    }

    public ArrayList<Long> getItemIds ( String id ) {
        List<Item> items = tcdaRepository.findById(id).get().getItems();
        List<Long> result = new ArrayList<>();
        for (Item item : items
        ) {
            result.add(item.getId());
        }
        return (ArrayList<Long>) result;
    }

    public String getItemtitle ( String tcdaId , long itemId ) {
        return tcdaRepository.getItemTitle(tcdaId , itemId);
    }

    public String getItemQuantity ( String tcdaId , long itemId ) {
        return tcdaRepository.getItemQuantity(tcdaId , itemId);
    }

    public String getWareHouseLocationId ( String id ) {
        return batchFileRepository.findById(id).get().getWareHouseLocationId();
    }

    public String getAmazonTechnicalName ( String id ) {
        return batchFileRepository.findById(id).get().getAmazonTechnicalName();
    }

    public String getCarrierInternalId ( String id ) {
        return batchFileRepository.findById(id).get().getCarrierInternalId();
    }

    public String getCurrencyCode ( String id ) {
        return batchFileRepository.findById(id).get().getCurrencyCode();
    }

    public String getBatchFileName ( String id ) {
        return batchFileRepository.findById(id).get().getName();
    }

    public String getBatchFileCity ( String id ) {
        return batchFileRepository.findById(id).get().getCity();
    }

    public String getBatchFileZip ( String id ) {
        return batchFileRepository.findById(id).get().getZip();
    }

    public String getBatchFileCountryCode ( String id ) {
        return batchFileRepository.findById(id).get().getCountryCode();
    }

    public String getBatchFileCountryName ( String id ) {
        return batchFileRepository.findById(id).get().getCountryName();
    }


    @Override
    public void run ( String... args ) throws Exception {

    }


}
