package com.pupd.backend.data

import org.h2.tools.RunScript
import java.io.FileReader
import java.sql.Connection
import javax.sql.DataSource

/**
 * Runs a series of scripts identified by scriptPaths on the DataSource.
 * Scripts must be SQL scripts with valid H2 syntax.
 *
 * @param scriptPaths Relative paths to SQL scripts
 */
internal fun DataSource.runSetupScripts(vararg scriptPaths: String) {
    var conn: Connection? = null
    try {
        conn = this.connection
        scriptPaths.forEach { path ->
            FileReader(path).use { reader ->
                RunScript.execute(conn, reader)
            }
        }
    } finally {
        conn?.close()
    }
}
