package com.exercise.repository;

import com.exercise.domain.Person;
import com.exercise.domain.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonRepositoryTest
{

    @Inject
    private PersonRepository repository;
    @Inject
    private TestEntityManager entityManager;

    @Test
    public void findByLastName_CorrectString_Success() {
        entityManager.merge(new Person("1", "testLastName", "testFirstName", new Date(),  "test@test.com"));
        List<Person> list = this.repository.findByLastName("testLastName");
        list.stream().forEach(x -> System.out.println(x.getLastName()));
        assertEquals(list.size(), 1);
    }

    @Test
    public void findById_CorrectString_Success() {
        entityManager.merge(new Person("1", "testLastName", "testFirstName", new Date(),  "test@test.com"));
        Person p = this.repository.findById("1");
        assertEquals(p.getId(), "1");
    }
}
