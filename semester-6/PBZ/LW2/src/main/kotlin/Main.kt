import com.google.gson.Gson
import org.apache.jena.ontology.DatatypeProperty
import org.apache.jena.ontology.OntClass
import org.apache.jena.ontology.OntModel
import org.apache.jena.ontology.OntModelSpec
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Property
import org.apache.jena.rdf.model.Resource
import org.apache.jena.vocabulary.XSD
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter
import java.io.Writer

interface Ontology {
    fun createClass(name: String): OntologyClass
    fun write(writer: Writer)
    fun getClass(name: String): OntologyClass?
    fun createDatatypeProperty(
        name: String,
        domain: OntologyResource,
        range: OntologyResource
    ): OntologyDataTypeProperty

    val ontModel: OntModel
}

interface OntologyResource {
    fun getResource(): Resource;
}

class OntologyResourceImpl(private val context: OntologyImpl, private val resource: Resource): OntologyResource {

    override fun getResource(): Resource {
        return resource
    }

}

interface OntologyDataTypeProperty: OntologyResource {
    val domain: OntologyResource
}

class OntologyDataTypePropertyImpl(
    private val context: OntologyImpl,
    private val datatypeProperty: DatatypeProperty
): OntologyDataTypeProperty {

    override val domain: OntologyResource
        get() = OntologyClassImpl(context, datatypeProperty.domain as OntClass)

    override fun getResource(): Resource {
        return datatypeProperty
    }

}


interface OntologyClass: OntologyResource {
    fun createSubclass(name: String): OntologyClass
    fun createSuperclass(name: String): OntologyClass
    fun setStringProperty(property: OntologyDataTypeProperty, value: String)
    fun setProperty(property: OntologyDataTypeProperty, value: OntologyResource)
}

class OntologyClassImpl(
    private val context: OntologyImpl,
    private val ontClass: OntClass
): OntologyClass {

    override fun getResource(): Resource {
        return ontClass
    }

    override fun createSubclass(name: String): OntologyClass {
        val clazz = ontClass.ontModel.createClass(context.ns + name)
        ontClass.addSubClass(clazz)
        return OntologyClassImpl(context, clazz)
    }

    override fun createSuperclass(name: String): OntologyClass {
        val clazz = ontClass.ontModel.createClass(context.ns + name)
        ontClass.addSuperClass(clazz)
        return OntologyClassImpl(context, clazz)
    }

    override fun setStringProperty(property: OntologyDataTypeProperty, value: String) {
        val literal = context.ontModel.createTypedLiteral(value)
        val st = context.ontModel.createStatement(this.ontClass, property.getResource() as Property, literal)
        context.ontModel.add(st)
    }

    override fun setProperty(property: OntologyDataTypeProperty, value: OntologyResource) {
        val st = context.ontModel.createStatement(this.ontClass, property.getResource() as Property, value.getResource())
        context.ontModel.add(st)
    }

    override fun toString(): String {
        return "SFOntologyClass[${ontClass.nameSpace}${ontClass.localName}]"
    }
}

class OntologyImpl(
    val ns: String,
    val prefix: String
): Ontology {

    override val ontModel: OntModel

    init {
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM)
        ontModel.setNsPrefix(prefix, ns)
    }

    override fun createClass(name: String): OntologyClass {
        val clazz = ontModel.createClass(ns + name)
        return OntologyClassImpl(this, clazz)
    }

    override fun createDatatypeProperty(
        name: String,
        domain: OntologyResource,
        range: OntologyResource
    ): OntologyDataTypeProperty {
        val prop = ontModel.createDatatypeProperty(ns + name)
        prop.setDomain(domain.getResource())
        prop.setRange(range.getResource())
        return OntologyDataTypePropertyImpl(this, prop)
    }

    override fun getClass(name: String): OntologyClass? {
        val clazz = ontModel.getOntClass(ns + name)
        return clazz?.let { OntologyClassImpl(this, it) }
    }

    override fun write(writer: Writer) {
        ontModel.write(writer)
    }

}

object OntologyFactory {

    fun createOntology(ns: String, prefix: String): Ontology {
        return OntologyImpl(ns, prefix)
    }

