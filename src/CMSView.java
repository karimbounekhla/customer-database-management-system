

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class representing the User Interface / GUI for the Main CMS Window.
 */
public class CMSView extends JFrame {
    private JList searchResultList;
    private ArrayList<Client> currResults;
    private JScrollPane scrollSearchPane;
    private ButtonGroup searchButtonGrp;
    private JRadioButton radioID, radioName, radioType;
    private JButton searchButton, clearSearchButton, addClientButton, saveButton, deleteButton;
    private JTextField searchField;
    private JTextField clientIDField, firstNameField, lastNameField, postalCodeField, phoneNumberField;
    private JTextArea addressTextArea;
    private JComboBox clientTypeCombo;
    private final Character[] clientTypeChoices = {'C', 'R'};

    /**
     * Constructor to set up Layout and add Elements
     */
    public CMSView() {
        super("Client Management Screen");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addElements();
    }

    /**
     * Method to display CMS View
     */
    public void run() {
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Refresh results to display list of clients found from search operation
     * @param results ArrayList containing Client objects from Search Query
     */
    public void refreshResults(ArrayList<Client> results) {
        currResults = results;
        searchResultList.setListData(currResults.toArray());
    }

    /**
     * Returns the current search result list
     * @return ArrayList of Client Objects representing latest search query
     */
    public ArrayList<Client> getCurrResults() {
        return currResults;
    }

    /**
     * Add listener to 'Search' button.
     *
     * @param e Action Listener object
     */
    public void addSearchListener(ActionListener e) {
        searchButton.addActionListener(e);
    }

    /**
     * Add listener to 'Clear Search' button.
     *
     * @param e Action Listener object
     */
    public void addClearSearchListener(ActionListener e) {
        clearSearchButton.addActionListener(e);
    }

    /**
     * Add listener to 'Add Client' button.
     *
     * @param e Action Listener object
     */
    public void addNewClientListener(ActionListener e) {
        addClientButton.addActionListener(e);
    }

    /**
     * Add listener to 'Save' button.
     *
     * @param e Action Listener object
     */
    public void addSaveListener(ActionListener e) {
        saveButton.addActionListener(e);
    }

    /**
     * Add listener to 'Delete' button.
     *
     * @param e Action Listener object
     */
    public void addDeleteListener(ActionListener e) {
        deleteButton.addActionListener(e);
    }

    /**
     * Add listener to 'Result List'.
     *
     * @param e Action Listener object
     */
    public void addResultListListener(ListSelectionListener e) {
        searchResultList.addListSelectionListener(e);
    }

    /**
     * Display an Error message.
     *
     * @param cont    parent container
     * @param message the message
     */
    public void errorMessage(Container cont, String message) {
        JOptionPane.showMessageDialog(cont, message,
                "Error!", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display a Success Message
     *
     * @param cont    parent container
     * @param message the message
     */
    public void successMessage(Container cont, String message) {
        JOptionPane.showMessageDialog(cont, message,
                "Success!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Returns user search input
     * @return search input
     */
    public String getSearchQuery() {
        return searchField.getText();
    }

    /**
     * Returns user search criteria
     * @return search criteria ('id', 'lastName' or 'clientType')
     */
    public String getSearchCriteria() {
        return searchButtonGrp.getSelection().getActionCommand();
    }

    /**
     * Clear search result list and input field
     */
    public void clearSearch() {
        searchField.setText("");
        DefaultListModel empty = new DefaultListModel();
        searchResultList.setModel(empty);
        clearClientDetails();
    }

    /**
     * Returns object of the client that is currently selected in the search
     * @return object of selected client, null if no client selected
     */
    public Client getSelectedClient() {
        Object selection = searchResultList.getSelectedValue();
        if (selection == null) return null;
        return (Client) selection;
    }

    /**
     * Populates the right panel with client details
     * @param client Client object of interest
     */
    public void populateClientDetails(Client client) {
        if (client == null) return;

        // Set text of all text fields with client information
        clientIDField.setText(client.getId()+"");
        firstNameField.setText(client.getFirstName());
        lastNameField.setText(client.getLastName());
        addressTextArea.setText(client.getAddress());
        postalCodeField.setText(client.getPostalCode());
        phoneNumberField.setText(client.getPhoneNumber());

        if (client.getClientType().equals("C")) {
            clientTypeCombo.setSelectedIndex(0);
        } else {
            clientTypeCombo.setSelectedIndex(1);
        }
    }

    /**
     * Get value of the first name field (right panel - client details)
     * @return value in first name field
     */
    public String getFirstNameField() {
        return firstNameField.getText();
    }

    /**
     * Get value of the last name field (right panel - client details)
     * @return value in last name field
     */
    public String getLastNameField() {
        return lastNameField.getText();
    }

    /**
     * Get value of the postal code field (right panel - client details)
     * @return value in postal code field
     */
    public String getPostalCodeField() {
        return postalCodeField.getText();
    }

    /**
     * Get value of the phone number field (right panel - client details)
     * @return value in phone number field
     */
    public String getPhoneNumberField() {
        return phoneNumberField.getText();
    }

    /**
     * Get value of the address field (right panel - client details)
     * @return value in address field
     */
    public String getAddressTextArea() {
        return addressTextArea.getText();
    }

    /**
     * Get value of the selected client type (right panel - client details)
     * @return selected client type from drop-down menu ('C' or 'R')
     */
    public String getClientTypeCombo() {
        return (clientTypeCombo.getSelectedIndex() == 0) ? "C" : "R";
    }

    /**
     * Clear all client details from right panel
     */
    public void clearClientDetails() {
        clientIDField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        addressTextArea.setText("");
        postalCodeField.setText("");
        phoneNumberField.setText("");
    }

    /**
     * Helper method to add Left and Right Panels
     */
    private void addElements() {
        JPanel searchPanel = createSearchPanel();
        JPanel resultPanel = createResultPanel();
        add(searchPanel, BorderLayout.WEST);
        add(resultPanel, BorderLayout.EAST);
    }

    /**
     * Helper method to create the Search Panel
     * @return JPanel of the search UI components
     */
    private JPanel createSearchPanel() {
        // Search Panel uses a GridLayout with 2 rows and 1 column
        JPanel searchPanel = new JPanel(new GridLayout(2, 1));
        searchPanel.setPreferredSize(new Dimension(300, searchPanel.getHeight()));

        // Set titled border
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Client"));

        // Search Result panel consists of a JList (to display Objects) in a JScrollPane
        JPanel searchResultsPanel = new JPanel();
        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.PAGE_AXIS));

        searchResultList = new JList();
        scrollSearchPane = new JScrollPane(searchResultList);

        searchResultsPanel.add(new JLabel("Search Results (Select entry to view)"));
        searchResultsPanel.add(scrollSearchPane);

        searchPanel.add(createSearchForm());
        searchPanel.add(searchResultsPanel);

        return searchPanel;
    }

    /**
     * Helper method to create Search Form UI Components
     * @return search form JPanel
     */
    private JPanel createSearchForm() {
        // Search Form uses a GriBagLayout
        JPanel searchForm = new JPanel();
        searchForm.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 5, 5, 5);
        searchForm.add(new JLabel("Select Type of Search to be Performed: "), gc);


        searchButtonGrp = new ButtonGroup();
        radioID = new JRadioButton("Client ID");
        radioID.setSelected(true);
        radioID.setActionCommand("id");
        radioName = new JRadioButton("Last Name");
        radioName.setActionCommand("lastName");
        radioType = new JRadioButton("Client Type");
        radioType.setActionCommand("clientType");
        searchButtonGrp.add(radioID);
        searchButtonGrp.add(radioName);
        searchButtonGrp.add(radioType);

        // Set UI components along vertical axis
        gc.gridy = 1;
        searchForm.add(radioID, gc);
        gc.gridy = 2;
        searchForm.add(radioName, gc);
        gc.gridy = 3;
        searchForm.add(radioType, gc);
        gc.gridy = 4;
        searchForm.add(new JLabel("Enter the Search Parameter below:"), gc);
        gc.gridy = 5;
        searchField = new JTextField(12);
        searchForm.add(searchField, gc);
        gc.gridy = 6;
        gc.gridx = 0;
        searchButton = new JButton("Search");
        clearSearchButton = new JButton("Clear Search");
        searchForm.add(searchButton, gc);
        gc.gridy = 7;
        searchForm.add(clearSearchButton, gc);

        return searchForm;
    }

    /**
     * Helper method to create the client details panel
     * @return client detail JPanel
     */
    private JPanel createResultPanel() {
        // Client details panel uses a GridLayout with 10 rows and 1 column
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(10, 1));
        resultPanel.setPreferredSize(new Dimension(400, resultPanel.getHeight()));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Client Information"));

        clientIDField = new JTextField(10);
        clientIDField.setEnabled(false);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        addressTextArea = new JTextArea(3, 20);
        addressTextArea.setLineWrap(true);
        JPanel addressRow = new JPanel();
        addressRow.add(new JLabel("Address: "));
        addressRow.add(addressTextArea);
        postalCodeField = new JTextField(10);
        phoneNumberField = new JTextField(20);
        clientTypeCombo = new JComboBox(clientTypeChoices);
        JPanel typeRow = new JPanel();
        typeRow.add(new JLabel("Client Type: "));
        typeRow.add(clientTypeCombo);
        saveButton = new JButton("Save");
        deleteButton = new JButton("Delete Client");
        addClientButton = new JButton("Add New Client");

        resultPanel.add(resultRow(new JLabel("Client ID: "), clientIDField));
        resultPanel.add(resultRow(new JLabel("First Name: "), firstNameField));
        resultPanel.add(resultRow(new JLabel("Last Name: "), lastNameField));
        resultPanel.add(addressRow);
        resultPanel.add(resultRow(new JLabel("Postal Code: "), postalCodeField));
        resultPanel.add(resultRow(new JLabel("Phone Number: "), phoneNumberField));
        resultPanel.add(typeRow);
        resultPanel.add(saveButton);
        resultPanel.add(deleteButton);
        resultPanel.add(addClientButton);

        return resultPanel;
    }

    /**
     * Helper method to create a panel for each row, containing a label and a text field
     * @param l label of the text input field
     * @param tf Text Field object
     * @return JPanel of that row
     */
    private JPanel resultRow(JLabel l, JTextField tf) {
        JPanel panel = new JPanel();
        panel.add(l);
        panel.add(tf);
        return panel;
    }
}
