
public class CMSApp {

    /**
     * Method used to initialize objects and run the application
     * @param args unused
     */
    public static void main(String[] args) {
        CMSView cmsv = new CMSView();
        InsertClientView icv = new InsertClientView();
        CMSModel cmsm = new CMSModel();
        CMSController theController = new CMSController(cmsv, icv, cmsm);

        // Run the Application
        theController.run();
    }

}
