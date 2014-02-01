package selfpatchfinalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Rick
 */
public class SelfPatchFinalizer {

    private static class InputStreamSkipper extends Thread{
        InputStream in;

        public InputStreamSkipper(InputStream in) {
            this.in = in;
        }
        
        @Override
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                while(in.read(buffer) != -1){
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(SelfPatchFinalizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * @param args
     * @throws java.io.IOException 
     */
    public static void main(String[] args) throws IOException {
        File patchFile = new File("patchList.txt");
        do{
            if(patchFile.exists()){
                boolean res = patch(patchFile);
                patchFile.delete();
                if(!res){
                    return;
                }
            }
            try {
                Process exec = Runtime.getRuntime().exec(new String[]{System.getProperty("java.home")+"/bin/java", "-jar", args[0]});
                new InputStreamSkipper(exec.getErrorStream()).start();
                new InputStreamSkipper(exec.getInputStream()).run();
            } catch (IOException ex) {
                Logger.getLogger(SelfPatchFinalizer.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "IOException: " + ex.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SelfPatchFinalizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(patchFile.exists());
        
        
        System.exit(0);
    }
    
    static final boolean patch(File f) throws IOException{
        ArrayList<String> args = new ArrayList<>();
        try (BufferedReader rd = new BufferedReader(new FileReader(f))) {
            String s;
            while((s = rd.readLine()) != null){
                args.add(s);
            }
        }
        for(String toCopy : args){
            File newFile = new File(toCopy+".new");
            File oldFile = new File(toCopy);
            if(!newFile.exists()){
                JOptionPane.showMessageDialog(null, toCopy + ".new does not exist!");
                return false;
            }
            
            if(oldFile.exists() && !oldFile.delete()){
                File oldSuffix = new File(toCopy+".old");
                if(oldSuffix.exists() && !oldSuffix.delete()){
                    JOptionPane.showMessageDialog(null, "Failed to delete previously existing " + toCopy + ".old");
                    return false;
                }
                if(!oldFile.renameTo(oldSuffix)){
                    JOptionPane.showMessageDialog(null, "Failed to delete " + toCopy);
                    return false;
                }
            }
            if(!newFile.renameTo(oldFile)){
                JOptionPane.showMessageDialog(null, "Failed to rename to " + toCopy);
                return false;
            }
        }
        return true;
    }
}
