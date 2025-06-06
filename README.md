Anggota Kelompok:
Vinsensius Christopher Nathaniel Arden - (00000048268)
Mario Ichwansyah Bhakti - (00000061622)
Sifa Nur Faizah - (00000082044) 
Raqqat Amarasangga Iswahyudi - (00000057063)


Tampilan dan navigasi apa saja yang sudah berhasil dibuat:

#### 1. Login & Sign Up Screen
   - Sudah Ada:
     - Navigasi ke `journey` setelah login dan signup.
     - Implementasi di `LoginScreen` dan `SignUpScreen` berjalan lancar.
   - Tampilan:
     - Form input dan tombol sudah ada (walau belum dijelaskan detail UI di kode yang kamu share sebelumnya).



#### 2. Journey Activity (Main Feature)
   - Sudah Ada:
     - Menampilkan daftar catatan/jurnal pengguna.
     - App Bar:
       - Teks "Hi, Vinsen 👋" (dinamis jika username diintegrasikan lebih lanjut).
       - Tombol Search dan Profile sudah berfungsi dan navigasi jalan.
     - Floating Action Button (FAB):
       - New Entry untuk menambah catatan baru.
     - Bottom Navigation:
       - Navigasi ke Journey, Calendar, Media, Atlas sudah berfungsi.



#### 3. New Entry Activity
   - Sudah Ada:
     - Halaman `NewEntryScreen` yang memungkinkan pengguna membuat entry baru.
     - Tombol Save, Media (imageUri), dan Geotag (location) sudah ada dalam kode callback di `NavGraph` dan `JourneyScreen` (walaupun UI pengunggahan gambar dan geotag belum diperlihatkan secara lengkap).
   - Navigasi ke Journey setelah save berhasil.



#### 4. Entry Detail Screen
   - Sudah Ada:
     - Menampilkan detail dari jurnal yang dipilih (`EntryDetailScreen`).
     - Ada gambar, tanggal & waktu, isi catatan.
     - App Bar dengan tombol Back, Option Menu untuk Edit & Delete (sudah diimplementasikan di `EntryDetailScreen`).



#### 5. Edit Screen
   - Sudah Ada:
     - Mirip dengan `NewEntryScreen` (kode `EditEntryScreen`).
     - Opsi Delete dengan pop-up konfirmasi sudah ada.
     - Navigasi Back setelah update/delete bekerja.



#### 6. Calendar Screen
   - Sudah Ada:
     - Kalender dengan penanda tanggal yang punya jurnal (`CalendarScreen`).
     - Tanggal yang memiliki jurnal dapat diklik dan menampilkan Dialog dengan isi jurnal.



#### 7. Media Screen
   - Sudah Ada:
     - Menampilkan semua gambar dari catatan (`MediaScreen`).
     - Klik gambar membuka `EntryDetailScreen` terkait (berdasarkan `NavGraph`).



#### 8. Atlas Screen
   - Sudah Ada:
     - `AtlasScreen` menampilkan peta (`GoogleMap`) dengan pin lokasi dari jurnal yang memiliki geotag.
     - Menggunakan `Google Maps Compose` dan `Marker` untuk masing-masing entry.



#### 9. Tablet Responsive Layout
   - Sebagian Ada:
     - `WindowSizeClass` di `MainActivity` sudah digunakan.
     - Bottom Navigation disembunyikan di tablet layout, namun belum ada `NavigationRail` yang konsisten.
     - Responsivitas belum sepenuhnya optimal, tetapi struktur kode mendukung pengembangan lebih lanjut.



#### 10. Search Screen
   - Sudah Ada:
     - Tampilan `SearchScreen` tersedia, namun fungsi search/filter data belum diimplementasikan sepenuhnya** (perlu integrasi filter berdasarkan keyword dari daftar `journals`).
