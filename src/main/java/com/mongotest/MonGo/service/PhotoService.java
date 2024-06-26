package com.mongotest.MonGo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongotest.MonGo.model.Photo;
import com.mongotest.MonGo.repository.PhotoRepository;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public String savePhoto(MultipartFile file) throws IOException {
        byte[] imageData = file.getBytes();

        // Create a new photo document
        Photo photo = new Photo();
        photo.setImageData(imageData);
        photo.setFileName(file.getOriginalFilename());
        photo.setFileType(file.getContentType());

        // Save the photo to MongoDB
        Photo savedPhoto = photoRepository.save(photo);

        // Return the ID of the saved photo
        return savedPhoto.getId();
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public byte[] getImageDataById(String id) {
        // Retrieve image data from the repository based on the provided ID
        Optional<Photo> imageOptional = photoRepository.findById(id);
        if (imageOptional.isPresent()) {
            Photo image = imageOptional.get();
            return image.getImageData();
        } else {
            return null;
        }
    }

    public boolean deleteImageById(String id) {
        // Delete the image from the repository
        Optional<Photo> imageOptional = photoRepository.findById(id);
        if (imageOptional.isPresent()) {
            photoRepository.deleteById(id);
            return true;
        } else {
            return false; // Image not found
        }
    }
}
