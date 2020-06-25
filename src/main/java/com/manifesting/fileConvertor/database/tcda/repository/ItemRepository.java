package com.manifesting.fileConvertor.database.tcda.repository;

import com.manifesting.fileConvertor.database.tcda.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {

}
