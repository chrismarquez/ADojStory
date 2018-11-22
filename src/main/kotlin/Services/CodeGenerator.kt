package Services

import Interfaces.ICodeGenerator
import Models.*
import kotlin.math.exp

abstract class CodeGenerator : ICodeGenerator {

    private var fields: MutableList<String> = mutableListOf()
    private var variables: MutableList<String> = mutableListOf()
    private var methods: MutableList<String> = mutableListOf()

    private var statements: MutableList<String> = mutableListOf()

    override fun genField(key: String, value: String): ICodeGenerator {
        var newfield = "$key : $value,"
        this.fields.add(newfield)
        this.variables.add(key)
        return this
    }

    override fun genStatement(expression: Expression, varName: String?): ICodeGenerator {
        when (expression.type) {
            Type.ASSIGN_LOCAL -> {
                if (expression.data !is Assignment) throw Exception("Not correct data type")
                this.statements.add("${expression.data.name} = ${expression.data.value};")
            }
            Type.CREATE_VAR -> {
                if (expression.data !is Assignment) throw Exception("Not correct data type")
                this.statements.add("let ${expression.data.name};")
            }
            Type.ASSIGN_GLOBAL -> {
                if (expression.data !is Assignment) throw Exception("Not correct data type")
                this.statements.add("this.${expression.data.name} = ${expression.data.value};")
            }
            Type.USE_METHOD -> {
                if (expression.data !is MethodCall) throw Exception("Not correct data type")
                this.statements.add("${expression.data.name}();")
            }
            Type.USE_METHOD_PARAMS -> {
                if (expression.data !is MethodCall) throw Exception("Not correct data type")
                var the_args = ""
                for (arg in expression.data.args) {
                    the_args += "$arg, "
                }
                this.statements.add("${expression.data.name}($the_args);")
            }
            Type.ASSIGN_ARR -> {
                if (expression.data !is ArrayAssignment) throw Exception("Not correct data type")
                this.statements.add("${expression.data.name} = ${expression.data.value};")
            }
            Type.ASSIGN_ARR_NUM -> {
                if (expression.data !is ArrayAssignment) throw Exception("Not correct data type")
                this.statements.add("${expression.data.name}[${expression.data.index}] = ${expression.data.value};")
            }
            Type.INCREASE_VAR_BY_VAL -> {
                if (expression.data !is Mutation) throw Exception("Not correct data type")
                this.statements.add("${expression.data.name} += ${expression.data.value};")
            }
            Type.DECREASE_VAR_BY_VAL -> {
                if (expression.data !is Mutation) throw Exception("Not correct data type")
                this.statements.add("${expression.data.name} -= ${expression.data.value};")
            }
        }
        return this
    }

    override fun genMethod(name: String, args: List<String>): ICodeGenerator {
        var the_args = ""
        for (arg in args) {
            the_args += "$arg, "
        }

        var the_statements = ""
        for (statement in  this.statements){
            the_statements += " $statement "
        }

        this.methods.add("$name : ($the_args) => { $the_statements },")
        this.variables.add(name)
        this.statements.clear()
        return this
    }

    override fun buildObject(name: String): String {

        // Stringify fields
        var the_fields = ""
        for (field in this.fields) {
            the_fields += " $field "
        }
        this.fields.clear()

        // Stringify Methods
        var the_methods = ""
        for (method in this.methods) {
            the_methods += " $method "
        }
        this.methods.clear()

        var objectString = "let $name = { $the_fields $the_methods}"

        return  objectString
    }
}
