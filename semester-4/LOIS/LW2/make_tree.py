import os, sys

# Для использования в качестве первого аргумента командной строки введите путь к исходному коду на Прологе
# а в качестве второго --- путь к файлу с командой для нахождения ответа.
# Пример: python3 make_tree.py lw2.pl query.pl
# В файле main.pdf появится дерево вывода.

# Если что-то не запускается, попробуйте поставить эти настройки на true, чтобы устновить нужные пакеты
# Скрипт написан под Ubuntu 22.04 и запуск на других системах не гарантирован.
# Для работы также нужно, чтобы в системе был установлен SWI-Prolog

INSTALL_TEX = False
INSTALL_LIBRARY = False

# Увеличивайте этот показатель, если у вас текст накладывается.

GAP_MODIFIER = 15

# А тут уже ничего не трогайте ниже

GAP_DEPTH = str(GAP_MODIFIER * 30)
EDGE_LABEL_SEP = str(GAP_MODIFIER * 5)
GAP_WIDTH = str(GAP_MODIFIER * 30)

PREAMBLE = ":- use_module(library(sldnfdraw)).\n\
:- sldnf.\n\
:- begin_program."

PREQUERY = ":- end_program.\n\
:- begin_query.\n"

AFTERQUERY = ":- end_query.\n"

TEX = "\
\\documentclass{article}\n\
\\usepackage[paperwidth=\maxdimen,paperheight=\maxdimen]{geometry}\n\
\\usepackage{epic,eepic}\n\
\\usepackage{ecltree}\n\
\\setlength{\GapDepth}{" + GAP_DEPTH + "pt}\n\
\\setlength{\EdgeLabelSep}{" + EDGE_LABEL_SEP + "pt}\n\
\\setlength{\GapWidth}{" + GAP_WIDTH + "pt}\n\
\\begin{document}\n\
\\input{temp}\n\
\\end{document}"

def install_tex():
    os.system("sudo apt install -y texlive texlive-extra-utils texlive-humanities texlive-pictures pdf2svg")

def prolog(code: str, file: str = None):
    if file is None:
        os.system(f"swipl -g \"{code}, halt\"")
    else:
        os.system(f"swipl -s {file} -g \"{code}, halt\"")

def clean():
    os.system("rm temp.pl temp.tex main.tex main.log main.dvi main.aux")

if __name__ == "__main__":
    if INSTALL_TEX:
        install_tex()

    if INSTALL_LIBRARY:
        prolog("pack_install(sldnfdraw)")
    
    with open(sys.argv[1]) as source:
        text = source.read()

    with open(sys.argv[2]) as query_source:
        query = query_source.read()

    generated = f"{PREAMBLE}\n{text}\n{PREQUERY}\n{query}\n{AFTERQUERY}"
    
    with open("temp.pl", "w") as output:
        output.write(generated)

    prolog(file="temp.pl", code="draw_goal('temp.tex')")

    with open("main.tex", "w") as tex:
        tex.write(TEX)

    os.system("latex --interaction=nonstopmode main.tex")
    os.system("dvipdf main.dvi")

    clean()
