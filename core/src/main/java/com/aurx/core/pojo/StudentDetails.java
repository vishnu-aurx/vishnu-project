package com.aurx.core.pojo;

import java.util.Comparator;
import java.util.List;

/**
 * This is a StudentDetails POJO class.
 */
public class StudentDetails implements Comparator<StudentDetails> {

  /**
   * name - The name.
   */
  private String name;

  /**
   * rollNumber - The rollNumber.
   */
  private String rollNumber;

  /**
   * marks - The list of marks
   */
  private List<Integer> marks;

  /**
   * averageMarks - The averageMarks.
   */
  private int averageMarks;

  /**
   * Default constructor of StudentDetails;
   */
  public StudentDetails() {
  }

  /**
   * This is a parameterized constructor used to set name,rollNumber,marks.
   *
   * @param name       - The name.
   * @param rollNumber - The rollNumber.
   * @param marks      - The marks.
   */
  public StudentDetails(String name, String rollNumber, List<Integer> marks) {
    this.name = name;
    this.rollNumber = rollNumber;
    this.marks = marks;

  }

  /**
   * This method returns the name.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * This method returns the rollNumber.
   *
   * @return - The rollNumber.
   */
  public String getRollNumber() {
    return rollNumber;
  }


  /**
   * This method returns the averageMarks.
   *
   * @return - The averageMarks.
   */
  public int getAverageMarks() {
    return averageMarks;
  }

  /**
   * This method is used to set the name.
   *
   * @param name - The name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * This method is used to set the rollNumber.
   *
   * @param rollNumber - The rollNumber.
   */
  public void setRollNumber(String rollNumber) {
    this.rollNumber = rollNumber;
  }


  /**
   * This method set the averageMarks.
   *
   * @param averageMarks - The averageMarks.
   */
  public void setAverageMarks(int averageMarks) {
    this.averageMarks = averageMarks;
  }

  /**
   * Method to convert class object into string format.
   *
   * @return - string value of object
   */
  @Override
  public String toString() {
    return "StudentDetails{" +
        "name='" + name + '\'' +
        ", rollNumber='" + rollNumber + '\'' +
        ", marks=" + marks +
        ", averageMarks=" + averageMarks +
        '}';
  }

  /**
   * This method is used to set the marks.
   *
   * @param marks - The List of marks.
   */
  public void setMarks(List<Integer> marks) {
    this.marks = marks;
  }

  /**
   * This method returns the List of marks.
   *
   * @return - The List of marks.
   */
  public List<Integer> getMarks() {
    return marks;
  }

  /**
   * Compares two StudentDetails objects based on their average marks in descending order.
   *
   * @param o1 The first StudentDetails object to compare.
   * @param o2 The second StudentDetails object to compare.
   * @return A negative value if o1 should be ordered before o2.
   */
  @Override
  public int compare(StudentDetails o1, StudentDetails o2) {
    return o2.getAverageMarks() - o1.getAverageMarks();
  }
}
