package main.service;

import main.logic.Patient;
import main.io.*;

import java.util.*;
import java.util.stream.*;

public class PatientService {
    private final Input input = new Input();

    // List operations
    public List<Patient> listOfPatientsWithTheSpecifiedDiagnosis(List<Patient> patients, String diagnosis){
        return patients.stream()
                .filter(patient -> patient.getMedicalDiagnosis().equals(diagnosis))
                .collect(Collectors.toList());
    }

    public List<Patient> listOfPatientsWhoseMedicalRecordNumberIsWithinTheSpecifiedInterval(List<Patient> patients, int minInterval, int maxInterval){
        return patients.stream()
                .filter(patient -> patient.getMedicalRecordNumber() >= minInterval && patient.getMedicalRecordNumber() <= maxInterval)
                .collect(Collectors.toList());
    }

    public List<Patient> quantityAndListOfPatientsWhosePhoneNumberBeginsWithTheSpecifiedDigit(List<Patient> patients, String phoneNumberDigit){
        return patients.stream()
                .filter(patient -> patient.getPhoneNumber().substring(4, 5).equals(phoneNumberDigit))
                .collect(Collectors.toList());
    }

    // Changing the list
    public void addNewPatient(List<Patient> patients){
        patients.add(input.newPatient());
        checkTheCorrectOder(patients);
    }

    public void deletePatientFromList(List<Patient> patients, int id){
        patients.removeIf(patient -> patient.getId() == id);
        checkTheCorrectOder(patients);
    }

    public void checkTheCorrectOder(List<Patient> patients){
        for(int i = 0; i < patients.size(); i++){
            if(patients.get(i).getId() != i + 1){
                patients.get(i).setId(i + 1);
            }
        }
    }

    //Список пацієнтів у порядку спадання кількості візитів у поточному році. Якщо вона
    //однакова - за зростанням номерів медичної картки.
    public List<Patient> listOfPatientsByCurrentYearVisitCount(List<Patient> patients){
        return patients.stream()
                .sorted(Comparator.comparingInt(Patient::getCurrentYearVisitCount).reversed()
                        .thenComparingInt(Patient::getMedicalRecordNumber))
                .collect(Collectors.toList());
    }

    public int findPatientById(List<Patient> patients, int id){
        return patients.stream()
                .filter(patient -> patient.getId() == id)
                .map(Patient::getMedicalRecordNumber)
                .findFirst()
                .orElse(-1);
    }

    // Map operations
    public Map<String, List<Patient>> createMap(List<Patient> patients){
        return patients.stream()
                .collect(Collectors.groupingBy(Patient::getMedicalDiagnosis));
    }

    public Map<String, List<Patient>> mapOfPatientsByDiagnosisWithMedicalRecordNumber(List<Patient> patients){
        return createMap(patients).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .sorted(Comparator.comparingInt(Patient::getMedicalRecordNumber))
                                .collect(Collectors.toList())
                ));
    }

    public Map<String, Patient> mapOfPatientsByDiagnosisWithMaxVisitCount(List<Patient> patients){
        return patients.stream()
                .collect(Collectors.toMap(
                        Patient::getMedicalDiagnosis,
                        patient -> patient,
                        (existing, replacement) -> existing.getCurrentYearVisitCount() > replacement.getCurrentYearVisitCount() ? existing : replacement
                ));
    }
}
