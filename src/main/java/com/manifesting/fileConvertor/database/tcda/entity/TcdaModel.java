package com.manifesting.fileConvertor.database.tcda.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TCDA")
public class TcdaModel {

        @Id
        String trackingId;
        String marketPlaceID;
        String scheduledDeliveryDate;
        String estimatedArrivalDate;
        String length;
        String height;
        String width;
        String name;
        String city;
        String zip;
        String countryCode;
        String countryName;
        String contactPhone;

        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinTable(name = "tcda_item",
                joinColumns = { @JoinColumn(name = "tcdaId")},
                inverseJoinColumns = { @JoinColumn (name = "itemId") } )

        private List<Item> items = new ArrayList<>();



}
