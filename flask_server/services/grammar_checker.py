import language_tool_python

tool = language_tool_python.LanguageTool("en-US")

def count_grammar_mistakes(text):
    matches = tool.check(text)
    return len(matches)
