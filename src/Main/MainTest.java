package Main;

import Forms.EmployeeRegistrationForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class MainTest {

    EmployeeRegistrationForm form;

    @BeforeEach
    void setUp() {
        form = new EmployeeRegistrationForm();
        form.initializeFrame();
        form.createAndShowGUI();
    }

    @AfterEach
    void tearDown() {
        if (form != null && form.frame != null) {
            form.frame.dispose();
        }
    }

    @Test
    void testValidateInputThrowsExactly() {
        assertThrowsExactly(NullPointerException.class, () -> {
            form.validateInput(null, null, null, null);
        });
    }

    @Test
    void testBuildSummaryFields() {
        String summary = form.buildSummary("Louis", "test@mail.com", "secret",
                "IT", "Development", new Date());
        
        assertFalse(summary.isEmpty(), "Summary should not be empty");
    }

    @Test
    void testMaskedPassword() {
        String summary = form.buildSummary("Louis", "mail", "secret",
                "IT", "Development", new Date());


        assertFalse(!summary.contains("*****"), "Password should be masked");
    }

    @RepeatedTest(4)
    void testClearFormRepeated() {
        form.clearForm();
        assertFalse(!form.nameField.getText().isEmpty(), "Name field should be empty");
        assertFalse(!form.emailField.getText().isEmpty(), "Email field should be empty");
        assertFalse(!(new String(form.passwordField.getPassword()).isEmpty()), "Password field should be empty");
    }
}

