package ua.vika;

import java.io.*;
import java.util.*;

/**
 * Created by zidd on 3/24/14.
 */
public class StudentMarks {
    private Subject subject;
    private Group group;
    private List<String> dates;
    private Map<Integer, List<String>> studentMarks;

    public Subject getSubject() {
        return subject;
    }

    public Group getGroup() {
        return group;
    }

    public List<String> getDates() {
        return dates;
    }

    public Map<Integer, List<String>> getStudentMarks() {
        return studentMarks;
    }

    public StudentMarks(Subject subject, Group group) {
        this.subject = subject;
        this.group = group;
        this.dates = new ArrayList<String>();
        this.studentMarks = new HashMap<Integer, List<String>>();
        readStudentMarksFromFile();
        readSubjectDatesFromFile();
    }

    private enum TypeFilePath {
        FOR_DATE, FOR_MARK;
    }

    private String getFilePath(TypeFilePath typeFilePath) {
        if(typeFilePath == TypeFilePath.FOR_DATE)  {
            return "resources/" + subject.name().toLowerCase() + "Date.txt";
        }
        else if(typeFilePath == TypeFilePath.FOR_MARK) {
            return "resources/" + subject.name().toLowerCase() + "Marks.txt";
        }
        return null;
    }

    private void readSubjectDatesFromFile() {
        BufferedReader br;
        FileInputStream fis;
        InputStreamReader isr;
        //FileReader fr;
        try{
            //fr = new FileReader(getFilePath(TypeFilePath.FOR_DATE));
            //br = new BufferedReader(fr);
            fis = new FileInputStream(getFilePath(TypeFilePath.FOR_DATE));
            isr = new InputStreamReader(fis,"UTF8");
            br = new BufferedReader(isr);

            String s;
            while((s = br.readLine()) != null) {
                dates.add(s);
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private void readStudentMarksFromFile() {
        BufferedReader br;
        FileInputStream fis;
        InputStreamReader isr;
        //FileReader fr;
        try{
            File file = new File(getFilePath(TypeFilePath.FOR_MARK));
            if(!file.exists()) return;
            //fr = new FileReader(file);
            //br = new BufferedReader(fr);
            fis = new FileInputStream(getFilePath(TypeFilePath.FOR_MARK));
            isr = new InputStreamReader(fis,"UTF8");
            br = new BufferedReader(isr);
            String s;
            int i = 1;
            while((s = br.readLine()) != null) {
                String[] marks = s.split(" ");
                List<String> marksList = new ArrayList<String>();
                for(String str : marks) {
                    if(str.equals("-1")) str = "";
                    marksList.add(str);
                }
                studentMarks.put(i++, marksList);
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//
//        //StudentMarks marks = new StudentMarks(Subject.GRAFIC, new Group("C:\\users\\zidd\\Desktop\\group.txt"));
//        System.out.println("Dates:");
//        for(String str : marks.dates) {
//            System.out.println(str);
//        }
//        System.out.println("Groups:");
//        for(Student stud : marks.group.getStudentsList()) {
//            System.out.println(stud.getFirstName() + " " + stud.getLastName());
//        }
//        System.out.println("Marks:");
//        Set<Map.Entry<Integer,List<String>>> entrySet = marks.studentMarks.entrySet();
//        for(Map.Entry<Integer,List<String>> entry : entrySet) {
//            System.out.println(entry.getKey() + ". " + marks.group.getStudent(entry.getKey()).getLastName() + ":");
//            for(String str : entry.getValue()) {
//                System.out.println(str);
//            }
//        }
//    }
}
