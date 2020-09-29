package Utility;

import Model.Player_Infos;
import Resources.Constants;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Diese Klasse ist zum Auslesen der Highscores-Tabelle von dem gespeicherten File gedacht, auch zum Einschreiben in den File
   Der File ist ein ".ser" File, speichert die serialisierte Highscores-Tabelle
 */
public class InStream_and_OutStream {
    /**
     *
     * @param highscores
     */
    public static void write_Highscores_in_Backup_file(List<Player_Infos> highscores){
        try {
            try (FileOutputStream out_stream = new FileOutputStream(Constants.BACKUP_FILE_FOR_HIGHSCORES); 
                ObjectOutputStream out = new ObjectOutputStream(out_stream)) {
                out.writeObject(highscores);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     *
     * @return
     */
    public static List<Player_Infos> read_Highscores_from_Backup_file(){
        List<Player_Infos> highscores = null;
        try {
            try (FileInputStream in_stream = new FileInputStream(Constants.BACKUP_FILE_FOR_HIGHSCORES); 
                ObjectInputStream in = new ObjectInputStream(in_stream)) {
                highscores = (List) in.readObject();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return highscores;
    }
}
