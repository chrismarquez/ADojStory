package Models

data class Expression(
    val type: Type,
    val data: Data
)

interface Data

data class Definition(
    val name: String
): Data

data class MethodCall(
    val name: String,
    val args: List<String>
): Data

data class Assignment(
    val name: String,
    val value: String
): Data

data class ArrayAssignment(
    val name: String,
    val index: Int,
    val value: String
): Data

data class Mutation(
    val name: String,
    val value: Int
): Data