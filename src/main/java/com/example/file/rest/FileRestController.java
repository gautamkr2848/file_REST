package com.example.file.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@RestController
@RequestMapping("/file")
public class FileRestController {

    @RequestMapping(
            value = "/download",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public void fileDownload(HttpServletResponse response) throws Exception {
        try {
            URL url = new URL("http://localhost:8080/");
            URLConnection urlCon = url.openConnection();
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream("abc.txt");

            byte[] buffer = new byte[1024];
            int bytesRead = is.read(buffer);
            while (bytesRead > 0) {
                fos.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer);
            }
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(
            value = "/delete",
            method = RequestMethod.DELETE
    )
    public ResponseEntity deleteFile(HttpServletResponse response) throws Exception {
        try
        {
            File file = new File("abc.txt");
            file.delete();
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Deletion successful.");
    }

    @RequestMapping(
            value = "/copy",
            method = RequestMethod.POST
    )
    public ResponseEntity copyFile(HttpServletResponse response) throws Exception {
        File destinationFile = null;
        if (!destinationFile.exists()) {
            destinationFile.createNewFile();
        }
        try {
            File sourceFile = new File("abc.txt");//find source file based on identifer
            if (!sourceFile.exists()) {
                return ResponseEntity.badRequest().body("Source file is not available");
            }
            copyFileUsingStream(sourceFile, destinationFile);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Copy of file successful.");
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