    fun createOntology(ns: String, prefix: String, file: File): Ontology {
        val ontology = createOntology(ns, prefix)
        ontology.ontModel.read(file.path)
        return ontology
    }

    fun createOntologyWithHierarchy(ns: String, prefix: String, func: HierarchySpec.() -> Unit): Ontology {
        val ontology = createOntology(ns, prefix)
        val spec = HierarchySpec(ontology)
        func(spec)
        return ontology
    }

    class HierarchySpec(
        private val ontology: Ontology
    ) {

        fun clazz(name: String, func: InnerHierarchySpec.() -> Unit = { }): Ontology {
            val clazz = ontology.createClass(name)
            func(InnerHierarchySpec(clazz))
            return ontology
        }

    }

    class InnerHierarchySpec(
        private val clazz: OntologyClass
    ) {

        fun clazz(name: String, func: InnerHierarchySpec.() -> Unit = { }) {
            val newClazz = clazz.createSubclass(name)
            func(InnerHierarchySpec(newClazz))
        }

    }

}

const val YAML_HIERARCHY = """Primate:
  Strepsirrhini:
    Lemuriformes:
      Lemur:
    Lorisiformes:
      Potto:
      Galago:
      Lori:
    Tarsiiformes:
      Tarsier:
  Haplorrhini:
    Plathyrini:
      Callitrichidae:
        Tarmarin:
        Marmoset:
      Cebidae:
        Squirrel:
        Capuchin:
      Atelidae:
        Spider:
        Howler:
        Wooly:
        Muriquis:
      Pitheciidae:
        Saki:
        Uakari:
        Titi:
      Aotidae:
        Night:
    Catarhini:
      Hominoidea:
        Hominidae:
          Ponginae:
            Orangutan:
          Homininae:
            Gorillini:
              Gorilla:
            Paninini:
              Chimp:
              Bonobo:
            Hominini:
              Human:
        Hylobatidae:
          Gibbon:
          Siamang:
      Cercopithecoidea:
        Cercopithecidae:
          Colobinae:
          Cercopithecinae:"""

val HABITATS = """{
  "Primate": {
    "habitat": "Varies depending on the species"
  },
  "Strepsirrhini": {
    "habitat": "Varies depending on the species"
  },
  "Lemuriformes": {
    "habitat": "Forests of Madagascar"
  },
  "Lemur": {
    "habitat": "Forests of Madagascar"
  },
  "Lorisiformes": {
    "habitat": "Tropical forests of South and Southeast Asia"
  },
  "Potto": {
    "habitat": "Tropical forests of Central and West Africa"
  },
  "Galago": {
    "habitat": "Various habitats in sub-Saharan Africa"
  },
  "Lori": {
    "habitat": "Rainforests of Southeast Asia"
  },
  "Tarsiiformes": {
    "habitat": "Southeast Asia"
  },
  "Tarsier": {
    "habitat": "Forests of Southeast Asia"
  },
  "Haplorrhini": {
    "habitat": "Varies depending on the species"
  },
  "Plathyrini": {
    "habitat": "Varies depending on the species"
  },
  "Callitrichidae": {
    "habitat": "South and Central America"
  },
  "Tamarin": {
    "habitat": "Tropical rainforests of South and Central America"
  },
  "Marmoset": {
    "habitat": "Tropical rainforests of South America"
  },
  "Cebidae": {
    "habitat": "Central and South America"
  },
  "Squirrel": {
    "habitat": "Varies depending on the species"
  },
  "Capuchin": {
    "habitat": "Tropical forests of Central and South America"
  },
  "Atelidae": {
    "habitat": "Central and South America"
  },
  "Spider": {
    "habitat": "Varies depending on the species"
  },
  "Howler": {
    "habitat": "Tropical forests of Central and South America"
  },
  "Wooly": {
    "habitat": "Tropical forests of South America"
  },
  "Muriquis": {
    "habitat": "Atlantic coastal forests of Brazil"
  },
  "Pitheciidae": {
    "habitat": "Tropical forests of South America"
  },
  "Saki": {
    "habitat": "Tropical rainforests of South America"
  },
  "Uakari": {
    "habitat": "Amazon rainforest and flooded forests of South America"
  },
  "Titi": {
    "habitat": "Tropical forests of South America"
  },
  "Aotidae": {
    "habitat": "Tropical rainforests of Central and South America"
  },
  "Night": {
    "habitat": "Tropical rainforests of Central and South America"
  },
  "Catarhini": {
    "habitat": "Varies depending on the species"
  },
  "Hominoidea": {
    "habitat": "Varies depending on the species"
  },
  "Hominidae": {
    "habitat": "Varies depending on the species"
  },
  "Ponginae": {
    "habitat": "Southeast Asia"
  },
  "Orangutan": {
    "habitat": "Rainforests of Borneo and Sumatra"
  },
  "Homininae": {
    "habitat": "Varies depending on the species"
  },
  "Gorillini": {
    "habitat": "Forests of Central and Western Africa"
  },
  "Gorilla": {
    "habitat": "Forests of Central and Western Africa"
  },
  "Paninini": {
    "habitat": "Forests of Central and Western Africa"
  },
  "Chimp": {
    "habitat": "Forests of Central and Western Africa"
  },
  "Bonobo": {
    "habitat": "Forests of the Democratic Republic of Congo"
  },
  "Hominini": {
    "habitat": "Varies depending on the species"
  },
  "Human": {
    "habitat": "Varies depending on the population"
  },
  "Hylobatidae": {
    "habitat": "Southeast Asia"
  },
  "Gibbon": {
    "habitat": "Tropical rainforests of Southeast Asia"
  },
  "Siamang": {
    "habitat": "Tropical rainforests of Southeast Asia"
  },
  "Cercopithecoidea": {
    "habitat": "Varies depending on the species"
  },
  "Cercopithecidae": {
    "habitat": "Varies depending on the species"
  },
  "Colobinae": {
    "habitat": "Varies depending on the species"
  },
  "Cercopithecinae": {
    "habitat": "Varies depending on the species"
  }
}"""

