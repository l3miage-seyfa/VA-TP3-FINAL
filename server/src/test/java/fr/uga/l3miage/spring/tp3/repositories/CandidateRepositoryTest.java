package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @BeforeEach
    public void clearAll(){
        candidateRepository.deleteAll();
        candidateEvaluationGridRepository.deleteAll();
        testCenterRepository.deleteAll();
    }


    //Set<CandidateEntity> findAllByTestCenterEntityCode(TestCenterCode code);
    @Test
    void testRequestFindAllByTestCenterEntityCode() {
        //Given
        TestCenterEntity testCenterEntityGRE = TestCenterEntity
                .builder()
                .code(TestCenterCode.GRE)
                .build();
        testCenterRepository.save(testCenterEntityGRE);

        TestCenterEntity testCenterEntityPAR = TestCenterEntity
                .builder()
                .code(TestCenterCode.PAR)
                .build();
        testCenterRepository.save(testCenterEntityPAR);

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .email("gre@test.com")
                .testCenterEntity(testCenterEntityGRE)
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .email("par@test.com")
                .testCenterEntity(testCenterEntityPAR)
                .build();


        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        //When
        Set<CandidateEntity> candidateEntityResponse = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.GRE);

        //Then
        assertThat(candidateEntityResponse).hasSize(1);
        assertThat(candidateEntityResponse.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(TestCenterCode.GRE);



    }


    //Set<CandidateEntity> findAllByCandidateEvaluationGridEntitiesGradeLessThan(double grade);
    @Test
    void testFindAllByCandidateEvaluationGridEntitiesGradeLessThan(){
        //given
        CandidateEntity candidateEntity = CandidateEntity.builder()
                .firstname("test")
                .email("test@gmail.com")
                .hasExtraTime(false)
                .build();

        candidateRepository.save(candidateEntity);

        CandidateEvaluationGridEntity candidateEvaluationGridEntity = CandidateEvaluationGridEntity.builder()
                .grade(5)
                .candidateEntity(candidateEntity)
                .build();


        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity);

        candidateEntity.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity));
        candidateRepository.save(candidateEntity);



        //when
        Set<CandidateEntity> candidateEntities = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(10);
        Set<CandidateEntity> equalCandidateEntities = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5);
        Set<CandidateEntity> lessCandidateEntities = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(4);
        //then
        assertThat(candidateEntities).hasSize(1);
        assertThat(equalCandidateEntities).hasSize(0);
        assertThat(lessCandidateEntities).hasSize(0);
    }

    //Set<CandidateEntity> findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate localDate);
    @Test
    void testFindAllByHasExtraTimeFalseAndBirthDateBefore(){
        //given
        CandidateEntity candidateEntity1 = CandidateEntity.builder()
                .firstname("test3a")
                .email("test3a@gmail.com")
                .hasExtraTime(false)
                .birthDate(LocalDate.of(1998,1,1))
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity.builder()
                .firstname("test3b")
                .email("test3b@gmail.com")
                .hasExtraTime(true)
                .birthDate(LocalDate.of(1997,1,1))
                .build();

        CandidateEntity candidateEntity3 = CandidateEntity.builder()
                .firstname("test3c")
                .email("test3c@gmail.com")
                .hasExtraTime(true)
                .birthDate(LocalDate.of(2001,1,1))
                .build();

        CandidateEntity candidateEntity4 = CandidateEntity.builder()
                .firstname("test3d")
                .email("test3d@gmail.com")
                .hasExtraTime(false)
                .birthDate(LocalDate.of(2004,1,1))
                .build();

        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);
        candidateRepository.save(candidateEntity3);
        candidateRepository.save(candidateEntity4);
        //when
        //Ext:false , <2000
        Set<CandidateEntity> candidateEntities1 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2000,1,1));
        //Ext:false , <2003
        Set<CandidateEntity> candidateEntities2 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2003,1,1));
        //Ext:false , <2005
        Set<CandidateEntity> candidateEntities3 = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2005,1,1));
        //then
        assertThat(candidateEntities1).hasSize( 1);
        assertThat(candidateEntities2).hasSize( 1);
        assertThat(candidateEntities3).hasSize( 2);
    }
}