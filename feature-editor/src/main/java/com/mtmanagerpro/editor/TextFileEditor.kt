package com.mtmanagerpro.editor

import java.io.File
import java.nio.charset.Charset

/** עורך טקסט בסיסי לקבצים שחולצו מה-ZIP */
class TextFileEditor {

    fun read(file: File): String = file.readText(Charset.forName("UTF-8"))

    fun save(file: File, content: String) {
        file.writeText(content, Charset.forName("UTF-8"))
    }
}
