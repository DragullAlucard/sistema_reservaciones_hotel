/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reservacioneshotel;

/**
 *
 * @author alexander
 */

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.File;

public class FireBaseConnection {

    public static void inicializar() {
        try {
            String path = "sistemareservacioneshotel-firebase-adminsdk-fbsvc-a93a260fa9.json";

            File f = new File(path);
            System.out.println("Buscando archivo en: " + f.getAbsolutePath());

            FileInputStream serviceAccount = new FileInputStream(f);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase conectado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al conectar con Firebase: " + e.getMessage());
        }
    }

    public static Firestore db() {
        return FirestoreClient.getFirestore();
    }
}
