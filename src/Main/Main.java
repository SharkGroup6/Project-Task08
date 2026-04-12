package Main;

import Forms.EmployeeRegistrationForm;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            EmployeeRegistrationForm app = new EmployeeRegistrationForm();
            app.createAndShowGUI();
        });
    }
}
