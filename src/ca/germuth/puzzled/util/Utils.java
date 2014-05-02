package ca.germuth.puzzled.util;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import ca.germuth.puzzled.puzzle.cube.Cube;

public class Utils {
	 /**
	  * TODO: Test if slower than using Java reflection: Reflection.getAllSubtypes
     * Get a list of classes that are subclasses of a given class, in the same
     * package as that class. Includes the given class in the result list.
     * Excludes any classes that are interfaces or abstract.
     *
     * @param c Class of which to find subclasses.
     */
    public static ArrayList<Class<?>> getConcreteTypes(Class<?> c) {

//        String p = c.getPackage().getName();
//
//        String p_path = p.replace('.', File.separatorChar);
//        ArrayList<Class<?>> subclasses = new ArrayList<Class<?>>();
//        int modifiers = c.getModifiers();
//        String[] classpath = System.getProperty("java.class.path").split(File.pathSeparator);
//        for (int i = 0; i < classpath.length; i++) {
//            File directory = new File(classpath[i], p_path);
//            File[] classfiles = directory.listFiles(new FileFilter() {
//
//                public boolean accept(File f) {
//                    return f.getName().endsWith(".class");
//                }
//            });
//            for (int j = 0; classfiles != null && j < classfiles.length; j++) {
//                String classfile = classfiles[j].getName();
//
//                classfile = classfile.substring(0, classfile.length() - 6);
//                Class<?> class_in_package;
//                try {
//                    class_in_package = Class.forName(p + '.' + classfile);
//                } catch (ClassNotFoundException e) {
//                    throw new IllegalStateException(
//                            "Should not happen.");
//                }
//                if (c.isAssignableFrom(class_in_package)) {
//                    modifiers = class_in_package.getModifiers();
//                    if (!(Modifier.isInterface(modifiers)
//                            || Modifier.isAbstract(modifiers))) {
//
//                        subclasses.add(class_in_package);
//                    }
//                }
//            }
//        }
//        if (subclasses.isEmpty()) {
//            // Try to add class passed.
//            modifiers = c.getModifiers();
//            if (!Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers) && Modifier.isPublic(modifiers)) {
//                subclasses.add(c);
//            } else {
//                throw new IllegalStateException(
//                        "No concrete subclasses found for " + c.getName());
//            }
//        }
//
//        return subclasses;
    	ArrayList<Class<?>> puzzles = new ArrayList<Class<?>>();
    	puzzles.add(Cube.class);
    	//puzzles.add(Minx.class);
    	return puzzles;
    }
    
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}