val RANGS = """{
    "Primate": {
    "habitat": "Varies depending on the species",
    "rang": "Order"
    },
    "Strepsirrhini": {
    "habitat": "Varies depending on the species",
    "rang": "Suborder"
    },
    "Lemuriformes": {
    "habitat": "Forests of Madagascar",
    "rang": "Infraorder"
    },
    "Lemur": {
    "habitat": "Forests of Madagascar",
    "rang": "Superfamily"
    },
    "Lorisiformes": {
    "habitat": "Tropical forests of South and Southeast Asia",
    "rang": "Infraorder"
    },
    "Potto": {
    "habitat": "Tropical forests of Central and West Africa",
    "rang": "Family"
    },
    "Galago": {
    "habitat": "Various habitats in sub-Saharan Africa",
    "rang": "Genus"
    },
    "Lori": {
    "habitat": "Rainforests of Southeast Asia",
    "rang": "Genus"
    },
    "Tarsiiformes": {
    "habitat": "Southeast Asia",
    "rang": "Infraorder"
    },
    "Tarsier": {
    "habitat": "Forests of Southeast Asia",
    "rang": "Family"
    },
    "Haplorrhini": {
    "habitat": "Varies depending on the species",
    "rang": "Suborder"
    },
    "Plathyrini": {
    "habitat": "Varies depending on the species",
    "rang": "Parvorder"
    },
    "Callitrichidae": {
    "habitat": "South and Central America",
    "rang": "Family"
    },
    "Tamarin": {
    "habitat": "Tropical rainforests of South and Central America",
    "rang": "Genus"
    },
    "Marmoset": {
    "habitat": "Tropical rainforests of South America",
    "rang": "Genus"
    },
    "Cebidae": {
    "habitat": "Central and South America",
    "rang": "Family"
    },
    "Squirrel": {
    "habitat": "Varies depending on the species",
    "rang": "Genus"
    },
    "Capuchin": {
    "habitat": "Tropical forests of Central and South America",
    "rang": "Genus"
    },
    "Atelidae": {
    "habitat": "Central and South America",
    "rang": "Family"
    },
    "Spider": {
    "habitat": "Varies depending on the species",
    "rang": "Genus"
    },
    "Howler": {
    "habitat": "Tropical forests of Central and South America",
    "rang": "Genus"
    },
    "Wooly": {
    "habitat": "Tropical forests of South America",
    "rang": "Genus"
    },
    "Muriquis": {
    "habitat": "Atlantic coastal forests of Brazil",
    "rang": "Genus"
    },
    "Pitheciidae": {
    "habitat": "Tropical forests of South America",
    "rang": "Family"
    },
    "Saki": {
    "habitat": "Tropical rainforests of South America",
    "rang": "Genus"
    },
    "Uakari": {
    "habitat": "Amazon rainforest and flooded forests of South America",
    "rang": "Genus"
    },
    "Titi": {
    "habitat": "Tropical forests of South America",
    "rang": "Genus"
    },
    "Aotidae": {
    "habitat": "Tropical rainforests of Central and South America",
    "rang": "Family"
    },
    "Night": {
    "habitat": "Tropical rainforests of Central and South America",
    "rang": "Genus"
    },
    "Catarhini": {
    "habitat": "Varies depending on the species",
    "rang": "Infraorder"
    },
    "Hominoidea": {
    "habitat": "Varies depending on the species",
    "rang": "Superfamily"
    },
    "Hominidae": {
    "habitat": "Varies depending on the species",
    "rang": "Family"
    },
    "Ponginae": {
    "habitat": "Southeast Asia",
    "rang": "Subfamily"
    },
    "Orangutan": {
    "habitat": "Rainforests of Borneo and Sumatra",
    "rang": "Genus"
    },
    "Homininae": {
    "habitat": "Varies depending on the species",
    "rang": "Subfamily"
    },
    "Gorillini": {
    "habitat": "Forests of Central and Western Africa",
    "rang": "Tribe"
    },
    "Gorilla": {
    "habitat": "Forests of Central and Western Africa",
    "rang": "Genus"
    },
    "Paninini": {
    "habitat": "Forests of Central and Western Africa",
    "rang": "Tribe"
    },
    "Chimp": {
    "habitat": "Forests of Central and Western Africa",
    "rang": "Genus"
    },
    "Bonobo": {
    "habitat": "Forests of the Democratic Republic of Congo",
    "rang": "Species"
    },
    "Hominini": {
    "habitat": "Varies depending on the species",
    "rang": "Tribe"
    },
    "Human": {
    "habitat": "Varies depending on the population",
    "rang": "Genus"
    },
    "Hylobatidae": {
    "habitat": "Southeast Asia",
    "rang": "Family"
    },
    "Gibbon": {
    "habitat": "Tropical rainforests of Southeast Asia",
    "rang": "Genus"
    },
    "Siamang": {
    "habitat": "Tropical rainforests of Southeast Asia",
    "rang": "Genus"
    },
    "Cercopithecoidea": {
    "habitat": "Varies depending on the species",
    "rang": "Superfamily"
    },
    "Cercopithecidae": {
    "habitat": "Varies depending on the species",
    "rang": "Family"
    },
    "Colobinae": {
    "habitat": "Varies depending on the species",
    "rang": "Subfamily"
    },
    "Cercopithecinae": {
    "habitat": "Varies depending on the species",
    "rang": "Subfamily"
    }
    }
""".trimIndent()

