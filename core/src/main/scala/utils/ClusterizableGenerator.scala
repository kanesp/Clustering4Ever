package org.clustering4ever.util
/**
 * @author Beck Gaël
 */
import scala.language.higherKinds
import scala.collection.{mutable, immutable}
import org.clustering4ever.vectorizables.Vectorizable
import org.clustering4ever.clusterizables.EasyClusterizable
import org.clustering4ever.clustering.ClusteringArgs
import org.clustering4ever.vectors.GVector
/**
 *
 */
object ClusterizableGenerator {
	/**
	 *
	 */
	def obtainEasyClusterizable[ID, V <: GVector[V]](id: ID, vector: V): EasyClusterizable[ID, V, V] = new EasyClusterizable(id, new Vectorizable(vector), vector)
}