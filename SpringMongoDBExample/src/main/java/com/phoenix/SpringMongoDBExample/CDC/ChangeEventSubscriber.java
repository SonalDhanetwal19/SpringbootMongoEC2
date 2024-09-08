package com.phoenix.SpringMongoDBExample.CDC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ChangeEventSubscriber implements ApplicationListener {

    @Autowired
    public ChangeStreamProcessor changeStreamProcessor;

    @Autowired
    public ChangeEventSubscriber(ChangeStreamProcessor changeStreamProcessor) {
        this.changeStreamProcessor = changeStreamProcessor;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        changeStreamProcessor.subscribeToChangeEventMethods();
    }
}
