package com.aurx.core.models;

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

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StudentDetailsModel {

  @ChildResource(name = "students")
  List<Resource> studentsResourceList;
  @Inject
  @Named("numberofstudent")
  int numberOfStudents;
  List<StudentDetails> students;
  List<StudentDetails> studentDetailsList;
  private final Logger logger = LoggerFactory.getLogger(StudentDetailsModel.class);

  @PostConstruct
  protected void init() {
    logger.info("init method start");
    fetchingStudentDetails();
    sortStudent();
    logger.info("this is student details : {}", students);

  }
  public void fetchingStudentDetails() {
    students = new ArrayList<>();
    if (studentsResourceList != null) {
      for (Resource eachResource : studentsResourceList) {
        List<Integer> marks = new ArrayList<>();
        ValueMap valueMap = eachResource.getValueMap();
        Iterator<Resource> resourceIterator = eachResource.listChildren();
        while (resourceIterator.hasNext()) {
          Resource markschildResource = resourceIterator.next();
          Iterable<Resource> children = markschildResource.getChildren();
          for (Resource marksResource : children) {
            ValueMap marksValueMap = marksResource.getValueMap();
            String subMarks = marksValueMap.get("subjectsmarks", "");
            if(!subMarks.isEmpty())
            marks.add(Integer.parseInt(subMarks));
          }
        }
        StudentDetails studentDetails = new StudentDetails(valueMap.get("studentname", ""),
            valueMap.get("studentrollnumber", ""), marks);
        students.add(studentDetails);
      }
    }
    calculatingAverageMarks();
  }

  private void calculatingAverageMarks() {
    for (StudentDetails studentMarksDetails : students) {
      List<Integer> studentMarks = studentMarksDetails.getMarks();
      int totalMarks = 0;
      int size = studentMarks.size();
      for (Integer mark : studentMarks) {
        totalMarks += mark;
      }
      if(size<=0){
        size=1;
      }
      int avg = totalMarks / size;
      studentMarksDetails.setAverageMarks(avg);
    }
  }

  private void sortStudent() {
    Collections.sort(students, new StudentDetails());
    if (numberOfStudents > students.size())
      numberOfStudents = students.size();

      studentDetailsList = new ArrayList<>();
      for (int i = 0; i < numberOfStudents; i++) {
        studentDetailsList.add(students.get(i));
      }
    }
  public List<StudentDetails> getStudents() {
    logger.info("this is student details : {}", studentDetailsList);
    return studentDetailsList;
  }
}
