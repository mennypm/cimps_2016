package comfred.cimps2016;

/**
 * Created by freddyiniguez on 9/17/16.
 */
public class Config {
    // Addresses of database connection and CRUD operations for CIMPS 2016
    public static final String URL_GET_CIMPER = "http://104.236.197.92/android/cimps_databases_2016/getCimper.php?id=";
    public static final String URL_SET_CIMPER = "http://104.236.197.92/android/cimps_databases_2016/setCimper.php";

    // Keys that will be used to send the request to the PHP scripts
    public static final String KEY_CIMPER_ID = "id";
    public static final String KEY_CIMPER_GAFFETE = "gaffete";

    // JSON Tags
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_NAME = "name";
    public static final String TAG_AFIL = "afiliation";
    public static final String TAG_GAFFETE = "gaffete";
    public static final String TAG_ACCEPT = "accept";

    // Assistant ID to be passed via Intent
    public static final String CIMPER_ID = "cimper_id";
}
