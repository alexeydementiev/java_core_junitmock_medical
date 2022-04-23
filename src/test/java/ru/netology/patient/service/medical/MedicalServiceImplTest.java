package ru.netology.patient.service.medical;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertServiceImpl;


class MedicalServiceImplTest {

    public MedicalServiceImpl medicalService;
    public PatientInfoRepository patientInfoRepository;
    public SendAlertServiceImpl sendAlertService;


    @BeforeEach
    void setUp() {
        patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

//        when(geoService.byIp(ipRus)).thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
//        when(geoService.byIp(ipUsa)).thenReturn(new Location("New York", Country.USA, null, 0));
//
//        when(localizationService.locale(Country.RUSSIA)).thenReturn(rusMessage);
//        when(localizationService.locale(Country.USA)).thenReturn(engMessage);
    }

    @Test
    void test_check_blood_pressure_normal_pressure() {
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfo patient = new PatientInfo("123", "Ivan", "Petrov", null, new HealthInfo(new BigDecimal(36.6).setScale(1), bloodPressure));
        when(patientInfoRepository.getById("123")).thenReturn(patient);
        String message = String.format("Warning, patient with id: %s, need help", patient.getId());

        medicalService.checkBloodPressure("123", bloodPressure);

        verify(patientInfoRepository, times(1)).getById("123");
        verify(sendAlertService, times(0)).send(any());
    }

    @Test
    void test_check_blood_pressure_bad_pressure() {
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        BloodPressure bloodPressure140 = new BloodPressure(140, 90);
        PatientInfo patient = new PatientInfo("123", "Ivan", "Petrov", null, new HealthInfo(new BigDecimal(36.6), bloodPressure));
        when(patientInfoRepository.getById("123")).thenReturn(patient);
        String message = String.format("Warning, patient with id: %s, need help", patient.getId());

        medicalService.checkBloodPressure("123", bloodPressure140);

        verify(patientInfoRepository, times(1)).getById("123");
        verify(sendAlertService, times(1)).send(message);
    }

    @Test
    void test_check_temperature_normal_temperature() {
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfo patient = new PatientInfo("123", "Ivan", "Petrov", null, new HealthInfo(new BigDecimal(36.6), bloodPressure));
        when(patientInfoRepository.getById("123")).thenReturn(patient);
        String message = String.format("Warning, patient with id: %s, need help", patient.getId());

        medicalService.checkTemperature("123", new BigDecimal(36.6));

        verify(patientInfoRepository, times(1)).getById("123");
        verify(sendAlertService, times(0)).send(any());
    }

    @Test
    void test_check_temperature_bad_temperature() {
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        PatientInfo patient = new PatientInfo("123", "Ivan", "Petrov", null, new HealthInfo(new BigDecimal(36.6), bloodPressure));
        when(patientInfoRepository.getById("123")).thenReturn(patient);
        String message = String.format("Warning, patient with id: %s, need help", patient.getId());

        medicalService.checkTemperature("123", new BigDecimal(39));

        verify(patientInfoRepository, times(1)).getById("123");
        verify(sendAlertService, times(1)).send(message);

    }

}

