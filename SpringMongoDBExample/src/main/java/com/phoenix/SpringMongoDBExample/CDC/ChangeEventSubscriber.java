//package com.phoenix.SpringMongoDBExample.CDC;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class ChangeEventSubscriber implements ApplicationListener {
//
//
//    public final ChangeStreamProcessorNew changeStreamProcessorNew;
//
//    @Autowired
//    public ChangeEventSubscriber(ChangeStreamProcessorNew changeStreamProcessorNew) {
//        this.changeStreamProcessorNew = changeStreamProcessorNew;
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationEvent event) {
//        System.out.println("inside onApplicationEvent");
//        changeStreamProcessorNew.subscribeToChangeEventMethods();
//    }
//}
