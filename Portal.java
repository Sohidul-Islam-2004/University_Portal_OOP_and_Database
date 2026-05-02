import java.nio.channels.SelectableChannel;
import java.sql.*;
import java.util.*;

class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

class CourseNotFoundException extends Exception {
    public CourseNotFoundException(String message) {
        super(message);
    }
}

class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/university_portal";
    private static final String USERNAME = "root"; // Change to your MySQL username
    private static final String PASSWORD = "sohidul"; // Change to your MySQL password

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}

class Result {
    private String studentId;
    private String courseId;
    private String courseName;
    private double marks;
    private String grade;
    private double gradePoint;
    private double credits;
    private String semester;

    public Result(String studentId, String courseId, double marks, String sm) {
        this.studentId = studentId;
        this.courseId = courseId;

        this.marks = marks;
        this.semester = sm;
        this.grade = calculateGrade(marks);
        this.gradePoint = calculateGradePoint(marks);
        String sql = "SELECT course_name, credits FROM courses WHERE course_id = ?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, courseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.courseName = rs.getString("course_name");
                this.credits = rs.getDouble("credits");
            } else {
                this.courseName = "Unknown Course";
                this.credits = 0.0;
                System.out.println("Warning: Course not found for ID: " + courseId);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching course details: " + e.getMessage());
            this.courseName = "Error loading course";
            this.credits = 0.0;
        }

    }

    private String calculateGrade(double marks) {
        if (marks >= 80)
            return "A+";
        else if (marks >= 75)
            return "A";
        else if (marks >= 70)
            return "A-";
        else if (marks >= 65)
            return "B+";
        else if (marks >= 60)
            return "B";
        else if (marks >= 55)
            return "B-";
        else if (marks >= 50)
            return "C+";
        else if (marks >= 45)
            return "C";
        else if (marks >= 40)
            return "D";
        else
            return "F";
    }

    private double calculateGradePoint(double marks) {
        if (marks >= 80)
            return 4.0;
        else if (marks >= 75)
            return 3.75;
        else if (marks >= 70)
            return 3.50;
        else if (marks >= 65)
            return 3.25;
        else if (marks >= 60)
            return 3.00;
        else if (marks >= 55)
            return 2.75;
        else if (marks >= 50)
            return 2.50;
        else if (marks >= 45)
            return 2.25;
        else if (marks >= 40)
            return 2.00;
        else
            return 0.0;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getMarks() {
        return marks;
    }

    public String getGrade() {
        return grade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public double getCredits() {
        return credits;
    }

    public String getSemester() {
        return semester;
    }

    public String toString() {
        return String.format("| %-6s | %-20s | %-5.1f | %-4s | %-4.2f | %-4.1f | %-10s |",
                courseId, courseName, marks, grade, gradePoint, credits, semester);
    }

    public boolean isPassed() {
        return marks >= 40;
    }
}

class Course {
    private String courseId;
    private String courseCode;
    private String courseName;
    private double credits;
    private String dept_name;

    public Course(String courseId, String courseCode, String courseName, double credits, String category) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.dept_name = category;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public String getCategory() {
        return dept_name;
    }

    public void setCategory(String category) {
        this.dept_name = category;
    }

    @Override
    public String toString() {
        return String.format("| %-3s | %-7s | %-45s | %-7.1f |",
                courseCode, courseName, credits, dept_name);
    }
}

abstract class Profile {
    protected String Contact_Number = "N/A";
    protected String Email = "N/A";
    protected String PresentAddress = "N/A";
    protected String PermanentAddress = "N/A";
    protected String BloodGroup = "N/A";
    protected String MaritalStatus = "N/A";
    protected String NationalID = "N/A";
    protected String DateBirth = "N/A";
    protected String password = "N/A";
    protected String ID = "N/A";
    protected String name = "N/A";
    protected String Department = "N/A";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public abstract void editProfile();

    public String truncateString(String str, int length) {
        if (str == null || str.length() <= length)
            return str;
        return str.substring(0, length - 3) + "...";
    }

    public void displayWelcomeMessage() {
        System.out.println("Welcome to the portal!");
    }

    public void changePassword(String type) throws InvalidCredentialsException {
        System.out.print("\nEnter current password: ");
        Scanner sc = new Scanner(System.in);
        String current = sc.next();

        if (!current.equals(password)) {
            throw new InvalidCredentialsException("Incorrect current password!");
        }

        while (true) {
            System.out.print("Enter new password: ");
            String newPass = sc.next();
            System.out.print("Confirm new password: ");
            String confirmPass = sc.next();
            String k = type + "_id";
            if (newPass.equals(confirmPass)) {
                password = newPass;

                String sql = "UPDATE " + type + " SET password =? WHERE " + k + "=?";

                try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

                    ps.setString(1, password);

                    ps.setString(2, ID);
                    ps.executeUpdate();

                } catch (SQLException e) {
                    System.out.println("Error updating Password : " + e.getMessage());
                }

                System.out.println("Password changed successfully!");
                return;
            } else {
                System.out.println("Passwords don't match! Try again!");
            }
        }
    }
}

