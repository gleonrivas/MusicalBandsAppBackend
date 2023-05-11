package com.example.solfamidasback.configSecurity.driveCredentials;
//package com.jrp.googleapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class GoogleDriveBasic {
    private static final FileDataStoreFactory DATA_STORE_FACTORY;

    static {
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(new java.io.File("C:\\Users\\orteg\\IdeaProjects\\SolfamidasBackend\\src\\main\\java\\com\\example\\solfamidasback\\configSecurity\\driveCredentials\\dataStore"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String redirectURI;
    HttpTransport httpTransport;
    JsonFactory jsonFactory;
    GoogleAuthorizationCodeFlow flow;
    Drive service;
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();;



    public GoogleDriveBasic(){
        this.redirectURI="http://localhost:8080";
        httpTransport = new NetHttpTransport();


        try {
            InputStream in = new FileInputStream("C:\\Users\\orteg\\IdeaProjects\\SolfamidasBackend\\src\\main\\java\\com\\example\\solfamidasback\\configSecurity\\driveCredentials\\credentials.json");

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
             flow = new GoogleAuthorizationCodeFlow.Builder(
                     new NetHttpTransport(), GsonFactory.getDefaultInstance(),
                     "399858254533-jrig7nlvulc450ct1obpu0adgntcnqij.apps.googleusercontent.com", "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",
                     Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(
                    DATA_STORE_FACTORY).setAccessType("offline").build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String getURL(){
        String url = flow.newAuthorizationUrl().setRedirectUri(redirectURI).build();
        return url;
    }



    public void setCode(String code) throws IOException{


        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        //Create a new authorized API client
        service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
    }


    public String uploadTextFile(String filePath, String title) throws IOException{
        setCode("AIzaSyBMXl5cYfnpJWoN8rxo2NpgKy4QjeRfl40");
        File body = new File();
        body.setName(title);
        body.setDescription("Solfamidas");
        body.setMimeType("image/png");
        java.io.File fileContent = new java.io.File(filePath);
        FileContent mediaContent = new FileContent("image/png", fileContent);
        File file = service.files().create(body, mediaContent).execute();
        return file.getId();
    }


    public String downloadTextFile(File file) throws IOException{
        GenericUrl url = new GenericUrl(file.getWebViewLink());
        HttpResponse response = service.getRequestFactory().buildGetRequest(url).execute();
        try {
            return new Scanner(response.getContent()).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }


    public String downloadTextFile(String fileID) throws IOException{
        File file=service.files().get(fileID).execute();
        return downloadTextFile(file);
    }


    public List<File> retrieveAllFiles() throws IOException {
        List<File> result = new ArrayList<File>();
        Files.List request = null;

        request = service.files().list();


        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getFiles());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        return result;
    }

}