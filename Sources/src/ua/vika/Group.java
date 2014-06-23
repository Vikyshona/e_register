package ua.vika;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zidd on 3/24/14.
 */
public class Group {
    private List<Student> studentsList;

    public List<Student> getStudentsList() {
        return studentsList;
    }

    public Group(String path) {
        studentsList = new ArrayList<Student>();
        readStudentListFromFile(path);
    }

    private void readStudentListFromFile(String path) {
        try {
            Scanner sc = new Scanner(new File(path),"UTF8");
            while(sc.hasNext()) {
                String lastName = sc.next();
                String firstName = sc.next();
                String middleName = sc.next();
                Student student = new Student(firstName, lastName, middleName);
                addStudent(student);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Student getStudent(int number) {
        return studentsList.get(number - 1);
    }

    private void addStudent(Student student) {
        studentsList.add(student);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(Student student : getStudentsList()) {
            sb.append( i++ + ". " + student.getLastName() + " " + student.getFirstName() + " " + student.getMiddleName() + "\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Group group = new Group("C:\\users\\zidd\\Desktop\\group.txt");
        List<Student> studentList = group.getStudentsList();
        System.out.println(studentList.size());
        for(Student stud : studentList) {
            System.out.println(stud.getFirstName() + " " + stud.getLastName());
        }
    }
}
