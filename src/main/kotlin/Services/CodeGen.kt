package Services

import Interfaces.ICodeGenerator
import Models.Expression

class CodeGen() : ICodeGenerator {
    override fun genField(key: String, value: String): ICodeGenerator {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun genStatement(expression: Expression, varName: String?): ICodeGenerator {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun genMethod(name: String, args: List<String>): ICodeGenerator {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildObject(name: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}