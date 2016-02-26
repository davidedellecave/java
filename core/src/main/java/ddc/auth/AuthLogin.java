package ddc.auth;

public interface AuthLogin {
        void login(AuthPrincipal principal, Object credentials) throws SecurityException;
}
