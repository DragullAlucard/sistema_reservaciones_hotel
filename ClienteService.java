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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author alexander
 */
public class ClienteService {

    // Nombre de la colección en Firestore
    private static final String COLLECTION_NAME = "clientes";

    // =========================
    // CREATE
    // =========================
    /**
     * Crea un nuevo cliente en Firestore.
     * @param cliente objeto Cliente con nombre, email y teléfono.
     * @return id generado del cliente, o null si hubo error.
     */
    public String crearCliente(Cliente cliente) {
        try {
            // Generar ID único
            String idCliente = UUID.randomUUID().toString();
            cliente.setIdCliente(idCliente);

            Firestore db = FireBaseConnection.db();

            // Guardar en la colección "clientes" con el ID generado
            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(idCliente)
                      .set(cliente);

            // Esperar la escritura (opcional, pero así sabemos si falló)
            future.get();

            return idCliente;

        } catch (Exception e) {
            System.out.println("Error creando cliente en Firestore: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // READ (por ID)
    // =========================
    /**
     * Obtiene un cliente por su ID desde Firestore.
     * @param idCliente ID del cliente.
     * @return Cliente si existe, o null si no se encontró o hubo error.
     */
    public Cliente obtenerClienteId(String idCliente) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<DocumentSnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .document(idCliente)
                      .get();

            DocumentSnapshot doc = future.get();

            if (doc.exists()) {
                Cliente cliente = doc.toObject(Cliente.class);
                // Aseguramos que el ID quede seteado
                if (cliente != null) {
                    cliente.setIdCliente(idCliente);
                }
                return cliente;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo cliente por ID: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // READ (todos)
    // =========================
    /**
     * Obtiene todos los clientes almacenados en Firestore.
     * @return Lista de clientes, puede estar vacía si no hay datos o hubo error.
     */
    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> lista = new ArrayList<>();

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION_NAME)
                      .get();

            List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documentos) {
                Cliente c = doc.toObject(Cliente.class);
                if (c != null) {
                    c.setIdCliente(doc.getId()); // ID del documento
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo todos los clientes: " + e.getMessage());
        }

        return lista;
    }

    // =========================
    // UPDATE
    // =========================
    /**
     * Actualiza un cliente existente en Firestore.
     * @param clienteActualizado objeto Cliente con el ID ya asignado.
     * @return true si se actualizó, false si hubo error.
     */
    public boolean actualizarCliente(Cliente clienteActualizado) {
        if (clienteActualizado == null || clienteActualizado.getIdCliente() == null) {
            System.out.println("No se puede actualizar: cliente o ID nulos.");
            return false;
        }

        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(clienteActualizado.getIdCliente())
                      .set(clienteActualizado);

            future.get();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizando cliente: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // DELETE
    // =========================
    /**
     * Elimina un cliente por ID en Firestore.
     * @param idCliente ID del cliente a eliminar.
     * @return true si se borró, false si hubo error.
     */
    public boolean eliminarCliente(String idCliente) {
        try {
            Firestore db = FireBaseConnection.db();

            ApiFuture<WriteResult> future =
                    db.collection(COLLECTION_NAME)
                      .document(idCliente)
                      .delete();

            future.get();
            return true;

        } catch (Exception e) {
            System.out.println("Error eliminando cliente: " + e.getMessage());
            return false;
        }
    }
}
