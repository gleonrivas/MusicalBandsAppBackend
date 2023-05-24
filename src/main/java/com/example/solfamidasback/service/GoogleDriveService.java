package com.example.solfamidasback.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.Arrays;


@Service
public class GoogleDriveService {



    private static final String APPLICATION_NAME = "Solfamidas";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String ACCESS_TOKEN = "tu_access_token";
    private static final String FILE_PATH = "/ruta/a/tu/archivo/archivo.txt";
    private static final String FILE_NAME = "archivo.txt";
    private static final String MIME_TYPE = "text/plain";

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        // Carga las credenciales desde el archivo JSON
        Credential credential = GoogleCredential.fromStream(GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/drive"));

        // Si tienes un access token, puedes establecerlo en las credenciales
        if (ACCESS_TOKEN != null) {
            credential.setAccessToken(ACCESS_TOKEN);
        }

        // Crea un objeto Drive autenticado con las credenciales cargadas
        Drive driveService = new Drive.Builder(credential.getTransport(), credential.getJsonFactory(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Crea un objeto File con la información del archivo a subir
        java.io.File fileContent = new java.io.File(FILE_PATH);
        File fileMetadata = new File();
        fileMetadata.setName(FILE_NAME);
        fileMetadata.setMimeType(MIME_TYPE);

        // Crea un objeto FileContent con el contenido del archivo a subir
        FileContent mediaContent = new FileContent(MIME_TYPE, fileContent);

        // Sube el archivo a Google Drive y obtiene el ID del archivo creado
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();
        System.out.println("Archivo subido con éxito. ID: " + uploadedFile.getId());
    }


    public static String uploadBasic() throws IOException {
        // Load pre-authorized user credentials from the environment.
        // TODO(developer) - See https://developers.google.com/identity for
        // guides on implementing OAuth2 for your application.
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                credentials);

        // Build a new authorized API client service.
        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("Solfamidas")
                .build();
        // Upload file photo.jpg on drive.
        File fileMetadata = new File();
        fileMetadata.setName("a.jpeg");
        // File's content.
        java.io.File filePath = new java.io.File("../src/main/java/com/example/solfamidasback/configSecurity/driveCredentials/a.png");
        // Specify media type and file-path for file.
        FileContent mediaContent = new FileContent("image/png", filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }


}

