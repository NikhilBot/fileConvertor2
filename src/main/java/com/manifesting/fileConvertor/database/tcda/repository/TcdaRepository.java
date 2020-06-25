package com.manifesting.fileConvertor.database.tcda.repository;

import com.manifesting.fileConvertor.database.tcda.entity.TcdaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TcdaRepository extends JpaRepository<TcdaModel,String> {

    @Query(value = "SELECT tracking_id FROM tcda" , nativeQuery = true)
    List<String> findAllTrackingId();


    @Query(value = "select it.item_title from item it,tcda_item ti,tcda t where t.tracking_id = :tracking_id and it.id = :id and t.tracking_id = ti.tcda_id and ti.item_id = it.id",nativeQuery = true)
    String getItemTitle( @Param("tracking_id") String tracking_id , @Param("id") Long id);

    @Query(value = "select it.item_quantity from item it,tcda_item ti,tcda t where t.tracking_id = :tracking_id and it.id = :id and t.tracking_id = ti.tcda_id and ti.item_id = it.id",nativeQuery = true)
    String getItemQuantity( @Param("tracking_id") String tracking_id , @Param("id") Long id);

}
