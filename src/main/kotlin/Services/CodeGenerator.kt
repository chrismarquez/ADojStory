package Services

import Interfaces.ICodeGenerator
import Models.Expression

class CodeGenerator : ICodeGenerator {

    private var fields: MutableList<String> = mutableListOf<String>()
    private var variables: MutableList<String> = mutableListOf<String>()


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
        return this
    }

    override fun buildObject(name: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}