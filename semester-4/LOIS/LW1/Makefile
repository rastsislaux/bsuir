ANTLR_VERSION=4.7.2
ANTLR_JAR_NAME=antlr-$(ANTLR_VERSION)-complete.jar
ANTLR_JAR_URL=https://www.antlr.org/download/$(ANTLR_JAR_NAME)
GRAMMAR_FILE=dnf.g4

.PHONY: all antlr clean

all: antlr

antlr: $(ANTLR_JAR_NAME) antlr4-python3-runtime
	java -jar $(ANTLR_JAR_NAME) -Dlanguage=Python3 -o dist $(GRAMMAR_FILE)

$(ANTLR_JAR_NAME):
	curl -O $(ANTLR_JAR_URL)

antlr4-python3-runtime:
	pip3 install antlr4-python3-runtime

clean:
	rm -rf *.pyc __pycache__ $(ANTLR_JAR_NAME)
