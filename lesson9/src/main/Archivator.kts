import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Archiver {

    fun zipFolder(sourceDir: File, zipFile: File) {
        var zos: ZipOutputStream? = null

        try {
            zos = ZipOutputStream(FileOutputStream(zipFile))
            addFiles(sourceDir, sourceDir.name, zos)
        } catch (e: Exception) {
            println("ошибка в архивировании: ${e.message}")
        } finally {
            zos?.close()
        }
    }

    private fun addFiles(file: File, path: String, zos: ZipOutputStream) {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    addFiles(f, path + "/" + f.name, zos)
                }
            }
            return
        }

        if (!file.name.endsWith(".txt") && !file.name.endsWith(".log")) {
            return
        }

        println("добавляется: $path (${file.length()} байт)")

        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
            val entry = ZipEntry(path)
            zos.putNextEntry(entry)

            val buffer = ByteArray(1024)
            var count: Int

            while (true) {
                count = fis.read(buffer)
                if (count == -1) break
                zos.write(buffer, 0, count)
            }

        } catch (e: Exception) {
            println("ошибка в файле $path")
        } finally {
            fis?.close()
        }
    }
}

