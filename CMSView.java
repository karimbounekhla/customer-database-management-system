package exercise234CMS;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    public CMSView() {
        super("Client Management Screen");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addElements();
    }

    public void run() {
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void refreshResults(ArrayList<Client> results) {
        currResults = results;
        searchResultList.setListData(currResults.toArray());
    }

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

    public String getSearchQuery() {
        return searchField.getText();
    }

    public String getSearchCriteria() {
        return searchButtonGrp.getSelection().getActionCommand();
    }

    public void clearSearch() {
        searchField.setText("");
        DefaultListModel empty = new DefaultListModel();
        searchResultList.setModel(empty);
        clearClientDetails();
    }

        public Client getSelectedClient() {
        Object selection = searchResultList.getSelectedValue();
        if (selection == null) return null;

        return (Client) selection;
    }

    public void populateClientDetails(Client client) {
        if (client == null) return;

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

    public String getFirstNameField() {
        return firstNameField.getText();
    }

    public String getLastNameField() {
        return lastNameField.getText();
    }

    public String getPostalCodeField() {
        return postalCodeField.getText();
    }

    public String getPhoneNumberField() {
        return phoneNumberField.getText();
    }

    public String getAddressTextArea() {
        return addressTextArea.getText();
    }

    public String getClientTypeCombo() {
        return (clientTypeCombo.getSelectedIndex() == 0) ? "C" : "R";
    }

    public void clearClientDetails() {
        clientIDField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        addressTextArea.setText("");
        postalCodeField.setText("");
        phoneNumberField.setText("");
    }

    private void addElements() {
        JPanel searchPanel = createSearchPanel();
        JPanel resultPanel = createResultPanel();
        add(searchPanel, BorderLayout.WEST);
        add(resultPanel, BorderLayout.EAST);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridLayout(2, 1));
        searchPanel.setPreferredSize(new Dimension(300, searchPanel.getHeight()));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Client"));

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

    private JPanel createSearchForm() {
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

    private JPanel createResultPanel() {
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

    private JPanel resultRow(JLabel l, JTextField tf) {
        JPanel panel = new JPanel();
        panel.add(l);
        panel.add(tf);
        return panel;
    }

    public static void main(String[] args) {
        CMSView cms = new CMSView();
        cms.run();
    }


}
