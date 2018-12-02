package org.clustering4ever.util
/**
 * @author Beck Gaël
 */
import scala.reflect.ClassTag
import scala.math.sqrt
import scala.collection.GenSeq
import scala.language.higherKinds
import spire.math.{Numeric => SNumeric}
import org.clustering4ever.scala.measurableclass.BinaryScalarVector
import scala.collection.mutable
/**
 *
 */
object VectorsBasicOperationsImplicits {
	/**
	 *
	 */
	implicit def addClassicVectors[@specialized(Int, Double) N, V[X] <: Seq[X]](v1: V[N], v2: V[N])(implicit num: SNumeric[N]): V[N] = {
		val builder = v1.genericBuilder.asInstanceOf[mutable.Builder[N, V[N]]]
		builder.sizeHint(v1.size)
		(0 until v1.size).foreach( i => builder += num.plus(v1(i), v2(i)) )
		builder.result
	}
	/**
	 *
	 */
	implicit def addMixtVectors[Vb[Int] <: Seq[Int], Vs[Double] <: Seq[Double]](v1: BinaryScalarVector[Vb[Int], Vs[Double]], v2: BinaryScalarVector[Vb[Int], Vs[Double]]): BinaryScalarVector[Vb[Int], Vs[Double]] = {
		val binaryPart = addClassicVectors(v1.binary, v2.binary)
		val scalarPart = addClassicVectors(v1.scalar, v2.scalar)
		new BinaryScalarVector(binaryPart, scalarPart)
	}
}
/**
 * Object which gather common operation on Vectors of any nature, aka scalar, binary, mixt
 */
object SumVectors {

	import VectorsBasicOperationsImplicits._
	/**
	 * add two vector no mather their types
	 */
	def sumVectors[V](v1: V, v2: V)(implicit f: (V, V) => V): V = f(v1, v2)
	/**
	 * Reduce an Array[Array[N]] into an Array[N]
	 */
	def sumColumnMatrix[V](cluster: GenSeq[V])(implicit f: (V, V) => V): V = cluster.reduce(sumVectors(_, _))
	/**
	 * Reduce Array of multiple vectors
	 */
	def reduceMultipleVectorsMatrice[V <: Seq[Double], It[X] <: Seq[X]](a: It[V], b: It[V])(implicit f: (V, V) => V) = {	
		val range = (0 until a.size)
		val builder = a.genericBuilder.asInstanceOf[mutable.Builder[V, It[V]]]
		builder.sizeHint(a.size)
		range.foreach{ i =>
			builder += sumVectors(a(i), b(i))
		}
		builder.result
	}
	/**
	 *
	 */
	def norm[V <: Seq[Double]](dot: V): Double = {
	  var s = 0D
	  var i = 0
	  while( i < dot.size ) {
	    val v = dot(i)
	    s += v * v
	    i += 1
	  }
	  sqrt(s)
	}
	/**
	 *
	 */
	def dotProd[V <: Seq[Double]](dot1: V, dot2: V): Double = {
		var dp = 0D
		var i = 0
		while( i < dot1.size ) {
			dp += dot1(i) * dot2(i)
			i += 1
		}
		dp
	}
}