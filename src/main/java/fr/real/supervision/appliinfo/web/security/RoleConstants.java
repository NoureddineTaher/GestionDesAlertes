package fr.real.supervision.appliinfo.web.security;

/**
 * Constants for Spring Security authorities.
 */
public class RoleConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String EXPLOITANT = "ROLE_EXPLOITANT";

    public static final String COMMUNIQUANT = "ROLE_COMMUNIQUANT";

    public static final String SUPER_ADMIN = "ROLE_SUPER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private RoleConstants() {
    }

}
