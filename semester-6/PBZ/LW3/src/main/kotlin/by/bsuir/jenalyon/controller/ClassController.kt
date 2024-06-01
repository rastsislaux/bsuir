package by.bsuir.jenalyon.controller

import jakarta.servlet.http.HttpServletResponse
import org.apache.jena.ontology.OntModel
import org.apache.jena.ontology.OntModelSpec
import org.apache.jena.query.QueryExecution
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.query.ResultSetFormatter
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.springframework.web.bind.annotation.*


@RestController
class ClassController {

    final val ontModel: OntModel

    init {
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM)
        // ontModel.setNsPrefix("monkey", "http://evoflex.tech/monkey#")
    }

    @PostMapping("/ont/read")
    fun readOnt(@RequestBody url: String) {
        ontModel.read(url)
    }

    @PostMapping("/ont/clear")
    fun clearOnt() {
        ontModel.removeAll()
    }

    @PostMapping("/ont/save", produces = ["application/rdf+xml"])
    fun saveOnt(resp: HttpServletResponse) {
        ontModel.write(resp.writer)
    }

    @GetMapping("/class")
    fun getClasses(): List<String> {
        val classes = ontModel.listClasses().toList()
        return classes.map { it.nameSpace + it.localName }
    }

    @PostMapping("/class/delete")
    fun delete(@RequestBody path: String) {
        ontModel.getOntClass(path).remove()
    }

    @PostMapping("/class")
    fun create(@RequestParam name: String) {
        ontModel.createClass("http://evoflex.tech/monkey#$name")
    }

    @PostMapping("/relations/for")
    fun getRelations(@RequestBody path: String): List<Map<String, String>> {
        return ontModel.getResource(path).listProperties().toList()
            .filter { it.`object` is Resource }
            .map { it ->
                val prName = it.predicate.uri
                val obj = it.`object`.toString()

                return@map mapOf(
                    "prName" to prName,
                    "obj" to obj
                )
            }
    }

    @PostMapping("/relations/all")
    fun getAllRelations(): List<String> {
        return ontModel.listAllOntProperties().toList()
            .stream()
            .map { it.uri }
            .toList()
    }

    @PostMapping("/relations/delete")
    fun deleteRelation(@RequestBody map: Map<String, String>) {
        val (subj, pred, obj) = listOf(map["subj"], map["pred"], map["obj"])

        val subjRes = ontModel.getOntResource(subj)
        val predRes = ontModel.getProperty(pred)
        val objRes = ontModel.getResource(obj)

        subjRes.removeProperty(predRes, objRes)
    }

    @PostMapping("/sparql")
    fun sparql(@RequestBody sparql: String): String {
        val query = QueryExecutionFactory.create(sparql, ontModel)

        query.use {
            val results = it.execSelect()
            return ResultSetFormatter.asText(results)
        }
    }

    @PostMapping("/primitive/create")
    fun createPrimitive(@RequestBody map: Map<String, String>) {
        val (subj, pred, obj) = listOf(map["subj"], map["pred"], map["obj"])

        val subjRes = ontModel.getOntResource(subj)
        val predRes = ontModel.getProperty(pred)

        subjRes.addProperty(predRes, obj)
    }

    @PostMapping("/primitive/delete")
    fun deletePrimitive(@RequestBody map: Map<String, String>) {
        val (subj, pred) = listOf(map["subj"], map["pred"])

        val subjRes = ontModel.getOntResource(subj)
        val predRes = ontModel.getProperty(pred)

        subjRes.removeAll(predRes)
    }

    @PostMapping("/primitive/for")
    fun getPrimitives(@RequestBody path: String): List<Map<String, String>> {
        return ontModel.getResource(path).listProperties().toList()
            .filter { it.`object` !is Resource }
            .map { it ->
                val prName = it.predicate.uri
                val obj = it.`object`.toString()

                return@map mapOf(
                    "prName" to prName,
                    "obj" to obj
                )
            }
    }

    @PostMapping("/individuals/for")
    fun getIndividualsFor(@RequestBody path: String): List<String> {
        return ontModel.getOntClass(path).listInstances().toList()
            .map {
                it.uri
            }
    }

    @PostMapping("/individuals/create")
    fun createIndividual(@RequestBody map: Map<String, String>) {
        val clazz = ontModel.getOntClass(map["clazz"])
        clazz.createIndividual("http://evoflex.tech/monkey#" + map["name"])
    }

    @PostMapping("/individuals/delete")
    fun deleteIndividual(@RequestBody map: Map<String, String>) {
        ontModel.getIndividual(map["url"]).remove()
    }

    @PostMapping("/relations/create")
    fun createRelation(@RequestBody body: Map<String, String>) {
        val (subj, pred, obj) = listOf(body["subj"], body["pred"], body["obj"])
        val subjRes = ontModel.getResource(subj)
        val predRes = ontModel.getProperty(pred)
        val objRes = ontModel.getResource(obj)

        subjRes.addProperty(predRes, objRes)
    }

}