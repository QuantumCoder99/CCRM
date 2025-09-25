import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class CCRMGui extends JFrame {
	private final Map<String, Student> students;
	private final Map<String, Course> courses;

	private final JTable studentsTable;
	private final DefaultTableModel studentsModel;

	private final JTable coursesTable;
	private final DefaultTableModel coursesModel;

	public CCRMGui() {
		super("Campus Course & Records Manager (CCRM)");
		this.students = new HashMap<>();
		this.courses = new HashMap<>();
		FileManager.ensureDirectories();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Students", buildStudentsPanel());
		tabs.addTab("Courses", buildCoursesPanel());
		tabs.addTab("Grades", buildGradesPanel());
		tabs.addTab("Files", buildFilesPanel());
		tabs.addTab("Reports", buildReportsPanel());
		setContentPane(tabs);

		studentsModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "GPA"}, 0) {
			@Override public boolean isCellEditable(int row, int column) { return false; }
		};
		studentsTable = new JTable(studentsModel);

		coursesModel = new DefaultTableModel(new Object[]{"ID", "Name", "Instructor", "Credits", "Enrolled"}, 0) {
			@Override public boolean isCellEditable(int row, int column) { return false; }
		};
		coursesTable = new JTable(coursesModel);
	}

	private JPanel buildStudentsPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel form = new JPanel(new GridLayout(2, 5, 8, 8));
		JTextField id = new JTextField();
		JTextField first = new JTextField();
		JTextField last = new JTextField();
		JTextField email = new JTextField();
		JButton add = new JButton("Add Student");
		form.add(new JLabel("ID"));
		form.add(new JLabel("First Name"));
		form.add(new JLabel("Last Name"));
		form.add(new JLabel("Email"));
		form.add(new JLabel(""));
		form.add(id); form.add(first); form.add(last); form.add(email); form.add(add);

		add.addActionListener((ActionEvent e) -> {
			String sid = id.getText().trim();
			if (sid.isEmpty() || students.containsKey(sid)) {
				JOptionPane.showMessageDialog(this, "Invalid or duplicate ID");
				return;
			}
			Student s = new Student(sid, first.getText().trim(), last.getText().trim(), email.getText().trim());
			students.put(sid, s);
			refreshStudents();
			id.setText(""); first.setText(""); last.setText(""); email.setText("");
		});

		panel.add(form, BorderLayout.NORTH);

		JScrollPane tableWrap = new JScrollPane();
		tableWrap.setViewportView(new JTableProxy(() -> studentsTable));
		panel.add(tableWrap, BorderLayout.CENTER);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton update = new JButton("Update Selected");
		JButton delete = new JButton("Delete Selected");
		actions.add(update); actions.add(delete);
		panel.add(actions, BorderLayout.SOUTH);

		update.addActionListener(e -> updateSelectedStudent());
		delete.addActionListener(e -> deleteSelectedStudent());

		return panel;
	}

	private JPanel buildCoursesPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel form = new JPanel(new GridLayout(2, 6, 8, 8));
		JTextField id = new JTextField();
		JTextField name = new JTextField();
		JTextField desc = new JTextField();
		JTextField credits = new JTextField();
		JTextField max = new JTextField();
		JButton add = new JButton("Add Course");
		form.add(new JLabel("ID"));
		form.add(new JLabel("Name"));
		form.add(new JLabel("Description"));
		form.add(new JLabel("Credits"));
		form.add(new JLabel("Max Students"));
		form.add(new JLabel(""));
		form.add(id); form.add(name); form.add(desc); form.add(credits); form.add(max); form.add(add);

		add.addActionListener(e -> {
			String cid = id.getText().trim();
			if (cid.isEmpty() || courses.containsKey(cid)) {
				JOptionPane.showMessageDialog(this, "Invalid or duplicate Course ID");
				return;
			}
			int cr = parseIntOrDefault(credits.getText().trim(), -1);
			int mx = parseIntOrDefault(max.getText().trim(), -1);
			if (cr < 0 || mx <= 0) {
				JOptionPane.showMessageDialog(this, "Enter valid credits and max students");
				return;
			}
			Course c = new Course(cid, name.getText().trim(), desc.getText().trim(), cr, mx);
			courses.put(cid, c);
			refreshCourses();
			id.setText(""); name.setText(""); desc.setText(""); credits.setText(""); max.setText("");
		});

		panel.add(form, BorderLayout.NORTH);

		JScrollPane tableWrap = new JScrollPane();
		tableWrap.setViewportView(new JTableProxy(() -> coursesTable));
		panel.add(tableWrap, BorderLayout.CENTER);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton update = new JButton("Update Selected");
		JButton delete = new JButton("Delete Selected");
		JButton instructor = new JButton("Assign Instructor");
		actions.add(update); actions.add(delete); actions.add(instructor);
		panel.add(actions, BorderLayout.SOUTH);

		update.addActionListener(e -> updateSelectedCourse());
		delete.addActionListener(e -> deleteSelectedCourse());
		instructor.addActionListener(e -> assignInstructor());

		return panel;
	}

	private JPanel buildGradesPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 16, 16));

		JTextField sid = new JTextField();
		JTextField cid = new JTextField();
		JTextField grade = new JTextField();
		JButton record = new JButton("Record/Update Grade");

		panel.add(labeled("Student ID", sid));
		panel.add(labeled("Course ID", cid));
		panel.add(labeled("Grade (0-100)", grade));
		panel.add(record);

		record.addActionListener(e -> {
			Student s = students.get(sid.getText().trim());
			if (s == null) { JOptionPane.showMessageDialog(this, "Student not found"); return; }
			String courseId = cid.getText().trim();
			if (!s.getEnrolledCourses().contains(courseId)) {
				JOptionPane.showMessageDialog(this, "Student not enrolled in course");
				return;
			}
			double g = parseDoubleOrDefault(grade.getText().trim(), -1);
			if (g < 0 || g > 100) { JOptionPane.showMessageDialog(this, "Enter 0-100"); return; }
			s.setGrade(courseId, g);
			refreshStudents();
		});

		JButton view = new JButton("View Student Grades");
		panel.add(view);
		view.addActionListener(e -> viewStudentGrades());

		JButton enroll = new JButton("Enroll Student in Course");
		panel.add(enroll);
		enroll.addActionListener(e -> enrollStudentInCourse());

		JButton unenroll = new JButton("Unenroll Student from Course");
		panel.add(unenroll);
		unenroll.addActionListener(e -> unenrollStudentFromCourse());

		return panel;
	}

	private JPanel buildFilesPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JTextField studentsCsv = new JTextField(18);
		JTextField coursesCsv = new JTextField(18);
		JButton exportS = new JButton("Export Students CSV");
		JButton exportC = new JButton("Export Courses CSV");
		JButton backup = new JButton("Create Backup");
		JButton restore = new JButton("Restore");
		panel.add(labeled("Students CSV filename", studentsCsv));
		panel.add(exportS);
		exportS.addActionListener(e -> FileManager.exportStudentsToCSV(students, orDefault(studentsCsv.getText(), "students.csv")));
		panel.add(labeled("Courses CSV filename", coursesCsv));
		panel.add(exportC);
		exportC.addActionListener(e -> FileManager.exportCoursesToCSV(courses, orDefault(coursesCsv.getText(), "courses.csv")));
		panel.add(backup);
		backup.addActionListener(e -> FileManager.createBackup(students, courses));
		panel.add(restore);
		restore.addActionListener(e -> restoreFromBackup());
		return panel;
	}

	private JPanel buildReportsPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JTextArea area = new JTextArea();
		area.setEditable(false);
		JButton refresh = new JButton("Refresh Report");
		panel.add(new JScrollPane(area), BorderLayout.CENTER);
		panel.add(refresh, BorderLayout.SOUTH);
		refresh.addActionListener(e -> area.setText(generateReport()));
		return panel;
	}

	private JPanel labeled(String label, JComponent component) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(label), BorderLayout.NORTH);
		p.add(component, BorderLayout.CENTER);
		return p;
	}

	private int parseIntOrDefault(String text, int def) {
		try { return Integer.parseInt(text); } catch (Exception e) { return def; }
	}
	private double parseDoubleOrDefault(String text, double def) {
		try { return Double.parseDouble(text); } catch (Exception e) { return def; }
	}
	private String orDefault(String t, String d) { return (t == null || t.trim().isEmpty()) ? d : t.trim(); }

	private void refreshStudents() {
		studentsModel.setRowCount(0);
		for (Student s : students.values()) {
			studentsModel.addRow(new Object[]{
				s.getStudentId(), s.getFullName(), s.getEmail(), String.format("%.2f", s.calculateGPA())
			});
		}
	}

	private void refreshCourses() {
		coursesModel.setRowCount(0);
		for (Course c : courses.values()) {
			coursesModel.addRow(new Object[]{
				c.getCourseId(), c.getCourseName(), c.getInstructor(), c.getCredits(), c.getEnrolledCount() + "/" + c.getMaxStudents()
			});
		}
	}

	private void updateSelectedStudent() {
		int row = studentsTable.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student first"); return; }
		String id = Objects.toString(studentsModel.getValueAt(row, 0), "");
		Student s = students.get(id);
		if (s == null) return;
		String first = JOptionPane.showInputDialog(this, "First Name", s.getFirstName());
		if (first != null) s.setFirstName(first.trim());
		String last = JOptionPane.showInputDialog(this, "Last Name", s.getLastName());
		if (last != null) s.setLastName(last.trim());
		String email = JOptionPane.showInputDialog(this, "Email", s.getEmail());
		if (email != null) s.setEmail(email.trim());
		refreshStudents();
	}

	private void deleteSelectedStudent() {
		int row = studentsTable.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student first"); return; }
		String id = Objects.toString(studentsModel.getValueAt(row, 0), "");
		students.remove(id);
		for (Course c : courses.values()) { c.removeStudent(id); }
		refreshStudents();
		refreshCourses();
	}

	private void updateSelectedCourse() {
		int row = coursesTable.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, "Select a course first"); return; }
		String id = Objects.toString(coursesModel.getValueAt(row, 0), "");
		Course c = courses.get(id);
		if (c == null) return;
		String name = JOptionPane.showInputDialog(this, "Course Name", c.getCourseName());
		if (name != null) c.setCourseName(name.trim());
		String desc = JOptionPane.showInputDialog(this, "Description", c.getDescription());
		if (desc != null) c.setDescription(desc.trim());
		String credits = JOptionPane.showInputDialog(this, "Credits", Integer.toString(c.getCredits()));
		if (credits != null) c.setCredits(parseIntOrDefault(credits.trim(), c.getCredits()));
		String max = JOptionPane.showInputDialog(this, "Max Students", Integer.toString(c.getMaxStudents()));
		if (max != null) c.setMaxStudents(parseIntOrDefault(max.trim(), c.getMaxStudents()));
		refreshCourses();
	}

	private void deleteSelectedCourse() {
		int row = coursesTable.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, "Select a course first"); return; }
		String id = Objects.toString(coursesModel.getValueAt(row, 0), "");
		courses.remove(id);
		for (Student s : students.values()) { s.unenrollFromCourse(id); }
		refreshCourses();
		refreshStudents();
	}

	private void assignInstructor() {
		int row = coursesTable.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, "Select a course first"); return; }
		String id = Objects.toString(coursesModel.getValueAt(row, 0), "");
		Course c = courses.get(id);
		if (c == null) return;
		String name = JOptionPane.showInputDialog(this, "Instructor Name", Optional.ofNullable(c.getInstructor()).orElse(""));
		if (name != null) c.setInstructor(name.trim());
		refreshCourses();
	}

	private void enrollStudentInCourse() {
		String sid = JOptionPane.showInputDialog(this, "Student ID");
		if (sid == null) return;
		String cid = JOptionPane.showInputDialog(this, "Course ID");
		if (cid == null) return;
		Student s = students.get(sid.trim());
		Course c = courses.get(cid.trim());
		if (s == null || c == null) { JOptionPane.showMessageDialog(this, "Student or Course not found"); return; }
		if (c.isFull()) { JOptionPane.showMessageDialog(this, "Course is full"); return; }
		if (s.getEnrolledCourses().contains(cid)) { JOptionPane.showMessageDialog(this, "Already enrolled"); return; }
		s.enrollInCourse(cid);
		c.addStudent(sid);
		refreshCourses();
	}

	private void unenrollStudentFromCourse() {
		String sid = JOptionPane.showInputDialog(this, "Student ID");
		if (sid == null) return;
		String cid = JOptionPane.showInputDialog(this, "Course ID");
		if (cid == null) return;
		Student s = students.get(sid.trim());
		Course c = courses.get(cid.trim());
		if (s == null || c == null) { JOptionPane.showMessageDialog(this, "Student or Course not found"); return; }
		s.unenrollFromCourse(cid);
		c.removeStudent(sid);
		refreshCourses();
		refreshStudents();
	}

	private void viewStudentGrades() {
		String sid = JOptionPane.showInputDialog(this, "Student ID");
		if (sid == null) return;
		Student s = students.get(sid.trim());
		if (s == null) { JOptionPane.showMessageDialog(this, "Student not found"); return; }
		StringBuilder sb = new StringBuilder();
		sb.append("Student: ").append(s.getFullName()).append("\n\n");
		if (s.getGrades().isEmpty()) {
			sb.append("No grades recorded.");
		} else {
			for (Map.Entry<String, Double> e : s.getGrades().entrySet()) {
				Course c = courses.get(e.getKey());
				String nm = c != null ? c.getCourseName() : "Unknown";
				sb.append(String.format("%s (%s): %.2f\n", nm, e.getKey(), e.getValue()));
			}
			sb.append(String.format("\nGPA: %.2f", s.calculateGPA()));
		}
		JOptionPane.showMessageDialog(this, sb.toString());
	}

	private void restoreFromBackup() {
		FileManager.listBackupFiles();
		String sFile = JOptionPane.showInputDialog(this, "Students backup filename");
		if (sFile == null) return;
		String cFile = JOptionPane.showInputDialog(this, "Courses backup filename");
		if (cFile == null) return;
		Map<String, Student> s = FileManager.loadStudentsBackup(sFile.trim());
		Map<String, Course> c = FileManager.loadCoursesBackup(cFile.trim());
		if (!s.isEmpty()) { this.students.clear(); this.students.putAll(s); }
		if (!c.isEmpty()) { this.courses.clear(); this.courses.putAll(c); }
		refreshStudents();
		refreshCourses();
	}

	private String generateReport() {
		StringBuilder sb = new StringBuilder();
		sb.append("Total Students: ").append(students.size()).append('\n');
		sb.append("Total Courses: ").append(courses.size()).append('\n');
		int totalEnroll = 0;
		for (Course c : courses.values()) totalEnroll += c.getEnrolledCount();
		sb.append("Total Enrollments: ").append(totalEnroll).append('\n');
		if (!courses.isEmpty()) {
			double avg = (double) totalEnroll / courses.size();
			sb.append(String.format("Average Enrollment per Course: %.2f\n", avg));
		}
		// GPA stats
		double totalGpa = 0, hi = -1, lo = 101; int n = 0; String top = "";
		for (Student s : students.values()) {
			double g = s.calculateGPA();
			if (g > 0) { n++; totalGpa += g; if (g > hi) { hi = g; top = s.getFullName(); } if (g < lo) lo = g; }
		}
		if (n > 0) {
			sb.append(String.format("Average GPA: %.2f\nHighest GPA: %.2f (%s)\nLowest GPA: %.2f\n", totalGpa / n, hi, top, lo));
		} else {
			sb.append("No GPA data yet.\n");
		}
		return sb.toString();
	}

	// Helper to embed a table created after constructor wires columns
	private static class JTableProxy extends JComponent {
		private final java.util.function.Supplier<JTable> supplier;
		JTableProxy(java.util.function.Supplier<JTable> supplier) { this.supplier = supplier; }
		@Override public Dimension getPreferredSize() { return new Dimension(100, 100); }
		@Override protected void paintComponent(Graphics g) { /* no-op */ }
		@Override public void addNotify() {
			super.addNotify();
			if (getComponentCount() == 0) {
				JTable table = supplier.get();
				setLayout(new BorderLayout());
				add(new JScrollPane(table), BorderLayout.CENTER);
			}
		}
	}
} 