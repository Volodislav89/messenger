package com.spring.messenger.service;

import com.spring.messenger.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorage {
    private final Path rootDir = Paths.get("upload");

    public FileStorage() {
//        try {
//            if (!Files.exists(rootDir)){
//                Files.createDirectory(rootDir);
//            } else {
//                System.out.println("Directory already exists");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public Path saveFile(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String name = multipartFile.getOriginalFilename();
            Files.copy(inputStream,
                    rootDir.resolve(name),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return rootDir.resolve(multipartFile.getOriginalFilename());
    }

    public Resource getFile(String filename) {
        Path file = rootDir.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("No such file");
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("No such file");
        }
    }
}
