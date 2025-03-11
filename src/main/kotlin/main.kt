import java.io.IO.println

data class Task(
    // Cuma ID yang immutable karena mau bisa update title dan desc
    val id: Int,              // ID (immutable)
    var title: String,        // Judul task (mutable)
    var description: String?, // Deskripsi opsional (bisa null)
    var priority: Int,        // Prioritas (1-5)
    var isCompleted: Boolean  // Status penyelesaian (bisa diubah)
)

class TaskManager {
    // Penyimpanan dalam memori menggunakan MutableList
    private val tasks = mutableListOf<Task>()
    private var lastId = 0  // Counter untuk ID otomatis

    // Membuat task baru
    fun createTask(title: String, description: String?, priority: Int) {
        Task(
            id = ++lastId,
            title = title,
            description = description,
            priority = priority,
            isCompleted = false
        ).apply {  // Apply untuk inisialisasi
            tasks.add(this)     // Menambahkan ke list
            println("Task dibuat: $this")
        }
    }

    // Memperbarui task yang ada
    fun updateTask(id: Int, newTitle: String?, newDescription: String?, newPriority: Int?) {
        tasks.find { it.id == id }?.also { task ->  // Menggunakan also untuk side effects
            // Mengupdate properti jika nilai baru tidak null
            newTitle?.let { task.title = it }
            newDescription?.let { task.description = it }
            newPriority?.let { task.priority = it }
            println("Task diperbarui: $task")
        } ?: run {  // Menggunakan run untuk menangani kasus null
            println("Task dengan ID $id tidak ditemukan!")
        }
    }

    // Mengganti status task
    fun toggleCompTask(id: Int) {
        tasks.find { it.id == id }?.let { task ->
            task.isCompleted = !task.isCompleted
            println("Status task telah diubah: ${task.title} ${if (task.isCompleted) "Selesai" else "Dalam Proses"}")
        } ?: println("Task dengan ID $id tidak ditemukan!")
    }

    // Menghapus task
    fun deleteTask(id: Int) {
        tasks.removeIf { it.id == id }.also { removed ->  // Menggunakan also untuk hasil operasi
            if (removed) println("Task dihapus")
            else println("Gagal menghapus: Task tidak ditemukan")
        }
    }

    // Menampilkan semua task
    fun listTasks() {
        if (tasks.isEmpty()) {
            println("Tidak ada task tersedia")
            return
        }
        println("\nDaftar Task:")
        tasks.forEachIndexed { index, task ->  // Iterasi dengan index
            println("${index + 1}. [ID: ${task.id}] ${task.title} - " +
                    "Prioritas: ${task.priority} " +
                    "(${if (task.isCompleted) "Selesai" else "Dalam Proses"})")
        }
    }
}

fun main() {
    val manager = TaskManager()
    val scanner = java.util.Scanner(System.`in`)

    while (true) {
        print(
            """
            === Aplikasi Manajemen Task ===
            1. Buat Task Baru
            2. Lihat Semua Task
            3. Update Task
            4. Ubah Status Task
            5. Hapus Task
            6. Keluar
            Pilihan Anda: 
            """.trimIndent()
        )

        when (scanner.nextInt()) {
            1 -> {
                // Input untuk membuat task
                print("Masukkan judul task: ")
                val title = readln()

                print("Masukkan deskripsi (opsional): ")
                val desc = readln().takeIf { it.isNotEmpty() }

                print("Masukkan prioritas (1-5): ")
                val inputPriority  = readln().toIntOrNull() ?: 3  // Default ke 3 jika invalid
                val priority = inputPriority.takeIf { it in 1..5 } ?: run {
                    println("Input tidak valid atau lebih dari 5, menggunakan nilai default: 3")
                    3
                }
                manager.createTask(title, desc, priority)
            }
            2 -> manager.listTasks()
            3 -> {
                // Input untuk update task
                print("Masukkan ID task yang akan diupdate: ")
                val id = readln().toInt()

                print("Masukkan judul baru (kosongkan jika tidak ingin mengubah): ")
                val newTitle = readln().takeIf { it.isNotEmpty() }

                print("Masukkan deskripsi baru (kosongkan jika tidak ingin mengubah): ")
                val newDesc = readln().takeIf { it.isNotEmpty() }

                print("Masukkan prioritas baru (kosongkan jika tidak ingin mengubah): ")
                val newPriority = readln().toIntOrNull()

                manager.updateTask(id, newTitle, newDesc, newPriority)
            }
            4 -> {
                // Input untuk mengubah status task
                println("Masukan ID Task yang ingin diubah status: ")
                val id = readln().toInt()
                manager.toggleCompTask(id)
            }
            5 -> {
                // Input untuk menghapus task
                print("Masukkan ID task yang akan dihapus: ")
                val id = readln().toInt()
                manager.deleteTask(id)
            }
            6 -> {
                println("Terima kasih! Sampai jumpa.")
                return
            }
            else -> println("Pilihan tidak valid!")
        }
    }
}