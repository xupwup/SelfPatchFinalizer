package selfpatchfinalizer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This class copies files from "a.new" to "a".
 * Useful for avoiding locked files. After copying
 * it calls "java -jar args[0]"
 * 
 * @author Rick
 */
public class SelfPatchFinalizer {

    /**
     * After copying
     * it calls "java -jar args[0]".
     * @param args The files to copy. 
     */
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Expected at least one argument.");
            return;
        }
        
        // this blocks as long as the parent does not read it
        // since the lolpatcher does not read this, this line will
        // block until the patcher exits.
        System.out.println("Finalizing patch...");
        
        for(String toCopy : args){
            File newFile = new File(toCopy+".new");
            File oldFile = new File(toCopy);
            if(!newFile.exists()){
                JOptionPane.showMessageDialog(null, toCopy + ".new does not exist!");
                return;
            }
            oldFile.delete();
            newFile.renameTo(oldFile);
        }
        try {
            Runtime.getRuntime().exec(new String[]{System.getProperty("java.home")+"/bin/java", "-jar", args[0]});
        } catch (IOException ex) {
            Logger.getLogger(SelfPatchFinalizer.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "IOException: " + ex.getMessage());
        }
        System.exit(0);
    }
}
