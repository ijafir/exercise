package com.exercise.service;

import com.exercise.domain.Person;

import java.util.Date;
import java.util.List;

public interface PersonService
{

    boolean isPersonExist(Person person);

    Person savePerson(Person person);

    Person findPersonById(String id);

    List<Person> findAllPersons();

    Person updatePerson(Person person);

    void deletePerson(String id);

    void deleteAllPersons();

    Person findByLastNameAndFirstNameAndDobAndEmail(String lastName, String firstName, Date dob, String email);
}