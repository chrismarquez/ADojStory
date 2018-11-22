package Interfaces

import Models.Expression

interface ICodeGenerator {
    fun genField(key: String, value: String): ICodeGenerator
    fun genStatement(expression: Expression, varName: String? = null): ICodeGenerator
    fun genMethod(name: String, args: List<String>): ICodeGenerator
    fun buildObject(name: String): String
}