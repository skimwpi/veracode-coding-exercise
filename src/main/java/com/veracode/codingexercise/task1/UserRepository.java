package com.veracode.codingexercise.task1;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    List<User> findByLastName(@Param("lastName") String lastName);
    List<User> findByFirstName(@Param("firstName") String firstName);
}
