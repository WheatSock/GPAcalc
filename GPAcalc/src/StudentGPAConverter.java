// Documentation comment describing the project, including purpose, programmers, and last modification date.
	/************************************************************************* 
	*  Project 1 for IT501 
	*  Student GPA Converter Program 
	* 
	*  Programmer: Group1
	*  Members: Emma Acevedo, Ian Kinney, Angelica Ochoa
	*  
	*  Last modified: September 10, 2024
	* 
	*  Purpose:  
	*  The program is referencing to the input from "grades.txt" file, which
	*  contains the grades of 5 students.
	*  
	*  The Student grades are converted from percentage grade to equivalent
	*  letter grade per course.
	*  
	*  The Student letter grade is converted to equivalent GPA per course.
	*  
	*  The system displays the output of the Student Average GPA grade.
	***********************************************************************/ 

// BufferedReader and FileReader for reading files.
import java.io.BufferedReader;
import java.io.FileReader;

// IOException for handling input/output exceptions.
import java.io.IOException;

// HashMap and Map for storing and managing student grade data.
import java.util.HashMap;
import java.util.Map;

// Defines a public class named StudentGPAConverter. This class contains methods and the main function to process and convert student grades.
public class StudentGPAConverter {
	
	// Defines a public static method numericToLetterGrade that converts a numeric grade to a letter grade.
    public static String numericToLetterGrade(double numericGrade) {
    	
    	// Converts numeric grades to letter grades based on predefined ranges.
        if (numericGrade > 94) return "A";
        if (numericGrade >= 90) return "A-";
        if (numericGrade >= 87) return "B+";
        if (numericGrade >= 84) return "B";
        if (numericGrade >= 80) return "B-";
        if (numericGrade >= 77) return "C+";
        if (numericGrade >= 74) return "C";
        if (numericGrade >= 70) return "C-";
        if (numericGrade >= 67) return "D+";
        if (numericGrade >= 64) return "D-";
        return "F";
    }

    // Defines a public static method letterGradeToGPA that converts a letter grade to a GPA value.
    public static double letterGradeToGPA(String letterGrade) {
    	
    	// Maps letter grades to GPA values using a switch statement.
        switch (letterGrade) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "D-": return 0.7;
            default: return 0.0;
        }
    }

    // Defines a public static method calculateGPA that calculates the average GPA for a student given an array of grade strings.
    public static double calculateGPA(String[] grades) {
    	
    	// Initializes variables to accumulate the total GPA and count the number of grades.
        double totalGPA = 0.0;
        int count = 0;

        // Iterates over each grade string and splits it into parts to extract the numeric grade.
        for (String grade : grades) {
            
        	// Extract numeric grade from each grade string
            String[] parts = grade.split(":");
         
            // Check if parts array has at least 2 elements. 
            // Checks if the split parts array is valid. If not, prints the problematic grade and continues with the next one.
            if (parts.length < 2) {
                System.err.print(grade);
                
                // Skip to the next grade
                continue; 
            }

            // Attempts to parse the numeric grade from the second part of the split string, cleaning it up first.
            try {
                double numericGrade = Double.parseDouble(parts[1].replace(";", "").trim());

                // Converts the numeric grade to a letter grade and then to a GPA value.
                String letterGrade = numericToLetterGrade(numericGrade);
                double gpa = letterGradeToGPA(letterGrade);

                // Accumulates the GPA and increments the count. 
                totalGPA += gpa;
                count++;
            
            // Handling any NumberFormatException if the numeric grade parsing fails.    
            } catch (NumberFormatException e) {
                System.err.println(" " + parts[1]);
            }
        }

        // Calculates and returns the average GPA if at least one grade was processed; otherwise, returns 0.0.
        return count > 0 ? totalGPA / count : 0.0;
    }

    // Defines a public static method readStudentData to read student data from a file and return it in a map.
    public static Map<String, String[]> readStudentData(String filename) throws IOException {
        
    	// Initializes necessary objects for file reading and data storage, including a HashMap for student grades, 
    	// a BufferedReader for file input, and variables for tracking the current student and accumulating grades.
    	Map<String, String[]> studentGrades = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        String currentStudent = null;
        StringBuilder gradesBuilder = new StringBuilder();

        // Reads each line of the file. If the line contains course information, it appends it to gradesBuilder. 
        // If it contains a student name, it updates the map with the previous student’s data and starts accumulating grades for the new student.
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.contains("MBA") || line.contains("IT")) {
                gradesBuilder.append(line).append(";");
            } else {
                if (currentStudent != null) {
                    studentGrades.put(currentStudent, gradesBuilder.toString().split(";"));
                }
                currentStudent = line;
                gradesBuilder = new StringBuilder();
            }
        }

        // Handles the last student’s data after finishing the file read.
        if (currentStudent != null) {
            studentGrades.put(currentStudent, gradesBuilder.toString().split(";"));
        }

        // Closes the BufferedReader and returns the studentGrades map.
        reader.close();
        return studentGrades;
    }

    // Defines the main method. Initializes the filename variable to specify the path to the input file.
    public static void main(String[] args) {
    	
    	// Path to reference TXT file, student grades.
    	String filename = "grades.txt"; 

        // Attempts to read student data from the specified file.
        try {
            Map<String, String[]> studentGrades = readStudentData(filename);

            // Iterates over the student data map, calculates the GPA for each student, and prints it.
            for (Map.Entry<String, String[]> entry : studentGrades.entrySet()) {
                String studentName = entry.getKey();
                String[] grades = entry.getValue();
                double gpa = calculateGPA(grades);
                System.out.printf("%s's GPA: %.2f%n", studentName, gpa);
            }
        
        // Catches and handles IOException if file reading fails, and ends the main method and the StudentGPAConverter class.
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}