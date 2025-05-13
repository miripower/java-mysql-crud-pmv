package com.polmirvid;

import com.polmirvid.dao.Categoria.CategoriaDao;
import com.polmirvid.dao.Categoria.CategoriaDaoImpl;
import com.polmirvid.dao.Producto.ProductoDao;
import com.polmirvid.dao.Producto.ProductoDaoImpl;
import com.polmirvid.model.Categoria;
import com.polmirvid.model.Producto;
import com.polmirvid.pool.MyDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class App {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.init();
        //testDao();
        //testPool();
    }

    public static void testDao() {
        CategoriaDao cDao = CategoriaDaoImpl.getInstance();
        ProductoDao pDao = ProductoDaoImpl.getInstance();

        //Crear Categoria y Producto
        Categoria cat  = new Categoria("Camisetas");
        Producto prod  = new Producto("Camiseta Blanca", 25.99, 10, cat);

        try {
            //Insertar Categoria y Producto
            int n = cDao.add(cat);
            int m = pDao.add(prod);
            System.out.println("El numero de registros insertados es: " + (m+n));

            //ListAll
            List<Categoria> categorias = cDao.getAll();
            List<Producto> productos = pDao.getAll();

            boolean hayCategorias = !categorias.isEmpty();
            boolean hayProductos = !productos.isEmpty();

            if (!hayCategorias) {
                System.out.println("No hay categorias");
            }
            if (!hayProductos) {
                System.out.println("No hay productos");
            }
            if (hayCategorias && hayProductos) {
                categorias.forEach(System.out::println);
                productos.forEach(System.out::println);
            }

            //GetById
            int idCat = 1;
            int idProd = 1;

            Categoria cat1 = cDao.getById(idCat);
            Producto prod1= pDao.getById(idProd);

            System.out.println("\nCategoria " + idCat + " = " + cat1);
            System.out.println("Producto " + idProd + " = " + prod1);

            //Update
            cat1.setNombre("Sudaderas");
            prod1.setNombre("Sudadera Negra");
            prod1.setPrecio(69.95);
            prod1.setStock(5);

            n = cDao.update(cat1);
            m = pDao.update(prod1);

            System.out.println(cat1);
            System.out.println(prod1);

            // Delete
            cDao.delete(idCat);
            pDao.delete(idProd);

            categorias = cDao.getAll();
            productos = pDao.getAll();

            hayCategorias = !categorias.isEmpty();
            hayProductos = !productos.isEmpty();

            System.out.println("\n--- Tras borrar ---");

            if (!hayCategorias) {
                System.out.println("No hay categorias");
            } else {
                categorias.forEach(System.out::println);
            }

            if (!hayProductos) {
                System.out.println("No hay productos");
            } else {
                productos.forEach(System.out::println);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testPool() {
        try (Connection conn = MyDataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
