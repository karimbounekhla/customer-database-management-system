package exercise234CMS;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CMSController {
    private CMSView theCMSView;
    private InsertClientView theInsertView;
    private CMSModel theModel;

    public CMSController(CMSView cmsv, InsertClientView icv, CMSModel cmsm) {
        theCMSView = cmsv;
        theInsertView = icv;
        theModel = cmsm;
        addListeners();
    }

    public void run() {
        theCMSView.run();
    }

    private void searchByID(int id) {
        if (invalidID(id+"")) return;

        ArrayList<Client> searchResults = theModel.getSearchResults("id", id+"");
        if (searchResults != null) {
            theCMSView.refreshResults(searchResults);
        }
    }

    private void searchByType(String clientType) {
        if (invalidType(clientType)) return;

        ArrayList<Client> searchResults = theModel.getSearchResults("clientType", clientType);
        if (searchResults != null) {
            theCMSView.refreshResults(searchResults);
        }
    }

    private void searchByLastName(String lastName) {
        if (invalidName("", lastName)) return;

        ArrayList<Client> searchResults = theModel.getSearchResults("lastName", lastName);
        if (searchResults != null) {
            theCMSView.refreshResults(searchResults);
        }
    }

    /**
     *
     * @param client
     * @return
     */
    private boolean isClientValid(Client client) {
        if (invalidName(client.getFirstName(), client.getLastName()) || invalidAddress(client.getAddress())
            || invalidPhone(client.getPhoneNumber()) || invalidPostalCode(client.getPostalCode())
            || invalidType(client.getClientType())) {
            return false;
        }
        return true;
    }

    private boolean invalidType(String clientType) {
        if (clientType.length() > 1 || !(clientType.equals("R") || clientType.equals("C"))) {
            theCMSView.errorMessage(theCMSView, "Client Type must be 1 character; either 'R' (Residential) " +
                    "or 'C' (Commercial).");
            return true;
        }
        return false;
    }

    private boolean invalidName(String firstN, String lastN) {
        if (firstN.length() > 20 || firstN.length() > 20) {
            theCMSView.errorMessage(theCMSView, "First and/or Last Names must be less than 20 characters");
            return true;
        }
        return false;
    }

    private boolean invalidAddress(String address) {
        if (address.length() > 50) {
            theCMSView.errorMessage(theCMSView, "Address must be less than 50 characters");
            return true;
        }
        return false;
    }

    private boolean invalidPhone(String phoneNum) {
        Pattern phonePattern = Pattern.compile("^\\d{3}-\\d{3}-\\d{4}$");
        if (phoneNum.length() > 13 || !phonePattern.matcher(phoneNum).matches()) {
            System.out.println(phoneNum);
            theCMSView.errorMessage(theCMSView, "Phone Number must be in the format: 123-456-7890");
            return true;
        }
        return false;
    }

    private boolean invalidPostalCode(String postalCode) {
        Pattern postalPattern = Pattern.compile("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$");
        if (postalCode.length() > 7 || !postalPattern.matcher(postalCode).matches()) {
            theCMSView.errorMessage(theCMSView, "Phone Number must be in the format: 123-456-7890");
            return true;
        }
        return false;
    }

    private boolean invalidID(String id) {
        Pattern IDPattern = Pattern.compile("^[0-9]+$");
        if (id.length() <= 4 && IDPattern.matcher(id).matches()) {
            return false;
        }
        theCMSView.errorMessage(theCMSView, "ID must contain only digits up to 9999");
        return true;
    }

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

    private class searchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String searchCriteria = theCMSView.getSearchCriteria();
            search(searchCriteria);
        }

        private void search(String criteria) {
            String query = theCMSView.getSearchQuery();
            if (criteria.equals("id") && !invalidID(query)) {
                searchByID(Integer.parseInt(query));
            } else if (criteria.equals("lastName")) {
                searchByLastName(query);
            } else if (criteria.equals("clientType")) {
                searchByType(query);
            } else {
                return;
            }
        }
    }

    private class clearSearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            theCMSView.clearSearch();
        }
    }

    private class addClientListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            theInsertView.clearFields();
            theInsertView.run();
        }
    }

    private class saveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (theCMSView.getSelectedClient() == null) {
                theCMSView.errorMessage(theCMSView, "No Client Selected.");
                return;
            }

            String newFirstName = theCMSView.getFirstNameField();
            String newLastName = theCMSView.getLastNameField();
            String newAddress = theCMSView.getAddressTextArea();
            String newPostalCode = theCMSView.getPostalCodeField();
            String newPhoneNumber = theCMSView.getPhoneNumberField();
            String newClientType = theCMSView.getClientTypeCombo();

            if (invalidName(newFirstName, newLastName) || invalidAddress(newAddress) ||
                    invalidPostalCode(newPostalCode) || invalidPhone(newPhoneNumber)) {
                return;
            }

            Client clientToUpdate = theCMSView.getSelectedClient();
            clientToUpdate.updateInfo(newFirstName, newLastName, newAddress, newPostalCode,
                                      newPhoneNumber, newClientType);

            theModel.updateClient(clientToUpdate.getId(), clientToUpdate);
            theCMSView.successMessage(theInsertView, "Successfully updated " +
                    "Client (ID: " + clientToUpdate.getId() + ")");
        }


    }

    private class deleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (theCMSView.getSelectedClient() == null) {
                theCMSView.errorMessage(theCMSView, "No Client Selected.");
                return;
            }
            int input = JOptionPane.showConfirmDialog(null,
                    "Do you want to delete the selected client?", "CONFIRMATION REQUIRED",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (input == 0) {
                int idToDelete = theCMSView.getSelectedClient().getId();
                theModel.deleteClient(idToDelete);
                theCMSView.successMessage(theInsertView, "Successfully deleted " +
                        "Client (ID: " + idToDelete + ")");
                removeResult();
                theCMSView.clearClientDetails();
            }
        }

        private void removeResult() {
            ArrayList<Client> newResults = theCMSView.getCurrResults();
            newResults.remove(theCMSView.getSelectedClient());
            theCMSView.refreshResults(newResults);
        }
    }

    private class resultDetailsListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Client selected = theCMSView.getSelectedClient();
                theCMSView.populateClientDetails(selected);
            }
        }
    }

    ////////////////////////////////////////////////////
    //////// Listeners for Insert Client Frame /////////
    ////////////////////////////////////////////////////

    private class insertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = theInsertView.getFirstNameField();
            String lastName = theInsertView.getLastNameField();
            String address = theInsertView.getAddressField();
            String postalCode = theInsertView.getPostalCode();
            String phoneNumber = theInsertView.getPhoneNumber();
            String clientType = (theInsertView.getClientType() == 0) ? "C" : "R";
            if (invalidName(firstName, lastName) || invalidAddress(address) ||
                invalidPostalCode(postalCode) || invalidPhone(phoneNumber)) {
                theInsertView.run();
                return;
            }
            Client clientToAdd = new Client(firstName, lastName, address, postalCode, phoneNumber, clientType);
            theModel.addClient(clientToAdd);
            theCMSView.successMessage(theInsertView, "Client added to the Database successfully!");
            theInsertView.dispose();
        }
    }

    private class cancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            theInsertView.dispose();
        }
    }

    public static void main(String[] args) {
        Pattern phonePattern = Pattern.compile("^\\d{3}-\\d{3}-\\d{4}$");
        String id = "584-594-3523";
        if (!phonePattern.matcher(id).matches()) {
            System.out.println("gucci");
        }
    }

}
