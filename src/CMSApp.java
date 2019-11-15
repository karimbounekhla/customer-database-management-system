
public class CMSApp {

    public static void main(String[] args) {
        CMSView cmsv = new CMSView();
        InsertClientView icv = new InsertClientView();
        CMSModel cmsm = new CMSModel();
        CMSController theController = new CMSController(cmsv, icv, cmsm);

        // Run the Application
        theController.run();
    }

}
