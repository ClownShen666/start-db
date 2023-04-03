import org.scalatest.FunSuite
import org.urbcomp.cupid.db.model.sample.ModelGenerator.{generateRoadNetwork, generateTrajectory}
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.mapmatch.MapMatch


class MapMatchTest extends FunSuite{

  test("test map matching") {
    val mapMatch: MapMatch = new MapMatch
    val rn = generateRoadNetwork()
    val traj = generateTrajectory()
    val trajArray = Array[Trajectory](traj)
    val result = mapMatch.mapMatch(rn,trajArray)

    assertResult(result.get(0).getMmPtList.size())(117)
  }

}