class Student extends Profile {

    private double CreditsCompleted = 0.0;
    private double CGPA = 0.0;
    private String admitted_semester = "N/A";
    private ArrayList<Course> enrolledCourses;
    Scanner sc = new Scanner(System.in);

    Student() {
        enrolledCourses = new ArrayList<>();
    }

    Student(String student_ID, String password) {
        this.ID = student_ID;
        this.password = password;
        this.enrolledCourses = new ArrayList<>();
        loadFromDatabase(student_ID);
    }

    @Override
    public void editProfile() {
        System.out.println("\n=========== Edit Profile ===========");

        System.out.print("Enter Name [" + name + "]: ");
        String input = sc.nextLine();
        if (!input.isEmpty())
            name = input;

        System.out.print("Enter Department [" + Department + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            Department = input;

        System.out.print("Enter Contact Number [" + Contact_Number + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            Contact_Number = input;

        System.out.print("Enter Email [" + Email + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            Email = input;

        System.out.print("Enter Present Address [" + PresentAddress + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            PresentAddress = input;

        System.out.print("Enter Permanent Address [" + PermanentAddress + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            PermanentAddress = input;

        System.out.print("Enter Blood Group [" + BloodGroup + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            BloodGroup = input;

        System.out.print("Enter Marital Status [" + MaritalStatus + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            MaritalStatus = input;

        System.out.print("Enter National ID [" + NationalID + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            NationalID = input;

        System.out.print("Enter Date of Birth [" + DateBirth + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            DateBirth = input;

        System.out.print("Enter Admitted Semester [" + admitted_semester + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            admitted_semester = input;

        updateProfileInDatabase();
        System.out.println("Profile updated successfully!");
    }

    private void updateProfileInDatabase() {
        String sql = "UPDATE student SET   name=?, email=?, contact_number=?, present_address=?, " +
                "permanent_address=?, blood_group=?, marital_status=?, national_id=?, date_birth=? , password=? , department=?, admitted_semester=? ,password =? WHERE student_id=?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, Email);
            ps.setString(3, Contact_Number);
            ps.setString(4, PresentAddress);
            ps.setString(5, PermanentAddress);
            ps.setString(6, BloodGroup);
            ps.setString(7, MaritalStatus);
            ps.setString(8, NationalID);
            ps.setString(9, DateBirth);
            ps.setString(10, password);
            ps.setString(11, Department);
            ps.setString(12, admitted_semester);
            ps.setString(13, password);
            ps.setString(14, ID);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }

    @Override
    public void displayWelcomeMessage() {
        System.out.println("Welcome to the Student Portal, " + name + "!");
    }

    public void enrollInCourse(String courseId) throws CourseNotFoundException {
        try {
            String checkSql = "SELECT * FROM courses WHERE course_id=?";
            try (PreparedStatement checkPs = DatabaseConnection.getConnection().prepareStatement(checkSql)) {
                checkPs.setString(1, courseId);
                ResultSet rs = checkPs.executeQuery();

                if (!rs.next()) {
                    throw new CourseNotFoundException("Course with ID " + courseId + " not found!");
                }

                Course course = new Course(
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getDouble("credits"),
                        rs.getString("dept_name"));
                String delete = "DELETE FROM advised WHERE s_id=? AND course_id=?";
                String enrollSql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";

                try (PreparedStatement enrollPs = DatabaseConnection.getConnection().prepareStatement(enrollSql);
                        PreparedStatement deleteps = DatabaseConnection.getConnection().prepareStatement(delete)) {

                    enrollPs.setString(1, ID);
                    enrollPs.setString(2, courseId);

                    deleteps.setString(1, ID);
                    deleteps.setString(2, courseId);

                    deleteps.executeUpdate();
                    enrollPs.executeUpdate();
                }
                enrolledCourses.add(course);

                System.out.println("Successfully enrolled in: " + course.getCourseName());
            }
        } catch (SQLException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        }
    }

    public void loadFromDatabase(String ID) {
        String sql = "SELECT * from student  where student_id =?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
                Email = rs.getString("email");
                Contact_Number = rs.getString("contact_number");
                PresentAddress = rs.getString("present_address");
                PermanentAddress = rs.getString("permanent_address");
                BloodGroup = rs.getString("blood_group");
                MaritalStatus = rs.getString("marital_status");
                NationalID = rs.getString("national_id");
                DateBirth = rs.getString("date_birth");
                Department = rs.getString("department");
                CreditsCompleted = rs.getDouble("credits_completed");
                CGPA = rs.getDouble("cgpa");
                admitted_semester = rs.getString("admitted_semester");
            }

            loadEnrolledCourses();

        } catch (SQLException e) {
            System.out.println("Error loading student data: " + e.getMessage());
        }
    }

    private void loadEnrolledCourses() {
        enrolledCourses.clear();
        String sql = "SELECT * FROM  courses NATURAL JOIN enrollments WHERE student_id =?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course course = new Course(
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getDouble("credits"),
                        rs.getString("dept_name"));
                enrolledCourses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    public void showStudentMenu() {
        displayWelcomeMessage();
        while (true) {
            System.out.println("\n=========== Student Menu ===========");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Change Password");
            System.out.println("4. My Courses");
            System.out.println("5. View Available Courses");
            System.out.println("6. Enroll in Course");
            System.out.println("7. View My Results & CGPA");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        displayProfile();
                        break;
                    case 2:
                        editProfile();
                        break;
                    case 3:
                        try {
                            changePassword("student");

                        } catch (InvalidCredentialsException e) {
                            System.out.println("Password change failed: " + e.getMessage());
                        }
                        break;
                    case 4:
                        viewMyCourses();
                        break;
                    case 5:
                        viewAvailableCourses();
                        break;
                    case 6:
                        enrollInNewCourse();
                        break;
                    case 7:
                        viewMyResults();
                        break;
                    case 8:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            }
        }
    }

    private void displayProfile() {
        System.out.println("\n=========== Student Profile ===========");
        System.out.println("Student ID         : " + ID);
        System.out.println("Name               : " + name);
        System.out.println("Department         : " + Department);
        System.out.println("Credits Completed  : " + CreditsCompleted);
        System.out.println("CGPA               : " + CGPA);
        System.out.println("----------------------------------------");
        System.out.println("Contact Number     : " + Contact_Number);
        System.out.println("Email              : " + Email);
        System.out.println("Present Address    : " + PresentAddress);
        System.out.println("Permanent Address  : " + PermanentAddress);
        System.out.println("Blood Group        : " + BloodGroup);
        System.out.println("Marital Status     : " + MaritalStatus);
        System.out.println("National ID        : " + NationalID);
        System.out.println("Date of Birth      : " + DateBirth);
        System.out.println("Admitted Semester  : " + admitted_semester);
        System.out.println("========================================");
    }

    private void viewAvailableCourses() {
        System.out.println(
                "+----------------+-----------------+----------------------------------------------------------------------+--------------+--------------------------------+");
        System.out.println(
                "|    Course ID    |      Code      |                            Course Name                               |   Credits    |          Department            |");
        System.out.println(
                "+----------------+-----------------+----------------------------------------------------------------------+--------------+--------------------------------+");

        String sql = "SELECT * FROM advised natural join courses ORDER BY courses.course_id";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.next()) {
                System.out.println(
                        "                                                                         No Courses Available");
                System.out.println(
                        "+----------------+-----------------+----------------------------------------------------------------------+--------------+--------------------------------+");
                return;
            }
            do {
                System.out.printf("| %-14s | %-15s | %-68s | %-12.1f | %-30s |\n",
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getDouble("credits"),
                        rs.getString("dept_name"));
            } while (rs.next());
            System.out.println(
                    "+----------------+-----------------+----------------------------------------------------------------------+--------------+--------------------------------+");
        } catch (SQLException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    private void viewMyCourses() {
        if (enrolledCourses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            return;
        }

        System.out.println(
                "+-----------------+--------------------------------------------------------------------------+--------------+----------------------------------+");
        System.out.println(
                "|      Code       |                            Course Name                                   |   Credits    |           Department             |");
        System.out.println(
                "+-----------------+--------------------------------------------------------------------------+--------------+----------------------------------+");

        for (Course course : enrolledCourses) {
            System.out.printf("| %-15s | %-72s | %-12.1f | %-32s |\n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getCredits(),
                    course.getCategory());
        }

        System.out.println(
                "+-----------------+--------------------------------------------------------------------------+--------------+----------------------------------+");
    }

    private void enrollInNewCourse() {
        try {
            viewAvailableCourses();
            System.out.print("Enter Course ID to enroll: ");
            String courseId = sc.next();
            String sql = "SELECT * FROM advised WHERE course_id=?";

            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

                ps.setString(1, courseId);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("Invalid Selection");
                    return;
                }

            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }
            enrollInCourse(courseId);
        } catch (CourseNotFoundException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        }
    }

    public void viewMyResults() {
        System.out.println(
                "\n===================================== My Academic Results =====================================");

        String sql = "SELECT * FROM results WHERE student_id=? ORDER BY semester, course_id";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();

            double totalCredits = 0;
            double totalGradePoints = 0;
            boolean hasResults = false;

            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");
            System.out.println(
                    "| Course ID  |                    Course Name                      | Marks | Grade |  GP   |  Cr   |    Semester    |");
            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");

            while (rs.next()) {
                hasResults = true;
                double gp = rs.getDouble("grade_point");
                double cr = rs.getDouble("credits");

                System.out.printf("| %-10s | %-50s | %-5.1f | %-5s | %-5.2f | %-5.1f | %-14s |\n",
                        rs.getString("course_id"),
                        truncateString(rs.getString("course_name"), 50),
                        rs.getDouble("marks"),
                        rs.getString("grade"),
                        gp,
                        cr,
                        rs.getString("semester"));

                if (gp > 0) {
                    totalGradePoints += gp * cr;
                    totalCredits += cr;
                }
            }

            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");

            if (hasResults && totalCredits > 0) {
                double cgpa = totalGradePoints / totalCredits;
                this.CGPA = cgpa;
                this.CreditsCompleted = totalCredits;

                updateCGPAInDatabase();

                System.out.printf(
                        " Total Credits Completed: %-4.1f                                                                           \n",
                        totalCredits);
                System.out.printf(
                        " Overall CGPA: %-4.2f                                                                                      \n",
                        cgpa);

                String status = cgpa >= 3.75 ? "Excellent"
                        : cgpa >= 3.50 ? "Very Good"
                                : cgpa >= 3.00 ? "Good" : cgpa >= 2.50 ? "Satisfactory" : "Needs Improvement";

                System.out.printf(
                        " Academic Status: %-20s                                                                   \n",
                        status);
            } else if (!hasResults) {
                System.out.println(
                        "                                No results available yet.                                                   ");
            }

            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");
        } catch (SQLException e) {
            System.out.println("Error viewing results: " + e.getMessage());
        }
    }

    private void updateCGPAInDatabase() {
        String sql = "UPDATE student SET cgpa=?, credits_completed=? WHERE student_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, CGPA);
            ps.setDouble(2, CreditsCompleted);
            ps.setString(3, ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating CGPA: " + e.getMessage());
        }
    }

    public static void registerNewStudent() {
        Scanner sc = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();

        System.out.println("==================================");
        System.out.println("         Student Portal");
        System.out.println("==================================");

        try {
            System.out.print("Enter Student ID: ");
            String newID = sc.next();

            String checkSql = "SELECT * FROM student WHERE student_id=?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, newID);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    System.out.println("Student ID already exists!");
                    return;
                }
            }

            System.out.print("Enter Password: ");
            String newPass = sc.next();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            String userSql = "INSERT INTO student (student_id, password, name) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(userSql)) {
                ps.setString(1, newID);
                ps.setString(2, newPass);
                ps.setString(3, name);
                ps.executeUpdate();
            }

            System.out.println("Student registered successfully.");

        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public static Student loginStudent() {
        Scanner sc = new Scanner(System.in);
        Random rd = new Random();
        int x = rd.nextInt(10);
        int y = rd.nextInt(10);

        System.out.println("==================================");
        System.out.println("         Student Portal");
        System.out.println("==================================");

        System.out.print("Enter Student ID     : ");
        String inputID = sc.next();

        System.out.print("Enter Password        : ");
        String inputPass = sc.next();

        System.out.println("----------------------------------");
        System.out.println("Security Check: " + x + " + " + y + " = ?");
        System.out.print("Answer                : ");

        try {
            int answer = sc.nextInt();
            sc.nextLine();
            System.out.println("----------------------------------");

            if (answer != x + y) {
                throw new InvalidCredentialsException("Wrong security answer!");
            }

            String sql = "SELECT * FROM student WHERE student_id=? AND password=? ";
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
                ps.setString(1, inputID);
                ps.setString(2, inputPass);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    System.out.println("\nLogin Successful! Welcome, Student.");
                    return new Student(inputID, inputPass);

                } else {
                    throw new InvalidCredentialsException("Invalid ID or password!");
                }
            }

        } catch (SQLException | InvalidCredentialsException e) {
            System.out.println("Login failed: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for security check!");
            sc.nextLine();
        }

        return null;
    }
}

class Faculty extends Profile {
    String JoinSemester = "N/A";
    int salary = 0;
    String positon = "N/A";
    private ArrayList<String> advisedStudents;

