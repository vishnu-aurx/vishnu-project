package com.aurx.core.models;

import static com.aurx.core.constant.ApplicationConstants.STUDENTS_NAME;
import static com.aurx.core.constant.ApplicationConstants.STUDENT_ROLL_NUMBER;
import static com.aurx.core.constant.ApplicationConstants.SUBJECTS_MARKS;
import static org.apache.commons.lang.StringUtils.EMPTY;

import com.aurx.core.pojo.StudentDetails;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StudentDetailsModel fetches the students details.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StudentDetailsModel {

  /**
   * studentsResourceList - The List of students resource.
   */
  @ChildResource(name = "students")
  List<Resource> studentsResourceList;

  /**
   * numberOfStudents - The numberOfStudents.
   */
  @Inject
  @Named("numberofstudent")
  int numberOfStudents;

  /**
   * students - This object is used to store the fetched student details..
   */
  List<StudentDetails> students;

  /**
   * sortedStudentDetailsList - This object is used to store the sorted student details.
   */
  List<StudentDetails> sortedStudentDetailsList;

  /**
   * logger - Logger object.
   */
  private final Logger logger = LoggerFactory.getLogger(StudentDetailsModel.class);

  /**
   * This method is invoked on the object initialization.
   */
  @PostConstruct
  protected void init() {
    logger.info("init method start");
    fetchingStudentDetails();
    sortStudent();
    logger.info("this is student details : {}", students);

  }

  /**
   * This method fetched students' details from studentsResourceList.
   */
  public void fetchingStudentDetails() {
    logger.info("Start of fetchingStudentDetails method");
    students = new ArrayList<>();
    if (studentsResourceList != null) {
      logger.info("studentsResourceList is not null");
      for (Resource eachResource : studentsResourceList) {
        List<Integer> marks = new ArrayList<>();
        ValueMap valueMap = eachResource.getValueMap();
        Iterator<Resource> resourceIterator = eachResource.listChildren();
        while (resourceIterator.hasNext()) {
          Resource markschildResource = resourceIterator.next();
          Iterable<Resource> children = markschildResource.getChildren();
          for (Resource marksResource : children) {
            ValueMap marksValueMap = marksResource.getValueMap();
            String subMarks = marksValueMap.get(SUBJECTS_MARKS, EMPTY);
            if (!subMarks.isEmpty()) {
              marks.add(Integer.parseInt(subMarks));
            }
          }
        }
        StudentDetails studentDetails = new StudentDetails(valueMap.get(STUDENTS_NAME, EMPTY),
            valueMap.get(STUDENT_ROLL_NUMBER, EMPTY), marks);
        students.add(studentDetails);
      }
    }
    calculatingAverageMarks();
    logger.info("End of fetchingStudentDetails with students: {}", students);
  }

  /**
   * This method is used to calculate the  average marks of students.
   */
  private void calculatingAverageMarks() {
    logger.info("Start of calculatingAverageMarks with students : {}", students);
    for (StudentDetails studentMarksDetails : students) {
      List<Integer> studentMarks = studentMarksDetails.getMarks();
      int totalMarks = 0;
      int size = studentMarks.size();
      for (Integer mark : studentMarks) {
        totalMarks += mark;
      }
      if (size <= 0) {
        size = 1;
      }
      int avg = totalMarks / size;
      studentMarksDetails.setAverageMarks(avg);
    }
    logger.info("End of calculatingAverageMarks method");
  }

  /**
   * This method is used to sort the students according to average marks.
   */
  private void sortStudent() {
    logger.info("Start of sortStudent");
    Collections.sort(students, new StudentDetails());
    sortedStudentDetailsList = new ArrayList<>();
    if (numberOfStudents > students.size()) {
      numberOfStudents = students.size();
    }
    for (int i = 0; i < numberOfStudents; i++) {
      sortedStudentDetailsList.add(students.get(i));
    }
  }

  public List<StudentDetails> getStudents() {
    logger.info("this is student details : {}", sortedStudentDetailsList);
    return sortedStudentDetailsList;
  }
}
