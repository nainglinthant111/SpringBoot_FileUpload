package com.mongotest.MonGo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongotest.MonGo.model.Book;
import com.mongotest.MonGo.repository.BookRepo;
import com.mongotest.MonGo.service.PhotoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BookController {

    @Autowired
    private BookRepo repository;

    @Autowired
    private PhotoService photoService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String AddPost(@RequestBody Book book) {
        repository.save(book);
        log.info("Add new Book id : " + book.getId());
        return "Add book width id : " + book.getId();
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<Book> getBooks() {
        log.info("Find All Book");
        return repository.findAll();
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public Optional<Book> getBook(@PathVariable int id) {
        log.info("Find Book by id : " + id);
        return repository.findById(id);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public String deleteBook(@PathVariable int id) {
        repository.deleteById(id);
        log.info("Delete Book by id : " + id);
        return "book deleted with id : " + id;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        try {
            String photoId = photoService.savePhoto(file);
            return ResponseEntity.ok(photoId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload photo");
        }
    }

    @GetMapping("images/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable String id) {
        // Fetch image data from the service layer based on the provided ID
        byte[] imageData = photoService.getImageDataById(id);

        if (imageData != null) {
            // Return the image data with appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust content type as needed
                    .body(imageData);
        } else {
            // If image data is not found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable String id) {
        // Delete the image from the database
        if (photoService.deleteImageById(id)) {
            log.info("Delete photo is succesfully!");
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
