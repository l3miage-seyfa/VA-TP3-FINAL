package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {

    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    @Test
    void getCandidateByIdThrowException(){

        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, ()->candidateComponent.getCandidatById(1L));
    }


    @Test
    void getCandidateByIdDontThrow(){

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .email("email")
                .build();

        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));

        assertDoesNotThrow(()->candidateComponent.getCandidatById(1L));
    }


}