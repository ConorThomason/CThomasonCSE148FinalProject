package model;

/*
 * The purpose of this class is to test the various methods that each of the classes have, their required integrations, and how they interact with each other in various situations.
 */
public class Demo {

	public static void main(String[] args) {
		//		Textbook textbook1 = new Textbook("TestBook", "Pearson", "Pearson Publishing", "5.99", "1");
//		//		TextBookBag textbookBag = new TextBookBag(10);
//		//		textbookBag.add(textbook1);
////		MasterCourseBag mCourseBag = new MasterCourseBag(20);
//////		mCourseBag.importData("DEFAULT");
//////		mCourseBag.display();
//		CourseBag courseBag = new CourseBag(1);
//		courseBag.setCoursesToGraduate("DEFAULT", "math");
		Student student1 = new Student("StudentFirst", "StudentLast", "111-111-1111", "Computer Science");
		Faculty faculty1 = new Faculty("FacultyFirst", "FacultyLast", "222-222-2222", "Prof.", "60000");
		PeopleBag personBag = new PeopleBag(2);
//		courseBag.setGrade("MAT141", "a");
//		courseBag.setGrade("MAT142", "B");
//		courseBag.setGrade("MAT200", "c");
		//Student student1 = new Student("TestFirst", "TestLast", "111-111-1111", 3.5, "math", courseBag);
////		personBag.add(student1);
////		personBag.display();
////		personBag.exportData();
	//	courseBag.display();
		Textbook textbook1 = new Textbook("Intro to Java Prog: Brief(My ProgLab Access Card)", "Liang", "Pearson", "$115.00", "9780134672977");
		Textbook textbook2 = new Textbook("Digital Design & Computer Architecture", "Harris", "Morgan Kaufmann Publishers", "$89.95", "9780123944245");
		Textbook textbook3 = new Textbook("Single Variable Calculus (w/Enh Web Assign", "Stewart", "Cengage Learning", "$157.25", "9781305524675");
		Textbook textbook4 = new Textbook("MAT 205 Discrete Mathematics (CUSTOM)", "Coe", "MCGRAW HILL CREATE (CUSTOM PUBLISHING)", "$136.25", "9781121741591");
		Textbook textbook5 = new Textbook("Linear Algebra: Models, Methods & Theory", "Tucker", "XanEdu", "$82.75", "9781506696720");
		Textbook textbook6 = new Textbook("Calculus: Multivariable Calculus", "Stewart", "Cengage Learning", "$305.75", "9781305266643");
		Textbook textbook7 = new Textbook("Intro to Ordinary Differential Equations", "Robinson", "CAMBRIDGE UNIV PRESS", "$95.75", "9780521533911");
		Textbook textbook8 = new Textbook("Elementary Linear Algebra (loose pgs)(w/ Enhanced WebAssign Access", "Larson", "Cengage Learning", "$150.00", "9781337604925");
		TextBookBag textbookBag = new TextBookBag(20);
		textbookBag.add(textbook1);
		textbookBag.add(textbook2);
		textbookBag.add(textbook3);
		textbookBag.add(textbook4);
		textbookBag.add(textbook5);
		textbookBag.add(textbook6);
		textbookBag.save();
		personBag.add(faculty1);
		personBag.add(student1);
		personBag.display();
		personBag.exportData();
		personBag.save();
		Course course1 = new Course("Computer Science College Seminar", "CSE110", null, 1);
		Course course2 = new Course("Fundamentals of Programming", "CSE118", textbookBag.getTextbook("9780134672977"), 3);
		Course course3 = new Course("Object-Oriented Programming", "CSE148", textbookBag.getTextbook("9780134672977"), 4);
		Course course4 = new Course("Data Structures and Algorithms", "CSE218", null, 3);
		Course course5 = new Course("Computer Architecture and Organization", "CSE222", textbookBag.getTextbook("9780123944245"), 3);
		Course course6 = new Course("Advanced OOP", "CSE248", null, 3);
		Course course7 = new Course("Calculus with Analytic Geometry I", "MAT141", textbookBag.getTextbook("9781305524675"), 4);
		Course course8 = new Course("Calculus with Analytic Geometry II", "MAT142", textbookBag.getTextbook("9781305524675"), 4);
		Course course9 = new Course("Discrete Mathematics", "MAT205", textbookBag.getTextbook("9780134672977"), 4);
		Course course10 = new Course("Applied Linear Algebra", "MAT210", textbookBag.getTextbook("9781506696720"), 3);
		Course course11 = new Course("Language, Logic and Proof", "MAT200", null, 3);
		Course course12 = new Course("Calculus with Analytic Geometry III", "MAT203", textbookBag.getTextbook("9781305266643"), 4);
		Course course13 = new Course("Differential Equations", "MAT204", textbookBag.getTextbook("9780521533911"), 4);
		Course course14 = new Course("Linear Algebra", "MAT206", textbookBag.getTextbook("9781337604925"), 4);
		MasterCourseBag mCourseBag = new MasterCourseBag(30);
		mCourseBag.add(course1);
		mCourseBag.add(course2);
		mCourseBag.add(course3);
		mCourseBag.add(course4);
		mCourseBag.add(course5);
		mCourseBag.add(course6);
		mCourseBag.add(course7);
		mCourseBag.add(course8);
		mCourseBag.add(course9);
		mCourseBag.add(course10);
		mCourseBag.add(course11);
		mCourseBag.add(course12);
		mCourseBag.add(course13);
		mCourseBag.add(course14);
		mCourseBag.display();
		mCourseBag.save();
//		generateData();
//		personBag.exportData();
//		Textbook textbook = mCourseBag.getCourse(4).getTextbookAssigned();
//		TextBookBag textbookBag = new TextBookBag(1);
//		textbookBag.add(textbook);
//		textbookBag.display();
		//generateData();
	}
	public static void generateData() { //This is used for the sake of testing importing/exporting, none of this is actually required. The now-generated files should suffice.
		//		//Generating test .txt files, organized into a method for my sanity
		
		
//		textbookBag.exportData();
//		textbookBag.importData("textbooksExport.txt");
//		textbookBag.display();
		
//		MasterCourseBag mCourseBag = new MasterCourseBag(10);
//		mCourseBag.add(course1);
//		mCourseBag.add(course2);
//		mCourseBag.add(course3);
//		mCourseBag.add(course4);
//		mCourseBag.add(course5);
//		mCourseBag.add(course6);
//		mCourseBag.add(course7);
//		mCourseBag.add(course8);
//		mCourseBag.add(course9);
//		mCourseBag.add(course10);
//		CourseBag courseBag = new CourseBag(20);
//		courseBag.importData("DEFAULT");
//		courseBag.add(course1);
//		courseBag.add(course2);
//		courseBag.add(course3);
//		courseBag.display();
		
//		mCourseBag.importData("DEFAULT");
		
//		mCourseBag.display();
//		mCourseBag.exportData();
//		mCourseBag.importData("masterCoursesExport.txt");
//		mCourseBag.display();
	}

	public static void generateMajors() {

	}
}
