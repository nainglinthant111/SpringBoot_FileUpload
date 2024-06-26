package com.mongotest.MonGo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongotest.MonGo.model.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String> {

}
