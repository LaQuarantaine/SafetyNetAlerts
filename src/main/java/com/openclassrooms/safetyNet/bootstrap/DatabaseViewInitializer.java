package com.openclassrooms.safetyNet.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Component
@RequiredArgsConstructor
public class DatabaseViewInitializer {

    private final ViewCreationService viewCreationService;

    @EventListener(ApplicationReadyEvent.class)
    public void createViewsAfterStartup() {
        viewCreationService.createOrReplacePersonWithMedicalRecordView();
        viewCreationService.createOrReplaceStationAddressView();
        System.out.println("2 vues SQL créées après démarrage !");
    }
}