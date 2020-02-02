package com.imed.submission.modal;

public class UploadAssignmentModal {

    private String id;
    private String courseName;
    private String courseYear;
    private String subject;
    private String topic;
    private String createdDate;
    private String createdBy;

    public UploadAssignmentModal(String id, String courseName, String courseYear, String subject,
                                 String topic, String createdDate, String createdBy) {
        this.id = id;
        this.courseName = courseName;
        this.courseYear = courseYear;
        this.subject = subject;
        this.topic = topic;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public UploadAssignmentModal(){

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "UploadAssignmentModal{" +
                "courseName='" + courseName + '\'' +
                ", courseYear='" + courseYear + '\'' +
                ", subject='" + subject + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
