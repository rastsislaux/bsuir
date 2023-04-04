ANTLR_VERSION=4.7.2
ANTLR_JAR_NAME=antlr-$(ANTLR_VERSION)-complete.jar
ANTLR_JAR_URL=https://www.antlr.org/download/$(ANTLR_JAR_NAME)
GRAMMAR_FILE=dnf.g4
DIST_DIR=dist

.PHONY: all antlr clean

all: antlr

antlr: $(ANTLR_JAR_NAME) antlr4-python3-runtime
	@echo "<----     \033[34mGenerate lexer and parser\033[0m        ---->"
	@java -jar $(ANTLR_JAR_NAME) -Dlanguage=Python3 -o $(DIST_DIR) $(GRAMMAR_FILE)

$(ANTLR_JAR_NAME):
	@echo "<----    \033[34mDownload an antlr distribution\033[0m    ---->"
	@curl -O $(ANTLR_JAR_URL)

antlr4-python3-runtime:
	@echo "<---- \033[34mInstall antlr python runtime library\033[0m ---->"
	@pip3 install antlr4-python3-runtime==${ANTLR_VERSION}

clean:
	@echo "<----        \033[34mClean all the mess\033[0m            ---->"
	@rm -rf *.pyc __pycache__ dist $(ANTLR_JAR_NAME)
