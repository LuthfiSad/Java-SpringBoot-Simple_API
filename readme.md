# Demo API

Ini adalah proyek API sederhana menggunakan Spring Boot. Proyek ini menyediakan endpoint untuk pendaftaran dan login pengguna serta akses ke data pengguna.

## Fitur

- **User Registration**: Endpoint untuk mendaftarkan pengguna baru.
- **User Login**: Endpoint untuk login pengguna.
- **Profile Management**: Endpoint untuk mengelola profil pengguna.

## Endpoints

### 1. User Registration

**URL**: `/auth/register`  
**Method**: `POST`  
**Description**: Mendaftarkan pengguna baru dengan email, password, nama, dan peran.  

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "admin"
}
```

**Responses**:
- `200 OK`: Berhasil mendaftarkan pengguna.
- `400 Bad Request`: Email sudah ada atau ada kesalahan validasi.

### 2. User Login

**URL**: `/auth/login`  
**Method**: `POST`  
**Description**: Login pengguna dengan email dan password.  

**Request Body**:
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Responses**:
- `200 OK`: Berhasil login, mengembalikan token JWT.
- `401 Unauthorized`: Email atau password salah.

### 3. Get User Profile

**URL**: `/profile/{id}`  
**Method**: `GET`  
**Description**: Mengambil profil pengguna berdasarkan ID.  

**Path Parameter**:
- `id`: ID pengguna.

**Responses**:
- `200 OK`: Mengembalikan data profil pengguna.
- `404 Not Found`: Pengguna tidak ditemukan.

## Dependensi

Proyek ini menggunakan dependensi berikut:

- **Spring Boot Starter Web**: Menyediakan fungsionalitas dasar untuk aplikasi web.
- **Spring Boot Starter Test**: Dependensi untuk pengujian.
- **Spring Boot DevTools**: Alat pengembangan untuk meningkatkan produktivitas.
- **Spring Boot Starter Validation**: Menyediakan validasi bean.
- **Spring Boot Starter Data MongoDB**: Dukungan untuk MongoDB.
- **Lombok**: Mengurangi boilerplate code dengan anotasi.
- **Spring Boot Starter Security**: Untuk keamanan dan encoding password.
- **JJWT**: Untuk pembuatan dan parsing token JWT.

## Cara Menjalankan Proyek

1. **Clone Repository**:
   ```bash
   git clone https://github.com/username/repository.git
   ```

2. **Masuk ke Direktori Proyek**:
   ```bash
   cd repository
   ```

3. **Jalankan Aplikasi**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Akses API**:
   - Anda dapat menggunakan alat seperti [Postman](https://www.postman.com/) atau `curl` untuk mengakses endpoint API.

## Kontribusi

Jika Anda ingin berkontribusi pada proyek ini, silakan ikuti langkah-langkah berikut:

1. Fork repositori ini.
2. Buat branch untuk perubahan Anda.
3. Lakukan commit pada perubahan.
4. Kirim pull request.
