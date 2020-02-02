package com.imed.submission.modal;

public class UserModel {

    private String firstName;
    private String lastName;
    private String courseName;
    private String courseYear;
    private boolean isStudent;
    private boolean isTeacher;
    private boolean isAdmin;
    private String userUid;
    private String phoneNumber;
    private String password;
    private String email;
    private String createdDate;
    private String createdBy;
    private String modifiedDate;
    private String modifiedBy;

    public UserModel(String firstName, String lastName, String courseName, String courseYear, boolean isStudent, boolean isTeacher, boolean isAdmin, String userUid, String phoneNumber, String password, String email, String createdDate, String createdBy, String modifiedDate, String modifiedBy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseName = courseName;
        this.courseYear = courseYear;
        this.isStudent = isStudent;
        this.isTeacher = isTeacher;
        this.isAdmin = isAdmin;
        this.userUid = userUid;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseYear='" + courseYear + '\'' +
                ", isStudent=" + isStudent +
                ", isTeacher=" + isTeacher +
                ", isAdmin=" + isAdmin +
                ", userUid='" + userUid + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
