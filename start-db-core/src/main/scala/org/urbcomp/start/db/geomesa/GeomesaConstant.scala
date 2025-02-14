/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.start.db.geomesa

import org.apache.calcite.plan.Convention
import org.urbcomp.start.db.geomesa.rel.IGeomesaRelNode

object GeomesaConstant {

  val CONVENTION = new Convention.Impl("Geomesa", classOf[IGeomesaRelNode])

  val ST_WITHIN = "st_within"
  val ST_TOUCHES = "st_touches"
  val ST_OVERLAPS = "st_overlaps"
  val ST_INTERSECTS = "st_intersects"
  val ST_EQUALS = "st_equals"
  val ST_DISJOINT = "st_disjoint"
  val ST_COVERS = "st_covers"
  val ST_CROSSES = "st_crosses"
  val ST_CONTAINS = "st_contains"
}
