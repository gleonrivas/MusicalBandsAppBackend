package com.example.solfamidasback.configSecurity.dropbox;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.v2.users.FullAccount;
import com.itextpdf.text.pdf.codec.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DropboxConfig {
    private static final String ACCESS_TOKEN = "sl.BfA0poK_dZydA7LJyhyNCueutBezrlC8Ob1EQ8Y8-JfHx8GleTupD6ZDJu9JOKCdUx1tc1AvFekM-bnEEmbQQFFv-Pb3sKqcgPlvq-MrUPknbthtY9d_gvTKHnP0VKddOOvfHGvgagdM";


    public static void GetCredentials() throws DbxException {
        DbxRequestConfig config;
        config = new DbxRequestConfig("dropbox/Solfamidas");
        DbxClientV2 client;
        client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account;
        DbxUserUsersRequests r1 = client.users();
        account = r1.getCurrentAccount();
    }


    public static String UploadFile(String fileRoute, String email, String fakepath) throws DbxException {

        // obtengo las credenciales
        GetCredentials();
        //genero un codigo propio para el archivo

        DbxRequestConfig config = new DbxRequestConfig("dropbox/Solfamidas");
        File file = new File(fileRoute);
        file.getName().toString();
        String fileAbsolute = file.getAbsoluteFile().toString().split("data")[0].concat(fakepath.concat("."+file.getAbsoluteFile().toString().split(";")[0].split("image")[1].split("\\\\")[1]));
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        try (InputStream in = new FileInputStream(fileAbsolute)) {
            FileMetadata metadata = client.files().uploadBuilder("/Solfamidas/" + email+ "/" + fakepath.concat("."+file.getAbsoluteFile().toString().split(";")[0].split("image")[1].split("\\\\")[1]))
                    .uploadAndFinish(in);

            return client.sharing().createSharedLinkWithSettings("/Solfamidas/" + email+ "/" + fakepath.concat("."+file.getAbsoluteFile().toString().split(";")[0].split("image")[1].split("\\\\")[1])).getUrl().replace("dl=0", "raw=1");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }





}