package com.manifesting.fileConvertor.database.tcda.repository;

import com.manifesting.fileConvertor.database.tcda.entity.BatchFileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchFileRepository extends JpaRepository<BatchFileModel,String > {
    @Query(value = "SELECT ware_house_location_id FROM batch_file" , nativeQuery = true)
    List<String> findAllId ();
}
