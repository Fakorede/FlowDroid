package checkers;

public class AutoCategoryCheckerFactory {
    public static AutoCategoryChecker getCheck(String appCategory, String serviceName) {
        switch (appCategory.toUpperCase()) {
            case "MEDIA":
                return new MediaChecker(serviceName);
            case "MESSAGING":
                // return new MessagingCheck(service);
            case "NAVIGATION":
                // return new NavigationCheck(service);
            case "POI":
                // return new POICheck();
            default:
                throw new IllegalArgumentException("Unknown app category: " + appCategory);
        }
    }
}
