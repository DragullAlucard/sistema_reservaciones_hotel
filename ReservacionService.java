/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author alexander
 */
public class ReservacionService {

    private static final String COLLECTION_NAME = "reservaciones";

    // =========================
    // CREATE
    // =========================
    public String crearReservacion(Reservacion reservacion) {
        try {
            String idReservacion = UUID.randomUUID().toString();
            reservacion.setIdReservacion(idReservacion);

            if (reservacion.getFechaCreacion() == null || reservacion.getFechaCreacion().isEmpty()) {
                reservacion.setFechaCreacion(LocalDateTime.now().toString());
            }

            if (reservacion.getEstadoReservacion() == null
                    || reservacion.getEstadoReservacion().isEmpty()) {
                reservacion.setEstadoReservacion("ACTIVA");
            }

            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(idReservacion)
                      .set(reservacion);

            future.get();
            return idReservacion;

        } catch (Exception e) {
            System.out.println("Error creando reservación en Firestore: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // READ (por ID)
    // =========================
    public Reservacion obtenerReservacionPorId(String idReservacion) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<DocumentSnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .document(idReservacion)
                      .get();

            DocumentSnapshot doc = future.get();

            if (doc.exists()) {
                Reservacion r = doc.toObject(Reservacion.class);
                if (r != null) {
                    r.setIdReservacion(idReservacion);
                }
                return r;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo reservación por ID: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // READ (todas)
    // =========================
    public List<Reservacion> obtenerTodasLasReservaciones() {
        List<Reservacion> lista = new ArrayList<>();

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .get();

            List<QueryDocumentSnapshot> docs = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                Reservacion r = doc.toObject(Reservacion.class);
                if (r != null) {
                    r.setIdReservacion(doc.getId());
                    lista.add(r);
                }
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo todas las reservaciones: " + e.getMessage());
        }

        return lista;
    }

    // =========================
    // READ (por habitación)
    // =========================
    public List<Reservacion> obtenerReservacionesPorHabitacion(String idHabitacion) {
        List<Reservacion> lista = new ArrayList<>();

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .whereEqualTo("idHabitacion", idHabitacion)
                      .get();

            List<QueryDocumentSnapshot> docs = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                Reservacion r = doc.toObject(Reservacion.class);
                if (r != null) {
                    r.setIdReservacion(doc.getId());
                    lista.add(r);
                }
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo reservaciones por habitación: " + e.getMessage());
        }

        return lista;
    }

    // =========================
    // UPDATE
    // =========================
    public boolean actualizarReservacion(Reservacion reservacionActualizada) {
        if (reservacionActualizada == null
                || reservacionActualizada.getIdReservacion() == null) {
            System.out.println("No se puede actualizar: reservación o ID nulos.");
            return false;
        }

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(reservacionActualizada.getIdReservacion())
                      .set(reservacionActualizada);

            future.get();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizando reservación: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // CANCELAR
    // =========================
    public boolean cancelarReservacion(String idReservacion) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(idReservacion)
                      .update("estadoReservacion", "CANCELADA");

            future.get();
            return true;

        } catch (Exception e) {
            System.out.println("Error cancelando reservación: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // DISPONIBILIDAD POR RANGO
    // =========================
    public boolean habitacionDisponibleEnFechas(String idHabitacion,
                                                LocalDateTime inicio,
                                                LocalDateTime fin) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .whereEqualTo("idHabitacion", idHabitacion)
                      .get();

            List<QueryDocumentSnapshot> docs = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                Reservacion r = doc.toObject(Reservacion.class);
                if (r == null) continue;

                if ("CANCELADA".equalsIgnoreCase(r.getEstadoReservacion())) {
                    continue;
                }

                // Convertimos los String almacenados a LocalDateTime
                LocalDateTime rInicio = LocalDateTime.parse(r.getInicio());
                LocalDateTime rFin    = LocalDateTime.parse(r.getFin());

                boolean seTraslapa =
                        inicio.isBefore(rFin) && fin.isAfter(rInicio);

                if (seTraslapa) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error comprobando disponibilidad de habitación: " + e.getMessage());
            return true; // para no bloquear por error
        }
    }

    // =========================
    // CÁLCULO DE NOCHES
    // =========================
    public long calcularNoches(Reservacion r) {
        if (r == null || r.getInicio() == null || r.getFin() == null) {
            return 0;
        }
        LocalDateTime ini = LocalDateTime.parse(r.getInicio());
        LocalDateTime fn  = LocalDateTime.parse(r.getFin());
        return ChronoUnit.DAYS.between(ini, fn);
    }
}
