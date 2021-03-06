/*
 * HawkEye Redux
 * Copyright (C) 2012-2013 Cubeville <http://www.cubeville.org> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.cubeville.hawkeye.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cubeville.hawkeye.HawkEye;

public class DatabaseUtil {

	/**
	 * Gets the name of specified table with the prefix attached
	 *
	 * @param table Table to get
	 * @return Table name with prefix
	 */
	public static String table(String table) {
		return "`" + HawkEye.getDatabase().getPrefix() + "_" + table + "`";
	}

	/**
	 * Attempts to create a database table
	 *
	 * @param table Name of the table to create
	 * @param query Query to create the table
	 * @return True if table is created (or already exists), false if not
	 */
	public static boolean createTable(String table, String query) {
		if (tableExists(table)) return true;

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = HawkEye.getDatabase().getConnection();
			ps = conn.prepareStatement(query);
			ps.execute();

			if (tableExists(table)) return true;
		} catch (SQLException e) {
			HawkEye.getLogger().error("Could not create table '" + table + "'", e);
		} finally {
			close(conn);
			close(ps);
		}

		return false;
	}

	/**
	 * Checks if the specified table exists in the database
	 *
	 * @param table Name of the table to check for
	 * @return True if table exists, false if not
	 */
	public static boolean tableExists(String table) {
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = HawkEye.getDatabase().getConnection();

			// Check if table exists
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, table, null);

			if (rs.next()) return true;
		} catch (SQLException e) {
			HawkEye.getLogger().error("Unable to determine if table '" + table + "' exists", e);
		} finally {
			close(conn);
			close(rs);
		}

		return false;
	}

	/**
	 * Wrapper method to close a database connection
	 *
	 * @param conn Connection to close
	 */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Wrapper method to close a statement
	 *
	 * @param stmt Statement to close
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Wrapper method to close a result set
	 *
	 * @param rs Result set to close
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ignore) {
			}
		}
	}

}
