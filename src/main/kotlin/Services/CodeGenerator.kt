package Services

import Interfaces.ICodeGenerator
import Models.Expression

abstract class CodeGenerator : ICodeGenerator {

    private var fields: MutableList<String> = mutableListOf<String>()
    private var variables: MutableList<String> = mutableListOf<String>()
    private var methods: MutableList<String> = mutableListOf<String>()

    private var statements: MutableList<String> = mutableListOf<String>()

    override fun genField(key: String, value: String): ICodeGenerator {
        var newfield = "$key : $value,"
        this.fields.add(newfield)
        this.variables.add(key)
        return this
    }

    override fun genStatement(expression: Expression, varName: String?): ICodeGenerator {
        return this
    }

    override fun genMethod(name: String, args: List<String>): ICodeGenerator {
        var the_args = ""
        for (arg in args) {
            the_args += "$arg, "
        }

        var the_statements = ""
        for (statement in  this.statements){
            the_statements += "\t $statement \n"
        }

        this.methods.add("$name : ($the_args) => { \t $the_statements \n };")
        this.variables.add(name)
        return this
    }

    override fun buildObject(name: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}