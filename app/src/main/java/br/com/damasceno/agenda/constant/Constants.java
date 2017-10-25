package br.com.damasceno.agenda.constant;

/**
 * Created by dmscn on 14/10/17.
 */

public interface Constants {
    public static final String TAG_LOG = "TAG_AGENDA";
    public static final String TAG_FRAG_WELCOME = "FRAG_WELCOME";
    public static final String TAG_FRAG_LOGIN = "FRAG_LOGIN";
    public static final String TAG_FRAG_REGISTER = "FRAG_REGISTER";
    public static final String TAG_REQUEST_LOGIN = "REQUEST_LOGIN";

    public static final String URL_BASE = "https://myrestfulapi.herokuapp.com";
    public static final String URL_AUTH = "/auth";
    public static final String URL_USER = "/users";
    public static final String URL_TASK = "/tasks";

    public static final String METHOD_POST = "POST";
    public static final String WEB_SERVER_MASTER_KEY = "HB3kBSU1GnVudS53aCZS6n5FlfdL6nyz";

    public static final String PREF_USER = "PREF_USER";
    public static final String KEY_CREDENTIALS_TOKEN = "KEY_CREDENTIALS_TOKEN";

    public static final long MOVE_DEFAULT_TIME = 300;
    public static final long FADE_DEFAULT_TIME = 1000;

    public static final String FRAG_TASK_TITLE = "Tasks";
    public static final String FRAG_EVENT_TITLE = "Events";
    public static final String FRAG_CONTACT_TITLE = "Contacts";
}
