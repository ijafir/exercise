package com.exercise.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByLastName(String lastName);
    Person findById(String id);
    Person findByLastNameAndFirstNameAndDobAndEmail(String lastName, String firstName, Date dob, String email);
}