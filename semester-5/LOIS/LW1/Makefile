################################################################################################
#                                                                                              #
#   Задача: реализовать прямой нечеткий логический вывод                                       #
#   Вариант: 21. Реализовать прямой нечеткий логический вывод, используя импликацию Гёделя     #
#   Авторы: Липский Р. В.   (121701)                                                           #
#           Жолнерчик И. А. (121701)                                                           #
#           Стронгин А. В.   (121701)                                                          #
#                                                                                              #
################################################################################################

ANTLR_VERSION=4.7.2
ANTLR_JAR_NAME=antlr-$(ANTLR_VERSION)-complete.jar
ANTLR_JAR_URL=https://www.antlr.org/download/$(ANTLR_JAR_NAME)
GRAMMAR_FILE=KB.g4
DIST_DIR=src/parser/antlr

.PHONY: all antlr clean

all: antlr

antlr: $(ANTLR_JAR_NAME) antlr4-python3-runtime
	@echo "<----     \033[34mGenerate lexer and parser\033[0m        ---->"
	@java -jar $(ANTLR_JAR_NAME) -Dlanguage=Python3 -o $(DIST_DIR) $(GRAMMAR_FILE)

$(ANTLR_JAR_NAME):
	@echo "<----    \033[34mDownload an antlr distribution\033[0m    ---->"
	@curl -O $(ANTLR_JAR_URL)

antlr4-python3-runtime: Pipfile.lock
	@echo "<---- \033[34mInstall dependencies from Pipfile\033[0m ---->"
	@pipenv install --ignore-pipfile

Pipfile.lock:
	@echo "<----     \033[34mCreate virtual environment\033[0m        ---->"
	@pipenv lock

clean:
	@echo "<----        \033[34mClean all the mess\033[0m            ---->"
	@rm -rf *.pyc __pycache__ $(DIST_DIR) $(ANTLR_JAR_NAME)
