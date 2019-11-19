import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Controller class for the Application. Controls the data flow between the model and the view,
 * as well as updating the view, while keeping them separate.
 * This class implements all listener classes for the CMS and Insert Client View.
 */
public class CMSController {
    private CMSView theCMSView;
    private InsertClientView theInsertView;
    private CMSModel theModel;
    private InputVerify verifyInput;

    /**
     * Constructor used to set references to the Model and view Objects
     * @param cmsv the Customer Management System View
     * @param icv the Insert Customer View
     * @param cmsm the Customer Management System Model
     */
    public CMSController(CMSView cmsv, InsertClientView icv, CMSModel cmsm) {
        theCMSView = cmsv;
        theInsertView = icv;
        theModel = cmsm;
        verifyInput = new InputVerify(theCMSView);
        addListeners();
    }

    /**
     * Displays CMS view
     */
    public void run() {
        theCMSView.run();
    }

    /**
     * Searches for clients by 'id' and displays the result.
     * @param criteria search criteria (id, lastName or type)
     * @param query search query
     */
    private void searchDB(String criteria, String query) {
        // Check that input is valid
        boolean validQuery;
        switch (criteria) {
            case "id":
                validQuery = !verifyInput.isInvalidID(query);
                break;
            case "lastName":
                validQuery = !verifyInput.isInvalidName("placeholder", query);
                break;
            case "clientType":
                validQuery = !verifyInput.isInvalidType(query);
                break;
            default:
                validQuery = false;
        }

        // If query valid, fetch result list from Model and display on view (if any results found)
        if (validQuery) {
            ArrayList<Client> searchResults = theModel.getSearchResults(criteria, query);
            if (searchResults != null) {
                theCMSView.refreshResults(searchResults);
            }
        }
    }

    /**
     * Checks that all client information is valid.
     * First/Last name < 20 characters
     * Address < 50 characters
     * Postal Code in format A1A 1A1 (where A is a letter, 1 is a digit)
     * Phone number in format 123-456-7890
     * Customer type - either 'C' or 'R'
     * @return True if client has any invalid input (name, address, postal code, phone num or type)
     */
    private boolean invalidClient(String fName, String lName, String address,
                                  String phone, String postalCode, String type) {
        if (verifyInput.isInvalidName(fName, lName)
            || verifyInput.isInvalidAddress(address)
            || verifyInput.isInvalidPhone(phone)
            || verifyInput.isInvalidPostalCode(postalCode)
            || verifyInput.isInvalidType(type)) {
            return true;
        }
        return false;
    }

    /**
     * Helper method to add Listeners to Java Swing objects
     */
    private void addListeners() {
        theCMSView.addDeleteListener(new deleteListener());
        theCMSView.addClearSearchListener(new clearSearchListener());
        theCMSView.addNewClientListener(new addClientListener());
        theCMSView.addResultListListener(new resultDetailsListener());
        theCMSView.addSaveListener(new saveListener());
        theCMSView.addSearchListener(new searchListener());
        theInsertView.addInsertListener(new insertListener());
        theInsertView.addCancelListener(new cancelListener());
    }

    ////////////////////////////////////////////////////
    ///////////// Listeners for CMS Frame /////////////
    ////////////////////////////////////////////////////

