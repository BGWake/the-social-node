package social.networking.model;

public class UserView {
    public static class Public {}
    static class Extended extends Public {}
    static class Private extends Extended {}
}