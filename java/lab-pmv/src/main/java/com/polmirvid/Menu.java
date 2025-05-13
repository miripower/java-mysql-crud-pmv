package com.polmirvid;

import com.polmirvid.dao.Categoria.CategoriaDao;
import com.polmirvid.dao.Categoria.CategoriaDaoImpl;
import com.polmirvid.dao.Producto.ProductoDao;
import com.polmirvid.dao.Producto.ProductoDaoImpl;
import com.polmirvid.model.Categoria;
import com.polmirvid.model.Producto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Menu {
    private KeyboardReader reader;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;

    public Menu() {
        reader = new KeyboardReader();
        categoriaDao = CategoriaDaoImpl.getInstance();
        productoDao = ProductoDaoImpl.getInstance();
    }

    public void init() {
        int opcion;

        do {
            menu();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1:
                    listarCategorias();
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    obtenerCategoriaPorId();
                    break;
                case 4:
                    obtenerProductoPorId();
                    break;
                case 5:
                    insertarCategoria();
                    break;
                case 6:
                    insertarProducto();
                    break;
                case 7:
                    actualizarCategoria();
                    break;
                case 8:
                    actualizarProducto();
                    break;
                case 9:
                    eliminarCategoria();
                    break;
                case 10:
                    eliminarProducto();
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
        System.out.println("\n\nSISTEMA DE GESTIÓN DE CATEGORÍAS Y PRODUCTOS");
        System.out.println("=============================================");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Salir");
        System.out.println("1: Listar todas las categorías");
        System.out.println("2: Listar todos los productos");
        System.out.println("3: Obtener categoría por ID");
        System.out.println("4: Obtener producto por ID");
        System.out.println("5: Insertar nueva categoría");
        System.out.println("6: Insertar nuevo producto");
        System.out.println("7: Actualizar categoría");
        System.out.println("8: Actualizar producto");
        System.out.println("9: Eliminar categoría");
        System.out.println("10: Eliminar producto");
        System.out.print("\nOpción: ");
    }

    public void listarCategorias() {
        System.out.println("\nLISTADO DE CATEGORÍAS");
        try {
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías registradas.");
            } else {
                printCabeceraCategoria();
                categorias.forEach(this::printCategoria);
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
                printCabeceraProducto();
                productos.forEach(this::printProducto);
            }
        } catch (SQLException e) {
            System.err.println("Error consultando los productos: " + e.getMessage());
        }
    }

    public void obtenerCategoriaPorId() {
        System.out.println("\nOBTENER CATEGORÍA POR ID");
        System.out.println("----------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a buscar: ");
            int id = reader.nextInt();

            Categoria categoria = categoriaDao.getById(id);

            if (categoria == null) {
                System.out.println("No hay categorías registradas en la base de datos con ese ID");
            } else {
                printCabeceraCategoria();
                printCategoria(categoria);
            }

        } catch (SQLException e) {
            System.err.println("Error consultando la categoría: " + e.getMessage());
        }
    }

    public void obtenerProductoPorId() {
        System.out.println("\nOBTENER PRODUCTO POR ID");
        System.out.println("--------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a buscar: ");
            int id = reader.nextInt();

            Producto producto = productoDao.getById(id);

            if (producto == null) {
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            } else {
                printCabeceraProducto();
                printProducto(producto);
            }

        } catch (SQLException e) {
            System.err.println("Error consultando el producto: " + e.getMessage());
        }
    }

    public void insertarCategoria() {
        System.out.println("\nINSERTAR NUEVA CATEGORÍA");
        System.out.print("Introduzca el nombre de la categoría: ");
        String nombre = reader.nextLine();
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

        try {
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No se pueden crear productos porque no hay categorías registradas.");
                return;
            }

            // Mostrar las categorías disponibles
            System.out.println("\nCATEGORÍAS DISPONIBLES:");
            printCabeceraCategoria();
            categorias.forEach(this::printCategoria);

            // Solicitar datos del producto
            System.out.print("\nIntroduzca el nombre del producto: ");
            String nombre = reader.nextLine();
            System.out.print("Introduzca el precio del producto: ");
            double precio = reader.nextDouble();
            System.out.print("Introduzca el stock del producto: ");
            int stock = reader.nextInt();
            System.out.print("Introduzca el ID de la categoría: ");
            int categoriaId = reader.nextInt();

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
        System.out.println("\nACTUALIZACIÓN DE UNA CATEGORÍA");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID de la categoría a actualizar: ");
            int id = reader.nextInt();

            Categoria categoria = categoriaDao.getById(id);

            if (categoria == null) {
                System.out.println("No hay categorías registradas en la base de datos con ese ID");
            } else {
                printCabeceraCategoria();
                printCategoria(categoria);
                System.out.println("\n");

                System.out.printf("Introduzca el nuevo nombre de la categoría (%s): ", categoria.getNombre());
                String nuevoNombre = reader.nextLine();
                nuevoNombre = (nuevoNombre.isBlank()) ? categoria.getNombre() : nuevoNombre;

                categoria.setNombre(nuevoNombre);
                categoriaDao.update(categoria);

                System.out.println("Categoría actualizada correctamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error actualizando la categoría: " + e.getMessage());
        }
    }

    public void actualizarProducto() {
        System.out.println("\nACTUALIZACIÓN DE UN PRODUCTO");
        System.out.println("----------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a actualizar: ");
            int id = reader.nextInt();

            Producto producto = productoDao.getById(id);

            if (producto == null) {
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            } else {
                printCabeceraProducto();
                printProducto(producto);
                System.out.println("\n");

                // Mostrar las categorías disponibles
                System.out.println("\nCATEGORÍAS DISPONIBLES:");
                List<Categoria> categorias = categoriaDao.getAll();
                printCabeceraCategoria();
                categorias.forEach(this::printCategoria);

                // Solicitar nuevos datos del producto
                System.out.printf("\nIntroduzca el nuevo nombre del producto (%s): ", producto.getNombre());
                String nuevoNombre = reader.nextLine();
                nuevoNombre = (nuevoNombre.isBlank()) ? producto.getNombre() : nuevoNombre;

                System.out.printf("Introduzca el nuevo precio del producto (%.2f): ", producto.getPrecio());
                double nuevoPrecio = reader.nextDouble();
                nuevoPrecio = (nuevoPrecio == 0) ? producto.getPrecio() : nuevoPrecio;

                System.out.printf("Introduzca el nuevo stock del producto (%d): ", producto.getStock());
                int nuevoStock = reader.nextInt();
                nuevoStock = (nuevoStock == 0) ? producto.getStock() : nuevoStock;

                System.out.print("Introduzca el nuevo ID de la categoría: ");
                int categoriaId = reader.nextInt();

                Categoria categoria = categoriaDao.getById(categoriaId);
                if (categoria != null) {
                    producto.setNombre(nuevoNombre);
                    producto.setPrecio(nuevoPrecio);
                    producto.setStock(nuevoStock);
                    producto.setCategoria(categoria);

                    productoDao.update(producto);
                    System.out.println("Producto actualizado correctamente.");
                } else {
                    System.out.println("Categoría no encontrada.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error actualizando el producto: " + e.getMessage());
        }
    }

    public void eliminarCategoria() {
        System.out.println("\nELIMINAR CATEGORÍA");
        System.out.print("Introduzca el ID de la categoría a eliminar: ");
        int id = reader.nextInt();
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
        int id = reader.nextInt();
        try {
            productoDao.delete(id);
            System.out.println("Producto eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error eliminando el producto: " + e.getMessage());
        }
    }

    // Cabeceras
    private void printCabeceraCategoria() {
        System.out.printf("%2s %30s", "ID", "NOMBRE");
        System.out.println("");
        IntStream.range(1, 50).forEach(x -> System.out.print("-"));
        System.out.println("");
    }

    private void printCabeceraProducto() {
        System.out.printf("%2s %30s %10s %8s %25s", "ID", "NOMBRE", "PRECIO", "STOCK", "CATEGORÍA");
        System.out.println("");
        IntStream.range(1, 100).forEach(x -> System.out.print("-"));
        System.out.println("");
    }

    // Imprimir
    private void printCategoria(Categoria cat) {
        System.out.printf("%2s %30s\n",
                cat.getIdCategoria(),
                cat.getNombre());
    }

    private void printProducto(Producto producto) {
        System.out.printf("%2s %30s %8.2f %10d %25s\n",
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin categoría");
    }

    static class KeyboardReader {
        BufferedReader br;
        StringTokenizer st;

        public KeyboardReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    System.err.println("Error leyendo del teclado");
                    ex.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                if (st.hasMoreElements()) {
                    str = st.nextToken("\n");
                } else {
                    str = br.readLine();
                }
            } catch (IOException ex) {
                System.err.println("Error leyendo del teclado");
                ex.printStackTrace();
            }
            return str;
        }
    }
}
