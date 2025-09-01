/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edespol.redvuelos;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {

    private static Firestore firestore;
    private static boolean initialized = false;

    public static void initFirebase() throws IOException {
        if (!initialized) {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            }
            firestore = FirestoreClient.getFirestore();
            initialized = true;
        }
    }

    public static Firestore getFirestore() {
        if (!initialized) {
            throw new IllegalStateException("Firebase no ha sido inicializado. Llama a initFirebase() primero.");
        }
        return firestore;
    }
}