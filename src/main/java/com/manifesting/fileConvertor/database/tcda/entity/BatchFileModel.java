package com.manifesting.fileConvertor.database.tcda.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BatchFile")
public class BatchFileModel {

        @Id
        String wareHouseLocationId;
        String amazonTechnicalName;
        String carrierInternalId;
        String currencyCode;
        String name;
        String city;
        String zip;
        String countryCode;
        String countryName;


}
