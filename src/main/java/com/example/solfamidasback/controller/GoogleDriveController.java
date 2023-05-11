package com.example.solfamidasback.controller;

import com.example.solfamidasback.service.GoogleDriveService;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
public class GoogleDriveController {

    @Autowired
    GoogleDriveService googleDriveService;

    // MainController.java

}
