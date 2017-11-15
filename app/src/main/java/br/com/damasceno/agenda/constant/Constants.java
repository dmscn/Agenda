package br.com.damasceno.agenda.constant;

public interface Constants {

    // Params
    public static final String PARAM_TASK = "TASK_POJO";

    // Tags
    public static final String TAG_LOG = "TAG_AGENDA";
    public static final String TAG_FRAG_WELCOME = "FRAG_WELCOME";
    public static final String TAG_FRAG_LOGIN = "FRAG_LOGIN";
    public static final String TAG_FRAG_REGISTER = "FRAG_REGISTER";
    public static final String TAG_REQUEST_LOGIN = "REQUEST_LOGIN";
    public static final String TAG_REQUEST_REGISTER = "REQUEST_REGISTER";
    public static final String TAG_REQUEST_ALL_TASKS = "REQUEST_ALL_TASKS";
    public static final String TAG_REQUEST_ADD_NEW_TASK = "REQUEST_ADD_NEW_TASK";
    public static final String TAG_REQUEST_REMOVE_TASK = "REQUEST_REMOVE_TASK";
    public static final String TAG_REQUEST_UPDATE_TASK = "REQUEST_UPDATE_TASK";

    // Web Service
    public static final int RESPONSE_SUCCESS = 201;
    public static final int RESPONSE_INVALID_CREDENTIALS = 401;
    public static final int RESPONSE_INVALID_FIELDS = 400;
    public static final int RESPONSE_EMAIL_ALREADY_EXISTS = 409;

    public static final String WEB_SERVER_MASTER_KEY = "QYiVImowlxDeI8rWwBf7pCZ2j3g72gxk";
    public static final String URL_BASE = "https://myrestfulapi.herokuapp.com";
    public static final String URL_AUTH = "/auth";
    public static final String URL_USER = "/users";
    public static final String URL_TASK = "/tasks";

    // Shared Preferences
    public static final String PREF_USER = "PREF_USER";
    public static final String KEY_CREDENTIALS_TOKEN = "KEY_CREDENTIALS_TOKEN";
    public static final String KEY_USER_ACCESS_TOKEN = "KEY_USER_ACCESS_TOKEN";
    public static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    public static final String KEY_USER_PICTURE = "KEY_USER_PICTURE";

    // Fragments
    public static final String FRAG_TASK_TITLE = "Tasks";
    public static final String FRAG_EVENT_TITLE = "Events";
    public static final String FRAG_CONTACT_TITLE = "Contacts";

    // Task Labels
    public static final String[] LABELS = {
            "Regular",
            "Work",
            "School",
            "Home"
    };

    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

}
