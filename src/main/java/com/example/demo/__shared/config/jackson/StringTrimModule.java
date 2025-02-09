package com.example.demo.__shared.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class StringTrimModule extends SimpleModule {

    public StringTrimModule() {
        addDeserializer(String.class, new StringTrimDeserializer());
    }
}
