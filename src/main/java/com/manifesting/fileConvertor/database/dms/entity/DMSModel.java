package com.manifesting.fileConvertor.database.dms.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DMS")
public class DMSModel {

    @Id
    String attributeId;
    String _function;

    public DMSModel() { }

    public DMSModel(String attributeId , String _function){
        this._function = _function;
        this.attributeId = attributeId;
    }


    public String getAttributeId () {
        return attributeId;
    }

    public void setAttributeId ( String artifactId ) {
        this.attributeId = attributeId;
    }

    public String get_function () {
        return _function;
    }

    public void set_function ( String _function ) {
        this._function = _function;
    }
}
