package com.exercise.service;

import com.exercise.domain.Person;
import com.exercise.domain.PersonRepository;
import com.exercise.service.exception.PersonAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Validated
@Transactional
public class PersonServiceBean implements PersonService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceBean.class);

    private PersonRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public PersonServiceBean(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isPersonExist(Person person) {
        return findPersonById(person.getId()) != null;
    }

    @Override
    public Person savePerson(Person person) {
        LOGGER.debug("Save {}", person);
        Person existing = repository.findById(person.getId());
        if (existing != null) {
            throw new PersonAlreadyExistsException(
                    String.format("There already exists a person with id = %s", person.getId()));
        }
        return repository.save(person);
    }

    @Override
    public Person findPersonById(String id) {
        LOGGER.debug("Search person by id: " + id);
        return repository.findById(id);
    }

    @Override
    public List<Person> findAllPersons() {
        LOGGER.debug("Retrieve the list of all persons!");
        return repository.findAll();
    }

    @Override
    public Person updatePerson(Person person) {
        LOGGER.debug("Person with id: " + person.getId() + " updated! ");
        if (!entityManager.contains(person))
            person = entityManager.merge(person);
        return person;
    }

    @Override
    public void deletePerson(String id) {
        LOGGER.debug("Person by id: " + id + " Deleted!");
        Person t = repository.findById(id);
        repository.delete(t);
    }

    @Override
    public void deleteAllPersons() {
        LOGGER.debug("The list all persons deleted!");
        repository.deleteAll();
    }
}