import java.net.HttpURLConnection
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// ===========================================
// Задача 2. REST — полный CRUD
// ===========================================
// Цель: реализовать все CRUD-операции для ресурса /posts.
// API: https://jsonplaceholder.typicode.com/posts
//
// TODO 1: Реализовать sendRequest() — универсальную функцию отправки запросов
// TODO 2: Реализовать 5 CRUD-функций (ниже)
// TODO 3: Вызвать каждую функцию в main() и вывести результат
//
// Вопросы после выполнения:
//   - В чём разница между PUT и PATCH?
//   - Почему POST возвращает 201, а PUT возвращает 200?
//   - Какой метод идемпотентный, а какой нет?

val BASE_URL = "https://jsonplaceholder.typicode.com/posts"

/** Универсальная функция для отправки HTTP-запросов.
 *  @param urlStr  — полный URL
 *  @param method  — HTTP-метод (GET, POST, PUT, DELETE)
 *  @param body    — тело запроса в формате JSON (null для GET/DELETE)
 *  @return Pair(statusCode, responseBody)
 */
fun sendRequest(urlStr: String, method: String, body: String? = null): Pair<Int, String> {
    // Открываем соединение
    val url = URL(urlStr)
    val connection = url.openConnection() as HttpURLConnection

    // Выставляем метод
    connection.requestMethod = method.uppercase()

    // Если есть тело запроса (для POST/PUT)
    if (body != null) {
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")

        // Записываем тело
        connection.outputStream.use { os ->
            val input = body.toByteArray(Charsets.UTF_8)
            os.write(input, 0, input.size)
        }
    }

    // Получаем код ответа
    val statusCode = connection.responseCode

    // Читаем ответ (inputStream для успешных запросов, errorStream для ошибок)
    val responseBody = try {
        val stream = if (statusCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        }
        stream.bufferedReader(Charsets.UTF_8).readText()
    } catch (e: Exception) {
        ""
    }

    // Закрываем соединение
    connection.disconnect()

    return Pair(statusCode, responseBody)
}

/** GET /posts — получить все посты */
fun getPosts(): String {
    // Отправляем GET запрос к /posts
    val (statusCode, responseBody) = sendRequest(
        urlStr = BASE_URL,
        method = "GET"
    )

    // Проверяем статус код
    return if (statusCode in 200..299) {
        responseBody  // возвращаем тело ответа с постами
    } else {
        "Ошибка $statusCode: $responseBody"
    }
}
/** GET /posts/{id} — получить пост по ID */
fun getPost(id: Int): String {
    val (statusCode, responseBody) = sendRequest(
        urlStr = "$BASE_URL/$id",
        method = "GET"
    )

    return if (statusCode in 200..299) {
        responseBody
    } else {
        "Ошибка $statusCode: $responseBody"
    }
}

fun createPost(json: String): String {
    val (statusCode, responseBody) = sendRequest(
        urlStr = BASE_URL,
        method = "POST",
        body = json
    )

    return if (statusCode in 200..299) {
        responseBody
    } else {
        "Ошибка $statusCode: $responseBody"
    }
}

fun updatePost(id: Int, json: String): String {
    val (statusCode, responseBody) = sendRequest(
        urlStr = "$BASE_URL/$id",
        method = "PUT",
        body = json
    )

    return if (statusCode in 200..299) {
        responseBody
    } else {
        "Ошибка $statusCode: $responseBody"
    }
}

fun deletePost(id: Int): Int {
    val (statusCode, _) = sendRequest(
        urlStr = "$BASE_URL/$id",
        method = "DELETE"
    )
    return statusCode
}

fun disableSslVerification() {
    val trustAll = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustAll, java.security.SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
}

fun main() {
    disableSslVerification()

    println("=== GET ALL ===")
    println(getPosts())

    println("\n=== GET ONE ===")
    println(getPost(1))

    println("\n=== CREATE ===")
    println(createPost("""{"title":"test","body":"hello","userId":1}"""))

    println("\n=== UPDATE ===")
    println(updatePost(1, """{"id":1,"title":"updated","body":"new","userId":1}"""))

    println("\n=== DELETE ===")
    println(deletePost(1))
}

main()