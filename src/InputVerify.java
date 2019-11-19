import java.util.regex.Pattern;

/**
 * Helper class to verify validity of User Input, using basic methods and RegEx.
 */
public class InputVerify {
    private CMSView cms;

    /**
     * Constructor class
     * @param cms Client Management System View - used to display error messages
     */
    public InputVerify(CMSView cms) {
        this.cms = cms;
    }

    /**
     * Method to check if client type is valid ('C' or 'R')
     * @param clientType client type
     * @return true if invalid, false if valid
     */
    public boolean isInvalidType(String clientType) {
        if (clientType.length() > 1 || !(clientType.equals("R") || clientType.equals("C"))) {
            System.out.println("ver" + clientType);
            cms.errorMessage(cms, "Client Type must be 1 character; either 'R' (Residential) " +
                    "or 'C' (Commercial).");
            return true;
        }
        return false;
    }

    /**
     * Method to check if the first and last name is valid (less than or equal to 20 characters)
     * @param firstN first name
     * @param lastN last name
     * @return true if invalid, false is valid
     */
    public boolean isInvalidName(String firstN, String lastN) {
        if (firstN.length() == 0 || firstN.length() > 20 || lastN.length() == 0 || lastN.length() > 20) {
            cms.errorMessage(cms, "First and/or Last Names cannot be empty and must be less than 20 characters");
            return true;
        }
        return false;
    }

    /**
     * Method to check if the address is valid (less than or equal to 50 characters)
     * @param address address
     * @return true if invalid, false is valid
     */
    public boolean isInvalidAddress(String address) {
        if (address.length() == 0 || address.length() > 50) {
            cms.errorMessage(cms, "Address cannot be empty and must be less than 50 characters");
            return true;
        }
        return false;
    }

    /**
     * Method to check if the phone number is valid, i.e. in the format 123-456-7890
     * @param phoneNum phone number
     * @return true if invalid, false is valid
     */
    public boolean isInvalidPhone(String phoneNum) {
        Pattern phonePattern = Pattern.compile("^\\d{3}-\\d{3}-\\d{4}$");
        if (phoneNum.length() > 13 || !phonePattern.matcher(phoneNum).matches()) {
            cms.errorMessage(cms, "Phone Number cannot be empty and must be in the format: 123-456-7890");
            return true;
        }
        return false;
    }

    /**
     * Method to check if the postal code is valid, i.e. in the format A1A 1A1 where A is any letter
     * and 1 is any digit
     * @param postalCode postal code
     * @return true if invalid, false is valid
     */
    public boolean isInvalidPostalCode(String postalCode) {
        Pattern postalPattern = Pattern.compile("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$");
        if (postalCode.length() > 7 || !postalPattern.matcher(postalCode).matches()) {
            cms.errorMessage(cms, "Phone Number cannot be empty and must be in the format: 123-456-7890");
            return true;
        }
        return false;
    }

    /**
     * Method to check if the ID is valid (4 or less digits)
     * @param id id
     * @return
     */
    public boolean isInvalidID(String id) {
        Pattern IDPattern = Pattern.compile("^[0-9]+$");
        if (id.length() <= 4 && IDPattern.matcher(id).matches()) {
            return false;
        }
        cms .errorMessage(cms, "ID cannot be empty, and must contain only digits (up to 4)");
        return true;
    }
}
