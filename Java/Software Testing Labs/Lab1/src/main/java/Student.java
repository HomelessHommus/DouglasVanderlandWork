import java.util.Calendar;

public class Student {
    private int idNumber = 0;
    private String familyName = "";
    private String personalName = "";
    private int yearOfBirth = 0;

    public Student(int idNumber, String familyName, String personalName, int yearOfBirth) {
        this.idNumber = idNumber;
        this.familyName = familyName;
        this.personalName = personalName;
        this.yearOfBirth = yearOfBirth;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public int getAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return currentYear - getYearOfBirth();
    }
}
