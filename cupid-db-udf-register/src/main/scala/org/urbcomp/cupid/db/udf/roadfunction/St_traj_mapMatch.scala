package org.urbcomp.cupid.db.udf.roadfunction

class St_traj_mapMatch extends AbstractUdf {
  override def name(): String = "st_traj_mapMatch"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(rnStr: String, trajectory: Trajectory): String = {
    val roadNetwork = RoadNetwork.fromGeoJSON(rnStr)
    val mapMatcher = new TiHmmMapMatcher(roadNetwork, new ManyToManyShortestPath(roadNetwork))
    val matchedTraj = mapMatcher.mapMatch(trajectory)
    matchedTraj.toGeoJSON
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (String, Trajectory) => String = evaluate
}
