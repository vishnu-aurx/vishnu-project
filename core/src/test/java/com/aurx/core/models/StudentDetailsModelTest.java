package com.aurx.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class StudentDetailsModelTest {

  private final AemContext context = new AemContext();

  private StudentDetailsModel studentDetailsModel;

  @Test
  void testGetStudents() {
    context.load(true).json("/com/aurx/core/models/StudentDetailsModel/StudentDetails.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    studentDetailsModel = context.request().adaptTo(StudentDetailsModel.class);
    assertNotNull(studentDetailsModel);
    assertNotNull(studentDetailsModel.getStudents());
    assertEquals(2, studentDetailsModel.getStudents().size());
  }

  @Test
  void testGetStudentsWithNumberOfStudents() {
    context.load(true).json("/com/aurx/core/models/StudentDetailsModel/StudentDetailsTest.json",
        "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    studentDetailsModel = context.request().adaptTo(StudentDetailsModel.class);
    assertNotNull(studentDetailsModel);
    assertNotNull(studentDetailsModel.getStudents());
    assertEquals(4, studentDetailsModel.getStudents().size());
  }

  @Test
  void testGetStudentsWithMarks() {
    context.load(true)
        .json("/com/aurx/core/models/StudentDetailsModel/StudentDetailsWithMarksTest.json",
            "/content/vishnu-project/us/en/test");
    context.currentResource("/content/vishnu-project/us/en/test");
    studentDetailsModel = context.request().adaptTo(StudentDetailsModel.class);
    assertNotNull(studentDetailsModel);
    assertNotNull(studentDetailsModel.getStudents());
  }


}