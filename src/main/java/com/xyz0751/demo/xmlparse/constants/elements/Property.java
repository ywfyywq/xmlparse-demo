package com.xyz0751.demo.xmlparse.constants.elements;

public interface Property extends DatabaseIdProvider, Properties, ObjectFactory, TransactionManager, DataSource {
    String SELF_NAME = "property";

    String NAME = "name";
    String VALUE = "value";
}
