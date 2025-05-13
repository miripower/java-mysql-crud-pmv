package com.polmirvid;

import com.polmirvid.dao.Categoria.CategoriaDao;
import com.polmirvid.dao.Categoria.CategoriaDaoImpl;
import com.polmirvid.dao.Producto.ProductoDao;
import com.polmirvid.dao.Producto.ProductoDaoImpl;
import com.polmirvid.model.Categoria;
import com.polmirvid.model.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    //Ya sé que scanner es más lenta que Keyboardreader pero la uso porque no me funciona KeyboardReader
    private Scanner scanner;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;

    public Menu() {
        scanner = new Scanner(System.in);
        categoriaDao = CategoriaDaoImpl.getInstance();
        productoDao = ProductoDaoImpl.getInstance();
    }

    public void init() {
        int opcion;

        do {
            menu();
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1: listarCategorias();
                    break;
                case 2: listarProductos();
                    break;
                case 3: insertarCategoria();
                    break;
                case 4: insertarProducto();
                    break;
                case 5: actualizarCategoria();
                    break;
                case 6: actualizarProducto();
                    break;
                case 7: eliminarCategoria();
                    break;
                case 8: eliminarProducto();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no es válido\n\n");
            }

        } while (opcion != 0);
    }

    public void menu() {
        System.out.println("SISTEMA DE GESTIÓN DE CATEGORÍAS Y PRODUCTOS");
        System.out.println("=============================================");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Salir");
        System.out.println("1: Listar todas las categorías");
        System.out.println("2: Listar todos los productos");
        System.out.println("3: Insertar nueva categoría");
        System.out.println("4: Insertar nuevo producto");
        System.out.println("5: Actualizar categoría");
        System.out.println("6: Actualizar producto");
        System.out.println("7: Eliminar categoría");
        System.out.println("8: Eliminar producto");
        System.out.print("\nOpción: ");
    }

    public void listarCategorias() {
        System.out.println("\nLISTADO DE CATEGORÍAS");
        try {
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías registradas.");
            } else {
                categorias.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Error consultando las categorías: " + e.getMessage());
        }
    }

    public void listarProductos() {
        System.out.println("\nLISTADO DE PRODUCTOS");
        try {
            List<Producto> productos = productoDao.getAll();
            if (productos.isEmpty()) {
                System.out.println("No hay productos registrados.");
            } else {
                productos.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Error consultando los productos: " + e.getMessage());
        }
    }

    public void insertarCategoria() {
        System.out.println("\nINSERTAR NUEVA CATEGORÍA");
        System.out.print("Introduzca el nombre de la categoría: ");
        String nombre = scanner.nextLine();
        Categoria categoria = new Categoria(nombre);

        try {
            categoriaDao.add(categoria);
            System.out.println("Categoría insertada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error insertando la categoría: " + e.getMessage());
        }
    }

    public void insertarProducto() {
        System.out.println("\nINSERTAR NUEVO PRODUCTO");
        System.out.print("Introduzca el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduzca el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Introduzca el stock del producto: ");
        int stock = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer
        System.out.print("Introduzca el ID de la categoría: ");
        int categoriaId = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer

        try {
            Categoria categoria = categoriaDao.getById(categoriaId);
            if (categoria != null) {
                Producto producto = new Producto(nombre, precio, stock, categoria);
                productoDao.add(producto);
                System.out.println("Producto insertado correctamente.");
            } else {
                System.out.println("Categoría no encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error insertando el producto: " + e.getMessage());
        }
    }

    public void actualizarCategoria() {
        System.out.println("\nACTUALIZAR CATEGORÍA");
        System.out.print("Introduzca el ID de la categoría a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer
        try {
            Categoria categoria = categoriaDao.getById(id);
            if (categoria != null) {
                System.out.print("Introduzca el nuevo nombre de la categoría (" + categoria.getNombre() + "): ");
                String nuevoNombre = scanner.nextLine();
                categoria.setNombre(nuevoNombre.isEmpty() ? categoria.getNombre() : nuevoNombre);
                categoriaDao.update(categoria);
                System.out.println("Categoría actualizada correctamente.");
            } else {
                System.out.println("Categoría no encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error actualizando la categoría: " + e.getMessage());
        }
    }

    public void actualizarProducto() {
        System.out.println("\nACTUALIZAR PRODUCTO");
        System.out.print("Introduzca el ID del producto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer
        try {
            Producto producto = productoDao.getById(id);
            if (producto != null) {
                System.out.print("Introduzca el nuevo nombre del producto (" + producto.getNombre() + "): ");
                String nuevoNombre = scanner.nextLine();
                producto.setNombre(nuevoNombre.isEmpty() ? producto.getNombre() : nuevoNombre);
                System.out.print("Introduzca el nuevo precio del producto (" + producto.getPrecio() + "): ");
                double nuevoPrecio = scanner.nextDouble();
                producto.setPrecio(nuevoPrecio != 0 ? nuevoPrecio : producto.getPrecio());
                System.out.print("Introduzca el nuevo stock del producto (" + producto.getStock() + "): ");
                int nuevoStock = scanner.nextInt();
                producto.setStock(nuevoStock != 0 ? nuevoStock : producto.getStock());
                productoDao.update(producto);
                System.out.println("Producto actualizado correctamente.");
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Error actualizando el producto: " + e.getMessage());
        }
    }

    public void eliminarCategoria() {
        System.out.println("\nELIMINAR CATEGORÍA");
        System.out.print("Introduzca el ID de la categoría a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer
        try {
            categoriaDao.delete(id);
            System.out.println("Categoría eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error eliminando la categoría: " + e.getMessage());
        }
    }

    public void eliminarProducto() {
        System.out.println("\nELIMINAR PRODUCTO");
        System.out.print("Introduzca el ID del producto a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer
        try {
            productoDao.delete(id);
            System.out.println("Producto eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error eliminando el producto: " + e.getMessage());
        }
    }
}