    /**
     * Listener class for the 'Search' Button
     */
    private class searchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Take in search criteria (ID, Last Name or Client Type) from radio button selection
            String searchCriteria = theCMSView.getSearchCriteria();
            String query = theCMSView.getSearchQuery();
            searchDB(searchCriteria, query);
        }
    }

    /**
     * Listener class for the 'Clear Search' Button
     */
    private class clearSearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            theCMSView.clearSearch();
        }
    }

    /**
     * Listener class for the 'Add New Client' Button
     */
    private class addClientListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Clears inputs and displays Insert View
            theInsertView.clearFields();
            theInsertView.run();
        }
    }

    /**
     * Listener Class for the 'Save' Button
     */
    private class saveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Ensures that a searched client is selected
            if (theCMSView.getSelectedClient() == null) {
                theCMSView.errorMessage(theCMSView, "No Client Selected.");
                return;
            }

            // Take inputs from Text Fields
            String newFirstName = theCMSView.getFirstNameField();
            String newLastName = theCMSView.getLastNameField();
            String newAddress = theCMSView.getAddressTextArea();
            String newPostalCode = theCMSView.getPostalCodeField();
            String newPhoneNumber = theCMSView.getPhoneNumberField();
            String newClientType = theCMSView.getClientTypeCombo();

            // Ensures that every input is valid, otherwise display appropriate messages and do nothing
            if (invalidClient(newFirstName, newLastName, newAddress, newPhoneNumber,
                              newPostalCode, newClientType)) {
                return;
            }

            // Get Client object from selection
            Client clientToUpdate = theCMSView.getSelectedClient();

            // Update client based on value of text fields
            clientToUpdate.updateInfo(newFirstName, newLastName, newAddress, newPostalCode,
                                      newPhoneNumber, newClientType);

            // Update Database with new information and show confirmation message
            theModel.updateClient(clientToUpdate.getId(), clientToUpdate);
            theCMSView.successMessage(theInsertView, "Successfully updated " +
                    "Client (ID: " + clientToUpdate.getId() + ")");
        }
    }

    /**
     * Listener Class for the 'Delete Client' Button
     */
    private class deleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Ensures that a searched client is selected
            if (theCMSView.getSelectedClient() == null) {
                theCMSView.errorMessage(theCMSView, "No Client Selected.");
                return;
            }

            // Display Confirmation Dialog before deleting
            int input = JOptionPane.showConfirmDialog(null,
                    "Do you want to delete the selected client?", "CONFIRMATION REQUIRED",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

            // If 'Yes' is pressed, delete client from DB and clear details
            if (input == 0) {
                int idToDelete = theCMSView.getSelectedClient().getId();
                theModel.deleteClient(idToDelete);
                theCMSView.successMessage(theInsertView, "Successfully deleted " +
                        "Client (ID: " + idToDelete + ")");
                removeResult();
                theCMSView.clearClientDetails();
            }
        }

        /**
         * Helper method that removes Object from JList, which allows the search query to
         * automatically refresh (removing the deleted client)
         */
        private void removeResult() {
            ArrayList<Client> newResults = theCMSView.getCurrResults();
            newResults.remove(theCMSView.getSelectedClient());
            theCMSView.refreshResults(newResults);
        }
    }

    /**
     * Listener Class for the Search Results List
     */
    private class resultDetailsListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // If an object (object) is selected, get that object and display client details
            if (!e.getValueIsAdjusting()) {
                Client selected = theCMSView.getSelectedClient();
                theCMSView.populateClientDetails(selected);
            }
        }
    }

    ////////////////////////////////////////////////////
    //////// Listeners for Insert Client Frame /////////
    ////////////////////////////////////////////////////

    /**
     * Listener Class for the 'Insert Client' Button
     */
    private class insertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Take inputs from text fields
            String firstName = theInsertView.getFirstNameField();
            String lastName = theInsertView.getLastNameField();
            String address = theInsertView.getAddressField();
            String postalCode = theInsertView.getPostalCode();
            String phoneNumber = theInsertView.getPhoneNumber();
            // Set clientType based on dropdown choice
            String clientType = (theInsertView.getClientType() == 0) ? "C" : "R";

            // Checks that all inputs are valid, display appropriate dialog boxes otherwise
            if (invalidClient(firstName, lastName, address, phoneNumber,
                    postalCode, clientType)) {
                theInsertView.run();
                return;
            }

            // Create client object and add to database
            Client clientToAdd = new Client(firstName, lastName, address, postalCode, phoneNumber, clientType);
            theModel.addClient(clientToAdd);
            theCMSView.successMessage(theInsertView, "Client added to the Database successfully!");
            theInsertView.dispose();
        }
    }

    /**
     * Listener Button for the 'Cancel' Button
     */
    private class cancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Close insert view
            theInsertView.dispose();
        }
    }
}