    Scanner sc = new Scanner(System.in);

    Faculty() {
        advisedStudents = new ArrayList<>();
    }

    Faculty(String faculty_ID, String password) {
        this.ID = faculty_ID;
        this.password = password;
        this.advisedStudents = new ArrayList<>();
        loadFromDatabase();
    }

    @Override
    public void editProfile() {
        System.out.println("\n=========== Edit Profile ===========");

        System.out.print("Enter Name [" + name + "]: ");
        String input = sc.nextLine();
        if (!input.isEmpty())
            name = input;

        System.out.print("Enter Department [" + Department + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            Department = input;

        System.out.print("Enter Contact Number [" + Contact_Number + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            Contact_Number = input;

        System.out.print("Enter Email [" + Email + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            Email = input;

        System.out.print("Enter Present Address [" + PresentAddress + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            PresentAddress = input;

        System.out.print("Enter Permanent Address [" + PermanentAddress + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            PermanentAddress = input;

        System.out.print("Enter Blood Group [" + BloodGroup + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            BloodGroup = input;

        System.out.print("Enter Marital Status [" + MaritalStatus + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            MaritalStatus = input;

        System.out.print("Enter National ID [" + NationalID + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            NationalID = input;

        System.out.print("Enter Date of Birth [" + DateBirth + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            DateBirth = input;
        System.out.print("Enter Position [" + positon + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            positon = input;

        System.out.print("Enter Joined Semester [" + JoinSemester + "]: ");
        input = sc.nextLine();
        if (!input.isEmpty())
            JoinSemester = input;

        updateProfileInDatabase();
        System.out.println("Profile updated successfully!");
    }

    private void updateProfileInDatabase() {
        String sql = "UPDATE faculty SET name=?, email=?, contact_number=?, present_address=?, " +
                "permanent_address=?, blood_group=?, marital_status=?, national_id=?, date_birth=? , position=?,joined_semester=? WHERE faculty_id=?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, Email);
            ps.setString(3, Contact_Number);
            ps.setString(4, PresentAddress);
            ps.setString(5, PermanentAddress);
            ps.setString(6, BloodGroup);
            ps.setString(7, MaritalStatus);
            ps.setString(8, NationalID);
            ps.setString(9, DateBirth);
            ps.setString(10, positon);
            ps.setString(11, JoinSemester);

            ps.setString(12, ID);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }

    @Override
    public void displayWelcomeMessage() {
        System.out.println("Welcome to the Faculty Portal, Professor " + name + "!");
    }

    public void loadFromDatabase() {
        String sql = "SELECT * FROM  faculty  WHERE faculty_id=?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
                Email = rs.getString("email");
                Contact_Number = rs.getString("contact_number");
                PresentAddress = rs.getString("present_address");
                PermanentAddress = rs.getString("permanent_address");
                BloodGroup = rs.getString("blood_group");
                MaritalStatus = rs.getString("marital_status");
                NationalID = rs.getString("national_id");
                DateBirth = rs.getString("date_birth");
                positon = rs.getString("position");
                Department = rs.getString("department");
                JoinSemester = rs.getString("joined_semester");
            }
        } catch (SQLException e) {
            System.out.println("Error loading faculty data: " + e.getMessage());
        }
    }

    public void showFacultyMenu() {
        displayWelcomeMessage();
        while (true) {
            System.out.println("\n=========== Faculty Menu ===========");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Change Password");
            System.out.println("4. View Student Profile");
            System.out.println("5. Advise Student Courses");
            System.out.println("6. View My Advised Students");
            System.out.println("7. Give Marks to Student");
            System.out.println("8. View Student Results");
            System.out.println("9. Logout");

            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        displayProfile();
                        break;
                    case 2:
                        editProfile();
                        break;
                    case 3:
                        try {
                            changePassword("faculty");

                        } catch (InvalidCredentialsException e) {
                            System.out.println("Password change failed: " + e.getMessage());
                        }
                        break;
                    case 4:
                        viewStudentProfile();
                        break;
                    case 5:
                        adviseStudent();
                        break;
                    case 6:
                        viewAdvisedStudents();
                        break;
                    case 7:
                        giveMarksToStudent();
                        break;
                    case 8:
                        viewStudentResults();
                        break;
                    case 9:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            }
        }
    }

    private void displayProfile() {
        System.out.println("\n=========== Faculty Profile ===========");
        System.out.println("Faculty ID         : " + ID);
        System.out.println("Name               : " + name);
        System.out.println("Position           : " + positon);
        System.out.println("Department         : " + Department);
        System.out.println("----------------------------------------");
        System.out.println("Contact Number     : " + Contact_Number);
        System.out.println("Email              : " + Email);
        System.out.println("Present Address    : " + PresentAddress);
        System.out.println("Permanent Address  : " + PermanentAddress);
        System.out.println("Blood Group        : " + BloodGroup);
        System.out.println("Marital Status     : " + MaritalStatus);
        System.out.println("National ID        : " + NationalID);
        System.out.println("Date of Birth      : " + DateBirth);
        System.out.println("Joined Semester    : " + JoinSemester);
        System.out.println("Salary             : " + salary);
        System.out.println("========================================");
    }

    private void viewStudentProfile() {
        System.out.print("Enter Student ID to view: ");
        String studentId = sc.next();

        String sql = "SELECT *  from student   WHERE student_id=?";
        String advisorsql = "select f.name from advisor natural join faculty as f where student_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
                PreparedStatement pk = DatabaseConnection.getConnection().prepareStatement(advisorsql)) {
            ps.setString(1, studentId);
            pk.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            ResultSet rk = pk.executeQuery();
            String advisor = "N/A";
            if (rk.next()) {
                advisor = rk.getString("name");
            }
            if (rs.next()) {
                System.out.println("\n=========== Student Profile ===========");
                System.out.println("Student ID         : " + rs.getString("student_id"));
                System.out.println("Name               : " + rs.getString("name"));

                System.out.println("Department         : " + rs.getString("department"));
                System.out.println("Credits Completed  : " + rs.getDouble("credits_completed"));
                System.out.println("CGPA               : " + rs.getDouble("cgpa"));
                System.out.println("Advisor            : " + advisor);
                System.out.println("----------------------------------------");
                System.out.println("Email              : " + rs.getString("email"));
                System.out.println("Contact            : " + rs.getString("contact_number"));
                System.out.println("========================================");
            } else {
                System.out.println("Student not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing student: " + e.getMessage());
        }
    }

    private void adviseStudent() {
        System.out.println("\n================================================");
        System.out.println("            COURSE ADVISING SYSTEM");
        System.out.println("================================================");

        viewAdvisedStudents();
        System.out.print("Enter Student ID: ");
        String studentId = sc.next();
        String sql = "SELECT s.student_id, s.name, s.email, s.contact_number, s.department from advisor natural join student as s  where faculty_id=?";
        try {
            String checkSql = "SELECT * FROM   (" + sql + ") as adv WHERE adv.student_id=?";
            try (PreparedStatement checkPs = DatabaseConnection.getConnection().prepareStatement(checkSql)) {
                checkPs.setString(1, ID);
                checkPs.setString(2, studentId);
                ResultSet rs = checkPs.executeQuery();

                if (!rs.next()) {
                    System.out.println("You are not Advisor of " + studentId + "!");
                    return;
                }
            }

            if (!advisedStudents.contains(studentId)) {
                advisedStudents.add(studentId);
            }

            displayCourseCatalog();
            while (true) {

                System.out.print("Enter course ID to register : ");
                String input = sc.next();

                String checkEnrollSql = "SELECT * FROM enrollments WHERE student_id=? AND course_id=?";
                try (PreparedStatement checkEnrollPs = DatabaseConnection.getConnection()
                        .prepareStatement(checkEnrollSql)) {
                    checkEnrollPs.setString(1, studentId);
                    checkEnrollPs.setString(2, input);
                    ResultSet enrollRs = checkEnrollPs.executeQuery();

                    if (!enrollRs.next()) {
                        String enrollSql = "INSERT INTO advised ( f_id , s_id, course_id) VALUES (  ? , ?, ?)";
                        try (PreparedStatement enrollPs = DatabaseConnection.getConnection()
                                .prepareStatement(enrollSql)) {
                            enrollPs.setString(1, ID);
                            enrollPs.setString(2, studentId);
                            enrollPs.setString(3, input);
                            enrollPs.executeUpdate();
                        }
                        break;
                    } else

                    {
                        System.out.println("Already Enrolled");

                    }
                }
            }

            System.out.println("\nCourses registered successfully for student " + studentId);

        } catch (SQLException e) {
            System.out.println("Error in advising: " + e.getMessage());
        }
    }

    private void displayCourseCatalog() {
        System.out.println(
                "+----------------+-----------------+----------------------------------------------------------------------+--------------+--------------------------------+");
        System.out.println(
                "|    Course ID    |      Code       |                            Course Name                               |   Credits    |          Department            |");
        System.out.println(
                "+----------------+-----------------+----------------------------------------------------------------------+--------------+--------------------------------+");

        String sql = "SELECT * FROM courses ORDER BY course_id";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("| %-14s | %-15s | %-68s | %-12.1f | %-30s |\n",
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getDouble("credits"),
                        rs.getString("dept_name"));
            }
            System.out.println(
                    "+----------------+-----------------+----------------------------------------------------------------------+--------------+--------------------------------+");
        } catch (SQLException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    public void viewAdvisedStudents() {
        System.out.println(
                "\n===================================== My Advised Students =====================================");

        String sql = "SELECT s.student_id, s.name, s.email, s.contact_number, s.department from advisor natural join student as s  where faculty_id=?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, this.ID);
            ResultSet rs = ps.executeQuery();

            boolean hasStudents = false;
            // String currentDepartment = "";
            int studentCount = 0;

            System.out.println(
                    "+--------------+--------------------------------------------------+----------------------------------+------------------+");
            System.out.println(
                    "| Student ID   | Student Name                                     | Email                            | Contact Number   |");
            System.out.println(
                    "+--------------+--------------------------------------------------+----------------------------------+------------------+");

            while (rs.next()) {
                hasStudents = true;
                studentCount++;

                System.out.printf("| %-12s | %-48s | %-32s | %-16s |\n",
                        rs.getString("student_id"),
                        truncateString(rs.getString("name"), 48),
                        truncateString(rs.getString("email"), 32),
                        rs.getString("contact_number") != null ? rs.getString("contact_number") : "N/A");
            }

            if (hasStudents) {
                System.out.println(
                        "+--------------+--------------------------------------------------+----------------------------------+------------------+");
                System.out.printf(
                        " Total Advised Students: %-3d                                                                                  \n",
                        studentCount);
                System.out.println(
                        "+--------------+--------------------------------------------------+----------------------------------+------------------+");
            } else {
                System.out.println(
                        "                                    No students advised yet.                                                   ");
                System.out.println(
                        "+--------------+--------------------------------------------------+----------------------------------+------------------+");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing advised students: " + e.getMessage());
        }
    }

    public void giveMarksToStudent() {
        System.out.println("\n========== Give Marks to Student ==========");
        String sql = "SELECT student_id, name, instructor.course_id FROM instructor " +
                "NATURAL JOIN enrollments NATURAL JOIN student WHERE faculty_id = ?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ID);
            ResultSet rs = ps.executeQuery();

            System.out.println(
                    "\n+------------------+----------------------------------------------------+------------------+");
            System.out.println(
                    "|   Student ID     |                    Student Name                    |    Course ID     |");
            System.out.println(
                    "+------------------+----------------------------------------------------+------------------+");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("| %-16s | %-50s | %-16s |\n",
                        rs.getString("student_id"),
                        truncateString(rs.getString("name"), 50),
                        rs.getString("course_id"));
            }

            if (found) {
                System.out.println(
                        "+------------------+----------------------------------------------------+------------------+");
            } else {
                System.out.println(
                        "|                          No students found in your courses                          |");
                System.out.println(
                        "+------------------+----------------------------------------------------+------------------+");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        String studentID;
        System.out.println("Enter Student ID : ");
        studentID = sc.next();
        System.out.print("Enter Course ID: ");
        String courseId = sc.next();

        System.out.print("Enter Marks (0-100): ");
        double marks = sc.nextDouble();
        String sm;
        System.out.println("Enter Semester : ");
        sm = sc.next();
        if (marks < 0 || marks > 100) {
            System.out.println("Invalid marks! Must be between 0-100.");
            return;
        }

        Result result = new Result(studentID, courseId, marks, sm);
        saveResultToDatabase(result);

        System.out.println("Marks given successfully! To ");
        System.out.println("Grade: " + result.getGrade() + " | Grade Point: " + result.getGradePoint());

        if (marks < 40) {
            System.out.println("WARNING: Student has FAILED this course!");
        }
    }

    private void saveResultToDatabase(Result result) {
        String sql = "INSERT INTO results (student_id, course_id, course_name, marks, grade, grade_point, credits, semester) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String del = "DELETE FROM enrollments WHERE course_id = ? AND student_id = ?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
                PreparedStatement ds = DatabaseConnection.getConnection().prepareStatement(del)) {
            ds.setString(1, result.getCourseId());
            ds.setString(2, result.getStudentId());
            ds.executeUpdate();
            ps.setString(1, result.getStudentId());
            ps.setString(2, result.getCourseId());
            ps.setString(3, result.getCourseName());
            ps.setDouble(4, result.getMarks());
            ps.setString(5, result.getGrade());
            ps.setDouble(6, result.getGradePoint());
            ps.setDouble(7, result.getCredits());
            ps.setString(8, result.getSemester());
            ps.executeUpdate();

            System.out.println("Result saved successfully!");

        } catch (SQLException e) {
            System.out.println("Error savinfg result: " + e.getMessage());
        }
    }

    public void viewStudentResults() {
        System.out.println("\n========== View Student Results ==========");
        System.out.print("Enter Student ID: ");
        String studentId = sc.next();

        String sql = "SELECT * FROM results WHERE student_id=? ORDER BY semester, course_id";
        String sql1 = "Select * from student where student_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
                PreparedStatement pk = DatabaseConnection.getConnection().prepareStatement(sql1)) {
            ps.setString(1, studentId);
            pk.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            ResultSet rk = pk.executeQuery();
            double totalCredits = 0;
            double cgpa = 0;
            if (rk.next()) {
                totalCredits = rk.getDouble("credits_completed");
                cgpa = rk.getDouble("cgpa");
            }

            boolean hasResults = false;

            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");
            System.out.println(
                    "| Course ID  |                    Course Name                      | Marks | Grade |  GP   |  Cr   |    Semester    |");
            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");

            while (rs.next()) {
                hasResults = true;
                double gp = rs.getDouble("grade_point");
                double cr = rs.getDouble("credits");

                System.out.printf("| %-10s | %-50s | %-5.1f | %-5s | %-5.2f | %-5.1f | %-14s |\n",
                        rs.getString("course_id"),
                        truncateString(rs.getString("course_name"), 50),
                        rs.getDouble("marks"),
                        rs.getString("grade"),
                        gp,
                        cr,
                        rs.getString("semester"));

            }

            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");

            if (hasResults && totalCredits > 0) {

                System.out.printf(
                        " Total Credits Completed: %-4.1f                                                                           \n",
                        totalCredits);
                System.out.printf(
                        " Overall CGPA: %-4.2f                                                                                      \n",
                        cgpa);

                String status = cgpa >= 3.75 ? "Excellent"
                        : cgpa >= 3.50 ? "Very Good"
                                : cgpa >= 3.00 ? "Good" : cgpa >= 2.50 ? "Satisfactory" : "Needs Improvement";

                System.out.printf(
                        " Academic Status: %-20s                                                                   \n",
                        status);
            } else if (!hasResults) {
                System.out.println(
                        "                                No results available yet.                                                   ");
            }

            System.out.println(
                    "+------------+----------------------------------------------------+-------+-------+-------+-------+----------------+");
        } catch (SQLException e) {
            System.out.println("Error viewing results: " + e.getMessage());
        }
    }

    public static void registerNewFaculty() {
        Scanner sc = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();

        System.out.println("==================================");
        System.out.println("         Faculty Portal");
        System.out.println("==================================");

        try {
            System.out.print("Enter Faculty ID: ");
            String newID = sc.next();

            String checkSql = "SELECT * FROM faculty WHERE faculty_id=?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, newID);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    System.out.println("Faculty ID already exists!");
                    return;
                }
            }

            System.out.print("Enter Password: ");
            String newPass = sc.next();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            String userSql = "INSERT INTO faculty (faculty_id, password,  name) VALUES (?, ?,  ?)";
            try (PreparedStatement ps = conn.prepareStatement(userSql)) {
                ps.setString(1, newID);
                ps.setString(2, newPass);
                ps.setString(3, name);
                ps.executeUpdate();
            }

            System.out.println("Faculty registered successfully.");

        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public static Faculty loginFaculty() {
        Scanner sc = new Scanner(System.in);
        Random rd = new Random();
        int x = rd.nextInt(20);
        int y = rd.nextInt(20);

        System.out.println("==================================");
        System.out.println("         Faculty Portal");
        System.out.println("==================================");

        System.out.print("Enter Faculty ID     : ");
        String inputID = sc.next();

        System.out.print("Enter Password        : ");
        String inputPass = sc.next();

        System.out.println("----------------------------------");
        System.out.println("Security Check: " + x + " + " + y + " = ?");
        System.out.print("Answer                : ");

        try {
            int answer = sc.nextInt();
            sc.nextLine();
            System.out.println("----------------------------------");

            if (answer != x + y) {
                throw new InvalidCredentialsException("Wrong security answer!");
            }

            String sql = "SELECT * FROM faculty WHERE faculty_id=? AND password=?";
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
                ps.setString(1, inputID);
                ps.setString(2, inputPass);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    System.out.println("\nLogin Successful! Welcome, Faculty.");
                    return new Faculty(inputID, inputPass);
                } else {
                    throw new InvalidCredentialsException("Invalid ID or password!");
                }
            }

        } catch (SQLException | InvalidCredentialsException e) {
            System.out.println("Login failed: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for security check!");
            sc.nextLine();
        }

        return null;
    }
}

public class Portal {
    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("           University");
        System.out.println("             Portal");
        System.out.println("==================================\n");
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            try {
                System.out.println("\nPlease select an option:");
                System.out.println("-------------------------");
                System.out.println("1 -> Student Portal");
                System.out.println("2 -> Faculty Portal");
                System.out.println("3 -> Exit");
                System.out.println("-------------------------");

                System.out.print("Enter your choice (1, 2, 3 ): ");
                choice = sc.nextInt();
                sc.nextLine();
                System.out.println();

                switch (choice) {
                    case 1:
                        handleStudentPortal();
                        break;
                    case 2:
                        handleFacultyPortal();
                        break;
                    case 3:
                        System.out.println("\nThank you for using the EWU Portal!");
                        System.out.println("==================================");
                        DatabaseConnection.closeConnection();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleStudentPortal() {
        Scanner sc = new Scanner(System.in);
        System.out.println("----- Student Portal -----");
        System.out.println("1. Register Student");
        System.out.println("2. Login as Student");
        System.out.print("Enter your choice: ");

        try {
            int studentChoice = sc.nextInt();
            sc.nextLine();

            if (studentChoice == 1) {
                Student.registerNewStudent();
            } else if (studentChoice == 2) {
                Student student = Student.loginStudent();
                if (student != null) {
                    student.showStudentMenu();
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            sc.nextLine();
        }
    }

    private static void handleFacultyPortal() {
        Scanner sc = new Scanner(System.in);
        System.out.println("----- Faculty Portal -----");
        System.out.println("1. Register Faculty");
        System.out.println("2. Login as Faculty");
        System.out.print("Enter your choice: ");

        try {
            int facultyChoice = sc.nextInt();
            sc.nextLine();

            if (facultyChoice == 1) {
                Faculty.registerNewFaculty();
            } else if (facultyChoice == 2) {
                Faculty faculty = Faculty.loginFaculty();
                if (faculty != null) {
                    faculty.showFacultyMenu();
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            sc.nextLine();
        }
    }
}