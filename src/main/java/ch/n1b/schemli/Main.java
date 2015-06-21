package ch.n1b.schemli;

import ch.n1b.libschem.LibschemAPI;
import ch.n1b.worldedit.schematic.data.DataException;
import ch.n1b.worldedit.schematic.schematic.Cuboid;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 20.06.2015.
 *
 * @author Thomas
 */
public class Main {

    private static String ROT0 = "_0";
    private static String ROT90 = "_90";
    private static String ROT180 = "_180";
    private static String ROT270 = "_270";

    private static String[] ROT = {ROT0, ROT90, ROT180, ROT270};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: schemli <input schematic(s)>");
            System.exit(1);
        }

        List<String> schematics = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
        schematics.parallelStream().forEach(s -> rotate(s));
    }

    private static void rotate(String path){
        File file = new File(path);

        if(!file.exists()){
            System.err.println("Cannot find file: " + path);
            System.exit(1);
        }

        if(!file.isFile()){
            System.err.println("Not a file: " + path);
            System.exit(1);
        }

        System.out.println("Loading " + path);
        Cuboid cuboid = null;
        try {
            cuboid= LibschemAPI.loadSchematic(file);
        } catch (IOException | DataException e) {
            System.err.println("Cannot load schematic: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }


        String filename = file.getName();

        String[] splits = filename.split("\\.");

        String prefix = splits[0];
        String postfix = ".schematic";
        if(splits.length>1) {
            prefix = String.join(".", Arrays.copyOf(splits, splits.length - 1));
            postfix = "." + splits[splits.length - 1];
        }

        String folder = "";
        if(file.getPath().lastIndexOf(File.separator)>0){
            folder =  file.getPath().substring(0, file.getPath().lastIndexOf(File.separator));
        }

        for (String rotationName : ROT) {
            String rotatedFilename = folder + prefix + rotationName + postfix;
            try {
                System.out.println("Saving " + rotatedFilename);
                LibschemAPI.saveSchematic(rotatedFilename,cuboid);
            } catch (IOException | DataException e) {
                System.err.println("Cannot save schematic: " + rotatedFilename);
                e.printStackTrace();
                System.exit(1);
            }
            cuboid.rotate2D(90);
        }
        System.out.println("Done.");
    }
}
