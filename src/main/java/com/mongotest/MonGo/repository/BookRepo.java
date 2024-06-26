package com.mongotest.MonGo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongotest.MonGo.model.Book;

@Repository
public interface BookRepo extends MongoRepository<Book, Integer> {

}
