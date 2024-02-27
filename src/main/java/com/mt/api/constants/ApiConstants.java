package com.mt.api.constants;

public final class ApiConstants {

    public static final String PATH_SEPARATOR = "/";

    public static final String API = PATH_SEPARATOR + "api";
    public static final String VERSION_1 = PATH_SEPARATOR + "v1";
    public static final String LANGUAGES = PATH_SEPARATOR + "languages";
    public static final String DOMAINS = PATH_SEPARATOR + "domains";
    public static final String TRANSLATE = PATH_SEPARATOR + "translate";
    public static final String VALIDATED_TRANSLATE = PATH_SEPARATOR + "validated-translate";

    public static final String LANGUAGE_VALUES = "{ \"en-US\", \"en-GB\", \"fr-FR\", \"de-DE\", \"it-IT\", \"es-ES\" }";

    public static final String DOMAIN_VALUES = "{ \"academic\", \"business\", \"general\", \"casual\", \"creative\" }";

    private ApiConstants() {
        //No instantiation as it is utility class to hold constants
    }
}
