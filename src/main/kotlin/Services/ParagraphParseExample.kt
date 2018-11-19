package Services

import Interfaces.IParagraphParser

class ParagraphParseExample () : IParagraphParser{
    override fun parseParagraph(paragraph: String): String {
        return paragraph
    }
}