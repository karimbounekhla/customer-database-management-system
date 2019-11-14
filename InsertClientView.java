package exercise234CMS;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * The 'View' class for the Insert Node GUI. This container is used to add a client to the
 * database.
 */
public class InsertClientView extends JFrame {
    // Instantiate all objects needed for this frame
    private JTextField firstNameField, lastNameField, addressField, postalCodeField, phoneNumberField;
    private JComboBox clientTypeCombo;
    private final Character[] clientTypeChoices = {'C', 'R'};
    private JButton insert, returnMain;

    /**
     * Instantiates a new Insert view object.
     */
    public InsertClientView() {
        super("Insert Client");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addElements();
    }

    /**
     * Display the frame when a user wants to insert a client.
     */
    public void run() {
        setSize(300, 100);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Clears the fields.
     */
    public void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        postalCodeField.setText("");
        phoneNumberField.setText("");
    }

    /**
     * Add listener to 'Add Client' button.
     *
     * @param e Action Listener object
     */
    public void addInsertListener(ActionListener e) {
        insert.addActionListener(e);
    }

    /**
     * Add listener to 'Cancel' button.
     *
     * @param e Action Listener object
     */
    public void addCancelListener(ActionListener e) {
        returnMain.addActionListener(e);
    }

    /**
     * Gets First Name field.
     *
     * @return the First Name field
     */
    public String getFirstNameField() {
        return firstNameField.getText();
    }

    /**
     * Gets Last Name field.
     *
     * @return the Last Name field
     */
    public String getLastNameField() {
        return lastNameField.getText();
    }

    /**
     * Gets Address Field.
     *
     * @return the Address field
     */
    public String getAddressField() {
        return addressField.getText();
    }

    /**
     * Gets Postal Code Field.
     *
     * @return the Postal Code field
     */
    public String getPostalCode() {
        return postalCodeField.getText();
    }

    /**
     * Gets Phone Number Field.
     *
     * @return the Number field
     */
    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    public int getClientType() {
        return clientTypeCombo.getSelectedIndex();
    }

    /**
     * Helper method to add each row to the main container
     */
    private void addElements() {
        add(new JLabel("Add a Client to the Database"));
        add(firstRow());
        add(secondRow());
        add(thirdRow());
        add(buttonRow());
    }

    /**
     * Helper method to populate the first input row with JFrame objects.
     * @return JPanel container object
     */
    private JPanel firstRow() {
        JPanel firstRow = new JPanel();

        firstNameField = new JTextField();
        firstNameField.setColumns(20);
        lastNameField = new JTextField();
        lastNameField.setColumns(20);
        firstRow.add(new JLabel("First Name: "));
        firstRow.add(firstNameField);
        firstRow.add(new JLabel(("Last Name: ")));
        firstRow.add(lastNameField);

        return firstRow;
    }

    /**
     * Helper method to populate the second input row with JFrame objects.
     * @return JPanel container object
     */
    private JPanel secondRow() {
        JPanel secondRow = new JPanel();

        addressField = new JTextField(50);
        postalCodeField = new JTextField(10);
        secondRow.add(new JLabel("Address: "));
        secondRow.add(addressField);
        secondRow.add(new JLabel(("Postal Code: ")));
        secondRow.add(postalCodeField);

        return secondRow;
    }

    private JPanel thirdRow() {
        JPanel thirdRow = new JPanel();

        phoneNumberField = new JTextField(10);
        clientTypeCombo = new JComboBox(clientTypeChoices);
        thirdRow.add(new JLabel("Phone Number: "));
        thirdRow.add(phoneNumberField);
        thirdRow.add(new JLabel("Client Type: "));
        thirdRow.add(clientTypeCombo);

        return thirdRow;
    }

    /**
     * Helper method to populate the last row (submit button) with JFrame objects.
     * @return JPanel container object
     */
    private JPanel buttonRow() {
        JPanel buttonRow = new JPanel();

        insert = new JButton("Insert Client");
        returnMain = new JButton(("Cancel"));
        buttonRow.add(insert);
        buttonRow.add(returnMain);

        return buttonRow;
    }

    public static void main(String[] args) {
        InsertClientView icv = new InsertClientView();
        icv.run();
    }
}
