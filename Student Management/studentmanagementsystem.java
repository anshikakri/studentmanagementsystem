import java.util.*;

class Subject {
    String name;

    Subject(String name) {
        this.name = name;
    }
}

class Course {
    String name;
    List<Subject> subjects;

    Course(String name) {
        this.name = name;
        this.subjects = new ArrayList<>();
    }

    void addSubject(String subjectName) {
        subjects.add(new Subject(subjectName));
    }

    void showSubjects() {
        for (Subject s : subjects) {
            System.out.println("- " + s.name);
        }
    }
}

class Student {
    static int idCounter = 1;
    int id;
    String name, email;
    int age;
    Course course;
    List<Subject> selectedSubjects = new ArrayList<>();
    int score = -1;
    boolean hasTakenExam = false;

    Student(String name, int age, String email) {
        this.id = idCounter++;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    void viewResult() {
        if (!hasTakenExam) {
            System.out.println("You haven't taken the exam yet.");
        } else {
            System.out.println("Score: " + score + "/5");
            System.out.println(score >= 3 ? "Result: PASS" : "Result: FAIL");
        }
    }
}

class Question {
    String question, optionA, optionB, optionC, optionD;
    char correctAnswer;

    Question(String q, String a, String b, String c, String d, char correct) {
        question = q;
        optionA = a;
        optionB = b;
        optionC = c;
        optionD = d;
        correctAnswer = correct;
    }

    boolean ask(Scanner sc) {
        System.out.println(question);
        System.out.println("A. " + optionA);
        System.out.println("B. " + optionB);
        System.out.println("C. " + optionC);
        System.out.println("D. " + optionD);
        System.out.print("Your answer: ");
        char ans = Character.toUpperCase(sc.next().charAt(0));
        return ans == correctAnswer;
    }
}

public class studentmanagementsystem {
    static Scanner sc = new Scanner(System.in);
    static List<Course> courseList = new ArrayList<>();
    static List<Student> students = new ArrayList<>();
    static List<Question> exam = new ArrayList<>();

    public static void main(String[] args) {
        seedQuestions();
        mainMenu();
    }

    static void mainMenu() {
        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Portal");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> studentMenu();
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add Course");
            System.out.println("2. Add Subject to Course");
            System.out.println("3. View Students");
            System.out.println("4. View Exam Results");
            System.out.println("5. Back");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter course name: ");
                    String cname = sc.nextLine();
                    courseList.add(new Course(cname));
                    System.out.println("Course added.");
                }
                case 2 -> {
                    listCourses();
                    System.out.print("Select course index: ");
                    int idx = sc.nextInt();
                    sc.nextLine();
                    if (idx >= 0 && idx < courseList.size()) {
                        System.out.print("Enter subject name: ");
                        String sub = sc.nextLine();
                        courseList.get(idx).addSubject(sub);
                        System.out.println("Subject added.");
                    } else {
                        System.out.println("Invalid index.");
                    }
                }
                case 3 -> {
                    for (Student s : students) {
                        System.out.println(s.id + ": " + s.name + ", " + s.email);
                    }
                }
                case 4 -> {
                    for (Student s : students) {
                        System.out.println(s.name + " - Score: " + (s.hasTakenExam ? s.score : "Not taken"));
                    }
                }
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void studentMenu() {
        System.out.println("\n--- Student Portal ---");
        System.out.println("1. Register");
        System.out.println("2. Take Exam");
        System.out.println("3. View Result");
        System.out.println("4. Back");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> registerStudent();
            case 2 -> takeExam();
            case 3 -> viewStudentResult();
            case 4 -> {
            }
            default -> System.out.println("Invalid option!");
        }
    }

    static void registerStudent() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Age: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        Student st = new Student(name, age, email);

        listCourses();
        System.out.print("Choose course index: ");
        int courseIdx = sc.nextInt();
        sc.nextLine();
        if (courseIdx >= 0 && courseIdx < courseList.size()) {
            Course selected = courseList.get(courseIdx);
            st.course = selected;
            System.out.println("Choose subjects from the course:");
            for (int i = 0; i < selected.subjects.size(); i++) {
                System.out.println(i + ". " + selected.subjects.get(i).name);
            }
            System.out.print("Enter subject indices (comma-separated): ");
            String[] subs = sc.nextLine().split(",");
            for (String s : subs) {
                int si = Integer.parseInt(s.trim());
                if (si >= 0 && si < selected.subjects.size()) {
                    st.selectedSubjects.add(selected.subjects.get(si));
                }
            }
            students.add(st);
            System.out.println("Registration successful. Your ID: " + st.id);
        } else {
            System.out.println("Invalid course.");
        }
    }

    static void takeExam() {
        System.out.print("Enter your ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        Student s = findStudentById(id);
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }
        if (s.hasTakenExam) {
            System.out.println("Exam already taken.");
            return;
        }
        int score = 0;
        for (Question q : exam) {
            if (q.ask(sc)) score++;
        }
        s.score = score;
        s.hasTakenExam = true;
        System.out.println("Exam completed. Score: " + score + "/5");
    }

    static void viewStudentResult() {
        System.out.print("Enter your ID: ");
        int id = sc.nextInt();
        Student s = findStudentById(id);
        if (s != null) {
            s.viewResult();
        } else {
            System.out.println("Student not found.");
        }
    }

    static Student findStudentById(int id) {
        for (Student s : students) {
            if (s.id == id) return s;
        }
        return null;
    }

    static void listCourses() {
        for (int i = 0; i < courseList.size(); i++) {
            System.out.println(i + ". " + courseList.get(i).name);
        }
    }

    static void seedQuestions() {
        exam.add(new Question("What is the size of int in Java?", "2 bytes", "4 bytes", "8 bytes", "Depends on OS", 'B'));
        exam.add(new Question("Which of these is not a Java keyword?", "class", "this", "void", "include", 'D'));
        exam.add(new Question("What is JVM?", "Java Virtual Machine", "Java Verified Machine", "Just Very Mad", "None", 'A'));
        exam.add(new Question("Which method is the starting point of a Java program?", "start()", "init()", "main()", "run()", 'C'));
        exam.add(new Question("Which keyword is used for inheritance?", "extends", "implements", "inherits", "instanceof", 'A'));
    }
}
