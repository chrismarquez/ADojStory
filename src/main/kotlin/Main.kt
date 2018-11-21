import Services.Beautifier


fun main() {
    println("Hello World!")
    val js = Beautifier.beautify("var a=1;b=2;var user={name:\n\"Andrew\"};")
    println(js)
}