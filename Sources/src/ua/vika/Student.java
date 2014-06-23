package ua.vika;

/**
 * Created by zidd on 3/24/14.
 */
public class Student {
    private String firstName;
    private String lastName;
    private String middleName;

    public Student(String firstName, String lastName, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName;
    }
}
