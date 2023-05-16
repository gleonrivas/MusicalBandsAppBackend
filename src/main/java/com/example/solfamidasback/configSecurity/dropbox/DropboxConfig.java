package com.example.solfamidasback.configSecurity.dropbox;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxClientV2Base;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.LinkMetadata;
import com.dropbox.core.v2.sharing.ShareFolderLaunch;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.example.solfamidasback.model.Users;

public class DropboxConfig {
    private static final String ACCESS_TOKEN = "sl.Beiv7DMTZtxMdE-Hq9wlmZAZpUD-hojRv4BHYCpohPImruvoXN85nZlhc6I_FkHwJ2XW_bSSMH0zcN-FPho9xAd-gjtm17H9vaLX-CeLHvf43Jsm-Wzlb50eQ9b6n0fH1QfZCUhtqLCE";

    public static void main(String args[]) throws FileNotFoundException, DbxException {
        System.out.println("Hi");

        try {

            DbxRequestConfig config;
            config = new DbxRequestConfig("dropbox/Solfamidas");
            DbxClientV2 client;
            client = new DbxClientV2(config, ACCESS_TOKEN);
            FullAccount account;
            DbxUserUsersRequests r1 = client.users();
            account = r1.getCurrentAccount();
            System.out.println(account.getName().getDisplayName());

            // Get files and folder metadata from Dropbox root directory
            ListFolderResult result = client.files().listFolder("/Solfamidas");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    System.out.println(metadata.getPathLower());
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }

        } catch (DbxException ex1) {
            ex1.printStackTrace();
        }

        DbxRequestConfig config = new DbxRequestConfig("dropbox/Solfamidas");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);;
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream("C:\\Users\\orteg\\IdeaProjects\\SolfamidasBackend\\src\\main\\java\\com\\example\\solfamidasback\\configSecurity\\dropbox\\test.txt")) {
           FileMetadata metadata = client.files().uploadBuilder("/Solfamidas/test.txt")
               .uploadAndFinish(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //DbxDownloader<FileMetadata> downloader = client.files().download("/test.txt");
          //  try {
            //   FileOutputStream out = new FileOutputStream("test.txt");
              //downloader.download(out);
              //out.close();
            //} catch (DbxException | IOException ex) {
             //       System.out.println(ex.getMessage());
           // }

    }

    public static void GetCredentials() throws DbxException {
        DbxRequestConfig config;
        config = new DbxRequestConfig("dropbox/Solfamidas");
        DbxClientV2 client;
        client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account;
        DbxUserUsersRequests r1 = client.users();
        account = r1.getCurrentAccount();
    }


    public static String UploadFile(String fileRoute, String email) throws DbxException {

        // obtengo las credenciales
        GetCredentials();
        //genero un codigo propio para el archivo

        DbxRequestConfig config = new DbxRequestConfig("dropbox/Solfamidas");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        String[] filename = fileRoute.split("");
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream(fileRoute)) {
            FileMetadata metadata = client.files().uploadBuilder("/Solfamidas/" + email + "/a.jpg")
                    .uploadAndFinish(in);

            return client.sharing().createSharedLinkWithSettings("/Solfamidas/" + email + "/a.jpg").getUrl().replace("dl=0", "raw=1");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }





}