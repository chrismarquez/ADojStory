package Services

import Interfaces.ICodeGenerator
import Models.*

class CodeGenerator : ICodeGenerator {

    private var fields: MutableList<String> = mutableListOf()
    private var variables: MutableList<String> = mutableListOf()
    private var methods: MutableList<String> = mutableListOf()

    private var statements: MutableList<String> = mutableListOf()

    override fun genField(key: String, value: String): ICodeGenerator {
        val newfield = "$key : $value,"
        this.fields.add(newfield)
        this.variables.add(key)
        return this
    }

    override fun genStatement(expression: Expression, varName: String?): ICodeGenerator {
        when (expression.type) {
            Type.PRINT -> {
                if (expression.data !is Definition) throw Exception("Not correct data type")
                this.statements.add("console.log(${expression.data.name});")
            }
            Type.ASSIGN_LOCAL -> {
                if (expression.data !is Assignment) throw Exception("Not correct data type")
                this.statements.add("${expression.data.name} = ${expression.data.value};")
            }
            Type.CREATE_VAR -> {
                if (expression.data !is Assignment) throw Exception("Not correct data type")
                this.statements.add("let ${expression.data.name} = ${expression.data.value};")
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
                var stringArgs = ""
                for (i in 0 until expression.data.args.size) {
                    val arg = expression.data.args[i]
                    stringArgs += arg + if (i != expression.data.args.size - 1) ", " else ""
                }
                this.statements.add("${expression.data.name}($stringArgs);")
            }
            Type.ASSIGN_GLOBAL_ARR -> {
                if (expression.data !is MethodCall) throw Exception("Not correct data type")
                var stringArgs = ""
                for (i in 0 until expression.data.args.size) {
                    val arg = expression.data.args[i]
                    stringArgs += arg + if (i != expression.data.args.size - 1) ", " else ""
                }
                this.statements.add("this.${expression.data.name} = [$stringArgs];")
            }
            Type.ASSIGN_ARR -> {
                if (expression.data !is MethodCall) throw Exception("Not correct data type")
                var stringArgs = ""
                for (i in 0 until expression.data.args.size) {
                    val arg = expression.data.args[i]
                    stringArgs += arg + if (i != expression.data.args.size - 1) ", " else ""
                }
                this.statements.add("${expression.data.name} = [$stringArgs];")
            }
            Type.ASSIGN_GLOBAL_ARR_NUM -> {
                if (expression.data !is ArrayAssignment) throw Exception("Not correct data type")
                this.statements.add("this.${expression.data.name}[${expression.data.index}] = ${expression.data.value};")
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
            Type.INCREASE_GLOBAL_BY_VAL -> {
                if (expression.data !is Mutation) throw Exception("Not correct data type")
                this.statements.add("this.${expression.data.name} += ${expression.data.value};")
            }
            Type.DECREASE_GLOBAL_BY_VAL -> {
                if (expression.data !is Mutation) throw Exception("Not correct data type")
                this.statements.add("this.${expression.data.name} -= ${expression.data.value};")
            }
        }
        return this
    }

    override fun genMethod(name: String, args: List<String>): ICodeGenerator {
        var stringArgs = ""
        for (i in 0 until args.size) {
            val arg = args[i]
            stringArgs += arg + if (i != args.size - 1) ", " else ""
        }

        var statements = ""
        for (statement in  this.statements){
            statements += " $statement "
        }

        this.methods.add("$name : ($stringArgs) => { $statements }")
        this.variables.add(name)
        this.statements.clear()
        return this
    }

    override fun buildObject(name: String): String {

        // Stringify fields
        var stringFields = ""
        for (field in this.fields) {
            stringFields += " $field "
        }
        this.fields.clear()

        // Stringify Methods
        var stringMethods = ""
        for (i in 0 until this.methods.size) {
            val method = methods[i]
            stringMethods += " $method" + if (i != methods.size - 1) ", " else ""
        }
        this.methods.clear()

        val objectString = "let $name = { $stringFields $stringMethods};"

        return objectString
    }
}
