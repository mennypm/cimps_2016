package comfred.cimps2017;

/**
 * Created by freddyiniguez on 9/17/16.
 */
public class Config {
    // Addresses of database connection and CRUD operations for CIMPS 2017
    public static final String URL_GET_CIMPER = "http://cimps.cimat.mx/registro/android/getCimper.php?id=";
    public static final String URL_SET_CIMPER = "http://cimps.cimat.mx/registro/android/setCimper.php";
    public static final String URL_SET_WORKSHOP = "http://cimps.cimat.mx/registro/android/setWorkshop.php";

    // Keys that will be used to send the request to the PHP scripts
    public static final String KEY_CIMPER_ID = "id";
    public static final String KEY_CIMPER_NAME = "name";
    public static final String KEY_CIMPER_AFIL = "afiliation";
    public static final String KEY_CIMPER_GAFFETE = "gaffete";
    public static final String KEY_CIMPER_ACCEPT = "accept";

    // JSON Tags
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_NAME = "name";
    public static final String TAG_AFIL = "afiliation";
    public static final String TAG_CATEGORY = "category";
    public static final String TAG_GAFFETE = "gaffete";
    public static final String TAG_ACCEPT = "accept";

    // Contains the int code for the camera permission
    public static final int CAMERA_REQUEST_CODE = 100;
}
