# Hotel Booking Application

## Deskripsi Program

Aplikasi **Hotel Booking Application** adalah sebuah program berbasis Java dengan antarmuka grafis (GUI) yang memungkinkan pengguna untuk melakukan manajemen pemesanan kamar hotel. Program ini dibangun dengan pendekatan **Object-Oriented Programming (OOP)** yang memisahkan tanggung jawab setiap komponen ke dalam kelas yang berbeda untuk memastikan struktur yang jelas dan pemeliharaan yang mudah.

Aplikasi ini mendukung fitur:
- Menambahkan data pemesanan kamar hotel.
- Menghitung total biaya berdasarkan tipe kamar dan jumlah malam menginap.
- Menampilkan daftar pemesanan dalam bentuk tabel.
- Menghapus pemesanan tertentu.
- Mengimpor dan mengekspor data pemesanan ke/dari file CSV.

## Struktur Program

### 1. `Booking`
Kelas ini bertanggung jawab untuk merepresentasikan data pemesanan.

**Atribut:**
- `name` : Nama pelanggan.
- `roomType` : Jenis kamar yang dipilih.
- `nights` : Jumlah malam menginap.
- `checkInDate` : Tanggal check-in.
- `totalCost` : Total biaya pemesanan.

**Fungsi:**
- Getter untuk setiap atribut agar data dapat diakses.

---

### 2. `BookingService`
Kelas ini bertugas menangani logika bisnis untuk manajemen pemesanan.

**Fungsi Utama:**
- `getRoomTypes` : Mengembalikan daftar tipe kamar yang tersedia.
- `calculateCost` : Menghitung total biaya berdasarkan tipe kamar dan jumlah malam.
- `addBooking` : Menambahkan pemesanan baru ke dalam daftar.
- `deleteBooking` : Menghapus pemesanan tertentu dari daftar.
- `getBookings` : Mendapatkan seluruh daftar pemesanan.

**Data Internal:**
- Tipe kamar (`roomTypes`) dan harga kamar (`roomPrices`).

---

### 3. `BookingView`
Kelas ini bertanggung jawab untuk elemen antarmuka pengguna (GUI).

**Komponen Utama:**
- Input data:
    - Nama pelanggan
    - Tipe kamar (dropdown)
    - Jumlah malam menginap
    - Tanggal check-in
- Label untuk menampilkan total biaya pemesanan.
- Tabel untuk menampilkan daftar pemesanan.
- Tombol aksi:
    - Tambahkan pemesanan
    - Hapus pemesanan
    - Impor/ekspor data CSV

**Fungsi Utama:**
- `addBooking`: Menambahkan data pemesanan baru ke dalam tabel berdasarkan input pengguna.
- `deleteBooking`: Menghapus pemesanan yang dipilih pada tabel.
- Menampilkan gambar kamar berdasarkan tipe kamar yang dipilih.

---

### 4. `HostelmopOOP`
Kelas utama yang menjalankan program.

---

## Fitur Program

### 1. Menambahkan Pemesanan
Pengguna dapat menambahkan pemesanan dengan:
- Mengisi nama pelanggan.
- Memilih tipe kamar dari daftar (Standard, Deluxe, Suite).
- Menentukan jumlah malam menginap.
- Memasukkan tanggal check-in dalam format `yyyy-mm-dd`.

Program akan menghitung total biaya secara otomatis berdasarkan harga kamar dan jumlah malam.

### 2. Menghapus Pemesanan
Pengguna dapat memilih satu baris pada tabel dan menghapusnya dengan menekan tombol **Delete Booking**.

### 3. Impor/Ekspor Data
- **Impor**: Membaca data pemesanan dari file `bookings.csv` dan menampilkannya di tabel.
- **Ekspor**: Menyimpan data dari tabel ke file `bookings.csv`.

### 4. Menampilkan Total Biaya
Setelah menambahkan pemesanan, total biaya akan ditampilkan di tabel dan di label bawah input data.

### 5. Menampilkan Gambar Kamar
Gambar kamar akan diperbarui sesuai dengan tipe kamar yang dipilih.

---

## Teknologi yang Digunakan
- **Java Swing**: Untuk membangun antarmuka pengguna.
- **Java IO**: Untuk membaca dan menulis data ke/dari file CSV.
- **Java Time API**: Untuk memvalidasi dan memformat tanggal.

---

## Cara Menjalankan Program
1. Pastikan Anda telah menginstal **JDK 8** atau versi yang lebih baru.
2. Salin semua file `.java` ke dalam direktori proyek Anda.
3. Kompilasi semua file dengan perintah:
   ```
   javac *.java
   ```
4. Jalankan program dengan perintah:
   ```
   java HostelmopOOP
   ```
5. Aplikasi akan terbuka dengan antarmuka grafis.

---

## Struktur Direktori
```
|-- Booking.java         (Kelas untuk data pemesanan)
|-- BookingService.java  (Kelas untuk logika bisnis)
|-- BookingView.java     (Kelas untuk GUI)
|-- HostelmopOOP.java    (Kelas utama untuk menjalankan program)
|-- bookings.csv         (File CSV untuk menyimpan data pemesanan)
```

---

## Catatan Tambahan
- Pastikan file gambar kamar tersedia di jalur yang sesuai dengan program.
- Format tanggal yang valid adalah `yyyy-mm-dd`.
- Data dalam tabel akan hilang setelah aplikasi ditutup kecuali diekspor ke file CSV.

