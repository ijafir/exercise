package com.exercise.web;

import com.exercise.domain.Person;
import com.exercise.service.PersonService;
import com.exercise.service.exception.PersonAlreadyExistsException;
import com.exercise.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RestController("person")
@RequestMapping("/api/v1/")
public class PersonController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    private static final String REQUIRED_FIELDS_MSG = "Id, First Name, Last Name, DOB is required!";
    private PersonService personService;

    @Inject
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Create a person
     *
     * @param person
     * @param ucBuilder
     * @return responseEntity: created person
     * @throws
     */
    @RequestMapping(value = "persons", method = RequestMethod.POST)
    public ResponseEntity<?> createPerson(@RequestBody Person person, UriComponentsBuilder ucBuilder) { ;
        LOGGER.debug(">>> Creating person with id: " + person.getId());
        if(!validatePerson(person))
            return new ResponseEntity<>(new CustomErrorType(REQUIRED_FIELDS_MSG), HttpStatus.BAD_REQUEST);

        if (personService.isPersonExist(person)) {
            LOGGER.debug("A person with name " + person.getId() + "exist.");
            return new ResponseEntity(new CustomErrorType("Unable to create. A Person with id " +
                    person.getId() + " already exist."),HttpStatus.CONFLICT);
        }
        if(personService.findByLastNameAndFirstNameAndDobAndEmail(person.getLastName(), person.getFirstName(),person.getDob(), person.getEmail()) != null)
            return new ResponseEntity<>(new CustomErrorType("A Contact already exists"), HttpStatus.BAD_REQUEST);

        personService.savePerson(person);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("person/{id}").buildAndExpand(person.getId()).toUri());
        return new ResponseEntity<>(person, headers, HttpStatus.CREATED);
    }

    /**
     * Returns a person by id
     *
     * @param id
     * @return responseEntity: fetched person
     * @throws
     */
    @RequestMapping(value = "persons/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPerson(@PathVariable("id") String id) {
        LOGGER.debug("Fetching person with id: " + id);
        Person person = personService.findPersonById(id);
        if (person == null) {
            LOGGER.debug("Person with id: " + id + ", not found!");
            return new ResponseEntity(new CustomErrorType("Person with id: " + id + ", not found!"),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    /**
     * Return all person s
     *
     * @param
     * @return responseEntity: List of Persons
     * @throws
     */
    @RequestMapping(value = "persons", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> listAllPersons() {
        LOGGER.debug("Received request to list all persons");
        List<Person> persons = personService.findAllPersons();
        if (persons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    /**
     * Update a person
     *
     * @param id
     * @param person : updated fields
     * @return responseEntity: person updated
     * @throws ParseException
     */
    @RequestMapping(value = "persons/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePersonFromDB(@PathVariable("id") String id,
                                                   @RequestBody Person person)throws ParseException
    {
        if(!validatePerson(person))
            return new ResponseEntity<>(new CustomErrorType(REQUIRED_FIELDS_MSG), HttpStatus.BAD_REQUEST);

        LOGGER.debug(">>> Updating person with id: " + id);
        Person currentPerson = personService.findPersonById(id);

        if (currentPerson == null) {
            LOGGER.debug("<<< Person with id: " + id + ", not found!");
            return new ResponseEntity<>(new CustomErrorType("Cannot Update Person with id: " + id + ", not found!"), HttpStatus.NOT_FOUND);

        }

        currentPerson.setLastName(person.getLastName());
        currentPerson.setFirstName(person.getFirstName());
        currentPerson.setDob(new SimpleDateFormat("yyyy-MM-dd").format(person.getDob()));
        currentPerson.setFirstName(person.getFirstName());
        personService.updatePerson(currentPerson);
        return new ResponseEntity<>(currentPerson, HttpStatus.OK);
    }

    /**
     * Delete a person
     *
     * @param id
     * @return responseEntity: No Content
     */
    @RequestMapping(value = "persons/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePersonFromDB(@PathVariable("id") String id) {
        LOGGER.debug("Fetching & Deleting Person with id: " + id + " is successfully removed from database!");

        Person person = personService.findPersonById(id);
        if (person == null) {
            LOGGER.debug("Unable to delete. Person with id: " + id + ", not found!");
            return new ResponseEntity<>(new CustomErrorType("Unable to delete. Person with id: " + id + ", not found!"), HttpStatus.NOT_FOUND);
        }
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /* Delete all persons */
    /**
     * Delete All Persons
     *
     * @param
     * @return responseEntity: no content
     * @throws
     */
    @RequestMapping(value = "persons", method = RequestMethod.DELETE)
    public ResponseEntity<Person> deleteAllPersons() {
        personService.deleteAllPersons();
        LOGGER.debug("Removed all persons from database!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlePersonAlreadyExistsException(PersonAlreadyExistsException exception) {
        return exception.getMessage();
    }

    private boolean validatePerson(Person p){
        if(StringUtils.isEmpty(p.getId()) || StringUtils.isEmpty(p.getFirstName()) ||StringUtils.isEmpty(p.getLastName())
            ||StringUtils.isEmpty(p.getDob()))
                return false;
        return true;
    }

}