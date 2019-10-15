package com.exercise.service;

import com.exercise.domain.Person;
import com.exercise.domain.PersonRepository;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonServiceTest
{

    @Inject
    private PersonService personService;
    @Inject
    private PersonRepository personRepository;
    @Inject
    private TestEntityManager testEntityManager;

    @Test
    public void contains_NotValidId_Success() throws ParseException
    {
        testEntityManager.merge(new Person("1", "testLastName", "testFirstName",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com"));
        testEntityManager.merge(new Person("2", "testLastName2", "testFirstName2",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test2@test.com"));
        Person isContains = personService.findPersonById("3");
        assertEquals(null, isContains);
    }

    @Test
    public void contains_ValidId_Success() throws ParseException {
        Person object = new Person("1", "testLastName", "testFirstName",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");
        testEntityManager.merge(object);
        boolean isContains = personService.isPersonExist(object);
        assertEquals(true, isContains);
    }

    @Test
    public void findAll_Success() throws ParseException {
        Person p1 = new Person("1", "testLastName", "testFirstName",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");
        Person p2 = new Person("2", "testLastName2", "testFirstName2",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");
        testEntityManager.merge(p1);
        testEntityManager.merge(p2);
        List<Person> list = personService.findAllPersons();
        List<Person> repositoryList = personRepository.findAll();
        assertArrayEquals(Arrays.array(repositoryList), Arrays.array(list));
    }

    @Test
    public void findAll_SuccessEmptyList() {
        List<Person> list = personService.findAllPersons();
        assertEquals(true, list.isEmpty());
    }


    @Test
    public void findOne_NotValidId_Success() throws ParseException{
        Person p = new Person("1", "testLastName", "testFirstName",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");
        testEntityManager.merge(p);
        String objectId = "100";
        Person repositoryObject = personService.findPersonById(objectId);
        assertNull(repositoryObject);
        assertNotEquals(p, repositoryObject);
    }
}
