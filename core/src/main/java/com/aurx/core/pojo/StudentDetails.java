package com.aurx.core.pojo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class StudentDetails implements Comparator<StudentDetails> {
  private String name;
  private String rollNumber;
  private List<Integer> marks;
  private int averageMarks;

  public StudentDetails() {
  }

  public StudentDetails(String name, String rollNumber, List<Integer> marks) {
    this.name = name;
    this.rollNumber = rollNumber;
    this.marks = marks;

  }

  public String getName() {
    return name;
  }

  public String getRollNumber() {
    return rollNumber;
  }


  public int getAverageMarks() {
    return averageMarks;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRollNumber(String rollNumber) {
    this.rollNumber = rollNumber;
  }



  public void setAverageMarks(int averageMarks) {
    this.averageMarks = averageMarks;
  }

  @Override
  public String toString() {
    return "StudentDetails{" +
        "name='" + name + '\'' +
        ", rollNumber='" + rollNumber + '\'' +
        ", marks=" + marks +
        ", averageMarks=" + averageMarks +
        '}';
  }

  public void setMarks(List<Integer> marks) {
    this.marks = marks;
  }

  public List<Integer> getMarks() {
    return marks;
  }


  @Override
  public int compare(StudentDetails o1, StudentDetails o2) {
//    return Integer.compare(o2.getAverageMarks(), o1.getAverageMarks());
      return o2.getAverageMarks()-o1.getAverageMarks();
  }
}
