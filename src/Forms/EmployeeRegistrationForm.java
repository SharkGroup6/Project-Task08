package Forms;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Date;

public class EmployeeRegistrationForm {

    JFrame frame;

    JTextField nameField, emailField;
    JPasswordField passwordField;
    JComboBox<String> departmentBox;
    JTree orgTree;
    JSpinner dateSpinner;

    public void createAndShowGUI() {
        initializeFrame();
        frame.add(createMainPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void initializeFrame() {
        frame = new JFrame("Employee Registration System");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    public JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Full Name:"));
        panel.add(createNameField());

        panel.add(new JLabel("Email:"));
        panel.add(createEmailField());

        panel.add(new JLabel("Password:"));
        panel.add(createPasswordField());

        panel.add(new JLabel("Department:"));
        panel.add(createDepartmentBox());

        panel.add(new JLabel("Date of Birth:"));
        panel.add(createDateSpinner());

        panel.add(new JLabel("Organization:"));
        panel.add(createTree());

        panel.add(createSubmitButton());
        panel.add(createClearButton());

        return panel;
    }

    public JTextField createNameField() {
        nameField = new JTextField();
        return nameField;
    }

    public JTextField createEmailField() {
        emailField = new JTextField();
        return emailField;
    }

    public JPasswordField createPasswordField() {
        passwordField = new JPasswordField();
        return passwordField;
    }

    public JComboBox<String> createDepartmentBox() {
        String[] departments = {"IT", "Finance", "HR", "Marketing"};
        departmentBox = new JComboBox<>(departments);
        return departmentBox;
    }

    public JSpinner createDateSpinner() {
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);
        return dateSpinner;
    }

    public JScrollPane createTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Company");

        DefaultMutableTreeNode it = new DefaultMutableTreeNode("IT");
        it.add(new DefaultMutableTreeNode("Development"));
        it.add(new DefaultMutableTreeNode("Support"));

        DefaultMutableTreeNode hr = new DefaultMutableTreeNode("HR");
        hr.add(new DefaultMutableTreeNode("Recruitment"));
        hr.add(new DefaultMutableTreeNode("Training"));

        root.add(it);
        root.add(hr);

        orgTree = new JTree(root);
        return new JScrollPane(orgTree);
    }

    public JButton createSubmitButton() {
        JButton btn = new JButton("Submit");
        btn.addActionListener(_ -> handleSubmit());
        return btn;
    }

    public JButton createClearButton() {
        JButton btn = new JButton("Clear");
        btn.addActionListener(_ -> clearForm());
        return btn;
    }

    public void handleSubmit() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String department = (String) departmentBox.getSelectedItem();
        Object selectedNode = orgTree.getLastSelectedPathComponent();
        Date dob = (Date) dateSpinner.getValue();

        if (!validateInput(name, email, password, selectedNode)) return;

        String summary = buildSummary(name, email, password, department, selectedNode, dob);

        JOptionPane.showMessageDialog(frame, summary);
    }

    boolean validateInput(String name, String email, String password, Object node) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || node == null) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields!");
            return false;
        }
        return true;
    }

    String buildSummary(String name, String email, String password,
                        String department, Object node, Date dob) {

        String maskedPassword = "*".repeat(password.length());

        return "Name: " + name +
                "\nEmail: " + email +
                "\nPassword: " + maskedPassword +
                "\nDepartment: " + department +
                "\nDOB: " + dob +
                "\nOrganization: " + node.toString();
    }

    public void clearForm() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        departmentBox.setSelectedIndex(0);
        orgTree.clearSelection();
        dateSpinner.setValue(new Date());
    }
}