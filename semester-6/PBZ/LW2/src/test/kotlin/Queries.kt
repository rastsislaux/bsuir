import org.apache.jena.query.QueryExecution
import org.junit.jupiter.api.Test
import java.io.File

class Queries {

    private val ontology = OntologyFactory.createOntology("http://evoflex.tech/monkey#", "monkey", File("monkeys_final.owl"))

    fun runQuery(q: String) {
        val rs = QueryExecution.model(ontology.ontModel)
            .query(q)
            .build()
            .execSelect()

        while (rs.hasNext()) {
            println(rs.next())
        }
    }

    @Test
    fun query1() {
        runQuery("""
                PREFIX monkey: <http://evoflex.tech/monkey#>
                SELECT ?superfamily
                WHERE {
                  monkey:Lemur monkey:rang ?superfamily .
                }
            """.trimIndent())
    }

    @Test
    fun findMe() {
        runQuery("""
            PREFIX monkey: <http://evoflex.tech/monkey#>
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            
            SELECT ?me
            WHERE {
                ?me rdf:type monkey:Human
            }
        """.trimIndent())
    }

    @Test
    fun query2() {
        runQuery("""
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX monkey: <http://evoflex.tech/monkey#>
            SELECT ?subclass
            WHERE {
              ?subclass rdfs:subClassOf monkey:Primate .
            }
        """.trimIndent())
    }

    @Test
    fun query3() {
        runQuery("""
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX monkey: <http://evoflex.tech/monkey#>
            SELECT ?subclass
            WHERE {
              ?subclass rdfs:subClassOf monkey:RangOfClassification .
            }
        """.trimIndent())
    }

    @Test
    fun query4() {
        runQuery("""
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX monkey: <http://evoflex.tech/monkey#>

            SELECT ?monkey ?rang
            WHERE {
              ?monkey rdfs:subClassOf* monkey:Primate .
              ?monkey monkey:rang ?rang .
            }
        """.trimIndent())
    }

    @Test
    fun query5() {
        runQuery(
            """
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX monkey: <http://evoflex.tech/monkey#>
            
            SELECT ?monkey ?habitat
            WHERE {
                ?monkey rdfs:subClassOf* monkey:Primate .
                ?monkey monkey:habitat ?habitat .
            }
            """.trimIndent()
        )
    }

    @Test
    fun query6() {
        runQuery("""
            PREFIX owl: <http://www.w3.org/2002/07/owl#>
            SELECT ?class1 ?class2
            WHERE {
                ?class1 owl:equivalentClass ?class2 .
            }
        """.trimIndent())
    }

}