fun main(args: Array<String>) {
    val ontology = OntologyFactory.createOntology(ns = "http://evoflex.tech/monkey#", prefix = "monkey")

    val yaml = Yaml()
    val data: Map<String, Any> = yaml.load(YAML_HIERARCHY)

    add(ontology, data)

    val primate = ontology.getClass("Primate")
    val primatomorpha = primate!!.createSuperclass("Primatomorpha")
    val euarchonta = primatomorpha.createSuperclass("Euarchonta")
    val euarchontoglires = euarchonta.createSuperclass("Euarchontoglires")
    val boreoeutheria = euarchontoglires.createSuperclass("Boreoeutheria")
    val exafroplacentalia = boreoeutheria.createSuperclass("Exafroplacentalia")
    val placentalia = exafroplacentalia.createSuperclass("Placentalia")
    val eutheria = placentalia.createSuperclass("Eueria")
    val theria = eutheria.createSuperclass("Theria")
    val mammalia = theria.createSuperclass("Mammalia")
    val mammaliaformes = mammalia.createSuperclass("Mammaliaformes")
    val mammaliamorpha = mammaliaformes.createSuperclass("Mammaliamorpha")
    val prozostrodontia = mammaliamorpha.createSuperclass("Prozostrodontia")
    val probainognathia = prozostrodontia.createSuperclass("Probainognathia")
    val eucynodontia = probainognathia.createSuperclass("Eucynodontia")
    val epicynodontia = eucynodontia.createSuperclass("Epicynodontia")
    val cynodontia = epicynodontia.createSuperclass("Cynodontia")
    val eutheriodontia = cynodontia.createSuperclass("Eueriodontia")
    val theriodont = eutheriodontia.createSuperclass("Theriodont")
    val neotherapsida = theriodont.createSuperclass("Neotherapsida")
    val eutherapsida = neotherapsida.createSuperclass("Eutherapsida")
    val therapsida = eutherapsida.createSuperclass("Therapsida")
    val sphenacodontoidea = therapsida.createSuperclass("Spheniacodontoidea")
    val sphenacodontia = sphenacodontoidea.createSuperclass("Spheniacodontia")
    val eupelycosauria = sphenacodontia.createSuperclass("Eupelycosauria")
    val synapsida = eupelycosauria.createSuperclass("Synapsida")
    val amniota = synapsida.createSuperclass("Amniota")
    val reptiliomorpha = amniota.createSuperclass("Reptiliomorpha")
    val tetrapoda = reptiliomorpha.createSuperclass("Tetrapoda")
    val stegocephalia = tetrapoda.createSuperclass("Stegocephalia")
    val elpistostegalia = stegocephalia.createSuperclass("Epistostegalia")
    val eotetrapodiformes = elpistostegalia.createSuperclass("Eotetrapodiformes")
    val tetrapodomorpha = eotetrapodiformes.createSuperclass("Tetrapodomorpha")
    val sarcopterygii = tetrapodomorpha.createSuperclass("Sarcopterygii")
    val osteichthyes = sarcopterygii.createSuperclass("Osteichthyes")
    val chordata = osteichthyes.createSuperclass("Chordata")
    val deuterostomia = chordata.createSuperclass("Deuterostomia")
    val nephrozoa = deuterostomia.createSuperclass("Nephrozoa")
    val bilateria = nephrozoa.createSuperclass("Bilateria")
    val eumetazoa = bilateria.createSuperclass("Eumetazoa")
    val animalia = eumetazoa.createSuperclass("Animalia")
    val eukaryota = animalia.createSuperclass("Eukaryota")

    val habitats: Map<String, Map<String, String>> = Gson().fromJson(RANGS, Map::class.java) as Map<String, Map<String, String>>

    val habitat = ontology.createDatatypeProperty("habitat", animalia, OntologyResourceImpl(ontology as OntologyImpl, XSD.xstring))

    val rangOfClassification = ontology.createClass("RangOfClassification")
    habitats.values.forEach {
        it["rang"]?.let { rangOfClassification.createSubclass(it) }
    }

    val rang = ontology.createDatatypeProperty("rang", eukaryota, rangOfClassification)

    habitats.forEach { (t, u) ->
        val clazz = ontology.getClass(t);
        clazz?.setStringProperty(habitat, u["habitat"]!!)
        u["rang"]?.let { clazz?.setProperty(rang, ontology.getClass(it)!!) }
    }

    val outputFile = "monkeys.owl"
    FileWriter(outputFile).use {
        ontology.write(it)
    }
}

fun add(ontology: Ontology, data: Map<String, Any?>?, clazz: OntologyClass? = null) {
    if (data == null) {
        return
    }

    for ((key, value) in data.entries) {
        if (clazz == null) {
            val cl = ontology.createClass(key)
            add(ontology, data[key] as Map<String, Any?>?, cl)
        } else {
            val cl = clazz.createSubclass(key)
            add(ontology, data[key] as Map<String, Any?>?, cl)
        }
    }
}