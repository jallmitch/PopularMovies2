package com.example.jessemitchell.popularmovies.app.POJOs;

/**
 * Created by jesse.mitchell on 1/16/2017.
 */

public class StudentTest
{
//    @SerializedName("StudentId")
//    @Expose
    private String studentId;
//    @SerializedName("StudentName")
//    @Expose
    private String studentName;
//    @SerializedName("StudentMarks")
//    @Expose
    private String studentMarks;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentMarks() {
        return studentMarks;
    }

    public void setStudentMarks(String studentMarks) {
        this.studentMarks = studentMarks;
    }
}
