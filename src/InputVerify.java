import java.util.regex.Pattern;

public class InputVerify {
    private CMSView cms;

    public InputVerify(CMSView cms) {
        this.cms = cms;
    }

    public boolean invalidType(String clientType) {
        if (clientType.length() > 1 || !(clientType.equals("R") || clientType.equals("C"))) {
            cms.errorMessage(cms, "Client Type must be 1 character; either 'R' (Residential) " +
                    "or 'C' (Commercial).");
            return true;
        }
        return false;
    }

    public boolean invalidName(String firstN, String lastN) {
        if (firstN.length() > 20 || firstN.length() > 20) {
            cms.errorMessage(cms, "First and/or Last Names must be less than 20 characters");
            return true;
        }
        return false;
    }

    public boolean invalidAddress(String address) {
        if (address.length() > 50) {
            cms.errorMessage(cms, "Address must be less than 50 characters");
            return true;
        }
        return false;
    }

    public boolean invalidPhone(String phoneNum) {
        Pattern phonePattern = Pattern.compile("^\\d{3}-\\d{3}-\\d{4}$");
        if (phoneNum.length() > 13 || !phonePattern.matcher(phoneNum).matches()) {
            cms.errorMessage(cms, "Phone Number must be in the format: 123-456-7890");
            return true;
        }
        return false;
    }

    public boolean invalidPostalCode(String postalCode) {
        Pattern postalPattern = Pattern.compile("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$");
        if (postalCode.length() > 7 || !postalPattern.matcher(postalCode).matches()) {
            cms.errorMessage(cms, "Phone Number must be in the format: 123-456-7890");
            return true;
        }
        return false;
    }

    public boolean invalidID(String id) {
        Pattern IDPattern = Pattern.compile("^[0-9]+$");
        if (id.length() <= 4 && IDPattern.matcher(id).matches()) {
            return false;
        }
        cms .errorMessage(cms, "ID must contain only digits up to 9999");
        return true;
    }
}
