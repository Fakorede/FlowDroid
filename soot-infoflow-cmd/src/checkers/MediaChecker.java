package checkers;

import java.util.HashSet;
import java.util.Set;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.util.Chain;

public class MediaChecker implements AutoCategoryChecker {
    private String serviceName;

    public MediaChecker(String serviceName) {
        this.serviceName = serviceName;
    }

    public void performChecks() {
        // MEDIA UI Callbacks
        Chain<SootClass> classes = Scene.v().getClasses();
        boolean containsOnCreate = false;
        boolean containsOnGetRoot = false;
        boolean containsOnLoadChildren = false;
        for (SootClass className : classes) {
            if (className.toString().equals(this.serviceName)) {
                for (SootMethod method : className.getMethods()) {
                    if (method.toString().contains("void onCreate()"))
                        containsOnCreate = true;
                    if (method.toString().contains("androidx.media.MediaBrowserServiceCompat$BrowserRoot onGetRoot(java.lang.String,int,android.os.Bundle)"))
                        containsOnGetRoot = true;
                    if (method.toString().contains("void onLoadChildren(java.lang.String,androidx.media.MediaBrowserServiceCompat$Result)"))
                        containsOnLoadChildren = true;
                    if (containsOnCreate && containsOnGetRoot && containsOnLoadChildren)
                        break;
                }
            }
        }

        // MEDIA Play Callbacks
        boolean containsOnPlay = false;
        boolean containsOnStop = false;
        boolean containsOnPrepare = false;
        boolean containsOnPause = false;
        boolean containsOnPlayFromMediaId = false;
        boolean containsOnPlayFromSearch = false;
        boolean containsOnSkipToNext = false;
        boolean containsOnSkipToPrevious = false;

        SootClass mediaCallbackClass = Scene.v().getSootClass("android.support.v4.media.session.MediaSessionCompat$Callback");
        Set<SootClass> subclasses = findSubClasses(mediaCallbackClass);

        for (SootClass subclass : subclasses) {
            if (!containsOnPrepare)
                containsOnPrepare = methodExistsInClass(subclass, "onPrepare", "void onPrepare()");
            if (!containsOnPause)
                containsOnPause = methodExistsInClass(subclass, "onPause", "void onPause()");
            if (!containsOnPlay)
                containsOnPlay = methodExistsInClass(subclass, "onPlay", "void onPlay()");
            if (!containsOnStop)
                containsOnStop = methodExistsInClass(subclass, "onStop", "void onStop()");
            if (!containsOnSkipToNext)
                containsOnSkipToNext = methodExistsInClass(subclass, "onSkipToNext", "void onSkipToNext()");
            if (!containsOnSkipToPrevious)
                containsOnSkipToPrevious = methodExistsInClass(subclass, "onSkipToPrevious", "void onSkipToPrevious()");
            if (!containsOnPlayFromSearch)
                containsOnPlayFromSearch = methodExistsInClass(subclass, "onPlayFromSearch", "void onPlayFromSearch(java.lang.String,android.os.Bundle)");
            if (!containsOnPlayFromMediaId)
                containsOnPlayFromMediaId = methodExistsInClass(subclass, "onPlayFromMediaId", "void onPlayFromMediaId(java.lang.String,android.os.Bundle)");
        }

        // RESULTS
        System.out.println("=============================================");
        System.out.println("MEDIA CHECKS:");
        System.out.println("=============================================");
        if (containsOnCreate)
            System.out.println("✅ APK implements onCreate() method.");
        else
            System.out.println("❌ APK does NOT implement onCreate() method. See https://developer.android.com/training/cars/media#browser_workflow");
        if (containsOnGetRoot)
            System.out.println("✅ APK implements onGetRoot() method.");
        else
            System.out.println("❌ APK does NOT implement onGetRoot() method. See https://developer.android.com/training/cars/media#browser_workflow");
        if (containsOnLoadChildren)
            System.out.println("✅ APK implements containsOnLoadChildren() method.");
        else
            System.out.println("❌ APK does NOT implement containsOnLoadChildren() method. See https://developer.android.com/training/cars/media#browser_workflow");


        System.out.println("=============================================");
        System.out.println("MEDIA PLAY CALLBACKS CHECK:");
        System.out.println("=============================================");
        if (containsOnPlay)
            System.out.println("✅ APK implements onPlay() method.");
        else
            System.out.println("❌ APK does NOT implement onPlay() method. See https://developer.android.com/training/cars/media#playback-commands");
        if (containsOnPause)
            System.out.println("✅ APK implements onPause() method.");
        else
            System.out.println("❌ APK does NOT implement onPause() method. See https://developer.android.com/training/cars/media#playback-commands");
        if (containsOnPrepare)
            System.out.println("✅ APK implements onPrepare() method.");
        else
            System.out.println("❌ APK does NOT implement onPrepare() method. See https://developer.android.com/training/cars/media#playback-commands");
        if (containsOnStop)
            System.out.println("✅ APK implements onStop() method.");
        else
            System.out.println("❌ APK does NOT implement onStop() method. See https://developer.android.com/training/cars/media#playback-commands");
        if (containsOnSkipToNext)
            System.out.println("✅ APK implements onSkipToNext() method.");
        else
            System.out.println("❌ APK does NOT implement onSkipToNext() method. See https://developer.android.com/training/cars/media#playback-commands");
        if (containsOnSkipToPrevious)
            System.out.println("✅ APK implements onSkipToPrevious() method.");
        else
            System.out.println("❌ APK does NOT implement onSkipToPrevious() method. See https://developer.android.com/training/cars/media#playback-commands");
        if (containsOnPlayFromSearch)
            System.out.println("✅ APK implements onPlayFromSearch() method.");
        else
            System.out.println("❌ APK does NOT implement onPlayFromSearch() method. See https://developer.android.com/training/cars/media#playback-commands");
        if (containsOnPlayFromMediaId)
            System.out.println("✅ APK implements onPlayFromMediaId() method.");
        else
            System.out.println("❌ APK does NOT implement onPlayFromMediaId() method. See https://developer.android.com/training/cars/media#playback-commands");
    }

    /**
     * Checks if a method with the specified name and signature exists in the given class.
     *
     * @param sootClass      The class to search in.
     * @param methodName     The name of the method to search for.
     * @param methodSignature The signature of the method to search for.
     * @return True if the method exists, false otherwise.
    */
    private boolean methodExistsInClass(SootClass sootClass, String methodName, String methodSignature) {
        for (SootMethod method : sootClass.getMethods()) {
            if (method.getName().equals(methodName) && method.getSubSignature().equals(methodSignature)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds all subclasses of the specified class.
     * 
     * @param sootClass The class whose subclasses are to be found.
     * @return a list of subclasses extending the specified class
    */
    private Set<SootClass> findSubClasses(SootClass baseClass) {
        Set<SootClass> subclasses = new HashSet<>();
        for (SootClass sootClass : Scene.v().getClasses()) {
            if (sootClass.hasSuperclass() && sootClass.getSuperclass().equals(baseClass)) {
                subclasses.add(sootClass);
            }
        }
        return subclasses;
    }
}
