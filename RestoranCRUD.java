/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.restorancrud;


/**
 *
 * @author Reyhand
 */
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestoranCRUD {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/restoran";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // Membuat tabel jika belum ada
            String createTableSQL = "CREATE TABLE IF NOT EXISTS menu_restoran " +
                    "(id INT AUTO_INCREMENT PRIMARY KEY, " +
                    " nama_makanan VARCHAR(255), " +
                    " harga_makanan INT, " +
                    " jumlah_pesanan INT, " +
                    " total_harga INT)";
            stmt.executeUpdate(createTableSQL);

            // Menu utama
            while (true) {
                System.out.println("1. Tambah Menu");
                System.out.println("2. Lihat Menu");
                System.out.println("3. Ubah Menu");
                System.out.println("4. Hapus Menu");
                System.out.println("0. Keluar");

                java.util.Scanner input = new java.util.Scanner(System.in);
                int choice = input.nextInt();

                switch (choice) {
                    case 1:
                        tambahMenu(conn);
                        break;
                    case 2:
                        lihatMenu(conn);
                        break;
                    case 3:
                        ubahMenu(conn);
                        break;
                    case 4:
                        hapusMenu(conn);
                        break;
                    case 0:
                        conn.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Pilihan tidak valid");
                        break;
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    static void tambahMenu(Connection conn) throws SQLException {
        java.util.Scanner input = new java.util.Scanner(System.in);

        System.out.print("Nama Makanan: ");
        String namaMakanan = input.nextLine();

        System.out.print("Harga Makanan: ");
        int hargaMakanan = input.nextInt();

        System.out.print("Jumlah Pesanan: ");
        int jumlahPesanan = input.nextInt();

        int totalHarga = hargaMakanan * jumlahPesanan;

        String insertSQL = "INSERT INTO menu_restoran (nama_makanan, harga_makanan, jumlah_pesanan, total_harga) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, namaMakanan);
            preparedStatement.setInt(2, hargaMakanan);
            preparedStatement.setInt(3, jumlahPesanan);
            preparedStatement.setInt(4, totalHarga);

            preparedStatement.executeUpdate();
        }

        System.out.println("Menu berhasil ditambahkan!");
    }

    static void lihatMenu(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String selectSQL = "SELECT * FROM menu_restoran";
        ResultSet rs = stmt.executeQuery(selectSQL);

        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Nama Makanan: " + rs.getString("nama_makanan"));
            System.out.println("Harga Makanan: " + rs.getInt("harga_makanan"));
            System.out.println("Jumlah Pesanan: " + rs.getInt("jumlah_pesanan"));
            System.out.println("Total Harga: " + rs.getInt("total_harga"));
            System.out.println("----------------------");
        }
    }

static void ubahMenu(Connection conn) throws SQLException {
    java.util.Scanner input = new java.util.Scanner(System.in);

    System.out.print("Masukkan ID menu yang akan diubah: ");
    int menuID = input.nextInt();
    input.nextLine();
        // Memeriksa apakah ID menu tersedia
        if (!isMenuExists(conn, menuID)) {
            System.out.println("ID menu tidak ditemukan.");
            return;
        }

        System.out.print("Nama Makanan Baru: ");
        String namaMakanan = input.nextLine(); // untuk menangkap new line
        namaMakanan = input.nextLine();

        System.out.print("Harga Makanan Baru: ");
        int hargaMakanan = input.nextInt();

        System.out.print("Jumlah Pesanan Baru: ");
        int jumlahPesanan = input.nextInt();

        int totalHarga = hargaMakanan * jumlahPesanan;

        String updateSQL = "UPDATE menu_restoran SET " +
                "nama_makanan = ?, " +
                "harga_makanan = ?, " +
                "jumlah_pesanan = ?, " +
                "total_harga = ? " +
                "WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, namaMakanan);
            preparedStatement.setInt(2, hargaMakanan);
            preparedStatement.setInt(3, jumlahPesanan);
            preparedStatement.setInt(4, totalHarga);
            preparedStatement.setInt(5, menuID);

            preparedStatement.executeUpdate();
        }

        System.out.println("Menu berhasil diubah!");
    }

    static void hapusMenu(Connection conn) throws SQLException {
        java.util.Scanner input = new java.util.Scanner(System.in);

        System.out.print("Masukkan ID menu yang akan dihapus: ");
        int menuID = input.nextInt();

        // Memeriksa apakah ID menu tersedia
        if (!isMenuExists(conn, menuID)) {
            System.out.println("ID menu tidak ditemukan.");
            return;
        }

        String deleteSQL = "DELETE FROM menu_restoran WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, menuID);

            preparedStatement.executeUpdate();
        }

        System.out.println("Menu berhasil dihapus!");
    }

    static boolean isMenuExists(Connection conn, int menuID) throws SQLException {
        String checkIDSQL = "SELECT * FROM menu_restoran WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(checkIDSQL)) {
            preparedStatement.setInt(1, menuID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        }
    }
}
