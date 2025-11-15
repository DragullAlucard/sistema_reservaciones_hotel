/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reservacioneshotel;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author alexander
 */
public class HabitacionService {

    // Nombre de la colección en Firestore
    private static final String COLLECTION_NAME = "habitaciones";

    // =========================
    // CREATE
    // =========================
    /**
     * Crea una nueva habitación en Firestore.
     * @param habitacion objeto Habitacion con datos básicos.
     * @return id generado de la habitación, o null si hubo error.
     */
    public String crearHabitacion(Habitacion habitacion) {
        try {
            String idHabitacion = UUID.randomUUID().toString();
            habitacion.setIdHabitacion(idHabitacion);

            if (habitacion.getEstadoHabitacion() == null
                    || habitacion.getEstadoHabitacion().isEmpty()) {
                habitacion.setEstadoHabitacion("DISPONIBLE");
            }

            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(idHabitacion)
                      .set(habitacion);

            future.get(); // esperamos a que se guarde

            return idHabitacion;

        } catch (Exception e) {
            System.out.println("Error creando habitación en Firestore: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // READ (por ID)
    // =========================
    /**
     * Obtiene una habitación por ID desde Firestore.
     * @param idHabitacion ID de la habitación.
     * @return Habitacion si existe, o null si no se encontró o hubo error.
     */
    public Habitacion obtenerHabitacionId(String idHabitacion) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<DocumentSnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .document(idHabitacion)
                      .get();

            DocumentSnapshot doc = future.get();

            if (doc.exists()) {
                Habitacion h = doc.toObject(Habitacion.class);
                if (h != null) {
                    h.setIdHabitacion(idHabitacion);
                }
                return h;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo habitación por ID: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // READ (todas)
    // =========================
    /**
     * Obtiene todas las habitaciones almacenadas en Firestore.
     * @return Lista de habitaciones (puede ser vacía si no hay datos o hay error).
     */
    public List<Habitacion> obtenerTodasLasHabitaciones() {
        List<Habitacion> lista = new ArrayList<>();

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .get();

            List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documentos) {
                Habitacion h = doc.toObject(Habitacion.class);
                if (h != null) {
                    h.setIdHabitacion(doc.getId());
                    lista.add(h);
                }
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo todas las habitaciones: " + e.getMessage());
        }

        return lista;
    }

    // =========================
    // UPDATE
    // =========================
    /**
     * Actualiza una habitación existente en Firestore.
     * @param habitacionActualizada objeto Habitacion con el ID ya asignado.
     * @return true si se actualizó, false si hubo error.
     */
    public boolean actualizarHabitacion(Habitacion habitacionActualizada) {
        if (habitacionActualizada == null
                || habitacionActualizada.getIdHabitacion() == null) {
            System.out.println("No se puede actualizar: habitación o ID nulos.");
            return false;
        }

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(habitacionActualizada.getIdHabitacion())
                      .set(habitacionActualizada);

            future.get();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizando habitación: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // DELETE
    // =========================
    /**
     * Elimina una habitación por ID en Firestore.
     * @param idHabitacion ID de la habitación a eliminar.
     * @return true si se borró, false si hubo error.
     */
    public boolean eliminarHabitacion(String idHabitacion) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(idHabitacion)
                      .delete();

            future.get();
            return true;

        } catch (Exception e) {
            System.out.println("Error eliminando habitación: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // HABITACIONES DISPONIBLES
    // =========================
    /**
     * Obtiene solo las habitaciones con estado "DISPONIBLE".
     * @return Lista de habitaciones disponibles.
     */
    public List<Habitacion> obtenerHabitacionesDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .whereEqualTo("estadoHabitacion", "DISPONIBLE")
                      .get();

            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                Habitacion h = doc.toObject(Habitacion.class);
                if (h != null) {
                    h.setIdHabitacion(doc.getId());
                    disponibles.add(h);
                }
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo habitaciones disponibles: " + e.getMessage());
        }

        return disponibles;
    }

    // =========================
    // CAMBIAR ESTADO
    // =========================
    /**
     * Cambia el estado de una habitación (por ejemplo, DISPONIBLE / OCUPADA).
     * @param idHabitacion ID de la habitación.
     * @param nuevoEstado  nuevo estado.
     * @return true si se actualizó, false si hubo error.
     */
    public boolean cambiarEstado(String idHabitacion, String nuevoEstado) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(idHabitacion)
                      .update("estadoHabitacion", nuevoEstado);

            future.get();
            return true;

        } catch (Exception e) {
            System.out.println("Error cambiando estado de habitación: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // VER SI ESTÁ DISPONIBLE
    // =========================
    /**
     * Indica si una habitación está en estado "DISPONIBLE".
     * @param idHabitacion ID de la habitación.
     * @return true si está disponible, false en caso contrario o error.
     */
    public boolean estaDisponible(String idHabitacion) {
        Habitacion h = obtenerHabitacionId(idHabitacion);
        if (h != null) {
            return "DISPONIBLE".equalsIgnoreCase(h.getEstadoHabitacion());
        }
        return false;
    }
}
